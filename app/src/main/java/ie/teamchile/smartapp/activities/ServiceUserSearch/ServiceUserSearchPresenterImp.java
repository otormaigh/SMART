package ie.teamchile.smartapp.activities.ServiceUserSearch;

import android.app.Activity;
import android.app.ProgressDialog;

import java.lang.ref.WeakReference;
import java.util.ArrayList;

import ie.teamchile.smartapp.BuildConfig;
import ie.teamchile.smartapp.R;
import ie.teamchile.smartapp.activities.Base.BaseModel;
import ie.teamchile.smartapp.activities.Base.BaseModelImp;
import ie.teamchile.smartapp.activities.Base.BasePresenterImp;
import ie.teamchile.smartapp.api.SmartApiClient;
import ie.teamchile.smartapp.model.ResponseBase;
import ie.teamchile.smartapp.model.ResponseServiceUser;
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
    private ServiceUserSearchView view;
    private WeakReference<Activity> weakActivity;
    private BaseModel model;
    private Realm realm;

    public ServiceUserSearchPresenterImp(ServiceUserSearchView view, WeakReference<Activity> weakActivity) {
        this.view = view;
        this.weakActivity = weakActivity;
        model = new BaseModelImp(this);
        realm = getEncryptedRealm();
    }

    @Override
    public void getHistories() {
        int babyId = model.getBaby().getId();
        int pregnancyId = model.getPregnancy().getId();

        SmartApiClient.getAuthorizedApiClient(realm).getVitKHistories(
                babyId,
                new Callback<ResponseBase>() {
                    @Override
                    public void success(ResponseBase responseBase, Response response) {
                        Timber.d("vit k history done");
                        model.saveVitKToRealm(responseBase.getVitKHistories());
                    }

                    @Override
                    public void failure(RetrofitError error) {
                        Timber.d("vit k history failure = " + error);
                    }
                }
        );
        SmartApiClient.getAuthorizedApiClient(realm).getHearingHistories(
                babyId,
                new Callback<ResponseBase>() {
                    @Override
                    public void success(ResponseBase responseBase, Response response) {
                        Timber.d("hearing history done");
                        model.saveHearingToRealm(responseBase.getHearingHistories());
                    }

                    @Override
                    public void failure(RetrofitError error) {
                        Timber.d("hearing history failure = " + error);
                    }
                }
        );
        SmartApiClient.getAuthorizedApiClient(realm).getNbstHistories(
                babyId,
                new Callback<ResponseBase>() {
                    @Override
                    public void success(ResponseBase responseBase, Response response) {
                        model.saveNbstToRealm(responseBase.getNbstHistories());
                    }

                    @Override
                    public void failure(RetrofitError error) {
                        Timber.d("nbst history failure = " + error);
                    }
                }
        );
        SmartApiClient.getAuthorizedApiClient(realm).getFeedingHistoriesByPregId(
                pregnancyId,
                new Callback<ResponseBase>() {
                    @Override
                    public void success(ResponseBase responseBase, Response response) {
                        Timber.d("feeding history done");
                        model.saveFeedingHistoriesToRealm(responseBase.getFeedingHistories());
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

        SmartApiClient.getAuthorizedApiClient(realm).getServiceUserByNameDobHospitalNum(
                name,
                hospitalNumber.trim(),
                dob.trim(),
                new Callback<ResponseBase>() {
                    @Override
                    public void success(ResponseBase responseBase, Response response) {
                        Timber.wtf("size = " + responseBase.getResponseServiceUsers().size());

                        if (!responseBase.getResponseServiceUsers().isEmpty()) {
                            if (view.shouldChangeActivity()) {
                                model.updateServiceUsers(responseBase);

                                getPregnancyNotes();
                                getHistories();

                                view.gotoServiceUserActivity();
                            } else {
                                view.updateServiceUserList(responseBase.getResponseServiceUsers());
                            }
                        } else {
                            view.updateServiceUserList(new ArrayList<ResponseServiceUser>());
                        }
                        pd.dismiss();
                    }

                    @Override
                    public void failure(RetrofitError error) {
                        Timber.d("ServiceUserSearchActivity user search failure = " + error);
                        view.updateServiceUserList(new ArrayList<ResponseServiceUser>());
                        pd.dismiss();
                    }
                }
        );
    }


    @Override
    public void getPregnancyNotes() {
        SmartApiClient.getAuthorizedApiClient(getEncryptedRealm()).getPregnancyNotes(
                model.getPregnancy().getId(),
                new Callback<ResponseBase>() {
                    @Override
                    public void success(ResponseBase responseBase, Response response) {
                        Timber.d("put getMidwiferyNotes retro success");

                        model.updatePregnancyNotes(responseBase.getResponsePregnancyNotes());
                    }

                    @Override
                    public void failure(RetrofitError error) {
                        Timber.d("put getMidwiferyNotes retro failure = " + error);
                    }
                }
        );
    }

    @Override
    public void onLeaveView() {
        model.deleteServiceUserFromRealm();
    }
}
