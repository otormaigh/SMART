package ie.teamchile.smartapp.activities;

import ie.teamchile.smartapp.R;
import ie.teamchile.smartapp.retrofit.ApiRootModel;
import ie.teamchile.smartapp.retrofit.ServiceUser;
import ie.teamchile.smartapp.retrofit.SmartApi;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

public class CreateAppointmentActivity extends BaseActivity {
	private ArrayAdapter<String> returnTypeAdapterArrayAdapter;
	private ArrayAdapter<CharSequence> visitPriorityAdapter;
	private String userName, clinic, apptDate, time, duration, priority, visitType,
			clinicIDStr, clinicName;
	private int appointmentInterval, userID;
	private Calendar c, myCalendar;
	private List<String> timeList = new ArrayList<String>();
	private List<String> returnTypeList = new ArrayList<String>();
	private List<String> babyIDs;
	private List<Integer> idList = new ArrayList<>();
	private AppointmentCalendarActivity passOptions = new AppointmentCalendarActivity();
	private SharedPreferences prefs;
	private AlertDialog.Builder alertDialog;
	private AlertDialog ad;
	private int clinicID, appointmentIntervalAsInt;
	private int p = 0;
	private EditText tvUserName;
	private Button confirmAppointment;
	private ImageButton btnUserSearch;
	private TextView textTime, textDate, textClinic;
	private Spinner visitReturnTypeSpinner, visitPrioritySpinner;
	private List<ServiceUser> serviceUserList = new ArrayList<>();
	private String returnType;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentForNav(R.layout.activity_create_appointment);
		//ButterKnife.inject(this);
		
		c = Calendar.getInstance();
		myCalendar = Calendar.getInstance();
		
		tvUserName = (EditText) findViewById(R.id.edit_service_user);
		confirmAppointment = (Button) findViewById(R.id.btn_confirm_appointment);
		btnUserSearch = (ImageButton) findViewById(R.id.btn_user_search);
		textTime = (TextView) findViewById(R.id.visit_time_text);
		textDate = (TextView) findViewById(R.id.visit_date_text);
		textClinic = (TextView) findViewById(R.id.visit_clinic_text);
		visitReturnTypeSpinner = (Spinner) findViewById(R.id.visit_return_type_spinner);
		visitPrioritySpinner = (Spinner) findViewById(R.id.visit_priority_spinner);

        confirmAppointment.setOnClickListener(new ButtonClick());
        btnUserSearch.setOnClickListener(new ButtonClick());
        
        visitReturnTypeSpinner.setOnItemSelectedListener(new MySpinnerOnItemSelectedListener());
		visitReturnTypeSpinner = (Spinner) findViewById(R.id.visit_return_type_spinner);

        visitPrioritySpinner.setOnItemSelectedListener(new MySpinnerOnItemSelectedListener());
        visitPriorityAdapter = ArrayAdapter.createFromResource(this, 
        				R.array.visit_priority_list, 
        				R.layout.spinner_layout_create_appt);
        visitPriorityAdapter.setDropDownViewResource(R.layout.spinner_layout);
        visitPrioritySpinner.setAdapter(visitPriorityAdapter);
        visitPrioritySpinner.setSelection(1);		//sets visit pirority to Scheduled

		Log.d("postAppointment", "time now: " + c.getTime());
		getSharedPrefs();

		myCalendar.setTime(AppointmentCalendarActivity.daySelected);
		textDate.setText(dfDateMonthNameYear.format(AppointmentCalendarActivity.daySelected));

		clinicID = Integer.parseInt(getIntent().getStringExtra("clinicID"));
		clinicName = ApiRootModel.getInstance().getClinicsMap().get(clinicID).getName();
		textClinic.setText(clinicName);

		appointmentInterval = ApiRootModel.getInstance().getClinicsMap().get(clinicID).getAppointmentInterval();
        time = getIntent().getStringExtra("time");
        textTime.setText(time);
        
		Log.d("postAppointment", "timeAfter: " + time);
		
