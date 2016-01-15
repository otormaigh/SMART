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
import ie.teamchile.smartapp.model.PostingData;
import ie.teamchile.smartapp.model.ResponseBase;
import ie.teamchile.smartapp.model.ResponseServiceUser;
import ie.teamchile.smartapp.util.Constants;
import ie.teamchile.smartapp.util.CustomDialogs;
import ie.teamchile.smartapp.util.SharedPrefs;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;
import timber.log.Timber;

/**
 * Created by elliot on 01/01/2016.
 */
public class CreateAppointmentPresenterImp extends BasePresenterImp implements CreateAppointmentPresenter {
    private CreateAppointmentView view;
    private WeakReference<Activity> weakActivity;
    private BaseModel model;

    public CreateAppointmentPresenterImp(CreateAppointmentView view, WeakReference<Activity> weakActivity) {
        this.view = view;
        this.weakActivity = weakActivity;
        model = new BaseModelImp(this, weakActivity);
    }

    @Override
    public void searchPatient(String serviceUserName) {
        final ProgressDialog pd = new CustomDialogs().showProgressDialog(
                weakActivity.get(),
                weakActivity.get().getString(R.string.fetching_information));

        SmartApiClient.getAuthorizedApiClient(getEncryptedRealm()).getServiceUserByName(
                serviceUserName,
                new Callback<ResponseBase>() {
                    @Override
                    public void success(ResponseBase responseBase, Response response) {
                        Timber.d("searchPatient success");
                        if (responseBase.getResponseServiceUsers().size() != 0) {
                            model.updateServiceUsers(responseBase);
                            pd.dismiss();
                            view.userSearchDialog(
                                    weakActivity.get().getString(R.string.search_results),
                                    model.getServiceUsers());
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

        SmartApiClient.getAuthorizedApiClient(getEncryptedRealm()).postAppointment(
                appointment,
                new Callback<ResponseBase>() {
                    @Override
                    public void success(ResponseBase responseBase, Response response) {
                        Timber.d("postAppointment success");
                        model.updateAppointment(responseBase.getAppointment());

                        if (returnType.equals(Constants.ARGS_NEW)) {
                            getAppointmentById(responseBase.getAppointment().getId(), priority, ad, pd);
                        } else {
                            if (ad.isShowing())
                                ad.cancel();

                            if (pd.isShowing())
                                pd.dismiss();

                            switch (priority) {
                                case Constants.ARGS_HOME_VISIT:
                                    view.gotoHomeVisitAppointment();
                                    break;
                                case Constants.ARGS_SCHEDULED:
                                    view.gotoClinicAppointment();
                                    break;
                                case Constants.ARGS_DROP_IN:
                                    view.gotoClinicAppointment();
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
                                ResponseBase body = (ResponseBase) error.getBodyAs(ResponseBase.class);
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
        SmartApiClient.getAuthorizedApiClient(getEncryptedRealm()).getAppointmentById(
                apptId + 1,
                new Callback<ResponseBase>() {
                    @Override
                    public void success(ResponseBase responseBase, Response response) {
                        Timber.d("getAppointmentById success");
                        model.updateAppointment(responseBase.getAppointment());

                        if (ad.isShowing())
                            ad.cancel();

                        if (pd.isShowing())
                            pd.dismiss();

                        switch (priority) {
                            case Constants.ARGS_HOME_VISIT:
                                view.gotoHomeVisitAppointment();
                                break;
                            case Constants.ARGS_SCHEDULED:
                                view.gotoClinicAppointment();
                                break;
                            case Constants.ARGS_DROP_IN:
                                view.gotoClinicAppointment();
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
    public ResponseServiceUser getServiceUser(int position) {
        return model.getServiceUsers().get(position);
    }

    @Override
    public int getLogingId() {
        return model.getLogin().getId();
    }
}
