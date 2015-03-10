package utility;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class JsonParseHelper {
	private static final String APPOINTMENTS = "appointments";
	private static final String ID = "id";
	private static final String SERVICE_USER = "service_user";
	private static final String SERVICE_USER_GESTATION = "gestation";
	private static final String SERVICE_USER_ID = "service_user_id";
	private static final String NAME = "name";
	private static final String CLINICS = "clinics";
	private static final String BABIES = "babies";	
	private static final String PREGNANCIES = "pregnancies";	
	private static final String SERVICE_USERS = "service_users";
	private static final String CLINICAL_FIELDS = "clinical_fields";
	private static final String PERSONAL_FIELDS = "personal_fields";
	private JSONObject jsonThing;
	
	public JsonParseHelper() {		
	}
	
	public String jsonParseHelper(JSONObject json, String tableName, String tableKey) {
		try {
			switch (tableName) {
			case APPOINTMENTS:
				switch (tableKey) {
				case SERVICE_USER_GESTATION:
					return ((JSONObject) json.get(SERVICE_USER)).get(SERVICE_USER_GESTATION).toString();
				case SERVICE_USER_ID: 
					return ((JSONObject) json.get(SERVICE_USER)).get(ID).toString();
				case NAME:
					return ((JSONObject) json.get(SERVICE_USER)).get(NAME).toString();
				default:
					return json.get(tableKey).toString();
				}
			case CLINICS:
				return json.get(tableKey).toString();
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return null;
	}

	public List<String> jsonParseHelper(JSONObject json, String tableName, String subTable, String tableKey) {
		List<String> returnedList = new ArrayList<String>();
		try {
			switch (tableName) {
			case SERVICE_USERS:
				switch (subTable) {
				case BABIES:
					JSONArray jArrayBaby = json.getJSONArray(BABIES);
					for(int i = 0; i < jArrayBaby.length(); i++){
						returnedList.add(((JSONObject) jArrayBaby.get(i)).get(tableKey).toString());
					}
					return returnedList;
				case PREGNANCIES:
					JSONArray jArrayPreg = json.getJSONArray(PREGNANCIES);
					for(int i = 0; i < jArrayPreg.length(); i++){
						returnedList.add(((JSONObject) jArrayPreg.get(i)).get(tableKey).toString());
					}
					return returnedList;
				}
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return null;
	}
	public List<String> jsonParseHelper(JSONObject json, String tableName, String subTable, String subSubTable, String tableKey) {
		List<String> returnedList = new ArrayList<String>();
		try {
			switch (tableName) {
			case SERVICE_USERS:
				JSONArray jArrayUser = json.getJSONArray(SERVICE_USERS);
				for(int i = 0; i < jArrayUser.length(); i++){
					switch (subTable) {
					case SERVICE_USERS:
						switch(subSubTable){
						case CLINICAL_FIELDS:
							returnedList.add(((JSONObject) jArrayUser.get(i)).getJSONObject(CLINICAL_FIELDS).get(tableKey).toString());
							break;
						case PERSONAL_FIELDS:
							returnedList.add(((JSONObject) jArrayUser.get(i)).getJSONObject(PERSONAL_FIELDS).get(tableKey).toString());
							break;
						}
					}
				}
				return returnedList;
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return null;
	}	
}