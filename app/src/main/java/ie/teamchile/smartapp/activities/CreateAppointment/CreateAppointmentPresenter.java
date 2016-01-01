package ie.teamchile.smartapp.activities.CreateAppointment;

import android.app.AlertDialog;
import android.app.ProgressDialog;

import ie.teamchile.smartapp.activities.Base.BasePresenter;
import ie.teamchile.smartapp.model.PostingData;
import ie.teamchile.smartapp.model.ServiceUser;

/**
 * Created by elliot on 01/01/2016.
 */
public interface CreateAppointmentPresenter extends BasePresenter {
    void searchPatient(String serviceUserName);

    void getAppointmentById(int apptId, String priority, AlertDialog ad, ProgressDialog pd);

    void postAppointment(String returnType, PostingData appointment, String priority, AlertDialog ad);

    ServiceUser getServiceUser(int position);

    int getLogingId();
}
