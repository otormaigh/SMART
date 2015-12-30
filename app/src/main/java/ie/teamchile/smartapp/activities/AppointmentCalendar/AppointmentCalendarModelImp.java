package ie.teamchile.smartapp.activities.AppointmentCalendar;

import ie.teamchile.smartapp.activities.Base.BaseModelImp;
import ie.teamchile.smartapp.model.Appointment;
import io.realm.Realm;

/**
 * Created by elliot on 30/12/2015.
 */
public class AppointmentCalendarModelImp extends BaseModelImp implements AppointmentCalendarModel {
    private Realm realm;

    public AppointmentCalendarModelImp(AppointmentCalendarPresenter appointmentCalendarPresenter) {
        super(appointmentCalendarPresenter);
        realm = appointmentCalendarPresenter.getEncryptedRealm();
    }
    @Override
    public void saveAppointmentToRealm(Appointment appointment) {
        realm.beginTransaction();
        realm.copyToRealmOrUpdate(appointment);
        realm.commitTransaction();
    }
}
