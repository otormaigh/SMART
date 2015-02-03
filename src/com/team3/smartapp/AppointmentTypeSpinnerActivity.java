package com.team3.smartapp;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

public class AppointmentTypeSpinnerActivity extends Activity {
    private Spinner appointmentSpinner, clinicOptionSpinner, visitOptionSpinner,
            dominoDublinSpinner, dominoWicklowSpinner, ethDublinSpinner, ethWicklowSpinner, satelliteSpinner, weekSpinner;
    private Button appointmentCalendar;
    private ArrayAdapter<CharSequence> appointmentAdapter, clinicAdapter, visitAdapter,
            dominoDublinAdapter, dominoWicklowAdapter, ethDublinAdapter, ethWicklowAdapter, satelliteAdapter, weekAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_appointment_type_spinner);

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
        appointmentCalendar.setVisibility(View.GONE);
    }

    private class ButtonClick implements View.OnClickListener {
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.appointment_calendar_button:
                    //Intent intent = new Intent(AppointmentTypeSpinnerActivity.this, AppointmentTypeSpinnerActivity.class);
                    //startActivity(intent);
                    Log.d("MYLOG", "Button Clicked");
                    Toast.makeText(AppointmentTypeSpinnerActivity.this, "Button Pressed", Toast.LENGTH_SHORT).show();
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
                            appointmentCalendar.setVisibility(View.GONE);
                            visitOptionSpinner.setSelection(0);
                            break;
                    }
                    break;
                case R.id.clinic_service_option_spinner:
                    switch (position) {
                        case 0:
                            appointmentSpinner.setVisibility(View.VISIBLE);
                            clinicOptionSpinner.setVisibility(View.VISIBLE);
                            visitOptionSpinner.setVisibility(View.GONE);
                            dominoDublinSpinner.setVisibility(View.GONE);
                            dominoWicklowSpinner.setVisibility(View.GONE);
                            ethDublinSpinner.setVisibility(View.GONE);
                            ethWicklowSpinner.setVisibility(View.GONE);
                            satelliteSpinner.setVisibility(View.GONE);
                            weekSpinner.setVisibility(View.GONE);
                            appointmentCalendar.setVisibility(View.GONE);
                            break;
                        case 1:     //Domino Dublin
                            dominoDublinSpinner.setVisibility(View.VISIBLE);
                            dominoDublinSpinner.setSelection(0);
                            break;
                        case 2:     //Domino Wicklow
                            dominoWicklowSpinner.setVisibility(View.VISIBLE);
                            dominoWicklowSpinner.setSelection(0);
                            break;
                        case 3:     //ETH Dublin
                            ethDublinSpinner.setVisibility(View.VISIBLE);
                            ethDublinSpinner.setSelection(0);
                            break;
                        case 4:     //ETH Wicklow
                            ethWicklowSpinner.setVisibility(View.VISIBLE);
                            ethWicklowSpinner.setSelection(0);
                            break;
                        case 5:     //Satellite
                            satelliteSpinner.setVisibility(View.VISIBLE);
                            satelliteSpinner.setSelection(0);
                            break;
                    }
                    break;
                case R.id.domino_dublin_spinner:
                    switch (position) {
                        case 0:
                            appointmentSpinner.setVisibility(View.VISIBLE);
                            clinicOptionSpinner.setVisibility(View.VISIBLE);
                            visitOptionSpinner.setVisibility(View.GONE);
                            dominoDublinSpinner.setVisibility(View.VISIBLE);
                            dominoWicklowSpinner.setVisibility(View.GONE);
                            ethDublinSpinner.setVisibility(View.GONE);
                            ethWicklowSpinner.setVisibility(View.GONE);
                            satelliteSpinner.setVisibility(View.GONE);
                            weekSpinner.setVisibility(View.GONE);
                            appointmentCalendar.setVisibility(View.GONE);
                            break;
                        case 1:     //NMH(OPD Location)
                            weekSpinner.setVisibility(View.VISIBLE);
                            weekSpinner.setSelection(0);
                            break;
                        case 2:     //Leopardstown
                            weekSpinner.setVisibility(View.VISIBLE);
                            weekSpinner.setSelection(0);
                            break;
                        case 3:     //Dun Laoghaire
                            weekSpinner.setVisibility(View.VISIBLE);
                            weekSpinner.setSelection(0);
                            break;
                        case 4:     //Churchtown
                            weekSpinner.setVisibility(View.VISIBLE);
                            weekSpinner.setSelection(0);
                            break;
                        case 5:     //NMH
                            weekSpinner.setVisibility(View.VISIBLE);
                            weekSpinner.setSelection(0);
                            break;
                    }
                    break;
                case R.id.domino_wicklow_spinner:
                    switch (position) {
                        case 0:
                            appointmentSpinner.setVisibility(View.VISIBLE);
                            clinicOptionSpinner.setVisibility(View.VISIBLE);
                            visitOptionSpinner.setVisibility(View.GONE);
                            dominoDublinSpinner.setVisibility(View.GONE);
                            dominoWicklowSpinner.setVisibility(View.VISIBLE);
                            ethDublinSpinner.setVisibility(View.GONE);
                            ethWicklowSpinner.setVisibility(View.GONE);
                            satelliteSpinner.setVisibility(View.GONE);
                            weekSpinner.setVisibility(View.GONE);
                            appointmentCalendar.setVisibility(View.GONE);
                            break;
                        case 1:     //Greystones (Monday)
                            weekSpinner.setVisibility(View.VISIBLE);
                            weekSpinner.setSelection(0);
                            break;
                        case 2:     //Greystones (Tuesday)
                            weekSpinner.setVisibility(View.VISIBLE);
                            weekSpinner.setSelection(0);
                            break;
                        case 3:     //Kilmacanogue
                            weekSpinner.setVisibility(View.VISIBLE);
                            weekSpinner.setSelection(0);
                            break;
                        case 4:     //Home Visits
                            weekSpinner.setVisibility(View.VISIBLE);
                            weekSpinner.setSelection(0);
                            break;
                    }
                    break;
                case R.id.eth_dublin_spinner:
                    switch (position) {
                        case 0:
                            appointmentSpinner.setVisibility(View.VISIBLE);
                            clinicOptionSpinner.setVisibility(View.VISIBLE);
                            visitOptionSpinner.setVisibility(View.GONE);
                            dominoDublinSpinner.setVisibility(View.GONE);
                            dominoWicklowSpinner.setVisibility(View.GONE);
                            ethDublinSpinner.setVisibility(View.VISIBLE);
                            ethWicklowSpinner.setVisibility(View.GONE);
                            satelliteSpinner.setVisibility(View.GONE);
                            weekSpinner.setVisibility(View.GONE);
                            appointmentCalendar.setVisibility(View.GONE);
                            break;
                        case 1:     //Ballinteer
                            weekSpinner.setVisibility(View.VISIBLE);
                            weekSpinner.setSelection(0);
                            break;
                        case 2:     //Dun Laoghaire
                            weekSpinner.setVisibility(View.VISIBLE);
                            weekSpinner.setSelection(0);
                            break;
                    }
                    break;
                case R.id.eth_wicklow_spinner:
                    switch (position) {
                        case 0:
                            appointmentSpinner.setVisibility(View.VISIBLE);
                            clinicOptionSpinner.setVisibility(View.VISIBLE);
                            visitOptionSpinner.setVisibility(View.GONE);
                            dominoDublinSpinner.setVisibility(View.GONE);
                            dominoWicklowSpinner.setVisibility(View.GONE);
                            ethDublinSpinner.setVisibility(View.GONE);
                            ethWicklowSpinner.setVisibility(View.VISIBLE);
                            satelliteSpinner.setVisibility(View.GONE);
                            weekSpinner.setVisibility(View.GONE);
                            appointmentCalendar.setVisibility(View.GONE);
                            break;
                        case 1:     //Ballinteer
                            weekSpinner.setVisibility(View.VISIBLE);
                            weekSpinner.setSelection(0);
                            break;
                        case 2:     //Dun Laoghaire
                            weekSpinner.setVisibility(View.VISIBLE);
                            weekSpinner.setSelection(0);
                            break;
                    }
                    break;
                case R.id.satellite_spinner:
                    switch (position) {
                        case 0:
                            appointmentSpinner.setVisibility(View.VISIBLE);
                            clinicOptionSpinner.setVisibility(View.VISIBLE);
                            visitOptionSpinner.setVisibility(View.GONE);
                            dominoDublinSpinner.setVisibility(View.GONE);
                            dominoWicklowSpinner.setVisibility(View.GONE);
                            ethDublinSpinner.setVisibility(View.GONE);
                            ethWicklowSpinner.setVisibility(View.GONE);
                            satelliteSpinner.setVisibility(View.VISIBLE);
                            weekSpinner.setVisibility(View.GONE);
                            appointmentCalendar.setVisibility(View.GONE);
                            break;
                        case 1:     //Greystones
                            weekSpinner.setVisibility(View.VISIBLE);
                            weekSpinner.setSelection(0);
                            break;
                        case 2:     //Arklow
                            weekSpinner.setVisibility(View.VISIBLE);
                            weekSpinner.setSelection(0);
                            break;
                        case 3:     //Newtownmountkennedy
                            weekSpinner.setVisibility(View.VISIBLE);
                            weekSpinner.setSelection(0);
                            break;
                        case 4:     //Bray
                            weekSpinner.setVisibility(View.VISIBLE);
                            weekSpinner.setSelection(0);
                            break;
                    }
                    break;
                case R.id.week_spinner:
                    switch (position) {
                        case 0:
                            appointmentCalendar.setVisibility(View.GONE);
                            break;
                        case 1:     //Week 1
                            appointmentCalendar.setVisibility(View.VISIBLE);
                            break;
                        case 2:     //Week 2
                            appointmentCalendar.setVisibility(View.VISIBLE);
                            break;
                        case 3:     //Week 3
                            appointmentCalendar.setVisibility(View.VISIBLE);
                            break;
                        case 4:     //Week 4
                            appointmentCalendar.setVisibility(View.VISIBLE);
                            break;
                        case 5:     //Week 5
                            appointmentCalendar.setVisibility(View.VISIBLE);
                            break;
                        case 6:     //Week 6
                            appointmentCalendar.setVisibility(View.VISIBLE);
                            break;
                    }
                    break;
            }
        }

        @Override
        public void onNothingSelected(AdapterView<?> parent) {
        }
    }
}