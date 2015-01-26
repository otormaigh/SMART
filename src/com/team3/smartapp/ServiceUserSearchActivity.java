package com.team3.smartapp;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class ServiceUserSearchActivity extends Activity {
    private String host = "10.0.2.2";
    private String port = "5432";
    private String username = "postgres";
    private String password = "password";
    private String database = "smart";

    private EditText searchParams;
    private Button search;
    private Button searchResult1;
    private Button searchResult2;
    private Button searchResult3;
    private String enteredSearch;
    private String db_name;
    private String db_hospital_number;
    private String db_email;
    private String db_mobile_num;

    Connection c;
    Statement stmt;
    ResultSet rs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_service_user_search);

        searchParams = (EditText) findViewById(R.id.search_params);
        search = (Button) findViewById(R.id.search);
        search.setOnClickListener(new ButtonClick());
        searchResult1 = (Button) findViewById(R.id.search_result_1);
        searchResult1.setOnClickListener(new ButtonClick());
        searchResult2 = (Button) findViewById(R.id.search_result_2);
        searchResult2.setOnClickListener(new ButtonClick());
        searchResult3 = (Button) findViewById(R.id.search_result_3);
        searchResult3.setOnClickListener(new ButtonClick());

        connectToDB();
    }

    private class ButtonClick implements View.OnClickListener {
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.search:
                    enteredSearch = searchParams.getText().toString();
                    new LongOperation().execute((String[]) null);
                    break;
                case R.id.search_result_1:
                    break;
                case R.id.search_result_2:
                    break;
                case R.id.search_result_3:
                    break;
            }
        }
    }

    private void connectToDB() {
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
        private String name;
        private String hospital_number;
        private String email;
        private String mobile_num;

        protected String doInBackground(String... params) {
            try {
                c = DriverManager.getConnection(
                        "jdbc:postgresql://" + host + ":" + port + "/" + database +
                                "\"," + username + "\"," + password);
            } catch (SQLException e) {
                Log.d("MYLOG", "Connection Failed! Check output console");
                e.printStackTrace();
            }
            if (c != null) {
                Log.d("MYLOG", "Connected to Database");
                try {
                    stmt = ((Connection) c).createStatement();
                    ResultSet rs = stmt
                            .executeQuery("SELECT * FROM service_users WHERE name = '"
                                    + enteredSearch + "';");
                    if (rs.next()) {
                        name = rs.getString("name");
                        hospital_number = rs.getString("hospital_number");
                        //appointment clinic
                        //appointment date
                        //appointment time
                        //duration
                        email = rs.getString("email");
                        mobile_num = rs.getString("mobile_phone");
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
            searchResult1.setText(name);
            db_name = name;
            db_hospital_number = hospital_number;
            db_email = email;
            db_mobile_num = mobile_num;
        }

        @Override
        protected void onPreExecute() {
        }

        @Override
        protected void onProgressUpdate(Void... values) {
        }
    }
}