package ie.teamchile.smartapp.model;

import java.util.Date;

import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by user on 5/26/15.
 */
public class ResponseClinic extends RealmObject {
    private String address;
    private RealmList<RealmInteger> announcementIds;
    private int appointmentInterval;
    private Date closingTime;
    private ResponseDays days;
    @PrimaryKey
    private int id;
    private ResponseLinks links;
    private String name;
    private Date openingTime;
    private String recurrence;
    private RealmList<RealmInteger> serviceOptionIds;
    private String type;

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public RealmList<RealmInteger> getAnnouncementIds() {
        return announcementIds;
    }

    public void setAnnouncementIds(RealmList<RealmInteger> announcementIds) {
        this.announcementIds = announcementIds;
    }

    public int getAppointmentInterval() {
        return appointmentInterval;
    }

    public void setAppointmentInterval(int appointmentInterval) {
        this.appointmentInterval = appointmentInterval;
    }

    public Date getClosingTime() {
        return closingTime;
    }

    public void setClosingTime(Date closingTime) {
        this.closingTime = closingTime;
    }

    public ResponseDays getDays() {
        return days;
    }

    public void setDays(ResponseDays days) {
        this.days = days;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public ResponseLinks getLinks() {
        return links;
    }

    public void setLinks(ResponseLinks links) {
        this.links = links;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Date getOpeningTime() {
        return openingTime;
    }

    public void setOpeningTime(Date openingTime) {
        this.openingTime = openingTime;
    }

    public String getRecurrence() {
        return recurrence;
    }

    public void setRecurrence(String recurrence) {
        this.recurrence = recurrence;
    }

    public RealmList<RealmInteger> getServiceOptionIds() {
        return serviceOptionIds;
    }

    public void setServiceOptionIds(RealmList<RealmInteger> serviceOptionIds) {
        this.serviceOptionIds = serviceOptionIds;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
