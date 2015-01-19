
package com.team3.smartapp;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

public class MainActivity extends Activity {	

	private Button loginButton;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		loginButton = (Button)findViewById(R.id.login);
		loginButton.setOnClickListener(new ButtonClick());
	}	
	


	private class ButtonClick implements View.OnClickListener{
		public void onClick(View v) {
			switch(v.getId()){
			case R.id.login:
				Intent intent = new Intent(MainActivity.this, AppointmentTypeActivity.class);
				startActivity(intent);
				break;
			}
		}
	}
}