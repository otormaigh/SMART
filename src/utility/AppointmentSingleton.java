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
 *
 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
 */
 

public class AppointmentSingleton {
	private static AppointmentSingleton singleInstance;
	private HashMap<String, HashMap<String, ArrayList<String>>> clinicIDHash;
	private HashMap<String, String> idHash;
	private ArrayList<String> idList;
	private AccessDBTable db = new AccessDBTable();
	private String response;
	private JSONArray query;
	private JSONObject jsonNew;
	
	private AppointmentSingleton() {
	}
	
	public static synchronized AppointmentSingleton getInstance() {
		if(singleInstance == null) {
			singleInstance = new AppointmentSingleton();
		}
		return singleInstance;
	}
	
	public void updateLocal(){
		new LongOperation().execute("appointments");
	}
	
	//Get full appointment table from database
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
				id = String.valueOf((jsonValues.get(i).getInt("id")));
				date = (String) jsonValues.get(i).get("date");
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
	
	public ArrayList<String> getClinicID(ArrayList<?> idList) { // get the specific id not list of???
		ArrayList<String> clinicID = new ArrayList<String>();
		JSONObject json;
		
		for(int i = 0; i < idList.size(); i++ ){
			try {
				json = new JSONObject(idHash.get(idList.get(i)));
				clinicID.add(json.get("clinic_id").toString());				
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}
		return clinicID;
	}
	
	public String getClinicID(String id){
		String clinicID = new String();
		JSONObject json;
		try {
			json = new JSONObject(idHash.get(id));
			clinicID = json.get("clinic_id").toString();
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return clinicID;
	}
	
	public ArrayList<String> getDate(ArrayList<String> idList) {
		ArrayList<String> date = new ArrayList<String>();
		JSONObject json;
		
		for(int i = 0; i < idList.size(); i++ ){
			try {
				json = new JSONObject(idHash.get(idList.get(i)));
				date.add(json.get("date").toString());				
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}
		return date;
	}
	
	public String getDate(String id) {
		String date = new String();
		JSONObject json;
		try {
			json = new JSONObject(idHash.get(id));
			date = json.get("date").toString();				
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return date;
	}
	
	public ArrayList<String> getAppointmentID(ArrayList<String> idList) {
		ArrayList<String> apptID = new ArrayList<String>();
		JSONObject json;
		
		for(int i = 0; i < idList.size(); i++ ){
			try {
				json = new JSONObject(idHash.get(idList.get(i)));
				apptID.add(json.get("id").toString());				
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}
		return apptID;
	}
	
	public String getAppointmentID(String id) {
		String apptID = new String();
		JSONObject json;
		try {
			json = new JSONObject(idHash.get(id));
			apptID = json.get("id").toString();				
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return apptID;
	}
	
	/**TODO getter method for links	  
	  "links": {
            "service_options": "/appointments/64/service_options", 
            "service_provider": "service_providers/14", 
            "service_user": "service_users/1"
        },
	*/
	
	public ArrayList<String> getPriority(ArrayList<String> idList) {
		ArrayList<String> priority = new ArrayList<String>();
		JSONObject json;
		
		for(int i = 0; i < idList.size(); i++ ){
			try {
				json = new JSONObject(idHash.get(idList.get(i)));
				priority.add(json.get("priority").toString());				
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}
		return priority;
	}
	
	public String getPriority(String id) {
		String priority = new String();
		JSONObject json;
		try {
			json = new JSONObject(idHash.get(id));
			priority = json.get("priority").toString();				
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return priority;
	}
	
	public ArrayList<String> getServiceOptionID(ArrayList<String> idList) {
		ArrayList<String> serviceOptionID = new ArrayList<String>();
		JSONObject json;
		
		for(int i = 0; i < idList.size(); i++ ){
			try {
				json = new JSONObject(idHash.get(idList.get(i)));
				serviceOptionID.add(json.get("service_option_ids").toString());				
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}
		return serviceOptionID;
	}
	
	public String getServiceOptionID(String id) {
		String serviceOptionID = new String();
		JSONObject json;
		try {
			json = new JSONObject(idHash.get(id));
			serviceOptionID = json.get("service_option_ids").toString();				
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return serviceOptionID;
	}	
	
	public ArrayList<String> getServiceProviderID(ArrayList<String> idList) {
		ArrayList<String> serviceProviderID = new ArrayList<String>();
		JSONObject json;
		
		for(int i = 0; i < idList.size(); i++ ){
			try {
				json = new JSONObject(idHash.get(idList.get(i)));
				serviceProviderID.add(json.get("service_provider_id").toString());				
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}
		return serviceProviderID;
	}
	
	public String getServiceProviderID(String id) {
		String serviceProviderID = new String();
		JSONObject json;
		try {
			json = new JSONObject(idHash.get(id));
			serviceProviderID = json.get("service_provider_id").toString();				
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return serviceProviderID;
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
	
	public String getGestation(String id){		
		String gest = new String();
		JSONObject json;
		try {
			json = new JSONObject(idHash.get(id));
			gest = ((JSONObject) json.get("service_user")).get("gestation").toString();				
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return gest;
	}
	
	public ArrayList<String> getName(ArrayList<String> idList){		
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
		String name = new String();
		JSONObject json;
		try {
			json = new JSONObject(idHash.get(id));
			name = ((JSONObject) json.get("service_user")).get("name").toString();				
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return name;
	}
	
	public ArrayList<String> getServiceUserID(ArrayList<String> idList) {
		ArrayList<String> serviceUserID = new ArrayList<String>();
		JSONObject json;
		
		for(int i = 0; i < idList.size(); i++ ){
			try {
				json = new JSONObject(idHash.get(idList.get(i)));
				serviceUserID.add(json.get("service_user_id").toString());				
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}
		return serviceUserID;
	}
	
	public String getServiceUserID(String id) {
		String serviceUserID = new String();
		JSONObject json;
		try {
			json = new JSONObject(idHash.get(id));
			serviceUserID = json.get("service_user_id").toString();				
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return serviceUserID;
	}
	
	public ArrayList<String> getTime(ArrayList<?> idList) {
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
		String time = new String();
		JSONObject json;
		try {
			json = new JSONObject(idHash.get(id));
			time = json.get("time").toString();				
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return time;
	}
	
	public ArrayList<String> getVisitLogs(ArrayList<?> idList) {
		ArrayList<String> visitLogs = new ArrayList<String>();
		JSONObject json;
		
		for(int i = 0; i < idList.size(); i++ ){
			try {
				json = new JSONObject(idHash.get(idList.get(i)));
				visitLogs.add(json.get("visit_logs").toString());				
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}
		return visitLogs;
	}
	
	public String getVisitLogs(String id){		
		String visitLogs = new String();
		JSONObject json;
		try {
			json = new JSONObject(idHash.get(id));
			visitLogs = json.get("visit_logs").toString();				
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return visitLogs;
	}
	
	public ArrayList<String> getVisitType(ArrayList<?> idList) {
		ArrayList<String> visitType = new ArrayList<String>();
		JSONObject json;
		
		for(int i = 0; i < idList.size(); i++ ){
			try {
				json = new JSONObject(idHash.get(idList.get(i)));
				visitType.add(json.get("visit_type").toString());				
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}
		return visitType;
	}
	
	public String getVisitType(String id){		
		String visitType = new String();
		JSONObject json;
		try {
			json = new JSONObject(idHash.get(id));
			visitType = json.get("visit_type").toString();				
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return visitType;
	}
}