package ie.teamchile.smartapp;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import utility.ServiceUserSingleton;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import connecttodb.AccessDBTable;

public class ServiceUserSearchActivity extends MenuInheritActivity {

	private EditText searchParams;
	private Button search, searchResult1, searchResult2, searchResult3;
	private String enteredSearch;
	Connection c;
	Statement stmt;
	ResultSet rs;
	JSONObject json;
	JSONArray query, query2, query3;
	AccessDBTable dbTable = new AccessDBTable();
	private ProgressDialog pd;
	private Intent intent;
	private String response;
	private JSONObject jsonNew;
	private AccessDBTable db = new AccessDBTable();
	//private String name1="";
	//private String name;
    //private String option1 = "service_users?name=";
   // private String option2 = "service_users?hospital_number=";
    //private String option3 = "service_users?dob=";
   

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
	}
	
	private class ButtonClick implements View.OnClickListener {
		public void onClick(View v) {
			switch (v.getId()) {
			case R.id.search:
				Log.d("MYLOG", "Search Button Pressed");
				enteredSearch = searchParams.getText().toString();
				new LongOperation(ServiceUserSearchActivity.this).execute("service_users?name=" + enteredSearch);
	        break;
			case R.id.search_result_1:
				Log.d("MYLOG", "First Result Button Pressed");
				Intent intent = new Intent(ServiceUserSearchActivity.this, ServiceUserActivity.class);
		        startActivity(intent);
				break;
			case R.id.search_result_2:
				break;
			case R.id.search_result_3:
				break;
			}
		}
	}

	private class LongOperation extends AsyncTask<String, Void, JSONObject>{
		private Context context;
		
		LongOperation(Context context){
			this.context = context;
		}
		@Override
		protected void onPreExecute() {
			pd = new ProgressDialog(context);
            pd.setMessage("Fetching Information");
            pd.show();
		}
		protected JSONObject doInBackground(String... params) {
			Log.d("MYLOG", "ServiceUserSearch DoInBackground");
            json = dbTable.accessDB(params[0]);
			return json;
		}
		@Override
		protected void onProgressUpdate(Void... values) {
			Log.d("MYLOG", "On progress update");
		}
		@Override
		protected void onPostExecute(JSONObject result) {
            Log.d("MYLOG", "onPostExecute");
            Log.d("bugs", "Result from on post " +result);
            ServiceUserSingleton.getInstance().setPatientInfo(result);
            String name = ServiceUserSingleton.getInstance().getBabyName().get(0);
            if(name!=null){
            searchResult1.setText(name);
            }else{
            	searchResult1.setText("No results found");
            	Toast.makeText(getApplicationContext(), "No search results found", Toast.LENGTH_SHORT).show();
            }
    		if(searchResult1.equals("No results found")){
    			searchResult1.setOnClickListener(null);
    		}
    		else
    			searchResult1.setOnClickListener(new ButtonClick());

            
            pd.dismiss();
           }
 
		} 		
}
