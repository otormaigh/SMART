package ie.teamchile.smartapp;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import connecttodb.AccessDBTable;
import utility.AppointmentSingleton;
import utility.ClinicSingleton;
import utility.ServiceOptionSingleton;
import utility.ToastAlert;
import utility.ServiceProviderSingleton;
import android.app.ProgressDialog;
import android.app.admin.DevicePolicyManager;
import android.content.ComponentCallbacks2;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

public class QuickMenuActivity extends MenuInheritActivity {
    private Button patientInfo;
    private Button bookAppointment;
    private Button calendar;
    private Button todaysAppointments;
    private boolean isViewVisible = false;
    private DevicePolicyManager deviceManager;
    private ProgressDialog pd;
    private JSONObject json;
	private JSONArray query;
	private AccessDBTable db = new AccessDBTable();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quick_menu);
        
        //if(AppointmentSingleton.getInstance() != null){
        	new updateLocal().execute("appointments", "clinics", "service_options");        	
       // }
        
        patientInfo = (Button) findViewById(R.id.patientInfo);
        patientInfo.setOnClickListener(new ButtonClick());
        bookAppointment = (Button) findViewById(R.id.bookAppointment);
        bookAppointment.setOnClickListener(new ButtonClick());
        calendar = (Button) findViewById(R.id.calendar);
        calendar.setOnClickListener(new ButtonClick());
        todaysAppointments = (Button) findViewById(R.id.todays_appointments);
        todaysAppointments.setOnClickListener(new ButtonClick());
        isViewVisible = true;
    }
    
    private class ButtonClick implements View.OnClickListener {
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.patientInfo:
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
                case R.id.todays_appointments:
                    Intent intentToday = new Intent(QuickMenuActivity.this, TodayAppointmentActivity.class);
                    startActivity(intentToday);
                    break;
            }
        }
    }
    
    @Override
    public void onBackPressed() {
    	if(ServiceProviderSingleton.getInstance().isLoggedIn()) {
    		ToastAlert ta = new ToastAlert(getBaseContext(), 
        			"  Already logged in, \n  Logout?", false);
    	}else {
    		
    	}    	
    }

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		System.out.println(ServiceProviderSingleton.getInstance().getUsername() + " " +
				ServiceProviderSingleton.getInstance().getPassword());
	}
	
	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		isViewVisible = true;		
		SharedPreferences.Editor prefs = getSharedPreferences("SMART", MODE_PRIVATE).edit();	
    	prefs.putBoolean("reuse", false);
		prefs.commit();
	}

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		//checkIsInBackground();
	}

	@Override
	protected void onStop() {
		// TODO Auto-generated method stub
		super.onStop();
	}

	@Override
	public void onTrimMemory(int level) {
		// TODO Auto-generated method stub
		super.onTrimMemory(level);
		if(level >= ComponentCallbacks2.TRIM_MEMORY_UI_HIDDEN) {
			isViewVisible = false;
			ToastAlert ta = new ToastAlert(getBaseContext(), "View is now hidden", false);				
		}
	}
	
	private class updateLocal extends AsyncTask<String, Void, JSONArray> {		
		@Override
		protected void onPreExecute() {
			pd = new ProgressDialog(QuickMenuActivity.this);
			pd.setMessage("Updating Information");
			pd.show();
		}
		protected JSONArray doInBackground(String... params) {
			Log.d("singleton", "in service options updateLocal doInBackground");
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
