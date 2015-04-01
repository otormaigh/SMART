package ie.teamchile.smartapp.activities;

import ie.teamchile.smartapp.R;
import ie.teamchile.smartapp.connecttodb.AccessDBTable;
import ie.teamchile.smartapp.utility.AppointmentSingleton;
import ie.teamchile.smartapp.utility.ClinicSingleton;
import ie.teamchile.smartapp.utility.ServiceUserSingleton;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import org.json.JSONObject;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class AppointmentCalendarActivity extends MenuInheritActivity {
	private static int serviceOptionSelected, weekSelected, clinicSelected;
	protected static Date daySelected;
	private Date openingAsDate, closingAsDate;
	private String clinicOpening, clinicClosing, closingMinusInterval,
				   dateSelectedStr, timeBefore, timeAfter, nameOfClinic;
	private int appointmentInterval, dayOfWeek;
	private DateFormat dfDateOnly = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
	private DateFormat dfTimeOnly = new SimpleDateFormat("HH:mm", Locale.getDefault());
	private DateFormat dfDateWithMonthName = new SimpleDateFormat("dd MMM", Locale.getDefault());
	private ArrayList<String> timeSingle, gestSingle, nameSingle;	
	private ArrayList<String> listOfId = new ArrayList<String>();
	private Calendar c = Calendar.getInstance(), myCalendar = Calendar.getInstance();
	private ListView listView;
	private BaseAdapter adapter;
	private Button dateInList, prevWeek, nextWeek;	
	private Intent intent;
	private ProgressDialog pd;
	private AccessDBTable db = new AccessDBTable();
	private JSONObject json;
	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_appointment_calendar);
        
        listView = (ListView)findViewById(R.id.list);
        dateInList = (Button)findViewById(R.id.date_button);
        prevWeek = (Button)findViewById(R.id.prev_button);
        prevWeek.setOnClickListener(new ButtonClick());
        nextWeek = (Button)findViewById(R.id.next_button);
        nextWeek.setOnClickListener(new ButtonClick());
        
        clinicOpening = ClinicSingleton.getInstance().getOpeningTime(String.valueOf(clinicSelected));
		clinicClosing = ClinicSingleton.getInstance().getClosingTime(String.valueOf(clinicSelected));
		appointmentInterval = Integer.parseInt(ClinicSingleton.getInstance().getAppointmentInterval(String.valueOf(clinicSelected)));
				
		try {
			openingAsDate = dfTimeOnly.parse(String.valueOf(clinicOpening));
			closingAsDate = dfTimeOnly.parse(String.valueOf(clinicClosing));
		} catch (ParseException e) {
			e.printStackTrace();
		}
		
		myCalendar.setTime(closingAsDate);
		myCalendar.add(Calendar.MINUTE, (- appointmentInterval));
		closingMinusInterval = dfTimeOnly.format(myCalendar.getTime());
       
		c.setTime(daySelected);
        dayOfWeek = c.get(Calendar.DAY_OF_WEEK);
        c.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
        
		Log.d("MYLOG", "Date set to " + c.getTime());		
		Log.d("MYLOG", "selectOption");
		Log.d("MYLOG", "region: " + serviceOptionSelected);
		Log.d("MYLOG", "hospital: " + clinicSelected);
		Log.d("MYLOG", "week: " + weekSelected);
		Log.d("MYLOG", "day: " + daySelected);
        setAptToListSingle(daySelected);
        adapter.notifyDataSetChanged();
        createDatePicker();
    }
    
    @Override
	protected void onResume() {
		super.onResume();
		Log.d("bugs", "in onResume");
		Log.d("bugs", "daySelected: " + daySelected);
		listView.setAdapter(null);
		adapter.notifyDataSetChanged();
		setAptToListSingle(daySelected);
	}

	private class ButtonClick implements View.OnClickListener {
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.prev_button:
                	c.setTime(daySelected);
                	c.add(Calendar.DAY_OF_YEAR, -7);
                	daySelected = c.getTime();
                	myCalendar.setTime(daySelected);
                	createDatePicker();
                	listView.setAdapter(null);
                	setAptToListSingle(c.getTime());
                	pauseButton();
                	break;
                case R.id.next_button:
                	c.setTime(daySelected);
                	c.add(Calendar.DAY_OF_YEAR, 7);
                	daySelected = c.getTime();
                	myCalendar.setTime(daySelected);
                	createDatePicker();
                	listView.setAdapter(null);
                	setAptToListSingle(c.getTime());
                	pauseButton();
                    break;
            }
        }
    }
    
    public void pauseButton(){
    	nextWeek.setEnabled(false);
    	prevWeek.setEnabled(false);
    	CountDownTimer nextTimer = new CountDownTimer(250, 250) {						
			@Override
			public void onTick(long millisUntilFinished) {								
			}						
			@Override
			public void onFinish() {	
				nextWeek.setEnabled(true);
				prevWeek.setEnabled(true);
			}
		};
		nextTimer.start();
    }
        
    public ArrayList<String> removeZeros(ArrayList<String> badList){
    	if(badList != null){
	    	for(int i = 0; i < badList.size(); i ++){
				if(badList.get(i).equals("0")){
					badList.remove("0");			
				}
			}
    	}
    	return badList;
    }
    
	private void createDatePicker() {
		myCalendar.setTime(daySelected);
		final DatePickerDialog.OnDateSetListener pickerDate = new DatePickerDialog.OnDateSetListener() {
			@Override
			public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
				myCalendar.set(Calendar.YEAR, year);
				myCalendar.set(Calendar.MONTH, monthOfYear);
				myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
				Log.d("postAppointment", "datePicker: " + myCalendar.getTime());
				Log.d("postAppointment", "datePicker formatted: " + dfDateOnly.format(myCalendar.getTime()));
				Log.d("bugs", "c.getDay: " + dayOfWeek);
				Log.d("bugs", "myCalendar.getDay: " + myCalendar.get(Calendar.DAY_OF_WEEK));
				if(myCalendar.get(Calendar.DAY_OF_WEEK) == dayOfWeek){
					dateInList.setText(dfDateWithMonthName.format(myCalendar.getTime()));
					setAptToListSingle(myCalendar.getTime());					
				} else {
					Toast.makeText(AppointmentCalendarActivity.this, "Invalid day selected", Toast.LENGTH_LONG).show();
				}
			}
		};
		dateInList.setOnClickListener(new OnClickListener() {
	        @Override
	        public void onClick(View v) {
	            new DatePickerDialog(AppointmentCalendarActivity.this, pickerDate, myCalendar
	                    .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
	                    myCalendar.get(Calendar.DAY_OF_MONTH)).show();	            
	        }
	    });
	}
    
    public void setAptToListSingle(Date dateSelected){
    	timeSingle = new ArrayList<String>();
    	nameSingle = new ArrayList<String>();
    	gestSingle = new ArrayList<String>();
    	daySelected = dateSelected;
    	dateSelectedStr = dfDateOnly.format(dateSelected);
    	
    	dateInList.setText(dfDateWithMonthName.format(dateSelected));
    	nameOfClinic = ClinicSingleton.getInstance().getClinicName(String.valueOf(clinicSelected));
    	setTitle(nameOfClinic);
    	
    	if(AppointmentSingleton.getInstance().getHashMapofClinicDateID().containsKey(String.valueOf(clinicSelected))){
    		Log.d("bugs", "appt map has key");
    		listOfId = AppointmentSingleton.getInstance().getListOfIDs(String.valueOf(clinicSelected), dateSelectedStr);
    	} else {
    		Log.d("bugs", "appt map doenst have key");
    		listOfId = new ArrayList<String>();
    	}

		if (listOfId == null || listOfId.isEmpty()) {
			timeSingle.add("---------");
			nameSingle.add("Free Slot");
			gestSingle.add("---------");
			listOfId = new ArrayList<String>();
			listOfId.add("0");
		} else if(listOfId != null || !listOfId.isEmpty()){	
			listOfId = removeZeros(listOfId);			
			timeSingle = AppointmentSingleton.getInstance().getTime(listOfId);
			nameSingle = AppointmentSingleton.getInstance().getName(listOfId);
			gestSingle = AppointmentSingleton.getInstance().getGestation(listOfId);	

			for (int i = 0; i < timeSingle.size() - 1; i++) {
				try {
					String timeFirst;
					String timeSecond;
					Date timeA = null;
					Date timeB = null;
					Date time = dfTimeOnly.parse(timeSingle.get(i));

					Log.d("bugs", "time: " + time + "\nclosingAsDate: " + closingAsDate);
					Log.d("bugs", "time equals close: " + time.equals(closingAsDate));
					timeFirst = timeSingle.get(i);
					timeSecond = timeSingle.get(i + 1);
					timeA = dfTimeOnly.parse(String.valueOf(timeFirst));
					timeB = dfTimeOnly.parse(String.valueOf(timeSecond));
					c.setTime(timeA);
					c.add(Calendar.MINUTE, appointmentInterval);

					if (!timeB.equals(c.getTime()) && !timeA.equals(timeB)) {
						Log.d("appointment", "Free Slot Here");
						timeSingle.add(i + 1, "---------");
						nameSingle.add(i + 1, "Free Slot");
						gestSingle.add(i + 1, "----------");
						listOfId.add(i + 1, "0");
					}
				} catch (ParseException e) {
					e.printStackTrace();
				}
			}
			if (!timeSingle.get(0).equals(clinicOpening)) {
				timeSingle.add(0, "---------");
				nameSingle.add(0, "Free Slot");
				gestSingle.add(0, "---------");
				listOfId.add(0, "0");				
			}
			if (!timeSingle.get((timeSingle.size() - 1)).equals(clinicClosing)) {
				timeSingle.add("---------");
				nameSingle.add("Free Slot");
				gestSingle.add("---------");
				listOfId.add("0");
			}
		}				
		adapter = new ListElementAdapter (AppointmentCalendarActivity.this, timeSingle, nameSingle, gestSingle);
		adapter.notifyDataSetChanged();
        listView.setAdapter(adapter);
        adapter.notifyDataSetChanged();
        listView.setOnItemClickListener(new OnItemListener());
    }
    
    private class ListElementAdapter extends BaseAdapter {
		LayoutInflater layoutInflater;
		ArrayList<String> aptTime, aptName, aptGest;

		@Override
		public void notifyDataSetChanged() {
			super.notifyDataSetChanged();
		}
		
		public ListElementAdapter(Context context, ArrayList<String> aptTime, 
								  ArrayList<String> aptName, ArrayList<String> aptGest) {
			super();
			Log.d("MYLOG", "daySelected: " + daySelected);
			Log.d("MYLOG", "List Adapter Called");
			this.aptTime = aptTime;
			this.aptName = aptName;
			this.aptGest = aptGest;
			layoutInflater = LayoutInflater.from(context);
		}
		
		@Override
		public int getCount() {
			return aptTime.size();
		}		
		@Override
		public Object getItem(int position) {
			return null;
		}		
		@Override
		public long getItemId(int position) {
			return position;
		}
		
		@Override
		public View getView(final int position, View convertView, ViewGroup parent) {
			convertView = layoutInflater.inflate(R.layout.list_rows, null);
			TextView timeText = (TextView) convertView.findViewById(R.id.time);
			TextView nameText = (TextView) convertView.findViewById(R.id.name);
			TextView gestText = (TextView) convertView.findViewById(R.id.gestation);
			
			
			timeText.setText(aptTime.get(position));
			nameText.setText(aptName.get(position));
			gestText.setText(aptGest.get(position));    	
			return convertView;
		}
	}
    
    private class OnItemListener implements OnItemClickListener {
		@Override
		public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3) {
			if(listOfId.get(position).equals("0")){
				intent = new Intent(AppointmentCalendarActivity.this, CreateAppointmentActivity.class);
				if(listOfId.size() == 1){
					myCalendar.setTime(openingAsDate);
					myCalendar.add(Calendar.MINUTE, - appointmentInterval);
					
					timeBefore = dfTimeOnly.format(myCalendar.getTime());
					timeAfter = clinicClosing;
				} 
				else if(listOfId.size() > 0 && position == 0){
					myCalendar.setTime(openingAsDate);
					myCalendar.add(Calendar.MINUTE, - appointmentInterval);
					
					timeBefore = dfTimeOnly.format(myCalendar.getTime());
					timeAfter = AppointmentSingleton.getInstance().getTime(listOfId.get(position + 1));
				} 
				else if(listOfId.size() > 0 && position == listOfId.size() - 1){
					timeBefore = AppointmentSingleton.getInstance().getTime(listOfId.get(position - 1));
					myCalendar.setTime(closingAsDate);
					myCalendar.add(Calendar.MINUTE, + appointmentInterval);
					timeAfter = dfTimeOnly.format(myCalendar.getTime());
				} 
				else if(listOfId.size() > 0 && listOfId.get(position - 1) != null && listOfId.get(position + 1) != null){
					timeBefore = AppointmentSingleton.getInstance().getTime(listOfId.get(position - 1));
					timeAfter = AppointmentSingleton.getInstance().getTime(listOfId.get(position + 1));
				}
				intent.putExtra("timeBefore", timeBefore);
				intent.putExtra("timeAfter", timeAfter);
				intent.putExtra("clinicID", String.valueOf(clinicSelected));
				startActivity(intent);
				
			} else {
				String serviceUserID = AppointmentSingleton.getInstance().getServiceUserID(listOfId.get(position));
				
				Log.d("bugs", "db string: " + "service_users" + "/" + serviceUserID);
				new LongOperation(AppointmentCalendarActivity.this).execute("service_users" + "/" + serviceUserID);
				
				intent = new Intent(AppointmentCalendarActivity.this, ServiceUserActivity.class);
			}
		}		    	
    }
    
    private class LongOperation extends AsyncTask<String, Void, JSONObject> {
		private Context context;
		
		public LongOperation(Context context){
			this.context = context;
		}
		@Override
		protected void onPreExecute() {
			pd = new ProgressDialog(context);
            pd.setMessage("Fetching Information");
            pd.show();
		}
		protected JSONObject doInBackground(String... params) {
			Log.d("singleton", "in service users updateLocal doInBackground");
			json = db.accessDB(params[0]);
			Log.d("singleton", "query = " + json);
			return json;
		}
		@Override
		protected void onProgressUpdate(Void... values) {
		}
		@Override
        protected void onPostExecute(JSONObject result) {
			ServiceUserSingleton.getInstance().setPatientInfo(result);
			pd.dismiss();
			startActivity(intent);	
        }
	}
    
    public void setServiceOptionSelected(int serviceOptionSelected){
    	AppointmentCalendarActivity.serviceOptionSelected = serviceOptionSelected;
    }
    public void setClinicSelected(int clinicSelected){
    	AppointmentCalendarActivity.clinicSelected = clinicSelected;
    }
    public void setWeekSelected(int weekSelected){
    	AppointmentCalendarActivity.weekSelected = weekSelected;
    }
    public void setDaySelected(Date daySelected){
    	AppointmentCalendarActivity.daySelected = daySelected;
    }
}