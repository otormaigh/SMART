package ie.teamchile.smartapp;

import android.app.Activity;
import android.app.AlertDialog;
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

public class ServiceUserActivity extends Activity {
	private TextView hospitalNumber, name, age, email, mobileNumber, road,  county,
            postCode, nextOfKinName, nextOfKinContactNumber;
    private String dob;
    private Button bookAppointmentButton, userContact;
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
		
		 Button userContact = (Button) findViewById(R.id.user_contact);
		 userContact.setOnClickListener(new View.OnClickListener() {
	         public void onClick(View view) {
	         makeCall();
	      }
	   });
        
	}
	
	private void makeCall() {
		// TODO Auto-generated method stub
    	new AlertDialog.Builder(this)
     	.setTitle(R.string.Logout_title)
     		.setMessage(R.string.Logout_dialog_message)
     		.setNegativeButton(R.string.No,
     				new DialogInterface.OnClickListener()
     				{
     					public void onClick(DialogInterface dialoginterface, int i)
     					{}
     				}
     			)
     		.setPositiveButton(R.string.Yes,
     				new DialogInterface.OnClickListener()
     				{
     					public void onClick(DialogInterface dialoginterface, int i)
     					{
     						 Log.i("Make call", "");

     					      Intent phoneIntent = new Intent(Intent.ACTION_CALL);
     					      phoneIntent.setData(Uri.parse(mobile_number.));
     						try {
     					         startActivity(phoneIntent);
     					         finish();
     					         Log.i("Finished making a call...", "");
     					      } catch (android.content.ActivityNotFoundException ex) {
     					         Toast.makeText(ServiceUserActivity.this, 
     					         "Call faild, please try again later.", Toast.LENGTH_SHORT).show();
     					      }
     					   }
     				
     				}
     			)
     		.show();
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
    private class ButtonClick implements View.OnClickListener {
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.book_appointment:
                    Intent intent = new Intent(ServiceUserActivity.this, AppointmentTypeSpinnerActivity.class);
                    startActivity(intent);
                    break;
            }
        }
    }
}