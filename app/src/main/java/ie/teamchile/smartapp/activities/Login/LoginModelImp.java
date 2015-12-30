package ie.teamchile.smartapp.activities.Login;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;

import java.lang.ref.WeakReference;
import java.util.Map;

import ie.teamchile.smartapp.R;
import ie.teamchile.smartapp.model.BaseResponseModel;
import ie.teamchile.smartapp.model.Login;
import ie.teamchile.smartapp.util.Constants;
import ie.teamchile.smartapp.util.SharedPrefs;
import io.realm.Realm;
import timber.log.Timber;

/**
 * Created by elliot on 27/12/2015.
 */
public class LoginModelImp implements LoginModel {
    private LoginPresenter loginPresenter;
    private Realm realm;
    private SharedPrefs prefsUtil;
    private WeakReference<Activity> weakActivity;

    public LoginModelImp(LoginPresenter loginPresenter, WeakReference<Activity> weakActivity) {
        this.loginPresenter = loginPresenter;
        realm = loginPresenter.getEncryptedRealm();
        prefsUtil = new SharedPrefs();
        this.weakActivity = weakActivity;
    }

    @Override
    public void saveLoginToRealm(Login login) {
        realm.beginTransaction();
        login.setLoggedIn(true);
        realm.copyToRealmOrUpdate(login);
        realm.commitTransaction();
    }

    @Override
    public void deleteRealmLogin() {
        if (realm.where(Login.class).findFirst() != null) {
            realm.beginTransaction();
            realm.where(Login.class).findFirst().setLoggedIn(false);
            realm.commitTransaction();
        }
    }

    @Override
    public void deleteSingletonInstance() {
        BaseResponseModel.getInstance().deleteInstance();
    }

    @Override
    public void getSharedPrefs() {
        Timber.d("getSharedPrefs called");
        SharedPreferences prefs = weakActivity.get().getSharedPreferences(weakActivity.get().getString(R.string.app_name), Context.MODE_PRIVATE);
        Map<String, ?> prefsMap = prefs.getAll();
        for (Map.Entry<String, ?> entry : prefsMap.entrySet()) {
            Timber.d("key = " + entry.getKey());
            if (entry.getKey().contains(Constants.APPOINTMENT_POST)) {
                Timber.d("get key = " + prefs.getString(entry.getKey(), ""));
                prefsUtil.postAppointment(
                        prefsUtil.getObjectFromString(
                                prefs.getString(entry.getKey(), "")),
                        weakActivity.get(), entry.getKey());
            }
        }
    }
}
