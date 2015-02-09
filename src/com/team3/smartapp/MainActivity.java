package com.team3.smartapp;

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

	private Button loginButton;
	private TextView usernameTextView;
	private TextView passwordTextView;
	private TextView about;
	Connection c;
	Statement stmt;
	ResultSet rs;
	Login_model login = new Login_model();
	GetToken getToken = new GetToken();

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

        c = null;
        stmt = null;
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
		protected String doInBackground(String... params) {
			String token = getToken.getToken(login.getUsername(), login.getPassword());
			//String token = getToken.getAuthKey("team_chile", "smartappiscoming");
			login.setToken(token);
            Log.d("MYLOG", "Token: " + token);
            Log.d("MYLOG", "Get Token: " + login.getToken());
			return null;
		}
		@Override
        protected void onPostExecute(String result) {
            checkCredentials();
        }
        @Override
        protected void onPreExecute() {
        }
        @Override
        protected void onProgressUpdate(Void... values) {
        	Log.d("MYLOG", "On progress update");
        }
	}
	private void getCredentials() {
		String username = usernameTextView.getText().toString();
		String password = passwordTextView.getText().toString();
		login.setUsername(username);
		login.setPassword(password);
	}

    private void checkCredentials(){    	
    	if (getToken.getResponseCode().equals("201")){
    		Toast.makeText(this, "Login Successful", Toast.LENGTH_LONG).show();
    		Intent intent = new Intent(MainActivity.this, QuickMenuActivity.class);
			startActivity(intent);
    	} else
    		Toast.makeText(this, "Login Failed", Toast.LENGTH_LONG).show();
	}
}