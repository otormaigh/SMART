package ie.teamchile.smartapp;

import android.os.Bundle;
import android.widget.TextView;

public class ConfirmAppointmentActivity extends MenuInheritActivity {
	private TextView userName, apptTime, apptDate;
	String details;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirm_appointment);
        userName = (TextView)findViewById(R.id.service_user_db);
        apptTime = (TextView)findViewById(R.id.appointment_time_db);
        apptDate = (TextView)findViewById(R.id.appointment_date_db);
        
        userName.setText(getIntent().getStringExtra("name"));
        apptTime.setText(getIntent().getStringExtra("time"));
        apptDate.setText(getIntent().getStringExtra("date"));
    }
}