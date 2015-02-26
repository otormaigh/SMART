
package ie.teamchile.smartapp;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Calendar;

import models.Appointments_model;
import models.Clinics_model;
import models.Login_model;
import utility.ConnectivityTester;
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
import connecttodb.Logout;


public class MainActivity extends MenuInheritActivity {
	private String token, username, password;
	private Button loginButton;
	private TextView usernameTextView, passwordTextView, about;
	private Connection c = null;
	private Statement stmt = null;
	private ResultSet rs = null;
	private Login_model login = new Login_model();
    private Logout logout = new Logout();
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
		}
		protected String doInBackground(String... params) {
			token = getToken.getToken(Login_model.getUsername(), Login_model.getPassword());
			//token = getToken.getToken("team_chile", "smartappiscoming");
            Log.d("MYLOG", "Token: " + token);
            Log.d("MYLOG", "Get Token: " + Login_model.getToken());
            
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
		login.setUsername(username);
		login.setPassword(password);
	}
    private void checkCredentials(){    	
    	if (getToken.getResponseCode().equals("201")){
    		Appointments_model.getSingletonIntance().updateLocal();
			Clinics_model.getSingletonIntance().updateLocal();
			
    		Toast.makeText(this, "Login Successful", Toast.LENGTH_LONG).show();
    		intent = new Intent(MainActivity.this, QuickMenuActivity.class);
			startActivity(intent);
			pd.dismiss();
    	} else
    		Toast.makeText(this, "Login Failed", Toast.LENGTH_LONG).show();
	}
    @Override
    public void onBackPressed() {
    	Toast.makeText(this, "There's no going back ye hear?!!!", Toast.LENGTH_LONG).show();
    }
}

