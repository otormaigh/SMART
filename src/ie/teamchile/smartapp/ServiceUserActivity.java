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
    private TextView hospitalNumber, name, age, email, mobileNumber, road, county,
            postCode, nextOfKinName, nextOfKinContactNumber;
    private String dob, uri;
    private Dialog dialog;
    private Button bookAppointmentButton, userContact, next_of_kin_contact, userPhoneCall, userSendSMS,
            userSendEmail, userCancel, kinPhoneCall, kinSendSMS, kinCancel;
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

    private class ButtonClick implements View.OnClickListener, DialogInterface {
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.book_appointment:
                    Intent intent = new Intent(ServiceUserActivity.this, AppointmentTypeSpinnerActivity.class);
                    startActivity(intent);
                    break;
                case R.id.user_contact:
                    usrContact();
                    break;
                case R.id.next_of_kin_contact:
                    kinContact();
                    break;
                case R.id.userPhoneCall:
                    Log.i("Make call", "");
                    uri = "tel:" + mobileNumber.getText().toString();
                    Intent phoneIntent = new Intent(Intent.ACTION_DIAL, Uri.parse(uri));
                    try {
                        startActivity(phoneIntent);
                    } catch (android.content.ActivityNotFoundException ex) {
                        Toast.makeText(ServiceUserActivity.this,
                                "Call failed, please try again later.", Toast.LENGTH_SHORT).show();
                    }
                    break;
                case R.id.userSendSMS:
                    Log.i("Send SMS", "");
                    String no = "" + mobileNumber.getText().toString();
                    Intent sendSMS = new Intent(Intent.ACTION_VIEW);
                    sendSMS.setType("vnd.android-dir/mms-sms");
                    sendSMS.putExtra("address", no);
                    try {
                        startActivity(sendSMS);
                    } catch (android.content.ActivityNotFoundException ex) {
                        Toast.makeText(ServiceUserActivity.this,
                                "SMS failed, please try again later.", Toast.LENGTH_SHORT).show();
                    }
                    break;
                case R.id.userSendEmail:
                    Log.i("Send Email", "");
                    String mail = "" + email.getText().toString();
                    Intent sendEmail = new Intent(Intent.ACTION_SEND);
                    try {
                        startActivity(sendEmail);
                    } catch (android.content.ActivityNotFoundException ex) {
                        Toast.makeText(ServiceUserActivity.this,
                                "Email failed, please try again later.", Toast.LENGTH_SHORT).show();
                    }
                    break;
                case R.id.kinPhoneCall:
                    Log.i("Make call", "");
                    String uri1 = "tel:" + nextOfKinContactNumber.getText().toString();
                    Intent phoneIntent1 = new Intent(Intent.ACTION_DIAL, Uri.parse(uri1));
                    try {
                        startActivity(phoneIntent1);
                    } catch (android.content.ActivityNotFoundException ex) {
                        Toast.makeText(ServiceUserActivity.this,
                                "Call failed, please try again later.", Toast.LENGTH_SHORT).show();
                    }
                    break;
                case R.id.kinSendSMS:
                    Log.i("Send SMS", "");
                    String no1 = "" + nextOfKinContactNumber.getText().toString();
                    Intent sendSMS1 = new Intent(Intent.ACTION_VIEW);
                    sendSMS1.setType("vnd.android-dir/mms-sms");
                    sendSMS1.putExtra("address", no1);
                    try {
                        startActivity(sendSMS1);
                    } catch (android.content.ActivityNotFoundException ex) {
                        Toast.makeText(ServiceUserActivity.this,
                                "SMS failed, please try again later.", Toast.LENGTH_SHORT).show();
                    }
                    break;
                case R.id.userCancel:
                    dialog.cancel();
                    break;
                case R.id.kinCancel:
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
        userPhoneCall = (Button) dialog.findViewById(R.id.userPhoneCall);
        userPhoneCall.setOnClickListener(new ButtonClick());
        userSendSMS = (Button) dialog.findViewById(R.id.userSendSMS);
        userSendSMS.setOnClickListener(new ButtonClick());
        userSendEmail = (Button) dialog.findViewById(R.id.userSendEmail);
        userSendEmail.setOnClickListener(new ButtonClick());
        userCancel = (Button) dialog.findViewById(R.id.userCancel);
        userCancel.setOnClickListener(new ButtonClick());
        dialog.show();
    }
    private void kinContact() {
        dialog = new Dialog(ServiceUserActivity.this);
        dialog.setContentView(R.layout.kin_contact_dialog_box);
        dialog.setTitle(R.string.contact_dialog_message);
        kinPhoneCall = (Button) dialog.findViewById(R.id.kinPhoneCall);
        kinPhoneCall.setOnClickListener(new ButtonClick());
        kinSendSMS = (Button) dialog.findViewById(R.id.kinSendSMS);
        kinSendSMS.setOnClickListener(new ButtonClick());
        kinCancel = (Button) dialog.findViewById(R.id.kinCancel);
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