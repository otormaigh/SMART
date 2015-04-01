package ie.teamchile.smartapp.activities;

/**
 * Today's Appointments for Service Provider
 * Barry Dempsey 10.03.15
 */

import ie.teamchile.smartapp.connecttodb.AccessDBTable;
import ie.teamchile.smartapp.utility.AppointmentSingleton;
import ie.teamchile.smartapp.utility.MyAdapter;
import ie.teamchile.smartapp.utility.ServiceUserSingleton;
import ie.teamchile.smartapp.utility.ToastAlert;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

import org.json.JSONObject;

import android.app.ListActivity;
import android.os.AsyncTask;
import android.os.Bundle;


public class TodayAppointmentActivity extends ListActivity   {
	private ArrayList<String>values;
	private ArrayList<String>addresses = new ArrayList<String>();
	private Calendar cal;
	private String address;
	private String date;
	

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		new LongOperation().execute("service_users/2");
		cal = Calendar.getInstance();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.UK);
		date = sdf.format(cal.getTime());
		values = new ArrayList<String>();
	}
	
	private class LongOperation extends AsyncTask<String, Void, JSONObject> {
			
		@Override
		protected JSONObject doInBackground(String... params) {
			AccessDBTable access = new AccessDBTable();
			JSONObject json = access.accessDB(params[0]);
			return json;
		}

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			ToastAlert ta = new ToastAlert(TodayAppointmentActivity.this, "Processing. . . ", false);
		}

		@Override
		protected void onPostExecute(JSONObject result) {
			super.onPostExecute(result);
			ServiceUserSingleton.getInstance().setPatientInfo(result);
			address = ServiceUserSingleton.getInstance().getUserHomeAddress().get(0);
			values = AppointmentSingleton.getInstance().getListOfIDs("3", "2014-12-24");
			
			if(values == null || values.size() == 0) {
				values = new ArrayList<String>();
				values.add("No appointments today");
			}
			MyAdapter adapter = new MyAdapter(TodayAppointmentActivity.this, values);
			setListAdapter(adapter);
		}

		@Override
		protected void onProgressUpdate(Void... values) {
			super.onProgressUpdate(values);
		}			
	}
}