package ie.teamchile.smartapp.activities.ServiceUser;

import ie.teamchile.smartapp.model.Baby;
import ie.teamchile.smartapp.model.Pregnancy;
import ie.teamchile.smartapp.model.ServiceProvider;
import ie.teamchile.smartapp.model.ServiceUser;

/**
 * Created by elliot on 28/12/2015.
 */
public interface ServiceUserModel {
    ServiceProvider getServiceProvider();

    ServiceUser getServiceUser();

    Baby getBaby();

    void updateBaby(Baby baby);

    Pregnancy getPregnancy();

    void updatePregnancy(Pregnancy pregnancy);

    void deleteServiceUserFromRealm();
}
