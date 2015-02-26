package ie.teamchile.smartapp;

import utility.ToastAlert;
import utility.UserSingleton;
import models.Login_model;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

public class QuickMenuActivity extends MenuInheritActivity {
    private Button patientInfo;
    private Button bookAppointment;
    private Button calendar;
    private Button todaysAppointments;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quick_menu);

        patientInfo = (Button) findViewById(R.id.patientInfo);
        patientInfo.setOnClickListener(new ButtonClick());
        bookAppointment = (Button) findViewById(R.id.bookAppointment);
        bookAppointment.setOnClickListener(new ButtonClick());
        calendar = (Button) findViewById(R.id.calendar);
        calendar.setOnClickListener(new ButtonClick());
        todaysAppointments = (Button) findViewById(R.id.todays_appointments);
        todaysAppointments.setOnClickListener(new ButtonClick());

        Login_model login = new Login_model();
        Log.d("MYLOG", "Before Other get token");
        Log.d("MYLOG", "Other get token: " + Login_model.getToken());
    }
    private class ButtonClick implements View.OnClickListener {
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.patientInfo:
                    Intent intentPatient = new Intent(QuickMenuActivity.this, ServiceUserSearchActivity.class);
                    startActivity(intentPatient);
                    break;
                case R.id.bookAppointment:
                    Intent intentBook = new Intent(QuickMenuActivity.this, AppointmentTypeSpinnerActivity.class);
                    startActivity(intentBook);
                    break;
                case R.id.calendar:
                    Intent intentCalendar = new Intent(QuickMenuActivity.this, CalendarActivity.class);
                    startActivity(intentCalendar);
                    break;
                case R.id.todays_appointments:
                    Intent intentToday = new Intent(QuickMenuActivity.this, TodayAppointmentActivity.class);
                    startActivity(intentToday);
                    break;
            }
        }
    }
    
    @Override
    public void onBackPressed() {
    	if(UserSingleton.getUserSingleton().isLoggedIn()) {
    		ToastAlert ta = new ToastAlert(getBaseContext(), 
        			"  Already logged in, \n  Logout?");
    	}else {
    		
    	}    	
    }
}