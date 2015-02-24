package models;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import connecttodb.AccessDBTable;

public class Appointments_model {
	private static Appointments_model singleInstance;
	private JSONArray appointmentArray = new JSONArray();
	private HashMap<String, ArrayList<String>> dateHash = new HashMap<String, ArrayList<String>>();
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
		
	private Appointments_model() {
	}
	
	public static Appointments_model getSingletonIntance() {
		if(singleInstance == null) {
			singleInstance = new Appointments_model();
		}
		return singleInstance;
	}
	public void updateLocal(){		
		try {
			response = db.accessDB(Login_model.getToken(), "appointments");
			jsonNew = new JSONObject(response);
			query = jsonNew.getJSONArray("appointments");
		} catch (JSONException e) {
			e.printStackTrace();
		}
		setAppointmentArrayToHashMap(query);
	}
	public JSONArray getAppointmentArray() {
		return appointmentArray;
	}
	public void setAppointmentArray(JSONArray appointmentArray) {
		this.appointmentArray = appointmentArray;
	}
	public HashMap<String, ArrayList<String>> getAppointmentArrayToHashMap(){
		return dateHash;
	}
	public void setAppointmentArrayToHashMap(JSONArray appointmentArray) {
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
	public String getId() {
		return id;
	}
	public String getDate() {
		return date;
	}
	public String getTime() {
		return time;
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