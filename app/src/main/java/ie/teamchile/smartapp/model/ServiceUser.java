package ie.teamchile.smartapp.model;

/**
 * Created by user on 5/26/15.
 */

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class ServiceUser {
    @Expose
    private String gestation;
    @Expose
    private Integer id;
    @Expose
    private String name;
    @SerializedName("baby_ids")
    @Expose
    private List<Integer> babyIds = new ArrayList<Integer>();
    @SerializedName("clinical_fields")
    @Expose
    private ClinicalFields clinicalFields;
    @SerializedName("hospital_number")
    @Expose
    private String hospitalNumber;
    @SerializedName("personal_fields")
    @Expose
    private PersonalFields personalFields;
    @SerializedName("pregnancy_ids")
    @Expose
    private List<Integer> pregnancyIds = new ArrayList<Integer>();


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

    public ServiceUser withId(Integer id) {
        this.id = id;
        return this;
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

    public ServiceUser withName(String name) {
        this.name = name;
        return this;
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
     * @return The clinicalFields
     */
    public ClinicalFields getClinicalFields() {
        return clinicalFields;
    }

    /**
     * @param clinicalFields The clinical_fields
     */
    public void setClinicalFields(ClinicalFields clinicalFields) {
        this.clinicalFields = clinicalFields;
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
     * @return The personalFields
     */
    public PersonalFields getPersonalFields() {
        return personalFields;
    }

    /**
     * @param personalFields The personal_fields
     */
    public void setPersonalFields(PersonalFields personalFields) {
        this.personalFields = personalFields;
    }

    /**
     * @return The pregnancyIds
     */
    public List<Integer> getPregnancyIds() {
        return pregnancyIds;
    }

    /**
     * @param pregnancyIds The pregnancy_ids
     */
    public void setPregnancyIds(List<Integer> pregnancyIds) {
        this.pregnancyIds = pregnancyIds;
    }

}
