package ie.teamchile.smartapp.activities.HomeVisitAppointment;

import android.content.Intent;

import ie.teamchile.smartapp.activities.Base.BasePresenter;

/**
 * Created by elliot on 01/01/2016.
 */
public interface HomeVisitAppointmentPresenter extends BasePresenter {
    void changeAttendStatus(boolean status, int appointmentId);

    void searchServiceUser(int serviceUserId, final Intent intent);

    void dismissProgressDialog();
}
