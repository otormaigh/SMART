package ie.teamchile.smartapp.activities.Base;

import java.util.List;

import ie.teamchile.smartapp.model.ResponseAppointment;
import ie.teamchile.smartapp.model.ResponseBaby;
import ie.teamchile.smartapp.model.ResponseBase;
import ie.teamchile.smartapp.model.ResponseClinic;
import ie.teamchile.smartapp.model.ResponseClinicTimeRecord;
import ie.teamchile.smartapp.model.ResponseFeedingHistory;
import ie.teamchile.smartapp.model.ResponseHearingHistory;
import ie.teamchile.smartapp.model.ResponseLogin;
import ie.teamchile.smartapp.model.ResponseNbstHistory;
import ie.teamchile.smartapp.model.ResponsePregnancy;
import ie.teamchile.smartapp.model.ResponsePregnancyNote;
import ie.teamchile.smartapp.model.ResponseServiceOption;
import ie.teamchile.smartapp.model.ResponseServiceProvider;
import ie.teamchile.smartapp.model.ResponseServiceUser;
import ie.teamchile.smartapp.model.ResponseServiceUserAction;
import ie.teamchile.smartapp.model.ResponseVitKHistory;

/**
 * Created by elliot on 28/12/2015.
 */
public interface BaseModel {
    void deleteSingletonInstance();

    ResponseLogin getLogin();

    void getLoginSharedPrefs();

    long getTimeFromPrefs();

    void saveAppointmentsToRealm(List<ResponseAppointment> responseAppointments);

    void saveServiceUserToRealm(ResponseBase responseBase);

    void saveServiceProviderToRealm(ResponseServiceProvider responseServiceProvider);

    void saveServiceOptionsToRealm(List<ResponseServiceOption> responseServiceOptions);

    List<ResponseClinic> getClinics();

    void updateClinics(List<ResponseClinic> responseClinics);

    List<ResponseClinicTimeRecord> getClinicTimeRecords();

    void updateClinicTimeRecords(List<ResponseClinicTimeRecord> responseClinicTimeRecords);

    void updateClinicTimeRecord(ResponseClinicTimeRecord responseClinicTimeRecord);

    void saveServiceUserActionsToRealm(List<ResponseServiceUserAction> responseServiceUserActions);

    List<ResponsePregnancyNote> getPregnancyNotes();

    void updatePregnancyNote(ResponsePregnancyNote responsePregnancyNote);

    void updatePregnancyNotes(List<ResponsePregnancyNote> responsePregnancyNotes);

    void deleteServiceUserFromRealm();

    void saveVitKToRealm(List<ResponseVitKHistory> vitKHistories);

    void saveHearingToRealm(List<ResponseHearingHistory> hearingHistories);

    void saveNbstToRealm(List<ResponseNbstHistory> nbstHistories);

    void saveFeedingHistoriesToRealm(List<ResponseFeedingHistory> feedingHistories);

    void saveAppointmentToRealm(ResponseAppointment responseAppointment);

    void saveLoginToRealm(ResponseLogin responseLogin);

    void deleteRealmLogin();

    ResponseServiceProvider getServiceProvider();

    ResponseServiceUser getServiceUser();

    List<ResponseServiceUser> getServiceUsers();

    ResponseBaby getBaby();

    void updateBaby(ResponseBaby responseBaby);

    ResponsePregnancy getPregnancy();

    void updatePregnancy(ResponsePregnancy responsePregnancy);

    void updateAntiD();

    void updateFeeding();

    void updateVitK();

    void updateHearing();

    void updateNbst();

    ResponseAppointment getAppointmentById(int appointmentId);

    List<ResponseAppointment> getAppointments();

    void updateAppointment(ResponseAppointment responseAppointment);

    void updateAppointments(List<ResponseAppointment> responseAppointments);

    List<ResponseServiceOption> getServiceOptions();

    void clearData();
}
