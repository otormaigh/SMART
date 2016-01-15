package ie.teamchile.smartapp.activities.SplashScreen;

import ie.teamchile.smartapp.activities.Base.BasePresenter;

/**
 * Created by elliot on 27/12/2015.
 */
public interface SplashScreenPresenter extends BasePresenter {
    void checkIfLoggedIn();

    void checkIfValidEnvironment();
}
