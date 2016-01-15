package ie.teamchile.smartapp.activities.Base;

import android.app.NotificationManager;

/**
 * Created by elliot on 27/12/2015.
 */
public interface BaseViewSec extends BaseViewPri{
    void setContentForNav(int layout);

    void setActionBarTitle(String title);

    void createNavDrawer();

    void showNotification(String title, String message, Class activity);

    NotificationManager getNotificationManager();

    void showLogoutDialog();
}