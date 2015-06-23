package ie.teamchile.smartapp.model;

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
    @SerializedName("hearing_histories")
    @Expose
    private String hearingHistories;
    @SerializedName("nbst_histories")
    @Expose
    private String nbstHistories;
    @SerializedName("vit_k_histories")
    @Expose
    private String vitKHistories;

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

    public String getHearingHistories() {
        return hearingHistories;
    }

    /**
     * @param hearingHistories The hearing_histories
     */
    public void setHearingHistories(String hearingHistories) {
        this.hearingHistories = hearingHistories;
    }

    /**
     * @return The nbstHistories
     */
    public String getNbstHistories() {
        return nbstHistories;
    }

    /**
     * @param nbstHistories The nbst_histories
     */
    public void setNbstHistories(String nbstHistories) {
        this.nbstHistories = nbstHistories;
    }

    /**
     * @return The vitKHistories
     */
    public String getVitKHistories() {
        return vitKHistories;
    }

    /**
     * @param vitKHistories The vit_k_histories
     */
    public void setVitKHistories(String vitKHistories) {
        this.vitKHistories = vitKHistories;
    }
}
