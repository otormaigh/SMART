package com.team3.smartapp;

import Enums.HospitalEnum;
import Enums.RegionEnum;
import android.app.Activity;
import android.os.Bundle;
import android.util.Log;

public class AppointmentCalendarActivity extends Activity {
	private int regionSelected, hospitalSelected, weekSelected, daySelected;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_appointment_calendar);
    }
    public void setOptionsSelected(int regionSelected, int hospitalSelected, int weekSelected, int daySelected){
    	this.regionSelected = regionSelected;
    	this.hospitalSelected = hospitalSelected;
    	this.weekSelected = weekSelected;
    	this.daySelected = daySelected;
    	
    	switch (regionSelected){
    	case 0:
    		break;
    	case 1:
    		RegionEnum domino_dublin = RegionEnum.DOMINO_DUBLIN;
    		Log.d("MYLOG", domino_dublin.toString() + " selected");
    		switch(hospitalSelected){
        	case 0:
        		break;
        	case 1:
        		HospitalEnum nmh_opd = HospitalEnum.NMH_OPD;
        		Log.d("MYLOG", nmh_opd.toString() + " selected");
        		break;
        	case 2:
        		Log.d("MYLOG", "Leopardstown selected");
        		break;
        	case 3:
        		Log.d("MYLOG", "Dun Laoghaire selected");
        		break;
        	case 4:
        		Log.d("MYLOG", "Churchtown selected");
        		break;
        	case 5:
        		Log.d("MYLOG", "NMH selected");
        		break;
        	}
    		break;
    	case 2:
    		Log.d("MYLOG", "Domino Wicklow selected");
    		switch(hospitalSelected){
        	case 0:
        		break;
        	case 1:
        		Log.d("MYLOG", "Greystones (Monday) selected");
        		break;
        	case 2:
        		Log.d("MYLOG", "Greystones (Tuesday) selected");
        		break;
        	case 3:
        		Log.d("MYLOG", "Kilmacanogue selected");
        		break;
        	case 4:
        		Log.d("MYLOG", "Home Visits selected");
        		break;
        	}
    		break;
    	case 3:
    		Log.d("MYLOG", "ETH Dublin selected");
    		switch(hospitalSelected){
        	case 0:
        		break;
        	case 1:
        		Log.d("MYLOG", "Ballinteer selected");
        		break;
        	case 2:
        		Log.d("MYLOG", "Dun Laoghaire selected");
        		break;
        	}
    		break;
    	case 4:
    		Log.d("MYLOG", "ETH Wicklow selected");
    		switch(hospitalSelected){
        	case 0:
        		break;
        	case 1:
        		Log.d("MYLOG", "Ballinteer selected");
        		break;
        	case 2:
        		Log.d("MYLOG", "Dun Laoghaire selected");
        		break;
        	}
    		break;
    	case 5:
    		Log.d("MYLOG", "Satellite selected");
    		switch(hospitalSelected){
        	case 0:
        		break;
        	case 1:
        		Log.d("MYLOG", "Greystones selected");
        		break;
        	case 2:
        		Log.d("MYLOG", "Arklow selected");
        		break;
        	case 3:
        		Log.d("MYLOG", "Newtownmountkennedy selected");
        		break;
        	case 4:
        		Log.d("MYLOG", "Bray selected");
        		break;
        	}
    		break;
    	}
    	switch(weekSelected){
    	case 0:
    		break;
    	case 1:
    		Log.d("MYLOG", "Week 1 selected");
    		break;
    	case 2:
    		Log.d("MYLOG", "Week 2 selected");
    		break;
    	case 3:
    		Log.d("MYLOG", "Week 3 selected");
    		break;
    	case 4:
    		Log.d("MYLOG", "Week 4 selected");
    		break;
    	case 5:
    		Log.d("MYLOG", "Week 5 selected");
    		break;
    	case 6:
    		Log.d("MYLOG", "Week 6 selected");
    		break;
    	}
    	switch(daySelected){
    	case 0:
    		break;
    	case 1:
    		Log.d("MYLOG", "Monday selected");
    		break;
    	case 2:
    		Log.d("MYLOG", "Tuesday selected");
    		break;
    	case 3:
    		Log.d("MYLOG", "Wednesday selected");
    		break;
    	case 4:
    		Log.d("MYLOG", "Thursday selected");
    		break;
    	case 5:
    		Log.d("MYLOG", "Friday selected");
    		break;
    	case 6:
    		Log.d("MYLOG", "Saturday selected");
    		break;
    	case 7:
    		Log.d("MYLOG", "Sunday selected");
    		break;
    	}
    }
}