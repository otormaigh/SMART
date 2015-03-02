
package ie.teamchile.smartapp;

import java.util.Calendar;

import utility.AppointmentSingleton;
import utility.ClinicSingleton;
import utility.ConnectivityTester;
import utility.ToastAlert;
import utility.UserSingleton;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.method.LinkMovementMethod;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import connecttodb.GetToken;

public class MainActivity extends MenuInheritActivity {
	private String username, password;
	private Button loginButton;
	private TextView usernameTextView, passwordTextView, about;
	private GetToken getToken = new GetToken();
	private Intent intent;
	ProgressDialog pd;
	private Calendar cal = Calendar.getInstance();
	private ConnectivityTester testConn = new ConnectivityTester(this);

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);	
		/*if(Login_model.getToken().equals("")){
			Log.d("MYLOG", "Token Empty");
			setContentView(R.layout.activity_main);	
		} else {
			Log.d("MYLOG", "Token not Empty");
			intent = new Intent(MainActivity.this, QuickMenuActivity.class);
			intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP |
            				Intent.FLAG_ACTIVITY_CLEAR_TASK |
            				Intent.FLAG_ACTIVITY_NEW_TASK);
			startActivity(intent);
			return;
		}*/
		
		//testConn.testTheNetworkConnection();
		//Log.d("MYLOG", "is 3g connected: " + testConn.is3GConnected());
		//Log.d("MYLOG", "is WiFi connected: " + testConn.isWifiConnected());
		
		Log.d("MYLOG", "Time is: " + cal.getTime());

		loginButton = (Button) findViewById(R.id.login);
		loginButton.setOnClickListener(new ButtonClick());
		usernameTextView = (TextView) findViewById(R.id.username);
		passwordTextView = (TextView) findViewById(R.id.password);
		
		about = (TextView) findViewById(R.id.about);
	    about.setMovementMethod(LinkMovementMethod.getInstance());
	    
	}
    private class ButtonClick implements View.OnClickListener {
		public void onClick(View v) {
			switch (v.getId()) {
                case R.id.login:              	
					//Intent intent = new Intent(MainActivity.this, QuickMenuActivity.class);
                	//startActivity(intent);
					//login.setToken("0c325638d97faf29d71f");
                	pd = new ProgressDialog(MainActivity.this);
                    pd.setMessage("logging in");
                    pd.show();
                	getCredentials();

					// Intent intent = new Intent(MainActivity.this,
					// QuickMenuActivity.class);
					// startActivity(intent);
					// login.setToken("0c325638d97faf29d71f");

					pd = new ProgressDialog(MainActivity.this);
					pd.setMessage("Logging In . . . .");
					pd.show();
				getCredentials();

				new LongOperation().execute((String[]) null);
				Log.d("MYLOG", "Button Clicked");
			}
		}
	}
	private class LongOperation extends AsyncTask<String, Void, String> {
		@Override
		protected void onPreExecute() {
			ToastAlert ta = new ToastAlert(getBaseContext(), "Loading data. . . ");
		}
		protected String doInBackground(String... params) {
			getToken.getToken(username, password);
			//getToken.getToken("team_chile", "smartappiscoming");
            
			return null;
		}
		@Override
		protected void onProgressUpdate(Void... values) {
			Log.d("MYLOG", "On progress update");
		}
		@Override
        protected void onPostExecute(String result) {
            checkCredentials();
        }
	}
	private void getCredentials() {
		username = usernameTextView.getText().toString();
		password = passwordTextView.getText().toString();
	}
    private void checkCredentials(){    	
    	if (getToken.getResponseCode().equals("201")){
    		Toast.makeText(this, "Login Successful", Toast.LENGTH_LONG).show();
    		// update Singleton
    		AppointmentSingleton.getSingletonIntance().updateLocal();
			ClinicSingleton.getSingletonIntance().updateLocal();
    		UserSingleton.getUserSingleton().setLoggedIn(true);
    		UserSingleton.getUserSingleton().setUsername(username);
    		UserSingleton.getUserSingleton().setPassword(password);
    		
    		// show the quick menu
    		intent = new Intent(MainActivity.this, QuickMenuActivity.class);
			startActivity(intent);
			pd.dismiss();
    	} else
    		Toast.makeText(this, "Login Failed", Toast.LENGTH_LONG).show();
	}
    @Override
    public void onBackPressed() {
    	if(UserSingleton.getUserSingleton().isLoggedIn()) {
    		ToastAlert ta = new ToastAlert(getBaseContext(), 
        			"  Already logged in, \n  logout?");
    	}else {    		
    	}    	
    }
}
