package ie.teamchile.smartapp.activities;

import android.content.ComponentCallbacks2;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import ie.teamchile.smartapp.R;
import ie.teamchile.smartapp.model.BaseModel;
import ie.teamchile.smartapp.model.Clinic;
import ie.teamchile.smartapp.model.PregnancyAction;
import ie.teamchile.smartapp.model.ServiceOption;
import ie.teamchile.smartapp.model.ServiceUserAction;
import ie.teamchile.smartapp.util.NotKeys;
import ie.teamchile.smartapp.util.SmartApi;
import ie.teamchile.smartapp.util.ToastAlert;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class QuickMenuActivity extends BaseActivity {
    private boolean isViewVisible;
    private Button btnPatientSearch, btnBookAppointment, btnClinicRecord, btnTodaysAppointments;
    private CountDownTimer timer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentForNav(R.layout.activity_quick_menu);

        Log.d("bugs", "quick menu in on create");

        btnPatientSearch = (Button) findViewById(R.id.btn_patient_search);
        btnPatientSearch.setOnClickListener(new ButtonClick());
        btnBookAppointment = (Button) findViewById(R.id.btn_book_appointment);
        btnBookAppointment.setOnClickListener(new ButtonClick());
        btnClinicRecord = (Button) findViewById(R.id.btn_clinic_record);
        btnClinicRecord.setOnClickListener(new ButtonClick());
        btnTodaysAppointments = (Button) findViewById(R.id.btn_todays_appointments);
        //btnTodaysAppointments.setOnClickListener(new ButtonClick());
        btnTodaysAppointments.setEnabled(false);
        isViewVisible = true;
        //checkIfLoggedIn();
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
    }

    @Override
    public void onBackPressed() {
        /*if (BaseModel.getInstance().getLoginStatus()) {
            showLogoutDialog();
        }*/
        moveTaskToBack(true);
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d("bugs", "quick menu in on resume");
        checkIfLoggedIn();
        isViewVisible = true;
        SharedPreferences.Editor prefs = getSharedPreferences("SMART", MODE_PRIVATE).edit();
        prefs.putBoolean("reuse", false);
        prefs.commit();

        if (BaseModel.getInstance().getLogin() == null) {
            Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
            startActivity(intent);
        }

        if(logServ != null){
            Log.d("logout", "thingALing resume = " + thingALing);
            Log.d("bugs", "logServ not null stopping timer");
            logServ.startTimer(false);
        }

        if (notificationManager != null) {
            notificationManager.cancelAll();
            Log.d("logout", "clear notifs");
        }
    }

    private void checkIfLoggedIn() {
        if (BaseModel.getInstance().getClinics().size() == 0 ||
                BaseModel.getInstance().getServiceOptions().size() == 0 ||
                BaseModel.getInstance().getAppointments().size() == 0) {
            Log.d("bugs", "quick menu no data available");
            updateData();
        } else {
            Log.d("bugs", "quick menu data available");
        }

        /*if (BaseModel.getInstance().getLoginStatus()) {
            Log.d("bugs", "logged in = " + BaseModel.getInstance().getLoginStatus());

        } else {
            Log.d("bugs", "logged in = " + BaseModel.getInstance().getLoginStatus());
            Intent login = new Intent(QuickMenuActivity.this, LoginActivity.class);
            startActivity(login);
        }*/
    }

    private void updateData() {
        done = 0;
        showProgressDialog(QuickMenuActivity.this, "Updating Information");

        updateAppointment(QuickMenuActivity.this);
        showDialog = false;

        api.getServiceProviderById(
                BaseModel.getInstance().getLogin().getId(),
                BaseModel.getInstance().getLogin().getToken(),
                NotKeys.API_KEY,
                new Callback<BaseModel>() {
                    @Override
                    public void success(BaseModel baseModel, Response response) {
                        Log.d("retro", "retro success");
                        BaseModel.getInstance().setServiceProvider(baseModel.getServiceProviders().get(0));
                        Log.d("Retrofit", "service provider finished");
                        done++;
                        Log.d("Retrofit", "done = " + done);
                    }

                    @Override
                    public void failure(RetrofitError error) {
                        Log.d("retro", "retro failure = " + error);
                    }
                }
        );

        api.getAllServiceOptions(
                BaseModel.getInstance().getLogin().getToken(),
                NotKeys.API_KEY,
                new Callback<BaseModel>() {
                    @Override
                    public void success(BaseModel baseModel, Response response) {
                        Map<Integer, ServiceOption> serviceOptionHomeMap = new HashMap<>();
                        List<ServiceOption> serviceOptionHomeList = new ArrayList<>();
                        Map<Integer, ServiceOption> serviceOptionClinicMap = new HashMap<>();
                        BaseModel.getInstance().setServiceOptions(baseModel.getServiceOptions());
                        for (int i = 0; i < baseModel.getServiceOptions().size(); i++) {
                            ServiceOption option = baseModel.getServiceOptions().get(i);
                            if (option.getHomeVisit()) {
                                serviceOptionHomeMap.put(option.getId(), option);
                                serviceOptionHomeList.add(option);
                            } else {
                                serviceOptionClinicMap.put(option.getId(), option);
                            }
                        }
                        BaseModel.getInstance().setServiceOptionsHomeList(serviceOptionHomeList);
                        BaseModel.getInstance().setServiceOptionsHomeMap(serviceOptionHomeMap);
                        BaseModel.getInstance().setServiceOptionsClinicMap(serviceOptionClinicMap);
                        Log.d("Retrofit", "service options finished");
                        done++;
                        Log.d("Retrofit", "done = " + done);
                    }

                    @Override
                    public void failure(RetrofitError error) {
                        Log.d("Retrofit", "service options retro failure " + error);
                    }
                }
        );
        api.getAllClinics(
                BaseModel.getInstance().getLogin().getToken(),
                NotKeys.API_KEY,
                new Callback<BaseModel>() {
                    @Override
                    public void success(BaseModel things, Response response) {
                        Map<Integer, Clinic> clinicMap = new HashMap<>();
                        BaseModel.getInstance().setClinics(things.getClinics());
                        for (int i = 0; i < things.getClinics().size(); i++) {
                            clinicMap.put(things.getClinics().get(i).getId(),
                                    things.getClinics().get(i));
                        }
                        BaseModel.getInstance().setClinicMap(clinicMap);
                        Log.d("Retrofit", "clinics finished");
                        done++;
                        Log.d("Retrofit", "done = " + done);
                    }

                    @Override
                    public void failure(RetrofitError error) {
                        Log.d("Retrofit", "clinics retro failure " + error);
                    }
                }
        );

        api.getServiceUserActions(
                BaseModel.getInstance().getLogin().getToken(),
                NotKeys.API_KEY,
                new Callback<BaseModel>() {
                    @Override
                    public void success(BaseModel baseModel, Response response) {
                        Log.d("Retrofit", "actions retro success");
                        done++;
                        Collections.sort(baseModel.getServiceUserActions(), new Comparator<ServiceUserAction>() {
                            @Override
                            public int compare(ServiceUserAction lhs, ServiceUserAction rhs) {
                                return (lhs.getShortCode()).compareTo(rhs.getShortCode());
                            }
                        });
                        BaseModel.getInstance().setServiceUserActions(baseModel.getServiceUserActions());
                    }

                    @Override
                    public void failure(RetrofitError error) {
                        Log.d("Retrofit", "actions retro failure " + error);
                    }
                }
        );

        timer = new CountDownTimer(200, 100) {
            @Override
            public void onTick(long millisUntilFinished) {
            }

            @Override
            public void onFinish() {
                if (done >= 5)
                    pd.dismiss();
                else
                    timer.start();
            }
        }.start();
    }

    private class ButtonClick implements View.OnClickListener {
        Intent intent;

        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.btn_patient_search:
                    intent = new Intent(QuickMenuActivity.this, ServiceUserSearchActivity.class);
                    break;
                case R.id.btn_book_appointment:
                    intent = new Intent(QuickMenuActivity.this, AppointmentTypeSpinnerActivity.class);
                    break;
                case R.id.btn_clinic_record:
                    intent = new Intent(QuickMenuActivity.this, ClinicTimeRecordActivity.class);
                    break;
                /*case R.id.btn_todays_appointments:
                    intent = new Intent(QuickMenuActivity.this, TodayAppointmentActivity.class);
                    break;*/
            }
            startActivity(intent);
        }
    }
}
