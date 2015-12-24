package ie.teamchile.smartapp.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.format.DateUtils;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;

import ie.teamchile.smartapp.R;
import ie.teamchile.smartapp.model.Login;
import ie.teamchile.smartapp.util.ClearData;
import ie.teamchile.smartapp.util.Constants;
import ie.teamchile.smartapp.util.SharedPrefs;
import io.realm.Realm;
import io.realm.RealmConfiguration;
import io.realm.exceptions.RealmMigrationNeededException;
import timber.log.Timber;

public class SplashScreenActivity extends AppCompatActivity implements View.OnClickListener {
    private String rootMsg;
    private Button btnYes;
    private Button btnNo;
    private SharedPrefs sharedPrefs = new SharedPrefs();
    private Realm realm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_SECURE, WindowManager.LayoutParams.FLAG_SECURE);
        setContentView(R.layout.activity_splash_screen);

        realm = getRealm();

        checkIfValidEnvironment();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        if(realm != null) {
            realm.close();
            realm = null;
        }
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

    private void checkIfValidEnvironment() {
       /* if (EnvironmentChecker.isDeviceRooted()
                || EnvironmentChecker.isCertInvalid(this)
                || EnvironmentChecker.isDebuggable(this)
                || EnvironmentChecker.isEmulator()) {
            finish();
        } else {*/
            btnYes = (Button) findViewById(R.id.btn_yes);
            btnYes.setOnClickListener(this);
            btnNo = (Button) findViewById(R.id.btn_no);
            btnNo.setOnClickListener(this);

            long time = sharedPrefs.getLongPrefs(this, Constants.SHARED_PREFS_SPLASH_LOG);

            if (time != 0) {
                if (DateUtils.isToday(time))
                    checkIfLoggedIn();
            }
        //}
    }

    private void checkIfLoggedIn() {
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
