package com.team3.smartapp;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import models.Login_model;
import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.SubMenu;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends Activity {
    private String host = "10.15.1.80";
    private String port = "5432";
    private String username = "postgres";
    private String password = "postgres";
    private String database = "smart_team_3";

	private Button loginButton;
	private TextView usernameTextView;
	private TextView passwordTextView;
	private String db_name;
	private String db_username;
	private String db_password;
	private int counter_password = 0;
	Connection c;
	Statement stmt;
	ResultSet rs;
	Login_model login = new Login_model();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		loginButton = (Button) findViewById(R.id.login);
		loginButton.setOnClickListener(new ButtonClick());
		usernameTextView = (TextView) findViewById(R.id.username);
		passwordTextView = (TextView) findViewById(R.id.password);

        c = null;
        stmt = null;

        try {
            Class.forName("org.postgresql.Driver");
        } catch (ClassNotFoundException e) {
            Log.d("MYLOG", "Where is your PostgreSQL JDBC Driver? "
                + "Include in your library path!");
            e.printStackTrace();
            Log.d("MYLOG", "PostgreSQL JDBC Driver Registered!");
        }
	}

	private class ButtonClick implements View.OnClickListener {
		public void onClick(View v) {
			switch (v.getId()) {
                case R.id.login:
                //Intent intent = new Intent(MainActivity.this, ServiceUserActivity.class);
               // startActivity(intent);

				getCredentials();
				new LongOperation().execute((String[]) null);
                Log.d("MYLOG", "Button Clicked");
			}
		}
	}

    private void connectToDB(){
        c = null;
        stmt = null;

        try {
            Class.forName("org.postgresql.Driver");
        } catch (ClassNotFoundException e) {
            Log.d("MYLOG", "Where is your PostgreSQL JDBC Driver? "
                + "Include in your library path!");
            e.printStackTrace();
            Log.d("MYLOG", "PostgreSQL JDBC Driver Registered!");
        }
    }
	private class LongOperation extends AsyncTask<String, Void, String> {
		protected String doInBackground(String... params) {
			try {
				c = DriverManager.getConnection("jdbc:postgresql://" + host + ":" + port + "/" + database, username, password);
                        /*"jdbc:postgresql://10.0.2.2:5432/smart", "postgres", "password");*/
			} catch (SQLException e) {
				Log.d("MYLOG", "Connection Failed! Check output console");
				e.printStackTrace();
			}
			if (c != null) {
				Log.d("MYLOG", "Connected to Database");
				getCredentials();
				try {
					stmt = ((Connection) c).createStatement();
					rs = stmt.executeQuery("SELECT * FROM service_providers WHERE username = '"
									     + login.getUsername() + "';");
					if (rs.next()) {
						db_name = rs.getString("name");
						db_username = rs.getString("username");
						db_password = rs.getString("password");
					} else {
						Log.d("MYLOG", "If database empty");
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
		}

        @Override
        protected void onPostExecute(String result) {
            login.setDb_name(db_name);
            login.setDb_username(db_username);
            login.setDb_password(db_password);
            checkCredentials();
        }

        @Override
        protected void onPreExecute() {
        }

        @Override
        protected void onProgressUpdate(Void... values) {
        }
	}

	private void getCredentials() {
		String username = usernameTextView.getText().toString();
		String password = passwordTextView.getText().toString();
		login.setUsername(username);
		login.setPassword(password);
	}

	private Object checkCredentials() {
    //private void checkCredentials(){
		if (login.getPassword().equals(login.getDb_password())) {
			Toast.makeText(MainActivity.this,
                           "Welcome " + login.getDb_name() + "\nLogin Successful",
					        Toast.LENGTH_SHORT).show();
			Intent intent = new Intent(MainActivity.this, ClinicActivity.class);
			startActivity(intent);
			return null;
		} else {
			switch (counter_password) {
			case 0:
				Toast.makeText(MainActivity.this,
						      "Incorrect password. You have 3 tries left.",
						       Toast.LENGTH_SHORT).show();
				counter_password++;
				break;
			case 1:
				Toast.makeText(MainActivity.this,
						      "Incorrect password. You have 2 tries left.",
						       Toast.LENGTH_SHORT).show();
				counter_password++;
				break;
			case 2:
				Toast.makeText(MainActivity.this,
						      "Incorrect password. You have 1 try left.",
						       Toast.LENGTH_SHORT).show();
				counter_password++;
				break;
			case 3:
				Toast.makeText(MainActivity.this,
						      "Incorrect password. You have no tries left.",
						       Toast.LENGTH_SHORT).show();
				break;
			}
		}
		return null;
	}
    private static final int GROUP_ID = 0;
	private static final int ITEM_ID = 1;

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.menu_service_user_search, menu);

		SubMenu sub = menu.addSubMenu(GROUP_ID, ITEM_ID, 0, "Options Submenu");
		sub.add(GROUP_ID, 10, 0, "Item 1");
		sub.add(GROUP_ID, 15, 1, "Item 2");
		sub.add(GROUP_ID, 15, 2, "Item 3");

		sub.setGroupCheckable(GROUP_ID, true, false);
		sub.setGroupCheckable(GROUP_ID, true, true);

		return super.onCreateOptionsMenu(menu);
	}
		
	{

	}
	   @Override
	   protected void onStart() {
	      super.onStart();
	      Log.d("msg", "The onStart() event");
	   }

	   @Override
	   protected void onResume() {
	      super.onResume();
	      Log.d("msg", "The onResume() event");
	   }

	   @Override
	   protected void onPause() {
	      super.onPause();
	      Log.d("msg", "The onPause() event");
	      
	      WindowManager.LayoutParams lp = getWindow().getAttributes();

	  	lp.screenBrightness = 5 / 100.0f;

	  	getWindow().setAttributes(lp);
	   }

	   @Override
	   protected void onStop() {
	      super.onStop();
	      Log.d("msg", "The onStop() event");
	   }

	   @Override
	   public void onDestroy() {
	      super.onDestroy();
	      Log.d("msg", "The onDestroy() event");
	   }

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.user_contact:
			System.out.println("User contact was selected!");
			Intent intent = new Intent(MainActivity.this, SubMenu.class);
			startActivity(intent);
			break;
}
		return super.onOptionsItemSelected(item);
	}
}
