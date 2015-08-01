package ie.teamchile.smartapp.model;

/**
 * Created by user on 5/27/15.
 */
public class Error {
    private String Error;
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
