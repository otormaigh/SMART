package ie.teamchile.smartapp.activities;

import android.content.ComponentCallbacks2;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import ie.teamchile.smartapp.R;
import ie.teamchile.smartapp.enums.CredentialsEnum;
import ie.teamchile.smartapp.model.ApiRootModel;
import ie.teamchile.smartapp.model.Appointment;
import ie.teamchile.smartapp.model.Clinic;
import ie.teamchile.smartapp.model.ServiceOption;
import ie.teamchile.smartapp.util.SmartApi;
import ie.teamchile.smartapp.util.ToastAlert;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class QuickMenuActivity extends BaseActivity {
    private boolean isViewVisible;
	private Button btnPatientSearch, btnBookAppointment, btnClinicRecord, btnTodaysAppointments;
	private CountDownTimer timer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
		setContentForNav(R.layout.activity_quick_menu);

        btnPatientSearch = (Button) findViewById(R.id.btn_patient_search);
        btnPatientSearch.setOnClickListener(new ButtonClick());
        btnBookAppointment = (Button) findViewById(R.id.btn_book_appointment);
        btnBookAppointment.setOnClickListener(new ButtonClick());
        btnClinicRecord = (Button) findViewById(R.id.btn_clinic_record);
		btnClinicRecord.setOnClickListener(new ButtonClick());
        btnTodaysAppointments = (Button) findViewById(R.id.btn_todays_appointments);
        //btnTodaysAppointments.setOnClickListener(new ButtonClick());
        btnTodaysAppointments.setEnabled(false);

		isViewVisible = true;
		updateData();
    }
    
	@Override
	protected void onNewIntent(Intent intent) {
	    super.onNewIntent(intent);
	    setIntent(intent);
	}
	
    private class ButtonClick implements View.OnClickListener {
		Intent intent;
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.btn_patient_search:
                    intent = new Intent(QuickMenuActivity.this, ServiceUserSearchActivity.class);
                    break;
                case R.id.btn_book_appointment:
					intent = new Intent(QuickMenuActivity.this, AppointmentTypeSpinnerActivity.class);
                    break;
                case R.id.btn_clinic_record:
					intent = new Intent(QuickMenuActivity.this, ClinicTimeRecordActivity.class);
                    break;
                /*case R.id.btn_todays_appointments:
                    intent = new Intent(QuickMenuActivity.this, TodayAppointmentActivity.class);
                    break;*/
            }
			startActivity(intent);
        }
    }
    
    @Override
    public void onBackPressed() {
    	if(ApiRootModel.getInstance().getLoginStatus()) {
    		new ToastAlert(getBaseContext(), 
        			"Already logged in, \n  Logout?", false);
    	}
    }

	@Override
	protected void onResume() {
		super.onResume();
		isViewVisible = true;
		SharedPreferences.Editor prefs = getSharedPreferences("SMART", MODE_PRIVATE).edit();
		prefs.putBoolean("reuse", false);
		prefs.commit();
	}

	@Override
	public void onTrimMemory(int level) {
		super.onTrimMemory(level);
		if(level >= ComponentCallbacks2.TRIM_MEMORY_UI_HIDDEN) {
			isViewVisible = false;
			new ToastAlert(getBaseContext(), "View is now hidden", false);
		} else
			isViewVisible = true;
	}

	private void updateData(){
		showProgressDialog(QuickMenuActivity.this, "Updating Information");

        updateAppointment(QuickMenuActivity.this);
        showDialog = false;

        api.getServiceProviderById(
                ApiRootModel.getInstance().getLogin().getId(),
                ApiRootModel.getInstance().getLogin().getToken(),
                SmartApi.API_KEY,
                new Callback<ApiRootModel>() {
                    @Override
                    public void success(ApiRootModel apiRootModel, Response response) {
                        Log.d("retro", "retro success");
                        ApiRootModel.getInstance().setServiceProvider(apiRootModel.getServiceProviders().get(0));
                        Log.d("Retrofit", "service provider finished");
                        done++;
                        Log.d("Retrofit", "done = " + done);
                    }

                    @Override
                    public void failure(RetrofitError error) {
                        Log.d("retro", "retro failure = " + error);
                    }
                }
        );

		api.getAllServiceOptions(
				ApiRootModel.getInstance().getLogin().getToken(),
				CredentialsEnum.API_KEY.toString(),
				new Callback<ApiRootModel>() {
					@Override
					public void success(ApiRootModel apiRootModel, Response response) {
                        Map<Integer, ServiceOption> serviceOptionHomeMap = new HashMap<>();
                        List<ServiceOption> serviceOptionHomeList = new ArrayList<>();
						Map<Integer, ServiceOption> serviceOptionClinicMap = new HashMap<>();
                        ApiRootModel.getInstance().setServiceOptions(apiRootModel.getServiceOptions());
                        for(int i = 0; i < apiRootModel.getServiceOptions().size(); i++){
                            ServiceOption option = apiRootModel.getServiceOptions().get(i);
                            if(option.getHomeVisit()){
                                serviceOptionHomeMap.put(option.getId(), option);
                                serviceOptionHomeList.add(option);
                            } else {
                                serviceOptionClinicMap.put(option.getId(), option);
                            }
						}
						ApiRootModel.getInstance().setServiceOptionsHomeList(serviceOptionHomeList);
						ApiRootModel.getInstance().setServiceOptionsHomeMap(serviceOptionHomeMap);
						ApiRootModel.getInstance().setServiceOptionsClinicMap(serviceOptionClinicMap);
						Log.d("Retrofit", "service options finished");
						done++;
						Log.d("Retrofit", "done = " + done);
					}

					@Override
					public void failure(RetrofitError error) {
						Log.d("Retrofit", "service options retro failure " + error);
					}
				}
		);
		api.getAllClinics(
				ApiRootModel.getInstance().getLogin().getToken(),
				CredentialsEnum.API_KEY.toString(),
				new Callback<ApiRootModel>() {
					@Override
					public void success(ApiRootModel things, Response response) {
						Map<Integer, Clinic> clinicMap = new HashMap<>();
						ApiRootModel.getInstance().setClinics(things.getClinics());
						for(int i = 0; i < things.getClinics().size(); i++){
							clinicMap.put(things.getClinics().get(i).getId(),
									things.getClinics().get(i));
						}
						ApiRootModel.getInstance().setClinicMap(clinicMap);
						Log.d("Retrofit", "clinics finished");
						done++;
						Log.d("Retrofit", "done = " + done);
					}

					@Override
					public void failure(RetrofitError error) {
						Log.d("Retrofit", "clinics retro failure " + error);
					}
				}
		);

		timer = new CountDownTimer(200, 100) {
			@Override
			public void onTick(long millisUntilFinished) { }

			@Override
			public void onFinish() {
				if(done == 4)
					pd.dismiss();
				else
					timer.start();
			}
		}.start();
	}
}
