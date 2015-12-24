package ie.teamchile.smartapp.model;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by user on 7/1/15.
 */
public class PregnancyAction extends RealmObject {
    private String action;
    private boolean complete;
    private String createdAt;
    @PrimaryKey
    private int id;
    private int pregnancyId;
    private int serviceProviderId;

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public boolean isComplete() {
        return complete;
    }

    public void setComplete(boolean complete) {
        this.complete = complete;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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
}
