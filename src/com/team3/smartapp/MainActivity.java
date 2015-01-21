package com.team3.smartapp;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import models.Login_model;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;


import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import android.os.CountDownTimer;

import android.util.Log;

import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends Activity {	
	private Button loginButton;
	private TextView usernameTextView;
	private TextView passwordTextView;	
	private String db_name;
	private String db_username;
	private String db_password;
	private int counter_username = 0;
	private int counter_password = 0;
	private String credentialType;
	Connection c;
	Statement stmt;
	ResultSet rs;
	Login_model login;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		loginButton = (Button)findViewById(R.id.login);
		loginButton.setOnClickListener(new ButtonClick());
		usernameTextView = (TextView)findViewById(R.id.username);
		passwordTextView = (TextView)findViewById(R.id.password);
		
		c = null;
		stmt = null;
		
		try {
			Class.forName("org.postgresql.Driver");
		}catch (ClassNotFoundException e) {
			Log.d("MYLOG", "Where is your PostgreSQL JDBC Driver? "
				+ "Include in your library path!");
			e.printStackTrace(); 
		Log.d("MYLOG", "PostgreSQL JDBC Driver Registered!");	
		}
	}	
	@Override
	protected void onStop() {
		super.onStop();
		try {
			rs.close();
			stmt.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	private class ButtonClick implements View.OnClickListener{
		public void onClick(View v) {
			switch(v.getId()){
			case R.id.login:				
				//showAlert();				
				if (counter_username == 3 || counter_password == 3){
					finish();
				} else {
					new LongOperation().execute((String[]) null);
					break;
				}
			}
		}
	}
	private class LongOperation extends AsyncTask<String, Void, String> {
        protected String doInBackground(String... params) { 
        	try {
        		c = DriverManager.getConnection(
        				"jdbc:postgresql://10.0.2.2:5432/smart", 
        				"postgres", "password");       		
    		} catch (SQLException e) {
    			Log.d("MYLOG", "Connection Failed! Check output console");
    			e.printStackTrace();
    		}
        	if (c != null) {
        		Log.d("MYLOG", "Connected to Database");
        		getCredentials();
    			try {
    				stmt = ((Connection) c).createStatement();
    				ResultSet rs = stmt.executeQuery(
    						  "SELECT * FROM service_providers WHERE username = '"
    						  + login.getUsername() + "';");
    				if(rs.next()){
    					db_name = rs.getString("name");
    		        	db_username = rs.getString("username");
    		        	db_password = rs.getString("password");
    				}else {
    					Log.d("MYLOG", "If database empty");
    					
    					switch (counter_username) {
    					case 0:
    						Toast.makeText(MainActivity.this, 
    								   	   "Incorrect username. You have 3 tries left.", 
    								   	   Toast.LENGTH_SHORT).show();
    						counter_username ++;
    						break;
    					case 1:
    						Toast.makeText(MainActivity.this, 
    									   "Incorrect username. You have 2 tries left.", 
    									   Toast.LENGTH_SHORT).show();
    						counter_username ++;
    						break;
    					case 2:
    						Toast.makeText(MainActivity.this, 
    									   "Incorrect username. You have 1 try left.", 
    									   Toast.LENGTH_SHORT).show();
    						counter_username ++;
    						break;
    					case 3:
    						finish();
    						break;
    					}
    				}
    				rs.close();
    				stmt.close();
    			} catch (SQLException e) {
    				e.printStackTrace();
    			}
    		} else {
    			Log.d("MYLOG", "Failed to make connection!");
    		}
        	return null;
        }@Override
        protected void onPostExecute(String result) {
        	login.setName(db_name);
        	login.setDb_username(db_username);
        	login.setDb_password(db_password);		
        	credentialType = "username";
			checkCredentials();
        }
        @Override
        protected void onPreExecute() {
        }
        @Override
        protected void onProgressUpdate(Void... values) {        	
        }
	}	
	private void getCredentials(){
		String username = usernameTextView.getText().toString();
		String password = passwordTextView.getText().toString();
		login.setUsername(username);
		login.setPassword(password);
	}		 
	private Object checkCredentials(){
		if (login.getPassword().equals(login.getDb_password())){
			Toast.makeText(MainActivity.this, 
						   "Welcome " + login.getName() + "\nLogin Successful", 
						   Toast.LENGTH_SHORT).show();
			Intent intent = new Intent(MainActivity.this, AppointmentTypeActivity.class);
			startActivity(intent);
			return null;
		}else {
			credentialType = "password";
			switch (counter_password) {
			case 0:
				Toast.makeText(MainActivity.this, 
						   	   "Incorrect password. You have 3 tries left.", 
						   	   Toast.LENGTH_SHORT).show();
				counter_password ++;
				break;
			case 1:
				Toast.makeText(MainActivity.this, 
							   "Incorrect password. You have 2 tries left.", 
							   Toast.LENGTH_SHORT).show();
				counter_password ++;
				break;
			case 2:
				Toast.makeText(MainActivity.this, 
							   "Incorrect password. You have 1 try left.", 
							   Toast.LENGTH_SHORT).show();
				counter_password ++;
				break;
			case 3:
				finish();
				break;
			}
		}
		return null;
	}
	@Override
	public void finish() {
		Toast.makeText(MainActivity.this, 
					   "Incorrect " + credentialType + ". You have no tries left. \n"
					   + "Closing app now.", 
					   Toast.LENGTH_SHORT).show();
		super.finish();
	}
	private void showAlert(){
		final AlertDialog alertDialog = new AlertDialog.Builder(this).create();  
		alertDialog.setTitle("Alert");  
		alertDialog.setMessage("00:03");
		alertDialog.show(); 

		new CountDownTimer(10000, 1000) {
		    @Override
		    public void onTick(long millisUntilFinished) {
		       alertDialog.setMessage("00:"+ (millisUntilFinished/1000));
		    }
		    @Override
		    public void onFinish() {
		        //View info.setVisibility(View.GONE);
		    }
		}.start();
	}
}