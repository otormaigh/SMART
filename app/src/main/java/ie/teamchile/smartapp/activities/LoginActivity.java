package ie.teamchile.smartapp.activities;

import ie.teamchile.smartapp.R;
import ie.teamchile.smartapp.connecttodb.GetToken;
import ie.teamchile.smartapp.retrofit.SmartApi;
import ie.teamchile.smartapp.enums.CredentialsEnum;
import ie.teamchile.smartapp.retrofit.ApiRootModel;
import ie.teamchile.smartapp.retrofit.Login;
import ie.teamchile.smartapp.retrofit.LoginJson;
import ie.teamchile.smartapp.utility.ServiceProviderSingleton;
import ie.teamchile.smartapp.utility.ToastAlert;
import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.Response;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class LoginActivity extends Activity {
	private String username, password;
	private Button loginButton;
	private TextView usernameTextView, passwordTextView, about;
	private GetToken getToken = new GetToken();
	private Intent intent;
	private ProgressDialog pd;
	private SmartApi api;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);
				
		loginButton = (Button) findViewById(R.id.login);
		loginButton.setOnClickListener(new ButtonClick());
		usernameTextView = (TextView) findViewById(R.id.username);
		passwordTextView = (TextView) findViewById(R.id.password);		
		about = (TextView) findViewById(R.id.about);
	    about.setOnClickListener(new ButtonClick());
	}
	
    private class ButtonClick implements View.OnClickListener {
		public void onClick(View v) {
			switch (v.getId()) {
			case R.id.login:
				pd = new ProgressDialog(LoginActivity.this);
				pd.setMessage("Logging In");
				pd.setCanceledOnTouchOutside(false);
				pd.setCancelable(false);
				pd.show();
				getCredentials();
				//new LongOperation().execute();
				doRetrofit();
				break;
			case R.id.about:
				Intent i = new Intent(Intent.ACTION_VIEW, 
					       Uri.parse("http://www.nmh.ie/about-us.8.html"));
					startActivity(i);
				break;
			}
		}
	}

	private void doRetrofit(){
		Login login = new Login(username, password);

		RestAdapter restAdapter = new RestAdapter.Builder()
				.setEndpoint(SmartApi.BASE_URL)
				//.setLogLevel(RestAdapter.LogLevel.FULL)
				.build();

		api = restAdapter.create(SmartApi.class);

		api.doLogin(login, new Callback<ApiRootModel>() {
			@Override
			public void success(ApiRootModel json, Response response) {
				//ServiceProviderSingleton.getInstance().setToken(json.getLogin().getToken());
				ApiRootModel.getInstance().setLogin(json.getLogin());
				Log.d("Retrofit", "token = " + json.getLogin().getToken());
				pd.dismiss();
				getAppointments();
			}

			@Override
			public void failure(RetrofitError error) {
				Log.d("Retrofit", "retro failure " + error);
				pd.dismiss();
			}
		});
	}

	private void getAppointments(){
		api.getAllAppointments(
				ApiRootModel.getInstance().getLogin().getToken(),
				CredentialsEnum.API_KEY.toString(),
				new Callback<ApiRootModel>() {
					@Override
					public void success(ApiRootModel apiRootModel, Response response) {
						ApiRootModel.getInstance().setAppointments(apiRootModel.getAppointments());
						Log.d("Retrofit", "appointments finished");
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
						ApiRootModel.getInstance().setServiceOptions(things.getServiceOptions());
						Log.d("Retrofit", "service options finished");
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
						ApiRootModel.getInstance().setClinics(things.getClinics());
						Log.d("Retrofit", "service options finished");
					}

					@Override
					public void failure(RetrofitError error) {
						Log.d("Retrofit", "service options retro failure " + error);
					}
				}
		);
	}

	private class LongOperation extends AsyncTask<Void, Void, Void> {
		@Override
		protected void onPreExecute() {
			pd = new ProgressDialog(LoginActivity.this);
			pd.setMessage("Logging In");
			pd.setCanceledOnTouchOutside(false);
			pd.setCancelable(false);
			pd.show();
		}
		protected Void doInBackground(Void... params) {
			getToken.getToken(username, password);            
			return null;
		}
		@Override
		protected void onProgressUpdate(Void... values) { }
		@Override
        protected void onPostExecute(Void thing) {
            checkCredentials();
            pd.dismiss();
        }
	}
	
	private void getCredentials() {
		username = usernameTextView.getText().toString();
		password = passwordTextView.getText().toString();
	}
	
    private void checkCredentials(){    
    	if(getToken.getResponseCode() != null && !getToken.getResponseCode().isEmpty()) {
    		if (getToken.getResponseCode().equals("201")){  		
        		ServiceProviderSingleton.getInstance().setLoggedIn(true);
        		ServiceProviderSingleton.getInstance().setUsername(username);
        		ServiceProviderSingleton.getInstance().setPassword(password); 
        		intent = new Intent(LoginActivity.this, QuickMenuActivity.class); 
        		startActivity(intent);
        	} else
        		Toast.makeText(this, "Login Failed", Toast.LENGTH_LONG).show();
    	}else {
    		new ToastAlert(LoginActivity.this, "Poor Internet Activity \nPlease check your settings", true);
    	}
	}
    
    @Override
    public void onBackPressed() {
    	if(ServiceProviderSingleton.getInstance().isLoggedIn()) {
    		new ToastAlert(getBaseContext(), "Already logged in, \n  logout?", true);
    	}else { 
			finish();   		
    	}    	
    }
}
