package ie.teamchile.smartapp.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by user on 5/26/15.
 */
public class ResponseBase {
    private static ResponseBase instance;
    private ResponseError errors;
    private ResponseLogin login;
    private ResponseAppointment appointment;
    private List<ResponseAppointment> appointments = new ArrayList<>();
    private List<ResponseServiceOption> serviceOptions = new ArrayList<>();
    private List<ResponseServiceUser> serviceUsers = new ArrayList<>();
    private ResponseServiceUser serviceUser;
    private List<ResponseServiceProvider> serviceProviders = new ArrayList<>();
    private List<ResponsePregnancy> pregnancies = new ArrayList<>();
    private ResponsePregnancy pregnancy = new ResponsePregnancy();
    private List<ResponseBaby> babies = new ArrayList<>();
    private ResponseBaby baby = new ResponseBaby();
    private List<ResponseAnnouncement> announcements = new ArrayList<>();
    private List<ResponseClinic> clinics = new ArrayList<>();
    private Map<Integer, ResponseClinic> clinicMap = new HashMap<>();
    private List<Integer> clinicStopped = new ArrayList<>();
    private List<Integer> clinicStarted = new ArrayList<>();
    private List<Integer> clinicNotStarted = new ArrayList<>();
    private Map<String, List<Integer>> clinicDayMap = new HashMap<>();
    private List<ResponseClinicTimeRecord> clinicTimeRecords = new ArrayList<>();
    private ResponseClinicTimeRecord clinicTimeRecord = new ResponseClinicTimeRecord();
    private List<ResponseAntiDHistory> antiDHistories = new ArrayList<>();
    private List<ResponsePregnancyNote> pregnancyNotes = new ArrayList<>();
    private ResponsePregnancyNote pregnancyNote = new ResponsePregnancyNote();
    private List<ResponseVitKHistory> vitKHistories = new ArrayList<>();
    private List<ResponseHearingHistory> hearingHistories = new ArrayList<>();
    private List<ResponseNbstHistory> nbstHistories = new ArrayList<>();
    private List<ResponseFeedingHistory> feedingHistories = new ArrayList<>();
    private List<ResponseServiceUserAction> serviceUserActions = new ArrayList<>();
    private List<ResponsePregnancyAction> pregnancyActions = new ArrayList<>();
    private Map<Integer, ResponseServiceUserAction> serviceUserActionMap = new HashMap<>();

    private ResponseBase() {
    }

    public static synchronized ResponseBase getInstance() {
        if (instance == null) {
            instance = new ResponseBase();
        }
        return instance;
    }

    public void deleteInstance() {
        instance = null;
    }

    public ResponseError getError() {
        return errors;
    }

    public void setError(ResponseError errors) {
        this.errors = errors;
    }

    public ResponseLogin getResponseLogin() {
        return login;
    }

    public ResponseAppointment getAppointment() {
        return appointment;
    }

    public void setAppointment(ResponseAppointment appointment) {
        this.appointment = appointment;
    }

    public List<ResponseAppointment> getAppointments() {
        return appointments;
    }

    public List<ResponseServiceOption> getResponseServiceOptions() {
        return serviceOptions;
    }

    public List<ResponseClinic> getResponseClinics() {
        return clinics;
    }

    public void setClinicMap(Map<Integer, ResponseClinic> clinicMap) {
        this.clinicMap = clinicMap;
    }

    public List<ResponseServiceProvider> getResponseServiceProviders() {
        return serviceProviders;
    }

    public List<ResponsePregnancy> getPregnancies() {
        return pregnancies;
    }

    public ResponsePregnancy getResponsePregnancy() {
        return pregnancy;
    }

    public List<ResponseBaby> getBabies() {
        return babies;
    }

    public ResponseBaby getResponseBaby() {
        return baby;
    }

    public List<ResponseServiceUser> getResponseServiceUsers() {
        return serviceUsers;
    }

    public List<ResponseClinicTimeRecord> getResponseClinicTimeRecords() {
        return clinicTimeRecords;
    }

    public ResponseClinicTimeRecord getResponseClinicTimeRecord() {
        return clinicTimeRecord;
    }

    public List<ResponseAntiDHistory> getAntiDHistories() {
        return antiDHistories;
    }

    public List<ResponsePregnancyNote> getResponsePregnancyNotes() {
        return pregnancyNotes;
    }

    public ResponsePregnancyNote getResponsePregnancyNote() {
        return pregnancyNote;
    }

    public List<ResponseVitKHistory> getVitKHistories() {
        return vitKHistories;
    }

    public List<ResponseHearingHistory> getHearingHistories() {
        return hearingHistories;
    }

    public List<ResponseNbstHistory> getNbstHistories() {
        return nbstHistories;
    }

    public List<ResponseFeedingHistory> getFeedingHistories() {
        return feedingHistories;
    }

    public List<ResponseServiceUserAction> getResponseServiceUserActions() {
        return serviceUserActions;
    }
}
