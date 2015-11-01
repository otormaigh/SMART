package ie.teamchile.smartapp.model;

import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by user on 5/26/15.
 */
public class Clinic extends RealmObject {
    private String address;
    private RealmList<RealmInteger> announcementIds;
    private Integer appointmentInterval;
    private String closingTime;
    private Days days;
    @PrimaryKey
    private int id;
    private Links links;
    private String name;
    private String openingTime;
    private String recurrence;
    private RealmList<RealmInteger> serviceOptionIds;
    private String type;

    /*public List<String> getTrueDays() {
        List<String> trueDays = new ArrayList<>();
        if (days.getMonday())
            trueDays.add("Monday");
        if (days.getTuesday())
            trueDays.add("Tuesday");
        if (days.getWednesday())
            trueDays.add("Wednesday");
        if (days.getThursday())
            trueDays.add("Thursday");
        if (days.getFriday())
            trueDays.add("Friday");
        if (days.getSaturday())
            trueDays.add("Saturday");
        if (days.getSunday())
            trueDays.add("Sunday");

        return trueDays;
    }*/

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

    public Integer getAppointmentInterval() {
        return appointmentInterval;
    }

    public void setAppointmentInterval(Integer appointmentInterval) {
        this.appointmentInterval = appointmentInterval;
    }

    public String getClosingTime() {
        return closingTime;
    }

    public void setClosingTime(String closingTime) {
        this.closingTime = closingTime;
    }

    public Days getDays() {
        return days;
    }

    public void setDays(Days days) {
        this.days = days;
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

    public String getOpeningTime() {
        return openingTime;
    }

    public void setOpeningTime(String openingTime) {
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
