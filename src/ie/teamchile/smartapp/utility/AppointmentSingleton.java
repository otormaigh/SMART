package ie.teamchile.smartapp.utility;

import ie.teamchile.smartapp.connecttodb.AccessDBTable;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

/* {
    "appointments": {
        "clinic_id": 4, 
        "date": "2015-03-05", 
        "id": 64, 
        "links": {
            "service_options": "/appointments/64/service_options", 
            "service_provider": "service_providers/14", 
            "service_user": "service_users/1"
        }, 
        "priority": "scheduled", 
        "service_option_ids": [], 
        "service_provider_id": 14, 
        "service_user": {
            "gestation": null, 
            "id": 1, 
            "name": "Shannon Mercury"
        }, 
        "service_user_id": 1, 
        "time": "10:15:00", 
        "visit_logs": [], 
        "visit_type": "post-natal"
    }
}
*/

/**
 * 
 * @author Elliot
 * 
 * Methods available.
 * All can be used with either ArrayList<String> of Appointment IDs
 * or just a single Appointment ID
 * 
 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * 
 * 
 * updateLocal
 * setHashMapofClinicDateID
 * setHashMapofIdAppt
 * getListOfIDs
 * sortDates
 * getClinicID
 * getDate
 * getAppointmentID
 * TODO: getLinks
 * getPriority
 * getServiceOptionID
 * getServiceProviderID
 * getGestation
 * getName
 * getServiceUserID
 * getTime
 * getVisitLogs
 * getVisitType 
 *
 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
 */

public class AppointmentSingleton {
	private boolean updated;
	private static AppointmentSingleton singleInstance;
	private HashMap<String, HashMap<String, ArrayList<String>>> clinicIDHash;
	private DateFormat sdfTime = new SimpleDateFormat("HH:mm:ss", Locale.getDefault());
	private DateFormat sdfHHmm = new SimpleDateFormat("HH:mm", Locale.getDefault());
	private HashMap<String, JSONObject> idHash;
	private ArrayList<String> idList;
	private AccessDBTable db = new AccessDBTable();
	private JsonParseHelper help = new JsonParseHelper();
	private JSONArray query;
	private JSONObject json;
	private ProgressDialog pd;
	
	private AppointmentSingleton() {
	}
	
	public static synchronized AppointmentSingleton getInstance() {
		if(singleInstance == null) {
			singleInstance = new AppointmentSingleton();
		}
		return singleInstance;
	}
	
	public boolean getUpdated(){
		return updated;
	}
	
	public void updateLocal(Context context){
		updated = true;
		new LongOperation(context).execute("appointments");
	}
	
	//Get full appointment table from database
	private class LongOperation extends AsyncTask<String, Void, JSONArray> {
		private Context context;
		public LongOperation(Context context){
			this.context = context;
		}
		@Override
		protected void onPreExecute() {
			pd = new ProgressDialog(context);
			pd.setMessage("Updating Appointments");
			pd.show();
		}
		protected JSONArray doInBackground(String... params) {
			Log.d("singleton", "in appointment updateLocal doInBackground");
			try {
				json = db.accessDB(params[0]);
				query = json.getJSONArray(params[0]);
			} catch (JSONException e) {
				e.printStackTrace();
			}
			Log.d("singleton", "query = " + query);
			setHashMapofClinicDateID(query);
			setHashMapofIdAppt(query);
			return query;
		}
		@Override
		protected void onProgressUpdate(Void... values) {
		}
		@Override
        protected void onPostExecute(JSONArray result) {			
			pd.dismiss();
        }
	}
	
