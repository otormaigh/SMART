package ie.teamchile.smartapp.activities;

import ie.teamchile.smartapp.R;
import ie.teamchile.smartapp.utility.ClinicSingleton;
import ie.teamchile.smartapp.utility.ServiceOptionSingleton;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import butterknife.InjectView;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

public class AppointmentTypeSpinnerActivity extends MenuInheritActivity {
	@InjectView (R.id.appointment_spinner) Spinner appointmentSpinner;
	@InjectView (R.id.clinic_service_option_spinner) Spinner serviceOptionSpinner;
	@InjectView (R.id.visit_service_option_spinner) Spinner visitOptionSpinner;
	@InjectView (R.id.clinic_spinner) Spinner clinicSpinner;
	@InjectView (R.id.day_spinner) Spinner daySpinner;
	@InjectView (R.id.week_spinner) Spinner weekSpinner;
	
	private SimpleDateFormat sdfDay = new SimpleDateFormat("E", Locale.getDefault());
	private SimpleDateFormat sdfDowMonthDay = new SimpleDateFormat("EEE, d MMM", Locale.getDefault());
    private ArrayAdapter<CharSequence> appointmentAdapter, visitAdapter;
    private ArrayAdapter<String> serviceOptionAdapter, clinicAdapter, dayAdapter, weekAdapter;
    private List<String> nameList, idList;
    private int serviceOptionSelected, clinicSelected, weekSelected;
	private Date daySelected, dayOfWeek;
    private Calendar c = Calendar.getInstance();
    private AppointmentCalendarActivity passOptions = new AppointmentCalendarActivity();
    private int spinnerWarning;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_appointment_type_spinner);
        
        c.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
        Log.d("MYLOG", "Date set to " + c.getTime());       
        
        spinnerWarning = getResources().getColor(R.color.lightBlue);
        
        appointmentSpinner.setOnItemSelectedListener(new MySpinnerOnItemSelectedListener());
        appointmentAdapter = ArrayAdapter.createFromResource(this, R.array.appointment_type, R.layout.spinner_layout);
        appointmentAdapter.setDropDownViewResource(R.layout.spinner_layout);
        appointmentSpinner.setAdapter(appointmentAdapter);
        
        setServiceOptionSpinner();
        
        serviceOptionSpinner.setOnItemSelectedListener(new MySpinnerOnItemSelectedListener());
	    serviceOptionAdapter = new ArrayAdapter<String>(this, R.layout.spinner_layout, nameList);
	    serviceOptionAdapter.setDropDownViewResource(R.layout.spinner_layout);
	    serviceOptionSpinner.setAdapter(serviceOptionAdapter);

        visitOptionSpinner.setOnItemSelectedListener(new MySpinnerOnItemSelectedListener());
        visitAdapter = ArrayAdapter.createFromResource(this, R.array.visit_service_option, R.layout.spinner_layout);
        visitAdapter.setDropDownViewResource(R.layout.spinner_layout);
        visitOptionSpinner.setAdapter(visitAdapter);

        weekSpinner.setOnItemSelectedListener(new MySpinnerOnItemSelectedListener());
        
        appointmentSpinner.setVisibility(View.VISIBLE);
        serviceOptionSpinner.setVisibility(View.GONE);
        visitOptionSpinner.setVisibility(View.GONE);
        clinicSpinner.setVisibility(View.GONE);
        daySpinner.setVisibility(View.GONE);
        weekSpinner.setVisibility(View.GONE);
    }
	
	private void setServiceOptionSpinner(){
		 int mapSize = ServiceOptionSingleton.getInstance().getMapOfID().size();
	        nameList = new ArrayList<String>();
	        nameList.add(Html.fromHtml("<font color=\"#FF0000\">" + "Select Service Option" + "</font>").toString());
	        
	        for(int i = 1; i < mapSize + 1; i++){
	        	nameList.add(ServiceOptionSingleton.getInstance().getName(String.valueOf(i)));
	        }
	}
	
	private void setClinicSpinner(String z){
		idList = ServiceOptionSingleton.getInstance().getClinicIDs(z);
		List<String> clinicNames = new ArrayList<String>();
		clinicNames.add(Html.fromHtml("<b>Select Clinic</b>").toString());
		
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
	
	private void setWeekSpinner(Date dayOfWeek){
		List<String> weeks = new ArrayList<String>();
		weeks.add("Select Week");
		
		for(int i = 1; i <= 10; i++){
            c = Calendar.getInstance();
            Log.d("MYLOG", "Week " + i + " selected");
    		c.add(Calendar.DAY_OF_YEAR, 7 * i);
    		c.set(Calendar.DAY_OF_WEEK, dayOfWeek.getDay() + 1);
    		weeks.add("Week " + i + " - " + sdfDowMonthDay.format(c.getTime()));
		}
		
		//weekAdapter = ArrayAdapter.createFromResource(this, R.array.weeks, R.layout.spinner_layout);
		weekAdapter = new ArrayAdapter<String>(this, R.layout.spinner_layout, weeks);
        weekAdapter.setDropDownViewResource(R.layout.spinner_layout);
        weekSpinner.setAdapter(weekAdapter);
	}
	
	private void setDaySpinner(List<String> days){
		for(int i = 0; i < days.size(); i++){
			String dayFirstLetterUpperCase = Character.toString(days.get(i).charAt(0))
					.toUpperCase(Locale.getDefault()) 
					+ days.get(i).substring(1);
			days.set(i, dayFirstLetterUpperCase);
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
                        	appointmentSpinner.setBackgroundColor(spinnerWarning);
                            appointmentSpinner.setVisibility(View.VISIBLE);
                            serviceOptionSpinner.setVisibility(View.GONE);
                            visitOptionSpinner.setVisibility(View.GONE);
                            clinicSpinner.setVisibility(View.GONE);
                            weekSpinner.setVisibility(View.GONE);
                            daySpinner.setVisibility(View.GONE);
                            break;
                        case 1:     //Clinic
                        	appointmentSpinner.setBackgroundColor(Color.TRANSPARENT);
                        	visitOptionSpinner.setVisibility(View.GONE);
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
                            visitOptionSpinner.setSelection(0);
                            break;
                    }
                    break;
                case R.id.clinic_service_option_spinner:
                    switch (position) {
                        case 0:
                            serviceOptionSelected = 0;
                            serviceOptionSpinner.setBackgroundColor(spinnerWarning);
                            appointmentSpinner.setVisibility(View.VISIBLE);
                            serviceOptionSpinner.setVisibility(View.VISIBLE);
                            visitOptionSpinner.setVisibility(View.GONE);
                            clinicSpinner.setVisibility(View.GONE);
                            weekSpinner.setVisibility(View.GONE);
                            daySpinner.setVisibility(View.GONE);
                            break;
                        default:
                        	serviceOptionSpinner.setBackgroundColor(Color.TRANSPARENT);
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
                            clinicSpinner.setBackgroundColor(spinnerWarning);
                            appointmentSpinner.setVisibility(View.VISIBLE);
                            serviceOptionSpinner.setVisibility(View.VISIBLE);
                            visitOptionSpinner.setVisibility(View.GONE);
                            clinicSpinner.setVisibility(View.VISIBLE);
                            daySpinner.setVisibility(View.GONE);
                            weekSpinner.setVisibility(View.GONE);
                            break;
                        default:
                        	clinicSpinner.setBackgroundColor(Color.TRANSPARENT);
                        	daySpinner.setVisibility(View.GONE);
                        	daySpinner.setSelection(0);
                        	weekSpinner.setVisibility(View.GONE);
                        	weekSpinner.setSelection(0);
                            
                        	clinicSelected = Integer.parseInt(idList.get(position - 1));
                        	
                        	if(ClinicSingleton.getInstance().getTrueDays(String.valueOf(clinicSelected)).size() > 1){
                        		setDaySpinner(ClinicSingleton.getInstance().getTrueDays(String.valueOf(clinicSelected)));
                        		daySpinner.setVisibility(View.VISIBLE);
                                daySpinner.setSelection(0);
                        	} else {
                        		try {
                        			weekSpinner.setVisibility(View.VISIBLE);
                                	weekSpinner.setSelection(0);
									dayOfWeek = sdfDay.parse(ClinicSingleton.getInstance()
											.getTrueDays(String.valueOf(clinicSelected)).get(0));
									setWeekSpinner(dayOfWeek);									
								} catch (ParseException e) {
									e.printStackTrace();
								}
                        	}
                        	break;
                    }
                    break;   
                case R.id.day_spinner:
                    switch (position) {
                        case 0:
                        	weekSpinner.setVisibility(View.GONE);
                        	daySpinner.setBackgroundColor(spinnerWarning);
                            break;
                        default:
                        	daySpinner.setBackgroundColor(Color.TRANSPARENT);
                        	weekSpinner.setVisibility(View.VISIBLE);
                        	weekSpinner.setSelection(0);
							try {
								dayOfWeek = sdfDay.parse(daySpinner.getSelectedItem().toString());
								setWeekSpinner(dayOfWeek);
							} catch (ParseException e) {
								e.printStackTrace();
							}
                        	break;
                    }
                    break;
                case R.id.week_spinner:
                    switch (position) {
                        case 0:
                            weekSelected = 0;
                            weekSpinner.setBackgroundColor(spinnerWarning);
                            c = Calendar.getInstance();
                            break;
                        default:
                        	weekSelected = position;
                        	weekSpinner.setBackgroundColor(Color.TRANSPARENT);
                            c = Calendar.getInstance();
                    		c.add(Calendar.DAY_OF_YEAR, 7 * position);
                			Log.d("MYLOG", "Plus " + (7 * position)  + " days is: " + c.getTime());	
                			daySelected = c.getTime();
                			addDayToTime(dayOfWeek);
                			changeActivity();
                        	break;
                    }
                    break;
            }
        }
        @Override
        public void onNothingSelected(AdapterView<?> parent) { }
    }
    
    public void changeActivity(){
    	passOptions.setServiceOptionSelected(serviceOptionSelected);
        passOptions.setClinicSelected(clinicSelected);
        passOptions.setWeekSelected(weekSelected);
        passOptions.setDaySelected(daySelected);
        
        Log.d("MYLOG", "clinic in spinner = " + clinicSelected);
        
        Intent intent = new Intent(AppointmentTypeSpinnerActivity.this, AppointmentCalendarActivity.class);
        startActivity(intent);
    }
    
    public void addDayToTime(Date dayOfWeek){
    	c.setTime(dayOfWeek);
		int dayAsInt = c.get(Calendar.DAY_OF_WEEK);
		c.setTime(daySelected);
		c.set(Calendar.DAY_OF_WEEK, dayAsInt);
		daySelected = c.getTime();
    }
}