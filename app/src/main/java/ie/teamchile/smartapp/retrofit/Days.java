package ie.teamchile.smartapp.retrofit;

import com.google.gson.annotations.Expose;

/**
 * Created by user on 5/26/15.
 */
public class Days {

    @Expose
    private Boolean friday;
    @Expose
    private Boolean monday;
    @Expose
    private Boolean saturday;
    @Expose
    private Boolean sunday;
    @Expose
    private Boolean thursday;
    @Expose
    private Boolean tuesday;
    @Expose
    private Boolean wednesday;

    /**
     * @return The friday
     */
    public Boolean getFriday() {
        return friday;
    }

    /**
     * @param friday The friday
     */
    public void setFriday(Boolean friday) {
        this.friday = friday;
    }

    /**
     * @return The monday
     */
    public Boolean getMonday() {
        return monday;
    }

    /**
     * @param monday The monday
     */
    public void setMonday(Boolean monday) {
        this.monday = monday;
    }

    /**
     * @return The saturday
     */
    public Boolean getSaturday() {
        return saturday;
    }

    /**
     * @param saturday The saturday
     */
    public void setSaturday(Boolean saturday) {
        this.saturday = saturday;
    }

    /**
     * @return The sunday
     */
    public Boolean getSunday() {
        return sunday;
    }

    /**
     * @param sunday The sunday
     */
    public void setSunday(Boolean sunday) {
        this.sunday = sunday;
    }

    /**
     * @return The thursday
     */
    public Boolean getThursday() {
        return thursday;
    }

    /**
     * @param thursday The thursday
     */
    public void setThursday(Boolean thursday) {
        this.thursday = thursday;
    }

    /**
     * @return The tuesday
     */
    public Boolean getTuesday() {
        return tuesday;
    }

    /**
     * @param tuesday The tuesday
     */
    public void setTuesday(Boolean tuesday) {
        this.tuesday = tuesday;
    }

    /**
     * @return The wednesday
     */
    public Boolean getWednesday() {
        return wednesday;
    }

    /**
     * @param wednesday The wednesday
     */
    public void setWednesday(Boolean wednesday) {
        this.wednesday = wednesday;
    }
}