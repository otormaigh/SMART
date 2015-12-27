package ie.teamchile.smartapp.activities.Login;

import android.app.Activity;
import android.app.ProgressDialog;

import net.hockeyapp.android.Tracking;

import java.lang.ref.WeakReference;
import java.util.Calendar;

import ie.teamchile.smartapp.api.SmartApiClient;
import ie.teamchile.smartapp.model.BaseModel;
import ie.teamchile.smartapp.model.PostingData;
import ie.teamchile.smartapp.util.Constants;
import ie.teamchile.smartapp.util.SharedPrefs;
import io.realm.Realm;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;
import timber.log.Timber;

/**
 * Created by elliot on 27/12/2015.
 */
public class LoginPresenterImp implements LoginPresenter {
    private LoginView loginView;
    private LoginModel loginModel;
    private WeakReference<Activity> weakActivity;
    private SharedPrefs prefsUtil;

    public LoginPresenterImp(LoginView loginView, Realm realm, Activity activity) {
        this.loginView = loginView;
        weakActivity = new WeakReference<>(activity);
        loginModel = new LoginModelImp(this, realm, weakActivity);
        prefsUtil = new SharedPrefs();
    }

    @Override
    public void postLogin(final ProgressDialog pd) {
        final PostingData login = new PostingData();
        login.postLogin(loginView.getUsername(), loginView.getPassword());

        SmartApiClient.getUnAuthorizedApiClient().postLogin(login, new Callback<BaseModel>() {
            @Override
            public void success(BaseModel baseModel, Response response) {
                Timber.d("postLogin success");
                Tracking.startUsage(weakActivity.get());
                prefsUtil.setLongPrefs(weakActivity.get(),
                        Calendar.getInstance().getTimeInMillis(),
                        Constants.SHARED_PREFS_SPLASH_LOG);
                prefsUtil.deletePrefs(weakActivity.get(), Constants.APPTS_GOT);

                loginModel.saveLoginToRealm(baseModel.getLogin());

                pd.dismiss();
                loginModel.getSharedPrefs();

                loginView.gotoQuickMenu();
            }

            @Override
            public void failure(RetrofitError error) {
                if (error.getResponse() != null) {
                    BaseModel body = (BaseModel) error.getBodyAs(BaseModel.class);
                    Timber.d("retro error = " + body.getError().getError());
                    loginView.showErrorToast(body.getError().getError());
                }
                pd.dismiss();
            }
        });
    }

    @Override
    public void onBackPressed() {
        loginModel.deleteRealmLogin();
        loginModel.deleteSingletonInstance();
    }
}
