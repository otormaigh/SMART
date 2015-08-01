package ie.teamchile.smartapp.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by user on 5/26/15.
 */
public class Baby {
    private String birthOutcome;
    private String deliveryDateTime;
    private String gender;
    private String hearing;
    private List<Object> hearingHistoryIds = new ArrayList<Object>();
    private String hospitalNumber;
    private Integer id;
    private Links links;
    private String name;
    private String nbst;
    private List<Object> nbstHistoryIds = new ArrayList<Object>();
    private Integer pregnancyId;
    private String vitK;
    private List<Object> vitKHistoryIds = new ArrayList<Object>();
    private Integer weight;

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

    public List<Object> getHearingHistoryIds() {
        return hearingHistoryIds;
    }

    public void setHearingHistoryIds(List<Object> hearingHistoryIds) {
        this.hearingHistoryIds = hearingHistoryIds;
    }

    public String getHospitalNumber() {
        return hospitalNumber;
    }

    public void setHospitalNumber(String hospitalNumber) {
        this.hospitalNumber = hospitalNumber;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
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

    public List<Object> getNbstHistoryIds() {
        return nbstHistoryIds;
    }

    public void setNbstHistoryIds(List<Object> nbstHistoryIds) {
        this.nbstHistoryIds = nbstHistoryIds;
    }

    public Integer getPregnancyId() {
        return pregnancyId;
    }

    public void setPregnancyId(Integer pregnancyId) {
        this.pregnancyId = pregnancyId;
    }

    public String getVitK() {
        return vitK;
    }

    public void setVitK(String vitK) {
        this.vitK = vitK;
    }

    public List<Object> getVitKHistoryIds() {
        return vitKHistoryIds;
    }

    public void setVitKHistoryIds(List<Object> vitKHistoryIds) {
        this.vitKHistoryIds = vitKHistoryIds;
    }

    public Integer getWeight() {
        return weight;
    }

    public void setWeight(Integer weight) {
        this.weight = weight;
    }
}