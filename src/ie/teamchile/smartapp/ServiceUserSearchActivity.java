package ie.teamchile.smartapp;

import ie.teamchile.smartapp.R;
import ie.teamchile.smartapp.R.id;
import ie.teamchile.smartapp.R.layout;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

import models.Login_model;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import connecttodb.AccessDBTable;

public class ServiceUserSearchActivity extends MenuInheritActivity {

	private EditText searchParams;
	private Button search, searchResult1, searchResult2,searchResult3;
	private String hospitalNumber, name, dob, email, mobileNumber, road,
            county, postCode, nextOfKinName, nextOfKinContactNumber;
	private String enteredSearch, first;
	private int arrayPos;
    private String token;

	Connection c;
	Statement stmt;
	ResultSet rs;
	JSONObject json;
	JSONArray query;
	Login_model login = new Login_model();
	AccessDBTable dbTable = new AccessDBTable();

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

        token = login.getToken();
	}
	private class ButtonClick implements View.OnClickListener {
		public void onClick(View v) {
			switch (v.getId()) {
			case R.id.search:
				Log.d("MYLOG", "Search Button Pressed");
				enteredSearch = searchParams.getText().toString();
				new LongOperation().execute((String[]) null);
				break;
			case R.id.search_result_1:
				Log.d("MYLOG", "First Result Button Pressed");
				Intent intent = new Intent(ServiceUserSearchActivity.this, ServiceUserActivity.class);
				intent.putExtra("hospital_number", hospitalNumber);
                intent.putExtra("name", name);
                intent.putExtra("dob", dob);
                intent.putExtra("email", email);
				intent.putExtra("mobile_number", mobileNumber);
				intent.putExtra("road", road);
				intent.putExtra("county", county);
				intent.putExtra("post_code", postCode);
				intent.putExtra("next_of_kin_name", nextOfKinName);
				intent.putExtra("next_of_kin_phone", nextOfKinContactNumber);
		        startActivity(intent);
				break;
			case R.id.search_result_2:
				break;
			case R.id.search_result_3:
				break;
			}
		}
	}
	public int getObjects(JSONArray obj, String key, String val) {
		for(int i = 0; i < obj.length(); i++){
			try {
				if(((JSONObject) ((JSONObject) obj.get(i)).get("personal_fields")).get(key).equals(val)){
					return i;
				}
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}
	    return 0;
	}
	public class LongOperation extends AsyncTask<String, Void, String> {
		@Override
		protected void onPreExecute() {
		}
		protected String doInBackground(String... params) {
			Log.d("MYLOG", "ServiceUserSearch DoInBackground");
			String dbQuery = dbTable.accessDB(token, "service_users");
			try {
				json = new JSONObject(dbQuery);
				query = json.getJSONArray("service_users");
				arrayPos = getObjects(query, "name", enteredSearch);

				first = (((JSONObject) ((JSONObject) query.get(arrayPos)).get("personal_fields")).get("name")).toString();
				hospitalNumber = (((JSONObject) query.get(arrayPos)).get("hospital_number")).toString();
                name = (((JSONObject) ((JSONObject) query.get(arrayPos)).get("personal_fields")).get("name")).toString();
                dob = (((JSONObject) ((JSONObject) query.get(arrayPos)).get("personal_fields")).get("dob")).toString();
				email = (((JSONObject) ((JSONObject) query.get(arrayPos)).get("personal_fields")).get("email")).toString();
				mobileNumber = (((JSONObject) ((JSONObject) query.get(arrayPos)).get("personal_fields")).get("mobile_phone")).toString();
				road = (((JSONObject) ((JSONObject) query.get(arrayPos)).get("personal_fields")).get("home_address")).toString();
				county = (((JSONObject) ((JSONObject) query.get(arrayPos)).get("personal_fields")).get("home_county")).toString();
				postCode = (((JSONObject) ((JSONObject) query.get(arrayPos)).get("personal_fields")).get("home_post_code")).toString();
				nextOfKinName = (((JSONObject) ((JSONObject) query.get(arrayPos)).get("personal_fields")).get("next_of_kin_name")).toString();
				nextOfKinContactNumber = (((JSONObject) ((JSONObject) query.get(arrayPos)).get("personal_fields")).get("next_of_kin_phone")).toString();

			} catch (JSONException e) {
				e.printStackTrace();
			}
			return null;
		}
		@Override
		protected void onProgressUpdate(Void... values) {
			Log.d("MYLOG", "On progress update");
		}
		@Override
		protected void onPostExecute(String result) {
            Log.d("MYLOG", "onPostExecute");
            searchResult1.setText(first);
            ServiceUserSearchActivity.this.hospitalNumber = hospitalNumber.toString();
            ServiceUserSearchActivity.this.name = name.toString();
            ServiceUserSearchActivity.this.dob = dob.toString();
            ServiceUserSearchActivity.this.email = email.toString();
            ServiceUserSearchActivity.this.mobileNumber = mobileNumber.toString();
            ServiceUserSearchActivity.this.road = road.toString();
            ServiceUserSearchActivity.this.county = county.toString();
            ServiceUserSearchActivity.this.postCode = postCode.toString();
            ServiceUserSearchActivity.this.nextOfKinName = nextOfKinName.toString();
            ServiceUserSearchActivity.this.nextOfKinContactNumber = nextOfKinContactNumber.toString();
		}
	}
}