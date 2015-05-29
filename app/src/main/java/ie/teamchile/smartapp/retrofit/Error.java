package ie.teamchile.smartapp.retrofit;

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

    /**
     * @return The Error
     */
    public String getError() {
        return Error;
    }

    /**
     * @param Error The Error
     */
    public void setError(String Error) {
        this.Error = Error;
    }

    /**
     * @return The AppointmentTaken
     */
    public String getAppointmentTaken() {
        return AppointmentTaken;
    }

    /**
     * @param AppointmentTaken The Appointment taken
     */
    public void setAppointmentTaken(String AppointmentTaken) {
        this.AppointmentTaken = AppointmentTaken;
    }
}
