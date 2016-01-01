package ie.teamchile.smartapp.activities.Login;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
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

import java.lang.ref.WeakReference;

import ie.teamchile.smartapp.BuildConfig;
import ie.teamchile.smartapp.R;
import ie.teamchile.smartapp.activities.About.AboutActivity;
import ie.teamchile.smartapp.activities.QuickMenu.QuickMenuActivity;
import ie.teamchile.smartapp.util.CustomDialogs;
import ie.teamchile.smartapp.util.NotKeys;
import timber.log.Timber;

public class LoginActivity extends AppCompatActivity implements LoginView, OnClickListener {
    private TextView tvUsername;
    private TextView tvPassword;
    private LoginPresenter loginPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        disableScreenshot();
        setContentView(R.layout.activity_login);

        initViews();

        loginPresenter = new LoginPresenterImp(this, new WeakReference<Activity>(LoginActivity.this));

        if (BuildConfig.DEBUG) {
            tvUsername.setText(NotKeys.USERNAME);
            tvPassword.setText(NotKeys.PASSWORD);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
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
        if (!BuildConfig.DEBUG)
            initHockeyApp();

        loginPresenter.onBackPressed();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        loginPresenter.onBackPressed();

        finish();
    }

    @Override
    public void validateInput() {
        if (!TextUtils.isEmpty(getUsername()) && !TextUtils.isEmpty(getPassword())) {
            ProgressDialog pd = new CustomDialogs().showProgressDialog(
                    LoginActivity.this,
                    getString(R.string.logging_in));
            loginPresenter.postLogin(pd);
        } else {
            new CustomDialogs().showErrorDialog(
                    LoginActivity.this,
                    getString(R.string.error_fields_empty));
        }
    }

    @Override
    public void gotoQuickMenu() {
        startActivity(new Intent(getApplicationContext(), QuickMenuActivity.class));
        finish();
    }

    @Override
    public void showErrorToast(String error) {
        Toast.makeText(getApplicationContext(), error, Toast.LENGTH_LONG).show();
    }

    @Override
    public void disableScreenshot() {
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_SECURE, WindowManager.LayoutParams.FLAG_SECURE);
    }

    @Override
    public void initViews() {
        findViewById(R.id.btn_login).setOnClickListener(this);
        findViewById(R.id.tv_about).setOnClickListener(this);
        tvUsername = (TextView) findViewById(R.id.et_username);
        tvPassword = (TextView) findViewById(R.id.et_password);
    }

    @Override
    public String getUsername() {
        return tvUsername.getText().toString();
    }

    @Override
    public String getPassword() {
        return tvPassword.getText().toString();
    }

    private void initHockeyApp() {
        CrashManager.register(getApplicationContext(), NotKeys.APP_ID);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_login:
                Tracking.startUsage(LoginActivity.this);
                Timber.d("hockeyApp Login pressed");
                Timber.d("hockeyApp timeUsage = " + Tracking.getUsageTime(LoginActivity.this));
                validateInput();
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
