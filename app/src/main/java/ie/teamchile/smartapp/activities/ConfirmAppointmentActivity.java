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
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class ConfirmAppointmentActivity extends BaseActivity {
    private TextView txtUserName, txtClinic, txtDateTime,
            txtEmailTo, txtSmsTo;
    private Button btnYes, btnNo;
    private String name, hospitalNumber, clinicName, date, monthDate, time,
            priority, visitType, timeBefore, timeAfter, email, sms;
    private int userId, serviceOptionId;
    private Calendar cal = Calendar.getInstance();
    private String returnType;
    private List<Integer> serviceOptionIdList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentForNav(R.layout.dialog_confirm_appointment);

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
        serviceOptionId = Integer.parseInt(getIntent().getStringExtra("serviceOptionId"));
        name = getIntent().getStringExtra("userName");
        hospitalNumber = getIntent().getStringExtra("hospitalNumber");
        email = getIntent().getStringExtra("email");
        sms = getIntent().getStringExtra("sms");

        txtUserName.setText(name + " (" + hospitalNumber + ")");
        //txtHospitalNumber.setText(hospitalNumber);
        txtClinic.setText(clinicName);
        txtDateTime.setText(time + " on " + monthDate);
        //txtTime.setText(time);
        txtEmailTo.setText(email);
        txtSmsTo.setText(sms);

        Log.d("bugs", "userId = " + userId);
    }

    private void postAppointment() {
        PostingData appointment = new PostingData();
        if (priority.equals("home-visit")) {
            Log.d("bugs", "homevisit");
            appointment.postAppointment(date, userId, priority, visitType, returnType, serviceOptionId);
        } else if (priority.equals("scheduled")) {
            Log.d("bugs", "scheduled");
            int clinicID = Integer.parseInt(getIntent().getStringExtra("clinicID"));
            appointment.postAppointment(date, time, userId, clinicID, priority, visitType, returnType);
        }

        RestAdapter restAdapter = new RestAdapter.Builder()
                .setEndpoint(SmartApi.BASE_URL)
                .setLogLevel(RestAdapter.LogLevel.FULL)
                .build();

        api = restAdapter.create(SmartApi.class);

        api.postAppointment(
                appointment,
                ApiRootModel.getInstance().getLogin().getToken(),
                SmartApi.API_KEY,
                new Callback<ApiRootModel>() {
                    @Override
                    public void success(ApiRootModel apiRootModel, Response response) {
                        ApiRootModel.getInstance().addAppointment(apiRootModel.getAppointment());
                        Intent intentClinic = new Intent(ConfirmAppointmentActivity.this, AppointmentCalendarActivity.class);
                        Intent intentHome = new Intent(ConfirmAppointmentActivity.this, HomeVisitAppointmentActivity.class);
                        pd.dismiss();
                        addNewApptToMaps();

                        if (priority.equals("home-visit"))
                            startActivity(intentHome);
                        else if (priority.equals("scheduled"))
                            startActivity(intentClinic);
                    }

                    @Override
                    public void failure(RetrofitError error) {
                        Log.d("Retrofit", "retro failure error = " + error);
                        pd.dismiss();
                    }
                }
        );
    }

    private void addNewApptToMaps() {
        List<Integer> clinicApptIdList;
        Map<String, List<Integer>> clinicVisitdateApptIdMap;
        Map<Integer, Map<String, List<Integer>>> clinicVisitClinicDateApptIdMap = new HashMap<>();
        Map<Integer, Appointment> clinicVisitIdApptMap = new HashMap<>();

        List<Integer> homeApptIdList;
        Map<String, List<Integer>> homeVisitdateApptIdMap;
        Map<Integer, Map<String, List<Integer>>> homeVisitClinicDateApptIdMap = new HashMap<>();
        Map<Integer, Appointment> homeVisitIdApptMap = new HashMap<>();
        for (int i = 0; i < ApiRootModel.getInstance().getAppointments().size(); i++) {
            clinicApptIdList = new ArrayList<>();
            homeApptIdList = new ArrayList<>();
            clinicVisitdateApptIdMap = new HashMap<>();
            homeVisitdateApptIdMap = new HashMap<>();
            Appointment appt = ApiRootModel.getInstance().getAppointments().get(i);
            String apptDate = appt.getDate();
            int apptId = appt.getId();
            int clinicId = appt.getClinicId();
            int serviceOptionId = 0;
            if (appt.getServiceOptionIds().size() > 0) {
                serviceOptionId = appt.getServiceOptionIds().get(0);
            }

            if (appt.getPriority().equals("home-visit")) {
                Log.d("bugs", " appt ID = " + appt.getId());
                if (homeVisitClinicDateApptIdMap.get(serviceOptionId) != null) {
                    homeVisitdateApptIdMap = homeVisitClinicDateApptIdMap.get(serviceOptionId);
                    if (homeVisitdateApptIdMap.get(apptDate) != null) {
                        homeApptIdList = homeVisitdateApptIdMap.get(apptDate);
                    }
                }
                homeApptIdList.add(apptId);
                homeVisitdateApptIdMap.put(apptDate, homeApptIdList);

                homeVisitClinicDateApptIdMap.put(serviceOptionId, homeVisitdateApptIdMap);
                homeVisitIdApptMap.put(apptId, appt);
            } else {
                if (clinicVisitClinicDateApptIdMap.get(clinicId) != null) {
                    clinicVisitdateApptIdMap = clinicVisitClinicDateApptIdMap.get(clinicId);
                    if (clinicVisitdateApptIdMap.get(apptDate) != null) {
                        clinicApptIdList = clinicVisitdateApptIdMap.get(apptDate);
                    }
                }
                clinicApptIdList.add(apptId);
                clinicVisitdateApptIdMap.put(apptDate, clinicApptIdList);

                clinicVisitClinicDateApptIdMap.put(clinicId, clinicVisitdateApptIdMap);
                clinicVisitIdApptMap.put(apptId, appt);
            }
        }
        ApiRootModel.getInstance().setClinicVisitClinicDateApptIdMap(clinicVisitClinicDateApptIdMap);
        ApiRootModel.getInstance().setClinicVisitIdApptMap(clinicVisitIdApptMap);

        ApiRootModel.getInstance().setHomeVisitOptionDateApptIdMap(homeVisitClinicDateApptIdMap);
        ApiRootModel.getInstance().setHomeVisitIdApptMap(homeVisitIdApptMap);
        Log.d("Retrofit", "appointments finished");
        pd.dismiss();
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
                    Intent intentHome = new Intent(ConfirmAppointmentActivity.this, HomeVisitAppointmentActivity.class);
                    Intent intentClinic = new Intent(ConfirmAppointmentActivity.this, AppointmentCalendarActivity.class);
                    if (priority.equals("home-visit"))
                        startActivity(intentHome);
                    else if (priority.equals("scheduled"))
                        startActivity(intentClinic);
                    /*int clinicID = Integer.parseInt(getIntent().getStringExtra("clinicID"));
                    intent.putExtra("from", "confirm");
                    intent.putExtra("clinicName", clinicName);
                    intent.putExtra("clinicID", String.valueOf(clinicID));
                    intent.putExtra("date", date);
                    intent.putExtra("time", time);
                    intent.putExtra("return_type", returnType);
                    intent.putExtra("priority", priority);
                    intent.putExtra("timeBefore", timeBefore);
                    intent.putExtra("timeAfter", timeAfter);
                    intent.putExtra("userId", String.valueOf(userId));
                    intent.putExtra("userName", name);
                    intent.putExtra("serviceOptionId", String.valueOf(serviceOptionId));*/
                    break;
            }
        }
    }
}