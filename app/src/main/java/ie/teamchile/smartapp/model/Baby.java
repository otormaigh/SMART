package ie.teamchile.smartapp.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by user on 5/26/15.
 */
public class Baby {

    @SerializedName("birth_outcome")
    @Expose
    private String birthOutcome;
    @SerializedName("delivery_date_time")
    @Expose
    private String deliveryDateTime;
    @Expose
    private String gender;
    @Expose
    private String hearing;
    @SerializedName("hearing_history_ids")
    @Expose
    private List<Object> hearingHistoryIds = new ArrayList<Object>();
    @SerializedName("hospital_number")
    @Expose
    private String hospitalNumber;
    @Expose
    private Integer id;
    @Expose
    private Links links;
    @Expose
    private String name;
    @Expose
    private String nbst;
    @SerializedName("nbst_history_ids")
    @Expose
    private List<Object> nbstHistoryIds = new ArrayList<Object>();
    @SerializedName("pregnancy_id")
    @Expose
    private Integer pregnancyId;
    @SerializedName("vit_k")
    @Expose
    private String vitK;
    @SerializedName("vit_k_history_ids")
    @Expose
    private List<Object> vitKHistoryIds = new ArrayList<Object>();
    @Expose
    private Integer weight;

    /**
     * @return The birthOutcome
     */
    public String getBirthOutcome() {
        return birthOutcome;
    }

    /**
     * @param birthOutcome The birth_outcome
     */
    public void setBirthOutcome(String birthOutcome) {
        this.birthOutcome = birthOutcome;
    }

    /**
     * @return The deliveryDateTime
     */
    public String getDeliveryDateTime() {
        return deliveryDateTime;
    }

    /**
     * @param deliveryDateTime The delivery_date_time
     */
    public void setDeliveryDateTime(String deliveryDateTime) {
        this.deliveryDateTime = deliveryDateTime;
    }

    /**
     * @return The gender
     */
    public String getGender() {
        return gender;
    }

    /**
     * @param gender The gender
     */
    public void setGender(String gender) {
        this.gender = gender;
    }

    /**
     * @return The hearing
     */
    public String getHearing() {
        return hearing;
    }

    /**
     * @param hearing The hearing
     */
    public void setHearing(String hearing) {
        this.hearing = hearing;
    }

    /**
     * @return The hearingHistoryIds
     */
    public List<Object> getHearingHistoryIds() {
        return hearingHistoryIds;
    }

    /**
     * @param hearingHistoryIds The hearing_history_ids
     */
    public void setHearingHistoryIds(List<Object> hearingHistoryIds) {
        this.hearingHistoryIds = hearingHistoryIds;
    }

    /**
     * @return The hospitalNumber
     */
    public String getHospitalNumber() {
        return hospitalNumber;
    }

    /**
     * @param hospitalNumber The hospital_number
     */
    public void setHospitalNumber(String hospitalNumber) {
        this.hospitalNumber = hospitalNumber;
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
     * @return The links
     */
    public Links getLinks() {
        return links;
    }

    /**
     * @param links The links
     */
    public void setLinks(Links links) {
        this.links = links;
    }

    /**
     * @return The name
     */
    public String getName() {
        return name;
    }

    /**
     * @param name The name
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * @return The nbst
     */
    public String getNbst() {
        return nbst;
    }

    /**
     * @param nbst The nbst
     */
    public void setNbst(String nbst) {
        this.nbst = nbst;
    }

    /**
     * @return The nbstHistoryIds
     */
    public List<Object> getNbstHistoryIds() {
        return nbstHistoryIds;
    }

    /**
     * @param nbstHistoryIds The nbst_history_ids
     */
    public void setNbstHistoryIds(List<Object> nbstHistoryIds) {
        this.nbstHistoryIds = nbstHistoryIds;
    }

    /**
     * @return The pregnancyId
     */
    public Integer getPregnancyId() {
        return pregnancyId;
    }

    /**
     * @param pregnancyId The pregnancy_id
     */
    public void setPregnancyId(Integer pregnancyId) {
        this.pregnancyId = pregnancyId;
    }

    /**
     * @return The vitK
     */
    public String getVitK() {
        return vitK;
    }

    /**
     * @param vitK The vit_k
     */
    public void setVitK(String vitK) {
        this.vitK = vitK;
    }

    /**
     * @return The vitKHistoryIds
     */
    public List<Object> getVitKHistoryIds() {
        return vitKHistoryIds;
    }

    /**
     * @param vitKHistoryIds The vit_k_history_ids
     */
    public void setVitKHistoryIds(List<Object> vitKHistoryIds) {
        this.vitKHistoryIds = vitKHistoryIds;
    }

    /**
     * @return The weight
     */
    public Integer getWeight() {
        return weight;
    }

    /**
     * @param weight The weight
     */
    public void setWeight(Integer weight) {
        this.weight = weight;
    }
}