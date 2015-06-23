package ie.teamchile.smartapp.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by user on 6/23/15.
 */
public class HearingHistory {
    @SerializedName("baby_id")
    @Expose
    private Integer babyId;
    @SerializedName("created_at")
    @Expose
    private String createdAt;
    @Expose
    private String hearing;
    @Expose
    private Integer id;
    @SerializedName("service_provider_id")
    @Expose
    private Integer serviceProviderId;
    @SerializedName("service_provider_name")
    @Expose
    private String serviceProviderName;

    /**
     * @return The babyId
     */
    public Integer getBabyId() {
        return babyId;
    }

    /**
     * @param babyId The baby_id
     */
    public void setBabyId(Integer babyId) {
        this.babyId = babyId;
    }

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
     * @return The hearing
     */
    public String getHearing() {
        return hearing;
    }

    /**
     * @param hearing The hearing
     */
    public void setHearing(String hearing) {
        this.hearing = hearing;
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
