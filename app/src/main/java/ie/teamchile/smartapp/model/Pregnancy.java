package ie.teamchile.smartapp.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by user on 5/26/15.
 */
public class Pregnancy {
    @SerializedName("additional_info")
    @Expose
    private String additionalInfo;
    @SerializedName("anti_d")
    @Expose
    private String antiD;
    @SerializedName("baby_ids")
    @Expose
    private List<Integer> babyIds = new ArrayList<Integer>();
    @SerializedName("birth_mode")
    @Expose
    private List<String> birthMode = new ArrayList<String>();
    @SerializedName("created_at")
    @Expose
    private String createdAt;
    @SerializedName("estimated_delivery_date")
    @Expose
    private String estimatedDeliveryDate;
    @Expose
    private String feeding;
    @Expose
    private String gestation;
    @SerializedName("baby_age")
    @Expose
    private String babyAge;
    @Expose
    private Integer id;
    @SerializedName("last_menstrual_period")
    @Expose
    private String lastMenstrualPeriod;
    @Expose
    private String perineum;
    @SerializedName("service_user_id")
    @Expose
    private Integer serviceUserId;
    @SerializedName("pregnancy_notes")
    @Expose
    private List<PregnancyNote> pregnancyNotes = new ArrayList<>();


    /**
     * @return The additionalInfo
     */
    public String getAdditionalInfo() {
        return additionalInfo;
    }

    /**
     * @param additionalInfo The additional_info
     */
    public void setAdditionalInfo(String additionalInfo) {
        this.additionalInfo = additionalInfo;
    }

    /**
     * @return The antiD
     */
    public String getAntiD() {
        return antiD;
    }

    /**
     * @param antiD The anti_d
     */
    public void setAntiD(String antiD) {
        this.antiD = antiD;
    }

    /**
     * @return The babyIds
     */
    public List<Integer> getBabyIds() {
        return babyIds;
    }

    /**
     * @param babyIds The baby_ids
     */
    public void setBabyIds(List<Integer> babyIds) {
        this.babyIds = babyIds;
    }

    /**
     * @return The birthMode
     */
    public List<String> getBirthMode() {
        return birthMode;
    }

    /**
     * @param birthMode The birth_mode
     */
    public void setBirthMode(List<String> birthMode) {
        this.birthMode = birthMode;
    }

    /**
     * @return The createdAt
     */
    public String getCreatedAt() {
        return createdAt;
    }

    /**
     * @param createdAt The created_at
     */
    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    /**
     * @return The estimatedDeliveryDate
     */
    public String getEstimatedDeliveryDate() {
        return estimatedDeliveryDate;
    }

    /**
     * @param estimatedDeliveryDate The estimated_delivery_date
     */
    public void setEstimatedDeliveryDate(String estimatedDeliveryDate) {
        this.estimatedDeliveryDate = estimatedDeliveryDate;
    }

    /**
     * @return The feeding
     */
    public String getFeeding() {
        return feeding;
    }

    /**
     * @param feeding The feeding
     */
    public void setFeeding(String feeding) {
        this.feeding = feeding;
    }

    /**
     * @return The gestation
     */
    public String getGestation() {
        return gestation;
    }

    /**
     * @param gestation The gestation
     */
    public void setGestation(String gestation) {
        this.gestation = gestation;
    }

    public String getBabyAge() {
        return babyAge;
    }

    public void setBabyAge(String babyAge) {
        this.babyAge = babyAge;
    }

    /**
     * @return The id
     */
    public Integer getId() {
        return id;
    }

    /**
     * @param id The id
     */
    public void setId(Integer id) {
        this.id = id;
    }

    /**
     * @return The lastMenstrualPeriod
     */
    public String getLastMenstrualPeriod() {
        return lastMenstrualPeriod;
    }

    /**
     * @param lastMenstrualPeriod The last_menstrual_period
     */
    public void setLastMenstrualPeriod(String lastMenstrualPeriod) {
        this.lastMenstrualPeriod = lastMenstrualPeriod;
    }

    /**
     * @return The perineum
     */
    public String getPerineum() {
        return perineum;
    }

    /**
     * @param perineum The perineum
     */
    public void setPerineum(String perineum) {
        this.perineum = perineum;
    }

    /**
     * @return The serviceUserId
     */
    public Integer getServiceUserId() {
        return serviceUserId;
    }

    /**
     * @param serviceUserId The service_user_id
     */
    public void setServiceUserId(Integer serviceUserId) {
        this.serviceUserId = serviceUserId;
    }

    /**
     * @return The pregnancyNotes
     */
    public List<PregnancyNote> getPregnancyNotes() {
        return pregnancyNotes;
    }

    /**
     * @param pregnancyNotes The pregnancy_notes
     */
    public void setPregnancyNotes(List<PregnancyNote> pregnancyNotes) {
        this.pregnancyNotes = pregnancyNotes;
    }

    public void addPregnancyNote(PregnancyNote pregnancyNote) {
        this.pregnancyNotes.add(pregnancyNote);
    }
}
