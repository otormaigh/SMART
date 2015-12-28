package ie.teamchile.smartapp.activities.Base;

import android.content.Intent;

import java.util.List;

import ie.teamchile.smartapp.model.Baby;
import ie.teamchile.smartapp.model.Pregnancy;

/**
 * Created by elliot on 27/12/2015.
 */
public interface BasePresenter {
    int getRecentPregnancy(List<Pregnancy> pregnancyList);

    int getRecentBaby(List<Baby> babyList);

    void doLogout(Intent intent);

    void doLogoutWithoutIntent();

    void showNotification(String title, String message, Class activity);

    void getAllAppointments();
}
