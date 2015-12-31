package ie.teamchile.smartapp.activities.Base;

import java.util.List;

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

/**
 * Created by elliot on 28/12/2015.
 */
public interface BaseModel {
    void deleteSingletonInstance();

    void getLoginSharedPrefs();

    long getTimeFromPrefs();

    void saveAppointmentsToRealm(List<Appointment> appointments);

    void saveServiceUserToRealm(BaseResponseModel baseResponseModel);

    void saveServiceProviderToRealm(ServiceProvider serviceProvider);

    void saveServiceOptionsToRealm(List<ServiceOption> serviceOptions);

    void saveClinicsToRealm(List<Clinic> clinics);

    void saveServiceUserActionsToRealm(List<ServiceUserAction> serviceUserActions);

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

    Baby getBaby();

    void updateBaby(Baby baby);

    Pregnancy getPregnancy();

    void updatePregnancy(Pregnancy pregnancy);

    void updateAntiD();

    void updateFeeding();

    void updateVitK();

    void updateHearing();

    void updateNbst();

    void clearData();
}
