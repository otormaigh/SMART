package ie.teamchile.smartapp.activities;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import ie.teamchile.smartapp.BuildConfig;
import ie.teamchile.smartapp.R;
import ie.teamchile.smartapp.api.SmartApiClient;
import ie.teamchile.smartapp.model.Baby;
import ie.teamchile.smartapp.model.BaseModel;
import ie.teamchile.smartapp.model.Pregnancy;
import ie.teamchile.smartapp.model.ServiceUser;
import ie.teamchile.smartapp.util.CustomDialogs;
import io.realm.Realm;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;
import timber.log.Timber;

public class ServiceUserSearchActivity extends BaseActivity {
    private BaseAdapter adapterListResults;
    private EditText searchName, searchHospitalNumber,
            searchDOBDay, searchDOBMonth, searchDOBYear;
    private Button search;
    private TextView tvSearchResults;
    private List<ServiceUser> serviceUserList = new ArrayList<>();
    private ListView lvSearchResults;
    private LinearLayout llNoUserFound;
    private Boolean changeActivity;
    private ProgressDialog pd;
    private Realm realm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentForNav(R.layout.activity_service_user_search);

        realm = Realm.getInstance(this);

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

        createResultList();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        if (realm != null) {
            realm.beginTransaction();
            realm.allObjects(ServiceUser.class).clear();
            realm.allObjects(Baby.class).clear();
            realm.allObjects(Pregnancy.class).clear();
            realm.commitTransaction();
            realm.close();
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

        realm.beginTransaction();
        realm.allObjects(ServiceUser.class).clear();
        realm.allObjects(Baby.class).clear();
        realm.allObjects(Pregnancy.class).clear();
        realm.commitTransaction();
    }

    private void createResultList() {
        adapterListResults = new AdapterListResultsInner();
        adapterListResults.notifyDataSetChanged();
        lvSearchResults.setAdapter(adapterListResults);
        //lvSearchResults.setEmptyView(llNoUserFound);
    }

    private void getHistories(int pregnancyId, int babyId) {
        SmartApiClient.getAuthorizedApiClient(this).getVitKHistories(
                babyId,
                new Callback<BaseModel>() {
                    @Override
                    public void success(BaseModel baseModel, Response response) {
                        Timber.d("vit k history done");
                        realm.beginTransaction();
                        realm.copyToRealmOrUpdate(baseModel.getVitKHistories());
                        realm.commitTransaction();
                    }

                    @Override
                    public void failure(RetrofitError error) {
                        Timber.d("vit k history failure = " + error);
                    }
                }
        );
        SmartApiClient.getAuthorizedApiClient(this).getHearingHistories(
                babyId,
                new Callback<BaseModel>() {
                    @Override
                    public void success(BaseModel baseModel, Response response) {
                        Timber.d("hearing history done");
                        realm.beginTransaction();
                        realm.copyToRealmOrUpdate(baseModel.getHearingHistories());
                        realm.commitTransaction();
                    }

                    @Override
                    public void failure(RetrofitError error) {
                        Timber.d("hearing history failure = " + error);
                    }
                }
        );
        SmartApiClient.getAuthorizedApiClient(this).getNbstHistories(
                babyId,
                new Callback<BaseModel>() {
                    @Override
                    public void success(BaseModel baseModel, Response response) {
                        realm.beginTransaction();
                        realm.copyToRealmOrUpdate(baseModel.getNbstHistories());
                        realm.commitTransaction();
                    }

                    @Override
                    public void failure(RetrofitError error) {
                        Timber.d("nbst history failure = " + error);
                    }
                }
        );
        SmartApiClient.getAuthorizedApiClient(this).getFeedingHistoriesByPregId(
                pregnancyId,
                new Callback<BaseModel>() {
                    @Override
                    public void success(BaseModel baseModel, Response response) {
                        Timber.d("feeding history done");
                        realm.beginTransaction();
                        realm.copyToRealmOrUpdate(baseModel.getFeedingHistories());
                        realm.commitTransaction();
                    }

                    @Override
                    public void failure(RetrofitError error) {
                        Timber.d("feeding history failure = " + error);
                    }
                }
        );
    }

