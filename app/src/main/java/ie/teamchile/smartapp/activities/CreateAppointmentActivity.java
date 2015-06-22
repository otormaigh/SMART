package ie.teamchile.smartapp.activities;

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

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import ie.teamchile.smartapp.R;
import ie.teamchile.smartapp.model.ApiRootModel;
import ie.teamchile.smartapp.model.ServiceUser;
import ie.teamchile.smartapp.util.AdapterSpinner;
import ie.teamchile.smartapp.util.SmartApi;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class CreateAppointmentActivity extends BaseActivity {
	private ArrayAdapter<String> visitPriorityAdapter, returnTypeAdapter;
	private String userName, apptDate, time, priority, visitType, clinicName;
	private int userID;
	private Calendar c, myCalendar;
    private Date daySelected;
	private List<Integer> idList = new ArrayList<>();
	private AppointmentCalendarActivity passOptions = new AppointmentCalendarActivity();
	private SharedPreferences prefs;
	private AlertDialog.Builder alertDialog;
	private AlertDialog ad;
	private int clinicID, serviceOptionId;
	private int p = 0;
	private EditText etUserName;
	private Button btnConfirmAppointment;
	private ImageButton btnUserSearch;
	private TextView tvTime, tvTimeTitle, tvDate, tvClinic;
	private Spinner visitReturnTypeSpinner, visitPrioritySpinner;
	private List<ServiceUser> serviceUserList = new ArrayList<>();
	private String returnType;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentForNav(R.layout.activity_create_appointment);

		c = Calendar.getInstance();
		myCalendar = Calendar.getInstance();
		
		etUserName = (EditText) findViewById(R.id.et_service_user);
		btnConfirmAppointment = (Button) findViewById(R.id.btn_confirm_appointment);
		btnUserSearch = (ImageButton) findViewById(R.id.btn_user_search);
		tvTime = (TextView) findViewById(R.id.tv_visit_time);
        tvTimeTitle = (TextView) findViewById(R.id.tv_visit_time_title);
		tvDate = (TextView) findViewById(R.id.tv_visit_date);
		tvClinic = (TextView) findViewById(R.id.tv_visit_clinic);

        btnConfirmAppointment.setOnClickListener(new ButtonClick());
        btnUserSearch.setOnClickListener(new ButtonClick());

        visitReturnTypeSpinner = (Spinner) findViewById(R.id.spnr_visit_return_type);
        visitReturnTypeSpinner.setOnItemSelectedListener(new MySpinnerOnItemSelectedListener());

        visitPrioritySpinner = (Spinner) findViewById(R.id.spnr_visit_priority);
        visitPrioritySpinner.setOnItemSelectedListener(new MySpinnerOnItemSelectedListener());

		Log.d("postAppointment", "time now: " + c.getTime());
		getSharedPrefs();

        clinicID = Integer.parseInt(getIntent().getStringExtra("clinicID"));
        serviceOptionId = Integer.parseInt(getIntent().getStringExtra("serviceOptionId"));

        checkDirectionOfIntent();
        setReturnTypeSpinner();
        setPrioritySpinner();
        checkIfEditEmpty();
	}

    private void clinicAppt(){
        getSharedPrefs();
        daySelected = AppointmentCalendarActivity.daySelected;
        clinicName = ApiRootModel.getInstance().getClinicMap().get(clinicID).getName();
        time = getIntent().getStringExtra("time");
        tvTime.setText(time);
        visitPrioritySpinner.setSelection(1);

        myCalendar.setTime(daySelected);
        tvDate.setText(dfDateMonthNameYear.format(daySelected));

        tvClinic.setText(clinicName);
    }

    private void homeVisitAppt(){
        daySelected = HomeVisitAppointmentActivity.daySelected;
        clinicName = ApiRootModel.getInstance().getServiceOptionsHomeMap().get(serviceOptionId).getName();
        tvTime.setVisibility(View.GONE);
        tvTimeTitle.setVisibility(View.GONE);
        visitPrioritySpinner.setSelection(3);

        myCalendar.setTime(daySelected);
        tvDate.setText(dfDateMonthNameYear.format(daySelected));

        tvClinic.setText(clinicName);
    }

    @Override
	protected void onResume() {
		super.onResume();
		Log.d("MYLOG", "In onResume CreateAppointment");
		checkDirectionOfIntent();
	}

	private void checkDirectionOfIntent(){
		String intentOrigin = getIntent().getStringExtra("from");
		if(intentOrigin.equals("clinic-appointment")) {
			clinicAppt();
            Log.d("bugs", "intent from clinic");
		} else if (intentOrigin.equals("confirm")) {
			etUserName.setText(getIntent().getStringExtra("userName"));
			userID = Integer.parseInt(getIntent().getStringExtra("userId"));
            Log.d("bugs", "intent from confirm");
		} else if (intentOrigin.equals("home-visit")) {
            homeVisitAppt();
            Log.d("bugs", "intent from home visit");
        }
	}

	private void checkIfEditEmpty(){
		if(TextUtils.equals(String.valueOf(userID), "") || TextUtils.equals(etUserName.getText(), "")) {
		    etUserName.setError("Field Empty");
        }
	}

    private Boolean checkIfOkToGo(){
        if(!userName.isEmpty() &&
                visitPrioritySpinner.getSelectedItemPosition() != 0 &&
                visitReturnTypeSpinner.getSelectedItemPosition() != 0){
            return true;
        } else
            return false;
    }

	private void getSharedPrefs(){
		prefs = getSharedPreferences("SMART", MODE_PRIVATE);

		if (prefs != null && prefs.getBoolean("reuse", false)) {
			etUserName.setText(prefs.getString("name", null));
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

    private void setPrioritySpinner() {
        visitPriorityAdapter = new AdapterSpinner(this,
                R.array.visit_priority_list,
                R.layout.spinner_layout,
                R.id.tv_spinner_item);
        visitPriorityAdapter.setDropDownViewResource(R.layout.spinner_layout);
        visitPrioritySpinner.setAdapter(visitPriorityAdapter);
    }

	private void setReturnTypeSpinner(){
        returnTypeAdapter = new AdapterSpinner(this,
                R.array.return_type_list,
				R.layout.spinner_layout,
                R.id.tv_spinner_item);
        returnTypeAdapter.setDropDownViewResource(R.layout.spinner_layout);
		visitReturnTypeSpinner.setAdapter(returnTypeAdapter);
	}

	private class ButtonClick implements View.OnClickListener {
        public void onClick(View v) {
            switch (v.getId()) {
            case R.id.btn_confirm_appointment:
            	userName = etUserName.getText().toString();
            	apptDate = dfDateOnly.format(myCalendar.getTime());
            	passOptions.setDaySelected(myCalendar.getTime());
            	checkIfEditEmpty();

            	if(checkIfOkToGo()) {
            		Intent intent = new Intent(CreateAppointmentActivity.this, ConfirmAppointmentActivity.class);
            		Bundle extras = new Bundle();
            		extras.putString("clinicName", clinicName);
            		extras.putString("clinicID", String.valueOf(clinicID));
            		extras.putString("serviceOptionId", String.valueOf(serviceOptionId));
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
            	userName = etUserName.getText().toString();
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
						buildAlertDialog(searchResults);
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
	
	private void buildAlertDialog(List<String> searchResults){      	    	
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
                case R.id.spnr_visit_return_type:
					switch(position){
						case 0:
                            break;
						case 1:
							returnType = "returning";
							break;
						case 2:
							returnType = "new";
							break;
					}
                	break;
                case R.id.spnr_visit_priority:
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
                    case 3:
                        priority = "home-visit";
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
					etUserName.setText(serviceUser.getPersonalFields().getName());
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
