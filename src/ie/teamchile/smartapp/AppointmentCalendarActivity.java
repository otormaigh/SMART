package ie.teamchile.smartapp;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import models.Login_model;

import org.json.JSONException;
import org.json.JSONObject;

import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import connecttodb.DateSorter;
import connecttodb.SetDateToHashMap;

public class AppointmentCalendarActivity extends MenuInheritActivity {
	private static int regionSelected, hospitalSelected, weekSelected;
	private static Date daySelected;
	private Date day = null;
	private DateFormat df = new SimpleDateFormat("yyyy-MM-dd - HH:mm:ss", Locale.getDefault());
	private DateFormat dfDateOnly = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
	private DateFormat dfTimeOnly = new SimpleDateFormat("HH:mm:ss", Locale.getDefault());	
	private ArrayList<JSONObject> aptsAtDate = new ArrayList<JSONObject>();
	private ArrayList<String> aptList = new ArrayList<String>();	
	private Calendar c = Calendar.getInstance();
	private DateSorter ds = new DateSorter();
	private SetDateToHashMap getDates = new SetDateToHashMap();
	private ListView listView;
	private Object time, name, gestation;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_appointment_calendar);
        listView = (ListView)findViewById(R.id.list);        
/*      ArrayAdapter<String> listItem = new ArrayAdapter<String>(this,
                android.R.layout.activity_list_item, android.R.id.list);
        listView.setAdapter(listItem);*/        
        
        c.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
        Log.d("MYLOG", "Date set to " + c.getTime());
		
		Log.d("MYLOG", "selectOption");
        Log.d("MYLOG", "region: " + regionSelected);
        Log.d("MYLOG", "hospital: " + hospitalSelected);
        Log.d("MYLOG", "week: " + weekSelected);
        Log.d("MYLOG", "day: " + daySelected);
		new LongOperation().execute((String[]) null);
    }
    public void setAptToList(ArrayList<JSONObject> aptsAtDate){
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
                        aptList.add(time + " --- " + name + " --- " + gestation);
                    } else {
                        Object timeFirst = ((JSONObject) ((JSONObject) aptsAtDate.get(i-1)).get("appointments")).get("time");
                        Date timeA = dfTimeOnly.parse(String.valueOf(timeFirst));
                        Date timeB = dfTimeOnly.parse(String.valueOf(time));
                        c.setTime(timeA);
                        c.add(Calendar.MINUTE, 15);
                        if(!(c.getTime().equals(timeB))){
                            aptList.add("---------- Free Slot ----------");
                            aptList.add(time + " --- " + name + " --- " + gestation);
                        } else {
                            aptList.add(time + " --- " + name + " --- " + gestation);
                        }
                    }
                }
            }
            ArrayAdapter<String> adapter = new ArrayAdapter<String>(AppointmentCalendarActivity.this, R.layout.list_rows, R.id.date, aptList);
            listView.setAdapter(adapter);
            listView.setTextFilterEnabled(true);
        } catch (JSONException | ParseException e) {
            e.printStackTrace();
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
    public class LongOperation extends AsyncTask<String, Void, ArrayList<JSONObject>> {    
    	//Date dayWanted;
		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			//dayWanted = AppointmentCalendarActivity.this.daySelected;
		}
		protected ArrayList<JSONObject> doInBackground(String... params) {
			String daySelectedStr = dfDateOnly.format(daySelected);
			aptsAtDate = getDates.setDateToHaspMap(Login_model.getToken(), daySelectedStr);
			return aptsAtDate;
		}
		@Override
		protected void onProgressUpdate(Void... values) {
		}
		@Override
        protected void onPostExecute(ArrayList<JSONObject> result) {	
			super.onPostExecute(result);
			Log.d("MYLOG", "result: " + result);
			setAptToList(result);
		}
	}
}