package ie.teamchile.smartapp.model;

import io.realm.RealmObject;

/**
 * Created by user on 5/26/15.
 */

public class ResponseLinks extends RealmObject {
    private String serviceOptions;
    private String serviceProvider;
    private String serviceUser;
    private String announcements;
    private String hearingHistories;
    private String nbstHistories;
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

    public String getServiceUser() {
        return serviceUser;
    }

    public void setServiceUser(String serviceUser) {
        this.serviceUser = serviceUser;
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
