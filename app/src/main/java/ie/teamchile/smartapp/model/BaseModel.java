package ie.teamchile.smartapp.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by user on 5/26/15.
 */
public class BaseModel {
    private static BaseModel instance;
    private Boolean isLoggedIn;
    @Expose
    private Error errors;
    private Login login;
    @Expose
    private Appointment appointment;
    @Expose
    private List<Appointment> appointments = new ArrayList<>();
    private Map<Integer, Appointment> clinicVisitIdApptMap = new HashMap<>();
    private Map<Integer, Map<String, List<Integer>>> clinicVisitClinicDateApptIdMap = new HashMap<>();
    private Map<Integer, Appointment> homeVisitIdApptMap = new HashMap<>();
    private Map<Integer, Map<String, List<Integer>>> homeVisitOptionDateApptIdMap = new HashMap<>();
    @SerializedName("service_options")
    @Expose
    private List<ServiceOption> serviceOptions = new ArrayList<>();
    private List<ServiceOption> serviceOptionsHomeList = new ArrayList<>();
    private Map<Integer, ServiceOption> serviceOptionsHomeMap = new HashMap<>();
    private Map<Integer, ServiceOption> serviceOptionsClinicMap = new HashMap<>();
    @SerializedName("service_users")
    @Expose
    private List<ServiceUser> serviceUsers = new ArrayList<>();
    @Expose
    private ServiceUser serviceUser;
    @SerializedName("service_providers")
    @Expose
    private List<ServiceProvider> serviceProviders = new ArrayList<>();
    @Expose
    private ServiceProvider serviceProvider = new ServiceProvider();
    @Expose
    private List<Pregnancy> pregnancies = new ArrayList<>();
    @Expose
    private Pregnancy pregnancy = new Pregnancy();
    @Expose
    private List<Baby> babies = new ArrayList<>();
    @Expose
    private Baby baby = new Baby();
    @Expose
    private List<Announcement> announcements = new ArrayList<>();
    @Expose
    private List<Clinic> clinics = new ArrayList<>();
    private Map<Integer, Clinic> clinicMap = new HashMap<>();
    private List<Integer> clinicStopped = new ArrayList<>();
    private List<Integer> clinicStarted = new ArrayList<>();
    private List<Integer> clinicNotStarted = new ArrayList<>();
    private Map<String, List<Integer>> clinicDayMap = new HashMap<>();
    @SerializedName("clinic_time_records")
    @Expose
    private List<ClinicTimeRecord> clinicTimeRecords = new ArrayList<>();
    @SerializedName("clinic_time_record")
    @Expose
    private ClinicTimeRecord clinicTimeRecord = new ClinicTimeRecord();
    @SerializedName("anti_d_histories")
    @Expose
    private List<AntiDHistory> antiDHistories = new ArrayList<>();
    @SerializedName("pregnancy_notes")
    @Expose
    private List<PregnancyNote> pregnancyNotes = new ArrayList<>();
    @SerializedName("pregnancy_note")
    @Expose
    private PregnancyNote pregnancyNote = new PregnancyNote();
    @SerializedName("vit_k_histories")
    @Expose
    private List<VitKHistory> vitKHistories = new ArrayList<>();
    @SerializedName("hearing_histories")
    @Expose
    private List<HearingHistory> hearingHistories = new ArrayList<>();
    @SerializedName("nbst_histories")
    @Expose
    private List<NbstHistory> nbstHistories = new ArrayList<>();
    @SerializedName("feeding_histories")
    @Expose
    private List<FeedingHistory> feedingHistories = new ArrayList<>();

    private BaseModel() {
    }

    public static synchronized BaseModel getInstance() {
        if (instance == null) {
            instance = new BaseModel();
        }
        return instance;
    }

    public Boolean getLoginStatus() {
        return isLoggedIn;
    }

    public void setLoginStatus(Boolean isLoggedIn) {
        this.isLoggedIn = isLoggedIn;
    }

    /**
     * @return The errors
     */
    public Error getError() {
        return errors;
    }

    /**
     * @param errors The errors
     */
    public void setError(Error errors) {
        this.errors = errors;
    }

    /**
     * @return The login
     */
    public Login getLogin() {
        return login;
    }

    /**
     * @param login The login
     */
    public void setLogin(Login login) {
        this.login = login;
    }

    public Appointment getAppointment() {
        return appointment;
    }

    /**
     * @return The appointments
     */
    public List<Appointment> getAppointments() {
        return appointments;
    }

    /**
     * @param appointments The appointments
     */
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

    /**
     * @param serviceOptions The service_options
     */
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

    /**
     * @return The clinics
     */
    public List<Clinic> getClinics() {
        return clinics;
    }

    /**
     * @param clinics The clinics
     */
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

    /**
     * @return The pregnancies
     */
    public List<Pregnancy> getPregnancies() {
        return pregnancies;
    }

    /**
     * @param pregnancies The pregnancies
     */
    public void setPregnancies(List<Pregnancy> pregnancies) {
        this.pregnancies = pregnancies;
    }

    public Pregnancy getPregnancy() {
        return pregnancy;
    }

    public void updatePregnancies(int position, Pregnancy pregnancy) {
        this.pregnancies.set(position, pregnancy);
    }

    /**
     * @return The babies
     */
    public List<Baby> getBabies() {
        return babies;
    }

    /**
     * @param babies The babies
     */
    public void setBabies(List<Baby> babies) {
        this.babies = babies;
    }

    public Baby getBaby() {
        return baby;
    }

    public void setBaby(Baby baby) {
        this.baby = baby;
    }

    /**
     * @return The announcements
     */
    public List<Announcement> getAnnouncements() {
        return announcements;
    }

    /**
     * @param announcements The announcements
     */

    public void setAnnouncements(List<Announcement> announcements) {
        this.announcements = announcements;
    }

    /**
     * @return The serviceUsers
     */
    public List<ServiceUser> getServiceUsers() {
        return serviceUsers;
    }

    /**
     * @param serviceUsers The service_users
     */
    public void setServiceUsers(List<ServiceUser> serviceUsers) {
        this.serviceUsers = serviceUsers;
    }

    public ServiceUser getServiceUser() {
        return serviceUser;
    }

    /**
     * @param serviceUser The service_user
     */
    public void setServiceUser(ServiceUser serviceUser) {
        this.serviceUser = serviceUser;
    }

    /**
     * @return The clinicTimeRecords
     */
    public List<ClinicTimeRecord> getClinicTimeRecords() {
        return clinicTimeRecords;
    }

    /**
     * @param clinicTimeRecords The clinic_time_records
     */
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

    /**
     * @return The antiDHistories
     */
    public List<AntiDHistory> getAntiDHistories() {
        return antiDHistories;
    }

    /**
     * @param antiDHistories The anti_d_histories
     */
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
}
