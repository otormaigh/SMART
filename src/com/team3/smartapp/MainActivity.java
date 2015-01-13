package com.team3.smartapp;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import com.team3.smartapp.*;

public class MainActivity extends Activity {
	
	private Button test;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		test = (Button)findViewById(R.id.login);
		test.setOnClickListener(new ButtonClick());
	}
	
	private class ButtonClick implements View.OnClickListener{
		public void onClick(View v) {
			switch(v.getId()){
			case R.id.login:
				Intent intentPatient = new Intent(MainActivity.this, ClinicActivity.class);
				startActivity(intentPatient);
				break;
			}
		}
	}
}
	
