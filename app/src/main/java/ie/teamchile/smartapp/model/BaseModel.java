package ie.teamchile.smartapp.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by user on 5/26/15.
 */
public class BaseModel {
    private static BaseModel instance;
    private Boolean isLoggedIn = false;
    private Error errors;
    private Login login;
    private Appointment appointment;
    private List<Appointment> appointments = new ArrayList<>();
    private Map<Integer, Appointment> clinicVisitIdApptMap = new HashMap<>();
    private Map<Integer, Map<String, List<Integer>>> clinicVisitClinicDateApptIdMap = new HashMap<>();
    private Map<Integer, Appointment> homeVisitIdApptMap = new HashMap<>();
    private Map<Integer, Map<String, List<Integer>>> homeVisitOptionDateApptIdMap = new HashMap<>();
    private List<ServiceOption> serviceOptions = new ArrayList<>();
    private List<ServiceOption> serviceOptionsHomeList = new ArrayList<>();
    private Map<Integer, ServiceOption> serviceOptionsHomeMap = new HashMap<>();
    private Map<Integer, ServiceOption> serviceOptionsClinicMap = new HashMap<>();
    private List<ServiceUser> serviceUsers = new ArrayList<>();
    private ServiceUser serviceUser;
    private List<ServiceProvider> serviceProviders = new ArrayList<>();
    private ServiceProvider serviceProvider = new ServiceProvider();
    private List<Pregnancy> pregnancies = new ArrayList<>();
    private Pregnancy pregnancy = new Pregnancy();
    private List<Baby> babies = new ArrayList<>();
    private Baby baby = new Baby();
    private List<Announcement> announcements = new ArrayList<>();
    private List<Clinic> clinics = new ArrayList<>();
    private Map<Integer, Clinic> clinicMap = new HashMap<>();
    private List<Integer> clinicStopped = new ArrayList<>();
    private List<Integer> clinicStarted = new ArrayList<>();
    private List<Integer> clinicNotStarted = new ArrayList<>();
    private Map<String, List<Integer>> clinicDayMap = new HashMap<>();
    private List<ClinicTimeRecord> clinicTimeRecords = new ArrayList<>();
    private ClinicTimeRecord clinicTimeRecord = new ClinicTimeRecord();
    private List<AntiDHistory> antiDHistories = new ArrayList<>();
    private List<PregnancyNote> pregnancyNotes = new ArrayList<>();
    private PregnancyNote pregnancyNote = new PregnancyNote();
    private List<VitKHistory> vitKHistories = new ArrayList<>();
    private List<HearingHistory> hearingHistories = new ArrayList<>();
    private List<NbstHistory> nbstHistories = new ArrayList<>();
    private List<FeedingHistory> feedingHistories = new ArrayList<>();
    private List<ServiceUserAction> serviceUserActions = new ArrayList<>();
    private List<PregnancyAction> pregnancyActions = new ArrayList<>();
    private Map<Integer, ServiceUserAction> serviceUserActionMap = new HashMap<>();

    private BaseModel() {
    }

    public static synchronized BaseModel getInstance() {
        if (instance == null) {
            instance = new BaseModel();
        }
        return instance;
    }

    public void deleteInstance() {
        if (instance == null) {
            instance = null;
        } else
            instance = null;
    }

    public Boolean getLoginStatus() {
        return isLoggedIn;
    }

    public void setLoginStatus(Boolean isLoggedIn) {
        this.isLoggedIn = isLoggedIn;
    }

    public Error getError() {
        return errors;
    }

    public void setError(Error errors) {
        this.errors = errors;
    }

    public Login getLogin() {
        return login;
    }

    public void setLogin(Login login) {
        this.login = login;
    }

    public Appointment getAppointment() {
        return appointment;
    }

    public List<Appointment> getAppointments() {
        return appointments;
    }

    public void setAppointments(List<Appointment> appointments) {
        this.appointments = appointments;
    }

    public void addAppointment(Appointment appointment) {
        this.appointments.add(appointment);
    }

    public Map<Integer, Map<String, List<Integer>>> getClinicVisitClinicDateApptIdMap() {
        return clinicVisitClinicDateApptIdMap;
    }

    public void setClinicVisitClinicDateApptIdMap(Map<Integer, Map<String, List<Integer>>> clinicVisitClinicDateApptIdMap) {
        this.clinicVisitClinicDateApptIdMap = clinicVisitClinicDateApptIdMap;
    }

