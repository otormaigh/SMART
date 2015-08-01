package ie.teamchile.smartapp.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by user on 5/26/15.
 */
public class Clinic {
    private String address;
    private List<Integer> announcementIds = new ArrayList<Integer>();
    private Integer appointmentInterval;
    private String closingTime;
    private Days days;
    private Integer id;
    private Links links;
    private String name;
    private String openingTime;
    private String recurrence;
    private List<Integer> serviceOptionIds = new ArrayList<Integer>();
    private String type;

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public List<Integer> getAnnouncementIds() {
        return announcementIds;
    }

    public void setAnnouncementIds(List<Integer> announcementIds) {
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

    public List<String> getTrueDays() {
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

    public List<Integer> getServiceOptionIds() {
        return serviceOptionIds;
    }

    public void setServiceOptionIds(List<Integer> serviceOptionIds) {
        this.serviceOptionIds = serviceOptionIds;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
