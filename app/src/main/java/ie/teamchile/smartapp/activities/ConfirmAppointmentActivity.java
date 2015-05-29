package ie.teamchile.smartapp.activities;

import ie.teamchile.smartapp.R;
import ie.teamchile.smartapp.retrofit.ApiRootModel;
import ie.teamchile.smartapp.retrofit.Appointment;
import ie.teamchile.smartapp.retrofit.PostingData;
import ie.teamchile.smartapp.retrofit.SmartApi;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class ConfirmAppointmentActivity extends MenuInheritActivity {
	private DateFormat sdfDateMonthName = new SimpleDateFormat("dd MMM yyyy", Locale.getDefault());
	private DateFormat sdfDate = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
    private TextView txtUserName, txtClinic, txtDateTime, 
    				 txtEmailTo, txtSmsTo;
    private Button btnYes, btnNo;
    private String name, hospitalNumber, clinicName, date, monthDate, time,
    		priority, visitType, timeBefore, timeAfter, email, sms;
    private int userId, clinicID;
    private Calendar cal = Calendar.getInstance();
    private String returnType;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentForNav(R.layout.activity_confirm_appointment);
        
        txtUserName = (TextView) findViewById(R.id.text_confirm_user);
        txtClinic = (TextView) findViewById(R.id.text_confirm_appt_location);
        txtDateTime = (TextView) findViewById(R.id.text_confirm_appt_time);
        txtEmailTo = (TextView) findViewById(R.id.text_confirm_email);
        txtSmsTo = (TextView) findViewById(R.id.text_confirm_sms);

        btnYes = (Button) findViewById(R.id.btn_yes_appointment);
        btnYes.setOnClickListener(new ButtonClick());
        btnNo = (Button) findViewById(R.id.btn_no_appointment);
        btnNo.setOnClickListener(new ButtonClick());
        
        clinicName = getIntent().getStringExtra("clinicName");
        clinicID = Integer.parseInt(getIntent().getStringExtra("clinicID"));
        date = getIntent().getStringExtra("date");
        try {
        	cal.setTime(sdfDate.parse(date));
        	monthDate = sdfDateMonthName.format(sdfDate.parse(date));
		} catch (ParseException e) {
			e.printStackTrace();
		}
        time = getIntent().getStringExtra("time");
        returnType = getIntent().getStringExtra("return_type");
        priority = getIntent().getStringExtra("priority");
        visitType = getIntent().getStringExtra("visitType");
        timeBefore = getIntent().getStringExtra("timeBefore");
        timeAfter = getIntent().getStringExtra("timeAfter"); 
        userId = Integer.parseInt(getIntent().getStringExtra("userId"));
        visitType = getIntent().getStringExtra("visitType"); 
        
        Log.d("appointment", "userId: " + userId + "\nclinicName: " + clinicName + 
        		"\nclinicID: " +  clinicID  + "\nDate: " + monthDate + 
				"\nTime: " + time + "\nReturn Type: " + returnType +
				"\nPriority: " + priority + "\nVisit Type: " + visitType);
        
        name = ApiRootModel.getInstance().getServiceUsers().get(0).getPersonalFields().getName();
        hospitalNumber = ApiRootModel.getInstance().getServiceUsers().get(0).getHospitalNumber();
        email = ApiRootModel.getInstance().getServiceUsers().get(0).getPersonalFields().getEmail();
        sms = ApiRootModel.getInstance().getServiceUsers().get(0).getPersonalFields().getMobilePhone();
        txtUserName.setText(name + " (" + hospitalNumber + ")");
        //txtHospitalNumber.setText(hospitalNumber);
        txtClinic.setText(clinicName);
        txtDateTime.setText(time + " on " + monthDate);
        //txtTime.setText(time);
        txtEmailTo.setText(email);
        txtSmsTo.setText(sms);
    }

	private class ButtonClick implements View.OnClickListener {
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.btn_yes_appointment:
                    Log.d("bugs", "yes 	 button clicked");
                    showProgressDialog(ConfirmAppointmentActivity.this,
                            "Booking Appointment");
                    postAppointment();
                	//new CreateAppointmentLongOperation(ConfirmAppointmentActivity.this).execute("appointments");
                    //passOptions.setDaySelected(cal.getTime());
                    break;
                case R.id.btn_no_appointment:
                	Log.d("bugs", "no button clicked");
                	Intent intent = new Intent(ConfirmAppointmentActivity.this, CreateAppointmentActivity.class);
                	intent.putExtra("from", "confirm");
                	intent.putExtra("clinicName", clinicName);
            		intent.putExtra("clinicID", clinicID);
            		intent.putExtra("date", date);
            		intent.putExtra("time", time);
            		intent.putExtra("return_type", returnType);
            		intent.putExtra("priority", priority);
            		intent.putExtra("timeBefore", timeBefore);
            		intent.putExtra("timeAfter", timeAfter);
            		intent.putExtra("userId", userId);
            		intent.putExtra("userName", name);
            		startActivity(intent);
                    break;
            }
        }
    }

    private void postAppointment(){
        PostingData appointment = new PostingData();
        appointment.postAppointment(date, (time + ":00"), userId, clinicID, priority, visitType, returnType);

        api.postAppointment(
                appointment,
                ApiRootModel.getInstance().getLogin().getToken(),
                SmartApi.API_KEY,
                new Callback<ApiRootModel>() {
                    @Override
                    public void success(ApiRootModel apiRootModel, Response response) {
                        //response.getBody();
                        ApiRootModel.getInstance().addAppointment(apiRootModel.getAppointment());
                        Intent intent = new Intent(ConfirmAppointmentActivity.this, AppointmentCalendarActivity.class);
                        pd.dismiss();
                        setApptToMaps();
                        startActivity(intent);
                        //updateAppointments(apptId);
                    }

                    @Override
                    public void failure(RetrofitError error) {
                        Log.d("Retrofit", "retro failure error = " + error.getBody());
                        pd.dismiss();
                    }
                }
        );
    }

    private void setApptToMaps(){
        List<Integer> apptIdList;
        Map<String, List<Integer>> dateApptIdMap;
        Map<Integer, Map<String, List<Integer>>> clinicDateApptIdMap = new HashMap<>();
        Map<Integer, Appointment> idApptMap = new HashMap<>();

        for(int i = 0; i < ApiRootModel.getInstance().getAppointments().size(); i++){
            apptIdList = new ArrayList<>();
            dateApptIdMap = new HashMap<>();
            String apptDate = ApiRootModel.getInstance().getAppointments().get(i).getDate();
            int apptId = ApiRootModel.getInstance().getAppointments().get(i).getId();
            int clinicId = ApiRootModel.getInstance().getAppointments().get(i).getClinicId();
            Appointment appt = ApiRootModel.getInstance().getAppointments().get(i);

            if(clinicDateApptIdMap.get(clinicId) != null){
                dateApptIdMap = clinicDateApptIdMap.get(clinicId);
                if(dateApptIdMap.get(apptDate) != null){
                    apptIdList = dateApptIdMap.get(apptDate);
                }
            }
            apptIdList.add(apptId);
            dateApptIdMap.put(apptDate, apptIdList);

            clinicDateApptIdMap.put(clinicId, dateApptIdMap);
            idApptMap.put(apptId, appt);
        }
        ApiRootModel.getInstance().setClinicDateApptIdMap(clinicDateApptIdMap);
        ApiRootModel.getInstance().setIdApptMap(idApptMap);
        Log.d("Retrofit", "appointments finished");
        pd.dismiss();
    }

    private void updateAppointments(int apptId) {
        api.getAppointmentById(
                apptId,
                ApiRootModel.getInstance().getLogin().getToken(),
                SmartApi.API_KEY,
                new Callback<ApiRootModel>() {
                    @Override
                    public void success(ApiRootModel apiRootModel, Response response) {
                        ApiRootModel.getInstance().addAppointment(apiRootModel.getAppointments().get(0));
                        Log.d("Retro", "retro success");
                    }

                    @Override
                    public void failure(RetrofitError error) {
                        Log.d("Retro", "retro railure error = " + error);
                    }
                }
        );
    }

    /*private void updateAppointments(final Intent intent){
        showProgressDialog(ConfirmAppointmentActivity.this,
                "Updating Appointments");
        api.getAllAppointments(
                ApiRootModel.getInstance().getLogin().getToken(),
                CredentialsEnum.API_KEY.toString(),
                new Callback<ApiRootModel>() {
                    @Override
                    public void success(ApiRootModel apiRootModel, Response response) {
                        ApiRootModel.getInstance().setAppointments(apiRootModel.getAppointments());
                        List<Integer> apptIdList;
                        Map<String, List<Integer>> dateApptIdMap;
                        Map<Integer, Map<String, List<Integer>>> clinicDateApptIdMap = new HashMap<>();
                        Map<Integer, Appointment> idApptMap = new HashMap<>();

                        for(int i = 0; i < apiRootModel.getAppointments().size(); i++){
                            apptIdList = new ArrayList<>();
                            dateApptIdMap = new HashMap<>();
                            String apptDate = apiRootModel.getAppointments().get(i).getDate();
                            int apptId = apiRootModel.getAppointments().get(i).getId();
                            int clinicId = apiRootModel.getAppointments().get(i).getClinicId();
                            Appointment appt = apiRootModel.getAppointments().get(i);

                            if(clinicDateApptIdMap.get(clinicId) != null){
                                dateApptIdMap = clinicDateApptIdMap.get(clinicId);
                                if(dateApptIdMap.get(apptDate) != null){
                                    apptIdList = dateApptIdMap.get(apptDate);
                                }
                            }
                            apptIdList.add(apptId);
                            dateApptIdMap.put(apptDate, apptIdList);

                            clinicDateApptIdMap.put(clinicId, dateApptIdMap);
                            idApptMap.put(apptId, appt);
                        }
                        ApiRootModel.getInstance().setClinicDateApptIdMap(clinicDateApptIdMap);
                        ApiRootModel.getInstance().setIdApptMap(idApptMap);
                        Log.d("Retrofit", "appointments finished");
                        pd.dismiss();
                        startActivity(intent);
                    }

                    @Override
                    public void failure(RetrofitError error) {
                        Log.d("Retrofit", "appointments retro failure " + error);
                        pd.dismiss();
                    }
                }
        );
    }*/
}