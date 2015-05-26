package ie.teamchile.smartapp.retrofit;

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

    /**
     * @return The active
     */
    public Boolean getActive() {
        return active;
    }

    /**
     * @param active The active
     */
    public void setActive(Boolean active) {
        this.active = active;
    }

    /**
     * @return The admin
     */
    public Boolean getAdmin() {
        return admin;
    }

    /**
     * @param admin The admin
     */
    public void setAdmin(Boolean admin) {
        this.admin = admin;
    }

    /**
     * @return The email
     */
    public String getEmail() {
        return email;
    }

    /**
     * @param email The email
     */
    public void setEmail(String email) {
        this.email = email;
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
     * @return The jobLevel
     */
    public String getJobLevel() {
        return jobLevel;
    }

    /**
     * @param jobLevel The job_level
     */
    public void setJobLevel(String jobLevel) {
        this.jobLevel = jobLevel;
    }

    /**
     * @return The jobOccupation
     */
    public String getJobOccupation() {
        return jobOccupation;
    }

    /**
     * @param jobOccupation The job_occupation
     */
    public void setJobOccupation(String jobOccupation) {
        this.jobOccupation = jobOccupation;
    }

    /**
     * @return The name
     */
    public String getName() {
        return name;
    }

    /**
     * @param name The name
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * @return The password
     */
    public String getPassword() {
        return password;
    }

    /**
     * @param password The password
     */
    public void setPassword(String password) {
        this.password = password;
    }

    /**
     * @return The primaryPhone
     */
    public String getPrimaryPhone() {
        return primaryPhone;
    }

    /**
     * @param primaryPhone The primary_phone
     */
    public void setPrimaryPhone(String primaryPhone) {
        this.primaryPhone = primaryPhone;
    }

    /**
     * @return The secondaryPhone
     */
    public String getSecondaryPhone() {
        return secondaryPhone;
    }

    /**
     * @param secondaryPhone The secondary_phone
     */
    public void setSecondaryPhone(String secondaryPhone) {
        this.secondaryPhone = secondaryPhone;
    }

    /**
     * @return The username
     */
    public String getUsername() {
        return username;
    }

    /**
     * @param username The username
     */
    public void setUsername(String username) {
        this.username = username;
    }
}
