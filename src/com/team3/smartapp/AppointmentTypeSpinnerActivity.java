package com.team3.smartapp;

import android.app.Activity;
import android.os.Bundle;
import android.text.Html;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;


public class AppointmentTypeSpinnerActivity extends Activity {
    private Spinner appointmentSpinner, clinicOptionSpinner, visitOptionSpinner,
            dominoDublinSpinner, weekSpinner;
    private Button appointmentCalendar;
    private ArrayAdapter<CharSequence> appointmentAdapter, clinicAdapter, visitAdapter,
            dominoDublinAdapter, weekAdapter;

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

        weekSpinner = (Spinner) findViewById(R.id.week_spinner);
        weekSpinner.setOnItemSelectedListener(new MySpinnerOnItemSelectedListener());
        weekAdapter = ArrayAdapter.createFromResource(this, R.array.weeks, R.layout.spinner_layout);
        weekAdapter.setDropDownViewResource(R.layout.spinner_layout);
        weekSpinner.setAdapter(weekAdapter);

        appointmentCalendar = (Button) findViewById(R.id.appointment_calendar_button);

        clinicOptionSpinner.setVisibility(View.GONE);
        visitOptionSpinner.setVisibility(View.GONE);
        dominoDublinSpinner.setVisibility(View.GONE);
        weekSpinner.setVisibility(View.GONE);
        appointmentCalendar.setVisibility(View.GONE);
    }

    private class MySpinnerOnItemSelectedListener implements AdapterView.OnItemSelectedListener {
        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            switch (parent.getId()) {
                case R.id.appointment_spinner:
                    switch (position) {
                        case 0:
                            break;
                        case 1:     //Clinic
                            clinicOptionSpinner.setVisibility(View.VISIBLE);
                            visitOptionSpinner.setVisibility(View.GONE);
                            dominoDublinSpinner.setVisibility(View.GONE);
                            weekSpinner.setVisibility(View.GONE);
                            appointmentCalendar.setVisibility(View.GONE);
                            Toast.makeText(AppointmentTypeSpinnerActivity.this,
                                    "Clinics selected",
                                    Toast.LENGTH_LONG).show();
                            break;
                        case 2:     //Visit
                            clinicOptionSpinner.setVisibility(View.GONE);
                            visitOptionSpinner.setVisibility(View.VISIBLE);
                            dominoDublinSpinner.setVisibility(View.GONE);
                            weekSpinner.setVisibility(View.GONE);
                            appointmentCalendar.setVisibility(View.GONE);
                            Toast.makeText(AppointmentTypeSpinnerActivity.this,
                                    "Visits selected",
                                    Toast.LENGTH_LONG).show();
                            break;
                    }
                    break;
                case R.id.clinic_service_option_spinner:
                    switch (position) {
                        case 1:     //Domino Dublin
                            clinicOptionSpinner.setVisibility(View.VISIBLE);
                            visitOptionSpinner.setVisibility(View.GONE);
                            dominoDublinSpinner.setVisibility(View.VISIBLE);
                            weekSpinner.setVisibility(View.GONE);
                            appointmentCalendar.setVisibility(View.GONE);
                            break;
                    }
                    break;
                case R.id.domino_dublin_spinner:
                    switch (position) {
                        case 1:
                            clinicOptionSpinner.setVisibility(View.VISIBLE);
                            visitOptionSpinner.setVisibility(View.GONE);
                            dominoDublinSpinner.setVisibility(View.VISIBLE);
                            weekSpinner.setVisibility(View.VISIBLE);
                            appointmentCalendar.setVisibility(View.GONE);
                            break;
                    }
                    break;
                case R.id.week_spinner:
                    switch (position) {
                        case 1:
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