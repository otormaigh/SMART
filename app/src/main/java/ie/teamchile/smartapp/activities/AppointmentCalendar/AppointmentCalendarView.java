package ie.teamchile.smartapp.activities.AppointmentCalendar;

import java.util.Date;

import ie.teamchile.smartapp.activities.Base.BaseViewSec;

/**
 * Created by elliot on 30/12/2015.
 */
public interface AppointmentCalendarView extends BaseViewSec {
    void gotoServiceUserActivity();

    void getAppointmentListForDay(Date dateSelected);
}
