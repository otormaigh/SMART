package ie.teamchile.smartapp.activities.AppointmentCalendar;

import ie.teamchile.smartapp.activities.Base.BaseModel;
import ie.teamchile.smartapp.model.Appointment;

/**
 * Created by elliot on 30/12/2015.
 */
public interface AppointmentCalendarModel extends BaseModel {
    void saveAppointmentToRealm(Appointment appointment);
}
