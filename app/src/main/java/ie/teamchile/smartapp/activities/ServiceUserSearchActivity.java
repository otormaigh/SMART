package ie.teamchile.smartapp.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import ie.teamchile.smartapp.R;
import ie.teamchile.smartapp.model.ApiRootModel;
import ie.teamchile.smartapp.model.ServiceUser;
import ie.teamchile.smartapp.utility.SmartApi;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class ServiceUserSearchActivity extends BaseActivity {
	private EditText searchName, searchHospitalNumber, 
					 searchDOBDay, searchDOBMonth, searchDOBYear;
	private Button search;
	private TextView tvNoUserFound, tvSearchResults;
	private ArrayList<String> searchResults = new ArrayList<>();
	private Intent intent;
	private ListView list;
	private ArrayAdapter<String> adapter;
	private List<String> hospitalNumberList = new ArrayList<>();
	private LinearLayout llNoUserFound;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentForNav(R.layout.activity_service_user_search);

		searchName = (EditText) findViewById(R.id.search_name);
		searchHospitalNumber = (EditText) findViewById(R.id.search_hospital_number);
		searchDOBDay = (EditText) findViewById(R.id.search_dob_day);
		searchDOBMonth = (EditText) findViewById(R.id.search_dob_month);
		searchDOBYear = (EditText) findViewById(R.id.search_dob_year);
		//tvNoUserFound = (TextView) findViewById(R.id.tv_no_user_found);
		tvSearchResults = (TextView) findViewById(R.id.tv_search_results);
		llNoUserFound = (LinearLayout) findViewById(R.id.ll_no_user_found);

		search = (Button) findViewById(R.id.search);
		search.setOnClickListener(new ButtonClick());

		list = (ListView) findViewById(R.id.search_results_list);
		list.setOnItemClickListener(new onItemListener());

		llNoUserFound.setVisibility(View.GONE);
		//tvNoUserFound.setVisibility(View.GONE);
		tvSearchResults.setVisibility(View.GONE);
	}

	private void createResultList(ArrayList<String> searchResults) {
		adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, searchResults);
		adapter.notifyDataSetChanged();
		list.setAdapter(adapter);
		adapter.notifyDataSetChanged();
	}

	private class onItemListener implements OnItemClickListener {
		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
			searchForPatient("", hospitalNumberList.get(position), "");
			intent = new Intent(ServiceUserSearchActivity.this, ServiceUserActivity.class);
		}
	}

	private class ButtonClick implements View.OnClickListener {
		public void onClick(View v) {
			switch (v.getId()) {

			case R.id.search:
				Log.d("MYLOG", "Search Button Pressed");

				llNoUserFound.setVisibility(View.GONE);
				//tvNoUserFound.setVisibility(View.GONE);
				tvSearchResults.setVisibility(View.GONE);
				
				InputMethodManager inputManager = (InputMethodManager)
                getSystemService(Context.INPUT_METHOD_SERVICE); 
				inputManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(),
                InputMethodManager.HIDE_NOT_ALWAYS);
				
				String dob = "";
				
				if(searchName.getText().toString().length() > 0 ||
				   searchHospitalNumber.getText().toString().length() > 0 ||
				   searchDOBDay.getText().toString().length() > 0 &&
				   searchDOBMonth.getText().toString().length() > 0 &&
				   searchDOBYear.getText().toString().length() > 0){
					
					if(searchDOBDay.getText().toString().length() > 0 &&
				       searchDOBMonth.getText().toString().length() > 0 &&
				       searchDOBYear.getText().toString().length() > 0){
						
						dob = searchDOBYear.getText() + "-" +
							  searchDOBMonth.getText() + "-" + 
							  searchDOBDay.getText();
					}

					searchForPatient(
							searchName.getText().toString(),
							searchHospitalNumber.getText().toString(),
							dob);
				}else{
					Toast.makeText(getApplicationContext(),
							"Please enter something in the search fields", Toast.LENGTH_SHORT).show();
				}				
				break;
			}
		}
	}

	private void searchForPatient(String name, String hospitalNumber, String dob){
		showProgressDialog(ServiceUserSearchActivity.this, "Fetching Information");
		api.getServiceUserByNameDobHospitalNum(
			name,
			hospitalNumber,
			dob,
			ApiRootModel.getInstance().getLogin().getToken(),
			SmartApi.API_KEY,
			new Callback<ApiRootModel>() {
				@Override
				public void success(ApiRootModel apiRootModel, Response response) {
					if (apiRootModel.getServiceUsers().size() > 0) {
						ApiRootModel.getInstance().setServiceUsers(apiRootModel.getServiceUsers());
						ApiRootModel.getInstance().setPregnancies(apiRootModel.getPregnancies());
						ApiRootModel.getInstance().setBabies(apiRootModel.getBabies());
						if (intent != null)
							startActivity(intent);
						else {
							searchResults.clear();
							hospitalNumberList.clear();
							for (int i = 0; i < apiRootModel.getServiceUsers().size(); i++) {
								ServiceUser serviceUser = ApiRootModel.getInstance().getServiceUsers().get(i);
								String name = serviceUser.getPersonalFields().getName();
								String hospitalNumber = serviceUser.getHospitalNumber();
								String dob = serviceUser.getPersonalFields().getDob();

								searchResults.add(name + " - " + hospitalNumber + " - " + dob);
								hospitalNumberList.add(hospitalNumber);
							}
							createResultList(searchResults);
							adapter.notifyDataSetChanged();
						}
					} else {
						llNoUserFound.setVisibility(View.VISIBLE);
						//tvNoUserFound.setVisibility(View.VISIBLE);
						if(adapter != null){
							adapter.clear();
						}
					}
					pd.dismiss();
				}

				@Override
				public void failure(RetrofitError error) {
					//tvNoUserFound.setVisibility(View.VISIBLE);
					pd.dismiss();
				}
			}
		);
	}
}