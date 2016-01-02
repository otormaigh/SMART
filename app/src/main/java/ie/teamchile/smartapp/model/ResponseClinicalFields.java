package ie.teamchile.smartapp.model;

import io.realm.RealmObject;

/**
 * Created by user on 5/27/15.
 */
public class ResponseClinicalFields extends RealmObject {
    private String bloodGroup;
    private String bmi;
    private String parity;
    private String previousObstetricHistory;
    private boolean rhesus;

    public String getBloodGroup() {
        return bloodGroup;
    }

    public void setBloodGroup(String bloodGroup) {
        this.bloodGroup = bloodGroup;
    }

    public String getBmi() {
        return bmi;
    }

    public void setBmi(String bmi) {
        this.bmi = bmi;
    }

    public String getParity() {
        return parity;
    }

    public void setParity(String parity) {
        this.parity = parity;
    }

    public String getPreviousObstetricHistory() {
        return previousObstetricHistory;
    }

    public void setPreviousObstetricHistory(String previousObstetricHistory) {
        this.previousObstetricHistory = previousObstetricHistory;
    }

    public boolean isRhesus() {
        return rhesus;
    }

    public void setRhesus(boolean rhesus) {
        this.rhesus = rhesus;
    }
}
