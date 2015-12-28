package ie.teamchile.smartapp.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by user on 5/26/15.
 */
public class BaseResponseModel {
    private static BaseResponseModel instance;
    private Error errors;
    private Login login;
    private Appointment appointment;
    private List<Appointment> appointments = new ArrayList<>();
    private List<ServiceOption> serviceOptions = new ArrayList<>();
    private List<ServiceUser> serviceUsers = new ArrayList<>();
    private ServiceUser serviceUser;
    private List<ServiceProvider> serviceProviders = new ArrayList<>();
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

    private BaseResponseModel() {
    }

    public static synchronized BaseResponseModel getInstance() {
        if (instance == null) {
            instance = new BaseResponseModel();
        }
        return instance;
    }

    public void deleteInstance() {
        if (instance == null) {
            instance = null;
        } else
            instance = null;
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

    public void setAppointment(Appointment appointment) {
        this.appointment = appointment;
    }

    public List<Appointment> getAppointments() {
        return appointments;
    }

    public List<ServiceOption> getServiceOptions() {
        return serviceOptions;
    }

    public List<Clinic> getClinics() {
        return clinics;
    }

    public void setClinicMap(Map<Integer, Clinic> clinicMap) {
        this.clinicMap = clinicMap;
    }

    public List<ServiceProvider> getServiceProviders() {
        return serviceProviders;
    }

    public List<Pregnancy> getPregnancies() {
        return pregnancies;
    }

    public Pregnancy getPregnancy() {
        return pregnancy;
    }

    public List<Baby> getBabies() {
        return babies;
    }

    public Baby getBaby() {
        return baby;
    }

    public List<ServiceUser> getServiceUsers() {
        return serviceUsers;
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

    public List<AntiDHistory> getAntiDHistories() {
        return antiDHistories;
    }

    public List<PregnancyNote> getPregnancyNotes() {
        return pregnancyNotes;
    }

    public PregnancyNote getPregnancyNote() {
        return pregnancyNote;
    }

    public List<VitKHistory> getVitKHistories() {
        return vitKHistories;
    }

    public List<HearingHistory> getHearingHistories() {
        return hearingHistories;
    }

    public List<NbstHistory> getNbstHistories() {
        return nbstHistories;
    }

    public List<FeedingHistory> getFeedingHistories() {
        return feedingHistories;
    }

    public void setClinicStopped(List<Integer> clinicStopped) {
        this.clinicStopped = clinicStopped;
    }

    public void setClinicStarted(List<Integer> clinicStarted) {
        this.clinicStarted = clinicStarted;
    }

    public void setClinicNotStarted(List<Integer> clinicNotStarted) {
        this.clinicNotStarted = clinicNotStarted;
    }

    public void setClinicDayMap(Map<String, List<Integer>> clinicDayMap) {
        this.clinicDayMap = clinicDayMap;
    }

    public List<ServiceUserAction> getServiceUserActions() {
        return serviceUserActions;
    }
}
