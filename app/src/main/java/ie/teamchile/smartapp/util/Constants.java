package ie.teamchile.smartapp.util;

/**
 * Created by user on 9/4/15.
 */
public interface Constants {

    int SPLASH_TIME_OUT = 2500;      //2.5 seconds

    String SHARED_PREFS_SPLASH_LOG = "splash_log";

    //Realm Clinic.class;
    String Key_ID = "id";

    //Realm ServiceOption.class
    String KEY_HOME_VISIT = "homeVisit";

    //Realm Appointment.class
    String KEY_CLINIC_ID = "clinicId";
    String KEY_SERVICE_OPTION_IDS = "serviceOptionIds";
    String KEY_DATE = "date";

    //Realm Pregnancy.class
    String KEY_SERVICE_USER_ID = "serviceUserId";

    String SERVER_DATE_FORMAT = "yyyy-MM-dd'T'HH:mm:ss";
    String SERVER_SMALL_DATE = "yyyy-MM-dd";
}
