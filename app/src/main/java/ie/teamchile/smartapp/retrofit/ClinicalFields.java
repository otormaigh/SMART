package ie.teamchile.smartapp.retrofit;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by user on 5/27/15.
 */
public class ClinicalFields {
    @SerializedName("blood_group")
    @Expose
    private String bloodGroup;
    @Expose
    private String bmi;
    @Expose
    private String parity;
    @SerializedName("previous_obstetric_history")
    @Expose
    private String previousObstetricHistory;
    @Expose
    private Boolean rhesus;

    /**
     * @return The bloodGroup
     */
    public String getBloodGroup() {
        return bloodGroup;
    }

    /**
     * @param bloodGroup The blood_group
     */
    public void setBloodGroup(String bloodGroup) {
        this.bloodGroup = bloodGroup;
    }

    /**
     * @return The bmi
     */
    public String getBmi() {
        return bmi;
    }

    /**
     * @param bmi The bmi
     */
    public void setBmi(String bmi) {
        this.bmi = bmi;
    }

    /**
     * @return The parity
     */
    public String getParity() {
        return parity;
    }

    /**
     * @param parity The parity
     */
    public void setParity(String parity) {
        this.parity = parity;
    }

    /**
     * @return The previousObstetricHistory
     */
    public String getPreviousObstetricHistory() {
        return previousObstetricHistory;
    }

    /**
     * @param previousObstetricHistory The previous_obstetric_history
     */
    public void setPreviousObstetricHistory(String previousObstetricHistory) {
        this.previousObstetricHistory = previousObstetricHistory;
    }

    /**
     * @return The rhesus
     */
    public Boolean getRhesus() {
        return rhesus;
    }

    /**
     * @param rhesus The rhesus
     */
    public void setRhesus(Boolean rhesus) {
        this.rhesus = rhesus;
    }
}
