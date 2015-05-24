package ie.teamchile.smartapp.activities;

import ie.teamchile.smartapp.R;
import ie.teamchile.smartapp.utility.ServiceUserSingleton;
import ie.teamchile.smartapp.utility.ToastAlert;

import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.Address;
import android.location.Geocoder;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TabHost;
import android.widget.TabHost.TabSpec;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

public class ServiceUserActivity extends MenuInheritActivity {
	private final CharSequence[] userContactList = {"Call Mobile", "Send SMS", "Send Email"};
	private final CharSequence[] kinContactList = {"Call Mobile", "Send SMS"};
	private TextView anteAge, anteGestation, anteParity, anteDeliveryTime, anteBloodGroup, anteRhesus;
	private TextView contactHospitalNumber, contactEmail, contactMobileNumber, contactRoad,
					 contactCounty, contactPostCode, contactNextOfKinName, contactAge, 
					 contactNextOfKinContactNumber, contactGestation, contactParity;
	private TextView postBirthMode, postPerineum, postAntiD, postDeliveryDate, postDeliveryTime,
					 postDaysSinceBirth, postBabyGender, postBirthWeight, postVitK, postHearing, 
					 postFeeding, postNBST, lastPeriod;
	private LinearLayout serviceUserContact, serviceUserAddress, serviceUserKin;	
	private String dob = "", age = "", hospitalNumber, email, mobile, userName, kinName,  
				   kinMobile, road, county, postCode, gestation, parity, estimtedDelivery,
				   perineum, birthMode, babyGender, babyWeightGrams = "", babyWeightKg = "", 
				   vitK, hearing, antiD, feeding, nbst, deliveryDateTime, daysSinceBirth,
				   userCall, userSMS, userEmail, lastPeriodDate;
	private List<String> babyID;
	private double grams = 0.0;
	private String sex_male = "ale";
	private String sex_female = "emale";
	private Dialog dialog;
	private Button bookAppointmentButton;
  	private TableRow tableParity;
	private DateFormat sdfDateTime = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZ", Locale.getDefault());
	private DateFormat sdfDate = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
	private DateFormat sdfMonthFullName = new SimpleDateFormat("dd MMMM yyyy", Locale.getDefault());
	private DateFormat sdfAMPM = new SimpleDateFormat("HH:mm a", Locale.getDefault());
	private Date dobAsDate = null;
	private Intent userCallIntent, userSmsIntent, userEmailIntent;
	private Calendar cal = Calendar.getInstance();
	private int b;		//position of most recent baby in list
	private int p;		//position of most recent pregnancy in list
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentForNav(R.layout.activity_service_user_tabhost);
		TabHost tabHost = (TabHost)findViewById(android.R.id.tabhost);
		tabHost.setup();

        TabSpec tab1 = tabHost.newTabSpec("Ante");
        tab1.setContent(R.id.tab_ante);
        tab1.setIndicator("Ante Natal");
        tabHost.addTab(tab1);

        TabSpec tab2 = tabHost.newTabSpec("Contact");
        tab2.setContent(R.id.tab_contact);
        tab2.setIndicator("Contact");
        tabHost.addTab(tab2);

        TabSpec tab3 = tabHost.newTabSpec("Post");
        tab3.setContent(R.id.tab_post);
        tab3.setIndicator("Post Natal");
        tabHost.addTab(tab3);

        tabHost.setCurrentTab(1);
        
        serviceUserContact = (LinearLayout)findViewById(R.id.service_user_contact);
        serviceUserContact.setOnClickListener(new ButtonClick());
        serviceUserAddress = (LinearLayout)findViewById(R.id.service_user_address);
        serviceUserAddress.setOnClickListener(new ButtonClick());
        serviceUserKin = (LinearLayout)findViewById(R.id.service_user_kin);
        serviceUserKin.setOnClickListener(new ButtonClick());
        
        anteAge = (TextView)findViewById(R.id.age_ante_natal);
		anteGestation = (TextView)findViewById(R.id.gestation);
		anteParity = (TextView)findViewById(R.id.parity_ante_natal);
		anteDeliveryTime = (TextView)findViewById(R.id.deliveryTime);
		anteBloodGroup = (TextView)findViewById(R.id.blood_group);
		anteRhesus = (TextView)findViewById(R.id.rhesus);
		lastPeriod = (TextView)findViewById(R.id.last_period);
		
