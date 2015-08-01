package ie.teamchile.smartapp.model;

import java.util.ArrayList;
import java.util.List;

public class Appointment {
    private Integer clinicId;
    private String date;
    private Integer id;
    private Links links;
    private String priority;
    private List<Integer> serviceOptionIds = new ArrayList<>();
    private Integer serviceProviderId;
    private ServiceUser serviceUser;
    private Integer serviceUserId;
    private String time;
    private List<Object> visitLogs = new ArrayList<>();
    private String visitType;
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