package ie.teamchile.smartapp.activities.QuickMenu;

import java.util.List;

import ie.teamchile.smartapp.model.Clinic;
import ie.teamchile.smartapp.model.ServiceOption;
import ie.teamchile.smartapp.model.ServiceProvider;
import ie.teamchile.smartapp.model.ServiceUserAction;
import io.realm.Realm;

/**
 * Created by elliot on 27/12/2015.
 */
public class QuickMenuModelImp implements QuickMenuModel {
    private Realm realm;

    public QuickMenuModelImp(QuickMenuPresenter quickMenuPresenter) {
        realm = quickMenuPresenter.getEncryptedRealm();
    }

    @Override
    public void saveServiceProviderToRealm(ServiceProvider serviceProvider) {
        realm.beginTransaction();
        realm.copyToRealmOrUpdate(serviceProvider);
        realm.commitTransaction();
    }

    @Override
    public void saveServiceOptionsToRealm(List<ServiceOption> serviceOptions) {
        realm.beginTransaction();
        realm.copyToRealmOrUpdate(serviceOptions);
        realm.commitTransaction();
    }

    @Override
    public void saveClinicsToRealm(List<Clinic> clinics) {
        realm.beginTransaction();
        realm.copyToRealmOrUpdate(clinics);
        realm.commitTransaction();
    }

    @Override
    public void saveServiceUserActionsToRealm(List<ServiceUserAction> serviceUserActions) {
        realm.beginTransaction();
        realm.copyToRealmOrUpdate(serviceUserActions);
        realm.commitTransaction();
    }
}
