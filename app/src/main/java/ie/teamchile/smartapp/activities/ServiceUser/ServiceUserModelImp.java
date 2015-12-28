package ie.teamchile.smartapp.activities.ServiceUser;

import ie.teamchile.smartapp.model.AntiDHistory;
import ie.teamchile.smartapp.model.Baby;
import ie.teamchile.smartapp.model.FeedingHistory;
import ie.teamchile.smartapp.model.HearingHistory;
import ie.teamchile.smartapp.model.NbstHistory;
import ie.teamchile.smartapp.model.Pregnancy;
import ie.teamchile.smartapp.model.ServiceProvider;
import ie.teamchile.smartapp.model.ServiceUser;
import ie.teamchile.smartapp.model.VitKHistory;
import ie.teamchile.smartapp.util.Constants;
import io.realm.Realm;

/**
 * Created by elliot on 28/12/2015.
 */
public class ServiceUserModelImp implements ServiceUserModel {
    private ServiceUserPresenter serviceUserPresenter;
    private Realm realm;
    private ServiceProvider serviceProvider;
    private ServiceUser serviceUser;
    private Baby baby;
    private Pregnancy pregnancy;

    public ServiceUserModelImp(ServiceUserPresenter serviceUserPresenter, Realm realm) {
        this.serviceUserPresenter = serviceUserPresenter;
        this.realm = realm;
    }
    @Override
    public ServiceProvider getServiceProvider() {
        if (serviceProvider == null)
            serviceProvider =  realm.where(ServiceProvider.class).findFirst();

        return serviceProvider;
    }

    @Override
    public ServiceUser getServiceUser() {
        if (serviceUser == null)
            serviceUser = realm.where(ServiceUser.class).findFirst();

        return serviceUser;
    }

    @Override
    public Baby getBaby() {
        if (baby == null) {
            baby = realm.where(Baby.class)
                    .equalTo(Constants.REALM_ID,
                            serviceUserPresenter.getRecentBaby(
                            realm.where(Baby.class).findAll()))
                    .findFirst();
        }

        return baby;
    }

    @Override
    public void updateBaby(Baby baby) {
        this.baby = baby;
    }

    @Override
    public Pregnancy getPregnancy() {
        if (pregnancy == null ) {
            pregnancy = realm.where(Pregnancy.class)
                    .equalTo(Constants.REALM_ID,
                            serviceUserPresenter.getRecentPregnancy(realm.where(Pregnancy.class).findAll()))
                    .findFirst();
        }

        return pregnancy;
    }

    @Override
    public void updatePregnancy(Pregnancy pregnancy) {
        this.pregnancy = pregnancy;
    }

    @Override
    public void deleteServiceUserFromRealm() {
        realm.beginTransaction();
        realm.allObjects(ServiceUser.class).clear();
        realm.allObjects(Pregnancy.class).clear();
        realm.allObjects(Baby.class).clear();
        realm.allObjects(AntiDHistory.class).clear();
        realm.allObjects(FeedingHistory.class).clear();
        realm.allObjects(NbstHistory.class).clear();
        realm.allObjects(HearingHistory.class).clear();
        realm.allObjects(VitKHistory.class).clear();
        realm.commitTransaction();
    }
}
