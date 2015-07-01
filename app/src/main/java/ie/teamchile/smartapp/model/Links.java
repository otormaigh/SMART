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

    public String getAnnouncements() {
        return announcements;
    }

    public void setAnnouncements(String announcements) {
        this.announcements = announcements;
    }

    public String getServiceOptions() {
        return serviceOptions;
    }

    public void setServiceOptions(String serviceOptions) {
        this.serviceOptions = serviceOptions;
    }

    public String getServiceProvider() {
        return serviceProvider;
    }

    public void setServiceProvider(String serviceProvider) {
        this.serviceProvider = serviceProvider;
    }

    public Links withServiceProvider(String serviceProvider) {
        this.serviceProvider = serviceProvider;
        return this;
    }

    public String getServiceUser() {
        return serviceUser;
    }

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

    public void setHearingHistories(String hearingHistories) {
        this.hearingHistories = hearingHistories;
    }

    public String getNbstHistories() {
        return nbstHistories;
    }

    public void setNbstHistories(String nbstHistories) {
        this.nbstHistories = nbstHistories;
    }

    public String getVitKHistories() {
        return vitKHistories;
    }

    public void setVitKHistories(String vitKHistories) {
        this.vitKHistories = vitKHistories;
    }
}
