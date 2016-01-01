package ie.teamchile.smartapp.activities.CreateAppointment;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.widget.Toast;

import java.lang.ref.WeakReference;
import java.util.Calendar;

import ie.teamchile.smartapp.R;
import ie.teamchile.smartapp.activities.Base.BaseModel;
import ie.teamchile.smartapp.activities.Base.BaseModelImp;
import ie.teamchile.smartapp.activities.Base.BasePresenterImp;
import ie.teamchile.smartapp.api.SmartApiClient;
import ie.teamchile.smartapp.model.BaseResponseModel;
import ie.teamchile.smartapp.model.PostingData;
import ie.teamchile.smartapp.model.ServiceUser;
import ie.teamchile.smartapp.util.Constants;
import ie.teamchile.smartapp.util.CustomDialogs;
import ie.teamchile.smartapp.util.SharedPrefs;
import io.realm.Realm;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;
import timber.log.Timber;

/**
 * Created by elliot on 01/01/2016.
 */
public class CreateAppointmentPresenterImp extends BasePresenterImp implements CreateAppointmentPresenter {
    private CreateAppointmentView createAppointmentView;
    private WeakReference<Activity> weakActivity;
    private Realm realm;
    private BaseModel baseModel;

    public CreateAppointmentPresenterImp(CreateAppointmentView createAppointmentView, WeakReference<Activity> weakActivity) {
        this.createAppointmentView = createAppointmentView;
        this.weakActivity = weakActivity;
        realm = getEncryptedRealm();
        baseModel = new BaseModelImp(this, weakActivity);
    }

    @Override
    public void searchPatient(String serviceUserName) {
        final ProgressDialog pd = new CustomDialogs().showProgressDialog(
                weakActivity.get(),
                weakActivity.get().getString(R.string.fetching_information));

        SmartApiClient.getAuthorizedApiClient(realm).getServiceUserByName(
                serviceUserName,
                new Callback<BaseResponseModel>() {
                    @Override
                    public void success(BaseResponseModel baseResponseModel, Response response) {
                        Timber.d("searchPatient success");
                        if (baseResponseModel.getServiceUsers().size() != 0) {
                            baseModel.saveServiceUserToRealm(baseResponseModel);
                            pd.dismiss();
                            createAppointmentView.userSearchDialog(
                                    weakActivity.get().getString(R.string.search_results),
                                    baseModel.getServiceUsers());
                        } else {
                            pd.dismiss();
                            new CustomDialogs().showWarningDialog(
                                    weakActivity.get(),
                                    weakActivity.get().getString(R.string.no_search_results_found));
                        }
                    }

                    @Override
                    public void failure(RetrofitError error) {
                        Timber.d("retro failure error = " + error);
                        if (!error.getKind().equals(RetrofitError.Kind.NETWORK)) {
                            switch (error.getResponse().getStatus()) {
                                case 500:
                                    new CustomDialogs().showWarningDialog(
                                            weakActivity.get(),
                                            weakActivity.get().getString(R.string.no_search_results_found));
                                    break;
                            }
                        }
                        pd.dismiss();
                    }
                }
        );
    }

    @Override
    public void postAppointment(final String returnType, final PostingData appointment, final String priority, final AlertDialog ad) {
        final ProgressDialog pd = new CustomDialogs().showProgressDialog(
                weakActivity.get(),
                weakActivity.get().getString(R.string.booking_appointment));

        SmartApiClient.getAuthorizedApiClient(realm).postAppointment(
                appointment,
                new Callback<BaseResponseModel>() {
                    @Override
                    public void success(BaseResponseModel baseResponseModel, Response response) {
                        Timber.d("postAppointment success");
                        baseModel.updateAppointment(baseResponseModel.getAppointment());

                        if (returnType.equals(Constants.ARGS_NEW)) {
                            getAppointmentById(baseResponseModel.getAppointment().getId(), priority, ad, pd);
                        } else {
                            if (ad.isShowing())
                                ad.cancel();

                            if (pd.isShowing())
                                pd.dismiss();

                            switch (priority) {
                                case Constants.ARGS_HOME_VISIT:
                                    createAppointmentView.gotoHomeVisitAppointment();
                                    break;
                                case Constants.ARGS_SCHEDULED:
                                    createAppointmentView.gotoClinicAppointment();
                                    break;
                                case Constants.ARGS_DROP_IN:
                                    createAppointmentView.gotoClinicAppointment();
                                    break;
                            }
                        }
                    }

                    @Override
                    public void failure(RetrofitError error) {
                        Timber.d("retro failure error = " + error);
                        checkRetroError(error, weakActivity.get());
                        if (error.getKind() != RetrofitError.Kind.NETWORK) {
                            if (error.getResponse().getStatus() == 422) {
                                BaseResponseModel body = (BaseResponseModel) error.getBodyAs(BaseResponseModel.class);
                                Toast.makeText(weakActivity.get(),
                                        body.getError().getAppointmentTaken(), Toast.LENGTH_LONG).show();
                            }
                        } else {
                            String time = Constants.DF_TIME_W_SEC.format(Calendar.getInstance().getTime());
                            String prefsTag = String.format(Constants.FORMAT_PREFS_APPT_POST, time);
                            SharedPrefs prefsUstil = new SharedPrefs();
                            prefsUstil.setJsonPrefs(weakActivity.get(), appointment, prefsTag);
                            Toast.makeText(weakActivity.get(),
                                    weakActivity.get().getString(R.string.error_no_internet_booking_appointment),
                                    Toast.LENGTH_LONG).show();
                        }
                        if (ad.isShowing())
                            ad.cancel();
                        if (pd.isShowing())
                            pd.dismiss();
                    }
                }
        );
    }

    @Override
    public void getAppointmentById(int apptId, final String priority, final AlertDialog ad, final ProgressDialog pd) {
        SmartApiClient.getAuthorizedApiClient(weakActivity.get()).getAppointmentById(
                apptId + 1,
                new Callback<BaseResponseModel>() {
                    @Override
                    public void success(BaseResponseModel baseResponseModel, Response response) {
                        Timber.d("getAppointmentById success");
                        baseModel.updateAppointment(baseResponseModel.getAppointment());

                        if (ad.isShowing())
                            ad.cancel();

                        if (pd.isShowing())
                            pd.dismiss();

                        switch (priority) {
                            case Constants.ARGS_HOME_VISIT:
                                createAppointmentView.gotoHomeVisitAppointment();
                                break;
                            case Constants.ARGS_SCHEDULED:
                                createAppointmentView.gotoClinicAppointment();
                                break;
                            case Constants.ARGS_DROP_IN:
                                createAppointmentView.gotoClinicAppointment();
                                break;
                        }
                    }

                    @Override
                    public void failure(RetrofitError error) {
                        Timber.d("getAppointmentById failure = " + error);
                        pd.dismiss();

                        if (ad.isShowing())
                            ad.cancel();

                        if (pd.isShowing())
                            pd.dismiss();
                    }
                }
        );
    }

    @Override
    public ServiceUser getServiceUser(int position) {
        return baseModel.getServiceUsers().get(position);
    }

    @Override
    public int getLogingId() {
        return baseModel.getLogin().getId();
    }
}
