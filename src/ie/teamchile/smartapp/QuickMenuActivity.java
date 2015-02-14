package ie.teamchile.smartapp;

import models.Login_model;
import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;
import connecttodb.Logout;

public class QuickMenuActivity extends Activity {
    private Button patientInfo;
    private Button bookAppointment;
    private Button calendar;
    private Button todaysAppointments;
    private Logout logout = new Logout();
    private Login_model login = new Login_model();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quick_menu);

        patientInfo = (Button) findViewById(R.id.patientInfo);
        patientInfo.setOnClickListener(new ButtonClick());
        bookAppointment = (Button) findViewById(R.id.bookAppointment);
        bookAppointment.setOnClickListener(new ButtonClick());
        calendar = (Button) findViewById(R.id.calendar);
        calendar.setOnClickListener(new ButtonClick());
        todaysAppointments = (Button) findViewById(R.id.todays_appointments);
        todaysAppointments.setOnClickListener(new ButtonClick());

        Login_model login = new Login_model();
        Log.d("MYLOG", "Before Other get token");
        Log.d("MYLOG", "Other get token: " + login.getToken());
    }
    private class ButtonClick implements View.OnClickListener {
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.patientInfo:
                    Intent intentPatient = new Intent(QuickMenuActivity.this, ServiceUserSearchActivity.class);
                    startActivity(intentPatient);
                    break;
                case R.id.bookAppointment:
                    Intent intentBook = new Intent(QuickMenuActivity.this, AppointmentTypeSpinnerActivity.class);
                    startActivity(intentBook);
                    break;
                case R.id.calendar:
                    Intent intentCalendar = new Intent(QuickMenuActivity.this, CalendarActivity.class);
                    startActivity(intentCalendar);
                    break;
                case R.id.todays_appointments:
                    Intent intentToday = new Intent(QuickMenuActivity.this, AppointmentCalendarActivity.class);
                    startActivity(intentToday);
                    break;
            }
        }
    }
	private class LongOperation extends AsyncTask<String, Void, String> {
		@Override
		protected void onPreExecute() {
		}
		protected String doInBackground(String... params) {
			logout.doLogout(Login_model.getToken());
			return null;
		}
		@Override
		protected void onProgressUpdate(Void... values) {
			Log.d("MYLOG", "On progress update");
		}
		@Override
        protected void onPostExecute(String result) {
        }
	}
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {		
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.main_menu, menu);
		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch(item.getItemId()) {
		case R.id.menu_item1 :		//logout			
			Log.d("MYLOG", "Logout button pressed");
			new LongOperation().execute((String[]) null);
			login.setToken(null);
			Intent intent = new Intent(getApplicationContext(), MainActivity.class);
			intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP); 
			Toast.makeText(this, "Logout Successful", Toast.LENGTH_LONG).show();
            startActivity(intent);
		case R.id.menu_item2 :
			System.out.println("Item 2 was selected!");
  
		}
		return super.onOptionsItemSelected(item);
	}
}