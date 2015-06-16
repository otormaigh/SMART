package ie.teamchile.smartapp.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by user on 6/16/15.
 */
public class PregnancyNote {
    @SerializedName("created_at")
    @Expose
    private String createdAt;
    @Expose
    private Integer id;
    @Expose
    private String note;
    @SerializedName("pregnancy_id")
    @Expose
    private Integer pregnancyId;
    @SerializedName("service_provider_id")
    @Expose
    private Integer serviceProviderId;

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
     * @return The note
     */
    public String getNote() {
        return note;
    }

    /**
     * @param note The note
     */
    public void setNote(String note) {
        this.note = note;
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
}
