package ie.teamchile.smartapp.activities;

import ie.teamchile.smartapp.R;
import ie.teamchile.smartapp.connecttodb.AccessDBTable;
import ie.teamchile.smartapp.enums.CredentialsEnum;
import ie.teamchile.smartapp.retrofit.ApiRootModel;
import ie.teamchile.smartapp.retrofit.Clinic;
import ie.teamchile.smartapp.retrofit.ServiceOption;
import ie.teamchile.smartapp.utility.ServiceProviderSingleton;
import ie.teamchile.smartapp.utility.ToastAlert;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

import android.app.ProgressDialog;
import android.content.ComponentCallbacks2;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import java.util.HashMap;
import java.util.Map;

public class QuickMenuActivity extends MenuInheritActivity {
    private boolean isViewVisible;
    private ProgressDialog pd;
	private AccessDBTable db = new AccessDBTable();
	private Button patientSearch, bookAppointment, calendar, todaysAppointments;
	private int done = 0;
	private CountDownTimer timer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
		setContentForNav(R.layout.activity_quick_menu);

        patientSearch = (Button) findViewById(R.id.patientSearch);
        patientSearch.setOnClickListener(new ButtonClick());
        bookAppointment = (Button) findViewById(R.id.bookAppointment);
        bookAppointment.setOnClickListener(new ButtonClick());
        calendar = (Button) findViewById(R.id.calendar);
        calendar.setOnClickListener(new ButtonClick());
        todaysAppointments = (Button) findViewById(R.id.todays_appointments);
        //todaysAppointments.setOnClickListener(new ButtonClick());

		isViewVisible = true;
		updateData();
    }
    
	@Override
	protected void onNewIntent(Intent intent) {
	    super.onNewIntent(intent);
	    setIntent(intent);
	}
	
    private class ButtonClick implements View.OnClickListener {
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.patientSearch:
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
                /*case R.id.todays_appointments:
                    Intent intentToday = new Intent(QuickMenuActivity.this, TodayAppointmentActivity.class);
                    startActivity(intentToday);
                    break;*/
            }
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
		}else 
			isViewVisible = true;
	}

	private void updateData(){
		pd = new ProgressDialog(QuickMenuActivity.this);
		pd.setMessage("Updating Information");
		pd.setCanceledOnTouchOutside(false);
		pd.setCancelable(false);
		pd.show();

		api.getAllAppointments(
				ApiRootModel.getInstance().getLogin().getToken(),
				CredentialsEnum.API_KEY.toString(),
				new Callback<ApiRootModel>() {
					@Override
					public void success(ApiRootModel apiRootModel, Response response) {
						ApiRootModel.getInstance().setAppointments(apiRootModel.getAppointments());
						Log.d("Retrofit", "appointments finished");
						done++;
						Log.d("Retrofit", "done = " + done);
					}

					@Override
					public void failure(RetrofitError error) {
						Log.d("Retrofit", "appointments retro failure " + error);
					}
				}
		);

		api.getAllServiceOptions(
				ApiRootModel.getInstance().getLogin().getToken(),
				CredentialsEnum.API_KEY.toString(),
				new Callback<ApiRootModel>() {
					@Override
					public void success(ApiRootModel things, Response response) {
						Map<Integer, ServiceOption> serviceOptionMap = new HashMap<>();
						ApiRootModel.getInstance().setServiceOptions(things.getServiceOptions());
						for(int i = 0; i < things.getServiceOptions().size(); i++){
							serviceOptionMap.put(things.getServiceOptions().get(i).getId(),
									things.getServiceOptions().get(i));
						}
						ApiRootModel.getInstance().setServiceOptionsMap(serviceOptionMap);
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
						ApiRootModel.getInstance().setClinicsMap(clinicMap);
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
				if(done == 3)
					pd.dismiss();
				else
					timer.start();
			}
		}.start();
	}

	private void setDataToMap(ApiRootModel apiRootModel){

	}
}
