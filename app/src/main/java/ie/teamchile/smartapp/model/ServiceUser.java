package ie.teamchile.smartapp.model;

/**
 * Created by user on 5/26/15.
 */

import io.realm.RealmList;
import io.realm.RealmObject;

public class ServiceUser extends RealmObject {
    private String gestation;
    private int id;
    private String name;
    private RealmList<RealmInteger> babyIds;
    private ClinicalFields clinicalFields;
    private String hospitalNumber;
    private PersonalFields personalFields;
    private RealmList<RealmInteger> pregnancyIds;

    public String getGestation() {
        return gestation;
    }

    public void setGestation(String gestation) {
        this.gestation = gestation;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public RealmList<RealmInteger> getBabyIds() {
        return babyIds;
    }

    public void setBabyIds(RealmList<RealmInteger> babyIds) {
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

    public RealmList<RealmInteger> getPregnancyIds() {
        return pregnancyIds;
    }

    public void setPregnancyIds(RealmList<RealmInteger> pregnancyIds) {
        this.pregnancyIds = pregnancyIds;
    }
}
