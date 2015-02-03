package com.team3.smartapp;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.widget.Button;

public class AppointmentTypeActivity extends Activity {
 
	Button clinics;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_appointment_type);
		
		clinics = (Button)findViewById(R.id.clinics);
		clinics.setText(Html.fromHtml("<b><big>" + "Clinics" + "</big></b>" +  "<br />" + 
		        "<small>" + "(antenatal)" + "</small>" + "<br />"));
		clinics.setOnClickListener(new ButtonClick());
		
	}
	private class ButtonClick implements View.OnClickListener{
		public void onClick(View v) {
			switch(v.getId()){
			case R.id.clinics:
				Intent intentPatient = new Intent(AppointmentTypeActivity.this, ClinicActivity.class);
				startActivity(intentPatient);
				break;
				
			}
		}
	}
}