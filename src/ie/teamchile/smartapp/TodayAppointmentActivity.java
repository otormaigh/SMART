package ie.teamchile.smartapp;

import java.util.ArrayList;

import utility.AppointmentSingleton;
import utility.MyAdapter;
import android.app.ListActivity;
import android.os.Bundle;

public class TodayAppointmentActivity extends ListActivity {
	private ArrayList<String>values;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		values = AppointmentSingleton.getInstance().getListOfIDs("4", "2015-03-05");
		if(values.size() == 0 || values == null) {
			values = new ArrayList<String>();
			values.add("Not appointments today");
		}
		//printMap(hash);
		MyAdapter adapter = new MyAdapter(this, values);
		setListAdapter(adapter);
	}	
}