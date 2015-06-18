package ie.teamchile.smartapp.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by user on 5/26/15.
 */

public class PostingData {
    @Expose
    public Login login;
    @Expose
    public Appointment appointment;
    @SerializedName("clinic_time_record")
    public ClinicTimeRecord clinicTimeRecord;
    @SerializedName("notes")
    public PregnancyNote pregnancyNote;
    @SerializedName("pregnancy")
    public Pregnancy pregnancy;
    @Expose
    public Note note;

    public PostingData() {
    }

    public void postLogin(String username, String password) {
        this.login = new Login(username, password);
    }

    public void postAppointment(String date, String time, int service_user_id,
                                int clinic_id, String priority, String visit_type, String return_type) {
        int service_provider_id = ApiRootModel.getInstance().getLogin().getId();

        this.appointment = new Appointment(date, time, service_provider_id, service_user_id, clinic_id,
                priority, visit_type, return_type);
    }

    public void putAppointmentStatus(Boolean attended, int clinic_id, int service_provider_id, int service_user_id){
        this.appointment = new Appointment(attended, clinic_id, service_provider_id, service_user_id);
    }

    public void postTimeRecords(String startTime, int clinicId, String date){
        this.clinicTimeRecord = new ClinicTimeRecord(startTime, clinicId, date);
    }

    public void updateTimeRecords(String endTime, String date, int clinicId){
        this.clinicTimeRecord = new ClinicTimeRecord(endTime, date, clinicId);
    }

    public void postPregnancyNote(int id, String note, int pregnancyId, int serviceProviderId){
        this.pregnancyNote = new PregnancyNote(id, note, pregnancyId, serviceProviderId);
    }

    public void putAntiD(String antiD, int serviceUserId){
        this.pregnancy = new Pregnancy(antiD, serviceUserId);
    }

    public void postNote(String note) {
        this.note = new Note(note);
    }
    private class Login {
        private String username;
        private String password;

        public Login(String username, String password) {
            this.username = username;
            this.password = password;
        }
    }

    private class Appointment {
        private String date;
        private String time;
        @SerializedName("clinic_id")
        private int clinicId;
        @SerializedName("service_provider_id")
        private int serviceProviderId;
        @SerializedName("service_user_id")
        private int serviceUserId;
        private String priority;
        @SerializedName("visit_type")
        private String visitType;
        @SerializedName("return_type")
        private String returnType;
        private Boolean attended;

        public Appointment(Boolean attended, int clinicId, int serviceProviderId, int serviceUserId){
            this.attended = attended;
            this.clinicId = clinicId;
            this.serviceProviderId = serviceProviderId;
            this.serviceUserId = serviceUserId;
        }

        public Appointment(String date, String time, int serviceProviderId, int serviceUserId,
                           int clinicId, String priority, String visitType, String returnType){
            this.date = date;
            this.time = time;
            this.serviceProviderId = serviceProviderId;
            this.serviceUserId = serviceUserId;
            this.clinicId = clinicId;
            this.priority = priority;
            this.visitType = visitType;
            this.returnType = returnType;
        }
    }

    private class ClinicTimeRecord {
        @SerializedName("start_time")
        private String startTime;
        @SerializedName("end_time")
        private String endTime;
        @SerializedName("clinic_id")
        private int clinicId;
        @Expose
        private String date;

        public ClinicTimeRecord(String startTime, int clinicId, String date) {
            this.startTime = startTime;
            this.clinicId = clinicId;
            this.date = date;
        }

        public ClinicTimeRecord(String endTime, String date, int clinicId){
            this.endTime = endTime;
            this.date = date;
            this.clinicId = clinicId;
        }
    }

    private class PregnancyNote {
        @Expose
        private int id;
        @Expose
        private String note;
        @SerializedName("pregnancy_id")
        private int pregnancyId;
        @SerializedName("service_provider_id")
        private int serviceProviderId;

        public PregnancyNote(int id, String note, int pregnancyId, int serviceProviderId){
            this.id  = id;
            this.note = note;
            this.pregnancyId = pregnancyId;
            this.serviceProviderId = serviceProviderId;
        }
    }

    private class Pregnancy {
        @SerializedName("anti_d")
        private String antiD;
        @SerializedName("service_user_id")
        private int serviceUserId;

        public Pregnancy(String antiD, int serviceUserId){
            this.antiD = antiD;
            this.serviceUserId = serviceUserId;
        }
    }

    private class Note {
        @Expose
        private String note;

        public Note(String note) {
            this.note = note;
        }
    }
}
