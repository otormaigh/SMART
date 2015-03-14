package ie.teamchile.smartapp;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import org.json.JSONObject;

import utility.AppointmentSingleton;
import utility.ClinicSingleton;
import utility.ServiceUserSingleton;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import connecttodb.AccessDBTable;
import connecttodb.DateSorter;
import connecttodb.SetDateToHashMap;

public class AppointmentCalendarActivity extends MenuInheritActivity {
	private static int serviceOptionSelected;
	protected static int clinicSelected;
	private static int weekSelected;
	protected static Date daySelected;
	private Date day = null, openingAsDate, closingAsDate;
	private String closingMinusInterval;
	private DateFormat df = new SimpleDateFormat("yyyy-MM-dd - HH:mm:ss", Locale.getDefault());
	private DateFormat dfDateOnly = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
	private DateFormat dfDateOnlyOther = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
	private DateFormat dfTimeOnly = new SimpleDateFormat("HH:mm:ss", Locale.getDefault());
	private DateFormat dfDateWithMonthName = new SimpleDateFormat("dd MMM yyyy", Locale.getDefault());	
	private ArrayList<JSONObject> aptsAtDate = new ArrayList<JSONObject>();
	private ArrayList<String> timeSingle, gestSingle, nameSingle;	
	private Calendar c = Calendar.getInstance();
	private Calendar myCalendar = Calendar.getInstance();
	private DateSorter ds = new DateSorter();
	private SetDateToHashMap getDates = new SetDateToHashMap();
	private ListView listView;
	private Object time, name, gestation;
	private BaseAdapter adapter;
	private TextView clinicName;
	private Button dateInList, prevWeek, nextWeek;
	private AccessDBTable table = new AccessDBTable();
	private String clinicOpening, clinicClosing;
	private int appointmentInterval;
	private ArrayList<String> listOfId = new ArrayList<String>();
	private Intent intent;
	private ProgressDialog pd;
	private AccessDBTable db = new AccessDBTable();
	private String response;
	private JSONObject json;
	private String timeBefore, timeAfter;
	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_appointment_calendar);
        listView = (ListView)findViewById(R.id.list);
        clinicName = (TextView)findViewById(R.id.clinic_name);
        dateInList = (Button)findViewById(R.id.date_button);
        prevWeek = (Button)findViewById(R.id.prev_button);
        prevWeek.setOnClickListener(new ButtonClick());
        nextWeek = (Button)findViewById(R.id.next_button);
        nextWeek.setOnClickListener(new ButtonClick());
        
        Log.d("bugs", "appointment map: " + AppointmentSingleton.getInstance().getHashMapofClinicDateID());
        
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
                
        c.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
        Log.d("MYLOG", "Date set to " + c.getTime());
		
		Log.d("MYLOG", "selectOption");
        Log.d("MYLOG", "region: " + serviceOptionSelected);
        Log.d("MYLOG", "hospital: " + clinicSelected);
        Log.d("MYLOG", "week: " + weekSelected);
        Log.d("MYLOG", "day: " + daySelected);
        setAptToListSingle(daySelected);
    }
    private class ButtonClick implements View.OnClickListener {
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.prev_button:
                	Log.d("bugs", "prev daySelected: " + daySelected.toLocaleString());
                	c.setTime(daySelected);
                	Log.d("bugs", "prev day was: " + c.getTime());
                	c.add(Calendar.DAY_OF_YEAR, -7);
                	Log.d("bugs", "prev day is: " + c.getTime());
                	daySelected = c.getTime();
                	setAptToListSingle(c.getTime());
                	//adapter.notifyDataSetChanged();
                	prevWeek.setEnabled(false);
                	nextWeek.setEnabled(false);
                	CountDownTimer prevTimer = new CountDownTimer(250, 250) {						
						@Override
						public void onTick(long millisUntilFinished) {								
						}						
						@Override
						public void onFinish() {	
							prevWeek.setEnabled(true);
							nextWeek.setEnabled(true);
						}
					};
					prevTimer.start();
                	break;
                case R.id.next_button:
                	Log.d("bugs", "next daySelected: " + daySelected.toLocaleString());
                	c.setTime(daySelected);
                	Log.d("bugs", "next day was: " + c.getTime());
                	c.add(Calendar.DAY_OF_YEAR, 7);
                	Log.d("bugs", "next day is: " + c.getTime());
                	daySelected = c.getTime();
                	setAptToListSingle(c.getTime());
                	//adapter.notifyDataSetChanged();
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
                    break;
            }
        }
    }
    public void updateList(){
    	setAptToListSingle(daySelected);
    }
    
    public ArrayList<String> removeZeros(ArrayList<String> badList){
    	if(badList != null){
	    	for(int i = 0; i < badList.size(); i ++){
				if(badList.get(i).equals("0")){
					badList.remove("0");
					Log.d("bugs", "listofID remove zero: " + badList);				
				}
			}
    	}
    	return badList;
    }
    
    public void setAptToListSingle(Date dateSelected){
    	timeSingle = new ArrayList<String>();
    	nameSingle = new ArrayList<String>();
    	gestSingle = new ArrayList<String>();
    	String dateSelectedStr = dfDateOnly.format(dateSelected);
    	Log.d("bugs", "dateSelected: " + dateSelected);
    	Log.d("bugs", "dateSelected: " + dateSelectedStr);
    	
    	dateInList.setText(dfDateOnlyOther.format(dateSelected));
    	Log.d("singleton", "String.valueOf(clinicSelected) " + String.valueOf(clinicSelected));
    	String nameOfClinic = ClinicSingleton.getInstance().getClinicName(String.valueOf(clinicSelected));
    	clinicName.setText(nameOfClinic);
    	Log.d("bugs", "listofID before: " + listOfId);
    	
		listOfId = AppointmentSingleton.getInstance().getListOfIDs(String.valueOf(clinicSelected), dateSelectedStr);
		
		Log.d("bugs", "clinicSelected: " + clinicSelected);
		Log.d("bugs", "clinic name: " + nameOfClinic);			
		
		if (listOfId == null || listOfId.isEmpty()) {
			timeSingle.add("---------");
			nameSingle.add("Free Slot");
			gestSingle.add("---------");
			listOfId = new ArrayList<String>();
			listOfId.add("0");
		} else if(listOfId != null || !listOfId.isEmpty()){			
			Log.d("bugs", "listofID before remove zero: " + listOfId);
			listOfId = removeZeros(listOfId);
			Log.d("bugs", "listofID after remove zero: " + listOfId);
			
			timeSingle = AppointmentSingleton.getInstance().getTime(listOfId);
			Log.d("singleton", "getTime(listOfId)  " + AppointmentSingleton.getInstance().getTime(listOfId));
			nameSingle = AppointmentSingleton.getInstance().getName(listOfId);
			Log.d("singleton", "getName(listOfId)  " + AppointmentSingleton.getInstance().getName(listOfId));
			gestSingle = AppointmentSingleton.getInstance().getGestation(listOfId);
			Log.d("singleton", "getGestation(listOfId)  " + AppointmentSingleton.getInstance().getGestation(listOfId));			

			for (int i = 0; i < timeSingle.size() - 1; i++) {
				String timeFirst;
				String timeSecond;
				Date timeA = null;
				Date timeB = null;
				try {
					timeFirst = timeSingle.get(i);
					timeSecond = timeSingle.get(i + 1);
					timeA = dfTimeOnly.parse(String.valueOf(timeFirst));
					timeB = dfTimeOnly.parse(String.valueOf(timeSecond));
					Log.d("appointment", "timeA: " + timeA);
					Log.d("appointment", "timeB: " + timeB);
					c.setTime(timeA);
					Log.d("appointment", "c.getTime: " + c.getTime());

					c.add(Calendar.MINUTE, appointmentInterval);
					Log.d("appointment", "c.getTime plus interval: " + c.getTime());
					Log.d("appointment", "timeB after timeA plus interval: " + timeB.equals(c.getTime()));
					if (!timeB.equals(c.getTime())) {
						Log.d("appointment", "Free Slot Here");
						timeSingle.add(i + 1, "----------");
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
			Log.d("bugs", "closingMinusInterval: " + closingMinusInterval);
			Log.d("bugs", "last appt: " + (timeSingle.size() - 1));

			if (!timeSingle.get((timeSingle.size() - 1)).equals(closingMinusInterval)) {
				timeSingle.add("---------");
				nameSingle.add("Free Slot");
				gestSingle.add("---------");
				listOfId.add("0");
			}
		}
		
		Log.d("bugs", "timeSingle after populate: " + timeSingle);
		Log.d("bugs", "nameSingle after populate: " + nameSingle);
		Log.d("bugs", "gestSingle after populate: " + gestSingle);
		Log.d("bugs", "listofID after populate: " + listOfId);		
		
		adapter = new ListElementAdapter (AppointmentCalendarActivity.this, timeSingle, nameSingle, gestSingle);
        listView.setAdapter(adapter);
        adapter.notifyDataSetChanged();
        listView.setOnItemClickListener(new OnItemListener());
    }
    private class ListElementAdapter extends BaseAdapter {
		Context context;
		LayoutInflater layoutInflater;
		int position;
		ArrayList<String> aptTime, aptName, aptGest;

		@Override
		public void notifyDataSetChanged() {
			super.notifyDataSetChanged();
		}
		
		public ListElementAdapter(Context context, ArrayList<String> aptTime, 
								  ArrayList<String> aptName, ArrayList<String> aptGest) {
			super();
			Log.d("MYLOG", "daySelected: " + daySelected.toLocaleString());
			Log.d("MYLOG", "List Adapter Called");
			this.context = context;
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
			this.position = position;
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
			Log.d("bugs", "listOfId in onClick: " + listOfId);
			Log.d("bugs", "appointment id: " + listOfId.get(position));	
			if(listOfId.get(position).equals("0")){
				intent = new Intent(AppointmentCalendarActivity.this, CreateAppointmentActivity.class);
				if(listOfId.size() == 1){
					myCalendar.setTime(openingAsDate);
					myCalendar.add(Calendar.MINUTE, - appointmentInterval);
					
					intent.putExtra("timeBefore", dfTimeOnly.format(myCalendar.getTime()));
					intent.putExtra("timeAfter", clinicClosing);
					startActivity(intent);
				} else if(listOfId.size() > 0 && position == 0){
					myCalendar.setTime(openingAsDate);
					myCalendar.add(Calendar.MINUTE, - appointmentInterval);
					intent.putExtra("timeBefore", dfTimeOnly.format(myCalendar.getTime()));
					
					timeAfter = AppointmentSingleton.getInstance().getTime(listOfId.get(position + 1));
					intent.putExtra("timeAfter", timeAfter);
				} else if(listOfId.size() > 0 && position == listOfId.size() - 1){
					timeBefore = AppointmentSingleton.getInstance().getTime(listOfId.get(position - 1));
					Log.d("postAppointment", "timeBefore: " + timeBefore);
					
					intent.putExtra("timeBefore", timeBefore);
					intent.putExtra("timeAfter", clinicClosing);					
				} else if(listOfId.size() > 0 && listOfId.get(position - 1) != null && listOfId.get(position + 1) != null){
					timeBefore = AppointmentSingleton.getInstance().getTime(listOfId.get(position - 1));
					timeAfter = AppointmentSingleton.getInstance().getTime(listOfId.get(position + 1));
					
					Log.d("postAppointment", "timeBefore: " + timeBefore);
					Log.d("postAppointment", "timeAfter: " + timeAfter);
					intent.putExtra("timeBefore", timeBefore);
					intent.putExtra("timeAfter", timeAfter);
				}
				intent.putExtra("clinicID", String.valueOf(clinicSelected));
				Log.d("postAppointment", "String.valueOf(clinicSelected) "+ String.valueOf(clinicSelected));
				startActivity(intent);
				
			} else {
				String nameFromDB = AppointmentSingleton.getInstance().getName(listOfId.get(position));
				String timeFromDB = AppointmentSingleton.getInstance().getTime(listOfId.get(position));
				String dateFromDB = AppointmentSingleton.getInstance().getDate(listOfId.get(position));
				
				try {
					dateFromDB = dfDateWithMonthName.format(dfDateOnly.parse(dateFromDB));
				} catch (ParseException e) {
					e.printStackTrace();
				}
				
				String clinicIDFromDB = AppointmentSingleton.getInstance().getClinicID(listOfId.get(position));	
				String clinicNameFromDB = ClinicSingleton.getInstance().getClinicName(clinicIDFromDB);
				int durationFromDB = Integer.parseInt(ClinicSingleton.getInstance().getAppointmentInterval(clinicIDFromDB));
				String serviceUserID = AppointmentSingleton.getInstance().getServiceUserID(listOfId.get(position));
				
				Log.d("singleton", "db string: " + "service_users" + "/" + serviceUserID);
				new LongOperation(AppointmentCalendarActivity.this).execute("service_users" + "/" + serviceUserID);
				
				intent = new Intent(AppointmentCalendarActivity.this, ConfirmAppointmentActivity.class);
				intent.putExtra("name", nameFromDB);
				intent.putExtra("time", timeFromDB);
				intent.putExtra("date", dateFromDB);
				intent.putExtra("clinicName", clinicNameFromDB);
				intent.putExtra("duration", String.valueOf(durationFromDB));
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