	/**
	 * 
	 * Takes in a JSONArray of appointments, iterates through it
	 * parses out date and id and sets the to a HashMap of 
	 * Date as Key and ArrayList of corresponding IDs as value
	 * 
	 */
	public void setHashMapofClinicDateID(JSONArray appointmentArray) {
		ArrayList<JSONObject> jsonValues = new ArrayList<JSONObject>();
		ArrayList<String> idArray = new ArrayList<String>();
		HashMap<String, ArrayList<String>> dateIDHash;
		HashMap<String, HashMap<String, ArrayList<String>>> clinicDateIDHash = new HashMap<String, HashMap<String, ArrayList<String>>>();
		String clinicId, date, id;
		try {
			for (int i = 0; i < appointmentArray.length(); i++) {
				jsonValues.add(appointmentArray.getJSONObject(i));
				jsonValues = sortDates(jsonValues);
			}	
			for (int i = 0; i < jsonValues.size(); i++) {
				idArray = new ArrayList<String>();
				dateIDHash = new HashMap<String, ArrayList<String>>();
				
				clinicId = String.valueOf((jsonValues.get(i).getInt("clinic_id")));
				date = (String) jsonValues.get(i).get("date");
				id = String.valueOf((jsonValues.get(i).getInt("id")));
				if (clinicDateIDHash.get(clinicId) != null) {
					dateIDHash = clinicDateIDHash.get(clinicId);
					if (dateIDHash.get(date) != null) {
						idArray = dateIDHash.get(date);
					}
					idArray.add(id);
					dateIDHash.put(date, idArray);
					clinicDateIDHash.put(clinicId, dateIDHash);
				} else {
					idArray.add(id);
					dateIDHash.put(date, idArray);
					clinicDateIDHash.put(clinicId, dateIDHash);
				}
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
		Log.d("singleton", "clinicIDHash: " + clinicDateIDHash.toString());
		this.clinicIDHash = clinicDateIDHash;
	}
	public HashMap<String, HashMap<String, ArrayList<String>>> getHashMapofClinicDateID(){
		return clinicIDHash;
	}
	
	/**
	 * 
	 * Takes in a JSONArray of appointments, iterates through it
	 * parses out id and appointment string and sets the to a 
	 * HashMap of ID as Key and appointment string as value
	 *  
	 */
	public void setHashMapofIdAppt(JSONArray appointmentArray) {
		ArrayList<JSONObject> jsonValues = new ArrayList<JSONObject>();
		HashMap<String, JSONObject> idHash = new HashMap<String, JSONObject>();
		String id;
		JSONObject appt;
		
		try {
			for (int i = 0; i < appointmentArray.length(); i++) {
				jsonValues.add(appointmentArray.getJSONObject(i));
				jsonValues = sortDates(jsonValues);
			}	
			for (int i = 0; i < jsonValues.size(); i++) {
				id = String.valueOf((jsonValues.get(i).getInt("id")));
				appt = jsonValues.get(i);				
				idHash.put(id, appt);
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
		this.idHash = idHash;
	}
	
	/**
	 * 
	 * Take in a clinic id and day in yyyy-MM-dd format
	 * searches that HashMap of Clinic/Date/ID
	 * and returns Appointment ID corresponding to that date
	 * 
	 */
	public ArrayList<String> getListOfIDs(String clinicIdTosearch, String dayToSearch) {
		idList = new ArrayList<String>();
		idList = clinicIDHash.get(clinicIdTosearch).get(dayToSearch);
		return idList;
	}	
	/**
	 * 
	 * takes in an ArrayList of JSONObject and returns
	 * an ArrayList of JSONObjects that is sorted in 
	 * chronological order based on date and time.
	 * 
	 */
	public ArrayList<JSONObject> sortDates(ArrayList<JSONObject> objToBeSorted) {
        Collections.sort(objToBeSorted, new Comparator<JSONObject>() {
            @Override
            public int compare(JSONObject a, JSONObject b) {
                String valA = new String();
                String valB = new String();
                try {
                    valA = (String) a.get("date") + " " + a.get("time");
                    valB = (String) b.get("date") + " " + b.get("time");
                } catch (JSONException e) {
                    System.out.printf("JSONException in combine JSONArrays sort section", e);
                }
                int comp = valA.compareTo(valB);

                if (comp > 0)
                    return 1;
                if (comp < 0)
                    return -1;
                return 0;
            }
        });
        return objToBeSorted;
    }
	
	public JSONObject getAppointmentString(String id){
		return idHash.get(id);
	}
	
	public String getClinicID(String id){
		JSONObject json = idHash.get(id);
		return help.jsonParseHelper(json, "appointments", "clinic_id");
	}
	
	public ArrayList<String> getClinicID(ArrayList<String> idList){
		ArrayList<String> clinicID = new ArrayList<String>();
		for(int i = 0; i < idList.size(); i++ ){
			JSONObject json = idHash.get(idList.get(i));
			clinicID.add(help.jsonParseHelper(json, "appointments", "clinic_id"));
		}
		return clinicID;
	}

	public String getDate(String id){
		JSONObject json = idHash.get(id);
		return help.jsonParseHelper(json, "appointments", "date");
	}
	
	public ArrayList<String> getDate(ArrayList<String> idList){
		ArrayList<String> date = new ArrayList<String>();
		for(int i = 0; i < idList.size(); i++ ){
			JSONObject json = idHash.get(idList.get(i));
			date.add(help.jsonParseHelper(json, "appointments", "date"));
		}
		return date;
	}

	public String getAppointmentID(String id){
		JSONObject json = idHash.get(id);
		return help.jsonParseHelper(json, "appointments", "appointment_id");
	}
	
	public ArrayList<String> getAppointmentID(ArrayList<String> idList){
		ArrayList<String> appointmentID = new ArrayList<String>();
		for(int i = 0; i < idList.size(); i++ ){
			JSONObject json = idHash.get(idList.get(i));
			appointmentID.add(help.jsonParseHelper(json, "appointments", "appointment_id"));
		}
		return appointmentID;
	}
	
	/**TODO do something with links	  
	  "links": {
            "service_options": "/appointments/64/service_options", 
            "service_provider": "service_providers/14", 
            "service_user": "service_users/1"
        },
	*/
	public String getLinks(String id){
		JSONObject json = idHash.get(id);
		return help.jsonParseHelper(json, "appointments", "links");
	}
	
	public ArrayList<String> getLinks(ArrayList<String> idList){
		ArrayList<String> links = new ArrayList<String>();
		for(int i = 0; i < idList.size(); i++ ){
			JSONObject json = idHash.get(idList.get(i));
			links.add(help.jsonParseHelper(json, "appointments", "links"));
		}
		return links;
	}
	
	public String getPriority(String id){
		JSONObject json = idHash.get(id);
		return help.jsonParseHelper(json, "appointments", "priority");
	}
	
	public ArrayList<String> getPriority(ArrayList<String> idList){
		ArrayList<String> priority = new ArrayList<String>();
		for(int i = 0; i < idList.size(); i++ ){
			JSONObject json = idHash.get(idList.get(i));
			priority.add(help.jsonParseHelper(json, "appointments", "priority"));
		}
		return priority;
	}
	
	public String getServiceOptionID(String id){
		JSONObject json = idHash.get(id);
		return help.jsonParseHelper(json, "appointments", "service_option_ids");
	}
	
	public ArrayList<String> getServiceOptionID(ArrayList<String> idList){
		ArrayList<String> serviceOptionID = new ArrayList<String>();
		for(int i = 0; i < idList.size(); i++ ){
			JSONObject json = idHash.get(idList.get(i));
			serviceOptionID.add(help.jsonParseHelper(json, "appointments", "service_option_ids"));
		}
		return serviceOptionID;
	}
	
	public String getServiceProviderID(String id){
		JSONObject json = idHash.get(id);
		return help.jsonParseHelper(json, "appointments", "service_provider_id");
	}
	
	public ArrayList<String> getServiceProviderID(ArrayList<String> idList){
		ArrayList<String> serviceProviderID = new ArrayList<String>();
		for(int i = 0; i < idList.size(); i++ ){
			JSONObject json = idHash.get(idList.get(i));
			serviceProviderID.add(help.jsonParseHelper(json, "appointments", "service_provider_id"));
		}
		return serviceProviderID;
	}
	
	public String getGestation(String id){
		JSONObject json = idHash.get(id);
		return help.jsonParseHelper(json, "appointments", "gestation");
	}
	
	public ArrayList<String> getGestation(ArrayList<String> idList){
		ArrayList<String> gestation = new ArrayList<String>();
		for(int i = 0; i < idList.size(); i++ ){
			JSONObject json = idHash.get(idList.get(i));
			gestation.add(help.jsonParseHelper(json, "appointments", "gestation"));
		}
		return gestation;
	}
	
	public String getServiceUserID(String id){
		JSONObject json = idHash.get(id);
		return help.jsonParseHelper(json, "appointments", "service_user_id");
	}
	
	public ArrayList<String> getServiceUserID(ArrayList<String> idList){
		ArrayList<String> serviceUserID = new ArrayList<String>();
		for(int i = 0; i < idList.size(); i++ ){
			JSONObject json = idHash.get(idList.get(i));
			serviceUserID.add(help.jsonParseHelper(json, "appointments", "service_user_id"));
		}
		return serviceUserID;
	}

	public String getName(String id){
		JSONObject json = idHash.get(id);
		return help.jsonParseHelper(json, "appointments", "name");
	}
	
	public ArrayList<String> getName(ArrayList<String> idList){
		ArrayList<String> name = new ArrayList<String>();
		for(int i = 0; i < idList.size(); i++ ){
			JSONObject json = idHash.get(idList.get(i));
			name.add(help.jsonParseHelper(json, "appointments", "name"));
		}
		return name;
	}
	
	public String getTime(String id){
		JSONObject json = idHash.get(id);
		return removeSeconds(help.jsonParseHelper(json, "appointments", "time"));
	}
	
	public ArrayList<String> getTime(ArrayList<String> idList) throws NullPointerException{
		ArrayList<String> time = new ArrayList<String>();
		for(int i = 0; i < idList.size(); i++ ){
			JSONObject json = idHash.get(idList.get(i));
			time.add(removeSeconds(help.jsonParseHelper(json, "appointments", "time")));
		}
		return time;
	}
	
	public String getVisitLogs(String id){
		JSONObject json = idHash.get(id);
		return help.jsonParseHelper(json, "appointments", "visit_logs");
	}
	
	public ArrayList<String> getVisitLogs(ArrayList<String> idList){
		ArrayList<String> visitLogs = new ArrayList<String>();
		for(int i = 0; i < idList.size(); i++ ){
			JSONObject json = idHash.get(idList.get(i));
			visitLogs.add(help.jsonParseHelper(json, "appointments", "visit_logs"));
		}
		return visitLogs;
	}
	
	public String getVisitType(String id){
		JSONObject json = idHash.get(id);
		return help.jsonParseHelper(json, "appointments", "visit_type");
	}
	
	public ArrayList<String> getVisitType(ArrayList<String> idList){
		ArrayList<String> visitType = new ArrayList<String>();
		for(int i = 0; i < idList.size(); i++ ){
			JSONObject json = idHash.get(idList.get(i));
			visitType.add(help.jsonParseHelper(json, "appointments", "visit_type"));
		}
		return visitType;
	}
	
	public String removeSeconds(String time){
		Date oldTime = null;
		String newTime = "";
		try {
			oldTime = sdfTime.parse(time);
			newTime = sdfHHmm.format(oldTime);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return newTime;
	}
}