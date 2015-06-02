package ie.teamchile.smartapp.utility;

import java.util.List;

import ie.teamchile.smartapp.model.ApiRootModel;
import ie.teamchile.smartapp.model.Appointment;
import ie.teamchile.smartapp.model.PostingData;
import retrofit.Callback;
import retrofit.http.Body;
import retrofit.http.DELETE;
import retrofit.http.GET;
import retrofit.http.Header;
import retrofit.http.POST;
import retrofit.http.PUT;
import retrofit.http.Path;
import retrofit.http.Query;

/**
 * Created by user on 5/26/15.
 */
public interface SmartApi {

    @POST("/login")
    void postLogin(
            @Body PostingData login,
            Callback<ApiRootModel> callback);

    @POST("/logout")
    void postLogout(
            @Header("Auth-Token") String authToken,
            @Header("Api-Key") String apiKey,
            Callback<ApiRootModel> callback);

    @POST("/appointments")
    void postAppointment(
            @Body PostingData appointment,
            @Header("Auth-Token") String authToken,
            @Header("Api-Key") String apiKey,
            Callback<ApiRootModel> callback);

    @GET("/appointments")
    void getAllAppointments(
            @Header("Auth-Token") String authToken,
            @Header("Api-Key") String apiKey,
            Callback<ApiRootModel> callback);

    @GET("/appointments/{appointment_id}")
    void getAppointmentById(
            @Path("appointment_id") int appointmentId,
            @Header("Auth-Token") String authToken,
            @Header("Api-Key") String apiKey,
            Callback<ApiRootModel> callback);

    @DELETE("/appointments/{appointment_id}")
    void deleteAppointmentById(
            @Path("appointment_id") String appointmentId,
            @Header("Auth-Token") String authToken,
            @Header("Api-Key") String apiKey,
            Callback<ApiRootModel> callback);

    @GET("/appointments")
    void getAppointmentsForDayClinic(
            @Query("date") String date,
            @Query("clinic_id") String clinic,
            Callback<List<Appointment>> callback);

    @PUT("/appointments/{id}")
    void putAppointmentStatus(
            @Body PostingData appointmentStatus,
            @Path("id") int appointmentId,
            @Header("Auth-Token") String authToken,
            @Header("Api-Key") String apiKey,
            Callback<ApiRootModel> callback);

    @GET("/service_options")
    void getAllServiceOptions(
            @Header("Auth-Token") String authToken,
            @Header("Api-Key") String apiKey,
            Callback<ApiRootModel> callback);

    @GET("/service_options/{service_option_id}")
    void getServiceOptionById(
            @Path("service_option_id") String serviceOptionId,
            @Header("Auth-Token") String authToken,
            @Header("Api-Key") String apiKey,
            Callback<ApiRootModel> callback);

    @GET("/clinics")
    void getAllClinics(
            @Header("Auth-Token") String authToken,
            @Header("Api-Key") String apiKey,
            Callback<ApiRootModel> callback);

    @GET("/clinics/{clinic_id}")
    void getClinicById(
            @Path("clinic_id") String clinicId,
            @Header("Auth-Token") String authToken,
            @Header("Api-Key") String apiKey,
            Callback<ApiRootModel> callback);

    @GET("/clinics/{clinic_id}/announcements")
    void getAllAnnouncementsForClinic(
            @Path("clinic_id") String clinicId,
            @Header("Auth-Token") String authToken,
            @Header("Api-Key") String apiKey,
            Callback<ApiRootModel> callback);

    @GET("/clinics/{clinic_id}/announcements/{announcement_id}")
    void getAnnouncementForClinicById(
            @Path("clinic_id") String clinicId,
            @Path("announcement_id") String announcementId,
            @Header("Auth-Token") String authToken,
            @Header("Api-Key") String apiKey,
            Callback<ApiRootModel> callback);

    @GET("/service_users")
    void getAllServiceUsers(
            @Header("Auth-Token") String authToken,
            @Header("Api-Key") String apiKey,
            Callback<ApiRootModel> callback);

    @GET("/service_users/{service_user_id}")
    void getServiceUserById(
            @Path("service_user_id") int serviceUserId,
            @Header("Auth-Token") String authToken,
            @Header("Api-Key") String apiKey,
            Callback<ApiRootModel> callback);

    @GET("/service_users")
    void getServiceUserByName(
            @Query("name") String serviceUserName,
            @Header("Auth-Token") String authToken,
            @Header("Api-Key") String apiKey,
            Callback<ApiRootModel> callback);

    @GET("/service_users")
    void getServiceUserByNameDobHospitalNum(
            @Query("name") String name,
            @Query("hospital_number") String hospitalNumber,
            @Query("dob") String dob,
            @Header("Auth-Token") String authToken,
            @Header("Api-Key") String apiKey,
            Callback<ApiRootModel> callback);

    @GET("/service_providers")
    void getAllServiceProviders(
            @Header("Auth-Token") String authToken,
            @Header("Api-Key") String apiKey,
            Callback<ApiRootModel> callback);

    @GET("/service_providers/{service_provider_id}")
    void getServiceProviderById(
            @Path("service_provider_id") String serviceProviderId,
            @Header("Auth-Token") String authToken,
            @Header("Api-Key") String apiKey,
            Callback<ApiRootModel> callback);

    @GET("/pregnancies")
    void getAllPregnancies(
            @Header("Auth-Token") String authToken,
            @Header("Api-Key") String apiKey,
            Callback<ApiRootModel> callback);

    @GET("/pregnancies/{pregnancy_ids}")
    void getPregnancyById(
            @Path("pregnancy_ids") String pregnancyIds,
            @Header("Auth-Token") String authToken,
            @Header("Api-Key") String apiKey,
            Callback<ApiRootModel> callback);

    @GET("/babies")
    void getAllBabies(
            @Header("Auth-Token") String authToken,
            @Header("Api-Key") String apiKey,
            Callback<ApiRootModel> callback);

    @GET("/babies/{baby_ids}")
    void getBabyById(
            @Path("baby_ids") String babyyIds,
            @Header("Auth-Token") String authToken,
            @Header("Api-Key") String apiKey,
            Callback<ApiRootModel> callback);
}
