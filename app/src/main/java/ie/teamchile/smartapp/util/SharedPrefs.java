package ie.teamchile.smartapp.util;

import android.content.SharedPreferences;

import com.google.gson.Gson;

import ie.teamchile.smartapp.activities.BaseActivity;
import ie.teamchile.smartapp.model.PostingData;

/**
 * Created by user on 7/8/15.
 */
public class SharedPrefs extends BaseActivity {
    private SharedPreferences mPrefs;

    public SharedPrefs() {
    }

    private PostingData getPrefs() {
        Gson gson = new Gson();
        String json = mPrefs.getString("MyObject", "");
        PostingData data = gson.fromJson(json, PostingData.class);

        return data;
    }

    private void setPrefs(PostingData data) {
        mPrefs = getPreferences(MODE_PRIVATE);
        SharedPreferences.Editor prefsEditor = mPrefs.edit();
        Gson gson = new Gson();
        String json = gson.toJson(data);
        prefsEditor.putString("MyObject", json);
        prefsEditor.commit();
    }
}
