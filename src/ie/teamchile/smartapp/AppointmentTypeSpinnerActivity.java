package ie.teamchile.smartapp;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import utility.ClinicSingleton;
import utility.ServiceOptionSingleton;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

public class AppointmentTypeSpinnerActivity extends MenuInheritActivity {
	private SimpleDateFormat sdfDay = new SimpleDateFormat("E", Locale.getDefault());
    private Spinner appointmentSpinner, serviceOptionSpinner, visitOptionSpinner, clinicSpinner, weekSpinner, daySpinner;
    private Button appointmentCalendar;
    private ArrayAdapter<CharSequence> appointmentAdapter, visitAdapter, weekAdapter;
    private ArrayAdapter<String> serviceOptionAdapter, clinicAdapter, dayAdapter;
    private List<String> nameList, idList;
    private int serviceOptionSelected, clinicSelected, weekSelected;
	private Date daySelected, dayOfWeek;
    private Calendar c = Calendar.getInstance();
    private AppointmentCalendarActivity passOptions = new AppointmentCalendarActivity();

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
        
        setServiceOptionSpinner();
        
        serviceOptionSpinner = (Spinner) findViewById(R.id.clinic_service_option_spinner);
        serviceOptionSpinner.setOnItemSelectedListener(new MySpinnerOnItemSelectedListener());
	    serviceOptionAdapter = new ArrayAdapter<String>(this, R.layout.spinner_layout, nameList);
	    serviceOptionAdapter.setDropDownViewResource(R.layout.spinner_layout);
	    serviceOptionSpinner.setAdapter(serviceOptionAdapter);

        visitOptionSpinner = (Spinner) findViewById(R.id.visit_service_option_spinner);
        visitOptionSpinner.setOnItemSelectedListener(new MySpinnerOnItemSelectedListener());
        visitAdapter = ArrayAdapter.createFromResource(this, R.array.visit_service_option, R.layout.spinner_layout);
        visitAdapter.setDropDownViewResource(R.layout.spinner_layout);
        visitOptionSpinner.setAdapter(visitAdapter);

        clinicSpinner = (Spinner) findViewById(R.id.clinic_spinner);
        
        weekSpinner = (Spinner) findViewById(R.id.week_spinner);
        weekSpinner.setOnItemSelectedListener(new MySpinnerOnItemSelectedListener());
        weekAdapter = ArrayAdapter.createFromResource(this, R.array.weeks, R.layout.spinner_layout);
        weekAdapter.setDropDownViewResource(R.layout.spinner_layout);
        weekSpinner.setAdapter(weekAdapter);

        daySpinner = (Spinner) findViewById(R.id.day_spinner);
/*        daySpinner.setOnItemSelectedListener(new MySpinnerOnItemSelectedListener());
        dayAdapter = ArrayAdapter.createFromResource(this, R.array.days, R.layout.spinner_layout);
        dayAdapter.setDropDownViewResource(R.layout.spinner_layout);
        daySpinner.setAdapter(dayAdapter);*/

        appointmentCalendar = (Button) findViewById(R.id.appointment_calendar_button);
        appointmentCalendar.setOnClickListener(new ButtonClick());

