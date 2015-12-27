package ie.teamchile.smartapp.activities.SpalshScreen;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;

import ie.teamchile.smartapp.BuildConfig;
import ie.teamchile.smartapp.R;
import ie.teamchile.smartapp.activities.Login.LoginActivity;
import ie.teamchile.smartapp.activities.QuickMenuActivity;
import ie.teamchile.smartapp.model.Login;
import ie.teamchile.smartapp.util.ClearData;
import io.realm.Realm;
import io.realm.RealmConfiguration;
import io.realm.exceptions.RealmMigrationNeededException;
import timber.log.Timber;

import static android.view.View.OnClickListener;

public class SplashScreenActivity extends AppCompatActivity implements SplashScreenView, OnClickListener {
    private Realm realm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        disableScreenshot();
        setContentView(R.layout.activity_splash_screen);

        SplashScreenPresenter splashScreenPresenter = new SplashScreenPresenterImp(this, SplashScreenActivity.this);

        initViews();

        realm = getRealm();

        if (BuildConfig.DEBUG) {
            splashScreenPresenter.checkIfValidEnvironment();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        if (realm != null) {
            realm.close();
            realm = null;
        }
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

    protected Realm getRealm() {
        if (realm == null) {
            try {
                realm = Realm.getInstance(this);
            } catch (RealmMigrationNeededException e) {
                Timber.e(Log.getStackTraceString(e));
                Realm.deleteRealm(new RealmConfiguration.Builder(this).build());
                realm = Realm.getInstance(this);
            }
        }
        return realm;
    }

    @Override
    public void checkIfLoggedIn() {
        if (realm.where(Login.class).findFirst() != null && realm.where(Login.class).findFirst().isLoggedIn()) {
            startActivity(new Intent(SplashScreenActivity.this, QuickMenuActivity.class));
        } else {
            new ClearData(getApplicationContext());
            startActivity(new Intent(SplashScreenActivity.this, LoginActivity.class));
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_yes:
                checkIfLoggedIn();
                break;
            case R.id.btn_no:
                finish();
                break;
        }
    }
}
