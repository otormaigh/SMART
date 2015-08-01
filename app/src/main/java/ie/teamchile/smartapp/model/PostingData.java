package ie.teamchile.smartapp.model;

/**
 * Created by user on 5/26/15.
 */

public class PostingData {
    public Login login;
    public Appointment appointment;
    public ClinicTimeRecord clinicTimeRecord;
    public PregnancyNote pregnancyNote;
    public Pregnancy pregnancy;
    public Note note;
    public Baby baby;
    public PregnancyAction pregnancyAction;

    public PostingData() {
    }

    public void postLogin(String username, String password) {
        this.login = new Login(username, password);
    }

    public void postAppointment(String date, int service_user_id,
                                String priority, String visit_type,
                                String return_type, int serviceOptionId) {
        int service_provider_id = BaseModel.getInstance().getLogin().getId();

        this.appointment = new Appointment(date, service_provider_id, service_user_id,
                priority, visit_type, return_type, serviceOptionId);
    }

    public void postAppointment(String date, String time, int service_user_id,
                                int clinic_id, String priority, String visit_type,
                                String return_type) {
        int service_provider_id = BaseModel.getInstance().getLogin().getId();

        this.appointment = new Appointment(date, time, service_provider_id, service_user_id, clinic_id,
                priority, visit_type, return_type);
    }

    public void putAppointmentStatus(Boolean attended, int clinic_id, int service_provider_id, int service_user_id) {
        this.appointment = new Appointment(attended, clinic_id, service_provider_id, service_user_id);
    }

    public void putStartTimeRecord(String startTime, int clinicId, String date) {
        this.clinicTimeRecord = new ClinicTimeRecord(startTime, clinicId, date);
    }

    public void putEndTimeRecord(String endTime, String date, int clinicId) {
        this.clinicTimeRecord = new ClinicTimeRecord(endTime, date, clinicId);
    }

    public void resetTimeRecord(String startTime, String endTime, String date, int clinicId) {
        this.clinicTimeRecord = new ClinicTimeRecord(startTime, endTime, date, clinicId);
    }

    public void postPregnancyNote(int id, String note, int pregnancyId, int serviceProviderId) {
        this.pregnancyNote = new PregnancyNote(id, note, pregnancyId, serviceProviderId);
    }

    public void putAntiD(String antiD, int serviceUserId) {
        this.pregnancy = new Pregnancy(antiD, serviceUserId);
    }

    public void putFeeding(String feeding, int serviceUserId) {
        this.pregnancy = new Pregnancy(serviceUserId, feeding);
    }

    public void putVitK(String vitK, int serviceUserId, int pregnancyId) {
        this.baby = new Baby(vitK, serviceUserId, pregnancyId);
    }

    public void putHearing(String hearing, int serviceUserId, int pregnancyId) {
        this.baby = new Baby(serviceUserId, hearing, pregnancyId);
    }

    public void putNBST(String nbst, int serviceUserId, int pregnancyId) {
        this.baby = new Baby(serviceUserId, pregnancyId, nbst);
    }

    public void postNote(String note) {
        this.note = new Note(note);
    }

    public void postPregnancyAction(String action) {
        this.pregnancyAction = new PregnancyAction(action);
    }

    public void putPregnancyActionStatus(boolean complete) {
        this.pregnancyAction = new PregnancyAction(complete);
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
        private Integer clinicId;
        private int serviceProviderId;
        private int serviceUserId;
        private String priority;
        private String visitType;
        private String returnType;
        private Boolean attended;
        private Integer serviceOptionId;

        public Appointment(Boolean attended, int clinicId, int serviceProviderId, int serviceUserId) {
            this.attended = attended;
            this.clinicId = clinicId;
            this.serviceProviderId = serviceProviderId;
            this.serviceUserId = serviceUserId;
        }

        public Appointment(String date, int serviceProviderId, int serviceUserId,
                           String priority, String visitType, String returnType,
                           int serviceOptionId) {
            this.date = date;
            this.serviceProviderId = serviceProviderId;
            this.serviceUserId = serviceUserId;
            this.priority = priority;
            this.visitType = visitType;
            this.returnType = returnType;
            this.serviceOptionId = serviceOptionId;
        }

        public Appointment(String date, String time, int serviceProviderId, int serviceUserId,
                           int clinicId, String priority, String visitType, String returnType) {
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
        private String startTime;
        private String endTime;
        private int clinicId;
        private String date;

        public ClinicTimeRecord(String startTime, int clinicId, String date) {
            this.startTime = startTime;
            this.clinicId = clinicId;
            this.date = date;
        }

        public ClinicTimeRecord(String endTime, String date, int clinicId) {
            this.endTime = endTime;
            this.date = date;
            this.clinicId = clinicId;
        }

        public ClinicTimeRecord(String startTime, String endTime, String date, int clinicId) {
            this.startTime = startTime;
            this.endTime = endTime;
            this.date = date;
            this.clinicId = clinicId;
        }
    }

    private class PregnancyNote {
        private int id;
        private String note;
        private int pregnancyId;
        private int serviceProviderId;

        public PregnancyNote(int id, String note, int pregnancyId, int serviceProviderId) {
            this.id = id;
            this.note = note;
            this.pregnancyId = pregnancyId;
            this.serviceProviderId = serviceProviderId;
        }
    }

    private class Pregnancy {
        private String antiD;
        private int serviceUserId;
        private String feeding;

        public Pregnancy(String antiD, int serviceUserId) {
            this.antiD = antiD;
            this.serviceUserId = serviceUserId;
        }

        public Pregnancy(int serviceUserId, String feeding) {
            this.serviceUserId = serviceUserId;
            this.feeding = feeding;
        }
    }

    private class Note {
        private String note;

        public Note(String note) {
            this.note = note;
        }
    }

    private class Baby {
        private String vitK;
        private int serviceUserId;
        private int pregnancyId;
        private String hearing;
        private String nbst;


        public Baby(String vitK, int serviceUserId, int pregnancyId) {
            this.vitK = vitK;
            this.serviceUserId = serviceUserId;
            this.pregnancyId = pregnancyId;
        }

        public Baby(int serviceUserId, String hearing, int pregnancyId) {
            this.serviceUserId = serviceUserId;
            this.hearing = hearing;
            this.pregnancyId = pregnancyId;
        }

        public Baby(int serviceUserId, int pregnancyId, String nbst) {
            this.serviceUserId = serviceUserId;
            this.pregnancyId = pregnancyId;
            this.nbst = nbst;
        }
    }

    private class PregnancyAction {
        private String action;
        private boolean complete;

        public PregnancyAction(String action) {
            this.action = action;
        }

        public PregnancyAction(boolean complete) {
            this.complete = complete;
        }
    }
}
