package ie.teamchile.smartapp.activities.AppointmentCalendar;

import android.app.Activity;
import android.app.ProgressDialog;
import android.widget.Toast;

import java.lang.ref.WeakReference;

import ie.teamchile.smartapp.R;
import ie.teamchile.smartapp.activities.Base.BaseModel;
import ie.teamchile.smartapp.activities.Base.BaseModelImp;
import ie.teamchile.smartapp.activities.Base.BasePresenterImp;
import ie.teamchile.smartapp.api.SmartApiClient;
import ie.teamchile.smartapp.model.ResponseBase;
import ie.teamchile.smartapp.model.ResponseLogin;
import ie.teamchile.smartapp.model.PostingData;
import ie.teamchile.smartapp.util.CustomDialogs;
import io.realm.Realm;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;
import timber.log.Timber;

/**
 * Created by elliot on 30/12/2015.
 */
public class AppointmentCalendarPresenterImp extends BasePresenterImp implements AppointmentCalendarPresenter {
    private AppointmentCalendarView view;
    private BaseModel model;
    private WeakReference<Activity> weakActivity;
    private Realm realm;

    public AppointmentCalendarPresenterImp(AppointmentCalendarView view, WeakReference<Activity> weakActivity) {
        this.view = view;
        this.weakActivity = weakActivity;
        model = new BaseModelImp(this);
        realm = getEncryptedRealm();
    }

    @Override
    public void changeAttendStatus(boolean status, int position, int clinicSelected, int serviceUserId, int appointmentId) {
        final ProgressDialog pd = new CustomDialogs()
                .showProgressDialog(
                        weakActivity.get(),
                        weakActivity.get().getString(R.string.changing_attended_status));

        PostingData attendedStatus = new PostingData();
        attendedStatus.putAppointmentStatus(
                status,
                clinicSelected,
                realm.where(ResponseLogin.class).findFirst().getId(),
                serviceUserId);

        SmartApiClient.getAuthorizedApiClient(realm).putAppointmentStatus(
                attendedStatus,
                appointmentId,
                new Callback<ResponseBase>() {
                    @Override
                    public void success(ResponseBase responseBase, Response response) {
                        Toast.makeText(weakActivity.get(),
                                weakActivity.get().getString(R.string.status_changed),
                                Toast.LENGTH_LONG).show();

                        model.saveAppointmentToRealm(responseBase.getAppointment());

                        pd.dismiss();
                    }

                    @Override
                    public void failure(RetrofitError error) {
                        Timber.d("retro error = " + error);
                        pd.dismiss();
                    }
                }
        );
    }

    @Override
    public void searchServiceUser(int serviceUserId) {
        final ProgressDialog pd = new CustomDialogs()
                .showProgressDialog(
                        weakActivity.get(),
                        weakActivity.get().getString(R.string.fetching_information));

        SmartApiClient.getAuthorizedApiClient(realm).getServiceUserById(serviceUserId,
                new Callback<ResponseBase>() {
                    @Override
                    public void success(ResponseBase responseBase, Response response) {
                        model.updateServiceUsers(responseBase);
                        view.gotoServiceUserActivity();

                        pd.dismiss();
                    }

                    @Override
                    public void failure(RetrofitError error) {
                        pd.dismiss();
                        Toast.makeText(
                                weakActivity.get(),
                                String.format(weakActivity.get().getString(R.string.error_search_patient), error),
                                Toast.LENGTH_LONG).show();
                    }
                });
    }
}
