package ie.teamchile.smartapp.activities;

import ie.teamchile.smartapp.R;
import ie.teamchile.smartapp.connecttodb.AccessDBTable;
import ie.teamchile.smartapp.utility.ServiceUserSingleton;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

public class ServiceUserSearchActivity extends MenuInheritActivity {
	private EditText searchName, searchHospitalNumber, searchDOB;
	private Button search;
	private ArrayList<String> searchResults = new ArrayList<String>();
	private JSONObject json;
	private AccessDBTable dbTable = new AccessDBTable();
	private ProgressDialog pd;
	private Intent intent;
	private ListView list;
	private ArrayAdapter<String> adapter;
	private List<String> hospitalNumberList = new ArrayList<String>();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_service_user_search);

		searchName = (EditText) findViewById(R.id.search_name);
		searchHospitalNumber = (EditText) findViewById(R.id.search_hospital_number);
		searchDOB = (EditText) findViewById(R.id.search_dob);

		search = (Button) findViewById(R.id.search);
		search.setOnClickListener(new ButtonClick());

		list = (ListView) findViewById(R.id.search_results_list);
		list.setOnItemClickListener(new onItemListener());
	}

	private void createResultList(ArrayList<String> searchResults) {
		adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, searchResults);
		adapter.notifyDataSetChanged();
		list.setAdapter(adapter);
		adapter.notifyDataSetChanged();
	}

	private class onItemListener implements OnItemClickListener {
		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
			new LongOperation(ServiceUserSearchActivity.this)
				     .execute("service_users?hospital_number=" + hospitalNumberList.get(position));
			intent = new Intent(ServiceUserSearchActivity.this, ServiceUserActivity.class);
		}
	}

	private class ButtonClick implements View.OnClickListener {
		public void onClick(View v) {
			switch (v.getId()) {

			case R.id.search:
				Log.d("MYLOG", "Search Button Pressed");
				
				String asyncQuery = "";
				
				///service_users?name=nore%20saturn&hospital_number=T14234388"
				
				if(searchName.getText().toString().length() > 0 ||
				   searchHospitalNumber.getText().toString().length() > 0 ||
				   searchDOB.getText().toString().length() > 0){
					
					asyncQuery = "?name=" + searchName.getText() + 
								 "&hospital_number=" + searchHospitalNumber.getText() + 
								 "&dob=" + searchDOB.getText();
					
					asyncQuery = asyncQuery.replaceAll("\\s","%20");
					
					Log.d("bugs", "asyncQuery: " + asyncQuery);
					new LongOperation(ServiceUserSearchActivity.this).execute("service_users" + asyncQuery);
				}else{
					Toast.makeText(getApplicationContext(),
							"Please enter something in the search fields", Toast.LENGTH_SHORT).show();
				}				
				break;
			}
		}
	}

	private class LongOperation extends AsyncTask<String, Void, JSONObject> {
		private Context context;

		public LongOperation(Context context) {
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
			Log.d("bugs", "Result from on post " + result);

			/*
			 * if result from database is empty (check if null) toast to say no
			 * query found if not empty do getSinglton.getName set this to
			 * button text
			 */
			try {
				if (intent != null) {
					startActivity(intent);
				} else {
					searchResults.clear();
					hospitalNumberList.clear();
					if (result.getJSONArray("service_users").length() != 0) {
						for (int i = 0; i < result.getJSONArray("service_users").length(); i++) {
							ServiceUserSingleton.getInstance().setPatientInfo(result);
							
							String name = ServiceUserSingleton.getInstance().getUserName().get(i);
							String hospitalNumber = ServiceUserSingleton.getInstance().getUserHospitalNumber().get(i);
							String dob = ServiceUserSingleton.getInstance().getUserDOB().get(i);
							
							searchResults.add(name + " - " + hospitalNumber + " - " + dob);
							hospitalNumberList.add(hospitalNumber);
							Log.d("bugs", "searchResults: " + searchResults);
						}
						createResultList(searchResults);	
						adapter.notifyDataSetChanged();
					} else {
						Toast.makeText(getApplicationContext(), "No search results found", Toast.LENGTH_SHORT).show();
					}
				}
			} catch (JSONException e) {
				e.printStackTrace();
			}
			pd.dismiss();
		}
	}
}