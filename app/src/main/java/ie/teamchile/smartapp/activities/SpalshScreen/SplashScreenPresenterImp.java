package ie.teamchile.smartapp.activities.SpalshScreen;

import android.app.Activity;
import android.text.format.DateUtils;

import java.lang.ref.WeakReference;

import ie.teamchile.smartapp.activities.Base.BasePresenterImp;
import ie.teamchile.smartapp.util.EnvironmentChecker;

/**
 * Created by elliot on 27/12/2015.
 */
public class SplashScreenPresenterImp extends BasePresenterImp implements SplashScreenPresenter {
    private SplashScreenView splashScreenView;
    private SplashScreenModel splashScreenModel;
    private WeakReference<Activity> weakActivity;

    public SplashScreenPresenterImp(SplashScreenView splashScreenView, WeakReference<Activity> weakActivity) {
        super(weakActivity);
        this.splashScreenView = splashScreenView;
        this.weakActivity = weakActivity;
        splashScreenModel = new SplashScreenModelImp(weakActivity);
    }

    @Override
    public void checkIfValidEnvironment() {
        if (EnvironmentChecker.isDeviceRooted()
                || EnvironmentChecker.isCertInvalid(weakActivity.get())
                || EnvironmentChecker.isDebuggable(weakActivity.get())
                || EnvironmentChecker.isEmulator()) {
            weakActivity.get().finish();
        } else {
            long time = splashScreenModel.getTimeFromPrefs();

            if (time != 0) {
                if (DateUtils.isToday(time))
                    splashScreenView.checkIfLoggedIn();
            }
        }
    }
}
