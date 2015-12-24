package ie.teamchile.smartapp.activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

import net.hockeyapp.android.CrashManager;
import net.hockeyapp.android.Tracking;
import net.hockeyapp.android.UpdateManager;

import java.util.Calendar;
import java.util.Map;

import ie.teamchile.smartapp.BuildConfig;
import ie.teamchile.smartapp.R;
import ie.teamchile.smartapp.api.SmartApiClient;
import ie.teamchile.smartapp.model.BaseModel;
import ie.teamchile.smartapp.model.Login;
import ie.teamchile.smartapp.model.PostingData;
import ie.teamchile.smartapp.util.Constants;
import ie.teamchile.smartapp.util.CustomDialogs;
import ie.teamchile.smartapp.util.NotKeys;
import ie.teamchile.smartapp.util.SharedPrefs;
import io.realm.Realm;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;
import timber.log.Timber;

public class LoginActivity extends AppCompatActivity implements OnClickListener {
    private SharedPrefs prefsUtil = new SharedPrefs();
    private String username, password;
    private TextView tvUsername;
    private TextView tvPassword;
    private ProgressDialog pd;
    private Realm realm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_SECURE, WindowManager.LayoutParams.FLAG_SECURE);
        setContentView(R.layout.activity_login);

        realm = Realm.getInstance(getApplicationContext());

        findViewById(R.id.btn_login).setOnClickListener(this);
        findViewById(R.id.tv_about).setOnClickListener(this);
        tvUsername = (TextView) findViewById(R.id.et_username);
        tvPassword = (TextView) findViewById(R.id.et_password);

        if(BuildConfig.DEBUG) {
            tvUsername.setText(NotKeys.USERNAME);
            tvPassword.setText(NotKeys.PASSWORD);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        if (realm != null)
            realm.close();
    }

    @Override
    protected void onPause() {
        super.onPause();
        //Unregister HockeyApp
        UpdateManager.unregister();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if(!BuildConfig.DEBUG)
            initHockeyApp();

        BaseModel.getInstance().deleteInstance();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        BaseModel.getInstance().deleteInstance();

        realm.beginTransaction();
        realm.where(Login.class).findFirst().setLoggedIn(false);
        realm.commitTransaction();

        finish();
    }

    private void initHockeyApp() {
        CrashManager.register(getApplicationContext(), NotKeys.APP_ID);
    }

    private void getSharedPrefs() {
        Timber.d("getSharedPrefs called");
        SharedPreferences prefs = getApplicationContext().getSharedPreferences("SMART", MODE_PRIVATE);
        Map<String, ?> prefsMap = prefs.getAll();
        for (Map.Entry<String, ?> entry : prefsMap.entrySet()) {
            Timber.d("key = " + entry.getKey());
            if (entry.getKey().contains("appointment_post")) {
                Timber.d("get key = " + prefs.getString(entry.getKey(), ""));
                prefsUtil.postAppointment(
                        prefsUtil.getObjectFromString(
                                prefs.getString(entry.getKey(), "")),
                        getApplicationContext(), entry.getKey());
            }
        }
    }

    private void postLogin() {
        final PostingData login = new PostingData();
        login.postLogin(username, password);

        SmartApiClient.getUnAuthorizedApiClient().postLogin(login, new Callback<BaseModel>() {
            @Override
            public void success(BaseModel baseModel, Response response) {
                Timber.d("postLogin success");
                Tracking.startUsage(LoginActivity.this);
                prefsUtil.setLongPrefs(getApplicationContext(),
                        Calendar.getInstance().getTimeInMillis(),
                        Constants.SHARED_PREFS_SPLASH_LOG);
                prefsUtil.deletePrefs(getApplicationContext(), "appts_got");

                realm.beginTransaction();
                baseModel.getLogin().setLoggedIn(true);
                realm.copyToRealmOrUpdate(baseModel.getLogin());
                realm.commitTransaction();

                pd.dismiss();
                getSharedPrefs();
                startActivity(new Intent(getApplicationContext(), QuickMenuActivity.class));
            }

            @Override
            public void failure(RetrofitError error) {
                if (error.getResponse() != null) {
                    BaseModel body = (BaseModel) error.getBodyAs(BaseModel.class);
                    Timber.d("retro error = " + body.getError().getError());
                    Toast.makeText(getApplicationContext(), body.getError().getError(), Toast.LENGTH_LONG).show();
                }
                pd.dismiss();
            }
        });
    }

    private void validateInput() {
        username = tvUsername.getText().toString();
        password = tvPassword.getText().toString();

        if (!TextUtils.isEmpty(username) &&
                !TextUtils.isEmpty(password)) {
            postLogin();
            pd = new CustomDialogs().showProgressDialog(
                    LoginActivity.this,
                    "Logging In");
        } else {
            new CustomDialogs().showErrorDialog(
                    LoginActivity.this,
                    "Error fields empty");
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_login:
                Tracking.startUsage(LoginActivity.this);
                Timber.d("hockeyApp Login pressed");
                Timber.d("hockeyApp timeUsage = " + Tracking.getUsageTime(LoginActivity.this));
                validateInput();
                //startActivity(new Intent(this, QuickMenuActivity.class));
                break;
            case R.id.tv_about:
                Tracking.stopUsage(LoginActivity.this);
                Intent intent = new Intent(getApplicationContext(), AboutActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
                startActivity(intent);
                break;
        }
    }
}
