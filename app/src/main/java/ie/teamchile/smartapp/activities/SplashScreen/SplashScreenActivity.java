package ie.teamchile.smartapp.activities.SplashScreen;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.WindowManager;

import java.lang.ref.WeakReference;

import ie.teamchile.smartapp.BuildConfig;
import ie.teamchile.smartapp.R;
import ie.teamchile.smartapp.activities.Login.LoginActivity;
import ie.teamchile.smartapp.activities.QuickMenu.QuickMenuActivity;

import static android.view.View.OnClickListener;

public class SplashScreenActivity extends AppCompatActivity implements SplashScreenView, OnClickListener {
    private SplashScreenPresenter splashScreenPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        disableScreenshot();
        setContentView(R.layout.activity_splash_screen);

        splashScreenPresenter = new SplashScreenPresenterImp(this, new WeakReference<Activity>(SplashScreenActivity.this));

        initViews();

        if (!BuildConfig.DEBUG) {
            splashScreenPresenter.checkIfValidEnvironment();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void disableScreenshot() {
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_SECURE, WindowManager.LayoutParams.FLAG_SECURE);
    }

    @Override
    public void initViews() {
        findViewById(R.id.btn_yes).setOnClickListener(this);
        findViewById(R.id.btn_no).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_yes:
                splashScreenPresenter.checkIfLoggedIn();
                break;
            case R.id.btn_no:
                finish();
                break;
        }
    }

    @Override
    public void gotoQuickMenu() {
        startActivity(new Intent(SplashScreenActivity.this, QuickMenuActivity.class));
    }

    @Override
    public void gotoLogin() {
        startActivity(new Intent(SplashScreenActivity.this, LoginActivity.class));
    }
}
