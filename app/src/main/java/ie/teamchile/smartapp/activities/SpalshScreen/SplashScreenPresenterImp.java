package ie.teamchile.smartapp.activities.SpalshScreen;

import android.app.Activity;
import android.text.format.DateUtils;

import java.lang.ref.WeakReference;

import ie.teamchile.smartapp.activities.Base.BaseModel;
import ie.teamchile.smartapp.activities.Base.BaseModelImp;
import ie.teamchile.smartapp.activities.Base.BasePresenterImp;
import ie.teamchile.smartapp.model.ResponseLogin;
import ie.teamchile.smartapp.util.EnvironmentChecker;
import io.realm.Realm;

/**
 * Created by elliot on 27/12/2015.
 */
public class SplashScreenPresenterImp extends BasePresenterImp implements SplashScreenPresenter {
    private SplashScreenView splashScreenView;
    private BaseModel baseModel;
    private WeakReference<Activity> weakActivity;
    private Realm realm;

    public SplashScreenPresenterImp(SplashScreenView splashScreenView, WeakReference<Activity> weakActivity) {
        super(weakActivity);
        this.splashScreenView = splashScreenView;
        this.weakActivity = weakActivity;
        baseModel = new BaseModelImp(this, weakActivity);
        realm = getEncryptedRealm();
    }

    @Override
    public void checkIfLoggedIn() {
        if (realm.where(ResponseLogin.class).findFirst() != null && realm.where(ResponseLogin.class).findFirst().isLoggedIn()) {
            splashScreenView.gotoQuickMenu();
        } else {
            splashScreenView.gotoLogin();
        }
    }

    @Override
    public void checkIfValidEnvironment() {
        if (EnvironmentChecker.isDeviceRooted()
                || EnvironmentChecker.isCertInvalid(weakActivity.get())
                || EnvironmentChecker.isDebuggable(weakActivity.get())
                || EnvironmentChecker.isEmulator()) {
            weakActivity.get().finish();
        } else {
            long time = baseModel.getTimeFromPrefs();

            if (time != 0) {
                if (DateUtils.isToday(time))
                    checkIfLoggedIn();
            }
        }
    }
}