		setTypeSpinner();
		checkIfEditEmpty();
		checkDirectionOfIntent();
	}
	
	@Override
	protected void onResume() {
		super.onResume();
		Log.d("MYLOG", "In onResume CreateAppointment");
		checkDirectionOfIntent();
	}
	
	private void checkDirectionOfIntent(){
		String intentOrigin = getIntent().getStringExtra("from"); 
		if(intentOrigin.equals("appointment")) {
			getSharedPrefs();
		} else if (intentOrigin.equals("confirm")) {
			tvUserName.setText(getIntent().getStringExtra("userName"));
			userID = Integer.parseInt(getIntent().getStringExtra("userId"));
		}
	}

	private void checkIfEditEmpty(){		
		if(TextUtils.equals(String.valueOf(userID), "") || TextUtils.equals(tvUserName.getText(), "")) {
		    tvUserName.setError("Field Empty");
		    return;
		 }
	}
	
	private void getSharedPrefs(){
		prefs = getSharedPreferences("SMART", MODE_PRIVATE);

		if (prefs != null && prefs.getBoolean("reuse", false)) {
			tvUserName.setText(prefs.getString("name", null));
			userID = Integer.parseInt(prefs.getString("id", ""));
			visitType = prefs.getString("visit_type", null);
		}
	}
	
