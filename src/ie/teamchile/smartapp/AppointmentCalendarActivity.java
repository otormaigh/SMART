package ie.teamchile.smartapp;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import org.json.JSONException;
import org.json.JSONObject;

import Enums.HospitalEnum;
import Enums.RegionEnum;
import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import connecttodb.DateSorterThing;

public class AppointmentCalendarActivity extends Activity {
	private TextView appointmentInfo;
	private static int regionSelected, hospitalSelected, weekSelected, daySelected;
	private int week_1, week_2, week_3, week_4, week_5, week_6, week_7;
	private String textPopulate;
	DateFormat df = new SimpleDateFormat("yyyy-MM-dd - HH:mm:ss");
	ArrayList<JSONObject> thing1 = new ArrayList<JSONObject>();
	ArrayList<JSONObject> thing2 = new ArrayList<JSONObject>();
	Calendar c = Calendar.getInstance();
	DateSorterThing ds = new DateSorterThing();

	Date startDate = null, endDate = null;
	Date day = null;
	Date thingDate;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_appointment_calendar);
        appointmentInfo = (TextView)findViewById(R.id.appointment_info);
        
        c.set(Calendar.YEAR, 2014);
		c.set(Calendar.MONTH, Calendar.OCTOBER);
		c.set(Calendar.DAY_OF_MONTH, 16);
		Log.d("MYLOG", "Date set to " + c.getTime());
		week_1 = c.get(Calendar.WEEK_OF_YEAR);
		Log.d("MYLOG", "week of year: " + week_1);
		week_2 = c.get(Calendar.WEEK_OF_YEAR) + 1;
		week_3 = c.get(Calendar.WEEK_OF_YEAR) + 2;
		week_4 = c.get(Calendar.WEEK_OF_YEAR) + 3;
		week_5 = c.get(Calendar.WEEK_OF_YEAR) + 4;
		week_6 = c.get(Calendar.WEEK_OF_YEAR) + 5;
		week_7 = c.get(Calendar.WEEK_OF_YEAR) + 6;

		Log.d("MYLOG", "selectOption");
        Log.d("MYLOG", "region1: " + regionSelected);
        Log.d("MYLOG", "hospital1: " + hospitalSelected);
        Log.d("MYLOG", "week1: " + weekSelected);
        Log.d("MYLOG", "day1: " + daySelected);
		setOptionsSelected(regionSelected, hospitalSelected, weekSelected, daySelected);
		new LongOperation().execute((String[]) null);	
		Log.d("MYLOG", "thing : " + thing1);
		for (int i = 0; i < thing1.size(); i++) {
			try {
				thingDate = df.parse((((JSONObject) thing1.get(i)).get("date")) + " - " + (((JSONObject) thing1.get(i)).get("time")));
				textPopulate += df.format(thingDate);
				Log.d("MYLOG", "thingDate is : " + thingDate);
			} catch (ParseException | JSONException e) {
				e.printStackTrace();
			}
		}		
		appointmentInfo.setText(textPopulate);		
    }
    public void setRegionSelected(int regionSelected){
    	this.regionSelected = regionSelected;
    }
    public void setHospitalSelected(int hospitalSelected){
    	this.hospitalSelected = hospitalSelected;
    }
    public void setWeekSelected(int weekSelected){
    	this.weekSelected = weekSelected;
    }
    public void setDaySelected(int daySelected){
    	this.daySelected = daySelected;
    }
    public void setOptionsSelected(int regionSelected, int hospitalSelected, int weekSelected, int daySelected){
		this.regionSelected = regionSelected;
    	this.hospitalSelected = hospitalSelected;
    	this.weekSelected = weekSelected;
    	this.daySelected = daySelected;
    	
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
        		Log.d("MYLOG", "Leopardstown selected");
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
			c.set(Calendar.WEEK_OF_YEAR, week_1);
			c.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
			startDate = c.getTime();
			Log.d("MYLOG", "startDate: " + startDate);
			//c.set(Calendar.WEEK_OF_YEAR, week_1);
			c.add(Calendar.DAY_OF_WEEK, 6);
			endDate = c.getTime();
			c.add(Calendar.DAY_OF_WEEK, -6);
			Log.d("MYLOG", "endDate: " + endDate);			
			break;
    	case 2:
    		Log.d("MYLOG", "Week 2 selected");
    		break;
    	case 3:
    		Log.d("MYLOG", "Week 3 selected");
    		break;
    	case 4:
    		Log.d("MYLOG", "Week 4 selected");
    		break;
    	case 5:
    		Log.d("MYLOG", "Week 5 selected");
    		break;
    	case 6:
    		Log.d("MYLOG", "Week 6 selected");
    		break;
    	case 7:
    		Log.d("MYLOG", "Week 7 selected");
    		break;
    	}
    	switch(daySelected){
    	case 0:
    		break;
    	case 1:
    		Log.d("MYLOG", "Monday selected");
    		break;
    	case 2:
    		Log.d("MYLOG", "Tuesday selected");
    		c.add(Calendar.DAY_OF_WEEK, 1);
    		Log.d("MYLOG", "" + c.getTime());
			day = c.getTime();  
			Log.d("MYLOG", "day1: " + day);
    		break;
    	case 3:
    		Log.d("MYLOG", "Wednesday selected");
    		break;
    	case 4:
    		Log.d("MYLOG", "Thursday selected");
    		break;
    	case 5:
    		Log.d("MYLOG", "Friday selected");
    		break;
    	case 6:
    		Log.d("MYLOG", "Saturday selected");
    		break;
    	case 7:
    		Log.d("MYLOG", "Sunday selected");
    		break;
    	}
    }
    public class LongOperation extends AsyncTask<String, Void, String> {    
    	Date dayThis;
		@Override
		protected void onPreExecute() {
			dayThis = AppointmentCalendarActivity.this.day;  
			Log.d("MYLOG", "this.day: " + dayThis);
		}
		protected String doInBackground(String... params) {
/*			try {
				Log.d("MYLOG", "in async");
				thing2 = ds.dateSorter(df.parse("2014-10-14 - 00:00:00"));
			} catch (ParseException e) {
				e.printStackTrace();
			}*/	
			//day = dayArray[0];
			Log.d("MYLOG", "this.day: " + dayThis);
			Log.d("MYLOG", "in async");
			Log.d("MYLOG", "day is : " + day);
			thing2 = ds.dateSorter(dayThis);
			return null;
		}
		@Override
		protected void onProgressUpdate(Void... values) {
			
			Log.d("MYLOG", "On progress update");
		}
		@Override
        protected void onPostExecute(String result) {
			thing1 = thing2;
			Log.d("MYLOG", "thing2 : " + thing2);
			
			for (int i = 0; i < thing1.size(); i++) {
				try {
					thingDate = df.parse((((JSONObject) thing2.get(i)).get("date")) + " - " + (((JSONObject) thing2.get(i)).get("time")));
					textPopulate += (df.format(thingDate));
					Log.d("MYLOG", "thingDate is : " + thingDate);
				} catch (ParseException | JSONException e) {
					e.printStackTrace();
				}
			}		
			appointmentInfo.setText(textPopulate);
		}
	}
}