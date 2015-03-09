package ie.teamchile.smartapp;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

import utility.AppointmentSingleton;
import utility.MyAdapter;
import android.app.ListActivity;
import android.os.Bundle;


public class TodayAppointmentActivity extends ListActivity {
	private ArrayList<String>values;
	private Calendar cal;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		cal = Calendar.getInstance();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.UK);
		String date = sdf.format(cal.getTime());
		
		values = new ArrayList<String>();		
		values = AppointmentSingleton.getInstance().getListOfIDs("4", "2015-03-05");

		if(values.size() == 0 || values == null) {
			values.add("Not appointments today");
		}
		//printMap(hash);
		MyAdapter adapter = new MyAdapter(this, values);
		setListAdapter(adapter);
	}	
}