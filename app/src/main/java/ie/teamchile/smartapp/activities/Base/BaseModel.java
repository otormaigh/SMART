package ie.teamchile.smartapp.activities.Base;

import java.util.List;

import ie.teamchile.smartapp.model.Appointment;
import ie.teamchile.smartapp.model.BaseResponseModel;

/**
 * Created by elliot on 28/12/2015.
 */
public interface BaseModel {
    void saveAppointmentsToRealm(List<Appointment> appointments);

    void saveServiceUserToRealm(BaseResponseModel baseResponseModel);
}
