package com.team3.smartapp;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.view.MenuItem;
import android.widget.ImageView;

public class MainActivity extends Activity {
	
	ImageView image;
	private Button button1;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		image = (ImageView) findViewById(R.id.imageView2);
		
		button1 = (Button)findViewById(R.id.login);
		button1.setOnClickListener(new ButtonClick());
	}
	
	private class ButtonClick implements View.OnClickListener{
		public void onClick(View v) {
			switch(v.getId()){
			case R.id.login:
				Intent intentPatient = new Intent(MainActivity.this, ClinicActivity.class);
				startActivity(intentPatient);
				break;
	
}