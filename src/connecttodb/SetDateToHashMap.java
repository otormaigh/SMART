package connecttodb;

import java.util.ArrayList;
import java.util.HashMap;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class SetDateToHashMap {
	private ArrayList<JSONObject> jsonValues = new ArrayList<JSONObject>();
	private ArrayList<JSONObject> aptsAtDate = new ArrayList<JSONObject>();
	private ArrayList<String> idArray;
	private HashMap<String, ArrayList<String>> dateHash = new HashMap<String, ArrayList<String>>();
	private JSONObject jsonNew;
	private AccessDBTable db;
	private String response, id, date, token, dateToBeSearched;
	private String tableURL = "appointments/";
	
	public ArrayList<JSONObject> setDateToHaspMap(String token, String dateToBeSearched){
		this.token = token;
		this.dateToBeSearched = dateToBeSearched;
		return searchForDate();
	}
	private ArrayList<JSONObject> searchForDate() {
		db = new AccessDBTable();
		response = db.accessDB(token, "appointments");
		try {
			jsonNew = new JSONObject(response);
			JSONArray query = jsonNew.getJSONArray("appointments");
			for (int i = 0; i < query.length(); i++)
				jsonValues.add(query.getJSONObject(i));
			
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
		return getDateForID(dateHash.get(dateToBeSearched));
	}
	private ArrayList<JSONObject> getDateForID(ArrayList<String> listOfIDs){
		//String aptsAtDate = "";
		for(int i = 0; i < listOfIDs.size(); i++){	
			JSONObject aptAsJson;
			try {
				aptAsJson = new JSONObject(db.accessDB(token, (tableURL + listOfIDs.get(i))));
				aptsAtDate.add(aptAsJson);
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}
		return aptsAtDate;		
	}
}