package ie.teamchile.smartapp.activities;

import ie.teamchile.smartapp.R;
import ie.teamchile.smartapp.connecttodb.AccessDBTable;
import ie.teamchile.smartapp.utility.ClinicSingleton;
import ie.teamchile.smartapp.utility.ServiceUserSingleton;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.NoSuchElementException;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

public class CreateAppointmentActivity extends MenuInheritActivity {
/*	@InjectView (R.id.edit_service_user) EditText userName;
	@InjectView (R.id.btn_confirm_appointment) Button confirmAppointment;
	@InjectView (R.id.btn_user_search) Button btnUserSearch;
	@InjectView (R.id.visit_time_text) TextView textTime;
	@InjectView (R.id.visit_date_text) TextView textDate;
	@InjectView (R.id.visit_clinic_text) TextView textClinic;
	@InjectView (R.id.visit_duration_spinner) Spinner visitDurationSpinner;
	@InjectView (R.id.visit_priority_spinner) Spinner visitPrioritySpinner;*/
	
	private SimpleDateFormat sdfTime = new SimpleDateFormat("HH:mm", Locale.getDefault());
	private SimpleDateFormat sdfDate = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
	private SimpleDateFormat sdfDateMonthName = new SimpleDateFormat("dd MMM yyyy", Locale.getDefault());
	private ArrayAdapter<CharSequence> visitPriorityAdapter;
	private String name, clinic, apptDate, time, duration, priority, visitType, appointmentInterval,
				   clinicIDStr, clinicName, userID = "";
	private AccessDBTable db = new AccessDBTable();
	private ProgressDialog pd;
	private Calendar c, myCalendar;
	private List<String> timeList = new ArrayList<String>();
	private List<String> durationList = new ArrayList<String>();
	private List<String> idList, babyIDs;
	//private Date beforeAsDate, afterAsDate, afterAsDateMinusInterval;
	private AppointmentCalendarActivity passOptions = new AppointmentCalendarActivity();
	private SharedPreferences prefs;
	private AlertDialog.Builder alertDialog;
	private AlertDialog ad;
	private int clinicID, appointmentIntervalAsInt;
	private int p = 0;
	private EditText userName;
	private Button confirmAppointment;
	private ImageButton btnUserSearch;
	private TextView textTime, textDate, textClinic;
	private Spinner visitDurationSpinner, visitPrioritySpinner;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentForNav(R.layout.activity_create_appointment);
		//ButterKnife.inject(this);
		
/*		c = Calendar.getInstance();
		myCalendar = Calendar.getInstance();
		
		userName = (EditText) findViewById(R.id.edit_service_user);
		confirmAppointment = (Button) findViewById(R.id.btn_confirm_appointment);
		btnUserSearch = (ImageButton) findViewById(R.id.btn_user_search);
		textTime = (TextView) findViewById(R.id.visit_time_text);
		textDate = (TextView) findViewById(R.id.visit_date_text);
		textClinic = (TextView) findViewById(R.id.visit_clinic_text);
		visitDurationSpinner = (Spinner) findViewById(R.id.visit_duration_spinner);
		visitPrioritySpinner = (Spinner) findViewById(R.id.visit_priority_spinner);

        confirmAppointment.setOnClickListener(new ButtonClick());
        btnUserSearch.setOnClickListener(new ButtonClick());
        
        visitDurationSpinner.setOnItemSelectedListener(new MySpinnerOnItemSelectedListener());
       
        visitPrioritySpinner.setOnItemSelectedListener(new MySpinnerOnItemSelectedListener());
        visitPriorityAdapter = ArrayAdapter.createFromResource(this, 
        				R.array.visit_priority_list, 
        				R.layout.spinner_layout_create_appt);
        visitPriorityAdapter.setDropDownViewResource(R.layout.spinner_layout);
        visitPrioritySpinner.setAdapter(visitPriorityAdapter);
        visitPrioritySpinner.setSelection(1);		//sets visit pirority to Scheduled
        
        Log.d("postAppointment", "time now: " + c.getTime());
        getSharedPrefs();

		myCalendar.setTime(AppointmentCalendarActivity.daySelected);
		textDate.setText(sdfDateMonthName.format(AppointmentCalendarActivity.daySelected));

		clinicID = Integer.parseInt(getIntent().getStringExtra("clinicID"));
		clinicName = ClinicSingleton.getInstance().getClinicName(String.valueOf(clinicID));
		textClinic.setText(clinicName);
		
        appointmentInterval = ClinicSingleton.getInstance().getAppointmentInterval(String.valueOf(clinicID));
        time = getIntent().getStringExtra("time"); 
        textTime.setText(time);
        
		Log.d("postAppointment", "timeAfter: " + time );
		
		setDurationSpinner();
		checkIfEditEmpty();
		checkDirectionOfIntent();*/
	}
	
	@Override
	protected void onResume() {
		super.onResume();
		Log.d("MYLOG", "In onResume CreateAppointment");
		checkDirectionOfIntent();
	}
	
	private void checkDirectionOfIntent(){
		String intentOrigin = getIntent().getStringExtra("from"); 
		if(intentOrigin.equals("appointment")) {
			getSharedPrefs();
		} else if (intentOrigin.equals("confirm")) {
			userName.setText(getIntent().getStringExtra("userName"));
			userID = getIntent().getStringExtra("userId");
		}
	}

	private void checkIfEditEmpty(){		
		if(TextUtils.equals(userID, "") || TextUtils.equals(userName.getText(), "")) {
		    userName.setError("Field Empty");
		    return;
		 }
	}
	
	private void getSharedPrefs(){
		prefs = getSharedPreferences("SMART", MODE_PRIVATE);

		if (prefs != null && prefs.getBoolean("reuse", false)) {
			userName.setText(prefs.getString("name", null));
			userID = prefs.getString("id", null);
			visitType = prefs.getString("visit_type", null);
		}
	}
	
