package ie.teamchile.smartapp;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import org.json.JSONException;
import org.json.JSONObject;

import utility.ClinicSingleton;
import utility.ServiceUserSingleton;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import connecttodb.AccessDBTable;
import connecttodb.PostAppointment;

public class CreateAppointmentActivity extends MenuInheritActivity {
	private SimpleDateFormat sdfTime = new SimpleDateFormat("HH:mm:ss", Locale.getDefault());
	private SimpleDateFormat sdfDate = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
	private SimpleDateFormat sdfDateMonthName = new SimpleDateFormat("dd MMM yyyy", Locale.getDefault());
	private EditText userName, editDate;
	private Button confirmAppointment;
	private Spinner visitTimeSpinner, visitDurationSpinner, visitTypeSpinner, visitPrioritySpinner, visitClinicSpinner;
	private String name, clinic, apptDate, time, duration, priority, visitType;
	private PostAppointment postAppt = new PostAppointment();
	private AccessDBTable db = new AccessDBTable();
	private String clinicIDStr, daySelected; 
	private int clinicID, appointmentIntervalAsInt;
	private ProgressDialog pd;
	private Calendar c, myCalendar;
	private ArrayList<String> timeList = new ArrayList<String>();
	private ArrayList<String> durationList = new ArrayList<String>();
	private String timeBefore, timeAfter;
	private Date beforeAsDate, afterAsDate, afterAsDateMinusInterval;
	private String appointmentInterval;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_create_appointment);
		
		c = Calendar.getInstance();
		myCalendar = Calendar.getInstance();
		
        userName = (EditText)findViewById(R.id.edit_service_user);
        editDate = (EditText)findViewById(R.id.edit_appointment_date);
        confirmAppointment = (Button) findViewById(R.id.btn_confirm_appointment);
        confirmAppointment.setOnClickListener(new ButtonClick());
        visitTimeSpinner = (Spinner) findViewById(R.id.visit_time_spinner);
        visitTimeSpinner.setOnItemSelectedListener(new MySpinnerOnItemSelectedListener());
        visitDurationSpinner = (Spinner) findViewById(R.id.visit_duration_spinner);
        visitDurationSpinner.setOnItemSelectedListener(new MySpinnerOnItemSelectedListener());
        visitTypeSpinner = (Spinner) findViewById(R.id.visit_type_spinner);
        visitTypeSpinner.setOnItemSelectedListener(new MySpinnerOnItemSelectedListener());
        visitPrioritySpinner = (Spinner) findViewById(R.id.visit_priority_spinner);
        visitPrioritySpinner.setOnItemSelectedListener(new MySpinnerOnItemSelectedListener());
        visitClinicSpinner = (Spinner) findViewById(R.id.visit_clinic_spinner);
        visitClinicSpinner.setOnItemSelectedListener(new MySpinnerOnItemSelectedListener());
        
        Log.d("postAppointment", "time now: " + c.getTime());

		myCalendar.setTime(AppointmentCalendarActivity.daySelected);
		editDate.setText(sdfDateMonthName.format(AppointmentCalendarActivity.daySelected));

        clinicID = AppointmentCalendarActivity.hospitalSelected;
        appointmentInterval = ClinicSingleton.getInstance().getAppointmentInterval(String.valueOf(clinicID));
        timeBefore = getIntent().getStringExtra("timeBefore");
        timeAfter = getIntent().getStringExtra("timeAfter");       
        
        Log.d("postAppointment", "timeBefore: " + timeBefore);
		Log.d("postAppointment", "timeAfter: " + timeAfter);
		
		setTimeSpinner();
		setClinicSpinner();
		setDurationSpinner();
		createDatePicker();
	}

	private void createDatePicker() {
		final DatePickerDialog.OnDateSetListener pickerDate = new DatePickerDialog.OnDateSetListener() {
			@Override
			public void onDateSet(DatePicker view, int year, int monthOfYear,
					int dayOfMonth) {
				myCalendar.set(Calendar.YEAR, year);
				myCalendar.set(Calendar.MONTH, monthOfYear);
				myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
				Log.d("postAppointment", "datePicker: " + myCalendar.getTime());
				Log.d("postAppointment", "datePicker formatted: " + sdfDate.format(myCalendar.getTime()));
				editDate.setText(sdfDateMonthName.format(myCalendar.getTime()));
			}
		};
		editDate.setOnClickListener(new OnClickListener() {
	        @Override
	        public void onClick(View v) {
	            new DatePickerDialog(CreateAppointmentActivity.this, pickerDate, myCalendar
	                    .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
	                    myCalendar.get(Calendar.DAY_OF_MONTH)).show();
	        }
	    });
	}
	private void setDurationSpinner(){
		durationList.add(appointmentInterval + " minutes");
		durationList.add(String.valueOf(appointmentIntervalAsInt + appointmentIntervalAsInt) + " minutes");
		
		visitDurationSpinner = (Spinner) findViewById(R.id.visit_duration_spinner);
	    ArrayAdapter<String> myArrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, durationList);
	    myArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
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
		    ArrayAdapter<String> myArrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, timeList);
		    myArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		    visitTimeSpinner.setAdapter(myArrayAdapter);		    
        } catch (ParseException e) {
			e.printStackTrace();
		}
	}
	private void setClinicSpinner(){
        if(clinicID < 7){
        	visitClinicSpinner.setSelection(clinicID);
        	clinicID = clinicID;
    	} else if(clinicID == 7){
    		clinicID = 6;        
    		visitClinicSpinner.setSelection(6);
    	} else if(clinicID > 7){
    		clinicID = clinicID - 1;     
    		visitClinicSpinner.setSelection(clinicID - 1);
    	}
	}
	private class ButtonClick implements View.OnClickListener {
        public void onClick(View v) {
            switch (v.getId()) {
            case R.id.btn_confirm_appointment:
            	name = userName.getText().toString();
            	//apptDate = editDate.getText().toString();
            	apptDate = sdfDate.format(myCalendar.getTime());
            	
            	Log.d("appointment", "name: " + name + "\nclinic: " +  clinic  + "\nclinic id: " + clinicID + "\nDate: " + apptDate + 
            						 "\nTime: " + time + "\nDuration: " + duration + "\nPriority: " + priority +
            						 "\nVisit Type: " + visitType);
            	
            	new LongOperation(CreateAppointmentActivity.this).execute("service_users?name=" + name);
            	
            	Log.d("postAppointment", "clinicID: " + clinicID);
            	Log.d("postAppointment", "clinicName: " + ClinicSingleton.getInstance().getClinicName(String.valueOf(clinicID)));
            	break;
            } 
        }
	}
    private class LongOperation extends AsyncTask<String, Void, JSONObject> {
		private Context context;
		private JSONObject json;
		
		public LongOperation(Context context){
			this.context = context;
		}
		@Override
		protected void onPreExecute() {
			pd = new ProgressDialog(context);
            pd.setMessage("Creating Appointment");
            pd.show();
            clinicIDStr = String.valueOf(clinicID);
            
		}
		protected JSONObject doInBackground(String... params) {
			try {
				String dbQuery = db.accessDB(params[0]);
				json = new JSONObject(dbQuery);

				ServiceUserSingleton.getInstance().setPatientInfo(json);
				String userID = ServiceUserSingleton.getInstance().getID();
				
				postAppt.postAppointment(userID, clinicIDStr, apptDate, time, duration,
						priority, visitType);

			} catch (JSONException e) {
				e.printStackTrace();
			}
			return null;
		}
		@Override
		protected void onProgressUpdate(Void... values) {
		}
		@Override
        protected void onPostExecute(JSONObject result) {
			pd.dismiss();
        }
	}
    private class MySpinnerOnItemSelectedListener implements AdapterView.OnItemSelectedListener {
        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            switch (parent.getId()) {
                case R.id.visit_duration_spinner:
                    duration = durationList.get(position);
                    break;
                case R.id.visit_type_spinner:
                    switch (position) {
                    case 0:
                    	//Select Visit Type
                    	break;
                    case 1:
                    	//Ante-Natal
                    	visitType = "ante-natal";
                    	break;
                    case 2:
                    	//Post-Natal
                    	visitType = "post-natal";
                    	break;
                    }
                    break;
                case R.id.visit_priority_spinner:
                    switch (position) {
                    case 0:
                    	//Select Visit Priority
                    	break;
                    case 1:
                    	priority = "scheduled";
                    	//Scheduled
                    	break;
                    }
                    break;
                case R.id.visit_time_spinner:
                    time = timeList.get(position);
                    break;
                case R.id.visit_clinic_spinner:
                	if(position < 7){
                		clinicID = position;
                	} else if(position == 7){
                		clinicID = 6;                		
                	} else if(position > 7){
                		clinicID = position - 1;                		
                	}
                	break;
            }
        }

		@Override
		public void onNothingSelected(AdapterView<?> arg0) {			
		}
    }
}