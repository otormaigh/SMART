package ie.teamchile.smartapp.activities;

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

import java.io.IOException;
import java.text.ParseException;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import ie.teamchile.smartapp.R;
import ie.teamchile.smartapp.model.ApiRootModel;
import ie.teamchile.smartapp.util.ToastAlert;

public class ServiceUserActivity extends BaseActivity {
	private final CharSequence[] userContactList = {"Call Mobile", "Send SMS", "Send Email"};
	private final CharSequence[] kinContactList = {"Call Mobile", "Send SMS"};
	private TextView tvAnteAge, tvAnteGestation, tvAnteParity, tvAnteDeliveryTime,
					 tvAnteBloodGroup, tvAnteRhesus, tvAnteLastPeriod;
	private TextView tvUsrHospitalNumber, tvUsrEmail, tvUsrMobileNumber, tvUsrRoad,
					 tvUsrCounty, tvUsrPostCode, tvUsrNextOfKinName, tvUsrAge,
					 tvUsrKinContact, tvUsrGestation, tvUsrParity;
	private TextView tvPostBirthMode, tvPostPerineum, tvPostAntiD, tvPostDeliveryDate, tvPostDeliveryTIme,
					 tvPostDaysSinceBirth, tvPostBabyGender, tvPostBirthWeight, tvPostVitK, tvPostHearing,
					 tvPostFeeding, tvPostNBST;
	private LinearLayout llUserContact, llUserAddress, llUserKinContact;
	private String dob = "", age = "", hospitalNumber, email, mobile, userName, kinName,  
				   kinMobile, road, county, postCode, gestation, parity, estimtedDelivery,
				   perineum, birthMode, babyGender, babyWeightKg = "",
				   vitK, hearing, antiD, feeding, nbst, deliveryDateTime, daysSinceBirth,
				   userCall, userSMS, userEmail, lastPeriodDate;
	private int babyWeightGrams = 0;
	private List<Integer> babyID;
	private double grams = 0.0;
	private String sex_male = "ale";
	private String sex_female = "emale";
	private Dialog dialog;
	private Button bookAppointmentButton;
  	private TableRow tableParity;
	private Date dobAsDate = null;
	private Intent userCallIntent, userSmsIntent, userEmailIntent;
	private Calendar cal = Calendar.getInstance();
	private int b;		//position of most recent baby in list
	private int p;		//position of most recent pregnancy in list
	private int userId;

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
        
        llUserContact = (LinearLayout)findViewById(R.id.ll_usr_contact);
        llUserContact.setOnClickListener(new ButtonClick());
        llUserAddress = (LinearLayout)findViewById(R.id.ll_usr_address);
        llUserAddress.setOnClickListener(new ButtonClick());
        llUserKinContact = (LinearLayout)findViewById(R.id.ll_usr_kin);
        llUserKinContact.setOnClickListener(new ButtonClick());
        
        tvAnteAge = (TextView)findViewById(R.id.tv_ante_age);
		tvAnteGestation = (TextView)findViewById(R.id.tv_ante_gestation);
		tvAnteParity = (TextView)findViewById(R.id.tv_ante_parity);
		tvAnteDeliveryTime = (TextView)findViewById(R.id.tv_ante_edd);
		tvAnteBloodGroup = (TextView)findViewById(R.id.tv_ante_blood);
		tvAnteRhesus = (TextView)findViewById(R.id.tv_ante_rhesus);
		tvAnteLastPeriod = (TextView)findViewById(R.id.tv_ante_last_period);
		
		tvUsrHospitalNumber = (TextView) findViewById(R.id.tv_usr_hospital_number);
		tvUsrAge = (TextView) findViewById(R.id.tv_usr_age);
		tvUsrEmail = (TextView) findViewById(R.id.tv_usr_email);
		tvUsrMobileNumber = (TextView) findViewById(R.id.tv_usr_mobile);
		tvUsrRoad = (TextView) findViewById(R.id.tv_usr_road);
		tvUsrCounty = (TextView) findViewById(R.id.tv_usr_county);
		tvUsrPostCode = (TextView) findViewById(R.id.tv_usr_post_code);
		tvUsrNextOfKinName = (TextView) findViewById(R.id.tv_usr_kin_name);
		tvUsrKinContact = (TextView) findViewById(R.id.tv_usr_kin_contact);
		tvUsrGestation = (TextView) findViewById(R.id.tv_usr_gestation);
		tvUsrParity = (TextView)findViewById(R.id.tv_usr_partiy);
		
		tvPostBirthMode = (TextView)findViewById(R.id.tv_post_mode);
		tvPostPerineum = (TextView)findViewById(R.id.tv_post_perineum);
		tvPostAntiD = (TextView)findViewById(R.id.tv_post_anti_d);
		tvPostDeliveryDate = (TextView)findViewById(R.id.tv_post_delivery_date);
		tvPostDeliveryTIme = (TextView)findViewById(R.id.tv_post_delivery_time);
		tvPostDaysSinceBirth = (TextView)findViewById(R.id.tv_post_days_since_birth);
		tvPostBabyGender = (TextView)findViewById(R.id.tv_post_gender);
		tvPostBirthWeight = (TextView)findViewById(R.id.tv_post_weight);
		tvPostVitK = (TextView)findViewById(R.id.tv_post_vit_k);
		tvPostHearing = (TextView)findViewById(R.id.tv_post_hearing);
		tvPostFeeding = (TextView)findViewById(R.id.tv_post_feeding);
		tvPostNBST = (TextView)findViewById(R.id.tv_post_nbst);
		
