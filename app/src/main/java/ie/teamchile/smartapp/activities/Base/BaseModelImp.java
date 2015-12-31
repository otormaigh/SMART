package ie.teamchile.smartapp.activities.Base;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;

import java.lang.ref.WeakReference;
import java.util.Calendar;
import java.util.List;
import java.util.Map;

import ie.teamchile.smartapp.R;
import ie.teamchile.smartapp.model.AntiDHistory;
import ie.teamchile.smartapp.model.Appointment;
import ie.teamchile.smartapp.model.Baby;
import ie.teamchile.smartapp.model.BaseResponseModel;
import ie.teamchile.smartapp.model.Clinic;
import ie.teamchile.smartapp.model.FeedingHistory;
import ie.teamchile.smartapp.model.HearingHistory;
import ie.teamchile.smartapp.model.Login;
import ie.teamchile.smartapp.model.NbstHistory;
import ie.teamchile.smartapp.model.Pregnancy;
import ie.teamchile.smartapp.model.PregnancyNote;
import ie.teamchile.smartapp.model.ServiceOption;
import ie.teamchile.smartapp.model.ServiceProvider;
import ie.teamchile.smartapp.model.ServiceUser;
import ie.teamchile.smartapp.model.ServiceUserAction;
import ie.teamchile.smartapp.model.VitKHistory;
import ie.teamchile.smartapp.util.Constants;
import ie.teamchile.smartapp.util.SharedPrefs;
import io.realm.Realm;
import timber.log.Timber;

/**
 * Created by elliot on 28/12/2015.
 */
public class BaseModelImp implements BaseModel {
    private BasePresenter basePresenter;
    private WeakReference<Activity> weakActivity;
    private Realm realm;
    private SharedPrefs sharedPrefs;

    public BaseModelImp(BasePresenter basePresenter) {
        this.basePresenter = basePresenter;
        realm = basePresenter.getEncryptedRealm();
    }

    public BaseModelImp(WeakReference<Activity> weakActivity) {
        this.weakActivity = weakActivity;
    }

    public BaseModelImp(BasePresenter basePresenter, WeakReference<Activity> weakActivity) {
        this.basePresenter = basePresenter;
        this.weakActivity = weakActivity;
        realm = basePresenter.getEncryptedRealm();
        sharedPrefs = new SharedPrefs();
    }

    @Override
    public void deleteSingletonInstance() {
        BaseResponseModel.getInstance().deleteInstance();
    }

    @Override
    public void getLoginSharedPrefs() {
        SharedPrefs sharedPrefs = new SharedPrefs();
        Timber.d("getSharedPrefs called");
        SharedPreferences prefs = weakActivity.get().getSharedPreferences(weakActivity.get().getString(R.string.app_name), Context.MODE_PRIVATE);
        Map<String, ?> prefsMap = prefs.getAll();
        for (Map.Entry<String, ?> entry : prefsMap.entrySet()) {
            Timber.d("key = " + entry.getKey());
            if (entry.getKey().contains(Constants.APPOINTMENT_POST)) {
                Timber.d("get key = " + prefs.getString(entry.getKey(), ""));
                sharedPrefs.postAppointment(
                        sharedPrefs.getObjectFromString(
                                prefs.getString(entry.getKey(), "")),
                        weakActivity.get(), entry.getKey());
            }
        }
    }

    @Override
    public long getTimeFromPrefs() {
        return sharedPrefs.getLongPrefs(weakActivity.get(), Constants.SHARED_PREFS_SPLASH_LOG);
    }

    @Override
    public void saveAppointmentsToRealm(List<Appointment> appointments) {
        realm.beginTransaction();
        realm.copyToRealmOrUpdate(appointments);
        realm.commitTransaction();
    }

    @Override
    public void saveServiceUserToRealm(BaseResponseModel baseResponseModel) {
        realm.beginTransaction();
        realm.copyToRealmOrUpdate(baseResponseModel.getServiceUsers());
        realm.copyToRealmOrUpdate(baseResponseModel.getPregnancies());
        realm.copyToRealmOrUpdate(baseResponseModel.getBabies());
        realm.copyToRealmOrUpdate(baseResponseModel.getAntiDHistories());
        realm.commitTransaction();
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

    @Override
    public void saveVitKToRealm(List<VitKHistory> vitKHistories) {
        realm.beginTransaction();
        realm.copyToRealmOrUpdate(vitKHistories);
        realm.commitTransaction();
    }

    @Override
    public void saveHearingToRealm(List<HearingHistory> hearingHistories) {
        realm.beginTransaction();
        realm.copyToRealmOrUpdate(hearingHistories);
        realm.commitTransaction();
    }

    @Override
    public void saveNbstToRealm(List<NbstHistory> nbstHistories) {
        realm.beginTransaction();
        realm.copyToRealmOrUpdate(nbstHistories);
        realm.commitTransaction();
    }

    @Override
    public void saveFeedingHistoriesToRealm(List<FeedingHistory> feedingHistories) {
        realm.beginTransaction();
        realm.copyToRealmOrUpdate(feedingHistories);
        realm.commitTransaction();
    }

    @Override
    public void saveAppointmentToRealm(Appointment appointment) {
        realm.beginTransaction();
        realm.copyToRealmOrUpdate(appointment);
        realm.commitTransaction();
    }

    @Override
    public void saveLoginToRealm(Login login) {
        realm.beginTransaction();
        login.setLoggedIn(true);
        realm.copyToRealmOrUpdate(login);
        realm.commitTransaction();
    }

    @Override
    public void deleteRealmLogin() {
        if (realm.where(Login.class).findFirst() != null) {
            realm.beginTransaction();
            realm.where(Login.class).findFirst().setLoggedIn(false);
            realm.commitTransaction();
        }
    }

    @Override
    public ServiceProvider getServiceProvider() {
        return realm.where(ServiceProvider.class).findFirst();
    }

    @Override
    public ServiceUser getServiceUser() {
        return realm.where(ServiceUser.class).findFirst();
    }

    @Override
    public Baby getBaby() {
        return realm.where(Baby.class)
                .equalTo(Constants.REALM_ID,
                        basePresenter.getRecentBaby(
                                realm.where(Baby.class).findAll()))
                .findFirst();
    }

    @Override
    public void updateBaby(Baby baby) {
        realm.beginTransaction();
        realm.copyToRealmOrUpdate(baby);
        realm.commitTransaction();
    }

    @Override
    public Pregnancy getPregnancy() {
        return realm.where(Pregnancy.class)
                .equalTo(Constants.REALM_ID,
                        basePresenter.getRecentPregnancy(
                                realm.where(Pregnancy.class).findAll()))
                .findFirst();
    }

    @Override
    public void updatePregnancy(Pregnancy pregnancy) {
        realm.beginTransaction();
        realm.copyToRealmOrUpdate(pregnancy);
        realm.commitTransaction();
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
}
