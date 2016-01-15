package ie.teamchile.smartapp.activities.AppointmentTypeSpinner;

import android.app.Activity;

import java.lang.ref.WeakReference;
import java.util.Calendar;
import java.util.Date;

import ie.teamchile.smartapp.activities.Base.BaseActivity;
import ie.teamchile.smartapp.activities.Base.BasePresenterImp;
import ie.teamchile.smartapp.util.AppointmentHelper;
import ie.teamchile.smartapp.util.SharedPrefs;

/**
 * Created by elliot on 30/12/2015.
 */
public class AppointmentTypeSpinnerPresenterImp extends BasePresenterImp implements AppointmentTypeSpinnerPresenter {
    private AppointmentTypeSpinnerView view;
    private WeakReference<Activity> weakActivity;

    public AppointmentTypeSpinnerPresenterImp(AppointmentTypeSpinnerView view,
                                              WeakReference<Activity> weakActivity) {
        super(weakActivity);
        this.view = view;
        this.weakActivity = weakActivity;
    }

    @Override
    public void getAppointment(int clinicId, Date dayOfWeek) {
        SharedPrefs sharedPrefs = new SharedPrefs();
        AppointmentHelper apptHelp = new AppointmentHelper(getEncryptedRealm());
        Calendar myCal = Calendar.getInstance();
        myCal.setTime(dayOfWeek);
        int dayAsInt = myCal.get(Calendar.DAY_OF_WEEK);
        myCal = Calendar.getInstance();
        myCal.set(Calendar.DAY_OF_WEEK, dayAsInt);
        Date todayDate = myCal.getTime();

        if (!sharedPrefs.getStringSetPrefs(weakActivity.get(),
                "appts_got").contains(String.valueOf(clinicId))) {
            BaseActivity.apptDone = 0;
            apptHelp.weekDateLooper(todayDate, clinicId);
            sharedPrefs.addToStringSetPrefs(weakActivity.get(),
                    "appts_got", String.valueOf(clinicId));
        }
    }
}
