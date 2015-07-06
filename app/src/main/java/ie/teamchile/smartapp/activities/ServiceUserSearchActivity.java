package ie.teamchile.smartapp.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import ie.teamchile.smartapp.R;
import ie.teamchile.smartapp.model.BaseModel;
import ie.teamchile.smartapp.model.FeedingHistory;
import ie.teamchile.smartapp.model.HearingHistory;
import ie.teamchile.smartapp.model.NbstHistory;
import ie.teamchile.smartapp.model.ServiceUser;
import ie.teamchile.smartapp.model.VitKHistory;
import ie.teamchile.smartapp.util.AdapterListResults;
import ie.teamchile.smartapp.util.NotKeys;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class ServiceUserSearchActivity extends BaseActivity {
    private EditText searchName, searchHospitalNumber,
            searchDOBDay, searchDOBMonth, searchDOBYear;
    private Button search;
    private TextView tvSearchResults;
    private ArrayList<String> searchResults = new ArrayList<>();
    private ArrayList<String> listName = new ArrayList<>();
    private ArrayList<String> listDob = new ArrayList<>();
    private ArrayList<String> listHospitalNumber = new ArrayList<>();
    private Intent intent;
    private ListView lvSearchResults;
    private List<String> hospitalNumberList = new ArrayList<>();
    private LinearLayout llNoUserFound;
    private Boolean changeActivity;
    private static BaseAdapter adapterListResults;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentForNav(R.layout.activity_service_user_search);

        searchName = (EditText) findViewById(R.id.et_search_name);
        searchHospitalNumber = (EditText) findViewById(R.id.et_search_hospital_number);
        searchDOBDay = (EditText) findViewById(R.id.et_search_dob_day);
        searchDOBMonth = (EditText) findViewById(R.id.et_search_dob_month);
        searchDOBYear = (EditText) findViewById(R.id.et_search_dob_year);
        tvSearchResults = (TextView) findViewById(R.id.tv_search_results);
        llNoUserFound = (LinearLayout) findViewById(R.id.ll_no_user_found);

        search = (Button) findViewById(R.id.btn_search);
        search.setOnClickListener(new ButtonClick());

        lvSearchResults = (ListView) findViewById(R.id.lv_search_results);
        lvSearchResults.setOnItemClickListener(new onItemListener());

        llNoUserFound.setVisibility(View.GONE);
        //tvNoUserFound.setVisibility(View.GONE);
        tvSearchResults.setVisibility(View.GONE);
    }

    private void createResultList() {
        //adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, searchResults);
        adapterListResults = new AdapterListResultsInner(this, listName, listDob, listHospitalNumber);
        adapterListResults.notifyDataSetChanged();
        lvSearchResults.setAdapter(adapterListResults);
        //adapterListResults.notifyDataSetChanged();
    }

    private void getHistories() {
        getRecentBabyPosition();
        getRecentBabyId();
        getRecentPregnancy();
        api.getVitKHistories(
                bId,
                BaseModel.getInstance().getLogin().getToken(),
                NotKeys.API_KEY,
                new Callback<BaseModel>() {
                    @Override
                    public void success(BaseModel baseModel, Response response) {
                        Collections.sort(baseModel.getVitKHistories(), new Comparator<VitKHistory>() {

                            @Override
                            public int compare(VitKHistory a, VitKHistory b) {
                                int valA;
                                int valB;

                                valA = a.getId();
                                valB = b.getId();

                                return -((Integer) valA).compareTo(valB);
                            }
                        });
                        BaseModel.getInstance().setVitKHistories(baseModel.getVitKHistories());
                        Log.d("retro", "vit k history done");
                    }

                    @Override
                    public void failure(RetrofitError error) {
                        Log.d("retro", "vit k history failure = " + error);
                    }
                }
        );
        api.getHearingHistories(
                bId,
                BaseModel.getInstance().getLogin().getToken(),
                NotKeys.API_KEY,
                new Callback<BaseModel>() {
                    @Override
                    public void success(BaseModel baseModel, Response response) {
                        Collections.sort(baseModel.getHearingHistories(), new Comparator<HearingHistory>() {

                            @Override
                            public int compare(HearingHistory a, HearingHistory b) {
                                int valA;
                                int valB;

                                valA = a.getId();
                                valB = b.getId();

                                return -((Integer) valA).compareTo(valB);
                            }
                        });
                        BaseModel.getInstance().setHearingHistories(baseModel.getHearingHistories());
                        Log.d("retro", "hearing history done");
                    }

                    @Override
                    public void failure(RetrofitError error) {
                        Log.d("retro", "hearing history failure = " + error);
                    }
                }
        );
        api.getNbstHistories(
                bId,
                BaseModel.getInstance().getLogin().getToken(),
                NotKeys.API_KEY,
                new Callback<BaseModel>() {
                    @Override
                    public void success(BaseModel baseModel, Response response) {
                        Collections.sort(baseModel.getNbstHistories(), new Comparator<NbstHistory>() {

                            @Override
                            public int compare(NbstHistory a, NbstHistory b) {
                                int valA;
                                int valB;

                                valA = a.getId();
                                valB = b.getId();

                                return -((Integer) valA).compareTo(valB);
                            }
                        });
                        BaseModel.getInstance().setNbstHistories(baseModel.getNbstHistories());
                        Log.d("retro", "nbst history done");
                    }

                    @Override
                    public void failure(RetrofitError error) {
                        Log.d("retro", "nbst history failure = " + error);
                    }
                }
        );
        api.getFeedingHistoriesByPregId(
                BaseModel.getInstance().getPregnancies().get(p).getId(),
                BaseModel.getInstance().getLogin().getToken(),
                NotKeys.API_KEY,
                new Callback<BaseModel>() {
                    @Override
                    public void success(BaseModel baseModel, Response response) {
                        Collections.sort(baseModel.getFeedingHistories(), new Comparator<FeedingHistory>() {

                            @Override
                            public int compare(FeedingHistory a, FeedingHistory b) {
                                int valA;
                                int valB;

                                valA = a.getId();
                                valB = b.getId();

                                return -((Integer) valA).compareTo(valB);
                            }
                        });
                        BaseModel.getInstance().setFeedingHistories(baseModel.getFeedingHistories());
                        Log.d("retro", "feeding history done");
                    }

                    @Override
                    public void failure(RetrofitError error) {
                        Log.d("retro", "feeding history failure = " + error);
                    }
                }
        );
    }

    private void searchForPatient(String name, String hospitalNumber, String dob) {
        if(name.equals("."))
            name = " ";
        else
            name = name.trim();
        listName.clear();
        listDob.clear();
        listHospitalNumber.clear();
        Log.d("retro", "ServiceUserSearchActivity user search success");
        showProgressDialog(ServiceUserSearchActivity.this, "Fetching Information");

        api.getServiceUserByNameDobHospitalNum(
                name,
                hospitalNumber.trim(),
                dob.trim(),
                BaseModel.getInstance().getLogin().getToken(),
                NotKeys.API_KEY,
                new Callback<BaseModel>() {
                    @Override
                    public void success(BaseModel baseModel, Response response) {
                        if (baseModel.getServiceUsers().size() > 0) {
                            BaseModel.getInstance().setServiceUsers(baseModel.getServiceUsers());
                            BaseModel.getInstance().setPregnancies(baseModel.getPregnancies());
                            BaseModel.getInstance().setBabies(baseModel.getBabies());
                            BaseModel.getInstance().setAntiDHistories(baseModel.getAntiDHistories());
                            if (changeActivity) {
                                startActivity(intent);
                                getHistories();
                            } else {
                                searchResults.clear();
                                hospitalNumberList.clear();
                                for (int i = 0; i < baseModel.getServiceUsers().size(); i++) {
                                    ServiceUser serviceUser = BaseModel.getInstance().getServiceUsers().get(i);
                                    String name = serviceUser.getPersonalFields().getName();
                                    String dob = serviceUser.getPersonalFields().getDob();
                                    String hospitalNumber = serviceUser.getHospitalNumber();

                                    listName.add(name);
                                    listDob.add(dob);
                                    listHospitalNumber.add(hospitalNumber);

                                    searchResults.add(name + " - " + hospitalNumber + " - " + dob);
                                    hospitalNumberList.add(hospitalNumber);
                                }
                                createResultList();
                                adapterListResults.notifyDataSetChanged();
                            }
                        } else {
                            llNoUserFound.setVisibility(View.VISIBLE);
                            if (adapterListResults != null) {
                                lvSearchResults.setAdapter(null);
                                adapterListResults.notifyDataSetChanged();
                            }
                        }
                        pd.dismiss();
                    }

                    @Override
                    public void failure(RetrofitError error) {
                        Log.d("retro", "ServiceUserSearchActivity user search failure = " + error);
                        checkRetroError(error, ServiceUserSearchActivity.this);
                        llNoUserFound.setVisibility(View.VISIBLE);
                        pd.dismiss();
                    }
                }
        );
    }

    private class onItemListener implements OnItemClickListener {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            changeActivity = true;
            searchForPatient("", hospitalNumberList.get(position), "");
            intent = new Intent(ServiceUserSearchActivity.this, ServiceUserActivity.class);
        }
    }

    private class ButtonClick implements View.OnClickListener {
        public void onClick(View v) {
            switch (v.getId()) {

                case R.id.btn_search:
                    changeActivity = false;
                    llNoUserFound.setVisibility(View.GONE);
                    tvSearchResults.setVisibility(View.GONE);

                    InputMethodManager inputManager = (InputMethodManager)
                            getSystemService(Context.INPUT_METHOD_SERVICE);
                    inputManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(),
                            InputMethodManager.HIDE_NOT_ALWAYS);

                    String dob = "";

                    if (searchName.getText().toString().length() > 0 ||
                            searchHospitalNumber.getText().toString().length() > 0 ||
                            searchDOBDay.getText().toString().length() > 0 &&
                                    searchDOBMonth.getText().toString().length() > 0 &&
                                    searchDOBYear.getText().toString().length() > 0) {

                        if (searchDOBDay.getText().toString().length() > 0 &&
                                searchDOBMonth.getText().toString().length() > 0 &&
                                searchDOBYear.getText().toString().length() > 0) {

                            dob = searchDOBYear.getText() + "-" +
                                    searchDOBMonth.getText() + "-" +
                                    searchDOBDay.getText();
                        }

                        searchForPatient(
                                searchName.getText().toString(),
                                searchHospitalNumber.getText().toString(),
                                dob);
                    } else {
                        Toast.makeText(getApplicationContext(),
                                "Please enter something in the search fields", Toast.LENGTH_LONG).show();
                        lvSearchResults.setAdapter(null);
                    }
                    break;
            }
        }
    }

    public class AdapterListResultsInner extends BaseAdapter {
        private Context context;
        private List<String> resultName;
        private List<String> resultDob;
        private List<String> resultHospitalNumber;
        private LayoutInflater layoutInflater;

        public AdapterListResultsInner(
                Context context,
                List<String> resultName,
                List<String> resultDob,
                List<String> resultHospitalNumber){
            this.context = context;
            this.resultName = resultName;
            this.resultDob = resultDob;
            this.resultHospitalNumber = resultHospitalNumber;
            layoutInflater = LayoutInflater.from(context);
        }
        @Override
        public int getCount() {
            return resultName.size();
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            convertView = layoutInflater.inflate(R.layout.list_layout_search_results, null);
            TextView tvName = (TextView) convertView.findViewById(R.id.tv_results_name);
            TextView tvDob = (TextView) convertView.findViewById(R.id.tv_results_dob);
            TextView tvHospitalNumber = (TextView) convertView.findViewById(R.id.tv_results_hospital_number);

            tvName.setText(resultName.get(position));
            tvDob.setText(resultDob.get(position));
            tvHospitalNumber.setText(resultHospitalNumber.get(position));
            return convertView;
        }

        @Override
        public void notifyDataSetChanged() {
            super.notifyDataSetChanged();
        }
    }

}