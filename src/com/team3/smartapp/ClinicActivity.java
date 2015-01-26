package com.team3.smartapp;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.Button;

public class ClinicActivity extends Activity {

	private Button dublin_domino;
	private Button wicklow_domino;
	private Button eth_dublin;
	private Button eth_wicklow;
	private Button satellite;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_clinic);

		dublin_domino = (Button) findViewById(R.id.domino_d);
		dublin_domino.setOnClickListener(new ButtonClick());

		wicklow_domino = (Button) findViewById(R.id.domino_w);
		wicklow_domino.setOnClickListener(new ButtonClick());

		eth_dublin = (Button) findViewById(R.id.ethd);
		eth_dublin.setOnClickListener(new ButtonClick());

		eth_wicklow = (Button) findViewById(R.id.ethw);
		eth_wicklow.setOnClickListener(new ButtonClick());

		satellite = (Button) findViewById(R.id.satellite);
		satellite.setOnClickListener(new ButtonClick());
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		return super.onCreateOptionsMenu(menu);
	}

	private class ButtonClick implements View.OnClickListener {
		public void onClick(View v) {
			switch (v.getId()) {
			case R.id.domino_d:
				Intent intentDominoDublin = new Intent(ClinicActivity.this,	DominoDublinActivity.class);
				startActivity(intentDominoDublin);
				break;
			case R.id.domino_w:
				Intent intentDominoWicklow = new Intent(ClinicActivity.this, DominoWicklowActivity.class);
				startActivity(intentDominoWicklow);
				break;
			case R.id.ethd:
				Intent intentEthdDublin = new Intent(ClinicActivity.this, EtcDublinActivity.class);
				startActivity(intentEthdDublin);
				break;
			case R.id.ethw:
				Intent intentEthdWicklow = new Intent(ClinicActivity.this, EtcWicklowActivity.class);
				startActivity(intentEthdWicklow);
				break;
			case R.id.satellite:
				Intent intentSatellite = new Intent(ClinicActivity.this, SatelliteActivity.class);
				startActivity(intentSatellite);
				break;
			}
		}
	}
}