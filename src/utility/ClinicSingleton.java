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

/*
"clinics": [
            {
                "address": "Leopardstown Shopping Centre, Unit 12, Ballyogan Road, Dublin 18", 
                "announcement_ids": [], 
                "appointment_interval": 15, 
                "closing_time": "15:00:00", 
                "days": {
                    "friday": false, 
                    "monday": false, 
                    "saturday": false, 
                    "sunday": false, 
                    "thursday": false, 
                    "tuesday": true, 
                    "wednesday": false
                }, 
                "id": 2, 
                "links": {
                    "announcements": "announcements", 
                    "service_options": "/service_options"
                }, 
                "name": "Leopardstown", 
                "opening_time": "09:00:00", 
                "recurrence": "weekly", 
                "service_option_ids": [
                    1
                ], 
                "type": "booking"
            }
        ]
*/

public class ClinicSingleton {	
	private static ClinicSingleton singleInstance;
	private HashMap<String, String> idHash;
	private ArrayList<JSONObject> jsonValues;
	private AccessDBTable db = new AccessDBTable();
	private String response;
	private JSONArray query;
	private JSONObject jsonNew;
	private ProgressDialog pd;

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
		this.idHash = idHash;
	}
	
	public HashMap<String, String> getHashMapofIdClinic(){
		return idHash;
	}

	public String getIDFromName(String name) {
		JSONObject json;
		String nameFromDB = "";
		for (int i = 1; i <= idHash.size(); i++) {
			try {
				json = new JSONObject(idHash.get(String.valueOf(i)));
				nameFromDB = json.get("name").toString();

				if (nameFromDB.equals(name)) {
					return String.valueOf(i);
				}
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}
		return null;
	}
	
	public String getAddress(String idToSearch) {
		String address = null;
		JSONObject json;		
		try {
			json = new JSONObject(idHash.get(idToSearch));
			address = json.get("address").toString();	
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return address;
	}
	
	public String getAnnouncementID(String idToSearch) {
		String announcementID = null;
		JSONObject json;		
		try {
			json = new JSONObject(idHash.get(idToSearch));
			announcementID = json.get("announcement_ids").toString();	
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return announcementID;
	}
	
	public String getAppointmentIntervals(String idToSearch) {
		String appointmentInterval = null;
		JSONObject json;		
		try {
			json = new JSONObject(idHash.get(idToSearch));
			appointmentInterval = json.get("appointment_interval").toString();	
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return appointmentInterval;
	}
	
	public String getClosingTime(String idToSearch) {
		String closingTime = null;
		JSONObject json;		
		try {
			json = new JSONObject(idHash.get(idToSearch));
			closingTime = json.get("closing_time").toString();	
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return closingTime;
	}
	
	public String getDays(String idToSearch) {
		String days = null;
		JSONObject json;		
		try {
			json = new JSONObject(idHash.get(idToSearch));
			days = json.get("days").toString();	
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return days;
	}
	
	public String getClinicID(String idToSearch) {
		String clinicId = null;
		JSONObject json;		
		try {
			json = new JSONObject(idHash.get(idToSearch));
			clinicId = json.get("id").toString();	
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return clinicId;
	}
	
	/**
	 * 
	 * TODO: create getter for links
	 *  "links": {
     *           "announcements": "announcements", 
     *           "service_options": "/service_options"
     *       }, 
     *       
	 */
	
	public String getClinicName(String idToSearch) {
		String name = null;
		JSONObject json;		
		try {
			json = new JSONObject(idHash.get(idToSearch));
			name = json.get("name").toString();	
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return name;
	}
	
	public String getOpeningHours(String idToSearch) {
		String openingTime = null;
		JSONObject json;		
		try {
			json = new JSONObject(idHash.get(idToSearch));
			openingTime = json.get("opening_time").toString();	
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return openingTime;
	}
	
	public String getRecurrence(String idToSearch) {
		String recurrence = null;
		JSONObject json;		
		try {
			json = new JSONObject(idHash.get(idToSearch));
			recurrence = json.get("recurrence").toString();	
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return recurrence;
	}

	public String getServiceOptionID(String idToSearch) {
		String serviceOptionID = null;
		JSONObject json;		
		try {
			json = new JSONObject(idHash.get(idToSearch));
			serviceOptionID = json.get("service_option_ids").toString();	
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return serviceOptionID;
	}

	public String getClinicType(String idToSearch) {
		String type = null;
		JSONObject json;		
		try {
			json = new JSONObject(idHash.get(idToSearch));
			type = json.get("type").toString();	
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return type;
	}
}