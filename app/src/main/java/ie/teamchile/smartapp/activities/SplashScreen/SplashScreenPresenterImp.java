package ie.teamchile.smartapp.activities.SplashScreen;

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
    private SplashScreenView view;
    private BaseModel model;
    private WeakReference<Activity> weakActivity;
    private Realm realm;

    public SplashScreenPresenterImp(SplashScreenView view, WeakReference<Activity> weakActivity) {
        super(weakActivity);
        this.view = view;
        this.weakActivity = weakActivity;
        model = new BaseModelImp(this, weakActivity);
        realm = getEncryptedRealm();
    }

    @Override
    public void checkIfLoggedIn() {
        if (realm.where(ResponseLogin.class).findFirst() != null && realm.where(ResponseLogin.class).findFirst().isLoggedIn()) {
            view.gotoQuickMenu();
        } else {
            view.gotoLogin();
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
            long time = model.getTimeFromPrefs();

            if (time != 0) {
                if (DateUtils.isToday(time))
                    checkIfLoggedIn();
            }
        }
    }
}
