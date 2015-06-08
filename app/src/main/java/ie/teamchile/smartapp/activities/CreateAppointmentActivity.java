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
import java.util.List;

import ie.teamchile.smartapp.R;
import ie.teamchile.smartapp.model.ApiRootModel;
import ie.teamchile.smartapp.model.ServiceUser;
import ie.teamchile.smartapp.utility.SmartApi;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class CreateAppointmentActivity extends BaseActivity {
	private ArrayAdapter<CharSequence> visitPriorityAdapter, visitPriorityAdapterSelect,
            returnTypeAdapter, returnTypeAdapterSelect;
	private String userName, apptDate, time, priority, visitType, clinicName;
	private int appointmentInterval, userID;
	private Calendar c, myCalendar;
	private List<Integer> idList = new ArrayList<>();
	private AppointmentCalendarActivity passOptions = new AppointmentCalendarActivity();
	private SharedPreferences prefs;
	private AlertDialog.Builder alertDialog;
	private AlertDialog ad;
	private int clinicID;
	private int p = 0;
	private EditText etUserName;
	private Button btnConfirmAppointment;
	private ImageButton btnUserSearch;
	private TextView tvTime, tvDate, tvClinic;
	private Spinner visitReturnTypeSpinner, visitReturnTypeSpinnerSelect,
            visitPrioritySpinner, visitPrioritySpinnerSelect;
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
		tvDate = (TextView) findViewById(R.id.tv_visit_date);
		tvClinic = (TextView) findViewById(R.id.tv_visit_clinic);

        btnConfirmAppointment.setOnClickListener(new ButtonClick());
        btnUserSearch.setOnClickListener(new ButtonClick());

        visitReturnTypeSpinnerSelect = (Spinner) findViewById(R.id.spnr_visit_return_type_select);
        visitReturnTypeSpinnerSelect.setOnItemSelectedListener(new MySpinnerOnItemSelectedListener());

        visitReturnTypeSpinner = (Spinner) findViewById(R.id.spnr_visit_return_type);
        visitReturnTypeSpinner.setOnItemSelectedListener(new MySpinnerOnItemSelectedListener());

        visitPrioritySpinner = (Spinner) findViewById(R.id.spnr_visit_priority);
        visitPrioritySpinner.setOnItemSelectedListener(new MySpinnerOnItemSelectedListener());

        visitPrioritySpinnerSelect = (Spinner) findViewById(R.id.spnr_visit_priority_select);
        visitPrioritySpinnerSelect.setOnItemSelectedListener(new MySpinnerOnItemSelectedListener());

        visitReturnTypeSpinnerSelect.setVisibility(View.GONE);
        visitPrioritySpinnerSelect.setVisibility(View.GONE);

		Log.d("postAppointment", "time now: " + c.getTime());
		getSharedPrefs();

		myCalendar.setTime(AppointmentCalendarActivity.daySelected);
		tvDate.setText(dfDateMonthNameYear.format(AppointmentCalendarActivity.daySelected));

		clinicID = Integer.parseInt(getIntent().getStringExtra("clinicID"));
		clinicName = ApiRootModel.getInstance().getClinicsMap().get(clinicID).getName();
		tvClinic.setText(clinicName);

		appointmentInterval = ApiRootModel.getInstance().getClinicsMap().get(clinicID).getAppointmentInterval();
        time = getIntent().getStringExtra("time");
        tvTime.setText(time);
        
		Log.d("postAppointment", "timeAfter: " + time);
		
		setReturnTypeSpinner();
        setPrioritySpinner();
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
			etUserName.setText(getIntent().getStringExtra("userName"));
			userID = Integer.parseInt(getIntent().getStringExtra("userId"));
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
                visitPrioritySpinnerSelect.getSelectedItemPosition() != 0 &&
                visitReturnTypeSpinner.getSelectedItemPosition() != 0 &&
                visitReturnTypeSpinnerSelect.getSelectedItemPosition() != 0){
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
        visitPriorityAdapter = ArrayAdapter.createFromResource(this,
                R.array.visit_priority_list,
                R.layout.spinner_layout_create_appt);
        visitPriorityAdapter.setDropDownViewResource(R.layout.spinner_layout);
        visitPrioritySpinner.setAdapter(visitPriorityAdapter);

        visitPriorityAdapterSelect = ArrayAdapter.createFromResource(this,
                R.array.visit_priority_list,
                R.layout.spinner_selected_layout_create_appt);
        visitPriorityAdapterSelect.setDropDownViewResource(R.layout.spinner_layout);
        visitPrioritySpinnerSelect.setAdapter(visitPriorityAdapterSelect);
    }

	private void setReturnTypeSpinner(){
        returnTypeAdapter = ArrayAdapter.createFromResource(this,
                R.array.return_type_list,
				R.layout.spinner_layout_create_appt);
        returnTypeAdapter.setDropDownViewResource(R.layout.spinner_layout);
		visitReturnTypeSpinner.setAdapter(returnTypeAdapter);

        returnTypeAdapterSelect = ArrayAdapter.createFromResource(this,
                R.array.return_type_list,
                R.layout.spinner_selected_layout_create_appt);
        returnTypeAdapterSelect.setDropDownViewResource(R.layout.spinner_layout);
        visitReturnTypeSpinnerSelect.setAdapter(returnTypeAdapterSelect);
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
                case R.id.spnr_visit_return_type:
					switch(position){
						case 0:
                            visitReturnTypeSpinner.setVisibility(View.VISIBLE);
                            visitReturnTypeSpinnerSelect.setVisibility(View.GONE);
                            visitReturnTypeSpinner.setSelection(position);
                            break;
						case 1:
                            visitReturnTypeSpinner.setVisibility(View.GONE);
                            visitReturnTypeSpinnerSelect.setVisibility(View.VISIBLE);
                            visitReturnTypeSpinnerSelect.setSelection(position);
							returnType = "returning";
							break;
						case 2:
                            visitReturnTypeSpinner.setVisibility(View.GONE);
                            visitReturnTypeSpinnerSelect.setVisibility(View.VISIBLE);
                            visitReturnTypeSpinnerSelect.setSelection(position);
							returnType = "new";
							break;
					}
                	break;
                case R.id.spnr_visit_return_type_select:
                    switch(position){
                        case 0:
                            visitReturnTypeSpinner.setVisibility(View.VISIBLE);
                            visitReturnTypeSpinnerSelect.setVisibility(View.GONE);
                            visitReturnTypeSpinner.setSelection(position);
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
                        visitPrioritySpinner.setVisibility(View.VISIBLE);
                        visitPrioritySpinnerSelect.setVisibility(View.GONE);
                    	break;
                    case 1:
                    	priority = "scheduled";
                        visitPrioritySpinner.setVisibility(View.GONE);
                        visitPrioritySpinnerSelect.setVisibility(View.VISIBLE);
                        visitPrioritySpinnerSelect.setSelection(position);
                    	//Scheduled
                    	break;
                    case 2:
                    	priority = "drop-in";
                        visitPrioritySpinner.setVisibility(View.GONE);
                        visitPrioritySpinnerSelect.setVisibility(View.VISIBLE);
                        visitPrioritySpinnerSelect.setSelection(position);
                    	//Drop-In
                    	break;
                    }
                    break;
                case R.id.spnr_visit_priority_select:
                switch (position) {
                    case 0:
                        //Select Visit Priority
                        visitPrioritySpinner.setVisibility(View.VISIBLE);
                        visitPrioritySpinnerSelect.setVisibility(View.GONE);
                        visitPrioritySpinner.setSelection(position);
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
