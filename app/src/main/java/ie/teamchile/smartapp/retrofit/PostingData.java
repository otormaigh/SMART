
package ie.teamchile.smartapp.retrofit;

/**
 * Created by user on 5/26/15.
 */

public class PostingData {
    public Login login;
    public Appointment appointment;

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
        private int service_provider_id;
        private int service_user_id;
        private int clinic_id;
        private String priority;
        private String visit_type;
        private String return_type;

        public Appointment(String date, String time, int service_provider_id, int service_user_id,
                           int clinic_id, String priority, String visit_type, String return_type){
            this.date = date;
            this.time = time;
            this.service_provider_id = service_provider_id;
            this.service_user_id = service_user_id;
            this.clinic_id = clinic_id;
            this.priority = priority;
            this.visit_type = visit_type;
            this.return_type = return_type;
        }
    }
}
