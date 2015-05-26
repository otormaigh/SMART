package ie.teamchile.smartapp.retrofit;

import java.util.ArrayList;
import java.util.List;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Appointment {

    @SerializedName("clinic_id")
    @Expose
    private Integer clinicId;
    @Expose
    private String date;
    @Expose
    private Integer id;
    @Expose
    private Links links;
    @Expose
    private String priority;
    @SerializedName("service_option_ids")
    @Expose
    private List<Object> serviceOptionIds = new ArrayList<>();
    @SerializedName("service_provider_id")
    @Expose
    private Integer serviceProviderId;
    @SerializedName("service_user")
    @Expose
    private ServiceUser serviceUser;
    @SerializedName("service_user_id")
    @Expose
    private Integer serviceUserId;
    @Expose
    private String time;
    @SerializedName("visit_logs")
    @Expose
    private List<Object> visitLogs = new ArrayList<>();
    @SerializedName("visit_type")
    @Expose
    private String visitType;

    /**
     * @return The clinicId
     */
    public Integer getClinicId() {
        return clinicId;
    }

    /**
     * @param clinicId The clinic_id
     */
    public void setClinicId(Integer clinicId) {
        this.clinicId = clinicId;
    }

    public Appointment withClinicId(Integer clinicId) {
        this.clinicId = clinicId;
        return this;
    }

    /**
     * @return The date
     */
    public String getDate() {
        return date;
    }

    /**
     * @param date The date
     */
    public void setDate(String date) {
        this.date = date;
    }

    public Appointment withDate(String date) {
        this.date = date;
        return this;
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

    public Appointment withId(Integer id) {
        this.id = id;
        return this;
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

    public Appointment withLinks(Links links) {
        this.links = links;
        return this;
    }

    /**
     * @return The priority
     */
    public String getPriority() {
        return priority;
    }

    /**
     * @param priority The priority
     */
    public void setPriority(String priority) {
        this.priority = priority;
    }

    public Appointment withPriority(String priority) {
        this.priority = priority;
        return this;
    }

    /**
     * @return The serviceOptionIds
     */
    public List<Object> getServiceOptionIds() {
        return serviceOptionIds;
    }

    /**
     * @param serviceOptionIds The service_option_ids
     */
    public void setServiceOptionIds(List<Object> serviceOptionIds) {
        this.serviceOptionIds = serviceOptionIds;
    }

    public Appointment withServiceOptionIds(List<Object> serviceOptionIds) {
        this.serviceOptionIds = serviceOptionIds;
        return this;
    }

    /**
     * @return The serviceProviderId
     */
    public Integer getServiceProviderId() {
        return serviceProviderId;
    }

    /**
     * @param serviceProviderId The service_provider_id
     */
    public void setServiceProviderId(Integer serviceProviderId) {
        this.serviceProviderId = serviceProviderId;
    }

    public Appointment withServiceProviderId(Integer serviceProviderId) {
        this.serviceProviderId = serviceProviderId;
        return this;
    }

    /**
     * @return The serviceUser
     */
    public ServiceUser getServiceUser() {
        return serviceUser;
    }

    /**
     * @param serviceUser The service_user
     */
    public void setServiceUser(ServiceUser serviceUser) {
        this.serviceUser = serviceUser;
    }

    public Appointment withServiceUser(ServiceUser serviceUser) {
        this.serviceUser = serviceUser;
        return this;
    }

    /**
     * @return The serviceUserId
     */
    public Integer getServiceUserId() {
        return serviceUserId;
    }

    /**
     * @param serviceUserId The service_user_id
     */
    public void setServiceUserId(Integer serviceUserId) {
        this.serviceUserId = serviceUserId;
    }

    public Appointment withServiceUserId(Integer serviceUserId) {
        this.serviceUserId = serviceUserId;
        return this;
    }

    /**
     * @return The time
     */
    public String getTime() {
        return time;
    }

    /**
     * @param time The time
     */
    public void setTime(String time) {
        this.time = time;
    }

    public Appointment withTime(String time) {
        this.time = time;
        return this;
    }

    /**
     * @return The visitLogs
     */
    public List<Object> getVisitLogs() {
        return visitLogs;
    }

    /**
     * @param visitLogs The visit_logs
     */
    public void setVisitLogs(List<Object> visitLogs) {
        this.visitLogs = visitLogs;
    }

    public Appointment withVisitLogs(List<Object> visitLogs) {
        this.visitLogs = visitLogs;
        return this;
    }

    /**
     * @return The visitType
     */
    public String getVisitType() {
        return visitType;
    }

    /**
     * @param visitType The visit_type
     */
    public void setVisitType(String visitType) {
        this.visitType = visitType;
    }

    public Appointment withVisitType(String visitType) {
        this.visitType = visitType;
        return this;
    }

}