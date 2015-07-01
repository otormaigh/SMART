package ie.teamchile.smartapp.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by user on 6/23/15.
 */
public class FeedingHistory {
    @SerializedName("created_at")
    @Expose
    private String createdAt;
    @Expose
    private String feeding;
    @Expose
    private Integer id;
    @SerializedName("pregnancy_id")
    @Expose
    private Integer pregnancyId;
    @SerializedName("service_provider_id")
    @Expose
    private Integer serviceProviderId;
    @SerializedName("service_provider_name")
    @Expose
    private String serviceProviderName;

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public String getFeeding() {
        return feeding;
    }

    public void setFeeding(String feeding) {
        this.feeding = feeding;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getPregnancyId() {
        return pregnancyId;
    }

    public void setPregnancyId(Integer pregnancyId) {
        this.pregnancyId = pregnancyId;
    }

    public Integer getServiceProviderId() {
        return serviceProviderId;
    }

    public void setServiceProviderId(Integer serviceProviderId) {
        this.serviceProviderId = serviceProviderId;
    }

    public String getServiceProviderName() {
        return serviceProviderName;
    }

    public void setServiceProviderName(String serviceProviderName) {
        this.serviceProviderName = serviceProviderName;
    }

}
