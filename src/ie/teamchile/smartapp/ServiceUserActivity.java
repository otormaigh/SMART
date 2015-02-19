package ie.teamchile.smartapp;


import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class ServiceUserActivity extends MenuInheritActivity {
	private TextView hospitalNumber, name, age, email, mobileNumber, road,  county,
            postCode, nextOfKinName, nextOfKinContactNumber;
    private String dob;
    private Button bookAppointmentButton, userContact, next_of_kin_contact;
    private Date dobAsDate;
    private Calendar cal = Calendar.getInstance();
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_service_user);

        dob = getIntent().getStringExtra("dob");
		
		hospitalNumber = (TextView) findViewById(R.id.hospital_number);
        name = (TextView) findViewById(R.id.name);
        age = (TextView) findViewById(R.id.age);
        email = (TextView) findViewById(R.id.email);
		mobileNumber = (TextView) findViewById(R.id.mobile_number);
		road = (TextView) findViewById(R.id.road);
		county = (TextView) findViewById(R.id.county);
		postCode = (TextView) findViewById(R.id.post_code);
		nextOfKinName = (TextView) findViewById(R.id.next_of_kin_name);
		nextOfKinContactNumber = (TextView) findViewById(R.id.next_of_kin_contact_number);
        bookAppointmentButton = (Button) findViewById(R.id.book_appointment);
        bookAppointmentButton.setOnClickListener(new ButtonClick());
        next_of_kin_contact = (Button) findViewById(R.id.next_of_kin_contact);
		next_of_kin_contact.setOnClickListener(new ButtonClick());
		userContact = (Button) findViewById(R.id.user_contact);
		userContact.setOnClickListener(new ButtonClick());
        

        hospitalNumber.setText(getIntent().getStringExtra("hospital_number"));
        name.setText(getIntent().getStringExtra("name"));
        age.setText(Integer.toString(getAge(dob)));
        email.setText(getIntent().getStringExtra("email"));
		mobileNumber.setText(getIntent().getStringExtra("mobile_number"));
		road.setText(getIntent().getStringExtra("road"));
		county.setText(getIntent().getStringExtra("county"));
		postCode.setText(getIntent().getStringExtra("post_code"));
		nextOfKinName.setText(getIntent().getStringExtra("next_of_kin_name"));
		nextOfKinContactNumber.setText(getIntent().getStringExtra("next_of_kin_phone"));
	
		
	}
	
	

	private class ButtonClick implements View.OnClickListener, Dialog.OnClickListener {
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.book_appointment:
                    Intent intent = new Intent(ServiceUserActivity.this, AppointmentTypeSpinnerActivity.class);
                    startActivity(intent);
                    break;
                case R.id.next_of_kin_contact:
                	kinContact();
                	break;
                case R.id.user_contact:
                	usrContact();
                	break;
            }
        }
		public void onClick(DialogInterface dialog, int which) {
			switch(which){	
			case R.id.makeCall2:
				Log.i("Make call", "");
				   String uri = "tel:"+mobileNumber.getText().toString();
				   Intent phoneIntent = new Intent(Intent.ACTION_DIAL, Uri.parse(uri));
				   try {				
				 		 startActivity(phoneIntent);			
				 	   } catch (android.content.ActivityNotFoundException ex) {
				 		 Toast.makeText(ServiceUserActivity.this, 
				 		"Call faild, please try again later.", Toast.LENGTH_SHORT).show();
				 	   }
				break;
			case R.id.sendSms2:
				Log.i("Send SMS", "");
				   String no = ""+mobileNumber.getText().toString();
				   Intent sendSMS = new Intent(Intent.ACTION_VIEW);
				   sendSMS.setType("vnd.android-dir/mms-sms");
				   sendSMS.putExtra("address", no);
				   try{
					    startActivity(sendSMS);
					  } catch (android.content.ActivityNotFoundException ex) {
						Toast.makeText(ServiceUserActivity.this, 
						"Call faild, please try again later.", Toast.LENGTH_SHORT).show();
					  }
				break;
			case R.id.sendEmail:
				Log.i("Send Email", "");
				String mail = ""+email.getText().toString();
				Intent sendEmail = new Intent(Intent.ACTION_SEND);
				try{
					startActivity(sendEmail);
				   }catch (android.content.ActivityNotFoundException ex) {
					Toast.makeText(ServiceUserActivity.this, 
				    "Call faild, please try again later.", Toast.LENGTH_SHORT).show();
				   }
			break;
			case R.id.cancel2:
			{
				dialog.cancel();
			}
			break;
			case R.id.makeCall:
				Log.i("Make call", "");
	 		    String uri1 = "tel:"+nextOfKinContactNumber.getText().toString();
	 		    Intent phoneIntent1 = new Intent(Intent.ACTION_DIAL, Uri.parse(uri1));
	 			try {	
	 				  startActivity(phoneIntent1);
	 				} catch (android.content.ActivityNotFoundException ex) {
	 				  Toast.makeText(ServiceUserActivity.this, 
	 			      "Call faild, please try again later.", Toast.LENGTH_SHORT).show();
	 				}
	     	break;
			case R.id.sendSms:
					Log.i("Send SMS", "");
					  String no1 = ""+nextOfKinContactNumber.getText().toString();
					   Intent sendSMS1 = new Intent(Intent.ACTION_VIEW);
					   sendSMS1.setType("vnd.android-dir/mms-sms");
					   sendSMS1.putExtra("address", no1);
					   try{
						    startActivity(sendSMS1);
						  } catch (android.content.ActivityNotFoundException ex) {
							Toast.makeText(ServiceUserActivity.this, 
							"Call faild, please try again later.", Toast.LENGTH_SHORT).show();
						  }
				
					break;
				case R.id.cancel:{
					dialog.cancel();
				}
					break;
			}
				
			}
			
	


		private void usrContact() {

		 // TODO Auto-generated method stub
		   final Dialog dialog = new Dialog(ServiceUserActivity.this);
		   dialog.setContentView(R.layout.user_contact_dialog_box);
		   dialog.setTitle(R.string.contact_dialog_message);
		   Button phoneCall = (Button) dialog.findViewById(R.id.makeCall2);
		   phoneCall.setOnClickListener(new ButtonClick());
		     	
		   Button sendSms1 = (Button) dialog.findViewById(R.id.sendSms2);
		   sendSms1.setOnClickListener(new ButtonClick());

		   
		 Button sendEmail = (Button) dialog.findViewById(R.id.sendEmail);
		 sendEmail.setOnClickListener(new ButtonClick());
			   
		
		Button cancel = (Button) dialog.findViewById(R.id.cancel2);
		cancel.setOnClickListener(new ButtonClick());
	   
		}
		
		private void kinContact() {
			// TODO Auto-generated method stub
	    	final Dialog dialog = new Dialog(ServiceUserActivity.this);
	    	dialog.setContentView(R.layout.kin_contact_dialog_box);
	    	dialog.setTitle(R.string.contact_dialog_message);
	    	
	    	Button phoneCall = (Button) dialog.findViewById(R.id.makeCall);
	    	phoneCall.setOnClickListener(new ButtonClick());
	    		
    		
	    	
    	
    	Button sendSms = (Button) dialog.findViewById(R.id.sendSms);
    	sendSms.setOnClickListener(new ButtonClick());

	
    	
	 	Button cancel = (Button) dialog.findViewById(R.id.cancel);
    	cancel.setOnClickListener(new ButtonClick());

		
	}
	}
		
    public int getAge(String dob) {
        try {
            DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
            dobAsDate =  df.parse(dob);

        } catch (ParseException e) {
            e.printStackTrace();
        }
       // Calendar cal = Calendar.getInstance();
        cal.setTime(dobAsDate);
        int year = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH);
        int day = cal.get(Calendar.DAY_OF_MONTH);

        Date now = new Date();
        int nowMonth = now.getMonth()+1;
        int nowYear = now.getYear()+1900;
        int result = nowYear - year;

        if (month > nowMonth) {
            result--;
        }
        else if (month == nowMonth) {
            int nowDay = now.getDate();

            if (day > nowDay) {
                result--;
            }
        }
        return result;
    }
}


