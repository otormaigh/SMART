package ie.teamchile.smartapp.activities.AppointmentTypeSpinner;

import android.app.Activity;

import java.lang.ref.WeakReference;

import ie.teamchile.smartapp.activities.Base.BasePresenterImp;

/**
 * Created by elliot on 30/12/2015.
 */
public class AppointmentTypeSpinnerPresenterImp extends BasePresenterImp implements AppointmentTypeSpinnerPresenter {
    private AppointmentTypeSpinnerView appointmentTypeSpinnerView;

    public AppointmentTypeSpinnerPresenterImp(AppointmentTypeSpinnerView appointmentTypeSpinnerView,
                                              WeakReference<Activity> weakActivity) {
        super(weakActivity);
        this.appointmentTypeSpinnerView = appointmentTypeSpinnerView;
    }
}
