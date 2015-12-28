package ie.teamchile.smartapp.activities.QuickMenu;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.CountDownTimer;

import java.lang.ref.WeakReference;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;

import ie.teamchile.smartapp.R;
import ie.teamchile.smartapp.activities.Base.BasePresenterImp;
import ie.teamchile.smartapp.api.SmartApiClient;
import ie.teamchile.smartapp.model.BaseModel;
import ie.teamchile.smartapp.model.Clinic;
import ie.teamchile.smartapp.model.Login;
import ie.teamchile.smartapp.model.ServiceUserAction;
import ie.teamchile.smartapp.util.CustomDialogs;
import io.realm.Realm;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;
import timber.log.Timber;

/**
 * Created by elliot on 27/12/2015.
 */
public class QuickMenuPresenterImp extends BasePresenterImp implements QuickMenuPresenter {
    private QuickMenuView quickMenuView;
    private WeakReference<Activity> weakActivity;
    private Realm realm;
    private QuickMenuModel quickMenuModel;
    private CountDownTimer timer;
    private int done;

    public QuickMenuPresenterImp(QuickMenuView quickMenuView, Activity activity, Realm realm) {
        this.quickMenuView = quickMenuView;
        weakActivity = new WeakReference<>(activity);
        this.realm = realm;
        quickMenuModel = new QuickMenuModelImp(realm);
    }

    @Override
    public void updateData() {
        done = 0;
        final ProgressDialog pd = new CustomDialogs().showProgressDialog(weakActivity.get(), weakActivity.get().getString(R.string.updating_info));

        SmartApiClient.getAuthorizedApiClient(weakActivity.get()).getServiceProviderById(
                realm.where(Login.class).findFirst().getId(),
                new Callback<BaseModel>() {
                    @Override
                    public void success(BaseModel baseModel, Response response) {
                        Timber.d("serviceProvider success");

                        quickMenuModel.saveServiceProviderToRealm(baseModel.getServiceProviders().get(0));

                        done++;
                        Timber.d("done = " + done);
                    }

                    @Override
                    public void failure(RetrofitError error) {
                        Timber.d("serviceProvider failure = " + error);
                        done++;
                    }
                }
        );

        SmartApiClient.getAuthorizedApiClient(weakActivity.get()).getAllServiceOptions(
                new Callback<BaseModel>() {
                    @Override
                    public void success(BaseModel baseModel, Response response) {
                        quickMenuModel.saveServiceOptionsToRealm(baseModel.getServiceOptions());

                        Timber.d("service options finished");
                        done++;
                        Timber.d("done = " + done);
                    }

                    @Override
                    public void failure(RetrofitError error) {
                        Timber.d("service options retro failure " + error);
                        done++;
                    }
                }
        );
        SmartApiClient.getAuthorizedApiClient(weakActivity.get()).getAllClinics(
                new Callback<BaseModel>() {
                    @Override
                    public void success(BaseModel baseModel, Response response) {
                        quickMenuModel.saveClinicsToRealm(baseModel.getClinics());

                        Map<Integer, Clinic> clinicMap = new HashMap<>();
                        for (int i = 0; i < baseModel.getClinics().size(); i++) {
                            clinicMap.put(baseModel.getClinics().get(i).getId(),
                                    baseModel.getClinics().get(i));
                        }
                        BaseModel.getInstance().setClinicMap(clinicMap);
                        Timber.d("clinics finished");
                        done++;
                        Timber.d("done = " + done);
                    }

                    @Override
                    public void failure(RetrofitError error) {
                        Timber.d("clinics retro failure " + error);
                        done++;
                    }
                }
        );

        SmartApiClient.getAuthorizedApiClient(weakActivity.get()).getServiceUserActions(
                new Callback<BaseModel>() {
                    @Override
                    public void success(BaseModel baseModel, Response response) {
                        Timber.d("actions retro success");
                        Collections.sort(baseModel.getServiceUserActions(), new Comparator<ServiceUserAction>() {
                            @Override
                            public int compare(ServiceUserAction lhs, ServiceUserAction rhs) {
                                return (lhs.getShortCode()).compareTo(rhs.getShortCode());
                            }
                        });
                        quickMenuModel.saveServiceUserActionsToRealm(baseModel.getServiceUserActions());
                        done++;
                    }

                    @Override
                    public void failure(RetrofitError error) {
                        Timber.d("serviceActions retro failure " + error);
                        done++;
                    }
                }
        );

        timer = new CountDownTimer(200, 100) {
            @Override
            public void onTick(long millisUntilFinished) {
            }

            @Override
            public void onFinish() {
                if (done >= 4)
                    pd.dismiss();
                else
                    timer.start();
            }
        }.start();
    }
}
