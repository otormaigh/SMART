package ie.teamchile.smartapp;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class ServiceUserActivity extends MenuInheritActivity {
	private TextView hospitalNumber, name, age, email, mobileNumber, road,
			county, postCode, nextOfKinName, nextOfKinContactNumber;
	private String dob, userCall, userSMS, userEmail, kinCall, kinSMS;
	private Dialog dialog;
	private Button bookAppointmentButton, userContact, next_of_kin_contact,
			userPhoneCall, userSendSMS, userSendEmail, userCancel,
			kinPhoneCall, kinSendSMS, kinCancel;
	private Date dobAsDate;
	private Intent userCallIntent, userSmsIntent, userEmailIntent,
			kinCallIntent, kinSmsIntent;
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

	private class ButtonClick implements View.OnClickListener, DialogInterface {
		public void onClick(View v) {
			switch (v.getId()) {
			case R.id.book_appointment:
				Intent intent = new Intent(ServiceUserActivity.this,
						AppointmentTypeSpinnerActivity.class);
				startActivity(intent);
				break;
			case R.id.user_contact:
				usrContact();
				break;
			case R.id.next_of_kin_contact:
				kinContact();
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
			DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
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
}