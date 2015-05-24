package ie.teamchile.smartapp.activities;

import ie.teamchile.smartapp.R;
import ie.teamchile.smartapp.connecttodb.AccessDBTable;
import ie.teamchile.smartapp.utility.AppointmentSingleton;
import ie.teamchile.smartapp.utility.ClinicSingleton;
import ie.teamchile.smartapp.utility.ServiceOptionSingleton;
import ie.teamchile.smartapp.utility.ServiceProviderSingleton;
import ie.teamchile.smartapp.utility.ToastAlert;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.ProgressDialog;
import android.content.ComponentCallbacks2;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;

public class QuickMenuActivity extends MenuInheritActivity {
    private boolean isViewVisible;
    private ProgressDialog pd;
    private JSONObject json;
	private JSONArray query;
	private AccessDBTable db = new AccessDBTable();	
	private Button patientSearch, bookAppointment, calendar, todaysAppointments;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
		setContentForNav(R.layout.activity_quick_menu);

        patientSearch = (Button) findViewById(R.id.patientSearch);
        patientSearch.setOnClickListener(new ButtonClick());
        bookAppointment = (Button) findViewById(R.id.bookAppointment);
        bookAppointment.setOnClickListener(new ButtonClick());
        calendar = (Button) findViewById(R.id.calendar);
        calendar.setOnClickListener(new ButtonClick());
        todaysAppointments = (Button) findViewById(R.id.todays_appointments);
        //todaysAppointments.setOnClickListener(new ButtonClick());
        
        if(!AppointmentSingleton.getInstance().getUpdated()){
        	new updateLocal().execute("appointments", "clinics", "service_options");        	
        }        
        isViewVisible = true;
    }
    
	@Override
	protected void onNewIntent(Intent intent) {
	    super.onNewIntent(intent);
	    setIntent(intent);
	}
	
    private class ButtonClick implements View.OnClickListener {
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.patientSearch:
                    Intent intentPatient = new Intent(QuickMenuActivity.this, ServiceUserSearchActivity.class);
                    startActivity(intentPatient);
                    break;
                case R.id.bookAppointment:
                    Intent intentBook = new Intent(QuickMenuActivity.this, AppointmentTypeSpinnerActivity.class);
                    startActivity(intentBook);
                    break;
                case R.id.calendar:
                    Intent intentCalendar = new Intent(QuickMenuActivity.this, CalendarActivity.class);
                    startActivity(intentCalendar);
                    break;
                /*case R.id.todays_appointments:
                    Intent intentToday = new Intent(QuickMenuActivity.this, TodayAppointmentActivity.class);
                    startActivity(intentToday);
                    break;*/
            }
        }
    }
    
    @Override
    public void onBackPressed() {
    	if(ServiceProviderSingleton.getInstance().isLoggedIn()) {
    		new ToastAlert(getBaseContext(), 
        			"Already logged in, \n  Logout?", false);
    	} else { }
    }

	@Override
	protected void onDestroy() {
		super.onDestroy();
		Log.d("Credentials", "User: " + ServiceProviderSingleton.getInstance().getUsername() + 
			  "\nPass: " + ServiceProviderSingleton.getInstance().getPassword());
	}
	
	@Override
	protected void onResume() {
		super.onResume();
		isViewVisible = true;		
		SharedPreferences.Editor prefs = getSharedPreferences("SMART", MODE_PRIVATE).edit();	
    	prefs.putBoolean("reuse", false);
		prefs.commit();
	}

	@Override
	protected void onPause() { super.onPause(); }

	@Override
	protected void onStop() { super.onStop(); }

	@Override
	public void onTrimMemory(int level) {
		super.onTrimMemory(level);
		if(level >= ComponentCallbacks2.TRIM_MEMORY_UI_HIDDEN) {
			isViewVisible = false;
			new ToastAlert(getBaseContext(), "View is now hidden", false);				
		}else 
			isViewVisible = true;
	}
	
	private class updateLocal extends AsyncTask<String, Void, JSONArray> {		
		@Override
		protected void onPreExecute() {
			pd = new ProgressDialog(QuickMenuActivity.this);
			pd.setMessage("Updating Information");
			pd.setCanceledOnTouchOutside(false);
			pd.setCancelable(false);
			pd.show();
		}
		protected JSONArray doInBackground(String... params) {
			try {
				json = db.accessDB(params[0]);
				query = json.getJSONArray(params[0]);
				AppointmentSingleton.getInstance().setHashMapofClinicDateID(query);
				AppointmentSingleton.getInstance().setHashMapofIdAppt(query);
				
				json = db.accessDB(params[1]);
				query = json.getJSONArray(params[1]);
				ClinicSingleton.getInstance().setHashMapofIdClinic(query);
				
				json = db.accessDB(params[2]);
				query = json.getJSONArray(params[2]);
				ServiceOptionSingleton.getInstance().setMapOfID(query);
			} catch (JSONException e) {
				e.printStackTrace();
			}
			return query;
		}
		@Override
		protected void onProgressUpdate(Void... values) {
		}
		@Override
        protected void onPostExecute(JSONArray result) {
			pd.dismiss();
        }
	}
}
