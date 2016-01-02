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
import ie.teamchile.smartapp.activities.Base.BaseModel;
import ie.teamchile.smartapp.activities.Base.BaseModelImp;
import ie.teamchile.smartapp.activities.Base.BasePresenterImp;
import ie.teamchile.smartapp.api.SmartApiClient;
import ie.teamchile.smartapp.model.ResponseBase;
import ie.teamchile.smartapp.model.ResponseClinic;
import ie.teamchile.smartapp.model.ResponseLogin;
import ie.teamchile.smartapp.model.ResponseServiceUserAction;
import ie.teamchile.smartapp.util.CustomDialogs;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;
import timber.log.Timber;

/**
 * Created by elliot on 27/12/2015.
 */
public class QuickMenuPresenterImp extends BasePresenterImp implements QuickMenuPresenter {
    private WeakReference<Activity> weakActivity;
    private BaseModel model;
    private CountDownTimer timer;
    private int done;

    public QuickMenuPresenterImp(WeakReference<Activity> weakActivity) {
        super(weakActivity);
        this.weakActivity = weakActivity;
        model = new BaseModelImp(this);
    }

    @Override
    public void updateData() {
        done = 0;
        final ProgressDialog pd = new CustomDialogs().showProgressDialog(weakActivity.get(), weakActivity.get().getString(R.string.updating_info));

        SmartApiClient.getAuthorizedApiClient(getEncryptedRealm()).getServiceProviderById(
                getEncryptedRealm().where(ResponseLogin.class).findFirst().getId(),
                new Callback<ResponseBase>() {
                    @Override
                    public void success(ResponseBase responseBase, Response response) {
                        Timber.d("serviceProvider success");

                        model.saveServiceProviderToRealm(responseBase.getResponseServiceProviders().get(0));

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

        SmartApiClient.getAuthorizedApiClient(getEncryptedRealm()).getAllServiceOptions(
                new Callback<ResponseBase>() {
                    @Override
                    public void success(ResponseBase responseBase, Response response) {
                        model.saveServiceOptionsToRealm(responseBase.getResponseServiceOptions());

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
        SmartApiClient.getAuthorizedApiClient(getEncryptedRealm()).getAllClinics(
                new Callback<ResponseBase>() {
                    @Override
                    public void success(ResponseBase responseBase, Response response) {
                        model.updateClinics(responseBase.getResponseClinics());

                        Map<Integer, ResponseClinic> clinicMap = new HashMap<>();
                        for (int i = 0; i < responseBase.getResponseClinics().size(); i++) {
                            clinicMap.put(responseBase.getResponseClinics().get(i).getId(),
                                    responseBase.getResponseClinics().get(i));
                        }
                        ResponseBase.getInstance().setClinicMap(clinicMap);
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

        SmartApiClient.getAuthorizedApiClient(getEncryptedRealm()).getServiceUserActions(
                new Callback<ResponseBase>() {
                    @Override
                    public void success(ResponseBase responseBase, Response response) {
                        Timber.d("actions retro success");
                        Collections.sort(responseBase.getResponseServiceUserActions(), new Comparator<ResponseServiceUserAction>() {
                            @Override
                            public int compare(ResponseServiceUserAction lhs, ResponseServiceUserAction rhs) {
                                return (lhs.getShortCode()).compareTo(rhs.getShortCode());
                            }
                        });
                        model.saveServiceUserActionsToRealm(responseBase.getResponseServiceUserActions());
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

    @Override
    public boolean isDataEmpty() {
        return model.getClinics().isEmpty() || model.getServiceOptions().isEmpty();
    }
}
