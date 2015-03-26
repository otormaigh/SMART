package ie.teamchile.smartapp.activities;

import ie.teamchile.smartapp.R;
import ie.teamchile.smartapp.utility.AppointmentSingleton;
import android.os.Bundle;
import android.util.Log;


public class CalendarActivity extends MenuInheritActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calendar);   
    }   
}