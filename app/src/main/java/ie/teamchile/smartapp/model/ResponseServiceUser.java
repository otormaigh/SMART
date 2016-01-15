package ie.teamchile.smartapp.model;

/**
 * Created by user on 5/26/15.
 */

import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class ResponseServiceUser extends RealmObject {
    private String gestation;
    @PrimaryKey
    private int id;
    private String name;
    private RealmList<RealmInteger> babyIds;
    private ResponseClinicalFields clinicalFields;
    private String hospitalNumber;
    private ResponsePersonalFields personalFields;
    private RealmList<RealmInteger> pregnancyIds;

    public ResponseServiceUser() {}

    public ResponseServiceUser(ResponseServiceUser serviceUser) {
        setGestation(serviceUser.getGestation());
        setId(serviceUser.getId());
        setName(serviceUser.getName());
        setBabyIds(serviceUser.getBabyIds());
        setClinicalFields(serviceUser.getClinicalFields());
        setHospitalNumber(serviceUser.getHospitalNumber());
        setPersonalFields(serviceUser.getPersonalFields());
        setPregnancyIds(serviceUser.getPregnancyIds());
    }

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

    public ResponseClinicalFields getClinicalFields() {
        return clinicalFields;
    }

    public void setClinicalFields(ResponseClinicalFields clinicalFields) {
        this.clinicalFields = clinicalFields;
    }

    public String getHospitalNumber() {
        return hospitalNumber;
    }

    public void setHospitalNumber(String hospitalNumber) {
        this.hospitalNumber = hospitalNumber;
    }

    public ResponsePersonalFields getPersonalFields() {
        return personalFields;
    }

    public void setPersonalFields(ResponsePersonalFields personalFields) {
        this.personalFields = personalFields;
    }

    public RealmList<RealmInteger> getPregnancyIds() {
        return pregnancyIds;
    }

    public void setPregnancyIds(RealmList<RealmInteger> pregnancyIds) {
        this.pregnancyIds = pregnancyIds;
    }
}
