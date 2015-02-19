package ie.teamchile.smartapp;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class TodayAppointmentActivity extends MenuInheritActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_today_appointment);
		ListView listView = (ListView)findViewById(R.id.list);
		// declare and initialize a String array with OS names
		String [] osNames = 
		new String []{"Lolli-pop", "Ice-Cream Sandwich", "Kit-Kat", "Eclair", "Choc-Ice", "Gingerbread"};
		
		// create an adapter of type String to populate the listview
		ArrayAdapter<String>adapter = new ArrayAdapter<String>(this,
									R.layout.activity_today_appointment,
									R.id.text_1, osNames);
		listView.setAdapter(adapter);
		listView.setOnItemClickListener((android.widget.AdapterView.OnItemClickListener) this);
	}
	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
		String[] osNames = null;
		Log.d("Item Listener", osNames[position]);
	}
}