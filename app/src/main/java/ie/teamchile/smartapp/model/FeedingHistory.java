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

    /**
     * @return The createdAt
     */
    public String getCreatedAt() {
        return createdAt;
    }

    /**
     * @param createdAt The created_at
     */
    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    /**
     * @return The feeding
     */
    public String getFeeding() {
        return feeding;
    }

    /**
     * @param feeding The feeding
     */
    public void setFeeding(String feeding) {
        this.feeding = feeding;
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
     * @return The pregnancyId
     */
    public Integer getPregnancyId() {
        return pregnancyId;
    }

    /**
     * @param pregnancyId The pregnancy_id
     */
    public void setPregnancyId(Integer pregnancyId) {
        this.pregnancyId = pregnancyId;
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

    /**
     * @return The serviceProviderName
     */
    public String getServiceProviderName() {
        return serviceProviderName;
    }

    /**
     * @param serviceProviderName The service_provider_name
     */
    public void setServiceProviderName(String serviceProviderName) {
        this.serviceProviderName = serviceProviderName;
    }

}
