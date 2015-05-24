package ie.teamchile.smartapp.activities;

import ie.teamchile.smartapp.R;
import ie.teamchile.smartapp.connecttodb.AccessDBTable;
import ie.teamchile.smartapp.connecttodb.PostAppointment;
import ie.teamchile.smartapp.utility.AppointmentSingleton;
import ie.teamchile.smartapp.utility.ServiceUserSingleton;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class ConfirmAppointmentActivity extends MenuInheritActivity {
	private DateFormat sdfDateMonthName = new SimpleDateFormat("dd MMM yyyy", Locale.getDefault());
	private DateFormat sdfDate = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
    private TextView txtUserName, txtClinic, txtDateTime, 
    				 txtEmailTo, txtSmsTo;
    private Button btnYes, btnNo;
    private String name, hospitalNumber, clinicName, date, monthDate, time, duration, 
    		priority, clinicID, userId, visitType, timeBefore, timeAfter, email, sms;
    private ProgressDialog pd;
    private AccessDBTable db = new AccessDBTable();
    private PostAppointment postAppt = new PostAppointment();
    private Calendar cal = Calendar.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentForNav(R.layout.activity_confirm_appointment);
        
        txtUserName = (TextView) findViewById(R.id.text_confirm_user);
        txtClinic = (TextView) findViewById(R.id.text_confirm_appt_location);
        txtDateTime = (TextView) findViewById(R.id.text_confirm_appt_time);
        txtEmailTo = (TextView) findViewById(R.id.text_confirm_email);
        txtSmsTo = (TextView) findViewById(R.id.text_confirm_sms);

        btnYes = (Button) findViewById(R.id.btn_yes_appointment);
        btnYes.setOnClickListener(new ButtonClick());
        btnNo = (Button) findViewById(R.id.btn_no_appointment);
        btnNo.setOnClickListener(new ButtonClick());
        
        clinicName = getIntent().getStringExtra("clinicName");
        clinicID = getIntent().getStringExtra("clinicID");
        date = getIntent().getStringExtra("date");
        try {
        	cal.setTime(sdfDate.parse(date));
        	monthDate = sdfDateMonthName.format(sdfDate.parse(date));
		} catch (ParseException e) {
			e.printStackTrace();
		}
        time = getIntent().getStringExtra("time");
        duration = getIntent().getStringExtra("duration");
        priority = getIntent().getStringExtra("priority");
        visitType = getIntent().getStringExtra("visitType");
        timeBefore = getIntent().getStringExtra("timeBefore");
        timeAfter = getIntent().getStringExtra("timeAfter"); 
        userId = getIntent().getStringExtra("userId"); 
        visitType = getIntent().getStringExtra("visitType"); 
        
        Log.d("appointment", "userId: " + userId + "\nclinicName: " + clinicName + 
        		"\nclinicID: " +  clinicID  + "\nDate: " + monthDate + 
				"\nTime: " + time + "\nDuration: " + duration + 
				"\nPriority: " + priority + "\nVisit Type: " + visitType);
        
        name = ServiceUserSingleton.getInstance().getUserName().get(0);
        hospitalNumber = ServiceUserSingleton.getInstance().getUserHospitalNumber().get(0);
        email = ServiceUserSingleton.getInstance().getUserEmail().get(0);
        sms = ServiceUserSingleton.getInstance().getUserMobilePhone().get(0);
        
        txtUserName.setText(name + " (" + hospitalNumber + ")");
        //txtHospitalNumber.setText(hospitalNumber);
        txtClinic.setText(clinicName);
        txtDateTime.setText(time + " on " + monthDate);
        //txtTime.setText(time);
        txtEmailTo.setText(email);
        txtSmsTo.setText(sms);
    }

	private class ButtonClick implements View.OnClickListener {
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.btn_yes_appointment:
                	Log.d("bugs", "yes 	 button clicked");
                	new CreateAppointmentLongOperation(ConfirmAppointmentActivity.this).execute("appointments");
                    //passOptions.setDaySelected(cal.getTime());
                    break;
                case R.id.btn_no_appointment:
                	Log.d("bugs", "no button clicked");
                	Intent intent = new Intent(ConfirmAppointmentActivity.this, CreateAppointmentActivity.class);
                	intent.putExtra("from", "confirm");
                	intent.putExtra("clinicName", clinicName);
            		intent.putExtra("clinicID", clinicID);
            		intent.putExtra("date", date);
            		intent.putExtra("time", time);
            		intent.putExtra("duration", duration);
            		intent.putExtra("priority", priority);
            		intent.putExtra("timeBefore", timeBefore);
            		intent.putExtra("timeAfter", timeAfter);
            		intent.putExtra("userId", userId);
            		intent.putExtra("userName", name);
            		startActivity(intent);
                    break;
            }
        }
    }

    private class CreateAppointmentLongOperation extends AsyncTask<String, Void, Void> {
        private Context context;
        private JSONObject json;
        private JSONArray query;
        private Boolean userFound;

        public CreateAppointmentLongOperation(Context context){ this.context = context; }

        @Override
        protected void onPreExecute() {
            pd = new ProgressDialog(context);
            pd.setMessage("Creating Appointment");
            pd.setCanceledOnTouchOutside(false);
			pd.setCancelable(false);
            pd.show();
        }
        protected Void doInBackground(String... params) {
        	Log.d("buggy_bug", "userId: " + userId + "\nclinicID: " +  clinicID  + 
        		  "\nDate: " + date +  "\nTime: " + time + ":00" + "\nDuration: " + duration + 
   				  "\nPriority: " + priority + "\nVisit Type: " + visitType);
            postAppt.postAppointment(userId, clinicID, date, (time + ":00") , duration, priority, visitType);
            userFound = true;
            try {
                json = db.accessDB(params[0]);
                query = json.getJSONArray(params[0]);
                AppointmentSingleton.getInstance().setHashMapofClinicDateID(query);
                AppointmentSingleton.getInstance().setHashMapofIdAppt(query);
            } catch (JSONException e) {
                e.printStackTrace();
            }
			return null;
        }
        
        @Override
        protected void onProgressUpdate(Void... values) { }
        
        @Override
        protected void onPostExecute(Void result) {
        	Intent intent = new Intent(ConfirmAppointmentActivity.this, AppointmentCalendarActivity.class);
        	try {
				Thread.sleep(500);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
            startActivity(intent);
            pd.dismiss();
        }
    }
}