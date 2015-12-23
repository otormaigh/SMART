package ie.teamchile.smartapp.activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.Button;

import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;

import ie.teamchile.smartapp.R;
import ie.teamchile.smartapp.api.SmartApiClient;
import ie.teamchile.smartapp.model.BaseModel;
import ie.teamchile.smartapp.model.Clinic;
import ie.teamchile.smartapp.model.Login;
import ie.teamchile.smartapp.model.ServiceUserAction;
import ie.teamchile.smartapp.util.CustomDialogs;
import io.realm.Realm;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;
import timber.log.Timber;

public class QuickMenuActivity extends BaseActivity {
    private boolean isViewVisible;
    private Button btnPatientSearch, btnBookAppointment, btnClinicRecord, btnTodaysAppointments;
    private CountDownTimer timer;
    private ProgressDialog pd;
    private Realm realm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentForNav(R.layout.activity_quick_menu);

        realm = Realm.getInstance(this);

        btnPatientSearch = (Button) findViewById(R.id.btn_patient_search);
        btnPatientSearch.setOnClickListener(new ButtonClick());
        btnBookAppointment = (Button) findViewById(R.id.btn_book_appointment);
        btnBookAppointment.setOnClickListener(new ButtonClick());
        btnClinicRecord = (Button) findViewById(R.id.btn_clinic_record);
        btnClinicRecord.setOnClickListener(new ButtonClick());
        btnTodaysAppointments = (Button) findViewById(R.id.btn_todays_appointments);
        btnTodaysAppointments.setOnClickListener(new ButtonClick());
        btnTodaysAppointments.setEnabled(false);
        isViewVisible = true;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        if (realm != null)
            realm.close();
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
        checkIfLoggedIn();
        isViewVisible = true;
        SharedPreferences.Editor prefs = getSharedPreferences("SMART", MODE_PRIVATE).edit();
        prefs.putBoolean("reuse", false);
        prefs.commit();

        if (realm.where(Login.class).findFirst() == null) {
            startActivity(new Intent(getApplicationContext(), LoginActivity.class));
        }

        if(logServ != null){
            logServ.startTimer(false);
        }

        if (notificationManager != null) {
            notificationManager.cancelAll();
        }
    }

    private void checkIfLoggedIn() {
        if (BaseModel.getInstance().getClinics().size() == 0 ||
                BaseModel.getInstance().getServiceOptions().size() == 0) {
            updateData();
        }
    }

    private void updateData() {
        done = 0;
        pd = new CustomDialogs().showProgressDialog(QuickMenuActivity.this, "Updating Information");

        showDialog = false;

        SmartApiClient.getAuthorizedApiClient(this).getServiceProviderById(
                realm.where(Login.class).findFirst().getId(),
                new Callback<BaseModel>() {
                    @Override
                    public void success(BaseModel baseModel, Response response) {
                        Timber.d("serviceProvider success");

                        realm.beginTransaction();
                        realm.copyToRealmOrUpdate(baseModel.getServiceProviders().get(0));
                        realm.commitTransaction();

                        done++;
                        Timber.d("done = " + done);
                    }

                    @Override
                    public void failure(RetrofitError error) {
                        Timber.d("serviceProvider failure = " + error);
                        done++;
                    }
                }
        );

        SmartApiClient.getAuthorizedApiClient(this).getAllServiceOptions(
                new Callback<BaseModel>() {
                    @Override
                    public void success(BaseModel baseModel, Response response) {
                        realm.beginTransaction();
                        realm.copyToRealmOrUpdate(baseModel.getServiceOptions());
                        realm.commitTransaction();

                        BaseModel.getInstance().setServiceOptions(baseModel.getServiceOptions());
                        Timber.d("service options finished");
                        done++;
                        Timber.d("done = " + done);
                    }

                    @Override
                    public void failure(RetrofitError error) {
                        Timber.d("service options retro failure " + error);
                        done++;
                    }
                }
        );
        SmartApiClient.getAuthorizedApiClient(this).getAllClinics(
                new Callback<BaseModel>() {
                    @Override
                    public void success(BaseModel things, Response response) {
                        realm.beginTransaction();
                        realm.copyToRealmOrUpdate(things.getClinics());
                        realm.commitTransaction();

                        Map<Integer, Clinic> clinicMap = new HashMap<>();
                        BaseModel.getInstance().setClinics(things.getClinics());
                        for (int i = 0; i < things.getClinics().size(); i++) {
                            clinicMap.put(things.getClinics().get(i).getId(),
                                    things.getClinics().get(i));
                        }
                        BaseModel.getInstance().setClinicMap(clinicMap);
                        Timber.d("clinics finished");
                        done++;
                        Timber.d("done = " + done);
                    }

                    @Override
                    public void failure(RetrofitError error) {
                        Timber.d("clinics retro failure " + error);
                        done++;
                    }
                }
        );

        SmartApiClient.getAuthorizedApiClient(this).getServiceUserActions(
                new Callback<BaseModel>() {
                    @Override
                    public void success(BaseModel baseModel, Response response) {
                        Timber.d("actions retro success");
                        realm.beginTransaction();
                        realm.copyToRealmOrUpdate(baseModel.getServiceUserActions());
                        realm.commitTransaction();

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
                        Timber.d("serviceActions retro failure " + error);
                        done++;
                    }
                }
        );

        timer = new CountDownTimer(200, 100) {
            @Override
            public void onTick(long millisUntilFinished) {
            }

            @Override
            public void onFinish() {
                if (done >= 4)
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
                case R.id.btn_todays_appointments:
                    intent = new Intent(QuickMenuActivity.this, TodayAppointmentActivity.class);
                    break;
            }
            startActivity(intent);
        }
    }
}
