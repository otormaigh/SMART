package ie.teamchile.smartapp.activities.AppointmentTypeSpinner;

import java.util.Date;

import ie.teamchile.smartapp.activities.Base.BasePresenter;

/**
 * Created by elliot on 30/12/2015.
 */
public interface AppointmentTypeSpinnerPresenter extends BasePresenter {
    void getAppointment(int clinicId, Date dayOfWeek);
}
