package ie.teamchile.smartapp.activities;

import ie.teamchile.smartapp.R;
import ie.teamchile.smartapp.utility.SmartApi;
import ie.teamchile.smartapp.model.ApiRootModel;
import ie.teamchile.smartapp.model.PostingData;
import ie.teamchile.smartapp.utility.ToastAlert;
import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.Response;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
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

		initRetrofit();
	}

	private void initRetrofit(){
		RestAdapter restAdapter = new RestAdapter.Builder()
				.setEndpoint(SmartApi.BASE_URL)
				.setLogLevel(RestAdapter.LogLevel.FULL)
				.build();

		api = restAdapter.create(SmartApi.class);
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
		PostingData login = new PostingData();
		login.postLogin(username, password);

		api.postLogin(login, new Callback<ApiRootModel>() {
			@Override
			public void success(ApiRootModel json, Response response) {
				ApiRootModel.getInstance().setLogin(json.getLogin());
				ApiRootModel.getInstance().setLoginStatus(true);
				pd.dismiss();
				intent = new Intent(LoginActivity.this, QuickMenuActivity.class);
				startActivity(intent);
			}

			@Override
			public void failure(RetrofitError error) {
				if (error.getResponse() != null) {
					ApiRootModel body = (ApiRootModel) error.getBodyAs(ApiRootModel.class);
					Log.d("Retrofit", "retro error = " + body.getError().getError());
					Toast.makeText(LoginActivity.this, body.getError().getError(), Toast.LENGTH_LONG).show();
				}
				pd.dismiss();
			}
		});
	}
	
	private void getCredentials() {
		username = usernameTextView.getText().toString();
		password = passwordTextView.getText().toString();
	}
    
    @Override
    public void onBackPressed() {
    	if(ApiRootModel.getInstance().getLoginStatus()) {
    		new ToastAlert(getBaseContext(), "Already logged in, \n  logout?", true);
    	}else { 
			finish();   		
    	}    	
    }
}
