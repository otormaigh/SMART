package ie.teamchile.smartapp.model;

import java.util.Date;

import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by user on 5/26/15.
 */
public class Pregnancy extends RealmObject {
    private String additionalInfo;
    private String antiD;
    private RealmList<RealmInteger> babyIds;
    //private RealmList<RealmString> birthMode;
    private String createdAt;
    private Date estimatedDeliveryDate;
    private String feeding;
    private String gestation;
    private String babyAge;
    @PrimaryKey
    private int id;
    private Date lastMenstrualPeriod;
    private String perineum;
    private int serviceUserId;
    private RealmList<PregnancyNote> pregnancyNotes;

    public String getAdditionalInfo() {
        return additionalInfo;
    }

    public void setAdditionalInfo(String additionalInfo) {
        this.additionalInfo = additionalInfo;
    }

    public String getAntiD() {
        return antiD;
    }

    public void setAntiD(String antiD) {
        this.antiD = antiD;
    }

    public RealmList<RealmInteger> getBabyIds() {
        return babyIds;
    }

    public void setBabyIds(RealmList<RealmInteger> babyIds) {
        this.babyIds = babyIds;
    }

    /*public RealmList<RealmString> getBirthMode() {
        return birthMode;
    }

    public void setBirthMode(RealmList<RealmString> birthMode) {
        this.birthMode = birthMode;
    }*/

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public Date getEstimatedDeliveryDate() {
        return estimatedDeliveryDate;
    }

    public void setEstimatedDeliveryDate(Date estimatedDeliveryDate) {
        this.estimatedDeliveryDate = estimatedDeliveryDate;
    }

    public String getFeeding() {
        return feeding;
    }

    public void setFeeding(String feeding) {
        this.feeding = feeding;
    }

    public String getGestation() {
        return gestation;
    }

    public void setGestation(String gestation) {
        this.gestation = gestation;
    }

    public String getBabyAge() {
        return babyAge;
    }

    public void setBabyAge(String babyAge) {
        this.babyAge = babyAge;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Date getLastMenstrualPeriod() {
        return lastMenstrualPeriod;
    }

    public void setLastMenstrualPeriod(Date lastMenstrualPeriod) {
        this.lastMenstrualPeriod = lastMenstrualPeriod;
    }

    public String getPerineum() {
        return perineum;
    }

    public void setPerineum(String perineum) {
        this.perineum = perineum;
    }

    public int getServiceUserId() {
        return serviceUserId;
    }

    public void setServiceUserId(int serviceUserId) {
        this.serviceUserId = serviceUserId;
    }

    public RealmList<PregnancyNote> getPregnancyNotes() {
        return pregnancyNotes;
    }

    public void setPregnancyNotes(RealmList<PregnancyNote> pregnancyNotes) {
        this.pregnancyNotes = pregnancyNotes;
    }
}
