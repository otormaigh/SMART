package com.team3.smartapp;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

public class DominoDublinActivity extends Activity {
	
	private Button domino_dublin_nmh;
	private Button domino_dublin_leopardstown;
	private Button domino_dublin_dunlaoghaire;
	private Button domino_dublin_churchtown;
	private Button domino_dublin_nmh2;
	private Button domino_dublin_homevisits;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_domino_dublin);
		
		domino_dublin_nmh = (Button)findViewById(R.id.domino_dublin_nmh);
		domino_dublin_nmh.setOnClickListener(new ButtonClick());
		
		domino_dublin_leopardstown = (Button)findViewById(R.id.domino_dublin_leopardstown);
		domino_dublin_leopardstown.setOnClickListener(new ButtonClick());
		
		domino_dublin_dunlaoghaire = (Button)findViewById(R.id.domino_dublin_dunlaoghaire);
		domino_dublin_dunlaoghaire.setOnClickListener(new ButtonClick());
		
		domino_dublin_churchtown = (Button)findViewById(R.id.domino_dublin_churchtown);
		domino_dublin_churchtown.setOnClickListener(new ButtonClick());
		
		domino_dublin_nmh2 = (Button)findViewById(R.id.domino_dublin_nmh2);
		domino_dublin_nmh2.setOnClickListener(new ButtonClick());
		
		domino_dublin_homevisits = (Button)findViewById(R.id.domino_dublin_homevisits);
		domino_dublin_homevisits.setOnClickListener(new ButtonClick());
	}
	
	
	private class ButtonClick implements View.OnClickListener{
		public void onClick(View v) {
			switch(v.getId()){
			case R.id.domino_dublin_nmh:
				Intent intentDominoDublinNmh = new Intent(DominoDublinActivity.this, ClinicDatesNMHActivity.class);
				startActivity(intentDominoDublinNmh);
				break;
			case R.id.domino_dublin_leopardstown:
				Intent intentDominoDublinLeopardstown = new Intent(DominoDublinActivity.this, ClinicDatesLeopardstownActivity.class);
				startActivity(intentDominoDublinLeopardstown);
				break;
			case R.id.domino_dublin_dunlaoghaire:
				Intent intentDominoDublinDunLaoghaire = new Intent(DominoDublinActivity.this, ClinicDatesDunLaoghaireActivity.class);
				startActivity(intentDominoDublinDunLaoghaire);
				break;
			case R.id.domino_dublin_churchtown:
				Intent intentDominoDublinChurchtown = new Intent(DominoDublinActivity.this, ClinicDatesChurchtownActivity.class);
				startActivity(intentDominoDublinChurchtown);
				break;
			case R.id.domino_dublin_nmh2:
				Intent intentDominoDublinNmh2 = new Intent(DominoDublinActivity.this, SatelliteActivity.class);
				startActivity(intentDominoDublinNmh2);
				break;
			case R.id.domino_dublin_homevisits:
				Intent intentDominoDublinHomevisits = new Intent(DominoDublinActivity.this, ClinicDatesHomevisitsActivity.class);
				startActivity(intentDominoDublinHomevisits);
				break;
			}
		}
	}
}
