package ie.teamchile.smartapp.activities.Login;

import ie.teamchile.smartapp.model.Login;

/**
 * Created by elliot on 27/12/2015.
 */
public interface LoginModel {
    void saveLoginToRealm(Login login);

    void deleteRealmLogin();

    void deleteSingletonInstance();

    void getSharedPrefs();
}
