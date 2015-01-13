package com.team3.smartapp;

import com.example.midwifeoragnizer.MainActivity;
import com.example.midwifeoragnizer.PatientActivity;
import com.example.midwifeoragnizer.R;
import com.example.midwifeoragnizer.MainActivity.ButtonClick;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

public class AppointmentTypeActivity extends Activity {
 
	Button clinics;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.appointment_type);
		
		clinics = (Button)findViewById(R.id.clinics);
		clinics.setOnClickListener(new ButtonClick());
		
	}
		private class ButtonClick implements View.OnClickListener{
			public void onClick(View v) {
				switch(v.clinics()){
				case R.id.clinics:
					Intent intentPatient = new Intent(MainActivity.this, ClinicActivity.class);
					startActivity(intentPatient);
					break;
		
		
 
	}
		}
	}
