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

import Enums.HospitalEnum;
import Enums.RegionEnum;
import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import connecttodb.DateSorterThing;
import connecttodb.SetDateToHashMap;

public class AppointmentCalendarActivity extends MenuInheritActivity {
	private static int regionSelected, hospitalSelected, weekSelected, daySelected;
	private Date day = null;
	private DateFormat df = new SimpleDateFormat("yyyy-MM-dd - HH:mm:ss", Locale.getDefault());
	private DateFormat dfDateOnly = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
	private DateFormat dfTimeOnly = new SimpleDateFormat("HH:mm:ss", Locale.getDefault());
	
	private ArrayList<JSONObject> thing = new ArrayList<JSONObject>();
	private ArrayList<String> aptList = new ArrayList<String>();
	
	private Calendar c = Calendar.getInstance();
	private DateSorterThing ds = new DateSorterThing();
	private SetDateToHashMap getDates = new SetDateToHashMap();
	private ListView listView;
	private Object time, name, gestation;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.list_layout);
        listView = (ListView)findViewById(R.id.list);
        
        c.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
        Log.d("MYLOG", "Date set to " + c.getTime());
		
		Log.d("MYLOG", "selectOption");
        Log.d("MYLOG", "region1: " + regionSelected);
        Log.d("MYLOG", "hospital1: " + hospitalSelected);
		setOptionsSelected(regionSelected, hospitalSelected, weekSelected, daySelected);
		new LongOperation().execute((String[]) null);
        Log.d("MYLOG", "banananananana");
    }
    public void doTheRoar(ArrayList<JSONObject> thing){
        try {
            for (int i = 0; i < thing.size(); i++) {
                Integer clinic_id = (((JSONObject) ((JSONObject) thing.get(i)).get("appointments")).getInt("clinic_id"));

                if (clinic_id == hospitalSelected) {
                    name = ((JSONObject) ((JSONObject) ((JSONObject) thing.get(i)).get("appointments")).get("service_user")).get("name");
                    time = ((JSONObject) ((JSONObject) thing.get(i)).get("appointments")).get("time");
                    gestation = ((JSONObject) ((JSONObject) ((JSONObject) thing.get(i)).get("appointments")).get("service_user")).get("gestation");
                    //get closing time
                    //check if last appointment is 15 minutes before closing time

                    if(i == 0){
                        aptList.add(time + " --- " + name + " --- " + gestation);
                    } else {
                        Object timeFirst = ((JSONObject) ((JSONObject) thing.get(i-1)).get("appointments")).get("time");
                        Date timeAtFirst = dfTimeOnly.parse(String.valueOf(timeFirst));
                        Date timeAtSecond = dfTimeOnly.parse(String.valueOf(time));
                        c.setTime(timeAtFirst);
                        c.add(Calendar.MINUTE, 15);
                        if(!(c.getTime().equals(timeAtSecond))){
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
    public void setDaySelected(int daySelected){
    	AppointmentCalendarActivity.daySelected = daySelected;
    }
    public void setOptionsSelected(int regionSelected, int hospitalSelected, int weekSelected, int daySelected){    	
    	switch (regionSelected){
    	case 0:
    		break;
    	case 1:
    		RegionEnum domino_dublin = RegionEnum.DOMINO_DUBLIN;
    		Log.d("MYLOG", domino_dublin.toString() + " selected");
    		switch(hospitalSelected){
        	case 0:
        		break;
        	case 1:
        		HospitalEnum nmh_opd = HospitalEnum.NMH_OPD;
        		Log.d("MYLOG", nmh_opd.toString() + " selected");
        		break;
        	case 2:
        		HospitalEnum leopardstown = HospitalEnum.LEOPARDSTOWN;
        		Log.d("MYLOG", leopardstown.toString() + " selected");
        		break;
        	case 3:
        		Log.d("MYLOG", "Dun Laoghaire selected");
        		break;
        	case 4:
        		Log.d("MYLOG", "Churchtown selected");
        		break;
        	case 5:
        		Log.d("MYLOG", "NMH selected");
        		break;
        	}
    		break;
    	case 2:
    		Log.d("MYLOG", "Domino Wicklow selected");
    		switch(hospitalSelected){
        	case 0:
        		break;
        	case 1:
        		Log.d("MYLOG", "Greystones (Monday) selected");
        		break;
        	case 2:
        		Log.d("MYLOG", "Greystones (Tuesday) selected");
        		break;
        	case 3:
        		Log.d("MYLOG", "Kilmacanogue selected");
        		break;
        	case 4:
        		Log.d("MYLOG", "Home Visits selected");
        		break;
        	}
    		break;
    	case 3:
    		Log.d("MYLOG", "ETH Dublin selected");
    		switch(hospitalSelected){
        	case 0:
        		break;
        	case 1:
        		Log.d("MYLOG", "Ballinteer selected");
        		break;
        	case 2:
        		Log.d("MYLOG", "Dun Laoghaire selected");
        		break;
        	}
    		break;
    	case 4:
    		Log.d("MYLOG", "ETH Wicklow selected");
    		switch(hospitalSelected){
        	case 0:
        		break;
        	case 1:
        		Log.d("MYLOG", "Ballinteer selected");
        		break;
        	case 2:
        		Log.d("MYLOG", "Dun Laoghaire selected");
        		break;
        	}
    		break;
    	case 5:
    		Log.d("MYLOG", "Satellite selected");
    		switch(hospitalSelected){
        	case 0:
        		break;
        	case 1:
        		Log.d("MYLOG", "Greystones selected");
        		break;
        	case 2:
        		Log.d("MYLOG", "Arklow selected");
        		break;
        	case 3:
        		Log.d("MYLOG", "Newtownmountkennedy selected");
        		break;
        	case 4:
        		Log.d("MYLOG", "Bray selected");
        		break;
        	}
    		break;
    	}
    	switch(weekSelected){
		case 0:
			break;
		case 1:
			Log.d("MYLOG", "Week 1 selected");
			break;
		case 2:
			Log.d("MYLOG", "Week 2 selected");
			c.add(Calendar.DAY_OF_YEAR, 7);
			Log.d("MYLOG", "Plus 7 days is: " + c.getTime());
			break;
		case 3:
    		Log.d("MYLOG", "Week 3 selected");
			c.add(Calendar.DAY_OF_YEAR, (c.getFirstDayOfWeek() + 14));
			Log.d("MYLOG", "Plus 14 days is: " + c.getTime());
    		break;
    	case 4:
    		Log.d("MYLOG", "Week 4 selected");
    		c.add(Calendar.DAY_OF_YEAR, (c.getFirstDayOfWeek() + 21));
			Log.d("MYLOG", "Plus 21 days is: " + c.getTime());				
    		break;
    	case 5:
    		Log.d("MYLOG", "Week 5 selected");
    		c.add(Calendar.DAY_OF_YEAR, (c.getFirstDayOfWeek() + 28));
			Log.d("MYLOG", "Plus 21 days is: " + c.getTime());	
    		break;
    	case 6:
    		Log.d("MYLOG", "Week 6 selected");
    		c.add(Calendar.DAY_OF_YEAR, (c.getFirstDayOfWeek() + 35));
			Log.d("MYLOG", "Plus 21 days is: " + c.getTime());	
    		break;
    	case 7:
    		Log.d("MYLOG", "Week 7 selected");
    		c.add(Calendar.DAY_OF_YEAR, (c.getFirstDayOfWeek() + 42));
			Log.d("MYLOG", "Plus 21 days is: " + c.getTime());	
    		break;
    	}
    	switch(daySelected){
    	case 0:
    		break;
    	case 1:
    		Log.d("MYLOG", "Monday selected");
    		c.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
    		Log.d("MYLOG", "" + c.getTime());
			day = c.getTime();  
			Log.d("MYLOG", "day1: " + day);
    		break;
    	case 2:
    		Log.d("MYLOG", "Tuesday selected");
    		c.set(Calendar.DAY_OF_WEEK, Calendar.TUESDAY);
    		Log.d("MYLOG", "" + c.getTime());
			day = c.getTime();  
			Log.d("MYLOG", "day1: " + day);
    		break;
    	case 3:
    		Log.d("MYLOG", "Wednesday selected");
    		c.set(Calendar.DAY_OF_WEEK, Calendar.WEDNESDAY);
    		Log.d("MYLOG", "" + c.getTime());
			day = c.getTime();  
			Log.d("MYLOG", "day1: " + day);
    		break;
    	case 4:
    		Log.d("MYLOG", "Thursday selected");
    		c.set(Calendar.DAY_OF_WEEK, Calendar.THURSDAY);
    		Log.d("MYLOG", "" + c.getTime());
			day = c.getTime();  
			Log.d("MYLOG", "day1: " + day);
    		break;
    	case 5:
    		Log.d("MYLOG", "Friday selected");
    		c.set(Calendar.DAY_OF_WEEK, Calendar.FRIDAY);
    		Log.d("MYLOG", "" + c.getTime());
			day = c.getTime();  
			Log.d("MYLOG", "day1: " + day);
    		break;
    	case 6:
    		Log.d("MYLOG", "Saturday selected");
    		c.set(Calendar.DAY_OF_WEEK, Calendar.SATURDAY);
    		Log.d("MYLOG", "" + c.getTime());
			day = c.getTime();  
			Log.d("MYLOG", "day1: " + day);
    		break;
    	case 7:
    		Log.d("MYLOG", "Sunday selected");
    		c.set(Calendar.DAY_OF_WEEK, Calendar.SUNDAY);
    		Log.d("MYLOG", "" + c.getTime());
			day = c.getTime();  
			Log.d("MYLOG", "day1: " + day);
    		break;
    	}
    }
    public class LongOperation extends AsyncTask<String, Void, ArrayList<JSONObject>> {    
    	Date dayWanted;
		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			dayWanted = AppointmentCalendarActivity.this.day;  
		}
		protected ArrayList<JSONObject> doInBackground(String... params) {
			String dayWantedStr = dfDateOnly.format(dayWanted);
			thing = getDates.setDateToHaspMap(Login_model.getToken(), dayWantedStr);
			return thing;
		}
		@Override
		protected void onProgressUpdate(Void... values) {
		}
		@Override
        protected void onPostExecute(ArrayList<JSONObject> result) {	
			super.onPostExecute(result);
			Log.d("MYLOG", "result: " + result);
            doTheRoar(result);
		}
	}
}