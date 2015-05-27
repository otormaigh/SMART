package ie.teamchile.smartapp.retrofit;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by user on 5/26/15.
 */
public class ApiRootModel {
    private static ApiRootModel instance;
    @Expose
    private Error error;
    private LoginJson login;
    @Expose
    private List<Appointment> appointments = new ArrayList<>();
    @SerializedName("service_options")
    @Expose
    private List<ServiceOption> serviceOptions = new ArrayList<>();
    @SerializedName("service_users")
    @Expose
    private List<ServiceUser> serviceUsers = new ArrayList<>();
    @SerializedName("service_providers")
    @Expose
    private List<ServiceProvider> serviceProviders = new ArrayList<>();
    @Expose
    private List<Pregnancy> pregnancies = new ArrayList<>();
    @Expose
    private List<Baby> babies = new ArrayList<>();
    @Expose
    private List<Announcement> announcements = new ArrayList<>();
    @Expose
    private List<Clinic> clinics = new ArrayList<>();

    private ApiRootModel() {
    }

    public static synchronized ApiRootModel getInstance() {
        if (instance == null) {
            instance = new ApiRootModel();
        }
        return instance;
    }

    /**
     * @return The error
     */
    public Error getError() {
        return error;
    }

    /**
     * @param error The error
     */
    public void setError(Error error) {
        this.error = error;
    }

    /**
     * @return The login
     */
    public LoginJson getLogin() {
        return login;
    }

    /**
     * @param login The login
     */
    public void setLogin(LoginJson login) {
        this.login = login;
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

    public List<ServiceOption> getServiceOptions() {
        return serviceOptions;
    }

    /**
     * @param serviceOptions The service_options
     */
    public void setServiceOptions(List<ServiceOption> serviceOptions) {
        this.serviceOptions = serviceOptions;
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

    /**
     * @return The service_providers
     */
    public List<ServiceProvider> getServiceProviders() {
        return serviceProviders;
    }

    /**
     * @param serviceProviders The service_providers
     */
    public void setServiceProviders(List<ServiceProvider> serviceProviders) {
        this.serviceProviders = serviceProviders;
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
}
