package ie.teamchile.smartapp.activities;

import ie.teamchile.smartapp.R;
import ie.teamchile.smartapp.R.layout;
import ie.teamchile.smartapp.utility.AppointmentSingleton;
import android.os.Bundle;
import android.util.Log;


public class CalendarActivity extends MenuInheritActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calendar);
        
        
        
        
		Log.d("APPTSingle", 
				" clinis id: " + AppointmentSingleton.getInstance().getClinicID("1") +
				"\n date: " + AppointmentSingleton.getInstance().getDate("1") +
				"\n appointment id: " + AppointmentSingleton.getInstance().getAppointmentID("1") +
				"\n priority: " + AppointmentSingleton.getInstance().getPriority("1") +
				"\n service option id: " + AppointmentSingleton.getInstance().getServiceOptionID("1") +
				"\n service provider id: " + AppointmentSingleton.getInstance().getServiceProviderID("1") +
				"\n gestation: " + AppointmentSingleton.getInstance().getGestation("1") + 
				"\n name: " + AppointmentSingleton.getInstance().getName("1") + 
				"\n service user id: " + AppointmentSingleton.getInstance().getServiceUserID("1") + 
				"\n time: " + AppointmentSingleton.getInstance().getTime("1") + 
				"\n visit log: " + AppointmentSingleton.getInstance().getVisitLogs("1") +
				"\n visit type: " + AppointmentSingleton.getInstance().getVisitType("1")); 
        
    }   
}
