package connecttodb;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.Locale;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;

public class DateSorter {
	private ArrayList<JSONObject> jsonValues = new ArrayList<JSONObject>();
	private ArrayList<JSONObject> appointmentsThatDay = new ArrayList<JSONObject>();
	private DateFormat df = new SimpleDateFormat("yyyy-MM-dd - HH:mm:ss", Locale.getDefault());
	private DateFormat dfDateOnly = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
	private JSONObject json;
	private JSONArray query;
	private Date dbDate, queryDate, jsonDate;
	private String valA, valB;
	private int comp;

	public  ArrayList<JSONObject> dateSorter(Date queryDate) {	
		return dateSorter();
	}
	private ArrayList<JSONObject> dateSorter() {
		Log.d("MYLOG", "queryDate: " + queryDate);
		AccessDBTable accessDB = new AccessDBTable();
		json = accessDB.accessDB("appointments");

		// put the response at a JSONObject
		try {
			query = json.getJSONArray("appointments");
			for (int i = 0; i < query.length(); i++)
				jsonValues.add(query.getJSONObject(i));
		} catch (JSONException e) {
			e.printStackTrace();
		}
		Log.d("MYLOG", "JSON VALUES");
		for (int i = 0; i < jsonValues.size(); i++) {
			try {
				jsonDate = df.parse((((JSONObject) jsonValues.get(i)).get("date")) + " - " + (((JSONObject) jsonValues.get(i)).get("time")));
				Log.d("MYLOG", "jsonDate is : " + jsonDate);
			} catch (ParseException | JSONException e) {
				e.printStackTrace();
			}
		}
		return getDates(jsonValues, queryDate);
	}
	private ArrayList<JSONObject> getDates(ArrayList<JSONObject> obj, Date dateToBeChecked) {
		try {
			for (int i = 0; i < obj.size(); i++) {
				dbDate = df.parse((((JSONObject) jsonValues.get(i)).get("date")) + " - " + (((JSONObject) jsonValues.get(i)).get("time")));
				Log.d("MYLOG", "dbDate is : " + dbDate);
				if ((dfDateOnly.format(dbDate)).equals(dfDateOnly.format(dateToBeChecked))){
					appointmentsThatDay.add(obj.get(i));
				}
			}
		} catch (JSONException | ParseException e) {
			e.printStackTrace();
		}
		return sortDates(appointmentsThatDay);
	}
	private ArrayList<JSONObject> sortDates(ArrayList<JSONObject> objToBeSorted) {
		Collections.sort(objToBeSorted, new Comparator<JSONObject>() {
			@Override
			public int compare(JSONObject a, JSONObject b) {
				try {
					valA = (String) a.get("date") + " " + a.get("time");
					valB = (String) b.get("date") + " " + b.get("time");
				} catch (JSONException e) {
					Log.d("MYLOG", "JSONException in combine JSONArrays sort section " + e);
				}
				comp = valA.compareTo(valB);

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