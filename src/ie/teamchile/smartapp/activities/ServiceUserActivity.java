package ie.teamchile.smartapp.activities;

import ie.teamchile.smartapp.R;
import ie.teamchile.smartapp.utility.ServiceUserSingleton;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import android.app.ActionBar;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TabHost;
import android.widget.TableRow;
import android.widget.TabHost.TabSpec;
import android.widget.TextView;
import android.widget.Toast;

public class ServiceUserActivity extends MenuInheritActivity {
	private TextView anteAge, anteGestation, anteParity, anteDeliveryTime, anteBloodGroup, anteRhesus;
	private TextView contactHospitalNumber, contactEmail, contactMobileNumber, contactRoad,
					 contactCounty, contactPostCode, contactNextOfKinName, contactAge, 
					 contactNextOfKinContactNumber, contactGestation, contactParity;
	private TextView postBirthMode, postPerineum, postAntiD, postDeliveryDate, postDeliveryTime,
					 postDaysSinceBirth, postBabyGender, postBirthWeight, postVitK, postHearing, 
					 postFeeding, postNBST, lastPeriod;
	
	private String dob = "", age = "", hospitalNumber, email, mobile, userName, kinName,  
				   kinMobile, road, county, postCode, gestation, parity, estimtedDelivery,
				   perineum, birthMode, babyGender, babyWeightGrams = "", babyWeightKg = "", 
				   vitK, hearing, antiD, feeding, nbst, deliveryDateTime, daysSinceBirth,
				   userCall, userSMS, userEmail, kinCall, kinSMS, lastPeriodDate;
	private int days = 0;
	private double grams = 0.0;
	private String sex_male = "ale";
	private String sex_female = "emale";
	private Dialog dialog;
	private Button bookAppointmentButton, userContact, next_of_kin_contact,
				   userPhoneCall, userSendSMS, userSendEmail, userCancel, userAddress,
				   kinPhoneCall, kinSendSMS, kinCancel;
	private TableRow obstetricHistory, tableParity;
	private DateFormat sdfDateTime = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZ", Locale.getDefault());
	private DateFormat sdfDate = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
	private DateFormat sdfMonthFullName = new SimpleDateFormat("dd MMMM yyyy", Locale.getDefault());
	private DateFormat sdfAMPM = new SimpleDateFormat("HH:mm a", Locale.getDefault());
	private Date dateOfDelivery = null, currentDate = null, dobAsDate = null;
	private Intent userCallIntent, userSmsIntent, userEmailIntent,
			kinCallIntent, kinSmsIntent;
	private String[] tabs = { "Ante Natal", "Contact", "Post Natal" };
	private Calendar cal = Calendar.getInstance();
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_user_search_tabhost);
		
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
		
		userContact = (Button) findViewById(R.id.user_contact);
		userContact.setOnClickListener(new ButtonClick());
		userAddress = (Button) findViewById(R.id.user_address);
		userAddress.setOnClickListener(new ButtonClick());	
		next_of_kin_contact = (Button) findViewById(R.id.next_of_kin_contact);
		next_of_kin_contact.setOnClickListener(new ButtonClick());
		bookAppointmentButton = (Button) findViewById(R.id.book_appointment);
		bookAppointmentButton.setOnClickListener(new ButtonClick());
		
		tableParity = (TableRow)findViewById(R.id.button_parity);
		tableParity.setOnClickListener(new ButtonClick());

		obstetricHistory = (TableRow) findViewById(R.id.obstretic_history);
		obstetricHistory.setOnClickListener(new ButtonClick());
		
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
			perineum = ServiceUserSingleton.getInstance().getPregnancyPerineum().get(0);
			birthMode = formatArrayString(ServiceUserSingleton.getInstance().getPregnancyBirthMode().get(0));
			babyGender = ServiceUserSingleton.getInstance().getBabyGender().get(0);
			babyWeightGrams = ServiceUserSingleton.getInstance().getBabyWeight().get(0);
			if(!babyWeightGrams.equals("null")){
				grams =  Double.parseDouble(babyWeightGrams);
				babyWeightKg = String.valueOf(getGramsToKg(grams));
			}
			gestation = ServiceUserSingleton.getInstance().getPregnancyGestation().get(0);
			parity = ServiceUserSingleton.getInstance().getUserParity().get(0);
			estimtedDelivery = ServiceUserSingleton.getInstance().getPregnancyEstimatedDeliveryDate().get(0);
			vitK = ServiceUserSingleton.getInstance().getBabyVitK().get(0);
			hearing = ServiceUserSingleton.getInstance().getBabyHearing().get(0);
			antiD = ServiceUserSingleton.getInstance().getPregnancyAntiD().get(0);
			feeding = ServiceUserSingleton.getInstance().getPregnancyFeeding().get(0);
			nbst = ServiceUserSingleton.getInstance().getBabyNewBornScreeningTest().get(0);
			lastPeriodDate = ServiceUserSingleton.getInstance().getPregnancyLastMenstrualPeriod().get(0);
			deliveryDateTime = ServiceUserSingleton.getInstance().getBabyDeliveryDateTime().get(0);		
			if(!deliveryDateTime.equals("null")){
				dateOfDelivery = sdfDateTime.parse(deliveryDateTime);
				days = getNoOfDays(dateOfDelivery);
				daysSinceBirth = String.valueOf(days);
			}
			setTitle(userName);
			
			if(parity.equals("0 + 0")){
				tableParity.setEnabled(false);
			}
			
			anteParity.setText(parity);
			anteGestation.setText(ServiceUserSingleton.getInstance().getPregnancyGestation().get(0));
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
			lastPeriod.setText(getLastPeriodDate(lastPeriodDate));
				
			if(babyGender.equalsIgnoreCase("M")){
				postBabyGender.setText(babyGender + sex_male);
			}
			else if (babyGender.equalsIgnoreCase("F")){
				postBabyGender.setText(babyGender + sex_female);
			}		
			postDaysSinceBirth.setText(daysSinceBirth);
		} catch (ParseException | NullPointerException e){
			e.printStackTrace();
		}
	}
	
	@Override
	protected void onNewIntent(Intent intent) {
	    super.onNewIntent(intent);
	    setIntent(intent);
	}
	
	private class ButtonClick implements View.OnClickListener, DialogInterface {
		public void onClick(View v) {
			switch (v.getId()) {
			case R.id.book_appointment:
				SharedPreferences.Editor prefs = getSharedPreferences("SMART", MODE_PRIVATE).edit();
				prefs.putString("name", ServiceUserSingleton.getInstance().getUserName().get(0));
				prefs.putString("id", ServiceUserSingleton.getInstance().getUserID().get(0));
				prefs.putBoolean("reuse", true);
				prefs.commit();
				
				Intent intentBook = new Intent(ServiceUserActivity.this, AppointmentTypeSpinnerActivity.class);
				startActivity(intentBook);
				break;
			case R.id.user_contact:
				usrContact();
				break;
			case R.id.next_of_kin_contact:
				kinContact();
				break;
			case R.id.user_address:
				userAddress();
				break;
			case R.id.user_Phone_Call:
				Log.i("Make call", "");
				userCall = "tel:" + contactMobileNumber.getText().toString();
				userCallIntent = new Intent(Intent.ACTION_DIAL,
						Uri.parse(userCall));
				try {
					startActivity(userCallIntent);
				} catch (android.content.ActivityNotFoundException ex) {
					Toast.makeText(ServiceUserActivity.this,
							"Call failed, please try again later.",
							Toast.LENGTH_SHORT).show();
				}finally{
					dialog.dismiss();
				}
				break;
			case R.id.user_Send_SMS:
				Log.i("Send SMS", "");
				userSMS = "" + contactMobileNumber.getText().toString();
				userSmsIntent = new Intent(Intent.ACTION_VIEW);
				userSmsIntent.setType("vnd.android-dir/mms-sms");
				userSmsIntent.putExtra("address", userSMS);
				try {
					startActivity(userSmsIntent);
				} catch (android.content.ActivityNotFoundException ex) {
					Toast.makeText(ServiceUserActivity.this,
							"SMS failed, please try again later.",
							Toast.LENGTH_SHORT).show();
				}finally{
					dialog.dismiss();
				}
				break;
			case R.id.user_Send_Email:
				Log.i("Send Email", "");
				userEmail = "" + contactEmail.getText().toString();
				
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
							Toast.LENGTH_SHORT).show();
				}finally{
					dialog.dismiss();
				}
				break;
			case R.id.kin_Phone_Call:
				Log.i("Make call", "");
				kinCall = "tel:" + contactNextOfKinContactNumber.getText().toString();
				kinCallIntent = new Intent(Intent.ACTION_DIAL,
						Uri.parse(kinCall));
				try {
					startActivity(kinCallIntent);
				} catch (android.content.ActivityNotFoundException ex) {
					Toast.makeText(ServiceUserActivity.this,
							"Call failed, please try again later.",
							Toast.LENGTH_SHORT).show();
				}finally{
					dialog.dismiss();
				}
				break;
			case R.id.kin_Send_SMS:
				Log.i("Send SMS", "");
				kinSMS = "" + contactNextOfKinContactNumber.getText().toString();
				kinSmsIntent = new Intent(Intent.ACTION_VIEW);
				kinSmsIntent.setType("vnd.android-dir/mms-sms");
				kinSmsIntent.putExtra("address", kinSMS);
				try {
					startActivity(kinSmsIntent);
				} catch (android.content.ActivityNotFoundException ex) {
					Toast.makeText(ServiceUserActivity.this,
							"SMS failed, please try again later.",
							Toast.LENGTH_SHORT).show();
				}finally{
					dialog.dismiss();
				}
				break;
			case R.id.user_Cancel:
				dialog.cancel();
				break;
			case R.id.kin_Cancel:
				dialog.cancel();
				break;
			/*case R.id.obstretic_history:
				Intent intent5 = new Intent(ServiceUserActivity.this, ObstreticHistoryActivity.class);
				startActivity(intent5);
				break;*/
			case R.id.button_parity:
				Intent intent6 = new Intent(ServiceUserActivity.this, ParityDetailsActivity.class);
				startActivity(intent6);
			}
		}
		@Override
		public void cancel() {
		}
		@Override
		public void dismiss() {
		}
	}
	
	public void usrContact() {
		dialog = new Dialog(ServiceUserActivity.this);
		dialog.setContentView(R.layout.user_contact_dialog_box);
		dialog.setTitle(R.string.contact_dialog_message);
		userPhoneCall = (Button) dialog.findViewById(R.id.user_Phone_Call);
		userPhoneCall.setOnClickListener(new ButtonClick());
		userSendSMS = (Button) dialog.findViewById(R.id.user_Send_SMS);
		userSendSMS.setOnClickListener(new ButtonClick());
		userSendEmail = (Button) dialog.findViewById(R.id.user_Send_Email);
		userSendEmail.setOnClickListener(new ButtonClick());
		userCancel = (Button) dialog.findViewById(R.id.user_Cancel);
		userCancel.setOnClickListener(new ButtonClick());
		dialog.show();
	}
	
	private void userAddress() {
        new AlertDialog.Builder(this)
        .setTitle(R.string.user_address_title)
        .setMessage(R.string.user_address_message)
        .setNegativeButton(R.string.No, new DialogInterface.OnClickListener() {
		public void onClick(DialogInterface dialoginterface, int i) {			
		}})
		.setPositiveButton(R.string.Yes, new DialogInterface.OnClickListener() {
		    public void onClick(DialogInterface dialoginterface, int i) {
		    	String addr = "" + contactRoad.getText().toString() + contactCounty.getText().toString() + contactPostCode.getText().toString();
		    	Uri uri = Uri.parse(addr);
		    	System.out.println(addr);
		    	Intent intent = new Intent(Intent.ACTION_VIEW, uri);
		        intent.setClassName("com.google.android.apps.maps", "com.google.android.maps.MapsActivity");
		    	startActivity(intent);
        }	
		}).show();		
	}

	private void kinContact() {
		dialog = new Dialog(ServiceUserActivity.this);
		dialog.setContentView(R.layout.kin_contact_dialog_box);
		dialog.setTitle(R.string.contact_dialog_message);
		kinPhoneCall = (Button) dialog.findViewById(R.id.kin_Phone_Call);
		kinPhoneCall.setOnClickListener(new ButtonClick());
		kinSendSMS = (Button) dialog.findViewById(R.id.kin_Send_SMS);
		kinSendSMS.setOnClickListener(new ButtonClick());
		kinCancel = (Button) dialog.findViewById(R.id.kin_Cancel);
		kinCancel.setOnClickListener(new ButtonClick());
		dialog.show();
	}
	public String getAge(String dob) {
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
	
	public String getEstimateDeliveryDate(String edd){
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

	
	
	public String getDeliveryDate(String edd){
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
	
	public String getDeliveryTime(String edd) {
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

    public int getNoOfDays(Date past){
    	cal = Calendar.getInstance();
        Date now = cal.getTime();
        return (int)((now.getTime() - past.getTime()) / (1000 * 60 * 60 * 24)); 
	}
    
    public double getGramsToKg(double grams){
	    	double kg = 0.0;
	    	kg = grams/1000;
	    	return kg;
    }
    
    private String formatArrayString(String toBeFormatted){
    	String formatedString = toBeFormatted
    		    .replace(",", "")  //remove the commas
    		    .replace("[", "")  //remove the right bracket
    		    .replace("]", "")  //remove the left bracket
    		    .replace("\"", "")
    		    .trim(); 
    	return formatedString;
    }
}
