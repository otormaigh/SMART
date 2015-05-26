package ie.teamchile.smartapp.retrofit;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by user on 5/26/15.
 */
public class Clinic {

    @Expose
    private String address;
    @SerializedName("announcement_ids")
    @Expose
    private List<Integer> announcementIds = new ArrayList<Integer>();
    @SerializedName("appointment_interval")
    @Expose
    private Integer appointmentInterval;
    @SerializedName("closing_time")
    @Expose
    private String closingTime;
    @Expose
    private Days days;
    @Expose
    private Integer id;
    @Expose
    private Links links;
    @Expose
    private String name;
    @SerializedName("opening_time")
    @Expose
    private String openingTime;
    @Expose
    private String recurrence;
    @SerializedName("service_option_ids")
    @Expose
    private List<Integer> serviceOptionIds = new ArrayList<Integer>();
    @Expose
    private String type;

    /**
     * @return The address
     */
    public String getAddress() {
        return address;
    }

    /**
     * @param address The address
     */
    public void setAddress(String address) {
        this.address = address;
    }

    /**
     * @return The announcementIds
     */
    public List<Integer> getAnnouncementIds() {
        return announcementIds;
    }

    /**
     * @param announcementIds The announcement_ids
     */
    public void setAnnouncementIds(List<Integer> announcementIds) {
        this.announcementIds = announcementIds;
    }

    /**
     * @return The appointmentInterval
     */
    public Integer getAppointmentInterval() {
        return appointmentInterval;
    }

    /**
     * @param appointmentInterval The appointment_interval
     */
    public void setAppointmentInterval(Integer appointmentInterval) {
        this.appointmentInterval = appointmentInterval;
    }

    /**
     * @return The closingTime
     */
    public String getClosingTime() {
        return closingTime;
    }

    /**
     * @param closingTime The closing_time
     */
    public void setClosingTime(String closingTime) {
        this.closingTime = closingTime;
    }

    /**
     * @return The days
     */
    public Days getDays() {
        return days;
    }

    /**
     * @param days The days
     */
    public void setDays(Days days) {
        this.days = days;
    }

    /**
     * @return The id
     */
    public Integer getId() {
        return id;
    }

    /**
     * @param id The id
     */
    public void setId(Integer id) {
        this.id = id;
    }

    /**
     * @return The links
     */
    public Links getLinks() {
        return links;
    }

    /**
     * @param links The links
     */
    public void setLinks(Links links) {
        this.links = links;
    }

    /**
     * @return The name
     */
    public String getName() {
        return name;
    }

    /**
     * @param name The name
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * @return The openingTime
     */
    public String getOpeningTime() {
        return openingTime;
    }

    /**
     * @param openingTime The opening_time
     */
    public void setOpeningTime(String openingTime) {
        this.openingTime = openingTime;
    }

    /**
     * @return The recurrence
     */
    public String getRecurrence() {
        return recurrence;
    }

    /**
     * @param recurrence The recurrence
     */
    public void setRecurrence(String recurrence) {
        this.recurrence = recurrence;
    }

    /**
     * @return The serviceOptionIds
     */
    public List<Integer> getServiceOptionIds() {
        return serviceOptionIds;
    }

    /**
     * @param serviceOptionIds The service_option_ids
     */
    public void setServiceOptionIds(List<Integer> serviceOptionIds) {
        this.serviceOptionIds = serviceOptionIds;
    }

    /**
     * @return The type
     */
    public String getType() {
        return type;
    }

    /**
     * @param type The type
     */
    public void setType(String type) {
        this.type = type;
    }
}
