package com.team3.smartapp;

import java.sql.Date;
import java.util.Calendar;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.widget.TextView;

public class ClinicDatesHomevisitsActivity extends Activity {
	private TextView calendarWidget;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_clinic_dates_homevisits);

		calendarWidget = (TextView) findViewById(R.id.calendar);
		Calendar cal = Calendar.getInstance();
		Intent intent = new Intent(Intent.ACTION_EDIT);
		intent.setType("vnd.android.cursor.item/event");
		intent.putExtra("beginTime", cal.getTimeInMillis());
		intent.putExtra("allDay", true);
		intent.putExtra("rrule", "FREQ=YEARLY");
		intent.putExtra("endTime", cal.getTimeInMillis() + 60 * 60 * 1000);
		intent.putExtra("title", "A Test Event from android app");
		startActivity(intent);
	}
}