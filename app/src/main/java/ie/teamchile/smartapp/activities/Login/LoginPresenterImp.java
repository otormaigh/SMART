package ie.teamchile.smartapp.activities.Login;

import android.app.Activity;
import android.app.ProgressDialog;

import net.hockeyapp.android.Tracking;

import java.lang.ref.WeakReference;
import java.util.Calendar;

import ie.teamchile.smartapp.activities.Base.BaseModel;
import ie.teamchile.smartapp.activities.Base.BaseModelImp;
import ie.teamchile.smartapp.activities.Base.BasePresenterImp;
import ie.teamchile.smartapp.api.SmartApiClient;
import ie.teamchile.smartapp.model.BaseResponseModel;
import ie.teamchile.smartapp.model.PostingData;
import ie.teamchile.smartapp.util.Constants;
import ie.teamchile.smartapp.util.SharedPrefs;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;
import timber.log.Timber;

/**
 * Created by elliot on 27/12/2015.
 */
public class LoginPresenterImp extends BasePresenterImp implements LoginPresenter {
    private LoginView loginView;
    private BaseModel baseModel;
    private WeakReference<Activity> weakActivity;
    private SharedPrefs prefsUtil;

    public LoginPresenterImp(LoginView loginView, WeakReference<Activity> weakActivity) {
        super(weakActivity);
        this.loginView = loginView;
        this.weakActivity = weakActivity;
        baseModel = new BaseModelImp(this, weakActivity);
        prefsUtil = new SharedPrefs();
    }

    @Override
    public void postLogin(final ProgressDialog pd) {
        final PostingData login = new PostingData();
        login.postLogin(loginView.getUsername(), loginView.getPassword());

        SmartApiClient.getUnAuthorizedApiClient().postLogin(login, new Callback<BaseResponseModel>() {
            @Override
            public void success(BaseResponseModel baseResponseModel, Response response) {
                Timber.d("postLogin success");
                Tracking.startUsage(weakActivity.get());
                prefsUtil.setLongPrefs(weakActivity.get(),
                        Calendar.getInstance().getTimeInMillis(),
                        Constants.SHARED_PREFS_SPLASH_LOG);
                prefsUtil.deletePrefs(weakActivity.get(), Constants.APPTS_GOT);

                baseModel.saveLoginToRealm(baseResponseModel.getLogin());

                pd.dismiss();
                baseModel.getLoginSharedPrefs();

                loginView.gotoQuickMenu();
            }

            @Override
            public void failure(RetrofitError error) {
                if (error.getResponse() != null) {
                    BaseResponseModel body = (BaseResponseModel) error.getBodyAs(BaseResponseModel.class);
                    Timber.d("retro error = " + body.getError().getError());
                    loginView.showErrorToast(body.getError().getError());
                }
                pd.dismiss();
            }
        });
    }

    @Override
    public void onBackPressed() {
        baseModel.clearData();
    }
}