/*    private void setSharedPrefs(){
    	SharedPreferences.Editor prefs = getSharedPreferences("SMART", MODE_PRIVATE).edit();
		prefs.putString("name", ServiceUserSingleton.getInstance().getUserName().get(0));
		prefs.putString("id", ServiceUserSingleton.getInstance().getUserID().get(0));
		prefs.putBoolean("reuse", true);
		prefs.commit();
    }*/

	private void setDurationSpinner(){
		durationList.add(appointmentInterval + " minutes");
		durationList.add(String.valueOf(appointmentIntervalAsInt + appointmentIntervalAsInt) + " minutes");
		
		visitDurationSpinner = (Spinner) findViewById(R.id.visit_duration_spinner);
	    ArrayAdapter<String> myArrayAdapter = new ArrayAdapter<String>(this, R.layout.spinner_layout_create_appt, durationList);
	    myArrayAdapter.setDropDownViewResource(R.layout.spinner_layout);
	    visitDurationSpinner.setAdapter(myArrayAdapter);
	}
	
	private class ButtonClick implements View.OnClickListener {
        public void onClick(View v) {
            switch (v.getId()) {
            case R.id.btn_confirm_appointment:
            	name = userName.getText().toString();
            	apptDate = sdfDate.format(myCalendar.getTime());
            	passOptions.setDaySelected(myCalendar.getTime());
            	checkIfEditEmpty();
            	           	
            	Log.d("appointment", "name: " + name + "\nclinic: " +  clinic  + "\nclinic id: " + clinicID + "\nDate: " + apptDate + 
            						 "\nTime: " + time + "\nDuration: " + duration + "\nPriority: " + priority +
            						 "\nVisit Type: " + visitType);
            	Log.d("postAppointment", "clinicID: " + clinicID);
            	Log.d("postAppointment", "clinicName: " + ClinicSingleton.getInstance().getClinicName(String.valueOf(clinicID)));
            	
            	if(!name.isEmpty() && visitPrioritySpinner.getSelectedItemPosition() != 0) {
            		
            		Intent intent = new Intent(CreateAppointmentActivity.this, ConfirmAppointmentActivity.class);
            		Bundle extras = new Bundle();
            		extras.putString("clinicName", ClinicSingleton.getInstance().getClinicName(String.valueOf(clinicID)));
            		extras.putString("clinicID", String.valueOf(clinicID));
            		extras.putString("date", apptDate);
            		extras.putString("time", time);
            		extras.putString("duration", duration);
            		extras.putString("priority", priority);
            		extras.putString("userId", userID);
            		extras.putString("visitType", visitType);
            		intent.putExtras(extras);
            		startActivity(intent);
            	}else {
            		pd = new ProgressDialog(CreateAppointmentActivity.this);
        			pd.setMessage("Cannot proceed, \nSome fields are empty!");
        			pd.show();
        			CountDownTimer timer = new CountDownTimer(2000, 1000){
						@Override
						public void onFinish() {
							pd.cancel();
						}
						@Override
						public void onTick(long millisUntilFinished) {
						}        				
        			}.start();
            	}
            	break;            
            case R.id.btn_user_search:
            	hideKeyboard();
            	userID = "";
            	name = userName.getText().toString();
            	//checkIfEditEmpty();
            	new UserSearchLongOperation(CreateAppointmentActivity.this, true).execute("service_users?name=" + name);
            	break;
            } 
        }
	}
	
	private void buildeAlertDialog(List<String> searchResults){      	    	
		LayoutInflater inflater = getLayoutInflater();
		alertDialog = new AlertDialog.Builder(CreateAppointmentActivity.this);
		View convertView = (View) inflater.inflate(R.layout.list, null);
		ListView list = (ListView) convertView.findViewById(R.id.list_dialog);
		
		list.setOnItemClickListener(new onItemListener());
		
		alertDialog.setView(convertView);
		alertDialog.setTitle("Search Results");
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(
				CreateAppointmentActivity.this, 
				android.R.layout.simple_list_item_1, 
				searchResults);
		list.setAdapter(adapter);
		ad = alertDialog.show();
	}
	
	private class UserSearchLongOperation extends AsyncTask<String, Void, JSONObject> {
		private Context context;
		private JSONObject json;
		private List<String> searchResults = new ArrayList<String>();
		private Boolean showDialog;
		private ProgressDialog pd;
		private String name, hospitalNumber, dob, id;

		public UserSearchLongOperation(Context context, Boolean showDialog) { 
			this.context = context; 
			this.showDialog = showDialog;
		}

		@Override
		protected void onPreExecute() {
			pd = new ProgressDialog(context);
			pd.setMessage("Fetching Information");
			pd.setCanceledOnTouchOutside(false);
			pd.setCancelable(false);
			pd.show();
		}

		protected JSONObject doInBackground(String... params) {
			json = db.accessDB(params[0]);
			return json;
		}

		@Override
		protected void onProgressUpdate(Void... values) {}

		@Override
		protected void onPostExecute(JSONObject result) {
			idList = new ArrayList<>();
			Log.d("bugs", "Result from on post " + result);
			try {
				if (result.getJSONArray("service_users").length() != 0) {
					for (int i = 0; i < result.getJSONArray("service_users").length(); i++) {
						ServiceUserSingleton.getInstance().setPatientInfo(result);
						name = ServiceUserSingleton.getInstance().getUserName().get(i);
						hospitalNumber = ServiceUserSingleton.getInstance().getUserHospitalNumber().get(i);
						dob = ServiceUserSingleton.getInstance().getUserDOB().get(i);
						id = ServiceUserSingleton.getInstance().getUserID().get(i);

						idList.add(id);
						searchResults.add(name + "\n" + hospitalNumber + "\n" + dob);
						Log.d("bugs", "searchResults: " + searchResults);
						
						postOrAnte();
						getRecentPregnancy();
					}
					
/*					if(idList.size() == 1){
						setSharedPrefs();
					}*/
					
					pd.dismiss();
					if(showDialog){
						Log.d("bugs", "showDialog true");
						buildeAlertDialog(searchResults);
					}  else {
						Log.d("bugs", "showDialog false");
						userName.setText(name);
					} 
				} else {
					pd.dismiss();
					Toast.makeText(getApplicationContext(), "No search results found", Toast.LENGTH_SHORT).show();
				}
			} catch (JSONException e) { e.printStackTrace(); }
		}
	}
    
    private class MySpinnerOnItemSelectedListener implements AdapterView.OnItemSelectedListener {
        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            switch (parent.getId()) {
                case R.id.visit_duration_spinner:
                	switch(position){
                	case 0: 
                		duration = durationList.get(position);
                		Log.d("bugs", "15 minutes");
                		break;
                	case 1:
                		Log.d("bugs", "timeList.size(): " + timeList.size());
            			if(timeList.size() == 2){
            				Log.d("bugs", "30 minutes if true");
            				Toast.makeText(CreateAppointmentActivity.this, 
            						"No 30 minute slots available", 
            						Toast.LENGTH_LONG).show();
            				visitDurationSpinner.setSelection(0);
            			}  else { duration = durationList.get(position); 
            			Log.d("bugs", "15 minutes if false");}
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
                case R.id.list_dialog:
                	Log.d("bugs", "list position is: " + position);
                	break;
            }
        }

		@Override
		public void onNothingSelected(AdapterView<?> arg0) { }
    }
    
	private class onItemListener implements OnItemClickListener {
		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
			switch (parent.getId()){
				case R.id.list_dialog:
					hideKeyboard();
					userID = idList.get(position);
					new UserSearchLongOperation(CreateAppointmentActivity.this, false).execute("service_users/" + userID);
					ad.cancel();
	            	Log.d("bugs", "list position is: " + position);
	            	break;
			}
		}
	}
	
	private void postOrAnte(){
		babyIDs = ServiceUserSingleton.getInstance().getPregnancyBabyIDs();	
		if(babyIDs.get(p).equals("[]")){
			visitType = "ante-natal";
    	}else {
    		visitType = "post-natal";
    	}
	}
	
    private void getRecentPregnancy(){
    	List<String> edd = new ArrayList<String>();
    	List<Date> asDate = new ArrayList<Date>();
    	
    	edd = ServiceUserSingleton.getInstance().getPregnancyEstimatedDeliveryDate();
    	Log.d("bugs", "edd.size(): " + edd.size());
    	if(edd.size() > 0){
    		for(int i = 0; i < edd.size(); i++){
    			if(!edd.get(i).equals("null")){
	    			Log.d("bugs", "edd.get(i): " + edd.get(i));
	        		try {
	    				asDate.add(sdfDate.parse(edd.get(i)));
	    			} catch (ParseException e) {
	    				e.printStackTrace();
	    			}
    			}
        	}    
    		try {
    			p = asDate.indexOf(Collections.max(asDate));
    		} catch (NoSuchElementException e) {
				e.printStackTrace();
			}
    	}    	
    }
    
	private void hideKeyboard() {
		InputMethodManager inputManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
		inputManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
	}
}
