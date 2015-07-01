package ie.teamchile.smartapp.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by user on 5/26/15.
 */
public class ServiceProvider {
    @Expose
    private Boolean active;
    @Expose
    private Boolean admin;
    @Expose
    private String email;
    @Expose
    private Integer id;
    @SerializedName("job_level")
    @Expose
    private String jobLevel;
    @SerializedName("job_occupation")
    @Expose
    private String jobOccupation;
    @Expose
    private String name;
    @Expose
    private String password;
    @SerializedName("primary_phone")
    @Expose
    private String primaryPhone;
    @SerializedName("secondary_phone")
    @Expose
    private String secondaryPhone;
    @Expose
    private String username;

    public Boolean getActive() {
        return active;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }

    public Boolean getAdmin() {
        return admin;
    }

    public void setAdmin(Boolean admin) {
        this.admin = admin;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getJobLevel() {
        return jobLevel;
    }

    public void setJobLevel(String jobLevel) {
        this.jobLevel = jobLevel;
    }

    public String getJobOccupation() {
        return jobOccupation;
    }

    public void setJobOccupation(String jobOccupation) {
        this.jobOccupation = jobOccupation;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPrimaryPhone() {
        return primaryPhone;
    }

    public void setPrimaryPhone(String primaryPhone) {
        this.primaryPhone = primaryPhone;
    }

    public String getSecondaryPhone() {
        return secondaryPhone;
    }

    public void setSecondaryPhone(String secondaryPhone) {
        this.secondaryPhone = secondaryPhone;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