		contactHospitalNumber = (TextView) findViewById(R.id.hospital_number);
		contactAge = (TextView) findViewById(R.id.age);
		contactEmail = (TextView) findViewById(R.id.email);
		contactMobileNumber = (TextView) findViewById(R.id.mobile_number);
		contactRoad = (TextView) findViewById(R.id.road);
		contactCounty = (TextView) findViewById(R.id.county);
		contactPostCode = (TextView) findViewById(R.id.post_code);
		contactNextOfKinName = (TextView) findViewById(R.id.next_of_kin_name);
		contactNextOfKinContactNumber = (TextView) findViewById(R.id.next_of_kin_contact_number);
		contactGestation = (TextView) findViewById(R.id.g);
		contactParity = (TextView)findViewById(R.id.p);
		
		postBirthMode = (TextView)findViewById(R.id.birth_mode);
		postPerineum = (TextView)findViewById(R.id.perineum);
		postAntiD = (TextView)findViewById(R.id.anti_d);
		postDeliveryDate = (TextView)findViewById(R.id.date_of_delivery);
		postDeliveryTime = (TextView)findViewById(R.id.time_of_delivery);
		postDaysSinceBirth = (TextView)findViewById(R.id.days_since_birth);
		postBabyGender = (TextView)findViewById(R.id.sex_of_baby);
		postBirthWeight = (TextView)findViewById(R.id.birth_weight);
		postVitK = (TextView)findViewById(R.id.vitk);
		postHearing = (TextView)findViewById(R.id.hearing);
		postFeeding = (TextView)findViewById(R.id.feeding);
		postNBST = (TextView)findViewById(R.id.nbst);
		
		bookAppointmentButton = (Button) findViewById(R.id.book_appointment);
		bookAppointmentButton.setOnClickListener(new ButtonClick());
		
		tableParity = (TableRow)findViewById(R.id.button_parity);
		tableParity.setOnClickListener(new ButtonClick());
		
