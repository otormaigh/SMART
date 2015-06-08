package ie.teamchile.smartapp.activities;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import ie.teamchile.smartapp.R;
import ie.teamchile.smartapp.model.ApiRootModel;

public class AppointmentTypeSpinnerActivity extends BaseActivity {
    private ArrayAdapter<CharSequence> appointmentAdapter, visitAdapter;
    private ArrayAdapter<String> serviceOptionAdapter, clinicAdapter, dayAdapter, weekAdapter;
    private List<String> serviceOptionNameList;
    private List<Integer> idList;
    private int serviceOptionSelected, clinicSelected, weekSelected;
	private Date daySelected, dayOfWeek;
    private Calendar c = Calendar.getInstance();
    private AppointmentCalendarActivity passOptions = new AppointmentCalendarActivity();
    private int spinnerWarning;
    private Spinner appointmentTypeSpinner, serviceOptionSpinner, 
    		visitOptionSpinner, clinicSpinner, daySpinner, weekSpinner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentForNav(R.layout.activity_appointment_type_spinner);

        c.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
        Log.d("MYLOG", "Date set to " + c.getTime());       
        
        spinnerWarning = getResources().getColor(R.color.lightBlue);
        
        appointmentTypeSpinner = (Spinner) findViewById(R.id.spnr_appointment_type);
        serviceOptionSpinner = (Spinner) findViewById(R.id.spnr_service_option);
        visitOptionSpinner = (Spinner) findViewById(R.id.spnr_visit_option);
        clinicSpinner = (Spinner) findViewById(R.id.spnr_clinic);
        daySpinner = (Spinner) findViewById(R.id.spnr_day);
        weekSpinner = (Spinner) findViewById(R.id.spnr_week);
        
        appointmentTypeSpinner.setOnItemSelectedListener(new MySpinnerOnItemSelectedListener());
        appointmentAdapter = ArrayAdapter.createFromResource(this, R.array.appointment_type_list, R.layout.spinner_layout);
        appointmentAdapter.setDropDownViewResource(R.layout.spinner_layout);
        appointmentTypeSpinner.setAdapter(appointmentAdapter);
        
        setServiceOptionSpinner();
        
        serviceOptionSpinner.setOnItemSelectedListener(new MySpinnerOnItemSelectedListener());
	    serviceOptionAdapter = new ArrayAdapter<>(this, R.layout.spinner_layout, serviceOptionNameList);
	    serviceOptionAdapter.setDropDownViewResource(R.layout.spinner_layout);
	    serviceOptionSpinner.setAdapter(serviceOptionAdapter);

        visitOptionSpinner.setOnItemSelectedListener(new MySpinnerOnItemSelectedListener());
        visitAdapter = ArrayAdapter.createFromResource(this, R.array.visit_service_option, R.layout.spinner_layout);
        visitAdapter.setDropDownViewResource(R.layout.spinner_layout);
        visitOptionSpinner.setAdapter(visitAdapter);

        weekSpinner.setOnItemSelectedListener(new MySpinnerOnItemSelectedListener());
        
