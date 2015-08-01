package ie.teamchile.smartapp.model;

/**
 * Created by user on 6/23/15.
 */
public class NbstHistory {
    private Integer babyId;
    private String createdAt;
    private Integer id;
    private String nbst;
    private Integer serviceProviderId;
    private String serviceProviderName;

    public Integer getBabyId() {
        return babyId;
    }

    public void setBabyId(Integer babyId) {
        this.babyId = babyId;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getNbst() {
        return nbst;
    }

    public void setNbst(String nbst) {
        this.nbst = nbst;
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