package ie.teamchile.smartapp.model;

/**
 * Created by user on 5/27/15.
 */
public class ResponseError {
    private String error;
    private String appointmentTaken;

    public String getError() {
        return error;
    }

    public void setError(String Error) {
        this.error = error;
    }

    public String getAppointmentTaken() {
        return appointmentTaken;
    }

    public void setAppointmentTaken(String appointmentTaken) {
        this.appointmentTaken = appointmentTaken;
    }
}
