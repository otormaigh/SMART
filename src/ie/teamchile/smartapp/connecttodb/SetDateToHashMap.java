package ie.teamchile.smartapp.connecttodb;

import ie.teamchile.smartapp.utility.AppointmentSingleton;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;

public class SetDateToHashMap {
	private ArrayList<JSONObject> jsonValues = new ArrayList<JSONObject>();
	private ArrayList<JSONObject> aptsAtDate = new ArrayList<JSONObject>();
	private ArrayList<String> idArray;
	private HashMap<String, ArrayList<String>> dateHash = new HashMap<String, ArrayList<String>>();
	private JSONObject json;
	private AccessDBTable db;
	private String response, id, date, token, dateToBeSearched;
	private String tableURL = "appointments/";
	
	public ArrayList<JSONObject> setDateToHaspMap(String dateToBeSearched){
		this.dateToBeSearched = dateToBeSearched;
		return searchForDate();
	}
	private ArrayList<JSONObject> searchForDate() {
		db = new AccessDBTable();
		json = db.accessDB("appointments");
		try {
			JSONArray query = json.getJSONArray("appointments");
			for (int i = 0; i < query.length(); i++)
				jsonValues.add(query.getJSONObject(i));
			    jsonValues = sortDates(jsonValues);
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
		for(int i = 0; i < listOfIDs.size(); i++){
			JSONObject aptAsJson;
			aptAsJson = db.accessDB(tableURL + listOfIDs.get(i));
			aptsAtDate.add(aptAsJson);
		}
		return aptsAtDate;
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