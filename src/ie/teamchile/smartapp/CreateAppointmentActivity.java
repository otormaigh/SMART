package ie.teamchile.smartapp;

import org.json.JSONException;
import org.json.JSONObject;


import utility.ServiceUserSingleton;
import android.app.Activity;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import connecttodb.AccessDBTable;
import connecttodb.PostAppointment;

public class CreateAppointmentActivity extends MenuInheritActivity {

	private TextView userName, appointmentClinic, apptDate, apptTime,
	 				 apptDuration, apptPriority, apptVisitType;
	private Button confirmAppointment;
	private String name, clinic, date, time, duration, priority, visitType;
	private PostAppointment postAppt = new PostAppointment();
	private AccessDBTable db = new AccessDBTable();
	private String response;
	private JSONObject jsonNew;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_create_appointment);
		
        userName = (TextView)findViewById(R.id.edit_service_user);
        appointmentClinic = (TextView) findViewById(R.id.edit_appointment_clinic);
        apptDate = (TextView)findViewById(R.id.edit_appointment_date);
        apptTime = (TextView)findViewById(R.id.edit_appointment_time);
        apptDuration = (TextView)findViewById(R.id.edit_duration);
        apptPriority = (TextView)findViewById(R.id.edit_priority);
        apptVisitType = (TextView)findViewById(R.id.edit_visit_type);
        confirmAppointment = (Button) findViewById(R.id.btn_confirm_appointment);
        confirmAppointment.setOnClickListener(new ButtonClick());
	}
	private class ButtonClick implements View.OnClickListener {
        public void onClick(View v) {
            switch (v.getId()) {
            case R.id.btn_confirm_appointment:
            	name = userName.getText().toString();
            	clinic = appointmentClinic.getText().toString();
            	date = apptDate.getText().toString();
            	time = apptTime.getText().toString();
            	duration = apptDuration.getText().toString();
            	priority = apptPriority.getText().toString();
            	visitType = apptVisitType.getText().toString();
            	
            	Log.d("appointment", "name: " + name + "\nclinic: " +  clinic + "\nDate: " + date + 
            						 "\nTime: " + time + "\nDuration: " + duration + "\nPriority: " + priority +
            						 "\nVisit Type: " + visitType);
            	
            	postAppt.postAppointment(name, clinic, date, time, 
						   				 duration, priority, visitType);
            	break;
            }
        }
	}
    private class LongOperation extends AsyncTask<String, Void, JSONObject> {
		private Context context;
		
		public LongOperation(Context context){
			this.context = context;
		}
		@Override
		protected void onPreExecute() {
/*			pd = new ProgressDialog(context);
            pd.setMessage("Fetching Information");
            pd.show();*/
		}
		protected JSONObject doInBackground(String... params) {
			Log.d("singleton", "in service users updateLocal doInBackground");
			try {
				response = db.accessDB(params[0]);
				jsonNew = new JSONObject(response);
			} catch (JSONException e) {
				e.printStackTrace();
			}
			Log.d("singleton", "query = " + jsonNew);
			return jsonNew;
		}
		@Override
		protected void onProgressUpdate(Void... values) {
		}
		@Override
        protected void onPostExecute(JSONObject result) {
			String hospitalNumber = ServiceUserSingleton.getInstance().getHospitalNumber();
/*			String email = ServiceUserSingleton.getSingletonIntance().getEmail(result);
			String mobile = ServiceUserSingleton.getSingletonIntance().getMobileNumber(result);;
			intent.putExtra("hospitalNumber", hospitalNumber);
			intent.putExtra("email", email);
			intent.putExtra("mobile", mobile);
			pd.dismiss();
			startActivity(intent);	*/
        }
	}
}
