
package com.team3.smartapp;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;
import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class ServiceUserActivity extends Activity {
	private TextView hospitalNumber, name, age, email, mobileNumber, road,  county,
            postCode, nextOfKinName, nextOfKinContactNumber;
    private String dob;
    private Button bookAppointmentButton;
    private Date dobAsDate;
    private Calendar cal = Calendar.getInstance();
	CharSequence[] items = { "go to maps","go back" };
	boolean[] itemsChecked = new boolean [items.length];
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
    public int getAge(String dob) {
        try {
            DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
            dobAsDate =  df.parse(dob);

        } catch (ParseException e) {
            e.printStackTrace();
        }
       // Calendar calf = Calendar.getInstance();
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
    private class ButtonClick implements View.OnClickListener {
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.book_appointment:
                    Intent intent = new Intent(ServiceUserActivity.this, AppointmentTypeSpinnerActivity.class);
                    startActivity(intent);
                    break;
            }}}
    public void onClick2(View v) {
    	showDialog(0);
    	}		

    protected Dialog onCreateDialog(int id) {
    	switch (id) {
    	case 0:
    	return new AlertDialog.Builder(this)
    	.setIcon(R.drawable.ic_launcher)
    	.setTitle("This is a dialog with some simple text...")
    	.setPositiveButton("OK",
    	new DialogInterface.OnClickListener() {
    	public void onClick(DialogInterface dialog, int whichButton)
    	{
    	Toast.makeText(getBaseContext(),
    	"OK clicked!", Toast.LENGTH_SHORT).show();
    	}
    	}
    	)
    	.setNegativeButton("Cancel",
    	new DialogInterface.OnClickListener() {
    	public void onClick(DialogInterface dialog, int whichButton)
    	{
    	Toast.makeText(getBaseContext(),
    	"Cancel clicked!", Toast.LENGTH_SHORT).show();
    	}
    	}
    	)
    	.setMultiChoiceItems(items, itemsChecked,
    	new DialogInterface.OnMultiChoiceClickListener() {
    	public void onClick(DialogInterface dialog,
    	int which, boolean isChecked) {
    	Toast.makeText(getBaseContext(),
    	items[which] + (isChecked ? " checked!":" unchecked!"),
    	Toast.LENGTH_SHORT).show();
    	
    	}
    	}
    	).create();
    	}
    	return null;
    	}
    	}
    	

































