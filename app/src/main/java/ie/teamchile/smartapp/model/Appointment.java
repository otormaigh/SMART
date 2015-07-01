package ie.teamchile.smartapp.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

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
    private List<Integer> serviceOptionIds = new ArrayList<>();
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
    @Expose
    private Boolean attended;

    public Integer getClinicId() {
        return clinicId;
    }

    public void setClinicId(Integer clinicId) {
        this.clinicId = clinicId;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
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

    public String getPriority() {
        return priority;
    }

    public void setPriority(String priority) {
        this.priority = priority;
    }

    public List<Integer> getServiceOptionIds() {
        return serviceOptionIds;
    }

    public void setServiceOptionIds(List<Integer> serviceOptionIds) {
        this.serviceOptionIds = serviceOptionIds;
    }

    public Integer getServiceProviderId() {
        return serviceProviderId;
    }

    public void setServiceProviderId(Integer serviceProviderId) {
        this.serviceProviderId = serviceProviderId;
    }

    public ServiceUser getServiceUser() {
        return serviceUser;
    }

    public void setServiceUser(ServiceUser serviceUser) {
        this.serviceUser = serviceUser;
    }

    public Integer getServiceUserId() {
        return serviceUserId;
    }

    public void setServiceUserId(Integer serviceUserId) {
        this.serviceUserId = serviceUserId;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public List<Object> getVisitLogs() {
        return visitLogs;
    }

    public void setVisitLogs(List<Object> visitLogs) {
        this.visitLogs = visitLogs;
    }

    public String getVisitType() {
        return visitType;
    }

    public void setVisitType(String visitType) {
        this.visitType = visitType;
    }

    public Boolean getAttended() {
        return attended;
    }

    public void setAttended(Boolean attended) {
        this.attended = attended;
    }
}