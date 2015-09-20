package ie.teamchile.smartapp;

import android.app.Application;

import timber.log.Timber;

/**
 * Created by user on 9/4/15.
 */
public class SmartApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();

        initTimber();
    }

    private void initTimber() {
        if (BuildConfig.DEBUG)
            Timber.plant(new Timber.DebugTree());
    }
}
