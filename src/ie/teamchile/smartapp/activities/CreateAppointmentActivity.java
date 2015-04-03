package ie.teamchile.smartapp.activities;

import ie.teamchile.smartapp.R;
import ie.teamchile.smartapp.connecttodb.AccessDBTable;
import ie.teamchile.smartapp.connecttodb.PostAppointment;
import ie.teamchile.smartapp.utility.AppointmentSingleton;
import ie.teamchile.smartapp.utility.ClinicSingleton;
import ie.teamchile.smartapp.utility.MyAdapter;
import ie.teamchile.smartapp.utility.ServiceUserSingleton;
import ie.teamchile.smartapp.utility.ToastAlert;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

public class CreateAppointmentActivity extends MenuInheritActivity {
	private SimpleDateFormat sdfTime = new SimpleDateFormat("HH:mm", Locale.getDefault());
	private SimpleDateFormat sdfDate = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
	private SimpleDateFormat sdfDateMonthName = new SimpleDateFormat("dd MMM yyyy", Locale.getDefault());
	private EditText userName;
	private TextView textDate, textClinic, textVisitType;
	private Button confirmAppointment, btnUserSearch;
	private Spinner visitTimeSpinner, visitDurationSpinner, visitPrioritySpinner;
	private ArrayAdapter<String> myArrayAdapter;
	private ArrayAdapter<CharSequence> visitPriorityAdapter;
	private String name, clinic, apptDate, time, duration, priority, visitType;
	private PostAppointment postAppt = new PostAppointment();
	private AccessDBTable db = new AccessDBTable();
	private String clinicIDStr, clinicName, userID = ""; 
	private int clinicID, appointmentIntervalAsInt;
	private ProgressDialog pd;
	private Calendar c, myCalendar;
	private ArrayList<String> timeList = new ArrayList<String>();
	private ArrayList<String> durationList = new ArrayList<String>();
	private String timeBefore, timeAfter;
	private Date beforeAsDate, afterAsDate, afterAsDateMinusInterval;
	private String appointmentInterval;
	private AppointmentCalendarActivity passOptions = new AppointmentCalendarActivity();
	private SharedPreferences prefs;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_create_appointment);
		
		c = Calendar.getInstance();
		myCalendar = Calendar.getInstance();		

        userName = (EditText)findViewById(R.id.edit_service_user);
        confirmAppointment = (Button) findViewById(R.id.btn_confirm_appointment);
        confirmAppointment.setOnClickListener(new ButtonClick());
        btnUserSearch = (Button) findViewById(R.id.btn_user_search);
        btnUserSearch.setOnClickListener(new ButtonClick());
        visitTimeSpinner = (Spinner) findViewById(R.id.visit_time_spinner);
        visitTimeSpinner.setOnItemSelectedListener(new MySpinnerOnItemSelectedListener());
        visitDurationSpinner = (Spinner) findViewById(R.id.visit_duration_spinner);
        visitDurationSpinner.setOnItemSelectedListener(new MySpinnerOnItemSelectedListener());
        
        visitPrioritySpinner = (Spinner) findViewById(R.id.visit_priority_spinner);
        visitPrioritySpinner.setOnItemSelectedListener(new MySpinnerOnItemSelectedListener());
        visitPriorityAdapter = ArrayAdapter.createFromResource(this, R.array.visit_priority_list, R.layout.spinner_layout);
        visitPriorityAdapter.setDropDownViewResource(R.layout.spinner_layout);
        visitPrioritySpinner.setAdapter(visitPriorityAdapter);
	    
        textVisitType = (TextView) findViewById(R.id.text_visit_type);
        
        textDate = (TextView)findViewById(R.id.visit_date_text);
        textClinic = (TextView)findViewById(R.id.visit_clinic_text);
        
        Log.d("postAppointment", "time now: " + c.getTime());
        getSharedPrefs();

		myCalendar.setTime(AppointmentCalendarActivity.daySelected);
		textDate.setText(sdfDateMonthName.format(AppointmentCalendarActivity.daySelected));

		clinicID = Integer.parseInt(getIntent().getStringExtra("clinicID"));
		clinicName = ClinicSingleton.getInstance().getClinicName(String.valueOf(clinicID));
		textClinic.setText(clinicName);
		
        appointmentInterval = ClinicSingleton.getInstance().getAppointmentInterval(String.valueOf(clinicID));
        timeBefore = getIntent().getStringExtra("timeBefore");
        timeAfter = getIntent().getStringExtra("timeAfter");       
        
        Log.d("postAppointment", "timeBefore: " + timeBefore);
		Log.d("postAppointment", "timeAfter: " + timeAfter);
		
		setTimeSpinner();
		setDurationSpinner();
	}
	
	private void getSharedPrefs(){
		prefs = getSharedPreferences("SMART", MODE_PRIVATE);

		if (prefs != null && prefs.getBoolean("reuse", false)) {
			userName.setText(prefs.getString("name", null));
			userID = prefs.getString("id", null);
			textVisitType.setText(prefs.getString("visit_type_str", null));
			visitType = prefs.getString("visit_type", null);
		}
	}

	private void setDurationSpinner(){
		durationList.add(appointmentInterval + " minutes");
		durationList.add(String.valueOf(appointmentIntervalAsInt + appointmentIntervalAsInt) + " minutes");
		
		visitDurationSpinner = (Spinner) findViewById(R.id.visit_duration_spinner);
	    ArrayAdapter<String> myArrayAdapter = new ArrayAdapter<String>(this, R.layout.spinner_layout, durationList);
	    myArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
	    myArrayAdapter.setDropDownViewResource(R.layout.spinner_layout);
	    visitDurationSpinner.setAdapter(myArrayAdapter);
	}
	private void setTimeSpinner(){
        try {
        	appointmentIntervalAsInt = Integer.parseInt(appointmentInterval);
        	beforeAsDate = sdfTime.parse(timeBefore);
			afterAsDate = sdfTime.parse(timeAfter);
			
			c.setTime(afterAsDate);
			c.add(Calendar.MINUTE, - appointmentIntervalAsInt);			
			afterAsDateMinusInterval = c.getTime();
			Log.d("postAppointment", "afterAsDateMinusInterval: " + afterAsDateMinusInterval);
			timeList.add("Select Time");
			
			while(beforeAsDate.before(afterAsDateMinusInterval)){
				Log.d("postAppointment", "beforeAsDate: " + beforeAsDate);
				Log.d("postAppointment", "afterAsDate: " + afterAsDate);
				c.setTime(beforeAsDate);
				c.add(Calendar.MINUTE, appointmentIntervalAsInt);
				beforeAsDate = c.getTime();
				timeList.add(sdfTime.format(c.getTime()));
			}
			
			Log.d("postAppointment", "timeList: " + timeList);
		    visitTimeSpinner = (Spinner) findViewById(R.id.visit_time_spinner);
		    ArrayAdapter<String> myArrayAdapter = new ArrayAdapter<String>(this, R.layout.spinner_layout, timeList);
		    myArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		    myArrayAdapter.setDropDownViewResource(R.layout.spinner_layout);
		    visitTimeSpinner.setAdapter(myArrayAdapter);		    
        } catch (ParseException e) {
			e.printStackTrace();
		}
	}
	
	private class ButtonClick implements View.OnClickListener {
        public void onClick(View v) {
            switch (v.getId()) {
            case R.id.btn_confirm_appointment:
            	name = userName.getText().toString();
            	apptDate = sdfDate.format(myCalendar.getTime());
            	passOptions.setDaySelected(myCalendar.getTime());
            	           	
            	Log.d("appointment", "name: " + name + "\nclinic: " +  clinic  + "\nclinic id: " + clinicID + "\nDate: " + apptDate + 
            						 "\nTime: " + time + "\nDuration: " + duration + "\nPriority: " + priority +
            						 "\nVisit Type: " + visitType);
            	Log.d("postAppointment", "clinicID: " + clinicID);
            	Log.d("postAppointment", "clinicName: " + ClinicSingleton.getInstance().getClinicName(String.valueOf(clinicID)));
            	break;            
            case R.id.btn_user_search:
            	//buildeAlertDialog();
            	break;
            } 
        }
	}
	
	private void buildeAlertDialog(List<String> searchResults){  
    	searchResults.add("nore saturn");
    	searchResults.add("shannon mercury");
    	searchResults.add("nore saturn");
    	searchResults.add("liffey neptune");
    	searchResults.add("nore saturn");
    	searchResults.add("shannon mercury");
    	searchResults.add("nore saturn");
    	searchResults.add("liffey neptune");
    	searchResults.add("nore saturn");
    	searchResults.add("shannon mercury");
    	searchResults.add("nore saturn");
    	searchResults.add("liffey neptune");
    	searchResults.add("nore saturn");
    	searchResults.add("shannon mercury");
    	searchResults.add("nore saturn");
    	searchResults.add("liffey neptune");
    	searchResults.add("nore saturn");
    	searchResults.add("shannon mercury");
    	searchResults.add("nore saturn");
    	searchResults.add("liffey neptune");
    	searchResults.add("nore saturn");
    	searchResults.add("shannon mercury");
    	searchResults.add("nore saturn");
    	searchResults.add("liffey neptune");
    	searchResults.add("nore saturn");
    	searchResults.add("shannon mercury");
    	searchResults.add("nore saturn");
    	searchResults.add("liffey neptune");
    	
    	if(!name.isEmpty() && visitPrioritySpinner.getSelectedItemPosition() != 0) {
    		new LongOperation(CreateAppointmentActivity.this).execute("service_users?name=" + name, "appointments");
    	}else {
    		ToastAlert ta = new ToastAlert(CreateAppointmentActivity.this, "Cannot proceed, \nSome fields are empty!", true);
    	}
    	    	
		LayoutInflater inflater = getLayoutInflater();
		AlertDialog.Builder alertDialog = new AlertDialog.Builder(CreateAppointmentActivity.this);
		View convertView = (View) inflater.inflate(R.layout.list, null);
		ListView list = (ListView) convertView.findViewById(R.id.list_dialog);
		
		list.setOnItemClickListener(new onItemListener());
		
		alertDialog.setView(convertView);
		alertDialog.setTitle("List");
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(CreateAppointmentActivity.this, android.R.layout.simple_list_item_1, searchResults);
		list.setAdapter(adapter);
		alertDialog.show();
	}
	
    private class LongOperation extends AsyncTask<String, Void, Boolean> {
		private Context context;
		private JSONObject json;
		private JSONArray query;
		private Boolean userFound;
		
		public LongOperation(Context context){ this.context = context; }
		@Override
		protected void onPreExecute() {
			pd = new ProgressDialog(context);
            pd.setMessage("Creating Appointment");
            pd.show();
            clinicIDStr = String.valueOf(clinicID);
            
		}
		protected Boolean doInBackground(String... params) {
			Log.d("bugs", "userID start: " + userID);
			
			if(!prefs.getBoolean("reuse", false)){
				json = db.accessDB(params[0]);
				ServiceUserSingleton.getInstance().setPatientInfo(json);
				if(ServiceUserSingleton.getInstance().getUserID().size() > 0){
					Log.d("bugs", "user found");
					userID = ServiceUserSingleton.getInstance().getUserID().get(0);
					userFound = true;
				}else {
					Log.d("bugs", "user not found");
					userFound = false;
				}
				postAppt.postAppointment(userID, clinicIDStr, apptDate, time, duration, priority, visitType);
			} else {
				postAppt.postAppointment(userID, clinicIDStr, apptDate, time, duration, priority, visitType);
				userFound = true;
			}
			try {
				json = db.accessDB(params[1]);
				query = json.getJSONArray(params[1]);
				AppointmentSingleton.getInstance().setHashMapofClinicDateID(query);
				AppointmentSingleton.getInstance().setHashMapofIdAppt(query);
			} catch (JSONException e) {
				e.printStackTrace();
			}						
			return userFound;
		}
		@Override
		protected void onProgressUpdate(Void... values) {
		}
		@Override
        protected void onPostExecute(Boolean result) {
			if (result) {
				Intent intent = new Intent(CreateAppointmentActivity.this, AppointmentCalendarActivity.class);
				startActivity(intent);
			}else {
				Toast.makeText(CreateAppointmentActivity.this, "No user found, try again", Toast.LENGTH_LONG).show();
			}
			pd.dismiss();
        }
	}
    
    private class MySpinnerOnItemSelectedListener implements AdapterView.OnItemSelectedListener {
        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            switch (parent.getId()) {
                case R.id.visit_duration_spinner:
                	switch(position){
                	case 0: 
                		break;
                	default:
                		duration = durationList.get(position);
                	break;
                	}
                case R.id.visit_priority_spinner:
                    switch (position) {
                    case 0:
                    	//Select Visit Priority
                    	break;
                    case 1:
                    	priority = "scheduled";
                    	//Scheduled
                    	break;
                    case 2:
                    	priority = "drop-in";
                    	//Drop-In
                    	break;
                    }
                    break;
                case R.id.visit_time_spinner:
                    time = timeList.get(position);
                    break;
                case R.id.list_dialog:
                	Log.d("bugs", "list position is: " + position);
                	break;
            }
        }

		@Override
		public void onNothingSelected(AdapterView<?> arg0) {			
		}
    }
    
	private class onItemListener implements OnItemClickListener {
		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
			switch (parent.getId()){
				case R.id.list_dialog:
	            	Log.d("bugs", "list position is: " + position);
	            	break;
			}
		}
	}
	
	/*private class LongOperation extends AsyncTask<String, Void, JSONObject> {
		private Context context;

		public LongOperation(Context context) {
			this.context = context;
		}

		@Override
		protected void onPreExecute() {
			pd = new ProgressDialog(context);
			pd.setMessage("Fetching Information");
			pd.show();
		}

		protected JSONObject doInBackground(String... params) {
			Log.d("MYLOG", "ServiceUserSearch DoInBackground");
			json = dbTable.accessDB(params[0]);
			return json;
		}

		@Override
		protected void onProgressUpdate(Void... values) {
			Log.d("MYLOG", "On progress update");
		}

		@Override
		protected void onPostExecute(JSONObject result) {
			Log.d("MYLOG", "onPostExecute");
			Log.d("bugs", "Result from on post " + result);

			
			 * if result from database is empty (check if null) toast to say no
			 * query found if not empty do getSinglton.getName set this to
			 * button text
			 
			try {
				if (intent != null) {
					ServiceUserSingleton.getInstance().setPatientInfo(result);
					startActivity(intent);
				} else {
					searchResults.clear();
					hospitalNumberList.clear();
					if (result.getJSONArray("service_users").length() != 0) {
						for (int i = 0; i < result.getJSONArray("service_users").length(); i++) {
							ServiceUserSingleton.getInstance().setPatientInfo(result);
							String name = ServiceUserSingleton.getInstance().getUserName().get(i);
							String hospitalNumber = ServiceUserSingleton.getInstance().getUserHospitalNumber().get(i);
							String dob = ServiceUserSingleton.getInstance().getUserDOB().get(i);
							
							searchResults.add(name + " - " + hospitalNumber + " - " + dob);
							hospitalNumberList.add(hospitalNumber);
							Log.d("bugs", "searchResults: " + searchResults);
						}
						createResultList(searchResults);	
						adapter.notifyDataSetChanged();
					} else {
						Toast.makeText(getApplicationContext(), "No search results found", Toast.LENGTH_SHORT).show();
					}
				}
			} catch (JSONException e) {
				e.printStackTrace();
			}
			pd.dismiss();
		}
	}*/
}