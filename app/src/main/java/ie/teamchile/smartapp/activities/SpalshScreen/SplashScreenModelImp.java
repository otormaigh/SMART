package ie.teamchile.smartapp.activities.SpalshScreen;

import android.app.Activity;

import java.lang.ref.WeakReference;

import ie.teamchile.smartapp.util.Constants;
import ie.teamchile.smartapp.util.SharedPrefs;

/**
 * Created by elliot on 27/12/2015.
 */
public class SplashScreenModelImp implements SplashScreenModel {
    private SharedPrefs sharedPrefs;
    private WeakReference<Activity> weakActivity;

    public SplashScreenModelImp(WeakReference<Activity> weakActivity) {
        this.weakActivity = weakActivity;
        sharedPrefs = new SharedPrefs();
    }
    @Override
    public long getTimeFromPrefs() {
        return sharedPrefs.getLongPrefs(weakActivity.get(), Constants.SHARED_PREFS_SPLASH_LOG);
    }
}
