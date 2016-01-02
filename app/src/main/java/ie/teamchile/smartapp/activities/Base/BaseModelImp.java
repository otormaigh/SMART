package ie.teamchile.smartapp.activities.Base;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import java.io.File;
import java.lang.ref.WeakReference;
import java.util.Calendar;
import java.util.List;
import java.util.Map;

import ie.teamchile.smartapp.R;
import ie.teamchile.smartapp.api.SmartApiClient;
import ie.teamchile.smartapp.model.ResponseAnnouncement;
import ie.teamchile.smartapp.model.ResponseAntiDHistory;
import ie.teamchile.smartapp.model.ResponseAppointment;
import ie.teamchile.smartapp.model.ResponseBaby;
import ie.teamchile.smartapp.model.ResponseBase;
import ie.teamchile.smartapp.model.ResponseClinic;
import ie.teamchile.smartapp.model.ResponseClinicTimeRecord;
import ie.teamchile.smartapp.model.ResponseClinicalFields;
import ie.teamchile.smartapp.model.ResponseDays;
import ie.teamchile.smartapp.model.ResponseFeedingHistory;
import ie.teamchile.smartapp.model.ResponseHearingHistory;
import ie.teamchile.smartapp.model.ResponseLinks;
import ie.teamchile.smartapp.model.ResponseLogin;
import ie.teamchile.smartapp.model.ResponseNbstHistory;
import ie.teamchile.smartapp.model.ResponsePersonalFields;
import ie.teamchile.smartapp.model.ResponsePregnancy;
import ie.teamchile.smartapp.model.ResponsePregnancyAction;
import ie.teamchile.smartapp.model.ResponsePregnancyNote;
import ie.teamchile.smartapp.model.RealmInteger;
import ie.teamchile.smartapp.model.RealmString;
import ie.teamchile.smartapp.model.ResponseServiceOption;
import ie.teamchile.smartapp.model.ResponseServiceProvider;
import ie.teamchile.smartapp.model.ResponseServiceUser;
import ie.teamchile.smartapp.model.ResponseServiceUserAction;
import ie.teamchile.smartapp.model.ResponseVitKHistory;
import ie.teamchile.smartapp.util.Constants;
import ie.teamchile.smartapp.util.SharedPrefs;
import io.realm.Realm;
import timber.log.Timber;

/**
 * Created by elliot on 28/12/2015.
 */
public class BaseModelImp implements BaseModel {
    private BasePresenter presenter;
    private WeakReference<Activity> weakActivity;
    private Realm realm;
    private SharedPrefs sharedPrefs;

    public BaseModelImp(BasePresenter presenter) {
        this.presenter = presenter;
        realm = presenter.getEncryptedRealm();
    }

    public BaseModelImp(BasePresenter presenter, WeakReference<Activity> weakActivity) {
        this.presenter = presenter;
        this.weakActivity = weakActivity;
        realm = presenter.getEncryptedRealm();
        sharedPrefs = new SharedPrefs();
    }

    @Override
    public void deleteSingletonInstance() {
        ResponseBase.getInstance().deleteInstance();
    }

