package ie.teamchile.smartapp.model;

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

    public Boolean getFriday() {
        return friday;
    }

    public void setFriday(Boolean friday) {
        this.friday = friday;
    }

    public Boolean getMonday() {
        return monday;
    }

    public void setMonday(Boolean monday) {
        this.monday = monday;
    }

    public Boolean getSaturday() {
        return saturday;
    }

    public void setSaturday(Boolean saturday) {
        this.saturday = saturday;
    }

    public Boolean getSunday() {
        return sunday;
    }

    public void setSunday(Boolean sunday) {
        this.sunday = sunday;
    }

    public Boolean getThursday() {
        return thursday;
    }

    public void setThursday(Boolean thursday) {
        this.thursday = thursday;
    }

    public Boolean getTuesday() {
        return tuesday;
    }

    public void setTuesday(Boolean tuesday) {
        this.tuesday = tuesday;
    }

    public Boolean getWednesday() {
        return wednesday;
    }

    public void setWednesday(Boolean wednesday) {
        this.wednesday = wednesday;
    }

}