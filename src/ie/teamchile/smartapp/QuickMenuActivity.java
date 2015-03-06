package ie.teamchile.smartapp;

import utility.ToastAlert;
import utility.UserSingleton;
import android.app.admin.DevicePolicyManager;
import android.content.ComponentCallbacks2;
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
    private boolean isViewVisible = false;
    DevicePolicyManager deviceManager;

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
        isViewVisible = true;
        
        Log.d("MYLOG", "Before Other get token");
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

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		System.out.println(UserSingleton.getUserSingleton().getUsername() + " " +
				UserSingleton.getUserSingleton().getPassword());
	}
	
	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		isViewVisible = true;		
	}

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		//checkIsInBackground();
	}

	@Override
	protected void onStop() {
		// TODO Auto-generated method stub
		super.onStop();
	}

	@Override
	public void onTrimMemory(int level) {
		// TODO Auto-generated method stub
		super.onTrimMemory(level);
		if(level >= ComponentCallbacks2.TRIM_MEMORY_UI_HIDDEN) {
			isViewVisible = false;
			ToastAlert ta = new ToastAlert(getBaseContext(), "View is now hidden");				
		}
	}
}