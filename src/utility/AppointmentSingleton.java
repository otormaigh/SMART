package utility;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.os.AsyncTask;
import android.util.Log;
import connecttodb.AccessDBTable;

public class AppointmentSingleton {
	private static AppointmentSingleton singleInstance;
	private JSONArray appointmentArray = new JSONArray();
	private HashMap<String, ArrayList<String>> dateHash = new HashMap<String, ArrayList<String>>();
	private HashMap<String, HashMap<String, ArrayList<String>>> clinicIDHash = new HashMap<String, HashMap<String, ArrayList<String>>>();
	private HashMap<String, String> idHash = new HashMap<String, String>();
	private ArrayList<String> idList;
	private AccessDBTable db = new AccessDBTable();
	private String response;
	private JSONArray query;
	private JSONObject jsonNew;
	
	private String id, clinicId, date, time, serviceProvderId, 
				   serviceUserId, vistType, serviceOptionId;
	private boolean priority;
		
	private AppointmentSingleton() {
	}
	
	public static AppointmentSingleton getSingletonIntance() {
		if(singleInstance == null) {
			singleInstance = new AppointmentSingleton();
		}
		return singleInstance;
	}
	public void updateLocal(){		
		new LongOperation().execute("appointments");
	}
	private class LongOperation extends AsyncTask<String, Void, JSONArray> {
		@Override
		protected void onPreExecute() {
		}
		protected JSONArray doInBackground(String... params) {
			Log.d("singleton", "in appointment updateLocal doInBackground");
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
			setHashMapofClinicDateID(result);
			setHashMapofIdAppt(result);
        }
	}
	public JSONArray getAppointmentArray() {
		return appointmentArray;
	}
	public void setAppointmentArray(JSONArray appointmentArray) {
		this.appointmentArray = appointmentArray;
	}
	public HashMap<String, ArrayList<String>> getHashMapofClinicDateID(){
		return dateHash;				//return Hashmap of Date as Key, ID as Value
	}
	public ArrayList<String> getIdAtDate(String query){
		return dateHash.get(query);		// returns id at date 
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
		ArrayList<String> idArray;
		HashMap<String, ArrayList<String>> dateHash;
		HashMap<String, HashMap<String, ArrayList<String>>> clinicIDHash = new HashMap<String, HashMap<String, ArrayList<String>>>();
		try {
			for (int i = 0; i < appointmentArray.length(); i++) {
				jsonValues.add(appointmentArray.getJSONObject(i));
				jsonValues = sortDates(jsonValues);
			}	
			for (int i = 0; i < jsonValues.size(); i++) {
				idArray = new ArrayList<String>();
				dateHash = new HashMap<String, ArrayList<String>>();
				
				clinicId = String.valueOf((jsonValues.get(i).getInt("clinic_id")));
				id = String.valueOf((jsonValues.get(i).getInt("id")));
				date = (String) jsonValues.get(i).get("date");
				if (clinicIDHash.get(clinicId) != null) {
					dateHash = clinicIDHash.get(clinicId);
					if (dateHash.get(date) != null) {
						idArray = dateHash.get(date);
					}
					idArray.add(id);
					dateHash.put(date, idArray);
					clinicIDHash.put(clinicId, dateHash);
				} else {
					idArray.add(id);
					dateHash.put(date, idArray);
					clinicIDHash.put(clinicId, dateHash);
				}
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
		Log.d("singleton", "clinicIDHash: " + clinicIDHash.toString());
		this.clinicIDHash = clinicIDHash;
	}
	public HashMap<String, String> getHashMapofIdAppt(){
		return idHash;
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
		HashMap<String, String> idHash = new HashMap<String, String>();
		String id;
		String appt;
		
		try {
			for (int i = 0; i < appointmentArray.length(); i++) {
				jsonValues.add(appointmentArray.getJSONObject(i));
				jsonValues = sortDates(jsonValues);
			}	
			for (int i = 0; i < jsonValues.size(); i++) {
				id = String.valueOf((jsonValues.get(i).getInt("id")));
				appt = jsonValues.get(i).toString();				
				idHash.put(id, appt);
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
		this.idHash = idHash;
	}
	/**
	 * 
	 * Take in a day in yyyy-MM-dd format
	 * searches that HashMap of Date/ID
	 * and returns ID corresponding to that date
	 * 
	 */
	public ArrayList<String> getIds(String clinicIdTosearch, String dayToSearch) {
		idList = new ArrayList<String>();
		idList = clinicIDHash.get(clinicIdTosearch).get(dayToSearch);
		return idList;
	}
	public String getDate() {		
		return date;
	}
	public ArrayList<String> getTime(ArrayList<?> idList) { // get the specific id not list of???
		ArrayList<String> time = new ArrayList<String>();
		JSONObject json;
		
		for(int i = 0; i < idList.size(); i++ ){
			try {
				json = new JSONObject(idHash.get(idList.get(i)));
				time.add(json.get("time").toString());				
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}
		return time;
	}
	public String getTime(String id){		
		String time = null;
		JSONObject json;
		try {
			json = new JSONObject(idHash.get(id));
			time = json.get("time").toString();				
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return time;
	}
	public String getDate(String id) {
		String date = null;
		JSONObject json;
		try {
			json = new JSONObject(idHash.get(id));
			date = json.get("date").toString();				
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return date;
	}
	public ArrayList<String> getName(ArrayList<?> idList){		
		ArrayList<String> name = new ArrayList<String>();
		JSONObject json;
		
		for(int i = 0; i < idList.size(); i++ ){
			try {
				json = new JSONObject(idHash.get(idList.get(i)));
				name.add(((JSONObject) json.get("service_user")).get("name").toString());				
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}
		return name;
	}
	public String getName(String id){		
		String name = null;
		JSONObject json;
		try {
			json = new JSONObject(idHash.get(id));
			name = ((JSONObject) json.get("service_user")).get("name").toString();				
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return name;
	}
	public ArrayList<String> getGestation(ArrayList<?> idList){		
		ArrayList<String> gest = new ArrayList<String>();
		JSONObject json;
		
		for(int i = 0; i < idList.size(); i++ ){
			try {
				json = new JSONObject(idHash.get(idList.get(i)));
				gest.add(((JSONObject) json.get("service_user")).get("gestation").toString());				
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}
		return gest;
	}
	/**
	 * 
	 * Takes in an ArrayList of appointment ids and returns
	 * time, name and gestation for each appointment wanted 
	 * in an ArrayList of Strings
	 * 
	 */
	public ArrayList<String> getAppointmentDetails(ArrayList<?> idList){		
		String time = null;
		String name = null;
		ArrayList<String> info = new ArrayList<String>();
		JSONObject json;
		
		for(int i = 0; i < idList.size(); i++ ){
			try {
				json = new JSONObject(idHash.get(idList.get(i)));
				time = (json.get("time").toString());
				name = (((JSONObject) json.get("service_user")).get("name").toString());
				info.add(time + " - - - - " + name);
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}
		return info;
	}
	public String getAppointmentDetails(String id){		
		String time = null;
		String name = null;
		String info = null;
		JSONObject json;
		try {
			json = new JSONObject(idHash.get(id));
			time = json.get("time").toString();	
			name = (((JSONObject) json.get("service_user")).get("name").toString());
			info = (time + " - - - - " + name);	
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return info;
	}
	public String getClinicID(String id){
		String clinicID = null;
		JSONObject json;
		try {
			json = new JSONObject(idHash.get(id));
			clinicID = json.get("clinic_id").toString();
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return clinicID;
	}
	public String getServiceProvderrId() {
		
		return serviceProvderId;
	}
	public String getServiceUserID(String id) {
		String serivceUserID = null;
		JSONObject json;
		try {
			json = new JSONObject(idHash.get(id));
			serivceUserID = json.get("service_user_id").toString();
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return serivceUserID;
	}
	public Boolean getPriority() {
		return priority;
	}
	public String getVistType() {
		return vistType;
	}	
	public String getServiceOptionId() {
		return serviceOptionId;
	}
	public void setId(String newId) {
		this.id = newId;
	}	
	public void setserviceProvderId(String newServiceProvderId) {
		this.serviceProvderId = newServiceProvderId;
	}
	public void setServiceUserId(String serviceUserId) {
		this.serviceUserId = serviceUserId;
	}
	public void setPriority(boolean Priority) {
		this.priority = Priority;
	}
	public void setVistType(String VistType) {
		this.vistType = VistType;
	}
	public void setServiceOptionId(String ServiceOptionId) {
		this.serviceOptionId = ServiceOptionId;
	}
	/**
	 * 
	 * takes in an ArrayLis of JSONObject and returns
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
}