        appointmentSpinner.setVisibility(View.VISIBLE);
        serviceOptionSpinner.setVisibility(View.GONE);
        visitOptionSpinner.setVisibility(View.GONE);
        clinicSpinner.setVisibility(View.GONE);
        weekSpinner.setVisibility(View.GONE);
        daySpinner.setVisibility(View.GONE);
        appointmentCalendar.setVisibility(View.GONE);
    }
	private class ButtonClick implements View.OnClickListener {
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.appointment_calendar_button:
                    Log.d("MYLOG", "region: " + serviceOptionSelected);
                    Log.d("MYLOG", "hospital: " + clinicSelected);
                    Log.d("MYLOG", "week: " + weekSelected);
                    Log.d("MYLOG", "day: " + daySelected);
                    passOptions.setServiceOptionSelected(serviceOptionSelected);
                    passOptions.setClinicSelected(clinicSelected);
                    passOptions.setWeekSelected(weekSelected);
                    passOptions.setDaySelected(daySelected);
                    Intent intent = new Intent(AppointmentTypeSpinnerActivity.this, AppointmentCalendarActivity.class);
                    startActivity(intent);
                    break;
            }
        }
    }
	
	private void setServiceOptionSpinner(){
		 int mapSize = ServiceOptionSingleton.getInstance().getMapOfID().size();
	        nameList = new ArrayList<String>();
	        nameList.add("Select Service Option");
	        
	        for(int i = 1; i < mapSize + 1; i++){
	        	nameList.add(ServiceOptionSingleton.getInstance().getName(String.valueOf(i)));
	        }
	}
	
	private void setClinicSpinner(String z){
		idList = ServiceOptionSingleton.getInstance().getClinicIDs(z);
		List<String> clinicNames = new ArrayList<String>();
		clinicNames.add("Select Clinic");
		
		if(idList != null){
			for(int i = 0; i < idList.size(); i++){
				clinicNames.add(ClinicSingleton.getInstance().getClinicName(idList.get(i)));
			}			
		}

        clinicSpinner.setOnItemSelectedListener(new MySpinnerOnItemSelectedListener());
        clinicAdapter = new ArrayAdapter<String>(this, R.layout.spinner_layout, clinicNames);
        clinicAdapter.setDropDownViewResource(R.layout.spinner_layout);
        clinicSpinner.setAdapter(clinicAdapter);
	}
	
	private void setDaySpinner(List<String> days){
		for(int i = 0; i < days.size(); i++){
			String thing = Character.toString(days.get(i).charAt(0)).toUpperCase() + days.get(i).substring(1);
			days.set(i, thing);
		}
		days.add(0, "Select Day");
		
		daySpinner.setOnItemSelectedListener(new MySpinnerOnItemSelectedListener());
        dayAdapter = new ArrayAdapter<String>(this, R.layout.spinner_layout, days);
        dayAdapter.setDropDownViewResource(R.layout.spinner_layout);
        daySpinner.setAdapter(dayAdapter);
	}
	
    private class MySpinnerOnItemSelectedListener implements AdapterView.OnItemSelectedListener {
        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            switch (parent.getId()) {
                case R.id.appointment_spinner:
                    switch (position) {
                        case 0:
                            appointmentSpinner.setVisibility(View.VISIBLE);
                            serviceOptionSpinner.setVisibility(View.GONE);
                            visitOptionSpinner.setVisibility(View.GONE);
                            clinicSpinner.setVisibility(View.GONE);
                            weekSpinner.setVisibility(View.GONE);
                            daySpinner.setVisibility(View.GONE);
                            appointmentCalendar.setVisibility(View.GONE);
                            break;
                        case 1:     //Clinic
                            serviceOptionSpinner.setVisibility(View.VISIBLE);
                            serviceOptionSpinner.setSelection(0);
                            break;
                        case 2:     //Visit
                            appointmentSpinner.setVisibility(View.VISIBLE);
                            serviceOptionSpinner.setVisibility(View.GONE);
                            visitOptionSpinner.setVisibility(View.VISIBLE);
                            clinicSpinner.setVisibility(View.GONE);
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
                            serviceOptionSelected = 0;
                            appointmentSpinner.setVisibility(View.VISIBLE);
                            serviceOptionSpinner.setVisibility(View.VISIBLE);
                            visitOptionSpinner.setVisibility(View.GONE);
                            clinicSpinner.setVisibility(View.GONE);
                            weekSpinner.setVisibility(View.GONE);
                            daySpinner.setVisibility(View.GONE);
                            appointmentCalendar.setVisibility(View.GONE);
                            break;
                        default:
                        	clinicSpinner.setVisibility(View.VISIBLE);
                        	serviceOptionSelected = position;
                        	setClinicSpinner(String.valueOf(position));
                        	clinicSpinner.setSelection(0);
                    }
                    break;
                case R.id.clinic_spinner:
                    switch (position) {
                        case 0:
                            clinicSelected = 0;
                            appointmentSpinner.setVisibility(View.VISIBLE);
                            serviceOptionSpinner.setVisibility(View.VISIBLE);
                            visitOptionSpinner.setVisibility(View.GONE);
                            clinicSpinner.setVisibility(View.VISIBLE);
                            weekSpinner.setVisibility(View.GONE);
                            daySpinner.setVisibility(View.GONE);
                            appointmentCalendar.setVisibility(View.GONE);
                            break;
                        default:
                        	weekSpinner.setVisibility(View.VISIBLE);
                        	clinicSelected = Integer.parseInt(idList.get(position - 1));
                        	weekSpinner.setSelection(0);
                    }
                    break;                    
                case R.id.week_spinner:
                    switch (position) {
                        case 0:
                            weekSelected = 0;
                            c = Calendar.getInstance();
                            daySpinner.setVisibility(View.GONE);
                            appointmentCalendar.setVisibility(View.GONE);
                            break;
                        default:
                        	weekSelected = position;
                            c = Calendar.getInstance();
                            Log.d("MYLOG", "Week " + position + " selected");
                    		c.add(Calendar.DAY_OF_YEAR, 7 * position);
                			Log.d("MYLOG", "Plus " + (7 * position)  + " days is: " + c.getTime());	
                			daySelected = c.getTime();
                            
                            if(ClinicSingleton.getInstance().getTrueDays(String.valueOf(clinicSelected)).size() > 1){
                        		setDaySpinner(ClinicSingleton.getInstance().getTrueDays(String.valueOf(clinicSelected)));
                        		daySpinner.setVisibility(View.VISIBLE);
                                daySpinner.setSelection(0);
                        	} else {
                        		try {
									dayOfWeek = sdfDay.parse(ClinicSingleton.getInstance().getTrueDays(String.valueOf(clinicSelected)).get(0));
									c.setTime(dayOfWeek);
									int dayAsInt = c.get(Calendar.DAY_OF_WEEK);
									c.setTime(daySelected);
									c.set(Calendar.DAY_OF_WEEK, dayAsInt);
									Log.d("bugs", "day selected: " + daySpinner.getSelectedItem().toString());
									Log.d("bugs", "day selected: " + c.getTime());
									daySelected = c.getTime();
								} catch (ParseException e) {
									e.printStackTrace();
								}
                        		appointmentCalendar.setVisibility(View.VISIBLE);
                        	}
                        	break;
                    }
                    break;
                case R.id.day_spinner:
                    switch (position) {
                        case 0:
                            appointmentCalendar.setVisibility(View.GONE);
                            break;
                        default:
							try {
								dayOfWeek = sdfDay.parse(daySpinner.getSelectedItem().toString());
								c.setTime(dayOfWeek);
								int dayAsInt = c.get(Calendar.DAY_OF_WEEK);
								c.setTime(daySelected);
								c.set(Calendar.DAY_OF_WEEK, dayAsInt);
								Log.d("bugs", "day selected: " + daySpinner.getSelectedItem().toString());
								Log.d("bugs", "day selected: " + c.getTime());
								appointmentCalendar.setVisibility(View.VISIBLE);
								daySelected = c.getTime();
							} catch (ParseException e) {
								e.printStackTrace();
							}
                        	break;/*
                        case 1:     //Monday
                        	Log.d("MYLOG", "Monday selected");
                        	String monday = "MONDAY";
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
                            break;*/
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