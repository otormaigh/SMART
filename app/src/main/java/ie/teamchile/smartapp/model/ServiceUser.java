package ie.teamchile.smartapp.model;

/**
 * Created by user on 5/26/15.
 */

import java.util.ArrayList;
import java.util.List;

public class ServiceUser {
    private String gestation;
    private Integer id;
    private String name;
    private List<Integer> babyIds = new ArrayList<Integer>();
    private ClinicalFields clinicalFields;
    private String hospitalNumber;
    private PersonalFields personalFields;
    private List<Integer> pregnancyIds = new ArrayList<Integer>();

    public String getGestation() {
        return gestation;
    }

    public void setGestation(String gestation) {
        this.gestation = gestation;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public ServiceUser withId(Integer id) {
        this.id = id;
        return this;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ServiceUser withName(String name) {
        this.name = name;
        return this;
    }

    public List<Integer> getBabyIds() {
        return babyIds;
    }

    public void setBabyIds(List<Integer> babyIds) {
        this.babyIds = babyIds;
    }

    public ClinicalFields getClinicalFields() {
        return clinicalFields;
    }

    public void setClinicalFields(ClinicalFields clinicalFields) {
        this.clinicalFields = clinicalFields;
    }

    public String getHospitalNumber() {
        return hospitalNumber;
    }

    public void setHospitalNumber(String hospitalNumber) {
        this.hospitalNumber = hospitalNumber;
    }

    public PersonalFields getPersonalFields() {
        return personalFields;
    }

    public void setPersonalFields(PersonalFields personalFields) {
        this.personalFields = personalFields;
    }

    public List<Integer> getPregnancyIds() {
        return pregnancyIds;
    }

    public void setPregnancyIds(List<Integer> pregnancyIds) {
        this.pregnancyIds = pregnancyIds;
    }

}