		try{
			dob = ServiceUserSingleton.getInstance().getUserDOB().get(0);
			if(!dob.equals("null")){
				age = getAge(dob);					
			}
			hospitalNumber = ServiceUserSingleton.getInstance().getUserHospitalNumber().get(0);
			email = ServiceUserSingleton.getInstance().getUserEmail().get(0);
			mobile = ServiceUserSingleton.getInstance().getUserMobilePhone().get(0);
			userName = ServiceUserSingleton.getInstance().getUserName().get(0);
			kinName = ServiceUserSingleton.getInstance().getUserNextOfKinName().get(0);
			kinMobile = ServiceUserSingleton.getInstance().getUserNextOfKinPhone().get(0);
			road = ServiceUserSingleton.getInstance().getUserHomeAddress().get(0);
			county = ServiceUserSingleton.getInstance().getUserHomeCounty().get(0);
			postCode = ServiceUserSingleton.getInstance().getUserHomePostCode().get(0);
			perineum = ServiceUserSingleton.getInstance().getPregnancyPerineum().get(p);
			birthMode = formatArrayString(ServiceUserSingleton.getInstance().getPregnancyBirthMode().get(p));
			babyGender = ServiceUserSingleton.getInstance().getBabyGender().get(b);
			babyWeightGrams = ServiceUserSingleton.getInstance().getBabyWeight().get(b);
			if(!babyWeightGrams.equals("null")){
				grams =  Double.parseDouble(babyWeightGrams);
				babyWeightKg = String.valueOf(getGramsToKg(grams));
			}
			babyID = ServiceUserSingleton.getInstance().getPregnancyBabyIDs();
			gestation = ServiceUserSingleton.getInstance().getPregnancyGestation().get(p);
			parity = ServiceUserSingleton.getInstance().getUserParity().get(0);
			estimtedDelivery = ServiceUserSingleton.getInstance().getPregnancyEstimatedDeliveryDate().get(p);
			lastPeriodDate = ServiceUserSingleton.getInstance().getPregnancyLastMenstrualPeriod().get(p);
			if(!estimtedDelivery.equals("null")){
				getRecentPregnancy();
			}
			vitK = ServiceUserSingleton.getInstance().getBabyVitK().get(b);
			hearing = ServiceUserSingleton.getInstance().getBabyHearing().get(b);
			antiD = ServiceUserSingleton.getInstance().getPregnancyAntiD().get(p);
			feeding = ServiceUserSingleton.getInstance().getPregnancyFeeding().get(p);
			nbst = ServiceUserSingleton.getInstance().getBabyNewBornScreeningTest().get(b);
			deliveryDateTime = ServiceUserSingleton.getInstance().getBabyDeliveryDateTime().get(b);		
			if(!deliveryDateTime.equals("null")){	
				getRecentBaby();
				daysSinceBirth = getNoOfDays(deliveryDateTime);
			}
			setActionBarTitle(userName);
			
			if(parity.equals("0 + 0")){
				tableParity.setEnabled(false);
			}
			
			anteParity.setText(parity);
			anteGestation.setText(ServiceUserSingleton.getInstance().getPregnancyGestation().get(p));
			anteRhesus.setText(ServiceUserSingleton.getInstance().getUserRhesus().get(0));
			anteBloodGroup.setText(ServiceUserSingleton.getInstance().getUserBloodGroup().get(0));
			anteDeliveryTime.setText(getEstimateDeliveryDate(estimtedDelivery));
			anteAge.setText(age);
			
			contactAge.setText(age);
			contactHospitalNumber.setText(hospitalNumber);
			contactEmail.setText(email);
			contactMobileNumber.setText(mobile);
			contactRoad.setText(road);
			contactCounty.setText(county);
			contactPostCode.setText(postCode);
			contactNextOfKinName.setText(kinName);
			contactNextOfKinContactNumber.setText(kinMobile);
			contactGestation.setText(gestation);
			contactParity.setText(parity);	
			
			postVitK.setText(vitK);
			postHearing.setText(hearing);
			postAntiD.setText(antiD);
			postFeeding.setText(feeding);
			postNBST.setText(nbst);
			postDeliveryDate.setText(getDeliveryDate(deliveryDateTime));
			postDeliveryTime.setText(getDeliveryTime(deliveryDateTime));		
			postPerineum.setText(perineum);		
			postBirthMode.setText(birthMode);
			postBirthWeight.setText(babyWeightKg);
			if(!lastPeriodDate.equals("null")){
				lastPeriod.setText(getLastPeriodDate(lastPeriodDate));
			}
			if(babyGender.equalsIgnoreCase("M")){
				postBabyGender.setText(babyGender + sex_male);
			}
			else if (babyGender.equalsIgnoreCase("F")){
				postBabyGender.setText(babyGender + sex_female);
			}		
			postDaysSinceBirth.setText(daysSinceBirth);
		} catch (NullPointerException e){
			e.printStackTrace();
		}
	}
	
	private class ButtonClick implements View.OnClickListener, DialogInterface {
		public void onClick(View v) {
			switch (v.getId()) {
			case R.id.book_appointment:
				setSharedPrefs();				
				Intent intentBook = new Intent(ServiceUserActivity.this, AppointmentTypeSpinnerActivity.class);
				startActivity(intentBook);
				break;
			case R.id.service_user_contact :
				dialogContact(userContactList);
				break;
			case R.id.service_user_kin :
				dialogContact(kinContactList);
				break;					
			case R.id.service_user_address :
				gotoMaps();
				break;
			case R.id.button_parity:
				Intent intent6 = new Intent(ServiceUserActivity.this, ParityDetailsActivity.class);
				startActivity(intent6);
				break;
			}
		}
		
		@Override
		public void cancel() { }
		
		@Override
		public void dismiss() { }
	}
	
	private void dialogContact(final CharSequence[] items) {
	    AlertDialog.Builder builder = new AlertDialog.Builder(this);
	    builder.setTitle(R.string.contact_title);

	    builder.setItems(items, new DialogInterface.OnClickListener() {
	        public void onClick(DialogInterface dialog, int item) {
	            if (items[item].equals("Call Mobile")) { makeCall(); }

	            if (items[item].equals("Send SMS")) { sendSms(); }
	            
	            if (items[item].equals("Send Email")) { sendEmail(); }
	        }
	    });
	    AlertDialog alert = builder.create();
	    alert.show();
	    alert.setCanceledOnTouchOutside(true);
	}
	
	private void makeCall(){
		userCall = "tel:" + contactMobileNumber.getText().toString();
		userCallIntent = new Intent(Intent.ACTION_DIAL,
				Uri.parse(userCall));
		try {
			startActivity(userCallIntent);
		} catch (android.content.ActivityNotFoundException ex) {
			Toast.makeText(ServiceUserActivity.this,
					"Call failed, please try again later.",
					Toast.LENGTH_LONG).show();
		}
	}
	
	private void sendSms() {
		userSMS = contactMobileNumber.getText().toString();
		userSmsIntent = new Intent(Intent.ACTION_VIEW);
		userSmsIntent.setType("vnd.android-dir/mms-sms");
		userSmsIntent.putExtra("address", userSMS);
		try {
			startActivity(userSmsIntent);
		} catch (android.content.ActivityNotFoundException ex) {
			Toast.makeText(ServiceUserActivity.this,
					"SMS failed, please try again later.",
					Toast.LENGTH_LONG).show();
		}
	}
	
	private void sendEmail() {
		userEmail = contactEmail.getText().toString();		
		userEmailIntent = new Intent(Intent.ACTION_SEND);
		userEmailIntent.setClassName("com.google.android.gm","com.google.android.gm.ComposeActivityGmail");
		userEmailIntent.setType("message/rfc822");
		userEmailIntent.putExtra(android.content.Intent.EXTRA_EMAIL,new String[] {userEmail});
		userEmailIntent.putExtra(Intent.EXTRA_SUBJECT, ""); 
		userEmailIntent.putExtra(Intent.EXTRA_TEXT, ""); 
		userEmailIntent.setType("plain/text"); 
		try {
			startActivity(userEmailIntent);
		} catch (android.content.ActivityNotFoundException ex) {
			Toast.makeText(ServiceUserActivity.this,
					"Email failed, please try again later.",
					Toast.LENGTH_LONG).show();
		}
	}
	
	private void gotoMaps() {
        new AlertDialog.Builder(this)
        .setTitle(R.string.user_address_title)
        .setMessage(R.string.user_address_message)
        .setNegativeButton(R.string.No, new DialogInterface.OnClickListener() {
		public void onClick(DialogInterface dialoginterface, int i) {			
		}})
		.setPositiveButton(R.string.Yes, new DialogInterface.OnClickListener() {
		    public void onClick(DialogInterface dialoginterface, int i) {
		    	String addr = "" + contactRoad.getText().toString() + ", " + contactCounty.getText().toString() + ", " + contactPostCode.getText().toString();
		    	Log.d("bugs", "geoCode: " + getGeoCoodinates(addr+"?z=12"));
		    	Uri uri = Uri.parse(getGeoCoodinates(addr)); //"geo:47.6,-122.3?z=18"
		    	Log.d("bugs", "addr: " + addr);
		    	Intent intent = new Intent(Intent.ACTION_VIEW, uri);
		        intent.setClassName("com.google.android.apps.maps", "com.google.android.maps.MapsActivity");
		    	startActivity(intent);
        }	
		}).show();		
	}
	
	private String getAge(String dob) {
		try {			
			dobAsDate = sdfDate.parse(dob);
			cal.setTime(dobAsDate);
		} catch (ParseException | NullPointerException e) {
			e.printStackTrace();
		}
		int year = cal.get(Calendar.YEAR);
		int month = cal.get(Calendar.MONTH);
		int day = cal.get(Calendar.DAY_OF_MONTH);

		Date now = new Date();
		int nowMonth = now.getMonth() + 1;
		int nowYear = now.getYear() + 1900;
		int result = nowYear - year;

		if (month > nowMonth) {
			result--;
		} else if (month == nowMonth) {
			int nowDay = now.getDate();

			if (day > nowDay) {
				result--;
			}
		}
		return String.valueOf(result);
	}
	
	private String getEstimateDeliveryDate(String edd){
        Date date;
        String ed = null;
		try{
			date = sdfDate.parse(edd);
	        ed = sdfMonthFullName.format(date);
		} catch (ParseException e) {
			e.printStackTrace();
		}
	    return ed;
	}	

	public String getLastPeriodDate(String edd){
        Date date;
        String ed = null;
		try{
			date = sdfDate.parse(edd);
	        ed = sdfMonthFullName.format(date);
		} catch (ParseException e) {
			e.printStackTrace();
		}
	    return ed;
	}	

	protected String getDeliveryDate(String edd){
       Date date;
       String dateOfDevelivery = null;
		try{
			date = sdfDateTime.parse(edd);
	        dateOfDevelivery = sdfMonthFullName.format(date);
		} catch (ParseException e) {
			e.printStackTrace();
		}
	    return dateOfDevelivery;
	}	
	
	private String getDeliveryTime(String edd) {
		String deliveryTime = null;
		Date date;
		try {
			date = sdfDateTime.parse(edd);

			deliveryTime = sdfAMPM.format(date);
			date = sdfDateTime.parse(edd);

		} catch (ParseException e) {
			e.printStackTrace();
		}
		return deliveryTime;
	}	

	private String getNoOfDays(String dateOfDelivery){
		int numOfDays = 0;
		try {
			Date dodAsDate = sdfDateTime.parse(deliveryDateTime);
			cal = Calendar.getInstance();
			Date now = cal.getTime();
			numOfDays = (int)((now.getTime() - dodAsDate.getTime()) / (1000 * 60 * 60 * 24)) + 1;
		} catch (ParseException e) {
			e.printStackTrace();
		}		
        
        return String.valueOf(numOfDays);  
	}
    
    private double getGramsToKg(double grams){
	    	double kg = 0.0;
	    	kg = grams/1000;
	    	return kg;
    }
    
    private String formatArrayString(String toBeFormatted){
    	String formatedString = toBeFormatted
    		    .replace(",", ", ")  //remove the commas
    		    .replace("[", "")  //remove the right bracket
    		    .replace("]", "")  //remove the left bracket
    		    .replace("\"", "")
    		    .trim(); 
    	return formatedString;
    }
    
    private void setSharedPrefs(){
    	Log.d("bugs", "baby ids: " + babyID);
    	SharedPreferences.Editor prefs = getSharedPreferences("SMART", MODE_PRIVATE).edit();
    	if(babyID.get(p).equals("[]")){
    		prefs.putString("visit_type_str", "Antenatal");
    		prefs.putString("visit_type", "ante-natal");
    	}else{
    		prefs.putString("visit_type_str", "Postnatal");
    		prefs.putString("visit_type", "post-natal");
    	}
		prefs.putString("name", ServiceUserSingleton.getInstance().getUserName().get(0));
		prefs.putString("id", ServiceUserSingleton.getInstance().getUserID().get(0));
		prefs.putBoolean("reuse", true);
		prefs.commit();
    }

    private void getRecentBaby(){
    	List<String> babyDateTime = ServiceUserSingleton.getInstance().getBabyDeliveryDateTime();
    	List<Date> asDate = new ArrayList<Date>();
    	for(int i = 0; i < babyDateTime.size(); i++){
    		try {
				asDate.add(sdfDateTime.parse(babyDateTime.get(i)));
			} catch (ParseException e) {
				e.printStackTrace();
			}
    	}    
    	b = asDate.indexOf(Collections.max(asDate));
    }
    
    private void getRecentPregnancy(){
    	List<String> edd = ServiceUserSingleton.getInstance().getPregnancyEstimatedDeliveryDate();
    	List<Date> asDate = new ArrayList<Date>();
    	for(int i = 0; i < edd.size(); i++){
    		try {
				asDate.add(sdfDate.parse(edd.get(i)));
			} catch (ParseException e) {
				e.printStackTrace();
			}
    	}    
    	p = asDate.indexOf(Collections.max(asDate));
    }
    
    private String getGeoCoodinates(String address) {
    	Geocoder geocoder = new Geocoder(ServiceUserActivity.this);
        Log.d("The Address", address);
        List<Address> addresses = null;
        try {
            if(!address.isEmpty() && address != null)
                addresses = geocoder.getFromLocationName(address, 1);
        }catch (IOException ioe) {
            ioe.printStackTrace();
        }

        if(addresses != null && addresses.size() > 0) {
            double latitude = addresses.get(0).getLatitude();
            double longitude = addresses.get(0).getLongitude();
            Log.d("Coordinates Found", String.valueOf(latitude));
            Log.d("Coordinates Found", String.valueOf(longitude));
            //"geo:47.6,-122.3?z=18"
            new ToastAlert(this,"geo:" + String.valueOf(latitude) + "," + String.valueOf(longitude) + "", false);
            return "geo:" +String.valueOf(latitude) + "," +String.valueOf(longitude) + "?z=12" +
            	   "&q=" + String.valueOf(latitude) + "," + String.valueOf(longitude) +
            	   "(" + userName + ")";
        }
        return "Not Found";        
    }
}