package ie.teamchile.smartapp.activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import ie.teamchile.smartapp.R;
import ie.teamchile.smartapp.model.ApiRootModel;
import ie.teamchile.smartapp.model.Appointment;
import ie.teamchile.smartapp.model.PostingData;
import ie.teamchile.smartapp.util.SmartApi;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class ConfirmAppointmentActivity extends BaseActivity {
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
        
        txtUserName = (TextView) findViewById(R.id.tv_confirm_name);
        txtClinic = (TextView) findViewById(R.id.tv_confirm_location);
        txtDateTime = (TextView) findViewById(R.id.tv_confirm_time);
        txtEmailTo = (TextView) findViewById(R.id.tv_confirm_email);
        txtSmsTo = (TextView) findViewById(R.id.tv_confirm_sms);

        btnYes = (Button) findViewById(R.id.btn_confirm_yes);
        btnYes.setOnClickListener(new ButtonClick());
        btnNo = (Button) findViewById(R.id.btn_confirm_no);
        btnNo.setOnClickListener(new ButtonClick());
        
        clinicName = getIntent().getStringExtra("clinicName");
        clinicID = Integer.parseInt(getIntent().getStringExtra("clinicID"));
        date = getIntent().getStringExtra("date");
        try {
        	cal.setTime(dfDateOnly.parse(date));
        	monthDate = dfDateMonthNameYear.format(dfDateOnly.parse(date));
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
                case R.id.btn_confirm_yes:
                    Log.d("bugs", "yes 	 button clicked");
                    showProgressDialog(ConfirmAppointmentActivity.this,
                            "Booking Appointment");
                    postAppointment();
                	//new CreateAppointmentLongOperation(ConfirmAppointmentActivity.this).execute("appointments");
                    //passOptions.setDaySelected(cal.getTime());
                    break;
                case R.id.btn_confirm_no:
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
        appointment.postAppointment(date, time, userId, clinicID, priority, visitType, returnType);

        api.postAppointment(
                appointment,
                ApiRootModel.getInstance().getLogin().getToken(),
                SmartApi.API_KEY,
                new Callback<ApiRootModel>() {
                    @Override
                    public void success(ApiRootModel apiRootModel, Response response) {
                        ApiRootModel.getInstance().addAppointment(apiRootModel.getAppointment());
                        Intent intent = new Intent(ConfirmAppointmentActivity.this, AppointmentCalendarActivity.class);
                        pd.dismiss();
                        addNewApptToMaps();
                        startActivity(intent);
                    }

                    @Override
                    public void failure(RetrofitError error) {
                        Log.d("Retrofit", "retro failure error = " + error);
                        pd.dismiss();
                    }
                }
        );
    }

    private void addNewApptToMaps(){
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
}