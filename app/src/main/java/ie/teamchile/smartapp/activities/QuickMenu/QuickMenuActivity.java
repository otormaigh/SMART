package ie.teamchile.smartapp.activities.QuickMenu;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;

import java.lang.ref.WeakReference;

import ie.teamchile.smartapp.R;
import ie.teamchile.smartapp.activities.AppointmentTypeSpinner.AppointmentTypeSpinnerActivity;
import ie.teamchile.smartapp.activities.Base.BaseActivity;
import ie.teamchile.smartapp.activities.ClinicTimeRecord.ClinicTimeRecordActivity;
import ie.teamchile.smartapp.activities.Login.LoginActivity;
import ie.teamchile.smartapp.activities.ServiceUserSearch.ServiceUserSearchActivity;
import ie.teamchile.smartapp.activities.TodayAppointmentActivity;
import ie.teamchile.smartapp.model.Clinic;
import ie.teamchile.smartapp.model.Login;
import ie.teamchile.smartapp.model.ServiceOption;
import ie.teamchile.smartapp.util.Constants;
import io.realm.Realm;

public class QuickMenuActivity extends BaseActivity implements QuickMenuView, OnClickListener {
    private Realm realm;
    private QuickMenuPresenter quickMenuPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentForNav(R.layout.activity_quick_menu);

        quickMenuPresenter = new QuickMenuPresenterImp(this, new WeakReference<Activity>(QuickMenuActivity.this));

        realm = quickMenuPresenter.getEncryptedRealm();

        initViews();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
    }

    @Override
    public void onBackPressed() {
        if (realm.where(Login.class).findFirst().isLoggedIn())
            showLogoutDialog();

        moveTaskToBack(true);
    }

    @Override
    protected void onResume() {
        super.onResume();
        checkIfLoggedIn();
        SharedPreferences.Editor prefs = getSharedPreferences(getString(R.string.app_name), MODE_PRIVATE).edit();
        prefs.putBoolean(Constants.REUSE, false);
        prefs.commit();

        if (realm.where(Login.class).findFirst() == null) {
            startActivity(new Intent(getApplicationContext(), LoginActivity.class));
        }

        if (logServ != null)
            logServ.startTimer(false);

        if (getNotificationManager() != null)
            getNotificationManager().cancelAll();
    }

    private void checkIfLoggedIn() {
        if (realm.where(Login.class).findFirst() == null
                && realm.where(Login.class).findFirst().isLoggedIn()
                || realm.where(Clinic.class).findAll().size() == 0
                || realm.where(ServiceOption.class).findAll().size() == 0) {
            quickMenuPresenter.updateData();
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_patient_search:
                startActivity(new Intent(getApplicationContext(), ServiceUserSearchActivity.class));
                break;
            case R.id.btn_book_appointment:
                startActivity(new Intent(getApplicationContext(), AppointmentTypeSpinnerActivity.class));
                break;
            case R.id.btn_clinic_record:
                startActivity(new Intent(getApplicationContext(), ClinicTimeRecordActivity.class));
                break;
            case R.id.btn_todays_appointments:
                startActivity(new Intent(getApplicationContext(), TodayAppointmentActivity.class));
                break;
        }
    }

    @Override
    public void initViews() {
        findViewById(R.id.btn_patient_search).setOnClickListener(this);
        findViewById(R.id.btn_book_appointment).setOnClickListener(this);
        findViewById(R.id.btn_clinic_record).setOnClickListener(this);
        findViewById(R.id.btn_todays_appointments).setOnClickListener(this);
        findViewById(R.id.btn_todays_appointments).setEnabled(false);
    }
}
