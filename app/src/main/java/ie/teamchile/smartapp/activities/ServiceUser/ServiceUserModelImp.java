package ie.teamchile.smartapp.activities.ServiceUser;

import java.util.Calendar;
import java.util.List;

import ie.teamchile.smartapp.activities.Base.BaseModelImp;
import ie.teamchile.smartapp.model.AntiDHistory;
import ie.teamchile.smartapp.model.Baby;
import ie.teamchile.smartapp.model.FeedingHistory;
import ie.teamchile.smartapp.model.HearingHistory;
import ie.teamchile.smartapp.model.NbstHistory;
import ie.teamchile.smartapp.model.Pregnancy;
import ie.teamchile.smartapp.model.PregnancyNote;
import ie.teamchile.smartapp.model.ServiceProvider;
import ie.teamchile.smartapp.model.ServiceUser;
import ie.teamchile.smartapp.model.VitKHistory;
import ie.teamchile.smartapp.util.Constants;
import io.realm.Realm;

/**
 * Created by elliot on 28/12/2015.
 */
public class ServiceUserModelImp extends BaseModelImp implements ServiceUserModel {
    private ServiceUserPresenter serviceUserPresenter;
    private Realm realm;
    private ServiceProvider serviceProvider;
    private ServiceUser serviceUser;
    private Baby baby;
    private Pregnancy pregnancy;

    public ServiceUserModelImp(ServiceUserPresenter serviceUserPresenter) {
        super(serviceUserPresenter);
        this.serviceUserPresenter = serviceUserPresenter;
        realm = serviceUserPresenter.getEncryptedRealm();
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
        realm.beginTransaction();
        realm.copyToRealmOrUpdate(baby);
        realm.commitTransaction();

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
        realm.beginTransaction();
        realm.copyToRealmOrUpdate(pregnancy);
        realm.commitTransaction();

        this.pregnancy = pregnancy;
    }

    @Override
    public void updateAntiD() {
        AntiDHistory antiDHistory = new AntiDHistory();
        antiDHistory.setAntiD(getPregnancy().getAntiD());
        antiDHistory.setCreatedAt(Calendar.getInstance().getTime());
        antiDHistory.setServiceProviderName(getServiceProvider().getName());

        realm.beginTransaction();
        realm.copyToRealmOrUpdate(antiDHistory);
        realm.commitTransaction();
    }

    @Override
    public void updateFeeding() {
        FeedingHistory feedingHistory = new FeedingHistory();
        feedingHistory.setFeeding(getPregnancy().getFeeding());
        feedingHistory.setCreatedAt(Calendar.getInstance().getTime());
        feedingHistory.setServiceProviderName(getServiceProvider().getName());

        realm.beginTransaction();
        realm.copyToRealmOrUpdate(feedingHistory);
        realm.commitTransaction();
    }

    @Override
    public void updateVitK() {
        VitKHistory vitKHistory = new VitKHistory();
        vitKHistory.setVitK(getBaby().getVitK());
        vitKHistory.setCreatedAt(Calendar.getInstance().getTime());
        vitKHistory.setServiceProviderName(getServiceProvider().getName());

        realm.beginTransaction();
        realm.copyToRealmOrUpdate(vitKHistory);
        realm.commitTransaction();
    }

    @Override
    public void updateHearing() {
        HearingHistory hearingHistory = new HearingHistory();
        hearingHistory.setHearing(getBaby().getHearing());
        hearingHistory.setCreatedAt(Calendar.getInstance().getTime());
        hearingHistory.setServiceProviderName(getServiceProvider().getName());

        realm.beginTransaction();
        realm.copyToRealmOrUpdate(hearingHistory);
        realm.commitTransaction();
    }

    @Override
    public void updateNbst() {
        NbstHistory nbstHistory = new NbstHistory();
        nbstHistory.setNbst(getBaby().getNbst());
        nbstHistory.setCreatedAt(Calendar.getInstance().getTime());
        nbstHistory.setServiceProviderName(getServiceProvider().getName());

        realm.beginTransaction();
        realm.copyToRealmOrUpdate(nbstHistory);
        realm.commitTransaction();
    }

    @Override
    public void updatePregnancyNotes(List<PregnancyNote> pregnancyNotes) {
        realm.beginTransaction();
        realm.copyToRealmOrUpdate(pregnancyNotes);
        realm.commitTransaction();
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
