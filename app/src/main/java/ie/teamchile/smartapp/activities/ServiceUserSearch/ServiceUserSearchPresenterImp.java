package ie.teamchile.smartapp.activities.ServiceUserSearch;

import android.app.Activity;
import android.app.ProgressDialog;

import java.lang.ref.WeakReference;
import java.util.ArrayList;

import ie.teamchile.smartapp.BuildConfig;
import ie.teamchile.smartapp.R;
import ie.teamchile.smartapp.activities.Base.BasePresenterImp;
import ie.teamchile.smartapp.api.SmartApiClient;
import ie.teamchile.smartapp.model.Baby;
import ie.teamchile.smartapp.model.BaseResponseModel;
import ie.teamchile.smartapp.model.Pregnancy;
import ie.teamchile.smartapp.model.ServiceUser;
import ie.teamchile.smartapp.util.CustomDialogs;
import io.realm.Realm;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;
import timber.log.Timber;

/**
 * Created by elliot on 27/12/2015.
 */
public class ServiceUserSearchPresenterImp extends BasePresenterImp implements ServiceUserSearchPresenter {
    private ServiceUserSearchView serviceUserSearchView;
    private WeakReference<Activity> weakActivity;
    private Realm realm;
    private ServiceUserSearchModel serviceUserSearchModel;

    public ServiceUserSearchPresenterImp(ServiceUserSearchView serviceUserSearchView, Activity activity, Realm realm) {
        this.serviceUserSearchView = serviceUserSearchView;
        weakActivity = new WeakReference<>(activity);
        this.realm = realm;
        serviceUserSearchModel = new ServiceUserSearchModelImp(realm);
    }

    @Override
    public void getHistories(int pregnancyId, int babyId) {
        SmartApiClient.getAuthorizedApiClient(weakActivity.get()).getVitKHistories(
                babyId,
                new Callback<BaseResponseModel>() {
                    @Override
                    public void success(BaseResponseModel baseResponseModel, Response response) {
                        Timber.d("vit k history done");
                        serviceUserSearchModel.saveVitKToRealm(baseResponseModel.getVitKHistories());
                    }

                    @Override
                    public void failure(RetrofitError error) {
                        Timber.d("vit k history failure = " + error);
                    }
                }
        );
        SmartApiClient.getAuthorizedApiClient(weakActivity.get()).getHearingHistories(
                babyId,
                new Callback<BaseResponseModel>() {
                    @Override
                    public void success(BaseResponseModel baseResponseModel, Response response) {
                        Timber.d("hearing history done");
                        serviceUserSearchModel.saveHearingToRealm(baseResponseModel.getHearingHistories());
                    }

                    @Override
                    public void failure(RetrofitError error) {
                        Timber.d("hearing history failure = " + error);
                    }
                }
        );
        SmartApiClient.getAuthorizedApiClient(weakActivity.get()).getNbstHistories(
                babyId,
                new Callback<BaseResponseModel>() {
                    @Override
                    public void success(BaseResponseModel baseResponseModel, Response response) {
                        serviceUserSearchModel.saveNbstToRealm(baseResponseModel.getNbstHistories());
                    }

                    @Override
                    public void failure(RetrofitError error) {
                        Timber.d("nbst history failure = " + error);
                    }
                }
        );
        SmartApiClient.getAuthorizedApiClient(weakActivity.get()).getFeedingHistoriesByPregId(
                pregnancyId,
                new Callback<BaseResponseModel>() {
                    @Override
                    public void success(BaseResponseModel baseResponseModel, Response response) {
                        Timber.d("feeding history done");
                        serviceUserSearchModel.saveFeedingHistoriesToRealm(baseResponseModel.getFeedingHistories());
                    }

                    @Override
                    public void failure(RetrofitError error) {
                        Timber.d("feeding history failure = " + error);
                    }
                }
        );
    }

    @Override
    public void searchForPatient(String name, String hospitalNumber, String dob) {
        if (name.equals(".") && BuildConfig.DEBUG)
            name = " ";
        else
            name = name.trim();

        final ProgressDialog pd = new CustomDialogs().showProgressDialog(
                weakActivity.get(),
                weakActivity.get().getString(R.string.fetching_information));

        SmartApiClient.getAuthorizedApiClient(weakActivity.get()).getServiceUserByNameDobHospitalNum(
                name,
                hospitalNumber.trim(),
                dob.trim(),
                new Callback<BaseResponseModel>() {
                    @Override
                    public void success(BaseResponseModel baseResponseModel, Response response) {
                        Timber.wtf("size = " + baseResponseModel.getServiceUsers().size());

                        if (baseResponseModel.getServiceUsers().size() != 0) {
                            if (serviceUserSearchView.shouldChangeActivity()) {
                                serviceUserSearchView.gotoServiceUserActivity();
                                serviceUserSearchModel.saveServiceUserToRealm(baseResponseModel);
                                getHistories(
                                        getRecentPregnancy(realm.where(Pregnancy.class).findAll()),
                                        getRecentBaby(realm.where(Baby.class).findAll()));
                            } else {
                                serviceUserSearchView.updateServiceUserList(baseResponseModel.getServiceUsers());
                            }
                        } else {
                            serviceUserSearchView.updateServiceUserList(new ArrayList<ServiceUser>());
                        }
                        pd.dismiss();
                    }

                    @Override
                    public void failure(RetrofitError error) {
                        Timber.d("ServiceUserSearchActivity user search failure = " + error);
                        serviceUserSearchView.updateServiceUserList(new ArrayList<ServiceUser>());
                        pd.dismiss();
                    }
                }
        );
    }

    @Override
    public void onLeaveView() {
        serviceUserSearchModel.deleteDataFromRealm();
    }
}
