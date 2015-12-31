package ie.teamchile.smartapp.activities.AppointmentCalendar;

import android.app.Activity;
import android.app.ProgressDialog;
import android.widget.Toast;

import java.lang.ref.WeakReference;

import ie.teamchile.smartapp.activities.Base.BaseModel;
import ie.teamchile.smartapp.activities.Base.BaseModelImp;
import ie.teamchile.smartapp.activities.Base.BasePresenterImp;
import ie.teamchile.smartapp.api.SmartApiClient;
import ie.teamchile.smartapp.model.BaseResponseModel;
import ie.teamchile.smartapp.model.Login;
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
    private AppointmentCalendarView appointmentCalendarView;
    private BaseModel baseModel;
    private WeakReference<Activity> weakActivity;
    private Realm realm;

    public AppointmentCalendarPresenterImp(AppointmentCalendarView appointmentCalendarView, WeakReference<Activity> weakActivity) {
        this.appointmentCalendarView = appointmentCalendarView;
        this.weakActivity = weakActivity;
        baseModel = new BaseModelImp(this);
        realm = getEncryptedRealm();
    }

    @Override
    public void changeAttendStatus(boolean status, int position, int clinicSelected, int serviceUserId, int appointmentId) {
        final ProgressDialog pd = new CustomDialogs()
                .showProgressDialog(
                        weakActivity.get(),
                        "Changing Attended Status");

        PostingData attendedStatus = new PostingData();
        attendedStatus.putAppointmentStatus(
                status,
                clinicSelected,
                realm.where(Login.class).findFirst().getId(),
                serviceUserId);

        SmartApiClient.getAuthorizedApiClient(realm).putAppointmentStatus(
                attendedStatus,
                appointmentId,
                new Callback<BaseResponseModel>() {
                    @Override
                    public void success(BaseResponseModel baseResponseModel, Response response) {
                        Toast.makeText(weakActivity.get(),
                                "Status changed", Toast.LENGTH_LONG).show();

                        baseModel.saveAppointmentToRealm(baseResponseModel.getAppointment());

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
                        "Fetching Information");

        SmartApiClient.getAuthorizedApiClient(realm).getServiceUserById(serviceUserId,
                new Callback<BaseResponseModel>() {
                    @Override
                    public void success(BaseResponseModel baseResponseModel, Response response) {
                        baseModel.saveServiceUserToRealm(baseResponseModel);
                        appointmentCalendarView.gotoServiceUserActivity();

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
}
