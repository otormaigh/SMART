package ie.teamchile.smartapp.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by user on 5/26/15.
 */
public class Pregnancy {
    private String additionalInfo;
    private String antiD;
    private List<Integer> babyIds = new ArrayList<Integer>();
    private List<String> birthMode = new ArrayList<String>();
    private String createdAt;
    private String estimatedDeliveryDate;
    private String feeding;
    private String gestation;
    private String babyAge;
    private Integer id;
    private String lastMenstrualPeriod;
    private String perineum;
    private Integer serviceUserId;
    private List<PregnancyNote> pregnancyNotes = new ArrayList<>();

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

    public List<Integer> getBabyIds() {
        return babyIds;
    }

    public void setBabyIds(List<Integer> babyIds) {
        this.babyIds = babyIds;
    }

    public List<String> getBirthMode() {
        return birthMode;
    }

    public void setBirthMode(List<String> birthMode) {
        this.birthMode = birthMode;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public String getEstimatedDeliveryDate() {
        return estimatedDeliveryDate;
    }

    public void setEstimatedDeliveryDate(String estimatedDeliveryDate) {
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

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getLastMenstrualPeriod() {
        return lastMenstrualPeriod;
    }

    public void setLastMenstrualPeriod(String lastMenstrualPeriod) {
        this.lastMenstrualPeriod = lastMenstrualPeriod;
    }

    public String getPerineum() {
        return perineum;
    }

    public void setPerineum(String perineum) {
        this.perineum = perineum;
    }

    public Integer getServiceUserId() {
        return serviceUserId;
    }

    public void setServiceUserId(Integer serviceUserId) {
        this.serviceUserId = serviceUserId;
    }

    public List<PregnancyNote> getPregnancyNotes() {
        return pregnancyNotes;
    }

    public void setPregnancyNotes(List<PregnancyNote> pregnancyNotes) {
        this.pregnancyNotes = pregnancyNotes;
    }

    public void addPregnancyNote(PregnancyNote pregnancyNote) {
        this.pregnancyNotes.add(pregnancyNote);
    }
}
