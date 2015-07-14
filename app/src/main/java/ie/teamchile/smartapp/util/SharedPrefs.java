package ie.teamchile.smartapp.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.google.gson.Gson;

import ie.teamchile.smartapp.activities.BaseActivity;
import ie.teamchile.smartapp.model.BaseModel;
import ie.teamchile.smartapp.model.PostingData;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * Created by user on 7/8/15.
 */
public class SharedPrefs extends BaseActivity {
    private SharedPreferences prefs;
    private SharedPreferences.Editor editor;

    public SharedPrefs() {
    }

    public String getStringPrefs(Context context, String tag){
        prefs = context.getSharedPreferences("SMART", MODE_PRIVATE);
        String prefsData = prefs.getString(tag, "");
        return prefsData;
    }


    public void setStringPrefs(Context context, String data, String tag) {
        editor = context.getSharedPreferences("SMART", MODE_PRIVATE).edit();
        editor.putString(tag, data);
        editor.commit();
    }

    public void overWriteStringPrefs(Context context, String tag, String data){
        prefs = context.getSharedPreferences("SMART", MODE_PRIVATE);
        if(prefs.contains(tag))
            deletePrefs(context, tag);

        setStringPrefs(context, data, tag);
    }

    public PostingData getObjectFromString(String dataAsString){
        Gson gson = new Gson();
        String json = dataAsString;
        PostingData data = gson.fromJson(json, PostingData.class);
        return data;
    }

    public PostingData getJsonPrefs(String tag, Context context) {
        prefs = context.getSharedPreferences("SMART", MODE_PRIVATE);
        String dataAsString = prefs.getString(tag, "");
        Gson gson = new Gson();
        String json = dataAsString;
        PostingData data = gson.fromJson(json, PostingData.class);

        return data;
    }

    public void setJsonPrefs(Context context, PostingData data, String tag) {
        editor = context.getSharedPreferences("SMART", MODE_PRIVATE).edit();
        Gson gson = new Gson();
        String json = gson.toJson(data);
        editor.putString(tag, json);
        editor.commit();
    }

    public void deletePrefs(Context context, String tag) {
        Log.d("prefs", "deletePrefs called");
        prefs = context.getSharedPreferences("SMART", MODE_PRIVATE);
        editor = context.getSharedPreferences("SMART", MODE_PRIVATE).edit();
        if(prefs.contains(tag)) {
            editor.remove(tag);
            editor.apply();
        }
    }

    public void postAppointment(PostingData data, final Context context, final String tag) {
        initRetrofit();

        api.postAppointment(
                data,
                BaseModel.getInstance().getLogin().getToken(),
                NotKeys.API_KEY,
                new Callback<BaseModel>() {
                    @Override
                    public void success(BaseModel baseModel, Response response) {
                        Log.d("retro", "post appointment success");
                        deletePrefs(context, tag);
                    }

                    @Override
                    public void failure(RetrofitError error) {
                        Log.d("retro", "post appointment failure = " + error);
                    }
                }
        );
    }
}
