package ie.teamchile.smartapp.util;

import android.content.Context;
import android.util.Log;

import java.io.File;

import ie.teamchile.smartapp.activities.BaseActivity;
import ie.teamchile.smartapp.model.Announcement;
import ie.teamchile.smartapp.model.Appointment;
import ie.teamchile.smartapp.model.Baby;
import ie.teamchile.smartapp.model.BaseModel;
import ie.teamchile.smartapp.model.Clinic;
import ie.teamchile.smartapp.model.ClinicTimeRecord;
import ie.teamchile.smartapp.model.ClinicalFields;
import ie.teamchile.smartapp.model.Days;
import ie.teamchile.smartapp.model.FeedingHistory;
import ie.teamchile.smartapp.model.HearingHistory;
import ie.teamchile.smartapp.model.Links;
import ie.teamchile.smartapp.model.Login;
import ie.teamchile.smartapp.model.NbstHistory;
import ie.teamchile.smartapp.model.PersonalFields;
import ie.teamchile.smartapp.model.Pregnancy;
import ie.teamchile.smartapp.model.PregnancyAction;
import ie.teamchile.smartapp.model.PregnancyNote;
import ie.teamchile.smartapp.model.RealmInteger;
import ie.teamchile.smartapp.model.RealmString;
import ie.teamchile.smartapp.model.ServiceOption;
import ie.teamchile.smartapp.model.ServiceProvider;
import ie.teamchile.smartapp.model.ServiceUser;
import ie.teamchile.smartapp.model.ServiceUserAction;
import ie.teamchile.smartapp.model.VitKHistory;
import io.realm.Realm;
import timber.log.Timber;

/**
 * Created by user on 7/4/15.
 */
public class ClearData extends BaseActivity{
    private SharedPrefs sharedPrefs = new SharedPrefs();
    private Context context;

    public ClearData(Context context) {
        Timber.d("ClearData called");
        this.context = context;
        startPurge();
        deleteSharedPref();
        clearRealm();
    }

    public void startPurge() {
        BaseModel.getInstance().deleteInstance();
        trimCache(context);
    }

    public static void trimCache(Context context) {
        try {
            File dir = context.getCacheDir();
            if (dir != null && dir.isDirectory()) {
                deleteDir(dir);
            }
        } catch (Exception e) {
            Timber.e(Log.getStackTraceString(e));
        }
    }

    public static boolean deleteDir(File dir) {
        if (dir != null && dir.isDirectory()) {
            String[] children = dir.list();
            boolean success;
            int length = children.length;
            for (int i = 0; i < length; i++) {
                success = deleteDir(new File(dir, children[i]));
                if (!success) {
                    return false;
                }
            }
        }
        return dir.delete();
    }

    public void deleteSharedPref(){
        sharedPrefs.deletePrefs(context, "appts_got");
        sharedPrefs.deletePrefs(context, "clinic_started");
        sharedPrefs.deletePrefs(context, "reuse");
        sharedPrefs.deletePrefs(context, "root_check");
    }

    public void clearRealm() {
        Realm realm = Realm.getInstance(context);
        realm.beginTransaction();
        realm.clear(Announcement.class);
        realm.clear(Appointment.class);
        realm.clear(Baby.class);
        realm.clear(Clinic.class);
        realm.clear(ClinicalFields.class);
        realm.clear(ClinicTimeRecord.class);
        realm.clear(Days.class);
        realm.clear(FeedingHistory.class);
        realm.clear(HearingHistory.class);
        realm.clear(Links.class);
        realm.clear(Login.class);
        realm.clear(NbstHistory.class);
        realm.clear(PersonalFields.class);
        realm.clear(Pregnancy.class);
        realm.clear(PregnancyAction.class);
        realm.clear(PregnancyNote.class);
        realm.clear(RealmInteger.class);
        realm.clear(RealmString.class);
        realm.clear(ServiceOption.class);
        realm.clear(ServiceProvider.class);
        realm.clear(ServiceUser.class);
        realm.clear(ServiceUserAction.class);
        realm.clear(VitKHistory.class);
        realm.commitTransaction();
    }
}
