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
import ie.teamchile.smartapp.model.ResponseBase;
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
    private LoginView view;
    private BaseModel model;
    private WeakReference<Activity> weakActivity;
    private SharedPrefs prefsUtil;

    public LoginPresenterImp(LoginView view, WeakReference<Activity> weakActivity) {
        super(weakActivity);
        this.view = view;
        this.weakActivity = weakActivity;
        model = new BaseModelImp(this, weakActivity);
        prefsUtil = new SharedPrefs();
    }

    @Override
    public void postLogin(final ProgressDialog pd) {
        final PostingData login = new PostingData();
        login.postLogin(view.getUsername(), view.getPassword());

        SmartApiClient.getUnAuthorizedApiClient().postLogin(login, new Callback<ResponseBase>() {
            @Override
            public void success(ResponseBase responseBase, Response response) {
                Timber.d("postLogin success");
                Tracking.startUsage(weakActivity.get());
                prefsUtil.setLongPrefs(weakActivity.get(),
                        Calendar.getInstance().getTimeInMillis(),
                        Constants.SHARED_PREFS_SPLASH_LOG);
                prefsUtil.deletePrefs(weakActivity.get(), Constants.APPTS_GOT);

                model.saveLoginToRealm(responseBase.getResponseLogin());

                pd.dismiss();
                model.getLoginSharedPrefs();

                view.gotoQuickMenu();
            }

            @Override
            public void failure(RetrofitError error) {
                if (error.getResponse() != null) {
                    ResponseBase body = (ResponseBase) error.getBodyAs(ResponseBase.class);
                    Timber.d("retro error = " + body.getError().getError());
                    view.showErrorToast(body.getError().getError());
                }
                pd.dismiss();
            }
        });
    }
}
