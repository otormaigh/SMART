package ie.teamchile.smartapp;

import android.os.Bundle;
import android.widget.TextView;

public class ConfirmAppointmentActivity extends MenuInheritActivity {
	private TextView userName, hospitalNumber, appointmentClinic, apptDate, apptTime,
					 duration, email, sms;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirm_appointment);
        
        userName = (TextView)findViewById(R.id.service_user_db);
        hospitalNumber = (TextView) findViewById(R.id.hospital_number_db);
        appointmentClinic = (TextView) findViewById(R.id.appointment_clinic_db);
        apptDate = (TextView)findViewById(R.id.appointment_date_db);
        apptTime = (TextView)findViewById(R.id.appointment_time_db);
        duration = (TextView)findViewById(R.id.duration_db);
        email = (TextView)findViewById(R.id.email_to_db);
        sms = (TextView)findViewById(R.id.sms_to_db);
        
        userName.setText(getIntent().getStringExtra("name"));
        hospitalNumber.setText(getIntent().getStringExtra("hospitalNumber"));
        appointmentClinic.setText(getIntent().getStringExtra("clinicName"));
        apptDate.setText(getIntent().getStringExtra("date"));
        apptTime.setText(getIntent().getStringExtra("time"));
        duration.setText(getIntent().getStringExtra("duration") + " minutes");
        email.setText(getIntent().getStringExtra("email"));
        sms.setText(getIntent().getStringExtra("mobile"));
    }
}