    private void searchForPatient(String name, String hospitalNumber, String dob) {
        if (name.equals(".") && BuildConfig.DEBUG)
            name = " ";
        else
            name = name.trim();
        pd = new CustomDialogs().showProgressDialog(
                ServiceUserSearchActivity.this,
                "Fetching Information");

        SmartApiClient.getAuthorizedApiClient(this).getServiceUserByNameDobHospitalNum(
                name,
                hospitalNumber.trim(),
                dob.trim(),
                new Callback<BaseModel>() {
                    @Override
                    public void success(BaseModel baseModel, Response response) {

                        Timber.wtf("size = " + baseModel.getServiceUsers().size());

                        if (baseModel.getServiceUsers().size() != 0) {
                            if (changeActivity) {
                                realm.beginTransaction();
                                realm.copyToRealmOrUpdate(baseModel.getServiceUsers());
                                realm.copyToRealmOrUpdate(baseModel.getPregnancies());
                                realm.copyToRealmOrUpdate(baseModel.getBabies());
                                realm.copyToRealmOrUpdate(baseModel.getAntiDHistories());
                                realm.commitTransaction();
                                startActivity(
                                        new Intent(ServiceUserSearchActivity.this,
                                                ServiceUserActivity.class));
                                getHistories(
                                        getRecentPregnancy(realm.where(Pregnancy.class).findAll()),
                                        getRecentBaby(realm.where(Baby.class).findAll()));
                            } else {
                                serviceUserList = baseModel.getServiceUsers();
                                adapterListResults.notifyDataSetChanged();
                            }
                        } else {
                            llNoUserFound.setVisibility(View.VISIBLE);
                            adapterListResults.notifyDataSetChanged();
                        }
                        pd.dismiss();
                    }

                    @Override
                    public void failure(RetrofitError error) {
                        Timber.d("ServiceUserSearchActivity user search failure = " + error);
                        adapterListResults.notifyDataSetChanged();
                        llNoUserFound.setVisibility(View.VISIBLE);
                        pd.dismiss();
                    }
                }
        );
    }

    private class onItemListener implements OnItemClickListener {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            String hospitalNumber = serviceUserList.get(position).getHospitalNumber();

            changeActivity = true;
            searchForPatient("", hospitalNumber, "");
        }
    }

    private class ButtonClick implements View.OnClickListener {
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.btn_search:
                    serviceUserList = new ArrayList<>();
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
                        tvSearchResults.setVisibility(View.VISIBLE);

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
                        serviceUserList.clear();
                        adapterListResults.notifyDataSetChanged();
                    }
                    break;
            }
        }
    }

    public class AdapterListResultsInner extends BaseAdapter {

        @Override
        public int getCount() {
            return serviceUserList.size();
        }

        @Override
        public ServiceUser getItem(int position) {
            return serviceUserList.get(position);
        }

        @Override
        public long getItemId(int position) {
            return serviceUserList.get(position).hashCode();
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            String name = getItem(position).getPersonalFields().getName();
            String dob = getItem(position).getPersonalFields().getDob();
            String hospitalNumber = getItem(position).getHospitalNumber();

            Context context = ServiceUserSearchActivity.this;
            ViewHolder viewHolder;
            if (convertView == null) {
                LayoutInflater inflater = LayoutInflater.from(context);
                convertView = inflater.inflate(R.layout.list_layout_search_results, parent, false);
                viewHolder = new ViewHolder(convertView);
                convertView.setTag(viewHolder);
            } else
                viewHolder = (ViewHolder) convertView.getTag();

            viewHolder.tvName.setText(name);
            viewHolder.tvDob.setText(dob);
            viewHolder.tvHospitalNumber.setText(hospitalNumber);
            return convertView;
        }

        @Override
        public void notifyDataSetChanged() {
            super.notifyDataSetChanged();
        }
    }

    protected class ViewHolder {
        TextView tvName;
        TextView tvDob;
        TextView tvHospitalNumber;

        public ViewHolder(View view) {
            tvName = (TextView) view.findViewById(R.id.tv_results_name);
            tvDob = (TextView) view.findViewById(R.id.tv_results_dob);
            tvHospitalNumber = (TextView) view.findViewById(R.id.tv_results_hospital_number);
        }
    }
}