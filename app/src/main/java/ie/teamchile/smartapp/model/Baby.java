package ie.teamchile.smartapp.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

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
    @SerializedName("hospital_number")
    @Expose
    private String hospitalNumber;
    @Expose
    private Integer id;
    @Expose
    private String name;
    @SerializedName("newborn_screening_test")
    @Expose
    private String newbornScreeningTest;
    @SerializedName("pregnancy_id")
    @Expose
    private Integer pregnancyId;
    @SerializedName("vitamin_k")
    @Expose
    private String vitaminK;
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
     * @return The newbornScreeningTest
     */
    public String getNewbornScreeningTest() {
        return newbornScreeningTest;
    }

    /**
     * @param newbornScreeningTest The newborn_screening_test
     */
    public void setNewbornScreeningTest(String newbornScreeningTest) {
        this.newbornScreeningTest = newbornScreeningTest;
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
     * @return The vitaminK
     */
    public String getVitaminK() {
        return vitaminK;
    }

    /**
     * @param vitaminK The vitamin_k
     */
    public void setVitaminK(String vitaminK) {
        this.vitaminK = vitaminK;
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
