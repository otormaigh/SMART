package utility;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.CountDownTimer;
import android.util.Log;
import connecttodb.AccessDBTable;

public class ServiceOptionSingleton {
	private static ServiceOptionSingleton singleInstance;
	private AccessDBTable db = new AccessDBTable();
	private JsonParseHelper help = new JsonParseHelper();
	private Map<String, JSONObject> idMap;
	private List<JSONObject> jsonValues;
	private JSONObject json;
	private JSONArray query;
	private ProgressDialog pd;
	
	private ServiceOptionSingleton() {
	}	
	
	public static synchronized ServiceOptionSingleton getInstance() {
		if(singleInstance == null) {
			singleInstance = new ServiceOptionSingleton();
		}
		return singleInstance;
	}	
	
	public void updateLocal(Context context, ProgressDialog pd){		
		new LongOperation().execute("service_options");
	}
	
	private class LongOperation extends AsyncTask<String, Void, JSONArray> {
		@Override
		protected void onPreExecute() {
		}
		protected JSONArray doInBackground(String... params) {
			Log.d("singleton", "in service options updateLocal doInBackground");
			try {
				json = db.accessDB(params[0]);
				query = json.getJSONArray(params[0]);
			} catch (JSONException e) {
				e.printStackTrace();
			}
			Log.d("singleton", "query = " + query);
			setMapOfID(query);
			return query;
		}
		@Override
		protected void onProgressUpdate(Void... values) {
		}
		@Override
        protected void onPostExecute(JSONArray result) {
        }
	}
	
	public void setMapOfID(JSONArray jArray){
		jsonValues = new ArrayList<JSONObject>();
		idMap = new HashMap<String, JSONObject>();
		String id; // key
		JSONObject serviceOption; // value
		
		try {
			for (int i = 0; i < jArray.length(); i++) {		 
				jsonValues.add(jArray.getJSONObject(i));		
			}	
			for (int i = 0; i < jsonValues.size(); i++) {
				id = String.valueOf((jsonValues.get(i).getInt("id")));
				serviceOption = jsonValues.get(i);			
				idMap.put(id, serviceOption);
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
		Log.d("singleton", "idMap of service options: " + idMap);		
	}
	
	public Map<String, JSONObject> getMapOfID(){
		return idMap;
	}
	
	public List<String> getClinicIDs(String id){
		JSONObject json = idMap.get(id);
		return help.jsonParseHelperList(json, "service_options", "clinic_ids");
	}
	
	public String getID(String id){
		JSONObject json = idMap.get(id);;
		return help.jsonParseHelper(json, "service_options", "id");
	}
	
	public String getName(String id){
		JSONObject json = idMap.get(id);
		return help.jsonParseHelper(json, "service_options", "name");
	}
}