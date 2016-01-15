package ie.teamchile.smartapp.activities.HomeVisitAppointment;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.widget.Toast;

import java.lang.ref.WeakReference;

import ie.teamchile.smartapp.activities.Base.BaseModel;
import ie.teamchile.smartapp.activities.Base.BaseModelImp;
import ie.teamchile.smartapp.activities.Base.BasePresenterImp;
import ie.teamchile.smartapp.api.SmartApiClient;
import ie.teamchile.smartapp.model.ResponseBase;
import ie.teamchile.smartapp.model.PostingData;
import ie.teamchile.smartapp.util.CustomDialogs;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;
import timber.log.Timber;

/**
 * Created by elliot on 01/01/2016.
 */
public class HomeVisitAppointmentPresenterImp extends BasePresenterImp implements HomeVisitAppointmentPresenter {
    private HomeVisitAppointmentView view;
    private WeakReference<Activity> weakActivity;
    private BaseModel model;
    private ProgressDialog pd;

    public HomeVisitAppointmentPresenterImp(HomeVisitAppointmentView view, WeakReference<Activity> weakActivity) {
        this.view = view;
        this.weakActivity = weakActivity;
        model = new BaseModelImp(this);
    }

    @Override
    public void changeAttendStatus(boolean status, int appointmentId) {
        pd = new CustomDialogs().showProgressDialog(
                weakActivity.get(),
                "Changing Attended Status");

        PostingData attendedStatus = new PostingData();
        attendedStatus.putAppointmentStatus(
                status,
                0,
                model.getLogin().getId(),
                model.getAppointmentById(appointmentId).getServiceUserId());

        SmartApiClient.getAuthorizedApiClient(getEncryptedRealm()).putAppointmentStatus(
                attendedStatus,
                appointmentId,
                new Callback<ResponseBase>() {
                    @Override
                    public void success(ResponseBase responseBase, Response response) {
                        Timber.d("changeAttendStatus success");
                        model.updateAppointment(responseBase.getAppointment());

                        Toast.makeText(weakActivity.get(),
                                "Status changed", Toast.LENGTH_LONG).show();
                        pd.dismiss();
                    }

                    @Override
                    public void failure(RetrofitError error) {
                        Timber.d("changeAttendStatus error = " + error);
                        pd.dismiss();
                    }
                }
        );
    }

    @Override
    public void searchServiceUser(int serviceUserId, final Intent intent) {
        pd = new CustomDialogs().showProgressDialog(
                weakActivity.get(),
                "Fetching Information");

        SmartApiClient.getAuthorizedApiClient(getEncryptedRealm()).getServiceUserById(serviceUserId,
                new Callback<ResponseBase>() {
                    @Override
                    public void success(ResponseBase responseBase, Response response) {
                        model.updateServiceUsers(responseBase);
                        weakActivity.get().startActivity(intent);
                        weakActivity.get().finish();
                        pd.dismiss();
                    }

                    @Override
                    public void failure(RetrofitError error) {
                        pd.dismiss();
                        Toast.makeText(
                                weakActivity.get(),
                                "Error Search Patient: " + error,
                                Toast.LENGTH_LONG).show();
                    }
                });
    }

    @Override
    public void dismissProgressDialog() {
        if (pd != null)
            pd.dismiss();
    }
}
