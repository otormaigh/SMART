package ie.teamchile.smartapp.util;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Locale;

/**
 * Created by user on 9/4/15.
 */
public interface Constants {

    int SPLASH_TIME_OUT = 2500;      //2.5 seconds

    String SHARED_PREFS_SPLASH_LOG = "splash_log";

    //Realm ResponseClinic.class;
    String REALM_ID = "id";

    //Realm ResponseServiceOption.class
    String REALM_HOME_VISIT = "homeVisit";

    //Realm ResponseAppointment.class
    String REALM_CLINIC_ID = "clinicId";
    String REALM_SERVICE_OPTION_IDS = "serviceOptionIds";
    String REALM_DATE = "date";

    //Realm ResponsePregnancy.class
    String REALM_SERVICE_USER_ID = "serviceUserId";

    //Date Formats
    DateFormat DF_AM_PM = new SimpleDateFormat("HH:mm a", Locale.getDefault());
    DateFormat DF_DAY_LONG = new SimpleDateFormat("EEEE", Locale.getDefault());
    DateFormat DF_DATE_TIME = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
    DateFormat DF_DATE_ONLY = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
    DateFormat DF_TIME_ONLY = new SimpleDateFormat("HH:mm", Locale.getDefault());
    DateFormat DF_DAY_SHORT = new SimpleDateFormat("E", Locale.getDefault());
    DateFormat DF_TIME_W_SEC = new SimpleDateFormat("HH:mm:ss", Locale.getDefault());
    DateFormat DF_DOW_MONTH_DAY = new SimpleDateFormat("EEE, d MMM", Locale.getDefault());
    DateFormat DF_MONTH_FULL_NAME = new SimpleDateFormat("dd MMMM yyyy", Locale.getDefault());
    DateFormat DF_DATE_TIME_W_ZONE = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZ", Locale.getDefault());
    DateFormat DF_SERVER_DATE_FULL = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.getDefault());
    DateFormat DF_SERVER_DATE_SMALL = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
    DateFormat DF_DATE_W_MONTH_NAME = new SimpleDateFormat("dd MMM", Locale.getDefault());
    DateFormat DF_HUMAN_READABLE_DATE = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
    DateFormat DF_DATE_MONTH_NAME_YEAR = new SimpleDateFormat("dd MMM yyyy", Locale.getDefault());
    DateFormat DF_HUMAN_READABLE_TIME_DATE = new SimpleDateFormat("HH:mm, dd/MM/yyyy", Locale.getDefault());

    //Shared Prefs
    String APPTS_GOT = "appts_got";
    String APPOINTMENT_POST = "appointment_post";
    String REUSE = "reuse";
    String NAME = "name";
    String ID = "id";
    String VISIT_TYPE = "visit_type";
    String HOSPITAL_NUMBER = "hospitalNumber";
    String EMAIL = "email";
    String MOBILE = "mobile";
    String CLINIC_STARTED = "clinic_started";

    //Intent Keys
    String ARGS_PREGNANCY_ID = "pregnancyId";
    String ARGS_CLINIC_ID = "clinicId";
    String ARGS_TIME = "time";
    String ARGS_SERVICE_OPTION_ID = "serviceOptionId";
    String ARGS_FROM = "from";
    String ARGS_CLINIC_APPOINTMENT = "clinic-appointment";

    String ARGS_HOME_VISIT = "home-visit";
    String ARGS_RETURNING = "returning";
    String ARGS_POST_NATAL = "post-natal";
    String ARGS_SCHEDULED = "scheduled";
    String ARGS_NEW = "new";
    String ARGS_DROP_IN = "drop-in";

    //String formatting
    String FORMAT_TV_CONFIRM_USERNAME = "%s (%s)";
    String FORMAT_TV_CONFIRM_DATE_TIME_1 = "%s, %s";
    String FORMAT_TV_CONFIRM_DATE_TIME_2 = "%s on %s";
    String FORMAT_PREFS_APPT_POST = "appointment_post_%s";
    String FORMAT_TV_CHAR_COUNT = "%s/%s";
    String FORMAT_DOB = "%s-%s-%s";
    String FORMAT_PARITY_DETAILS_TITLE = "%s (%s)";
    String FORMAT_DOMINO = "%s (Domino)";
    String FORMAT_SATELLITE = "%s (Satellite)";

    String TPW_ABOUT_URL = "/#/about";
}
