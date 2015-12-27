package ie.teamchile.smartapp.activities.ServiceUserSearch;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import ie.teamchile.smartapp.R;
import ie.teamchile.smartapp.activities.Base.BaseActivity;
import ie.teamchile.smartapp.activities.ServiceUserActivity;
import ie.teamchile.smartapp.model.ServiceUser;
import ie.teamchile.smartapp.util.Constants;
import io.realm.Realm;

public class ServiceUserSearchActivity extends BaseActivity implements ServiceUserSearchView, OnClickListener, OnItemClickListener {
    private BaseAdapter adapterListResults;
    private EditText searchName, searchHospitalNumber,
            searchDOBDay, searchDOBMonth, searchDOBYear;
    private TextView tvSearchResults;
    private List<ServiceUser> serviceUserList = new ArrayList<>();
    private ListView lvSearchResults;
    private LinearLayout llNoUserFound;
    private boolean changeActivity;
    private Realm realm;
    private ServiceUserSearchPresenter serviceUserSearchPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentForNav(R.layout.activity_service_user_search);

        realm = Realm.getInstance(getApplicationContext());

        serviceUserSearchPresenter = new ServiceUserSearchPresenterImp(this, getApplicationContext(), realm);

        initViews();

        createResultList();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        if (realm != null) {
            serviceUserSearchPresenter.onLeaveView();
            realm.close();
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

        serviceUserSearchPresenter.onLeaveView();
    }

    @Override
    public void initViews() {
        findViewById(R.id.btn_search).setOnClickListener(this);
        searchName = (EditText) findViewById(R.id.et_search_name);
        searchHospitalNumber = (EditText) findViewById(R.id.et_search_hospital_number);
        searchDOBDay = (EditText) findViewById(R.id.et_search_dob_day);
        searchDOBMonth = (EditText) findViewById(R.id.et_search_dob_month);
        searchDOBYear = (EditText) findViewById(R.id.et_search_dob_year);
        tvSearchResults = (TextView) findViewById(R.id.tv_search_results);
        llNoUserFound = (LinearLayout) findViewById(R.id.ll_no_user_found);
        lvSearchResults = (ListView) findViewById(R.id.lv_search_results);

        lvSearchResults.setOnItemClickListener(this);

        llNoUserFound.setVisibility(View.GONE);
        tvSearchResults.setVisibility(View.GONE);
    }

    private void createResultList() {
        adapterListResults = new AdapterListResultsInner();
        adapterListResults.notifyDataSetChanged();
        lvSearchResults.setAdapter(adapterListResults);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        String hospitalNumber = serviceUserList.get(position).getHospitalNumber();

        changeActivity = true;
        serviceUserSearchPresenter.searchForPatient("", hospitalNumber, "");
    }

    @Override
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

                if (!TextUtils.isEmpty(searchName.getText()) ||
                        !TextUtils.isEmpty(searchHospitalNumber.getText()) ||
                        !TextUtils.isEmpty(searchDOBDay.getText()) &&
                                !TextUtils.isEmpty(searchDOBMonth.getText()) &&
                                !TextUtils.isEmpty(searchDOBYear.getText())) {

                    tvSearchResults.setVisibility(View.VISIBLE);

                    if (!TextUtils.isEmpty(searchDOBDay.getText()) &&
                            !TextUtils.isEmpty(searchDOBMonth.getText()) &&
                            !TextUtils.isEmpty(searchDOBYear.getText())) {

                        dob = String.format(Constants.FORMAT_DOB,
                                searchDOBYear.getText(),
                                searchDOBMonth.getText(),
                                searchDOBDay.getText());
                    }

                    serviceUserSearchPresenter.searchForPatient(
                            searchName.getText().toString(),
                            searchHospitalNumber.getText().toString(),
                            dob);
                } else {
                    Toast.makeText(getApplicationContext(),
                            getString(R.string.enter_something_into_fields), Toast.LENGTH_LONG).show();
                    serviceUserList.clear();
                    adapterListResults.notifyDataSetChanged();
                }
                break;
        }
    }

    @Override
    public void gotoServiceUserActivity() {
        startActivity(new Intent(getApplicationContext(), ServiceUserActivity.class));
    }

    @Override
    public void updateServiceUserList(List<ServiceUser> serviceUsers) {
        if (serviceUsers.isEmpty()) {
            llNoUserFound.setVisibility(View.VISIBLE);
        }

        serviceUserList = serviceUsers;
        adapterListResults.notifyDataSetChanged();
    }

    @Override
    public boolean shouldChangeActivity() {
        return changeActivity;
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