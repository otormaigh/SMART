package ie.teamchile.smartapp;

import java.util.Calendar;
import java.util.Date;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

public class AppointmentTypeSpinnerActivity extends MenuInheritActivity {
    private Spinner appointmentSpinner, clinicOptionSpinner, visitOptionSpinner, dominoDublinSpinner,
            dominoWicklowSpinner, ethDublinSpinner, ethWicklowSpinner, satelliteSpinner, weekSpinner, daySpinner;
    private Button appointmentCalendar;
    private ArrayAdapter<CharSequence> appointmentAdapter, clinicAdapter, visitAdapter, dominoDublinAdapter,
            dominoWicklowAdapter, ethDublinAdapter, ethWicklowAdapter, satelliteAdapter, weekAdapter, dayAdapter;
    private int regionSelected, hospitalSelected, weekSelected;
	private Date daySelected;
    private Calendar c = Calendar.getInstance();
    
    AppointmentCalendarActivity passOptions = new AppointmentCalendarActivity();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_appointment_type_spinner);
        
        c.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
        Log.d("MYLOG", "Date set to " + c.getTime());

        appointmentSpinner = (Spinner) findViewById(R.id.appointment_spinner);
        appointmentSpinner.setOnItemSelectedListener(new MySpinnerOnItemSelectedListener());
        appointmentAdapter = ArrayAdapter.createFromResource(this, R.array.appointment_type, R.layout.spinner_layout);
        appointmentAdapter.setDropDownViewResource(R.layout.spinner_layout);
        appointmentSpinner.setAdapter(appointmentAdapter);

        clinicOptionSpinner = (Spinner) findViewById(R.id.clinic_service_option_spinner);
        clinicOptionSpinner.setOnItemSelectedListener(new MySpinnerOnItemSelectedListener());
        clinicAdapter = ArrayAdapter.createFromResource(this, R.array.clinic_service_option, R.layout.spinner_layout);
        clinicAdapter.setDropDownViewResource(R.layout.spinner_layout);
        clinicOptionSpinner.setAdapter(clinicAdapter);

        visitOptionSpinner = (Spinner) findViewById(R.id.visit_service_option_spinner);
        visitOptionSpinner.setOnItemSelectedListener(new MySpinnerOnItemSelectedListener());
        visitAdapter = ArrayAdapter.createFromResource(this, R.array.visit_service_option, R.layout.spinner_layout);
        visitAdapter.setDropDownViewResource(R.layout.spinner_layout);
        visitOptionSpinner.setAdapter(visitAdapter);

        dominoDublinSpinner = (Spinner) findViewById(R.id.domino_dublin_spinner);
        dominoDublinSpinner.setOnItemSelectedListener(new MySpinnerOnItemSelectedListener());
        dominoDublinAdapter = ArrayAdapter.createFromResource(this, R.array.domino_dublin, R.layout.spinner_layout);
        dominoDublinAdapter.setDropDownViewResource(R.layout.spinner_layout);
        dominoDublinSpinner.setAdapter(dominoDublinAdapter);

        dominoWicklowSpinner = (Spinner) findViewById(R.id.domino_wicklow_spinner);
        dominoWicklowSpinner.setOnItemSelectedListener(new MySpinnerOnItemSelectedListener());
        dominoWicklowAdapter = ArrayAdapter.createFromResource(this, R.array.domino_wicklow, R.layout.spinner_layout);
        dominoWicklowAdapter.setDropDownViewResource(R.layout.spinner_layout);
        dominoWicklowSpinner.setAdapter(dominoWicklowAdapter);

        ethDublinSpinner = (Spinner) findViewById(R.id.eth_dublin_spinner);
        ethDublinSpinner.setOnItemSelectedListener(new MySpinnerOnItemSelectedListener());
        ethDublinAdapter = ArrayAdapter.createFromResource(this, R.array.eth, R.layout.spinner_layout);
        ethDublinAdapter.setDropDownViewResource(R.layout.spinner_layout);
        ethDublinSpinner.setAdapter(ethDublinAdapter);

        ethWicklowSpinner = (Spinner) findViewById(R.id.eth_wicklow_spinner);
        ethWicklowSpinner.setOnItemSelectedListener(new MySpinnerOnItemSelectedListener());
        ethWicklowAdapter = ArrayAdapter.createFromResource(this, R.array.eth, R.layout.spinner_layout);
        ethWicklowAdapter.setDropDownViewResource(R.layout.spinner_layout);
        ethWicklowSpinner.setAdapter(ethWicklowAdapter);

        satelliteSpinner = (Spinner) findViewById(R.id.satellite_spinner);
        satelliteSpinner.setOnItemSelectedListener(new MySpinnerOnItemSelectedListener());
        satelliteAdapter = ArrayAdapter.createFromResource(this, R.array.satellite, R.layout.spinner_layout);
        satelliteAdapter.setDropDownViewResource(R.layout.spinner_layout);
        satelliteSpinner.setAdapter(satelliteAdapter);

        weekSpinner = (Spinner) findViewById(R.id.week_spinner);
        weekSpinner.setOnItemSelectedListener(new MySpinnerOnItemSelectedListener());
        weekAdapter = ArrayAdapter.createFromResource(this, R.array.weeks, R.layout.spinner_layout);
        weekAdapter.setDropDownViewResource(R.layout.spinner_layout);
        weekSpinner.setAdapter(weekAdapter);

        daySpinner = (Spinner) findViewById(R.id.day_spinner);
        daySpinner.setOnItemSelectedListener(new MySpinnerOnItemSelectedListener());
        dayAdapter = ArrayAdapter.createFromResource(this, R.array.days, R.layout.spinner_layout);
        dayAdapter.setDropDownViewResource(R.layout.spinner_layout);
        daySpinner.setAdapter(dayAdapter);

        appointmentCalendar = (Button) findViewById(R.id.appointment_calendar_button);
        appointmentCalendar.setOnClickListener(new ButtonClick());

        appointmentSpinner.setVisibility(View.VISIBLE);
        clinicOptionSpinner.setVisibility(View.GONE);
        visitOptionSpinner.setVisibility(View.GONE);
        dominoDublinSpinner.setVisibility(View.GONE);
        dominoWicklowSpinner.setVisibility(View.GONE);
        ethDublinSpinner.setVisibility(View.GONE);
        ethWicklowSpinner.setVisibility(View.GONE);
        satelliteSpinner.setVisibility(View.GONE);
        weekSpinner.setVisibility(View.GONE);
        daySpinner.setVisibility(View.GONE);
        appointmentCalendar.setVisibility(View.GONE);
    }
	private class ButtonClick implements View.OnClickListener {
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.appointment_calendar_button:
                    Log.d("MYLOG", "region: " + regionSelected);
                    Log.d("MYLOG", "hospital: " + hospitalSelected);
                    Log.d("MYLOG", "week: " + weekSelected);
                    Log.d("MYLOG", "day: " + daySelected);
                    passOptions.setRegionSelected(regionSelected);
                    passOptions.setHospitalSelected(hospitalSelected);
                    passOptions.setWeekSelected(weekSelected);
                    passOptions.setDaySelected(daySelected);
                    Intent intent = new Intent(getApplicationContext(), AppointmentCalendarActivity.class);
                    startActivity(intent);
                    break;
            }
        }
    }
    private class MySpinnerOnItemSelectedListener implements AdapterView.OnItemSelectedListener {
        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            switch (parent.getId()) {
                case R.id.appointment_spinner:
                    switch (position) {
                        case 0:
                            appointmentSpinner.setVisibility(View.VISIBLE);
                            clinicOptionSpinner.setVisibility(View.GONE);
                            visitOptionSpinner.setVisibility(View.GONE);
                            dominoDublinSpinner.setVisibility(View.GONE);
                            dominoWicklowSpinner.setVisibility(View.GONE);
                            ethDublinSpinner.setVisibility(View.GONE);
                            ethWicklowSpinner.setVisibility(View.GONE);
                            satelliteSpinner.setVisibility(View.GONE);
                            weekSpinner.setVisibility(View.GONE);
                            daySpinner.setVisibility(View.GONE);
                            appointmentCalendar.setVisibility(View.GONE);
                            break;
                        case 1:     //Clinic
                            clinicOptionSpinner.setVisibility(View.VISIBLE);
                            clinicOptionSpinner.setSelection(0);
                            break;
                        case 2:     //Visit
                            appointmentSpinner.setVisibility(View.VISIBLE);
                            clinicOptionSpinner.setVisibility(View.GONE);
                            visitOptionSpinner.setVisibility(View.VISIBLE);
                            dominoDublinSpinner.setVisibility(View.GONE);
                            dominoWicklowSpinner.setVisibility(View.GONE);
                            ethDublinSpinner.setVisibility(View.GONE);
                            ethWicklowSpinner.setVisibility(View.GONE);
                            satelliteSpinner.setVisibility(View.GONE);
                            weekSpinner.setVisibility(View.GONE);
                            daySpinner.setVisibility(View.GONE);
                            appointmentCalendar.setVisibility(View.GONE);
                            visitOptionSpinner.setSelection(0);
                            break;
                    }
                    break;
                case R.id.clinic_service_option_spinner:
                    switch (position) {
                        case 0:
                            regionSelected = 0;
                            appointmentSpinner.setVisibility(View.VISIBLE);
                            clinicOptionSpinner.setVisibility(View.VISIBLE);
                            visitOptionSpinner.setVisibility(View.GONE);
                            dominoDublinSpinner.setVisibility(View.GONE);
                            dominoWicklowSpinner.setVisibility(View.GONE);
                            ethDublinSpinner.setVisibility(View.GONE);
                            ethWicklowSpinner.setVisibility(View.GONE);
                            satelliteSpinner.setVisibility(View.GONE);
                            weekSpinner.setVisibility(View.GONE);
                            daySpinner.setVisibility(View.GONE);
                            appointmentCalendar.setVisibility(View.GONE);
                            break;
                        case 1:     //Domino Dublin
                            regionSelected = 1;
                            dominoDublinSpinner.setVisibility(View.VISIBLE);
                            dominoDublinSpinner.setSelection(0);
                            break;
                        case 2:     //Domino Wicklow
                            regionSelected = 2;
                            dominoWicklowSpinner.setVisibility(View.VISIBLE);
                            dominoWicklowSpinner.setSelection(0);
                            break;
                        case 3:     //ETH Dublin
                            regionSelected = 3;
                            ethDublinSpinner.setVisibility(View.VISIBLE);
                            ethDublinSpinner.setSelection(0);
                            break;
                        case 4:     //ETH Wicklow
                            regionSelected = 4;
                            ethWicklowSpinner.setVisibility(View.VISIBLE);
                            ethWicklowSpinner.setSelection(0);
                            break;
                        case 5:     //Satellite
                            regionSelected = 5;
                            satelliteSpinner.setVisibility(View.VISIBLE);
                            satelliteSpinner.setSelection(0);
                            break;
                    }
                    break;
                case R.id.domino_dublin_spinner:
                    switch (position) {
                        case 0:
                            hospitalSelected = 0;
                            appointmentSpinner.setVisibility(View.VISIBLE);
                            clinicOptionSpinner.setVisibility(View.VISIBLE);
                            visitOptionSpinner.setVisibility(View.GONE);
                            dominoDublinSpinner.setVisibility(View.VISIBLE);
                            dominoWicklowSpinner.setVisibility(View.GONE);
                            ethDublinSpinner.setVisibility(View.GONE);
                            ethWicklowSpinner.setVisibility(View.GONE);
                            satelliteSpinner.setVisibility(View.GONE);
                            weekSpinner.setVisibility(View.GONE);
                            daySpinner.setVisibility(View.GONE);
                            appointmentCalendar.setVisibility(View.GONE);
                            break;
                        case 1:     //NMH(OPD Location)
                            hospitalSelected = 1;
                            weekSpinner.setVisibility(View.VISIBLE);
                            weekSpinner.setSelection(0);
                            break;
                        case 2:     //Leopardstown
                            hospitalSelected = 2;
                            weekSpinner.setVisibility(View.VISIBLE);
                            weekSpinner.setSelection(0);
                            break;
                        case 3:     //Dun Laoghaire
                            hospitalSelected = 3;
                            weekSpinner.setVisibility(View.VISIBLE);
                            weekSpinner.setSelection(0);
                            break;
                        case 4:     //Churchtown
                            hospitalSelected = 4;
                            weekSpinner.setVisibility(View.VISIBLE);
                            weekSpinner.setSelection(0);
                            break;
                        case 5:     //NMH
                            hospitalSelected = 5;
                            weekSpinner.setVisibility(View.VISIBLE);
                            weekSpinner.setSelection(0);
                            break;
                    }
                    break;
                case R.id.domino_wicklow_spinner:
                    switch (position) {
                        case 0:
                            hospitalSelected = 0;
                            appointmentSpinner.setVisibility(View.VISIBLE);
                            clinicOptionSpinner.setVisibility(View.VISIBLE);
                            visitOptionSpinner.setVisibility(View.GONE);
                            dominoDublinSpinner.setVisibility(View.GONE);
                            dominoWicklowSpinner.setVisibility(View.VISIBLE);
                            ethDublinSpinner.setVisibility(View.GONE);
                            ethWicklowSpinner.setVisibility(View.GONE);
                            satelliteSpinner.setVisibility(View.GONE);
                            weekSpinner.setVisibility(View.GONE);
                            daySpinner.setVisibility(View.GONE);
                            appointmentCalendar.setVisibility(View.GONE);
                            break;
                        case 1:     //Greystones (Monday)
                            hospitalSelected = 6;
                            weekSpinner.setVisibility(View.VISIBLE);
                            weekSpinner.setSelection(0);
                            break;
                        case 2:     //Greystones (Tuesday)
                            hospitalSelected = 6;
                            weekSpinner.setVisibility(View.VISIBLE);
                            weekSpinner.setSelection(0);
                            break;
                        case 3:     //Kilmacanogue
                            hospitalSelected = 7;
                            weekSpinner.setVisibility(View.VISIBLE);
                            weekSpinner.setSelection(0);
                            break;
                        case 4:     //Home Visits
                            hospitalSelected = 4;
                            weekSpinner.setVisibility(View.VISIBLE);
                            weekSpinner.setSelection(0);
                            break;
                    }
                    break;
                case R.id.eth_dublin_spinner:
                    switch (position) {
                        case 0:
                            hospitalSelected = 0;
                            appointmentSpinner.setVisibility(View.VISIBLE);
                            clinicOptionSpinner.setVisibility(View.VISIBLE);
                            visitOptionSpinner.setVisibility(View.GONE);
                            dominoDublinSpinner.setVisibility(View.GONE);
                            dominoWicklowSpinner.setVisibility(View.GONE);
                            ethDublinSpinner.setVisibility(View.VISIBLE);
                            ethWicklowSpinner.setVisibility(View.GONE);
                            satelliteSpinner.setVisibility(View.GONE);
                            weekSpinner.setVisibility(View.GONE);
                            daySpinner.setVisibility(View.GONE);
                            appointmentCalendar.setVisibility(View.GONE);
                            break;
                        case 1:     //Ballinteer
                            hospitalSelected = 8;
                            weekSpinner.setVisibility(View.VISIBLE);
                            weekSpinner.setSelection(0);
                            break;
                        case 2:     //Dun Laoghaire
                            hospitalSelected = 9;
                            weekSpinner.setVisibility(View.VISIBLE);
                            weekSpinner.setSelection(0);
                            break;
                    }
                    break;
                case R.id.eth_wicklow_spinner:
                    switch (position) {
                        case 0:
                            hospitalSelected = 0;
                            appointmentSpinner.setVisibility(View.VISIBLE);
                            clinicOptionSpinner.setVisibility(View.VISIBLE);
                            visitOptionSpinner.setVisibility(View.GONE);
                            dominoDublinSpinner.setVisibility(View.GONE);
                            dominoWicklowSpinner.setVisibility(View.GONE);
                            ethDublinSpinner.setVisibility(View.GONE);
                            ethWicklowSpinner.setVisibility(View.VISIBLE);
                            satelliteSpinner.setVisibility(View.GONE);
                            weekSpinner.setVisibility(View.GONE);
                            daySpinner.setVisibility(View.GONE);
                            appointmentCalendar.setVisibility(View.GONE);
                            break;
                        case 1:     //Ballinteer
                            hospitalSelected = 8;
                            weekSpinner.setVisibility(View.VISIBLE);
                            weekSpinner.setSelection(0);
                            break;
                        case 2:     //Dun Laoghaire
                            hospitalSelected = 9;
                            weekSpinner.setVisibility(View.VISIBLE);
                            weekSpinner.setSelection(0);
                            break;
                    }
                    break;
                case R.id.satellite_spinner:
                    switch (position) {
                        case 0:
                            hospitalSelected = 0;
                            appointmentSpinner.setVisibility(View.VISIBLE);
                            clinicOptionSpinner.setVisibility(View.VISIBLE);
                            visitOptionSpinner.setVisibility(View.GONE);
                            dominoDublinSpinner.setVisibility(View.GONE);
                            dominoWicklowSpinner.setVisibility(View.GONE);
                            ethDublinSpinner.setVisibility(View.GONE);
                            ethWicklowSpinner.setVisibility(View.GONE);
                            satelliteSpinner.setVisibility(View.VISIBLE);
                            weekSpinner.setVisibility(View.GONE);
                            daySpinner.setVisibility(View.GONE);
                            appointmentCalendar.setVisibility(View.GONE);
                            break;
                        case 1:     //Greystones
                            hospitalSelected = 10;
                            weekSpinner.setVisibility(View.VISIBLE);
                            weekSpinner.setSelection(0);
                            break;
                        case 2:     //Arklow
                            hospitalSelected = 11;
                            weekSpinner.setVisibility(View.VISIBLE);
                            weekSpinner.setSelection(0);
                            break;
                        case 3:     //Newtownmountkennedy
                            hospitalSelected = 12;
                            weekSpinner.setVisibility(View.VISIBLE);
                            weekSpinner.setSelection(0);
                            break;
                        case 4:     //Bray
                            hospitalSelected = 13;
                            weekSpinner.setVisibility(View.VISIBLE);
                            weekSpinner.setSelection(0);
                            break;
                    }
                    break;                    
                case R.id.week_spinner:
                    switch (position) {
                        case 0:
                            weekSelected = 0;
                            daySpinner.setVisibility(View.GONE);
                            appointmentCalendar.setVisibility(View.GONE);
                            break;
                        case 1:     //Week 1
                            weekSelected = 1;
                            daySpinner.setVisibility(View.VISIBLE);
                            daySpinner.setSelection(0);
                            break;
                        case 2:     //Week 2
                            weekSelected = 2;
                            Log.d("MYLOG", "Week 2 selected");
                			c.add(Calendar.DAY_OF_YEAR, 7);
                			Log.d("MYLOG", "Plus 7 days is: " + c.getTime());
                            daySpinner.setVisibility(View.VISIBLE);
                            daySpinner.setSelection(0);
                            break;
                        case 3:     //Week 3
                            weekSelected = 3;
                            Log.d("MYLOG", "Week 3 selected");
                			c.add(Calendar.DAY_OF_YEAR, (c.getFirstDayOfWeek() + 14));
                			Log.d("MYLOG", "Plus 14 days is: " + c.getTime());
                            daySpinner.setVisibility(View.VISIBLE);
                            daySpinner.setSelection(0);
                            break;
                        case 4:     //Week 4
                            weekSelected = 4;
                            Log.d("MYLOG", "Week 4 selected");
                    		c.add(Calendar.DAY_OF_YEAR, (c.getFirstDayOfWeek() + 21));
                			Log.d("MYLOG", "Plus 21 days is: " + c.getTime());
                            daySpinner.setVisibility(View.VISIBLE);
                            daySpinner.setSelection(0);
                            break;
                        case 5:     //Week 5
                            weekSelected = 5;
                            Log.d("MYLOG", "Week 5 selected");
                    		c.add(Calendar.DAY_OF_YEAR, (c.getFirstDayOfWeek() + 28));
                			Log.d("MYLOG", "Plus 21 days is: " + c.getTime());
                            daySpinner.setVisibility(View.VISIBLE);
                            daySpinner.setSelection(0);
                            break;
                        case 6:     //Week 6
                            weekSelected = 6;
                            Log.d("MYLOG", "Week 6 selected");
                    		c.add(Calendar.DAY_OF_YEAR, (c.getFirstDayOfWeek() + 35));
                			Log.d("MYLOG", "Plus 21 days is: " + c.getTime());	
                            daySpinner.setVisibility(View.VISIBLE);
                            daySpinner.setSelection(0);
                            break;
                        case 7:     //Week 7
                            weekSelected = 7;
                            Log.d("MYLOG", "Week 7 selected");
                    		c.add(Calendar.DAY_OF_YEAR, (c.getFirstDayOfWeek() + 42));
                			Log.d("MYLOG", "Plus 21 days is: " + c.getTime());	
                            daySpinner.setVisibility(View.VISIBLE);
                            daySpinner.setSelection(0);
                            break;
                    }
                    break;
                case R.id.day_spinner:
                    switch (position) {
                        case 0:
                            appointmentCalendar.setVisibility(View.GONE);
                            break;
                        case 1:     //Monday
                        	Log.d("MYLOG", "Monday selected");
                    		c.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
                    		Log.d("MYLOG", "" + c.getTime());
                			daySelected = c.getTime();  
                			
                			Log.d("MYLOG", "day from spinner: " + daySelected);
                            appointmentCalendar.setVisibility(View.VISIBLE);
                            break;
                        case 2:     //Tuesday
                        	Log.d("MYLOG", "Tuesday selected");
                    		c.set(Calendar.DAY_OF_WEEK, Calendar.TUESDAY);
                    		Log.d("MYLOG", "" + c.getTime());
                    		daySelected = c.getTime();  
                    		
                			Log.d("MYLOG", "day from spinner: " + daySelected);
                            appointmentCalendar.setVisibility(View.VISIBLE);
                            break;
                        case 3:     //Wednesday
                        	Log.d("MYLOG", "Wednesday selected");
                    		c.set(Calendar.DAY_OF_WEEK, Calendar.WEDNESDAY);
                    		Log.d("MYLOG", "" + c.getTime());                    		
                    		daySelected = c.getTime();  
                    		
                			Log.d("MYLOG", "day from spinner: " + daySelected);
                            appointmentCalendar.setVisibility(View.VISIBLE);
                            break;
                        case 4:     //Thursday
                        	Log.d("MYLOG", "Thursday selected");
                    		c.set(Calendar.DAY_OF_WEEK, Calendar.THURSDAY);
                    		Log.d("MYLOG", "" + c.getTime());
                    		daySelected = c.getTime();  
                    		
                			Log.d("MYLOG", "day from spinner: " + daySelected);
                            appointmentCalendar.setVisibility(View.VISIBLE);
                            break;
                        case 5:     //Friday
                        	Log.d("MYLOG", "Friday selected");
                    		c.set(Calendar.DAY_OF_WEEK, Calendar.FRIDAY);
                    		Log.d("MYLOG", "" + c.getTime());
                    		daySelected = c.getTime();  
                    		
                			Log.d("MYLOG", "day from spinner: " + daySelected);
                            appointmentCalendar.setVisibility(View.VISIBLE);
                            break;
                        case 6:     //Saturday
                        	Log.d("MYLOG", "Saturday selected");
                    		c.set(Calendar.DAY_OF_WEEK, Calendar.SATURDAY);
                    		Log.d("MYLOG", "" + c.getTime());
                    		daySelected = c.getTime();  
                    		
                			Log.d("MYLOG", "day from spinner: " + daySelected);
                            appointmentCalendar.setVisibility(View.VISIBLE);
                            break;
                        case 7:     //Sunday
                        	Log.d("MYLOG", "Sunday selected");
                    		c.set(Calendar.DAY_OF_WEEK, Calendar.SUNDAY);
                    		Log.d("MYLOG", "" + c.getTime());
                    		daySelected = c.getTime();  
                    		
                			Log.d("MYLOG", "day from spinner: " + daySelected);
                            appointmentCalendar.setVisibility(View.VISIBLE);
                            break;
                    }
                    break;
            }
            //setOptionsSelected(region, hospital, week, day);
            //if today is Wednesday hide spinners for Week 1 Day Monday, Tuesday
            //Today is Tuesday 10/02/15
            //if region 1, hospital 1, week 1, day 3
            //show appointments for Wednesday 11/02 in Domino Dublin, NMH (OPD Location)
            //if region 3, hospital 2, week 5, day 4
            //show appointments for Thursday 5/03 in ETH Dublin, Dun Laoghaire
        }
        @Override
        public void onNothingSelected(AdapterView<?> parent) {
        }
    }
}