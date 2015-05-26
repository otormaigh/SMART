package ie.teamchile.smartapp.retrofit;

/**
 * Created by user on 5/26/15.
 */

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Links {
    @SerializedName("service_options")
    @Expose
    private String serviceOptions;
    @SerializedName("service_provider")
    @Expose
    private String serviceProvider;
    @SerializedName("service_user")
    @Expose
    private String serviceUser;
    @Expose
    private String announcements;

    /**
     * @return The announcements
     */
    public String getAnnouncements() {
        return announcements;
    }

    /**
     * @param announcements The announcements
     */
    public void setAnnouncements(String announcements) {
        this.announcements = announcements;
    }

    /**
     * @return The serviceOptions
     */
    public String getServiceOptions() {
        return serviceOptions;
    }

    /**
     * @param serviceOptions The service_options
     */
    public void setServiceOptions(String serviceOptions) {
        this.serviceOptions = serviceOptions;
    }

    /**
     * @return The serviceProvider
     */
    public String getServiceProvider() {
        return serviceProvider;
    }

    /**
     * @param serviceProvider The service_provider
     */
    public void setServiceProvider(String serviceProvider) {
        this.serviceProvider = serviceProvider;
    }

    public Links withServiceProvider(String serviceProvider) {
        this.serviceProvider = serviceProvider;
        return this;
    }

    /**
     * @return The serviceUser
     */
    public String getServiceUser() {
        return serviceUser;
    }

    /**
     * @param serviceUser The service_user
     */
    public void setServiceUser(String serviceUser) {
        this.serviceUser = serviceUser;
    }

    public Links withServiceUser(String serviceUser) {
        this.serviceUser = serviceUser;
        return this;
    }
}
