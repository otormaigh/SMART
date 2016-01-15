package ie.teamchile.smartapp.model;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by user on 5/26/15.
 */
public class ResponseServiceProvider extends RealmObject {
    private boolean active;
    private boolean admin;
    private String email;
    @PrimaryKey
    private int id;
    private String jobLevel;
    private String jobOccupation;
    private String name;
    private String password;
    private String primaryPhone;
    private String secondaryPhone;
    private String username;

    public ResponseServiceProvider() {}

    public ResponseServiceProvider(ResponseServiceProvider serviceProvider) {
        setActive(serviceProvider.isActive());
        setAdmin(serviceProvider.isAdmin());
        setEmail(serviceProvider.getEmail());
        setId(serviceProvider.getId());
        setJobLevel(serviceProvider.getJobLevel());
        setJobOccupation(serviceProvider.getJobOccupation());
        setName(serviceProvider.getName());
        setPassword(serviceProvider.getPassword());
        setPrimaryPhone(serviceProvider.getPrimaryPhone());
        setSecondaryPhone(serviceProvider.getSecondaryPhone());
        setUsername(serviceProvider.getUsername());
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public boolean isAdmin() {
        return admin;
    }

    public void setAdmin(boolean admin) {
        this.admin = admin;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
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
