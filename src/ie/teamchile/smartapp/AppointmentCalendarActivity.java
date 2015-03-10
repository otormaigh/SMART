package ie.teamchile.smartapp;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import org.json.JSONException;
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
import android.test.ServiceTestCase;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;
import connecttodb.AccessDBTable;
import connecttodb.DateSorter;
import connecttodb.SetDateToHashMap;

public class AppointmentCalendarActivity extends MenuInheritActivity {
	private static int regionSelected;
	protected static int hospitalSelected;
	private static int weekSelected;
	protected static Date daySelected;
	private Date day = null, openingAsDate, closingAsDate;
	private DateFormat df = new SimpleDateFormat("yyyy-MM-dd - HH:mm:ss", Locale.getDefault());
	private DateFormat dfDateOnly = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
	private DateFormat dfDateOnlyOther = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
	private DateFormat dfTimeOnly = new SimpleDateFormat("HH:mm:ss", Locale.getDefault());
	private DateFormat dfDateWithMonthName = new SimpleDateFormat("dd MMM yyyy", Locale.getDefault());	
	private ArrayList<JSONObject> aptsAtDate = new ArrayList<JSONObject>();
	private ArrayList<String> aptList = new ArrayList<String>();	
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
	private ArrayList<String> listOfId;
	private Intent intent;
	private ProgressDialog pd;
	private AccessDBTable db = new AccessDBTable();
	private String response;
	private JSONObject jsonNew;
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
        
        clinicOpening = ClinicSingleton.getInstance().getOpeningTime(String.valueOf(hospitalSelected));
		clinicClosing = ClinicSingleton.getInstance().getClosingTime(String.valueOf(hospitalSelected));
		appointmentInterval = Integer.parseInt(ClinicSingleton.getInstance().getAppointmentInterval(String.valueOf(hospitalSelected)));
		
		try {
			openingAsDate = dfTimeOnly.parse(String.valueOf(clinicOpening));
			closingAsDate = dfTimeOnly.parse(String.valueOf(clinicClosing));
		} catch (ParseException e) {
			e.printStackTrace();
		}
                
        c.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
        Log.d("MYLOG", "Date set to " + c.getTime());
		
		Log.d("MYLOG", "selectOption");
        Log.d("MYLOG", "region: " + regionSelected);
        Log.d("MYLOG", "hospital: " + hospitalSelected);
        Log.d("MYLOG", "week: " + weekSelected);
        Log.d("MYLOG", "day: " + daySelected);
        setAptToListSingle(daySelected);
    }
    private class ButtonClick implements View.OnClickListener {
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.prev_button:
                	Log.d("MYLOG", "daySelected: " + daySelected.toLocaleString());
                	c.setTime(daySelected);
                	Log.d("MYLOG", "day was: " + c.getTime());
                	c.add(Calendar.DAY_OF_YEAR, -7);
                	Log.d("MYLOG", "day is: " + c.getTime());
                	daySelected = c.getTime();
                	setAptToListSingle(daySelected);
                	adapter.notifyDataSetChanged();
                	prevWeek.setEnabled(false);
                	CountDownTimer prevTimer = new CountDownTimer(1000, 1000) {						
						@Override
						public void onTick(long millisUntilFinished) {								
						}						
						@Override
						public void onFinish() {	
							prevWeek.setEnabled(true);
						}
					}.start();
                    break;
                case R.id.next_button:
                	Log.d("MYLOG", "daySelected: " + daySelected.toLocaleString());
                	c.setTime(daySelected);
                	Log.d("MYLOG", "day was: " + c.getTime());
                	c.add(Calendar.DAY_OF_YEAR, 7);
                	Log.d("MYLOG", "day is: " + c.getTime());
                	daySelected = c.getTime();
                	setAptToListSingle(daySelected);
                	adapter.notifyDataSetChanged();
                	nextWeek.setEnabled(false);
                	CountDownTimer nextTimer = new CountDownTimer(1000, 1000) {						
						@Override
						public void onTick(long millisUntilFinished) {								
						}						
						@Override
						public void onFinish() {	
							nextWeek.setEnabled(true);
						}
					}.start();
                    break;
            }
        }
    }
/*    public void updateList(){
    	Log.d("MYLOG", "daySelected: " + daySelected.toLocaleString());
    	c.setTime(daySelected);
    	Log.d("MYLOG", "day was: " + c.getTime());
    	c.add(Calendar.DAY_OF_YEAR, 7);
    	Log.d("MYLOG", "day is: " + c.getTime());
    	daySelected = c.getTime();
    	adapter.notifyDataSetChanged();
    	setAptToListSingle(daySelected);
    }*/
    public void setAptToListSingle(Date daySelected){
    	ArrayList<String> timeSingle = new ArrayList<String>();
    	ArrayList<String> nameSingle = new ArrayList<String>();
    	ArrayList<String> gestSingle = new ArrayList<String>();
    	//listOfId = new ArrayList<String>();
    	String daySelectedStr = dfDateOnly.format(daySelected); 
    	
    	dateInList.setText(dfDateOnlyOther.format(daySelected));
    	Log.d("singleton", "String.valueOf(hospitalSelected) " + String.valueOf(hospitalSelected));
    	String nameOfClinic = ClinicSingleton.getInstance().getClinicName(String.valueOf(hospitalSelected));
    	clinicName.setText(nameOfClinic);
		listOfId = AppointmentSingleton.getInstance().getListOfIDs(String.valueOf(hospitalSelected), daySelectedStr);
		
		if (listOfId == null || listOfId.isEmpty()) {
			timeSingle.add("---------");
			nameSingle.add("Free Slot");
			gestSingle.add("---------");
			listOfId = new ArrayList<String>();
			listOfId.add("0");
		} else if(listOfId != null || !listOfId.isEmpty()){
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

			if (!timeSingle.get((timeSingle.size() - 1)).equals(clinicClosing)) {
				timeSingle.add("---------");
				nameSingle.add("Free Slot");
				gestSingle.add("---------");
				listOfId.add("0");
			}
		}
		adapter = new ListElementAdapter (AppointmentCalendarActivity.this, timeSingle, nameSingle, gestSingle);
		
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new OnItemListener());
    }
    private class ListElementAdapter extends BaseAdapter {
		Context context;
		LayoutInflater layoutInflater;
		int position;
		ArrayList<String> aptTime, aptName, aptGest, idList;

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
			this.idList = idList;
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
			
			//prevWeek.setEnabled(true); 
	    	//nextWeek.setEnabled(true); 	    	
			return convertView;
		}
	}
    
    private class OnItemListener implements OnItemClickListener {
		@Override
		public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3) {
			Log.d("appointmentClick", "appointment id: " + listOfId.get(position));	
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
				intent.putExtra("clinicID", String.valueOf(hospitalSelected));
				Log.d("postAppointment", "String.valueOf(hospitalSelected) "+ String.valueOf(hospitalSelected));
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
			try {
				response = db.accessDB(params[0]);
				jsonNew = new JSONObject(response);
			} catch (JSONException e) {
				e.printStackTrace();
			}
			Log.d("singleton", "query = " + jsonNew);
			return jsonNew;
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
    public void setRegionSelected(int regionSelected){
    	AppointmentCalendarActivity.regionSelected = regionSelected;
    }
    public void setHospitalSelected(int hospitalSelected){
    	AppointmentCalendarActivity.hospitalSelected = hospitalSelected;
    }
    public void setWeekSelected(int weekSelected){
    	AppointmentCalendarActivity.weekSelected = weekSelected;
    }
    public void setDaySelected(Date daySelected){
    	AppointmentCalendarActivity.daySelected = daySelected;
    }
}