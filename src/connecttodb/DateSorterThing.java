package connecttodb;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;

public class DateSorterThing {
	String inDate = null;
	Date outDate = null;
	ArrayList<Date> dates = new ArrayList<Date>();
	ArrayList<JSONObject> jsonValues = new ArrayList<JSONObject>();
	ArrayList<JSONObject> appointmentsThatDay = new ArrayList<JSONObject>();
	ArrayList<JSONObject> objSorted = new ArrayList<JSONObject>();
	DateFormat df = new SimpleDateFormat("yyyy-MM-dd - HH:mm:ss");
	DateFormat dfDateOnly = new SimpleDateFormat("yyyy-MM-dd");
	JSONObject jsonNew;
	Date dateAsDate, timeAsDate, nowDate, dbDate, queryDate;
	Calendar c = Calendar.getInstance();
	Date week1 = (nowDate);

	public DateSorterThing() {		
	}
	public ArrayList<JSONObject> dateSorter(Date queryDate) {
		Log.d("MYLOG", "queryDate: " + queryDate);
		AccessDBTable accessDB = new AccessDBTable();
		String response = accessDB.accessDB("0c325638d97faf29d71f", "appointments");

		// put the response at a JSONObject
		try {
			jsonNew = new JSONObject(response);
			JSONArray query = jsonNew.getJSONArray("appointments");
			for (int i = 0; i < query.length(); i++)
				jsonValues.add(query.getJSONObject(i));
		} catch (JSONException e) {
			e.printStackTrace();
		}
		Log.d("MYLOG", "JSON VALUES");
		for (int i = 0; i < jsonValues.size(); i++) {
			try {
				Date jsonDate = df.parse((((JSONObject) jsonValues.get(i)).get("date")) + " - " + (((JSONObject) jsonValues.get(i)).get("time")));
				Log.d("MYLOG", "jsonDate is : " + jsonDate);
			} catch (ParseException | JSONException e) {
				e.printStackTrace();
			}
		}
		return getDates(jsonValues, queryDate);
	}
	public ArrayList<JSONObject> getDates(ArrayList<JSONObject> obj, Date dateToBeChecked) {
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
					Log.d("MYLOG", "JSONException in combine JSONArrays sort section " + e);
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