package ie.teamchile.smartapp.activities.Base;

import java.util.List;

import ie.teamchile.smartapp.model.Appointment;
import ie.teamchile.smartapp.model.Baby;
import ie.teamchile.smartapp.model.BaseResponseModel;
import ie.teamchile.smartapp.model.Clinic;
import ie.teamchile.smartapp.model.ClinicTimeRecord;
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

/**
 * Created by elliot on 28/12/2015.
 */
public interface BaseModel {
    void deleteSingletonInstance();

    Login getLogin();

    void getLoginSharedPrefs();

    long getTimeFromPrefs();

    void saveAppointmentsToRealm(List<Appointment> appointments);

    void saveServiceUserToRealm(BaseResponseModel baseResponseModel);

    void saveServiceProviderToRealm(ServiceProvider serviceProvider);

    void saveServiceOptionsToRealm(List<ServiceOption> serviceOptions);

    List<Clinic> getClinics();

    void updateClinics(List<Clinic> clinics);

    List<ClinicTimeRecord> getClinicTimeRecords();

    void updateClinicTimeRecords(List<ClinicTimeRecord> clinicTimeRecords);

    void updateClinicTimeRecord(ClinicTimeRecord clinicTimeRecord);

    void saveServiceUserActionsToRealm(List<ServiceUserAction> serviceUserActions);

    List<PregnancyNote> getPregnancyNotes();

    void updatePregnancyNote(PregnancyNote pregnancyNote);

    void updatePregnancyNotes(List<PregnancyNote> pregnancyNotes);

    void deleteServiceUserFromRealm();

    void saveVitKToRealm(List<VitKHistory> vitKHistories);

    void saveHearingToRealm(List<HearingHistory> hearingHistories);

    void saveNbstToRealm(List<NbstHistory> nbstHistories);

    void saveFeedingHistoriesToRealm(List<FeedingHistory> feedingHistories);

    void saveAppointmentToRealm(Appointment appointment);

    void saveLoginToRealm(Login login);

    void deleteRealmLogin();

    ServiceProvider getServiceProvider();

    ServiceUser getServiceUser();

    List<ServiceUser> getServiceUsers();

    Baby getBaby();

    void updateBaby(Baby baby);

    Pregnancy getPregnancy();

    void updatePregnancy(Pregnancy pregnancy);

    void updateAntiD();

    void updateFeeding();

    void updateVitK();

    void updateHearing();

    void updateNbst();

    Appointment getAppointmentById(int appointmentId);

    List<Appointment> getAppointments();

    void updateAppointment(Appointment appointment);

    void updateAppointments(List<Appointment> appointments);

    List<ServiceOption> getServiceOptions();

    void clearData();
}