    public Map<Integer, Appointment> getClinicVisitIdApptMap() {
        return clinicVisitIdApptMap;
    }

    public void setClinicVisitIdApptMap(Map<Integer, Appointment> clinicVisitIdApptMap) {
        this.clinicVisitIdApptMap = clinicVisitIdApptMap;
    }

    public Map<Integer, Appointment> getHomeVisitIdApptMap() {
        return homeVisitIdApptMap;
    }

    public void setHomeVisitIdApptMap(Map<Integer, Appointment> homeVisitIdApptMap) {
        this.homeVisitIdApptMap = homeVisitIdApptMap;
    }

    public Map<Integer, Map<String, List<Integer>>> getHomeVisitOptionDateApptIdMap() {
        return homeVisitOptionDateApptIdMap;
    }

    public void setHomeVisitOptionDateApptIdMap(Map<Integer, Map<String, List<Integer>>> homeVisitOptionDateApptIdMap) {
        this.homeVisitOptionDateApptIdMap = homeVisitOptionDateApptIdMap;
    }

    public List<ServiceOption> getServiceOptions() {
        return serviceOptions;
    }

    public void setServiceOptions(List<ServiceOption> serviceOptions) {
        this.serviceOptions = serviceOptions;
    }

    public List<ServiceOption> getServiceOptionsHomeList() {
        return serviceOptionsHomeList;
    }

    public void setServiceOptionsHomeList(List<ServiceOption> serviceOptionsHomeList) {
        this.serviceOptionsHomeList = serviceOptionsHomeList;
    }

    public Map<Integer, ServiceOption> getServiceOptionsHomeMap() {
        return serviceOptionsHomeMap;
    }

    public void setServiceOptionsHomeMap(Map<Integer, ServiceOption> serviceOptionsHomeMap) {
        this.serviceOptionsHomeMap = serviceOptionsHomeMap;
    }

    public Map<Integer, ServiceOption> getServiceOptionsClinicMap() {
        return serviceOptionsClinicMap;
    }

    public void setServiceOptionsClinicMap(Map<Integer, ServiceOption> serviceOptionsClinicMap) {
        this.serviceOptionsClinicMap = serviceOptionsClinicMap;
    }

    public List<Clinic> getClinics() {
        return clinics;
    }

    public void setClinics(List<Clinic> clinics) {
        this.clinics = clinics;
    }

    public Map<Integer, Clinic> getClinicMap() {
        return clinicMap;
    }

    public void setClinicMap(Map<Integer, Clinic> clinicMap) {
        this.clinicMap = clinicMap;
    }

    public List<ServiceProvider> getServiceProviders() {
        return serviceProviders;
    }

    public void setServiceProviders(List<ServiceProvider> serviceProviders) {
        this.serviceProviders = serviceProviders;
    }

    public ServiceProvider getServiceProvider() {
        return serviceProvider;
    }

    public void setServiceProvider(ServiceProvider serviceProvider) {
        this.serviceProvider = serviceProvider;
    }

    public List<Pregnancy> getPregnancies() {
        return pregnancies;
    }

    public void setPregnancies(List<Pregnancy> pregnancies) {
        this.pregnancies = pregnancies;
    }

    public Pregnancy getPregnancy() {
        return pregnancy;
    }

    public void updatePregnancies(int position, Pregnancy pregnancy) {
        this.pregnancies.set(position, pregnancy);
    }

    public List<Baby> getBabies() {
        return babies;
    }

    public void setBabies(List<Baby> babies) {
        this.babies = babies;
    }

    public Baby getBaby() {
        return baby;
    }

    public void setBaby(Baby baby) {
        this.baby = baby;
    }

    public List<Announcement> getAnnouncements() {
        return announcements;
    }

    public void setAnnouncements(List<Announcement> announcements) {
        this.announcements = announcements;
    }

    public List<ServiceUser> getServiceUsers() {
        return serviceUsers;
    }

    public void setServiceUsers(List<ServiceUser> serviceUsers) {
        this.serviceUsers = serviceUsers;
    }

    public ServiceUser getServiceUser() {
        return serviceUser;
    }

    public void setServiceUser(ServiceUser serviceUser) {
        this.serviceUser = serviceUser;
    }

    public List<ClinicTimeRecord> getClinicTimeRecords() {
        return clinicTimeRecords;
    }

