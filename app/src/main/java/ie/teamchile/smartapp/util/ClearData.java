package ie.teamchile.smartapp.util;

import android.content.Context;
import android.util.Log;

import java.io.File;

import ie.teamchile.smartapp.activities.BaseActivity;
import ie.teamchile.smartapp.model.BaseModel;
import timber.log.Timber;

/**
 * Created by user on 7/4/15.
 */
public class ClearData extends BaseActivity{
    private SharedPrefs sharedPrefs = new SharedPrefs();

    public ClearData(Context context) {
        Log.d("purge", "ClearData called");
        startPurge(context);
        deleteSharedPref(context);
    }

    public void startPurge(Context context) {
        Log.d("purge", "startPurge called");
        BaseModel.getInstance().deleteInstance();
        trimCache(context);
    }

    public static void trimCache(Context context) {
        Log.d("purge", "trimcache called");
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
        Log.d("purge", "deleteDir called");
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

    public void deleteSharedPref(Context context){
        sharedPrefs.deletePrefs(context, "appts_got");
        sharedPrefs.deletePrefs(context, "clinic_started");
        sharedPrefs.deletePrefs(context, "reuse");
        sharedPrefs.deletePrefs(context, "root_check");
    }
}