		bookAppointmentButton = (Button) findViewById(R.id.btn_usr_book_appointment);
		bookAppointmentButton.setOnClickListener(new ButtonClick());
		
		tableParity = (TableRow)findViewById(R.id.tr_ante_parity);
		tableParity.setOnClickListener(new ButtonClick());

		try{
			dob = ApiRootModel.getInstance().getServiceUsers().get(0).getPersonalFields().getDob();
			if(dob != null)
				age = getAge(dob);					
			getRecentPregnancy();
			getRecentBaby();

			userId = ApiRootModel.getInstance().getServiceUsers().get(0).getId();
			hospitalNumber = ApiRootModel.getInstance().getServiceUsers().get(0).getHospitalNumber();
			email = ApiRootModel.getInstance().getServiceUsers().get(0).getPersonalFields().getEmail();
			mobile = ApiRootModel.getInstance().getServiceUsers().get(0).getPersonalFields().getMobilePhone();
			userName = ApiRootModel.getInstance().getServiceUsers().get(0).getPersonalFields().getName();
			kinName = ApiRootModel.getInstance().getServiceUsers().get(0).getPersonalFields().getNextOfKinName();
			kinMobile = ApiRootModel.getInstance().getServiceUsers().get(0).getPersonalFields().getNextOfKinPhone();
			road = ApiRootModel.getInstance().getServiceUsers().get(0).getPersonalFields().getHomeAddress();
			county = ApiRootModel.getInstance().getServiceUsers().get(0).getPersonalFields().getHomeCounty();
			postCode = ApiRootModel.getInstance().getServiceUsers().get(0).getPersonalFields().getHomePostCode();
			perineum = ApiRootModel.getInstance().getPregnancies().get(p).getPerineum();
			List<String> birthModeList = ApiRootModel.getInstance().getPregnancies().get(p).getBirthMode();
			if(birthModeList !=  null)
				birthMode = putArrayToString(ApiRootModel.getInstance().getPregnancies().get(p).getBirthMode());
			else
				birthMode = "";
			babyGender = ApiRootModel.getInstance().getBabies().get(b).getGender();
			if(ApiRootModel.getInstance().getBabies().get(b).getWeight() != null)
				babyWeightKg = getGramsToKg(ApiRootModel.getInstance().getBabies().get(b).getWeight());
			else
				babyWeightKg = "";
			babyID = ApiRootModel.getInstance().getServiceUsers().get(0).getBabyIds();
			gestation = ApiRootModel.getInstance().getPregnancies().get(p).getGestation();
			parity = ApiRootModel.getInstance().getServiceUsers().get(0).getClinicalFields().getParity();
			estimtedDelivery = ApiRootModel.getInstance().getPregnancies().get(p).getEstimatedDeliveryDate();
			lastPeriodDate = ApiRootModel.getInstance().getPregnancies().get(p).getLastMenstrualPeriod();
			vitK = ApiRootModel.getInstance().getBabies().get(b).getVitaminK();
			hearing = ApiRootModel.getInstance().getBabies().get(b).getHearing();
			antiD = ApiRootModel.getInstance().getPregnancies().get(p).getAntiD();
			feeding = ApiRootModel.getInstance().getPregnancies().get(p).getFeeding();
			if(feeding == null)
				feeding = "";
			nbst = ApiRootModel.getInstance().getBabies().get(b).getNewbornScreeningTest();
			deliveryDateTime = ApiRootModel.getInstance().getBabies().get(b).getDeliveryDateTime();
			if(deliveryDateTime != null)
				daysSinceBirth = getNoOfDays(deliveryDateTime);
			setActionBarTitle(userName);
			
			if(parity.equals("0 + 0"))
				tableParity.setEnabled(false);
			tvAnteParity.setText(parity);
			tvAnteGestation.setText(gestation);
			tvAnteRhesus.setText(ApiRootModel.getInstance().getServiceUsers().get(0).getClinicalFields().getRhesus().toString());
			tvAnteBloodGroup.setText(ApiRootModel.getInstance().getServiceUsers().get(0).getClinicalFields().getBloodGroup());
			if(estimtedDelivery != null)
				tvAnteDeliveryTime.setText(getEstimateDeliveryDate(estimtedDelivery));
			else
			tvAnteDeliveryTime.setText("");
			tvAnteAge.setText(age);
			
			tvUsrAge.setText(age);
			tvUsrHospitalNumber.setText(hospitalNumber);
			tvUsrEmail.setText(email);
			tvUsrMobileNumber.setText(mobile);
			tvUsrRoad.setText(road);
			tvUsrCounty.setText(county);
			tvUsrPostCode.setText(postCode);
			tvUsrNextOfKinName.setText(kinName);
			tvUsrKinContact.setText(kinMobile);
			tvUsrGestation.setText(gestation);
			tvUsrParity.setText(parity);
			
			tvPostVitK.setText(vitK);
			tvPostHearing.setText(hearing);
			tvPostAntiD.setText(antiD);
			tvPostFeeding.setText(feeding);
			tvPostNBST.setText(nbst);
			tvPostDeliveryDate.setText(getDeliveryDate(deliveryDateTime));
			tvPostDeliveryTIme.setText(getDeliveryTime(deliveryDateTime));
			tvPostPerineum.setText(perineum);
			tvPostBirthMode.setText(birthMode);
			tvPostBirthWeight.setText(babyWeightKg);
			if(lastPeriodDate != null)
				tvAnteLastPeriod.setText(getLastPeriodDate(lastPeriodDate));
			if(babyGender.equalsIgnoreCase("M"))
				tvPostBabyGender.setText(babyGender + sex_male);
			else if (babyGender.equalsIgnoreCase("F"))
				tvPostBabyGender.setText(babyGender + sex_female);
			tvPostDaysSinceBirth.setText(daysSinceBirth);
		} catch (NullPointerException e){
			e.printStackTrace();
		}
	}

	private class ButtonClick implements View.OnClickListener, DialogInterface {
		public void onClick(View v) {
			switch (v.getId()) {
			case R.id.btn_usr_book_appointment:
				setSharedPrefs();				
				Intent intentBook = new Intent(ServiceUserActivity.this, AppointmentTypeSpinnerActivity.class);
				startActivity(intentBook);
				break;
			case R.id.ll_usr_contact:
				dialogContact(userContactList);
				break;
			case R.id.ll_usr_kin:
				dialogContact(kinContactList);
				break;					
			case R.id.ll_usr_address:
				gotoMaps();
				break;
			case R.id.tr_ante_parity:
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
	            if (items[item].equals("Call Mobile"))
					makeCall();
	            if (items[item].equals("Send SMS"))
					sendSms();
	            if (items[item].equals("Send Email"))
					sendEmail();
	        }
	    });
	    AlertDialog alert = builder.create();
	    alert.show();
	    alert.setCanceledOnTouchOutside(true);
	}
	
	private void makeCall(){
		userCall = "tel:" + tvUsrMobileNumber.getText().toString();
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
		userSMS = tvUsrMobileNumber.getText().toString();
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
		userEmail = tvUsrEmail.getText().toString();
		userEmailIntent = new Intent(Intent.ACTION_SEND);
		userEmailIntent.setClassName("com.google.android.gm","com.google.android.gm.ComposeActivityGmail");
		userEmailIntent.setType("message/rfc822");
		userEmailIntent.putExtra(android.content.Intent.EXTRA_EMAIL, new String[] {userEmail});
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
		    	String addr = tvUsrRoad.getText().toString() + ", "
                        + tvUsrCounty.getText().toString() + ", "
                        + tvUsrPostCode.getText().toString();
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
			dobAsDate = dfDateOnly.parse(dob);
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
			date = dfDateOnly.parse(edd);
	        ed = dfMonthFullName.format(date);
		} catch (ParseException e) {
			e.printStackTrace();
		}
	    return ed;
	}	

	public String getLastPeriodDate(String edd){
        Date date;
        String ed = null;
		try{
			date = dfDateOnly.parse(edd);
	        ed = dfMonthFullName.format(date);
		} catch (ParseException e) {
			e.printStackTrace();
		}
	    return ed;
	}	

	protected String getDeliveryDate(String edd){
       Date date;
       String dateOfDelivery = null;
		try{
			date = dfDateOnly.parse(edd);
	        dateOfDelivery = dfMonthFullName.format(date);
		} catch (ParseException e) {
			e.printStackTrace();
		}
	    return dateOfDelivery;
	}	
	
	private String getDeliveryTime(String edd) {
        String deliveryTime = null;
        Date date;
        try {
            date = dfDateOnly.parse(edd);

            deliveryTime = dfAMPM.format(date);
            date = dfDateTimeWZone.parse(edd);

        } catch (ParseException e) {
            e.printStackTrace();
        }
        return deliveryTime;
    }

	private String getNoOfDays(String dateOfDelivery){
		int numOfDays = 0;
		try {
			Date dodAsDate = dfDateTimeWZone.parse(deliveryDateTime);
			cal = Calendar.getInstance();
			Date now = cal.getTime();
			numOfDays = (int)((now.getTime() - dodAsDate.getTime()) / (1000 * 60 * 60 * 24)) + 1;
		} catch (ParseException e) {
			e.printStackTrace();
		}		
        
        return String.valueOf(numOfDays);  
	}
    
    private String getGramsToKg(int grams){
	    	double kg = 0.0;
	    	kg = grams/1000.0;
	    	return String.valueOf(kg);
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
		prefs.putString("name", userName);
		prefs.putString("id", String.valueOf(userId));
		prefs.putBoolean("reuse", true);
		prefs.commit();
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