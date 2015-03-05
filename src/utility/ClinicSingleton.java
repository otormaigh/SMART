package utility;

import java.util.ArrayList;
import java.util.HashMap;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import connecttodb.AccessDBTable;

public class ClinicSingleton {	
	private static ClinicSingleton singleInstance;
	private HashMap<String, String> idHash;
	private ArrayList<JSONObject> jsonValues;
	private AccessDBTable db = new AccessDBTable();
	private String response;
	private JSONArray query;
	private JSONObject jsonNew;
	private ProgressDialog pd;

	private int id, appointmentIntervals;
	private String name, address, openingHour, closingHour,
				   recurrence, type, days;

	private ClinicSingleton() {
	}	
	public static synchronized ClinicSingleton getSingletonIntance() {
		if(singleInstance == null) {
			singleInstance = new ClinicSingleton();
		}
		return singleInstance;
	}	
	public void updateLocal(Context context){		
		new LongOperation(context).execute("clinics");
	}
	private class LongOperation extends AsyncTask<String, Void, JSONArray> {
		private Context context;
		public LongOperation(Context context){
			this.context = context;
		}
		@Override
		protected void onPreExecute() {
			pd = new ProgressDialog(context);
			pd.setMessage("Logging In . . . .");
			pd.show();
		}
		protected JSONArray doInBackground(String... params) {
			Log.d("singleton", "in clinic updateLocal doInBackground");
			try {
				// read in full clinic list
				response = db.accessDB(params[0]);
				// parse response as JsonObject
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
			setHashMapofIdClinic(result);
			pd.dismiss();
        }
	}
	public HashMap<String, String> getHashMapofIdClinic(){
		return idHash;
	}
	public void setHashMapofIdClinic(JSONArray clinicArray) {
		jsonValues = new ArrayList<JSONObject>();
		idHash = new HashMap<String, String>();
		String id; // key
		String clinic; // value
		
		try {
			/**
			 * iterates through input JSONArray
			 * parses JSONObjects from JSONArray
			 * puts them into an ArrayList of JSONObjects
			 */
			for (int i = 0; i < clinicArray.length(); i++) {		 
				jsonValues.add(clinicArray.getJSONObject(i));		
			}	
			/**
			 * iterates through ArrayList
			 * parses out id and clinic String
			 * sets to a HashMap of ID as Key and Clinic String as value
			 */
			for (int i = 0; i < jsonValues.size(); i++) {
				id = String.valueOf((jsonValues.get(i).getInt("id")));
				clinic = jsonValues.get(i).toString();			
				idHash.put(id, clinic);
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
		Log.d("singleton", "idhash of clinics: " + idHash);
		//this.idHash = idHash;
	}

	public int getId() {
		return id;
	}
	public String getIDFromName(String name){
		JSONObject json;
		String nameFromDB = "";
		for(int i = 1; i <= idHash.size(); i++){
			try {
				json = new JSONObject(idHash.get(String.valueOf(i)));			
				nameFromDB = json.get("name").toString();	
			
			if(nameFromDB.equals(name)){
				return String.valueOf(i);
			}
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}
		return null;
	}
	public String getName(String idToSearch) {
		/**
		 *  helper method to give back the name of the clinic
		 *  in the ID's hashmap
		 */
		JSONObject json;		
		try {
			json = new JSONObject(idHash.get(idToSearch));
			name = json.get("name").toString();	
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return name;
	}

	public String getAddress() {
		return address;
	}

	public String getOpeningHours(String idToSearch) {
		/**
		 * helper to get the opening times from the 
		 * HashMap of ID's
		 */
		JSONObject json;		
		try {
			json = new JSONObject(idHash.get(idToSearch));
			openingHour = json.get("opening_time").toString();	
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return openingHour;
	}

	public String getClosingHours(String idToSearch) {
		/**
		 * helper to get the closing times from the 
		 * HashMap of ID's
		 */
		JSONObject json;		
		try {
			json = new JSONObject(idHash.get(idToSearch));
			closingHour = json.get("closing_time").toString();	
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return closingHour;
	}

	public String getRecurrence() {
		return recurrence;
	}

	public String getType() {
		return type;
	}

	public int getAppointmentIntervals(String idToSearch) {
		/**
		 * helper to get the closing times from the 
		 * HashMap of ID's
		 */
		JSONObject json;		
		try {
			json = new JSONObject(idHash.get(idToSearch));
			appointmentIntervals = (int) json.get("appointment_interval");	
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return appointmentIntervals;
	}

	public String getDays() {
		return days;
	}
}