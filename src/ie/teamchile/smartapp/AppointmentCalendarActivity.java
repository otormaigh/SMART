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
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import connecttodb.AccessDBTable;
import connecttodb.DateSorter;
import connecttodb.SetDateToHashMap;

public class AppointmentCalendarActivity extends MenuInheritActivity {
	private static int regionSelected, hospitalSelected, weekSelected;
	private static Date daySelected;
	private Date day = null;
	private DateFormat df = new SimpleDateFormat("yyyy-MM-dd - HH:mm:ss", Locale.getDefault());
	private DateFormat dfDateOnly = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
	private DateFormat dfDateOnlyOther = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
	private DateFormat dfTimeOnly = new SimpleDateFormat("HH:mm:ss", Locale.getDefault());	
	private ArrayList<JSONObject> aptsAtDate = new ArrayList<JSONObject>();
	private ArrayList<String> aptList = new ArrayList<String>();	
	private Calendar c = Calendar.getInstance();
	private DateSorter ds = new DateSorter();
	private SetDateToHashMap getDates = new SetDateToHashMap();
	private ListView listView;
	private Object time, name, gestation;
	private BaseAdapter adapter;
	private TextView clinicName;
	private Button dateInList, prevWeek, nextWeek;
	private AccessDBTable table = new AccessDBTable();
	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_appointment_calendar);
        listView = (ListView)findViewById(R.id.list);
        clinicName = (TextView)findViewById(R.id.clinic_name);
        dateInList = (Button)findViewById(R.id.date_button);
        //dateInList.setText(dfDateOnlyOther.format(daySelected));
        prevWeek = (Button)findViewById(R.id.prev_button);
        prevWeek.setOnClickListener(new ButtonClick());
        nextWeek = (Button)findViewById(R.id.next_button);
        nextWeek.setOnClickListener(new ButtonClick());
                
        c.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
        Log.d("MYLOG", "Date set to " + c.getTime());
		
		Log.d("MYLOG", "selectOption");
        Log.d("MYLOG", "region: " + regionSelected);
        Log.d("MYLOG", "hospital: " + hospitalSelected);
        Log.d("MYLOG", "week: " + weekSelected);
        Log.d("MYLOG", "day: " + daySelected);
		//new LongOperation().execute((String[]) null);
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
                	listView.setAdapter(null);
                	adapter.notifyDataSetChanged();
                	setAptToListSingle(daySelected);
                    break;
                case R.id.next_button:
                	Log.d("MYLOG", "daySelected: " + daySelected.toLocaleString());
                	c.setTime(daySelected);
                	Log.d("MYLOG", "day was: " + c.getTime());
                	c.add(Calendar.DAY_OF_YEAR, 7);
                	Log.d("MYLOG", "day is: " + c.getTime());
                	daySelected = c.getTime();
                	listView.setAdapter(null);
                	adapter.notifyDataSetChanged();
                	setAptToListSingle(daySelected);
                    break;
            }
        }
    }
    public void setAptToListSingle(Date daySelected){
    	ArrayList<String> timeSingle = new ArrayList<String>();
    	ArrayList<String> nameSingle = new ArrayList<String>();
    	ArrayList<String> gestSingle = new ArrayList<String>();
    	String daySelectedStr = dfDateOnly.format(daySelected);
    	
    	dateInList.setText(dfDateOnlyOther.format(daySelected));
    	Log.d("singleton", "String.valueOf(hospitalSelected) " + String.valueOf(hospitalSelected));
    	String nameOfClinic = ClinicSingleton.getSingletonIntance().getName(String.valueOf(hospitalSelected));
    	clinicName.setText(nameOfClinic);
    	String openingHours = ClinicSingleton.getSingletonIntance().getOpeningHours(String.valueOf(hospitalSelected));
    	String closingHours = ClinicSingleton.getSingletonIntance().getClosingHours(String.valueOf(hospitalSelected));
    	
    	Log.d("singleton", "opening time: " + openingHours);
    	Log.d("singleton", "closing time: " + closingHours);
        
		Log.d("singleton", "getHashMapofDateID: " + AppointmentSingleton.getSingletonIntance().getHashMapofDateID());
		Log.d("singleton", "getHashMapofDateID: " + AppointmentSingleton.getSingletonIntance().getHashMapofIdAppt());
		ArrayList<String> listOfId = AppointmentSingleton.getSingletonIntance().getIdAtDate(daySelectedStr);		
		
		timeSingle = AppointmentSingleton.getSingletonIntance().getTime(listOfId);
		Log.d("singleton", "getTime(listOfId)  " + AppointmentSingleton.getSingletonIntance().getTime(listOfId));
		nameSingle = AppointmentSingleton.getSingletonIntance().getName(listOfId);
		Log.d("singleton", "getName(listOfId)  " + AppointmentSingleton.getSingletonIntance().getName(listOfId));
		gestSingle = AppointmentSingleton.getSingletonIntance().getGestation(listOfId);
		Log.d("singleton", "getGestation(listOfId)  " + AppointmentSingleton.getSingletonIntance().getGestation(listOfId));		
		
		AppointmentSingleton.getSingletonIntance().getAppointmentDetails(listOfId);
		Log.d("singleton", "getAppointmentDetails(listOfId)  " + AppointmentSingleton.getSingletonIntance().getAppointmentDetails(listOfId));
        
		adapter = new ListElementAdapter (AppointmentCalendarActivity.this, timeSingle, nameSingle, gestSingle);
		
        listView.setAdapter(adapter);
    }
    /*public void setAptToList(ArrayList<JSONObject> aptsAtDate){
    	ArrayList<String> timeList = new ArrayList<String>();
    	ArrayList<String> nameList = new ArrayList<String>();
    	ArrayList<String> gestList = new ArrayList<String>();
    	Log.d("MYLOG", "timeList without data: " + timeList);
        Log.d("MYLOG", "nameList without data: " + nameList);
        Log.d("MYLOG", "gestList without data: " + gestList);
        try {
            for (int i = 0; i < aptsAtDate.size(); i++) {
                Integer clinic_id = (((JSONObject) ((JSONObject) aptsAtDate.get(i)).get("appointments")).getInt("clinic_id"));
                if (clinic_id == hospitalSelected) {
                    name = ((JSONObject) ((JSONObject) ((JSONObject) aptsAtDate.get(i)).get("appointments")).get("service_user")).get("name");
                    time = ((JSONObject) ((JSONObject) aptsAtDate.get(i)).get("appointments")).get("time");
                    gestation = ((JSONObject) ((JSONObject) ((JSONObject) aptsAtDate.get(i)).get("appointments")).get("service_user")).get("gestation");
                    //get closing time
                    //check if last appointment is 15 minutes before closing time
                    //if yes put in free slot after appointment

                    if(i == 0){
                    	timeList.add("----------");
                    	nameList.add("Free Slot");
                    	gestList.add("----------");
                    } else {
                        Object timeFirst = ((JSONObject) ((JSONObject) aptsAtDate.get(i-1)).get("appointments")).get("time");
                        Date timeA = dfTimeOnly.parse(String.valueOf(timeFirst));
                        Date timeB = dfTimeOnly.parse(String.valueOf(time));
                        c.setTime(timeA);
                        c.add(Calendar.MINUTE, 15);
                        if(!(c.getTime().equals(timeB))){
                        	timeList.add("----------");
                        	nameList.add("Free Slot");
                        	gestList.add("----------");
                        	
                        	timeList.add(time.toString());
                        	nameList.add(name.toString());
                        	gestList.add(gestation.toString());                        	
                        } else {                        	
                        	timeList.add(time.toString());
                        	nameList.add(name.toString());
                        	gestList.add(gestation.toString());
                        }
                    }
                }
            }
            Log.d("MYLOG", "timeList with data: " + timeList);
            Log.d("MYLOG", "nameList with data: " + nameList);
            Log.d("MYLOG", "gestList with data: " + gestList);
            ArrayAdapter<String> adapter = new ArrayAdapter<String>(AppointmentCalendarActivity.this, R.layout.list_rows, R.id.date, aptList);
            listView.setAdapter(adapter);
            listView.setTextFilterEnabled(true);
            
            //listView = (ListView) findViewById(R.id.list);
            //adapter = new ListElementAdapter (AppointmentCalendarActivity.this, timeList, nameList, gestList);
            
            ArrayList<String> timeSingle = new ArrayList<String>();
        	ArrayList<String> nameSingle = new ArrayList<String>();
        	ArrayList<String> gestSingle = new ArrayList<String>();
        	String daySelectedStr = dfDateOnly.format(daySelected);
            
			Log.d("singleton", "getHashMapofDateID: " + AppointmentSingleton.getSingletonIntance().getHashMapofDateID());
			Log.d("singleton", "getHashMapofDateID: " + AppointmentSingleton.getSingletonIntance().getHashMapofIdAppt());
			ArrayList<String> listOfId = AppointmentSingleton.getSingletonIntance().getIdAtDate(daySelectedStr);
			
			
			timeSingle = AppointmentSingleton.getSingletonIntance().getTime(listOfId);
			Log.d("singleton", "getTime(listOfId)  " + AppointmentSingleton.getSingletonIntance().getTime(listOfId));
			nameSingle = AppointmentSingleton.getSingletonIntance().getName(listOfId);
			Log.d("singleton", "getName(listOfId)  " + AppointmentSingleton.getSingletonIntance().getName(listOfId));
			gestSingle = AppointmentSingleton.getSingletonIntance().getGestation(listOfId);
			Log.d("singleton", "getGestation(listOfId)  " + AppointmentSingleton.getSingletonIntance().getGestation(listOfId));
			
			
			AppointmentSingleton.getSingletonIntance().getAppointmentDetails(listOfId);
			Log.d("singleton", "getAppointmentDetails(listOfId)  " + AppointmentSingleton.getSingletonIntance().getAppointmentDetails(listOfId));
            
			adapter = new ListElementAdapter (AppointmentCalendarActivity.this, timeSingle, nameSingle, gestSingle);
			
            listView.setAdapter(adapter);
            //listView.setTextFilterEnabled(true);            
        } catch (JSONException | ParseException e) {
            e.printStackTrace();
        }
    }*/
    private class ListElementAdapter extends BaseAdapter {
		Context context;
		LayoutInflater layoutInflater;
		int position;
		ArrayList<String> aptTime, aptName, aptGest;

		@Override
		public void notifyDataSetChanged() {
			super.notifyDataSetChanged();
		}
		public ListElementAdapter(Context context, ArrayList<String> aptTime, ArrayList<String> aptName, ArrayList<String> aptGest) {
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
        this.daySelected = daySelected;
    }
    /*public class LongOperation extends AsyncTask<String, Void, ArrayList<JSONObject>> {    
    	//Date dayWanted;
    	String clinicNameFromDB = null;
    	String namefromDB = null;
    	String fromDB = null;
    	JSONObject jsonFromDB = null;
    	String daySelectedStr = null;
    	DateSorter ds = new DateSorter();
    	SetDateToHashMap getDates = new SetDateToHashMap();
    	private AccessDBTable table = new AccessDBTable();
		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			//dayWanted = AppointmentCalendarActivity.this.daySelected;
		}
		protected ArrayList<JSONObject> doInBackground(String... params) {
			try {
				fromDB = table.accessDB(Login_model.getToken(), "clinics/" + hospitalSelected);
				jsonFromDB = new JSONObject(fromDB);				
				namefromDB = jsonFromDB.getJSONArray("clinics").getJSONObject(0).getString("name");
				
				Log.d("MYLOG", "-------------------------");
				Log.d("MYLOG", "clinicName before: " + namefromDB);
				Log.d("MYLOG", "-------------------------");
			} catch (JSONException e) {
				e.printStackTrace();
			}			
			daySelectedStr = dfDateOnly.format(daySelected);
			aptsAtDate = getDates.setDateToHaspMap(Login_model.getToken(), daySelectedStr);
			AppointmentSingleton.getSingletonIntance().updateLocal();	
			return aptsAtDate;
		}
		@Override
		protected void onProgressUpdate(Void... values) {
		}
		@Override
        protected void onPostExecute(ArrayList<JSONObject> result) {	
			super.onPostExecute(result);
			clinicName.setText(namefromDB);
			Log.d("MYLOG", "-------------------------");
			Log.d("MYLOG", "clinicName after: " + namefromDB);
			Log.d("MYLOG", "-------------------------");
			Log.d("MYLOG", "result: " + result);
			setAptToList(result);
		}
	}*/
}