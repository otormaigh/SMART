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
		values = AppointmentSingleton.getIntance().getListOfIDs("4", "2015-03-05");
		MyAdapter adapter = new MyAdapter(this, values);
		setListAdapter(adapter);
	}	
}