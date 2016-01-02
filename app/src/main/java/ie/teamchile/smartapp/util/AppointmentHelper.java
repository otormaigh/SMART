package ie.teamchile.smartapp.util;

import android.util.Log;

import java.util.Calendar;
import java.util.Date;

import ie.teamchile.smartapp.activities.Base.BaseActivity;
import ie.teamchile.smartapp.api.SmartApiClient;
import ie.teamchile.smartapp.model.ResponseBase;
import io.realm.Realm;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * Created by user on 7/14/15.
 */
public class AppointmentHelper extends BaseActivity {
    private Realm realm;

    public AppointmentHelper(Realm realm) {
        this.realm = realm;
    }

    public void weekDateLooper(Date todayDate, int clinicId) {
        Log.d("MYLOG", "getAppointmentsForClinic called");
        Log.d("MYLOG", "todayDate = " + Constants.DF_DATE_ONLY.format(todayDate) + ", clinicId = " + clinicId);
        Calendar c = Calendar.getInstance();
        //Date todayDate = c.getTime();
        c.setTime(todayDate);
        String today = Constants.DF_DATE_ONLY.format(c.getTime());
        c.add(Calendar.WEEK_OF_YEAR, 10);
        Date todayPlus10Weeks = c.getTime();

        while (todayDate.before(todayPlus10Weeks)) {
            Log.d("MYLOG", "todayDate = " + c.getTime());
            c.setTime(todayDate);
            String date = Constants.DF_DATE_ONLY.format(c.getTime());
            c.add(Calendar.WEEK_OF_YEAR, 1);
            todayDate = c.getTime();

            getAppointmentsForClinic(date, clinicId);
        }
    }

    public void getAppointmentsForClinic(String date, int clinicId) {
        SmartApiClient.getAuthorizedApiClient(realm).getAppointmentsForDayClinic(
                date,
                clinicId,
                new Callback<ResponseBase>() {
                    @Override
                    public void success(ResponseBase responseBase, Response response) {
                        Log.d("retro", "getAppointmentsForClinic success");
                        realm.beginTransaction();
                        realm.copyToRealmOrUpdate(responseBase.getAppointments());
                        realm.commitTransaction();

                        BaseActivity.apptDone++;
                    }

                    @Override
                    public void failure(RetrofitError error) {
                        Log.d("retro", "getAppointmentsForClinic failure = " + error);
                        BaseActivity.apptDone++;
                    }
                }
        );
    }

    public void getAppointmentsHomeVisit(String date, int serviceOptionId) {
        SmartApiClient.getAuthorizedApiClient(realm).getHomeVisitApptByDateId(
                "home-visit",
                date,
                serviceOptionId,
                new Callback<ResponseBase>() {
                    @Override
                    public void success(ResponseBase responseBase, Response response) {
                        Log.d("retro", "getAppointmentsForClinic success");
                        realm.beginTransaction();
                        realm.copyToRealmOrUpdate(responseBase.getAppointments());
                        realm.commitTransaction();

                        BaseActivity.apptDone++;
                    }

                    @Override
                    public void failure(RetrofitError error) {
                        Log.d("retro", "getAppointmentsForClinic failure = " + error);
                        BaseActivity.apptDone++;
                    }
                }
        );
    }
}