    public void setClinicTimeRecords(List<ClinicTimeRecord> clinicTimeRecords) {
        this.clinicTimeRecords = clinicTimeRecords;
    }

    public ClinicTimeRecord getClinicTimeRecord() {
        return clinicTimeRecord;
    }

    public void setClinicTimeRecord(ClinicTimeRecord clinicTimeRecord) {
        this.clinicTimeRecord = clinicTimeRecord;
    }

    public void addClinicTimeRecord(ClinicTimeRecord clinicTimeRecord) {
        clinicTimeRecords.add(clinicTimeRecord);
    }

    public List<AntiDHistory> getAntiDHistories() {
        return antiDHistories;
    }

    public void setAntiDHistories(List<AntiDHistory> antiDHistories) {
        this.antiDHistories = antiDHistories;
    }

    public void addAntiDHistory(AntiDHistory antiDHistory) {
        this.antiDHistories.add(antiDHistory);
    }

    public List<PregnancyNote> getPregnancyNotes() {
        return pregnancyNotes;
    }

    public void setPregnancyNotes(List<PregnancyNote> pregnancyNotes) {
        this.pregnancyNotes = pregnancyNotes;
    }

    public void addPregnancyNote(PregnancyNote pregnancyNote) {
        this.pregnancyNotes.add(pregnancyNote);
    }

    public PregnancyNote getPregnancyNote() {
        return pregnancyNote;
    }

    public void setPregnancyNote(PregnancyNote pregnancyNote) {
        this.pregnancyNote = pregnancyNote;
    }

    public List<VitKHistory> getVitKHistories() {
        return vitKHistories;
    }

    public void setVitKHistories(List<VitKHistory> vitKHistories) {
        this.vitKHistories = vitKHistories;
    }

    public List<HearingHistory> getHearingHistories() {
        return hearingHistories;
    }

    public void setHearingHistories(List<HearingHistory> hearingHistories) {
        this.hearingHistories = hearingHistories;
    }

    public List<NbstHistory> getNbstHistories() {
        return nbstHistories;
    }

    public void setNbstHistories(List<NbstHistory> nbstHistories) {
        this.nbstHistories = nbstHistories;
    }

    public List<FeedingHistory> getFeedingHistories() {
        return feedingHistories;
    }

    public void setFeedingHistories(List<FeedingHistory> feedingHistories) {
        this.feedingHistories = feedingHistories;
    }

    public List<Integer> getClinicStopped() {
        return clinicStopped;
    }

    public void setClinicStopped(List<Integer> clinicStopped) {
        this.clinicStopped = clinicStopped;
    }

    public void addClinicStopped(int clinicStopped) {
        this.clinicStopped.add(clinicStopped);
    }

    public List<Integer> getClinicStarted() {
        return clinicStarted;
    }

    public void setClinicStarted(List<Integer> clinicStarted) {
        this.clinicStarted = clinicStarted;
    }

    public void addClinicStarted(int clinicStarted) {
        this.clinicStarted.add(clinicStarted);
    }

    public List<Integer> getClinicNotStarted() {
        return clinicNotStarted;
    }

    public void setClinicNotStarted(List<Integer> clinicNotStarted) {
        this.clinicNotStarted = clinicNotStarted;
    }

    public void addClinicNotStarted(int clinicNotStarted) {
        this.clinicNotStarted.add(clinicNotStarted);
    }

    public Map<String, List<Integer>> getClinicDayMap() {
        return clinicDayMap;
    }

    public void setClinicDayMap(Map<String, List<Integer>> clinicDayMap) {
        this.clinicDayMap = clinicDayMap;
    }

    public List<ServiceUserAction> getServiceUserActions() {
        return serviceUserActions;
    }

    public void setServiceUserActions(List<ServiceUserAction> serviceUserActions) {
        this.serviceUserActions = serviceUserActions;
    }

    public List<PregnancyAction> getPregnancyActions() {
        return pregnancyActions;
    }

    public void setPregnancyActions(List<PregnancyAction> pregnancyActions) {
        this.pregnancyActions = pregnancyActions;
    }

    public Map<Integer, ServiceUserAction> getServiceUserActionMap() {
        return serviceUserActionMap;
    }

    public void setServiceUserActionMap(Map<Integer, ServiceUserAction> serviceUserActionMap) {
        this.serviceUserActionMap = serviceUserActionMap;
    }
}
