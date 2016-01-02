package ie.teamchile.smartapp.model;

import java.util.Date;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by user on 6/16/15.
 */
public class ResponsePregnancyNote extends RealmObject {
    private Date createdAt;
    @PrimaryKey
    private int id;
    private String note;
    private int pregnancyId;
    private int serviceProviderId;
    private String serviceProviderName;

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public int getPregnancyId() {
        return pregnancyId;
    }

    public void setPregnancyId(int pregnancyId) {
        this.pregnancyId = pregnancyId;
    }

    public int getServiceProviderId() {
        return serviceProviderId;
    }

    public void setServiceProviderId(int serviceProviderId) {
        this.serviceProviderId = serviceProviderId;
    }

    public String getServiceProviderName() {
        return serviceProviderName;
    }

    public void setServiceProviderName(String serviceProviderName) {
        this.serviceProviderName = serviceProviderName;
    }
}
