package ie.teamchile.smartapp.model;

/**
 * Created by user on 5/27/15.
 */
public class ClinicalFields {
    private String bloodGroup;
    private String bmi;
    private String parity;
    private String previousObstetricHistory;
    private Boolean rhesus;

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

    public Boolean getRhesus() {
        return rhesus;
    }

    public void setRhesus(Boolean rhesus) {
        this.rhesus = rhesus;
    }
}
