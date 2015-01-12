package com.team3.smartapp;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;

public class AppointmentTypeActivity extends Activity {
 
	Button clinics;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.appointment_type);
		addListenerOnButton();
	}
	
	
	public void addListenerOnButton() {
		 
		final Context context = this;
 
		clinics = (Button) findViewById(R.id.clinics);
 
		clinics.setOnClickListener(new OnClickListener() {
 
			@Override
			public void onClick(View arg0) {
 
			    Intent intent = new Intent(context, activity_clinic.class);
                startActivity(intent);   
 
			}
 
		});
 
}