/*    private void setSharedPrefs(){
    	SharedPreferences.Editor prefs = getSharedPreferences("SMART", MODE_PRIVATE).edit();
		prefs.putString("userName", ApiRootModel.getInstance().getServiceUsers().get(0).getPersonalFields().getName(););
		prefs.putString("id", ApiRootModel.getInstance().getServiceUsers().get(0).getId().toString());
		prefs.putBoolean("reuse", true);
		prefs.commit();
    }*/

	private void setTypeSpinner(){
		returnTypeList.add("Select Type");
		returnTypeList.add("Follow-up (15 min)");
		returnTypeList.add("New (30 min)");

		returnTypeAdapterArrayAdapter = new ArrayAdapter<>(this,
				R.layout.spinner_layout_create_appt,
				returnTypeList);
		returnTypeAdapterArrayAdapter.setDropDownViewResource(R.layout.spinner_layout);
		visitReturnTypeSpinner.setAdapter(returnTypeAdapterArrayAdapter);
	}
	
	private class ButtonClick implements View.OnClickListener {
        public void onClick(View v) {
            switch (v.getId()) {
            case R.id.btn_confirm_appointment:
            	userName = tvUserName.getText().toString();
            	apptDate = dfDateOnly.format(myCalendar.getTime());
            	passOptions.setDaySelected(myCalendar.getTime());
            	checkIfEditEmpty();

            	if(!userName.isEmpty() && visitPrioritySpinner.getSelectedItemPosition() != 0) {
            		Intent intent = new Intent(CreateAppointmentActivity.this, ConfirmAppointmentActivity.class);
            		Bundle extras = new Bundle();
            		extras.putString("clinicName", clinicName);
            		extras.putString("clinicID", String.valueOf(clinicID));
            		extras.putString("date", apptDate);
            		extras.putString("time", time);
            		extras.putString("return_type", returnType);
            		extras.putString("priority", priority);
            		extras.putString("userId", String.valueOf(userID));
            		extras.putString("visitType", visitType);
            		intent.putExtras(extras);
            		startActivity(intent);
            	} else {
					showProgressDialog(
							CreateAppointmentActivity.this,
							"Cannot proceed, \nSome fields are empty!");
        			new CountDownTimer(2000, 1000){
						@Override
						public void onFinish() {
							pd.dismiss();
						}
						@Override
						public void onTick(long millisUntilFinished) { }
        			}.start();
            	}
            	break;            
            case R.id.btn_user_search:
            	hideKeyboard();
            	userID = 0;
            	userName = tvUserName.getText().toString();
            	checkIfEditEmpty();
				showProgressDialog(CreateAppointmentActivity.this, "Fetching Information");
				searchPatient(userName);
            	break;
            } 
        }
	}

	private void searchPatient(String serviceUserName){
		api.getServiceUserByName(
			serviceUserName,
			ApiRootModel.getInstance().getLogin().getToken(),
			SmartApi.API_KEY,
			new Callback<ApiRootModel>() {
				@Override
				public void success(ApiRootModel apiRootModel, Response response) {
					String name, hospitalNumber, dob;
					List<String> searchResults = new ArrayList<>();
					int id;
					if(apiRootModel.getServiceUsers().size() != 0){
						ApiRootModel.getInstance().setServiceUsers(apiRootModel.getServiceUsers());
						ApiRootModel.getInstance().setPregnancies(apiRootModel.getPregnancies());
						ApiRootModel.getInstance().setBabies(apiRootModel.getBabies());
						for(int i = 0; i < apiRootModel.getServiceUsers().size(); i++){
							ServiceUser serviceUserItem = apiRootModel.getServiceUsers().get(i);
							serviceUserList.add(serviceUserItem);
							name = serviceUserItem.getPersonalFields().getName();
							hospitalNumber = serviceUserItem.getHospitalNumber();
							dob = serviceUserItem.getPersonalFields().getDob();
							id = serviceUserItem.getId();

							Log.d("Retro", name + "\n" + hospitalNumber + "\n" + dob + "\n" + id);
							idList.add(id);
							searchResults.add(name + "\n" + hospitalNumber + "\n" + dob);
						}
						//postOrAnte();
						//getRecentPregnancy();
						pd.dismiss();
						buildeAlertDialog(searchResults);
					} else {
						pd.dismiss();
						Toast.makeText(getApplicationContext(), "No search results found", Toast.LENGTH_SHORT).show();
					}
				}

				@Override
				public void failure(RetrofitError error) {
					Log.d("Retro", "retro failure error = " + error);
				}
			}
		);
	}
	
	private void buildeAlertDialog(List<String> searchResults){      	    	
		LayoutInflater inflater = getLayoutInflater();
		alertDialog = new AlertDialog.Builder(CreateAppointmentActivity.this);
		View convertView = (View) inflater.inflate(R.layout.list, null);
		ListView list = (ListView) convertView.findViewById(R.id.list_dialog);
		
		list.setOnItemClickListener(new onItemListener());
		
		alertDialog.setView(convertView);
		alertDialog.setTitle("Search Results");
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(
				CreateAppointmentActivity.this, 
				android.R.layout.simple_list_item_1, 
				searchResults);
		list.setAdapter(adapter);
		ad = alertDialog.show();
	}

    private class MySpinnerOnItemSelectedListener implements AdapterView.OnItemSelectedListener {
        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            switch (parent.getId()) {
                case R.id.visit_return_type_spinner:
					switch(position){
						case 0:
							//visitReturnTypeSpinner.setBackgroundColor(spinnerWarning);
							//visitReturnTypeSpinner.set(R.drawable.ic_menu_grey600_24dp);
							break;
						case 1:
							//visitReturnTypeSpinner.setBackgroundColor(Color.TRANSPARENT);
							returnType = "returning";
							break;
						case 2:
							//visitReturnTypeSpinner.setBackgroundColor(Color.TRANSPARENT);
							returnType = "new";
							break;
					}
                	break;
                case R.id.visit_priority_spinner:
                    switch (position) {
                    case 0:
                    	//Select Visit Priority
                    	break;
                    case 1:
                    	priority = "scheduled";
                    	//Scheduled
                    	break;
                    case 2:
                    	priority = "drop-in";
                    	//Drop-In
                    	break;
                    }
                    break;
                case R.id.list_dialog:
                	Log.d("bugs", "list position is: " + position);
                	break;
            }
        }

		@Override
		public void onNothingSelected(AdapterView<?> arg0) { }
    }
    
	private class onItemListener implements OnItemClickListener {
		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
			switch (parent.getId()){
				case R.id.list_dialog:
					hideKeyboard();
					ServiceUser serviceUser = serviceUserList.get(position);
					tvUserName.setText(serviceUser.getPersonalFields().getName());
					userID = serviceUser.getId();
					postOrAnte();
					ad.cancel();
	            	break;
			}
		}
	}
	
	private void postOrAnte(){


		visitType = "post-natal";		//this need to be changed


		/*//babyIDs = ;
		if(babyIDs.get(p).equals("[]")){
			visitType = "ante-natal";
    	}else {
    		visitType = "post-natal";
    	}*/
	}
	/*
    private void getRecentPregnancy(){
    	List<String> edd = new ArrayList<String>();
    	List<Date> asDate = new ArrayList<Date>();
    	
    	edd = getPregnancyEstimatedDeliveryDate();
    	Log.d("bugs", "edd.size(): " + edd.size());
    	if(edd.size() > 0){
    		for(int i = 0; i < edd.size(); i++){
    			if(!edd.get(i).equals("null")){
	    			Log.d("bugs", "edd.get(i): " + edd.get(i));
	        		try {
	    				asDate.add(dfDateOnly.parse(edd.get(i)));
	    			} catch (ParseException e) {
	    				e.printStackTrace();
	    			}
    			}
        	}    
    		try {
    			p = asDate.indexOf(Collections.max(asDate));
    		} catch (NoSuchElementException e) {
				e.printStackTrace();
			}
    	}    	
    }*/
    
	private void hideKeyboard() {
		InputMethodManager inputManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
		inputManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
	}
}
