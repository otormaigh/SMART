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
import android.util.Log;
import connecttodb.AccessDBTable;

public class ServiceOptionSingleton {
	private static ServiceOptionSingleton singleInstance;
	private AccessDBTable db = new AccessDBTable();
	private JsonParseHelper help = new JsonParseHelper();
	private List<String> clinicIDs = new ArrayList<String>();;
	private Map<String, JSONObject> idMap;
	private List<JSONObject> jsonValues;
	private ProgressDialog pd;
	private JSONObject jsonNew;
	private JSONArray query;
	private String response;
	
	private ServiceOptionSingleton() {
	}	
	
	public static synchronized ServiceOptionSingleton getInstance() {
		if(singleInstance == null) {
			singleInstance = new ServiceOptionSingleton();
		}
		return singleInstance;
	}	
	
	public void updateLocal(Context context){		
		new LongOperation(context).execute("service_options");
	}
	
	private class LongOperation extends AsyncTask<String, Void, JSONArray> {
		private Context context;
		public LongOperation(Context context){
			this.context = context;
		}
		@Override
		protected void onPreExecute() {
			pd = new ProgressDialog(context);
			pd.setMessage("Updating Service Options");
			pd.show();
		}
		protected JSONArray doInBackground(String... params) {
			Log.d("singleton", "in service options updateLocal doInBackground");
			try {
				response = db.accessDB(params[0]);
				jsonNew = new JSONObject(response);
				query = jsonNew.getJSONArray(params[0]);
			} catch (JSONException e) {
				e.printStackTrace();
			}
			Log.d("singleton", "query = " + query);
			return query;
		}
		@Override
		protected void onProgressUpdate(Void... values) {
		}
		@Override
        protected void onPostExecute(JSONArray result) {
			setMapOfID(result);
			pd.dismiss();
        }
	}
	
	private void setMapOfID(JSONArray jArray){
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
	
	public List<Integer> getClinicIDs(String id){
		JSONObject json = idMap.get(id);
		JSONArray jArray = new JSONArray();
		ArrayList<Integer> clinicID = new ArrayList<Integer>();
		try {
			jArray = json.getJSONArray("clinic_ids");
			for(int i = 0; i < jArray.length(); i++){
				clinicID.add(jArray.getInt(i));
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return clinicID;
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