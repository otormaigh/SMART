package models;

import java.util.ArrayList;
import java.util.HashMap;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import connecttodb.AccessDBTable;
import android.os.AsyncTask;
import android.util.Log;

public class Clinics_model {	
	private static Clinics_model singleInstance;
	private JSONArray clinicsArray = new JSONArray();
	private HashMap<String, String> idHash = new HashMap<String, String>();
	private ArrayList<String> idList;
	private AccessDBTable db = new AccessDBTable();
	private String response;
	private JSONArray query;
	private JSONObject jsonNew;

	private int id;
	private String name;
	private String address;
	private String openingHours;
	private String closingHours;
	private String recurrence;
	private String type;
	private int appointmentIntervals;
	private String days;

	private Clinics_model() {
	}	
	public static Clinics_model getSingletonIntance() {
		if(singleInstance == null) {
			singleInstance = new Clinics_model();
		}
		return singleInstance;
	}	
	public void updateLocal(){		
		new LongOperation().execute();
	}
	private class LongOperation extends AsyncTask<Void, Void, JSONArray> {
		@Override
		protected void onPreExecute() {
		}
		protected JSONArray doInBackground(Void... params) {
			Log.d("singleton", "in updateLocal doInBackground");
			try {
				// read in full clinic list
				response = db.accessDB(Login_model.getToken(), "clinics");
				// parse response as JsonObject
				jsonNew = new JSONObject(response);
				query = jsonNew.getJSONArray("clinics");
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
        }
	}
	public HashMap<String, String> getHashMapofIdClinic(){
		return idHash;
	}
	public void setHashMapofIdClinic(JSONArray clinicArray) {
		ArrayList<JSONObject> jsonValues = new ArrayList<JSONObject>();
		HashMap<String, String> idHash = new HashMap<String, String>();
		String id; // key
		String clinic; // value
		
		try {
			for (int i = 0; i < clinicArray.length(); i++) {
				jsonValues.add(clinicArray.getJSONObject(i));
			}	
			for (int i = 0; i < jsonValues.size(); i++) {
				id = String.valueOf((jsonValues.get(i).getInt("id")));
				clinic = jsonValues.get(i).toString();			
				idHash.put(id, clinic);
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
		Log.d("singleton", "idhash of clinics: " + idHash);
		this.idHash = idHash;
	}

	public int getId() {
		return id;
	}

	public void setId(int newId) {
		id = newId;
	}

	public String getName(String idToSearch) {
		/**
		 *  helper method to give back the name of the clinic
		 *  in the ID's hashmap
		 */
		String name = "";
		JSONObject jsondude;		
		try {
			jsondude = new JSONObject(idHash.get(idToSearch));
			name = jsondude.get("name").toString();	
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return name;
	}

	public void setName(String newName) {
		name = newName;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String newAddress) {
		address = newAddress;
	}

	public String getOpeningHours(String idToSearch) {
		/**
		 * helper to get the opening times from the 
		 * HashMap of ID's
		 */
		String openingHour = "";
		JSONObject jsondude;		
		try {
			jsondude = new JSONObject(idHash.get(idToSearch));
			openingHour = jsondude.get("opening_time").toString();	
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return openingHour;
	}

	public void setOpeningHours(String newOpeningHours) {
		openingHours = newOpeningHours;
	}

	public String getClosingHours(String idToSearch) {
		String closingHour = "";
		JSONObject jsondude;		
		try {
			jsondude = new JSONObject(idHash.get(idToSearch));
			closingHour = jsondude.get("closing_time").toString();	
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return closingHour;
	}

	public void setClosingHours(String newClosingHours) {
		closingHours = newClosingHours;
	}

	public String getRecurrence() {
		return recurrence;
	}

	public void setRecurrence(String newRecurrence) {
		recurrence = newRecurrence;
	}

	public String getType() {
		return type;
	}

	public void setType(String newType) {
		type = newType;
	}

	public int getAppointmentIntervals() {
		return appointmentIntervals;
	}

	public void setAppointmentIntervals(int newAppointmentIntervals) {
		appointmentIntervals = newAppointmentIntervals;
	}

	public String getDays() {
		return days;
	}

	public void setDays(String newDays) {
		days = newDays;
	}
}