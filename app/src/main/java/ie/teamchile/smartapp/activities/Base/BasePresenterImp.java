package ie.teamchile.smartapp.activities.Base;

import android.app.Activity;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import net.hockeyapp.android.Tracking;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import ie.teamchile.smartapp.R;
import ie.teamchile.smartapp.activities.SpalshScreen.SplashScreenActivity;
import ie.teamchile.smartapp.api.SmartApiClient;
import ie.teamchile.smartapp.model.Baby;
import ie.teamchile.smartapp.model.BaseResponseModel;
import ie.teamchile.smartapp.model.Pregnancy;
import ie.teamchile.smartapp.util.ClearData;
import ie.teamchile.smartapp.util.CustomDialogs;
import io.realm.Realm;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;
import timber.log.Timber;

/**
 * Created by elliot on 27/12/2015.
 */
public class BasePresenterImp implements  BasePresenter {
    private WeakReference<Activity> weakActivity;
    private BaseModel baseModel;
    private NotificationManager notificationManager;

    public BasePresenterImp(Activity activity, Realm realm) {
        weakActivity = new WeakReference<>(activity);
        baseModel = new BaseModelImp(realm);
    }

    @Override
    public int getRecentPregnancy(List<Pregnancy> pregnancyList) {
        List<Long> asDate = new ArrayList<>();
        if (pregnancyList.size() > 1) {
            try {
                for (int i = 0; i < pregnancyList.size(); i++) {
                    if (pregnancyList.get(i).getEstimatedDeliveryDate() != null) {
                        asDate.add(pregnancyList.get(i).getEstimatedDeliveryDate().getTime());
                    } else {
                        asDate.add(0L);
                    }
                }
                return pregnancyList
                        .get(asDate.indexOf(Collections.max(asDate)))
                        .getId();
            } catch (NullPointerException e) {
                Timber.e(Log.getStackTraceString(e));
                return 0;
            }
        } else if (pregnancyList.isEmpty()) {
            return 0;
        } else {
            return pregnancyList.get(0).getId();
        }
    }

    @Override
    public int getRecentBaby(List<Baby> babyList) {
        List<Long> asDate = new ArrayList<>();
        if (babyList.size() > 1) {
            try {
                for (int i = 0; i < babyList.size(); i++) {
                    if (babyList.get(i).getDeliveryDateTime() != null) {
                        asDate.add(babyList.get(i).getDeliveryDateTime().getTime());
                    } else {
                        asDate.add(0L);
                    }
                }
                return babyList
                        .get(asDate.indexOf(Collections.max(asDate)))
                        .getId();
            } catch (NullPointerException e) {
                Timber.e(Log.getStackTraceString(e));
                return 0;
            }
        } else if (babyList.isEmpty()) {
            return 0;
        } else {
            return babyList.get(0).getId();
        }
    }

    @Override
    public void doLogout(final Intent intent) {
        final ProgressDialog pd = new CustomDialogs()
                .showProgressDialog(
                        weakActivity.get(),
                        weakActivity.get().getString(R.string.logging_out));

        SmartApiClient.getAuthorizedApiClient(weakActivity.get()).postLogout(
                "",
                new Callback<BaseResponseModel>() {
                    @Override
                    public void success(BaseResponseModel baseResponseModel, Response response) {
                        Timber.d("logout success");
                        Tracking.stopUsage(weakActivity.get());
                        Timber.d("timeUsage = " + Tracking.getUsageTime(weakActivity.get()));
                        switch (response.getStatus()) {
                            case 200:
                                Timber.d("in logout success 200");
                                doLogout(intent);
                                break;
                            default:
                                Timber.d("in logout success response = " + response.getStatus());
                        }
                    }

                    @Override
                    public void failure(RetrofitError error) {
                        Timber.d("in logout failure error = " + error);
                        if (error.getResponse().getStatus() == 401) {
                            Toast.makeText(weakActivity.get(),
                                    weakActivity.get().getString(R.string.you_are_now_logged_out),
                                    Toast.LENGTH_SHORT).show();
                            pd.dismiss();
                            weakActivity.get().startActivity(intent);
                        } else {
                            Toast.makeText(weakActivity.get(),
                                    weakActivity.get().getString(R.string.error_logout),
                                    Toast.LENGTH_SHORT).show();
                            pd.dismiss();
                        }
                        new ClearData(weakActivity.get());
                    }
                }
        );
    }

    @Override
    public void doLogoutWithoutIntent() {
        SmartApiClient.getAuthorizedApiClient(weakActivity.get()).postLogout(
                "",
                new Callback<BaseResponseModel>() {
                    @Override
                    public void success(BaseResponseModel baseResponseModel, Response response) {
                        Timber.d("in logout success");
                        Tracking.stopUsage(weakActivity.get());
                        Timber.d("timeUsage QuickMenu = " + Tracking.getUsageTime(weakActivity.get()));
                        new ClearData(weakActivity.get());
                        weakActivity.get().finish();
                        if (notificationManager != null) {
                            notificationManager.cancelAll();
                            showNotification(weakActivity.get().getString(R.string.app_name),
                                    weakActivity.get().getString(R.string.success_logout),
                                    SplashScreenActivity.class);
                        } else
                            showNotification(weakActivity.get().getString(R.string.app_name),
                                    weakActivity.get().getString(R.string.success_logout),
                                    SplashScreenActivity.class);
                        new ClearData(weakActivity.get());
                    }

                    @Override
                    public void failure(RetrofitError error) {
                        Timber.d("in logout failure error = " + error);
                        weakActivity.get().finish();
                        new ClearData(weakActivity.get());
                    }
                }
        );
    }

    @Override
    public void showNotification(String title, String message, Class activityClass) {
        notificationManager = (NotificationManager)
                weakActivity.get().getSystemService(Context.NOTIFICATION_SERVICE);
        Intent intent = new Intent(weakActivity.get(), activityClass);
        PendingIntent pIntent = PendingIntent.getActivity(weakActivity.get(), 0, intent, 0);

        Notification n = new Notification.Builder(weakActivity.get())
                .setContentTitle(title)
                .setContentText(message)
                .setSmallIcon(R.drawable.ic_launcher)
                .setContentIntent(pIntent)
                .setAutoCancel(true).build();

        notificationManager.notify(0, n);
    }

    @Override
    public void getAllAppointments() {
        final ProgressDialog pd = new CustomDialogs()
                .showProgressDialog(
                        weakActivity.get(),
                        weakActivity.get().getString(R.string.updating_appointments));

        SmartApiClient.getAuthorizedApiClient(weakActivity.get()).getAllAppointments(
                new Callback<BaseResponseModel>() {
                    @Override
                    public void success(BaseResponseModel baseResponseModel, Response response) {
                        baseModel.saveAppointmentsToRealm(baseResponseModel.getAppointments());
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
}