        appointmentTypeSpinner.setVisibility(View.VISIBLE);
        serviceOptionSpinner.setVisibility(View.GONE);
        visitOptionSpinner.setVisibility(View.GONE);
        clinicSpinner.setVisibility(View.GONE);
        daySpinner.setVisibility(View.GONE);
        weekSpinner.setVisibility(View.GONE);
    }
	
	private void setServiceOptionSpinner(){
        int mapSize = ApiRootModel.getInstance().getServiceOptionsMap().size();
        serviceOptionNameList = new ArrayList<>();
        serviceOptionNameList.add("Select Service Option");

        for(int i = 1; i <= mapSize; i++){
            serviceOptionNameList.add(ApiRootModel.getInstance().getServiceOptionsMap().get(i).getName());
        }
	}
	
	private void setClinicSpinner(int z){
        idList = ApiRootModel.getInstance().getServiceOptionsMap().get(z).getClinicIds();
        Log.d("Retrofit", "clinic id list = " + idList);
		List<String> clinicNames = new ArrayList<>();
		clinicNames.add("Select Clinic");

		if(idList != null){
			for(int i = 0; i < idList.size(); i++){
				clinicNames.add(ApiRootModel.getInstance().getClinicsMap().get(idList.get(i)).getName());
			}
		}

        clinicSpinner.setOnItemSelectedListener(new MySpinnerOnItemSelectedListener());
        clinicAdapter = new ArrayAdapter<>(this, R.layout.spinner_layout, clinicNames);
        clinicAdapter.setDropDownViewResource(R.layout.spinner_layout);
        clinicSpinner.setAdapter(clinicAdapter);
	}
	
	private void setWeekSpinner(Date dayOfWeek){
		List<String> weeks = new ArrayList<>();
		weeks.add("Select Week");
		
		for(int i = 1; i <= 10; i++){
            c = Calendar.getInstance();
            Log.d("MYLOG", "Week " + i + " selected");
    		c.add(Calendar.DAY_OF_YEAR, 7 * i);
    		c.set(Calendar.DAY_OF_WEEK, dayOfWeek.getDay() + 1);
    		weeks.add("Week " + i + " - " + dfDowMonthDay.format(c.getTime()));
		}
		
		weekAdapter = new ArrayAdapter<>(this, R.layout.spinner_layout, weeks);
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
        dayAdapter = new ArrayAdapter<>(this, R.layout.spinner_layout, days);
        dayAdapter.setDropDownViewResource(R.layout.spinner_layout);
        daySpinner.setAdapter(dayAdapter);
	}
	
    private class MySpinnerOnItemSelectedListener implements AdapterView.OnItemSelectedListener {
        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            switch (parent.getId()) {
                case R.id.spnr_appointment_type:
                    switch (position) {
                        case 0:
                        	appointmentTypeSpinner.setBackgroundColor(spinnerWarning);
                            appointmentTypeSpinner.setVisibility(View.VISIBLE);
                            serviceOptionSpinner.setVisibility(View.GONE);
                            visitOptionSpinner.setVisibility(View.GONE);
                            clinicSpinner.setVisibility(View.GONE);
                            weekSpinner.setVisibility(View.GONE);
                            daySpinner.setVisibility(View.GONE);
                            break;
                        case 1:     //Clinic
                        	appointmentTypeSpinner.setBackgroundColor(Color.TRANSPARENT);
                        	visitOptionSpinner.setVisibility(View.GONE);
                            serviceOptionSpinner.setVisibility(View.VISIBLE);
                            serviceOptionSpinner.setSelection(0);
                            break;
                        case 2:     //Visit
                            appointmentTypeSpinner.setVisibility(View.VISIBLE);
                            serviceOptionSpinner.setVisibility(View.GONE);
                            visitOptionSpinner.setVisibility(View.VISIBLE);
                            clinicSpinner.setVisibility(View.GONE);
                            weekSpinner.setVisibility(View.GONE);
                            daySpinner.setVisibility(View.GONE);
                            visitOptionSpinner.setSelection(0);
                            break;
                    }
                    break;
                case R.id.spnr_service_option:
                    switch (position) {
                        case 0:
                            serviceOptionSelected = 0;
                            serviceOptionSpinner.setBackgroundColor(spinnerWarning);
                            appointmentTypeSpinner.setVisibility(View.VISIBLE);
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
                        	setClinicSpinner(position);
                        	clinicSpinner.setSelection(0);
                    }
                    break;
                case R.id.spnr_clinic:
                    switch (position) {
                        case 0:
                            clinicSelected = 0;
                            clinicSpinner.setBackgroundColor(spinnerWarning);
                            appointmentTypeSpinner.setVisibility(View.VISIBLE);
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
                            
                        	clinicSelected = idList.get(position - 1);
                            List<String> trueDays = ApiRootModel.getInstance().getClinicsMap().get(clinicSelected).getTrueDays();

                            if(trueDays.size() > 1){
                                setDaySpinner(trueDays);
                                daySpinner.setVisibility(View.VISIBLE);
                                daySpinner.setSelection(0);
                            } else {
                                try {
                                    weekSpinner.setVisibility(View.VISIBLE);
                                    weekSpinner.setSelection(0);
                                    dayOfWeek = dfDayShort.parse(trueDays.get(0));
                                    setWeekSpinner(dayOfWeek);
                                } catch (ParseException e) {
                                    e.printStackTrace();
                                }
                            }
                        	break;
                    }
                    break;   
                case R.id.spnr_day:
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
								dayOfWeek = dfDayShort.parse(daySpinner.getSelectedItem().toString());
								setWeekSpinner(dayOfWeek);
							} catch (ParseException e) {
								e.printStackTrace();
							}
                        	break;
                    }
                    break;
                case R.id.spnr_week:
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