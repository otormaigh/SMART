package ie.teamchile.smartapp.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by user on 6/16/15.
 */
public class AntiDHistory {
    @Expose
    private Integer id;
    @SerializedName("anti_d")
    @Expose
    private String antiD;
    @SerializedName("created_at")
    @Expose
    private String createdAt;
    @SerializedName("pregnancy_id")
    @Expose
    private Integer pregnancyId;
    @SerializedName("service_provider_name")
    @Expose
    private String serviceProviderName;

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
     * @return The antiD
     */
    public String getAntiD() {
        return antiD;
    }

    /**
     * @param antiD The anti_d
     */
    public void setAntiD(String antiD) {
        this.antiD = antiD;
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

    public String getServiceProviderName() {
        return serviceProviderName;
    }

    public void setServiceProviderName(String serviceProviderName) {
        this.serviceProviderName = serviceProviderName;
    }

}
