package ie.teamchile.smartapp.model;

import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by user on 5/26/15.
 */
public class Baby extends RealmObject {
    private String birthOutcome;
    private String deliveryDateTime;
    private String gender;
    private String hearing;
    private RealmList<RealmInteger> hearingHistoryIds;
    private String hospitalNumber;
    @PrimaryKey
    private int id;
    private Links links;
    private String name;
    private String nbst;
    private RealmList<RealmInteger> nbstHistoryIds;
    private int pregnancyId;
    private String vitK;
    private RealmList<RealmInteger> vitKHistoryIds;
    private int weight;

    public String getBirthOutcome() {
        return birthOutcome;
    }

    public void setBirthOutcome(String birthOutcome) {
        this.birthOutcome = birthOutcome;
    }

    public String getDeliveryDateTime() {
        return deliveryDateTime;
    }

    public void setDeliveryDateTime(String deliveryDateTime) {
        this.deliveryDateTime = deliveryDateTime;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getHearing() {
        return hearing;
    }

    public void setHearing(String hearing) {
        this.hearing = hearing;
    }

    public RealmList<RealmInteger> getHearingHistoryIds() {
        return hearingHistoryIds;
    }

    public void setHearingHistoryIds(RealmList<RealmInteger> hearingHistoryIds) {
        this.hearingHistoryIds = hearingHistoryIds;
    }

    public String getHospitalNumber() {
        return hospitalNumber;
    }

    public void setHospitalNumber(String hospitalNumber) {
        this.hospitalNumber = hospitalNumber;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Links getLinks() {
        return links;
    }

    public void setLinks(Links links) {
        this.links = links;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNbst() {
        return nbst;
    }

    public void setNbst(String nbst) {
        this.nbst = nbst;
    }

    public RealmList<RealmInteger> getNbstHistoryIds() {
        return nbstHistoryIds;
    }

    public void setNbstHistoryIds(RealmList<RealmInteger> nbstHistoryIds) {
        this.nbstHistoryIds = nbstHistoryIds;
    }

    public int getPregnancyId() {
        return pregnancyId;
    }

    public void setPregnancyId(int pregnancyId) {
        this.pregnancyId = pregnancyId;
    }

    public String getVitK() {
        return vitK;
    }

    public void setVitK(String vitK) {
        this.vitK = vitK;
    }

    public RealmList<RealmInteger> getVitKHistoryIds() {
        return vitKHistoryIds;
    }

    public void setVitKHistoryIds(RealmList<RealmInteger> vitKHistoryIds) {
        this.vitKHistoryIds = vitKHistoryIds;
    }

    public int getWeight() {
        return weight;
    }

    public void setWeight(int weight) {
        this.weight = weight;
    }
}