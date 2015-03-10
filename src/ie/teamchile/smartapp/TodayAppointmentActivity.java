package ie.teamchile.smartapp;

/**
 * Today's Appointments for Service Provider
 * Barry Dempsey 10.03.15
 */

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

import org.json.JSONException;
import org.json.JSONObject;

import utility.AppointmentSingleton;
import utility.MyAdapter;
import utility.ServiceUserSingleton;
import utility.ToastAlert;
import android.app.ListActivity;
import android.os.AsyncTask;
import android.os.Bundle;
import connecttodb.AccessDBTable;


public class TodayAppointmentActivity extends ListActivity {
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
				JSONObject json = null;
				try {
					json = new JSONObject(access.accessDB(params[0]));
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				return json;
			}
	
			@Override
			protected void onPreExecute() {
				// TODO Auto-generated method stub
				super.onPreExecute();
				ToastAlert ta = new ToastAlert(TodayAppointmentActivity.this, "Processing. . . ", false);
			}
	
			@Override
			protected void onPostExecute(JSONObject result) {
				// TODO Auto-generated method stub
				super.onPostExecute(result);
				ServiceUserSingleton.getInstance().setPatientInfo(result);
				address = ServiceUserSingleton.getInstance().getAddress();

				if(values != null)
					values = AppointmentSingleton.getInstance().getListOfIDs("2", date);
				MyAdapter adapter = new MyAdapter(TodayAppointmentActivity.this, values);
				setListAdapter(adapter);
			}
	
			@Override
			protected void onProgressUpdate(Void... values) {
				// TODO Auto-generated method stub
				super.onProgressUpdate(values);
			}			
		}
}