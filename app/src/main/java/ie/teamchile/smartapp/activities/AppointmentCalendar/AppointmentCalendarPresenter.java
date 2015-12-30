package ie.teamchile.smartapp.activities.AppointmentCalendar;

import ie.teamchile.smartapp.activities.Base.BasePresenter;

/**
 * Created by elliot on 30/12/2015.
 */
public interface AppointmentCalendarPresenter extends BasePresenter {
    void changeAttendStatus(boolean status, int position, int clinicSelected, int serviceUserId, int appointmentId);

    void searchServiceUser(int serviceUserId);
}
