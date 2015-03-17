package utility;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;

public class ServiceOptionSingleton {
	private static ServiceOptionSingleton singleInstance;
	private JsonParseHelper help = new JsonParseHelper();
	private Map<String, JSONObject> idMap;
	private List<JSONObject> jsonValues;
	
	private ServiceOptionSingleton() {
	}	
	
	public static synchronized ServiceOptionSingleton getInstance() {
		if(singleInstance == null) {
			singleInstance = new ServiceOptionSingleton();
		}
		return singleInstance;
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