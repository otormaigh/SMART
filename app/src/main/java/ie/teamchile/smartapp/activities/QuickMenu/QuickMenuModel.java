package ie.teamchile.smartapp.activities.QuickMenu;

import java.util.List;

import ie.teamchile.smartapp.model.Clinic;
import ie.teamchile.smartapp.model.ServiceOption;
import ie.teamchile.smartapp.model.ServiceProvider;
import ie.teamchile.smartapp.model.ServiceUserAction;

/**
 * Created by elliot on 27/12/2015.
 */
public interface QuickMenuModel {
    void saveServiceProviderToRealm(ServiceProvider serviceProvider);
    void saveServiceOptionsToRealm(List<ServiceOption> serviceOptions);
    void saveClinicsToRealm(List<Clinic> clinics);
    void saveServiceUserActionsToRealm(List<ServiceUserAction> serviceUserActions);
}
