package ie.teamchile.smartapp.model;

import java.util.Date;

import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class ResponseAppointment extends RealmObject {
    private int clinicId;
    private String date;
    @PrimaryKey
    private int id;
    private ResponseLinks links;
    private String priority;
    private RealmList<RealmInteger> serviceOptionIds;
    private int serviceProviderId;
    private ResponseServiceUser serviceUser;
    private int serviceUserId;
    private Date time;
    //private RealmList<Object> visitLogs;
    private String visitType;
    private boolean attended;

    public ResponseAppointment() {}

    public ResponseAppointment(ResponseAppointment appointment) {
        setClinicId(appointment.getClinicId());
        setDate(appointment.getDate());
        setId(appointment.getId());
        setLinks(appointment.getLinks());
        setPriority(appointment.getPriority());
        setServiceOptionIds(appointment.getServiceOptionIds());
        setServiceProviderId(appointment.getServiceProviderId());
        setServiceUser(appointment.getServiceUser());
        setServiceUserId(appointment.getServiceUserId());
        setTime(appointment.getTime());
        setVisitType(appointment.getVisitType());
        setAttended(appointment.isAttended());
    }

    public int getClinicId() {
        return clinicId;
    }

    public void setClinicId(int clinicId) {
        this.clinicId = clinicId;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
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

    public String getPriority() {
        return priority;
    }

    public void setPriority(String priority) {
        this.priority = priority;
    }

    public RealmList<RealmInteger> getServiceOptionIds() {
        return serviceOptionIds;
    }

    public void setServiceOptionIds(RealmList<RealmInteger> serviceOptionIds) {
        this.serviceOptionIds = serviceOptionIds;
    }

    public int getServiceProviderId() {
        return serviceProviderId;
    }

    public void setServiceProviderId(int serviceProviderId) {
        this.serviceProviderId = serviceProviderId;
    }

    public ResponseServiceUser getServiceUser() {
        return serviceUser;
    }

    public void setServiceUser(ResponseServiceUser serviceUser) {
        this.serviceUser = serviceUser;
    }

    public int getServiceUserId() {
        return serviceUserId;
    }

    public void setServiceUserId(int serviceUserId) {
        this.serviceUserId = serviceUserId;
    }

    public Date getTime() {
        return time;
    }

    public void setTime(Date time) {
        this.time = time;
    }

    public String getVisitType() {
        return visitType;
    }

    public void setVisitType(String visitType) {
        this.visitType = visitType;
    }

    public boolean isAttended() {
        return attended;
    }

    public void setAttended(boolean attended) {
        this.attended = attended;
    }
}