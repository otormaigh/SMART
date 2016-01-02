package ie.teamchile.smartapp.activities.Base;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import net.hockeyapp.android.Tracking;

import java.lang.ref.WeakReference;
import java.net.ConnectException;
import java.net.UnknownHostException;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.List;

import javax.net.ssl.SSLHandshakeException;

import ie.teamchile.smartapp.R;
import ie.teamchile.smartapp.activities.SpalshScreen.SplashScreenActivity;
import ie.teamchile.smartapp.api.SmartApiClient;
import ie.teamchile.smartapp.model.ResponseBaby;
import ie.teamchile.smartapp.model.ResponseBase;
import ie.teamchile.smartapp.model.ResponsePregnancy;
import ie.teamchile.smartapp.util.Constants;
import ie.teamchile.smartapp.util.CustomDialogs;
import io.realm.Realm;
import io.realm.RealmConfiguration;
import io.realm.exceptions.RealmMigrationNeededException;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;
import timber.log.Timber;

/**
 * Created by elliot on 27/12/2015.
 */
public class BasePresenterImp implements BasePresenter {
    private static Realm realm;
    private BaseViewSec baseViewSec;
    private WeakReference<Activity> weakActivity;
    private BaseModel model;

    public BasePresenterImp() {
    }

    public BasePresenterImp(WeakReference<Activity> weakActivity) {
        this.weakActivity = weakActivity;
        model = new BaseModelImp(this, weakActivity);
    }

    public BasePresenterImp(BaseViewSec baseViewSec, WeakReference<Activity> weakActivity) {
        this.baseViewSec = baseViewSec;
        this.weakActivity = weakActivity;
        model = new BaseModelImp(this, weakActivity);
    }

    @Override
    public Realm getEncryptedRealm() {
        if (realm == null) {
            RealmConfiguration realmConfiguration = getRealmConfiguration();
            try {
                realm = Realm.getInstance(realmConfiguration);
            } catch (RealmMigrationNeededException e) {
                Timber.e(Log.getStackTraceString(e));
                Realm.deleteRealm(realmConfiguration);
                realm = Realm.getInstance(realmConfiguration);
            }
        }
        return realm;
    }

    private RealmConfiguration getRealmConfiguration() {
        byte[] key = new byte[64];
        new SecureRandom().nextBytes(key);

        RealmConfiguration realmConfiguration = new RealmConfiguration.Builder(weakActivity.get())
                .encryptionKey(key)
                .build();

        Realm.deleteRealm(realmConfiguration);

        return realmConfiguration;
    }

    @Override
    public void closeRealm() {
        if (realm != null)
            realm.close();

        realm = null;
    }

    @Override
    public int getRecentPregnancy(List<ResponsePregnancy> responsePregnancyList) {
        List<Long> asDate = new ArrayList<>();
        if (responsePregnancyList.size() > 1) {
            try {
                for (int i = 0; i < responsePregnancyList.size(); i++) {
                    if (responsePregnancyList.get(i).getEstimatedDeliveryDate() != null) {
                        asDate.add(responsePregnancyList.get(i).getEstimatedDeliveryDate().getTime());
                    } else {
                        asDate.add(0L);
                    }
                }
                return responsePregnancyList
                        .get(asDate.indexOf(Collections.max(asDate)))
                        .getId();
            } catch (NullPointerException e) {
                Timber.e(Log.getStackTraceString(e));
                return 0;
            }
        } else if (responsePregnancyList.isEmpty()) {
            return 0;
        } else {
            return responsePregnancyList.get(0).getId();
        }
    }

    @Override
    public int getRecentBaby(List<ResponseBaby> responseBabyList) {
        List<Long> asDate = new ArrayList<>();
        if (responseBabyList.size() > 1) {
            try {
                for (int i = 0; i < responseBabyList.size(); i++) {
                    if (responseBabyList.get(i).getDeliveryDateTime() != null) {
                        asDate.add(responseBabyList.get(i).getDeliveryDateTime().getTime());
                    } else {
                        asDate.add(0L);
                    }
                }
                return responseBabyList
                        .get(asDate.indexOf(Collections.max(asDate)))
                        .getId();
            } catch (NullPointerException e) {
                Timber.e(Log.getStackTraceString(e));
                return 0;
            }
        } else if (responseBabyList.isEmpty()) {
            return 0;
        } else {
            return responseBabyList.get(0).getId();
        }
    }