    @Override
    public ResponseLogin getLogin() {
        return realm.where(ResponseLogin.class).findFirst();
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
                        weakActivity.get(),
                        realm,
                        entry.getKey());
            }
        }
    }

    @Override
    public long getTimeFromPrefs() {
        return sharedPrefs.getLongPrefs(weakActivity.get(), Constants.SHARED_PREFS_SPLASH_LOG);
    }

    @Override
    public void saveAppointmentsToRealm(List<ResponseAppointment> responseAppointments) {
        realm.beginTransaction();
        realm.copyToRealmOrUpdate(responseAppointments);
        realm.commitTransaction();
    }

    @Override
    public void saveServiceUserToRealm(ResponseBase responseBase) {
        realm.beginTransaction();
        realm.copyToRealmOrUpdate(responseBase.getResponseServiceUsers());
        realm.copyToRealmOrUpdate(responseBase.getPregnancies());
        realm.copyToRealmOrUpdate(responseBase.getBabies());
        realm.copyToRealmOrUpdate(responseBase.getAntiDHistories());
        realm.commitTransaction();
    }

    @Override
    public void saveServiceProviderToRealm(ResponseServiceProvider responseServiceProvider) {
        realm.beginTransaction();
        realm.copyToRealmOrUpdate(responseServiceProvider);
        realm.commitTransaction();
    }

    @Override
    public void saveServiceOptionsToRealm(List<ResponseServiceOption> responseServiceOptions) {
        realm.beginTransaction();
        realm.copyToRealmOrUpdate(responseServiceOptions);
        realm.commitTransaction();
    }

    @Override
    public List<ResponseClinic> getClinics() {
        return realm.where(ResponseClinic.class).findAll();
    }

    @Override
    public void updateClinics(List<ResponseClinic> responseClinics) {
        realm.beginTransaction();
        realm.copyToRealmOrUpdate(responseClinics);
        realm.commitTransaction();
    }

    @Override
    public List<ResponseClinicTimeRecord> getClinicTimeRecords() {
        return realm.where(ResponseClinicTimeRecord.class).findAll();
    }

    @Override
    public void updateClinicTimeRecords(List<ResponseClinicTimeRecord> responseClinicTimeRecords) {
        realm.beginTransaction();
        realm.copyToRealmOrUpdate(responseClinicTimeRecords);
        realm.commitTransaction();
    }

    @Override
    public void updateClinicTimeRecord(ResponseClinicTimeRecord responseClinicTimeRecord) {
        realm.beginTransaction();
        realm.copyToRealmOrUpdate(responseClinicTimeRecord);
        realm.commitTransaction();
    }

    @Override
    public void saveServiceUserActionsToRealm(List<ResponseServiceUserAction> responseServiceUserActions) {
        realm.beginTransaction();
        realm.copyToRealmOrUpdate(responseServiceUserActions);
        realm.commitTransaction();
    }

    @Override
    public List<ResponsePregnancyNote> getPregnancyNotes() {
        return realm.where(ResponsePregnancyNote.class).findAll();
    }

    @Override
    public void updatePregnancyNote(ResponsePregnancyNote responsePregnancyNote) {
        realm.beginTransaction();
        realm.copyToRealmOrUpdate(responsePregnancyNote);
        realm.commitTransaction();
    }

    @Override
    public void updatePregnancyNotes(List<ResponsePregnancyNote> responsePregnancyNotes) {
        realm.beginTransaction();
        realm.copyToRealmOrUpdate(responsePregnancyNotes);
        realm.commitTransaction();
    }

    @Override
    public void deleteServiceUserFromRealm() {
        realm.beginTransaction();
        realm.allObjects(ResponseServiceUser.class).clear();
        realm.allObjects(ResponsePregnancy.class).clear();
        realm.allObjects(ResponseBaby.class).clear();
        realm.allObjects(ResponseAntiDHistory.class).clear();
        realm.allObjects(ResponseFeedingHistory.class).clear();
        realm.allObjects(ResponseNbstHistory.class).clear();
        realm.allObjects(ResponseHearingHistory.class).clear();
        realm.allObjects(ResponseVitKHistory.class).clear();
        realm.commitTransaction();
    }

    @Override
    public void saveVitKToRealm(List<ResponseVitKHistory> vitKHistories) {
        realm.beginTransaction();
        realm.copyToRealmOrUpdate(vitKHistories);
        realm.commitTransaction();
    }

    @Override
    public void saveHearingToRealm(List<ResponseHearingHistory> hearingHistories) {
        realm.beginTransaction();
        realm.copyToRealmOrUpdate(hearingHistories);
        realm.commitTransaction();
    }

    @Override
    public void saveNbstToRealm(List<ResponseNbstHistory> nbstHistories) {
        realm.beginTransaction();
        realm.copyToRealmOrUpdate(nbstHistories);
        realm.commitTransaction();
    }

    @Override
    public void saveFeedingHistoriesToRealm(List<ResponseFeedingHistory> feedingHistories) {
        realm.beginTransaction();
        realm.copyToRealmOrUpdate(feedingHistories);
        realm.commitTransaction();
    }

    @Override
    public void saveAppointmentToRealm(ResponseAppointment responseAppointment) {
        realm.beginTransaction();
        realm.copyToRealmOrUpdate(responseAppointment);
        realm.commitTransaction();
    }

    @Override
    public void saveLoginToRealm(ResponseLogin responseLogin) {
        realm.beginTransaction();
        responseLogin.setLoggedIn(true);
        realm.copyToRealmOrUpdate(responseLogin);
        realm.commitTransaction();
    }

    @Override
    public void deleteRealmLogin() {
        realm.beginTransaction();
        realm.clear(ResponseLogin.class);
        realm.commitTransaction();
    }

    @Override
    public ResponseServiceProvider getServiceProvider() {
        return realm.where(ResponseServiceProvider.class).findFirst();
    }

    @Override
    public ResponseServiceUser getServiceUser() {
        return realm.where(ResponseServiceUser.class).findFirst();
    }

    @Override
    public List<ResponseServiceUser> getServiceUsers() {
        return realm.where(ResponseServiceUser.class).findAll();
    }

    @Override
    public ResponseBaby getBaby() {
        return realm.where(ResponseBaby.class)
                .equalTo(Constants.REALM_ID,
                        presenter.getRecentBaby(
                                realm.where(ResponseBaby.class).findAll()))
                .findFirst();
    }

    @Override
    public void updateBaby(ResponseBaby responseBaby) {
        realm.beginTransaction();
        realm.copyToRealmOrUpdate(responseBaby);
        realm.commitTransaction();
    }

    @Override
    public ResponsePregnancy getPregnancy() {
        return realm.where(ResponsePregnancy.class)
                .equalTo(Constants.REALM_ID,
                        presenter.getRecentPregnancy(
                                realm.where(ResponsePregnancy.class).findAll()))
                .findFirst();
    }

    @Override
    public void updatePregnancy(ResponsePregnancy responsePregnancy) {
        realm.beginTransaction();
        realm.copyToRealmOrUpdate(responsePregnancy);
        realm.commitTransaction();
    }

    @Override
    public void updateAntiD() {
        ResponseAntiDHistory responseAntiDHistory = new ResponseAntiDHistory();
        responseAntiDHistory.setAntiD(getPregnancy().getAntiD());
        responseAntiDHistory.setCreatedAt(Calendar.getInstance().getTime());
        responseAntiDHistory.setServiceProviderName(getServiceProvider().getName());

        realm.beginTransaction();
        realm.copyToRealmOrUpdate(responseAntiDHistory);
        realm.commitTransaction();
    }

    @Override
    public void updateFeeding() {
        ResponseFeedingHistory responseFeedingHistory = new ResponseFeedingHistory();
        responseFeedingHistory.setFeeding(getPregnancy().getFeeding());
        responseFeedingHistory.setCreatedAt(Calendar.getInstance().getTime());
        responseFeedingHistory.setServiceProviderName(getServiceProvider().getName());

        realm.beginTransaction();
        realm.copyToRealmOrUpdate(responseFeedingHistory);
        realm.commitTransaction();
    }

    @Override
    public void updateVitK() {
        ResponseVitKHistory responseVitKHistory = new ResponseVitKHistory();
        responseVitKHistory.setVitK(getBaby().getVitK());
        responseVitKHistory.setCreatedAt(Calendar.getInstance().getTime());
        responseVitKHistory.setServiceProviderName(getServiceProvider().getName());

        realm.beginTransaction();
        realm.copyToRealmOrUpdate(responseVitKHistory);
        realm.commitTransaction();
    }

    @Override
    public void updateHearing() {
        ResponseHearingHistory responseHearingHistory = new ResponseHearingHistory();
        responseHearingHistory.setHearing(getBaby().getHearing());
        responseHearingHistory.setCreatedAt(Calendar.getInstance().getTime());
        responseHearingHistory.setServiceProviderName(getServiceProvider().getName());

        realm.beginTransaction();
        realm.copyToRealmOrUpdate(responseHearingHistory);
        realm.commitTransaction();
    }

    @Override
    public void updateNbst() {
        ResponseNbstHistory responseNbstHistory = new ResponseNbstHistory();
        responseNbstHistory.setNbst(getBaby().getNbst());
        responseNbstHistory.setCreatedAt(Calendar.getInstance().getTime());
        responseNbstHistory.setServiceProviderName(getServiceProvider().getName());

        realm.beginTransaction();
        realm.copyToRealmOrUpdate(responseNbstHistory);
        realm.commitTransaction();
    }

    @Override
    public ResponseAppointment getAppointmentById(int appointmentId) {
        return realm.where(ResponseAppointment.class).equalTo(Constants.REALM_ID, appointmentId).findFirst();
    }

    @Override
    public List<ResponseAppointment> getAppointments() {
        return realm.where(ResponseAppointment.class).findAll();
    }

    @Override
    public void updateAppointment(ResponseAppointment responseAppointment) {
        realm.beginTransaction();
        realm.copyToRealmOrUpdate(responseAppointment);
        realm.commitTransaction();
    }

    @Override
    public void updateAppointments(List<ResponseAppointment> responseAppointments) {
        realm.beginTransaction();
        realm.copyToRealmOrUpdate(responseAppointments);
        realm.commitTransaction();
    }

    @Override
    public List<ResponseServiceOption> getServiceOptions() {
        return realm.where(ResponseServiceOption.class).findAll();
    }

    @Override
    public void clearData() {
        startPurge();
        deleteSharedPref();
        deleteRealm();
        SmartApiClient.clearApiClient();
    }

    private void startPurge() {
        ResponseBase.getInstance().deleteInstance();
        trimCache(weakActivity.get());
    }

    private void trimCache(Context context) {
        try {
            File dir = context.getCacheDir();
            if (dir != null && dir.isDirectory()) {
                deleteDir(dir);
            }
        } catch (Exception e) {
            Timber.e(Log.getStackTraceString(e));
        }
    }

    private boolean deleteDir(File dir) {
        if (dir != null && dir.isDirectory()) {
            String[] children = dir.list();
            int length = children.length;
            for (int i = 0; i < length; i++) {
                return deleteDir(new File(dir, children[i]));
            }
        }
        return dir.delete();
    }

    private void deleteSharedPref() {
        sharedPrefs.deletePrefs(weakActivity.get(), "appts_got");
        sharedPrefs.deletePrefs(weakActivity.get(), "clinic_started");
        sharedPrefs.deletePrefs(weakActivity.get(), "reuse");
        sharedPrefs.deletePrefs(weakActivity.get(), "root_check");
    }

    private void deleteRealm() {
        realm.beginTransaction();
        realm.clear(ResponseAnnouncement.class);
        realm.clear(ResponseAppointment.class);
        realm.clear(ResponseBaby.class);
        realm.clear(ResponseClinic.class);
        realm.clear(ResponseClinicalFields.class);
        realm.clear(ResponseClinicTimeRecord.class);
        realm.clear(ResponseDays.class);
        realm.clear(ResponseFeedingHistory.class);
        realm.clear(ResponseHearingHistory.class);
        realm.clear(ResponseLinks.class);
        realm.clear(ResponseLogin.class);
        realm.clear(ResponseNbstHistory.class);
        realm.clear(ResponsePersonalFields.class);
        realm.clear(ResponsePregnancy.class);
        realm.clear(ResponsePregnancyAction.class);
        realm.clear(ResponsePregnancyNote.class);
        realm.clear(RealmInteger.class);
        realm.clear(RealmString.class);
        realm.clear(ResponseServiceOption.class);
        realm.clear(ResponseServiceProvider.class);
        realm.clear(ResponseServiceUser.class);
        realm.clear(ResponseServiceUserAction.class);
        realm.clear(ResponseVitKHistory.class);
        realm.commitTransaction();

        presenter.closeRealm();
    }
}
