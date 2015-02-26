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
	private HashMap<String, String> idHash = new HashMap<String, String>();
	private ArrayList<String> idList;
	private AccessDBTable db = new AccessDBTable();
	private String response;
	private JSONArray query;
	private JSONObject jsonNew;
	
	private String id;
	private String date;
	private String time;
	private String serviceProvderId;
	private String serviceUserId;
	private boolean priority;
	private String vistType;
	private String serviceOptionId;	
		
	private AppointmentSingleton() {
	}
	
	public static AppointmentSingleton getSingletonIntance() {
		if(singleInstance == null) {
			singleInstance = new AppointmentSingleton();
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
				response = db.accessDB("appointments");
				jsonNew = new JSONObject(response);
				query = jsonNew.getJSONArray("appointments");
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
			setHashMapofDateID(result);
			setHashMapofIdAppt(result);
        }
	}
	public JSONArray getAppointmentArray() {
		return appointmentArray;
	}
	public void setAppointmentArray(JSONArray appointmentArray) {
		this.appointmentArray = appointmentArray;
	}
	public HashMap<String, ArrayList<String>> getHashMapofDateID(){
		return dateHash;				//return Hashmap of Date as Key, ID as Value
	}
	public ArrayList<String> getIdAtDate(String query){
		return dateHash.get(query);		// returns id at date 
	}
	public void setHashMapofDateID(JSONArray appointmentArray) {
		ArrayList<JSONObject> jsonValues = new ArrayList<JSONObject>();
		ArrayList<String> idArray;
		HashMap<String, ArrayList<String>> dateHash = new HashMap<String, ArrayList<String>>();
		
		try {
			for (int i = 0; i < appointmentArray.length(); i++) {
				jsonValues.add(appointmentArray.getJSONObject(i));
				jsonValues = sortDates(jsonValues);
			}	
			for (int i = 0; i < jsonValues.size(); i++) {
				idArray = new ArrayList<String>();
				id = String.valueOf((jsonValues.get(i).getInt("id")));
				date = (String) jsonValues.get(i).get("date");
				if (dateHash.get(date) != null) {
					idArray = dateHash.get(date);
					idArray.add(id);
					dateHash.put(date, idArray);
				} else {
					idArray.add(id);
					dateHash.put(date, idArray);
				}
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
		this.dateHash = dateHash;
	}
	public HashMap<String, String> getHashMapofIdAppt(){
		return idHash;
	}
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
	public ArrayList<String> getIds(String dayToSearch) {
		idList = new ArrayList<String>();
		idList = dateHash.get(dayToSearch);
		return idList;
	}
	public String getDate() {		
		return date;
	}
	public ArrayList<String> getTime(ArrayList<?> idList) { // get the specific id not list of. . 
		ArrayList<String> time = new ArrayList<String>();
		JSONObject jsondude;
		
		for(int i = 0; i < idList.size(); i++ ){
			try {
				jsondude = new JSONObject(idHash.get(idList.get(i)));
				time.add(jsondude.get("time").toString());				
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}
		return time;
	}
	public ArrayList<String> getName(ArrayList<?> idList){		
		ArrayList<String> name = new ArrayList<String>();
		JSONObject jsondude;
		
		for(int i = 0; i < idList.size(); i++ ){
			try {
				jsondude = new JSONObject(idHash.get(idList.get(i)));
				name.add(((JSONObject) jsondude.get("service_user")).get("name").toString());				
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}
		return name;
	}
	public ArrayList<String> getGestation(ArrayList<?> idList){		
		ArrayList<String> gest = new ArrayList<String>();
		JSONObject jsondude;
		
		for(int i = 0; i < idList.size(); i++ ){
			try {
				jsondude = new JSONObject(idHash.get(idList.get(i)));
				gest.add(((JSONObject) jsondude.get("service_user")).get("gestation").toString());				
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}
		return gest;
	}
	public ArrayList<String> getAppointmentDetails(ArrayList<?> idList){		
		String time = null;
		String name = null;
		ArrayList<String> info = new ArrayList<String>();
		JSONObject jsondude;
		
		for(int i = 0; i < idList.size(); i++ ){
			try {
				jsondude = new JSONObject(idHash.get(idList.get(i)));
				time = (jsondude.get("time").toString());
				name = (((JSONObject) jsondude.get("service_user")).get("name").toString());
				info.add(time + " - - - - " + name);
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}
		return info;
	}
	public String getServiceProvderrId() {
		return serviceProvderId;
	}
	public String getServiceUserId() {
		return serviceUserId;
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
	public void setDate(String newDate) {
		this.date = newDate;
	}
	public void settime(String newTime) {
		this.time = newTime;
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