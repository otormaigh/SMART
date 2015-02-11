package ie.teamchile.smartapp;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

import models.Login_model;
import connecttodb.GetToken;
import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.method.LinkMovementMethod;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends Activity {

	private String token, username, password;
	private Button loginButton;
	private TextView usernameTextView, passwordTextView, about;
	private Connection c = null;
	private Statement stmt = null;
	private ResultSet rs = null;
	private Login_model login = new Login_model();
	private GetToken getToken = new GetToken();
	private Intent intent;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

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
                Intent intent = new Intent(MainActivity.this, QuickMenuActivity.class);
                startActivity(intent);
				login.setToken("0c325638d97faf29d71f");

                //getCredentials();
				//new LongOperation().execute((String[]) null);
				Log.d("MYLOG", "Button Clicked");
			}
		}
	}

	public class LongOperation extends AsyncTask<String, Void, String> {
		@Override
		protected void onPreExecute() {
		}
		protected String doInBackground(String... params) {
			token = getToken.getToken(Login_model.getUsername(), Login_model.getPassword());
			//token = getToken.getToken("team_chile", "smartappiscoming");
			login.setToken(token);
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
    		Toast.makeText(this, "Login Successful", Toast.LENGTH_LONG).show();
    		intent = new Intent(MainActivity.this, QuickMenuActivity.class);
			startActivity(intent);
    	} else
    		Toast.makeText(this, "Login Failed", Toast.LENGTH_LONG).show();
	}
}