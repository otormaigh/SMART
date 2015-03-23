package ie.teamchile.smartapp.activities;

import ie.teamchile.smartapp.R;
import ie.teamchile.smartapp.connecttodb.AccessDBTable;
import ie.teamchile.smartapp.utility.ServiceUserSingleton;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

public class ServiceUserSearchActivity extends MenuInheritActivity {
	private EditText searchParams;
	private Spinner searchOption;
	private Button search;
	private String enteredSearch;
	private ArrayList<String> searchResults = new ArrayList<String>();
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
	private ListView list;
	private ArrayAdapter<String> adapter;
	private ArrayAdapter<CharSequence> searchAdapter;
	private List<String> hospitalNumberList = new ArrayList<String>();
	private String searchUrl = "";
	private String name;
	private String option1 = "service_users?name=";
	private String option2 = "service_users?hospital_number=";
	private String option3 = "service_users?dob=";
	private InputMethodManager inputMgr;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_service_user_search);

		searchOption = (Spinner) findViewById(R.id.search_option);
		searchParams = (EditText) findViewById(R.id.search_params);

		search = (Button) findViewById(R.id.search);
		search.setOnClickListener(new ButtonClick());
		searchAdapter = ArrayAdapter.createFromResource(this, R.array.patient_search_options, R.layout.spinner_layout);
		searchAdapter.setDropDownViewResource(R.layout.spinner_layout);
		searchOption.setOnItemSelectedListener(new MySpinnerOnItemSelectedListener());
		searchOption.setAdapter(searchAdapter);

		list = (ListView) findViewById(R.id.search_results_list);
		list.setOnItemClickListener(new onItemListener());
		
		inputMgr = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
	}

	private void createResultList(ArrayList<String> searchResults) {
		adapter = new ArrayAdapter<String>(this,
				android.R.layout.simple_list_item_1, searchResults);
		adapter.notifyDataSetChanged();
		list.setAdapter(adapter);
		adapter.notifyDataSetChanged();
	}

	public void onBackPressed() {
		Intent goToQuickMenu = new Intent(ServiceUserSearchActivity.this,
				QuickMenuActivity.class);
		startActivity(goToQuickMenu);
	}

	private class onItemListener implements OnItemClickListener {
		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position,
				long id) {
			new LongOperation(ServiceUserSearchActivity.this)
					.execute("service_users?hospital_number="
							+ hospitalNumberList.get(position));
			intent = new Intent(ServiceUserSearchActivity.this,
					ServiceUserActivity.class);
		}
	}

	private class MySpinnerOnItemSelectedListener implements
			AdapterView.OnItemSelectedListener {
		@Override
		public void onItemSelected(AdapterView<?> parent, View view,
				int position, long id) {
			switch (parent.getId()) {
			case R.id.search_option:
				switch (position) {
				case 0:
					searchParams.setEnabled(false);
					getWindow().setSoftInputMode(
							WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
					search.setEnabled(false);
					// search.setClickable(true);
					break;
				case 1:
					searchParams.setEnabled(true);
					getWindow().setSoftInputMode(

					WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
					search.setEnabled(true);
					searchUrl = option1;
					break;
				case 2:
					searchParams.setEnabled(true);
					getWindow()
							.setSoftInputMode(
									WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);

					search.setEnabled(true);
					searchUrl = option2;
					break;
				case 3:
					searchParams.setEnabled(true);
					getWindow()
							.setSoftInputMode(
									WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);

					search.setEnabled(true);
					searchUrl = option3;
					break;

				}
			}
		}

		@Override
		public void onNothingSelected(AdapterView<?> parent) {
		}
	}

	private class ButtonClick implements View.OnClickListener {
		public void onClick(View v) {
			switch (v.getId()) {

			case R.id.search:
				Log.d("MYLOG", "Search Button Pressed");
				inputMgr.hideSoftInputFromWindow(searchParams.getWindowToken(), 0);
				list.setAdapter(null);
				enteredSearch = searchParams.getText().toString();
				new LongOperation(ServiceUserSearchActivity.this)
						.execute(searchUrl + enteredSearch);
				break;
			}
		}
	}

	private class LongOperation extends AsyncTask<String, Void, JSONObject> {
		private Context context;

		LongOperation(Context context) {
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
			ServiceUserSingleton.getInstance().setPatientInfo(result);
			Log.d("bugs", "fom single: "
					+ ServiceUserSingleton.getInstance().getUserName());

			/*
			 * if result from database is empty (chcek if null) toast to say no
			 * query found if not empty do getSinglton.getName set this to
			 * button text
			 */

			if (intent != null) {
				startActivity(intent);
			} else {
				searchResults.clear();
				searchAdapter.notifyDataSetChanged();
				hospitalNumberList.clear();
				if (ServiceUserSingleton.getInstance().getUserName().size() != 0) {
					for (int i = 0; i < ServiceUserSingleton.getInstance()
							.getUserName().size(); i++) {
						String name = ServiceUserSingleton.getInstance()
								.getUserName().get(i);
						String hospitalNumber = ServiceUserSingleton
								.getInstance().getUserHospitalNumber().get(i);
						searchResults.add(name + " - " + hospitalNumber);
						hospitalNumberList.add(hospitalNumber);
						Log.d("bugs", "searchResults: " + searchResults);
					}
					createResultList(searchResults);
					InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
					imm.hideSoftInputFromWindow(search.getWindowToken(), 0);

					searchParams.setText(null);
					adapter.notifyDataSetChanged();
				} else {
					// searchResults.add("No results found");
					searchParams.setText(null);
					Toast.makeText(getApplicationContext(),
							"No search results found", Toast.LENGTH_SHORT)
							.show();
					// createResultList(searchResults);
				}
			}
			pd.dismiss();
		}
	}
}