    @Override
    public void doLogout(final Intent intent) {
        final ProgressDialog pd = new CustomDialogs()
                .showProgressDialog(
                        weakActivity.get(),
                        weakActivity.get().getString(R.string.logging_out));

        SmartApiClient.getAuthorizedApiClient(realm).postLogout(
                "",
                new Callback<ResponseBase>() {
                    @Override
                    public void success(ResponseBase responseBase, Response response) {
                        Timber.d("logout success");
                        Tracking.stopUsage(weakActivity.get());
                        Timber.d("timeUsage = " + Tracking.getUsageTime(weakActivity.get()));

                        Toast.makeText(weakActivity.get(),
                                weakActivity.get().getString(R.string.you_are_now_logged_out),
                                Toast.LENGTH_SHORT).show();
                        pd.dismiss();
                        weakActivity.get().startActivity(intent);
                        model.clearData();
                    }

                    @Override
                    public void failure(RetrofitError error) {
                        Timber.d("in logout failure error = " + error);
                        Toast.makeText(weakActivity.get(),
                                weakActivity.get().getString(R.string.error_logout),
                                Toast.LENGTH_SHORT).show();
                        pd.dismiss();
                        weakActivity.get().startActivity(intent);
                        model.clearData();
                    }
                }
        );
    }

    @Override
    public void doLogoutWithoutIntent() {
        SmartApiClient.getAuthorizedApiClient(realm).postLogout(
                "",
                new Callback<ResponseBase>() {
                    @Override
                    public void success(ResponseBase responseBase, Response response) {
                        Timber.d("in logout success");
                        Tracking.stopUsage(weakActivity.get());
                        Timber.d("timeUsage QuickMenu = " + Tracking.getUsageTime(weakActivity.get()));
                        model.clearData();
                        weakActivity.get().finish();
                        if (baseViewSec.getNotificationManager() != null) {
                            baseViewSec.getNotificationManager().cancelAll();
                            baseViewSec.showNotification(weakActivity.get().getString(R.string.app_name),
                                    weakActivity.get().getString(R.string.success_logout),
                                    SplashScreenActivity.class);
                        } else
                            baseViewSec.showNotification(weakActivity.get().getString(R.string.app_name),
                                    weakActivity.get().getString(R.string.success_logout),
                                    SplashScreenActivity.class);
                        model.clearData();
                    }

                    @Override
                    public void failure(RetrofitError error) {
                        Timber.d("in logout failure error = " + error);
                        model.clearData();
                        weakActivity.get().finish();
                    }
                }
        );
    }

    @Override
    public void getAllAppointments() {
        final ProgressDialog pd = new CustomDialogs()
                .showProgressDialog(
                        weakActivity.get(),
                        weakActivity.get().getString(R.string.updating_appointments));

        SmartApiClient.getAuthorizedApiClient(realm).getAllAppointments(
                new Callback<ResponseBase>() {
                    @Override
                    public void success(ResponseBase responseBase, Response response) {
                        model.saveAppointmentsToRealm(responseBase.getAppointments());
                        pd.dismiss();
                    }

                    @Override
                    public void failure(RetrofitError error) {
                        Timber.d("appointments retro failure " + error);
                        Toast.makeText(weakActivity.get(), weakActivity.get().getString(R.string.error_downloading_appointments),
                                Toast.LENGTH_LONG).show();
                        pd.dismiss();
                    }
                }
        );
    }

    @Override
    public void checkRetroError(RetrofitError error, Context context) {
        String time = Constants.DF_TIME_W_SEC.format(Calendar.getInstance().getTime());
        try {
            throw (error.getCause());
        } catch (UnknownHostException e) {
            Timber.e(Log.getStackTraceString(e));
            Timber.e("UnknownHostException");
            // unknown host
        } catch (SSLHandshakeException e) {
            Timber.e(Log.getStackTraceString(e));
            Timber.e("SSLHandshakeException");
        } catch (ConnectException e) {
            Timber.e(Log.getStackTraceString(e));
            Timber.e("ConnectException");
            /*if(!checkIfConnected(context)){
                new SharedPrefs().setJsonPrefs(data, time);
            }*/
        } catch (Throwable throwable) {
            Timber.e(Log.getStackTraceString(throwable));
            Timber.e("Throwable");
        }
        Timber.d(error.toString());
    }

    @Override
    public boolean isLoggedIn() {
        return model.getLogin() == null && model.getLogin().isLoggedIn();
    }
}
