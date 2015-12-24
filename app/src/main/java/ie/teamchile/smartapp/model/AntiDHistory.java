package ie.teamchile.smartapp.model;

import java.util.Date;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by user on 6/16/15.
 */
public class AntiDHistory extends RealmObject {
    @PrimaryKey
    private int id;
    private String antiD;
    private Date createdAt;
    private int pregnancyId;
    private String serviceProviderName;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getAntiD() {
        return antiD;
    }

    public void setAntiD(String antiD) {
        this.antiD = antiD;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public int getPregnancyId() {
        return pregnancyId;
    }

    public void setPregnancyId(int pregnancyId) {
        this.pregnancyId = pregnancyId;
    }

    public String getServiceProviderName() {
        return serviceProviderName;
    }

    public void setServiceProviderName(String serviceProviderName) {
        this.serviceProviderName = serviceProviderName;
    }
}
