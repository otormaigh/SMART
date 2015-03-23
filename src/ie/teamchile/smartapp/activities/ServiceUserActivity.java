package ie.teamchile.smartapp.activities;

import ie.teamchile.smartapp.R;
import ie.teamchile.smartapp.utility.ServiceUserSingleton;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

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
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class ServiceUserActivity extends MenuInheritActivity {
	private TextView hospitalNumber,name, ageServiceUser, email, mobileNumber, road,
			county, postCode, nextOfKinName, nextOfKinContactNumber, gestation, parity;
	private String dob, userCall, userSMS, userEmail, kinCall, kinSMS;
	private Dialog dialog;
	private Button bookAppointmentButton, userContact, next_of_kin_contact,
			userPhoneCall, userSendSMS, userSendEmail, userCancel, userAddress,
			kinPhoneCall, kinSendSMS, kinCancel;
	
	private ImageView anteNatal, postNatal, userImage;
	private Date dobAsDate;
	private Intent userCallIntent, userSmsIntent, userEmailIntent,
			kinCallIntent, kinSmsIntent;
	private Calendar cal = Calendar.getInstance();
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_service_user);

		//dob = getIntent().getStringExtra("dob");

		hospitalNumber = (TextView) findViewById(R.id.hospital_number);
		name = (TextView) findViewById(R.id.name);
		ageServiceUser=(TextView) findViewById(R.id.age);
		email = (TextView) findViewById(R.id.email);
		mobileNumber = (TextView) findViewById(R.id.mobile_number);
		road = (TextView) findViewById(R.id.road);
		county = (TextView) findViewById(R.id.county);
		postCode = (TextView) findViewById(R.id.post_code);
		nextOfKinName = (TextView) findViewById(R.id.next_of_kin_name);
		nextOfKinContactNumber = (TextView) findViewById(R.id.next_of_kin_contact_number);
		gestation = (TextView) findViewById(R.id.g);
		parity = (TextView)findViewById(R.id.p);

		String dob = ServiceUserSingleton.getInstance().getUserDOB().get(0);
		int anteNatalAge = getAge(dob);
		String theAge = String.valueOf(anteNatalAge);		

		ageServiceUser.setText(theAge);
		String hospitalNumberStr = ServiceUserSingleton.getInstance().getUserHospitalNumber().get(0);
		String emailStr = ServiceUserSingleton.getInstance().getUserEmail().get(0);
		String mobileStr = ServiceUserSingleton.getInstance().getUserMobilePhone().get(0);
		String nameStr = ServiceUserSingleton.getInstance().getUserName().get(0);
		String kinName = ServiceUserSingleton.getInstance().getUserNextOfKinName().get(0);
		String kinMobile = ServiceUserSingleton.getInstance().getUserNextOfKinPhone().get(0);
		String roadStr = ServiceUserSingleton.getInstance().getUserHomeAddress().get(0);
		String countyStr = ServiceUserSingleton.getInstance().getUserHomeCounty().get(0);
		String postCodeStr = ServiceUserSingleton.getInstance().getUserHomePostCode().get(0);
		String gestationStr = ServiceUserSingleton.getInstance().getPregnancyGestation().get(0);
		String parityStr = ServiceUserSingleton.getInstance().getUserParity().get(0);

		name.setText(nameStr);
		
		hospitalNumber.setText(hospitalNumberStr);
		email.setText(emailStr);
		mobileNumber.setText(mobileStr);
		road.setText(roadStr);
		county.setText(countyStr);
		postCode.setText(postCodeStr);
		nextOfKinName.setText(kinName);
		nextOfKinContactNumber.setText(kinMobile);
		gestation.setText(gestationStr);
		parity.setText(parityStr);
		
		bookAppointmentButton = (Button) findViewById(R.id.book_appointment);
		bookAppointmentButton.setOnClickListener(new ButtonClick());
		next_of_kin_contact = (Button) findViewById(R.id.next_of_kin_contact);
		next_of_kin_contact.setOnClickListener(new ButtonClick());
		userContact = (Button) findViewById(R.id.user_contact);
		userContact.setOnClickListener(new ButtonClick());
		userAddress = (Button) findViewById(R.id.user_address);
		userAddress.setOnClickListener(new ButtonClick());
		
		anteNatal = (ImageView)findViewById(R.id.ante_natal);
		anteNatal.setOnClickListener(new ButtonClick());
		
		postNatal = (ImageView)findViewById(R.id.post_natal);
		postNatal.setOnClickListener(new ButtonClick());
		
		userImage = (ImageView)findViewById(R.id.user_image);
		userImage.setOnClickListener(new ButtonClick());	
	}
	
	@Override
	protected void onNewIntent(Intent intent) {
	    super.onNewIntent(intent);
	    setIntent(intent);
	}
	
	private class ButtonClick implements View.OnClickListener, DialogInterface {
		public void onClick(View v) {
			switch (v.getId()) {
			case R.id.ante_natal:
				Intent intent = new Intent(ServiceUserActivity.this, AnteNatalActivity.class);
				startActivity(intent);
				break;
			case R.id.post_natal:
				Intent intent1 = new Intent(ServiceUserActivity.this, PostNatalActivity.class);
				startActivity(intent1);
				break;
			case R.id.user_image:
				Intent intent2 = new Intent(getApplicationContext(), ServiceUserActivity.class);
				startActivity(intent2);
				break;
			case R.id.book_appointment:
				SharedPreferences.Editor prefs = getSharedPreferences("SMART", MODE_PRIVATE).edit();
				prefs.putString("name", ServiceUserSingleton.getInstance().getUserName().get(0));
				prefs.putString("id", ServiceUserSingleton.getInstance().getUserID().get(0));
				prefs.putBoolean("reuse", true);
				prefs.commit();
				
				Intent intent3 = new Intent(ServiceUserActivity.this, AppointmentTypeSpinnerActivity.class);
				startActivity(intent3);
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
				userCall = "tel:" + mobileNumber.getText().toString();
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
				userSMS = "" + mobileNumber.getText().toString();
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
				userEmail = "" + email.getText().toString();
				
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
				kinCall = "tel:" + nextOfKinContactNumber.getText().toString();
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
				kinSMS = "" + nextOfKinContactNumber.getText().toString();
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
		    	String addr = "" + road.getText().toString() + county.getText().toString() + postCode.getText().toString();
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
	public int getAge(String dob) {
		try {
			SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
			dobAsDate = df.parse(dob);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		// Calendar cal = Calendar.getInstance();
		cal.setTime(dobAsDate);
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
		return result;
	}
	
	public String getEstimateDeliveryDate(String edd){
		 // *** note that it's "yyyy-MM-dd hh:mm:ss" not "yyyy-mm-dd hh:mm:ss"  
        SimpleDateFormat dt = new SimpleDateFormat("yyyy-mm-dd", Locale.getDefault());
        Date date;
        String ed = null;
		try{
			date = dt.parse(edd);
			// *** same for the format String below
	        SimpleDateFormat dt1 = new SimpleDateFormat("dd MMMM yyyy", Locale.getDefault());
	        ed = dt1.format(date);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	    return ed;
	}
	
	public String getDeliveryDate(String edd){
       SimpleDateFormat dt = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZ", Locale.getDefault());
       Date date;
       String dateOfDevelivery = null;
		try{
			date = dt.parse(edd);
			// *** same for the format String below
	        SimpleDateFormat dt1 = new SimpleDateFormat("dd MMMM yyyy", Locale.getDefault());
	        dateOfDevelivery = dt1.format(date);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	    return dateOfDevelivery;
	}	
	
	public String getDeliveryTime(String edd) {
		String deliveryTime = null;
		Date date;
        SimpleDateFormat dti = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZ", Locale.getDefault());
        SimpleDateFormat fd = new SimpleDateFormat("HH:mm a", Locale.getDefault());
  	  try {
  		  date = dti.parse(edd);
  		 
  		  deliveryTime = fd.format(date);
		  date = dti.parse(edd);
		
	} catch (ParseException e) {
		e.printStackTrace();
	}
       return deliveryTime;
	}
	

    public int getNoOfDays(Date now, Date past){

        now = cal.getTime();
        return (int)((now.getTime() - past.getTime()) / (1000 * 60 * 60 * 24)); 
	}
    
    public double getGramsToKg(double grams){
	    	double kg = 0.0;
	    	kg = grams/1000;
	    	return kg;
    }
}