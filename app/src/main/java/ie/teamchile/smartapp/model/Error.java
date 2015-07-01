package ie.teamchile.smartapp.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by user on 5/27/15.
 */
public class Error {
    @Expose
    private String Error;
    @SerializedName("Appointment taken")
    @Expose
    private String AppointmentTaken;

    public String getError() {
        return Error;
    }

    public void setError(String Error) {
        this.Error = Error;
    }

    public String getAppointmentTaken() {
        return AppointmentTaken;
    }

    public void setAppointmentTaken(String AppointmentTaken) {
        this.AppointmentTaken = AppointmentTaken;
    }
}
