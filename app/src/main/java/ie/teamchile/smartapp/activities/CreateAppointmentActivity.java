package ie.teamchile.smartapp.activities;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import ie.teamchile.smartapp.R;
import ie.teamchile.smartapp.api.SmartApiClient;
import ie.teamchile.smartapp.model.BaseModel;
import ie.teamchile.smartapp.model.Clinic;
import ie.teamchile.smartapp.model.Login;
import ie.teamchile.smartapp.model.PostingData;
import ie.teamchile.smartapp.model.ServiceOption;
import ie.teamchile.smartapp.model.ServiceUser;
import ie.teamchile.smartapp.util.AdapterListResults;
import ie.teamchile.smartapp.util.AdapterSpinner;
import ie.teamchile.smartapp.util.Constants;
import ie.teamchile.smartapp.util.CustomDialogs;
import ie.teamchile.smartapp.util.SharedPrefs;
import io.realm.Realm;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;
import timber.log.Timber;

public class CreateAppointmentActivity extends BaseActivity implements OnClickListener, OnItemSelectedListener, OnItemClickListener {
    private ArrayAdapter<String> visitPriorityAdapter, returnTypeAdapter;
    private String userName, apptDate, time, priority, visitType, clinicName,
            hospitalNumber, email, sms, address;
    private int userID;
    private Calendar c, myCalendar;
    private Date daySelected;
    private AppointmentCalendarActivity passOptions = new AppointmentCalendarActivity();
    private SharedPreferences prefs;
    private AlertDialog.Builder alertDialog;
    private AlertDialog ad;
    private int clinicID, serviceOptionId;
    private EditText etUserName;
    private TextView tvTime, tvTimeTitle, tvDate, tvClinic, tvReturnTitle, tvPriorityTitle;
    private Spinner visitReturnTypeSpinner, visitPrioritySpinner;
    private List<ServiceUser> serviceUserList = new ArrayList<>();
    private String returnType;
    private List<String> listName = new ArrayList<>();
    private List<String> listDob = new ArrayList<>();
    private List<String> listHospitalNumber = new ArrayList<>();
    private ProgressDialog pd;
    private Realm realm;
    private Intent intentClinic;
    private Intent intentHome;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentForNav(R.layout.activity_create_appointment);

        realm = Realm.getInstance(getApplicationContext());

        intentClinic = new Intent(getApplicationContext(), AppointmentCalendarActivity.class);
        intentHome = new Intent(getApplicationContext(), HomeVisitAppointmentActivity.class);

        c = Calendar.getInstance();
        myCalendar = Calendar.getInstance();

        etUserName = (EditText) findViewById(R.id.et_service_user);
        tvTime = (TextView) findViewById(R.id.tv_visit_time);
        tvTimeTitle = (TextView) findViewById(R.id.tv_visit_time_title);
        tvDate = (TextView) findViewById(R.id.tv_visit_date);
        tvClinic = (TextView) findViewById(R.id.tv_visit_clinic);
        tvReturnTitle = (TextView) findViewById(R.id.tv_return_type_title);
        tvPriorityTitle = (TextView) findViewById(R.id.tv_prioirty_title);

        etUserName.setText(null);

        findViewById(R.id.btn_confirm_appointment).setOnClickListener(this);
        findViewById(R.id.btn_user_search).setOnClickListener(this);

        visitReturnTypeSpinner = (Spinner) findViewById(R.id.spnr_visit_return_type);
        visitReturnTypeSpinner.setOnItemSelectedListener(this);

        visitPrioritySpinner = (Spinner) findViewById(R.id.spnr_visit_priority);
        visitPrioritySpinner.setOnItemSelectedListener(this);

        getSharedPrefs();

        setReturnTypeSpinner();
        setPrioritySpinner();
        checkIfEditEmpty();
        checkDirectionOfIntent();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        if (realm != null)
            realm.close();
    }

    private void clinicAppt() {
        clinicID = Integer.parseInt(getIntent().getStringExtra(Constants.ARGS_CLINIC_ID));
        daySelected = AppointmentCalendarActivity.daySelected;
        clinicName = realm.where(Clinic.class).equalTo(Constants.REALM_ID, clinicID).findFirst().getName();
        time = getIntent().getStringExtra(Constants.ARGS_TIME);
        tvTime.setText(time);
        visitPrioritySpinner.setSelection(1);

        myCalendar.setTime(daySelected);
        tvDate.setText(dfDateMonthNameYear.format(daySelected));

        tvClinic.setText(clinicName);
    }

    private void homeVisitAppt() {
        daySelected = HomeVisitAppointmentActivity.daySelected;
        serviceOptionId = Integer.parseInt(getIntent().getStringExtra(Constants.ARGS_SERVICE_OPTION_ID));
        clinicName = realm.where(ServiceOption.class).equalTo(Constants.REALM_ID, serviceOptionId).findFirst().getName();
        tvTime.setVisibility(View.GONE);
        tvTimeTitle.setVisibility(View.GONE);
        visitReturnTypeSpinner.setVisibility(View.GONE);
        visitPrioritySpinner.setVisibility(View.GONE);
        tvPriorityTitle.setVisibility(View.GONE);
        tvReturnTitle.setVisibility(View.GONE);

        priority = Constants.ARGS_HOME_VISIT;
        returnType = Constants.ARGS_RETURNING;

        myCalendar.setTime(daySelected);
        tvDate.setText(dfDateMonthNameYear.format(daySelected));

        tvClinic.setText(clinicName);
    }

    @Override
    protected void onResume() {
        super.onResume();
        checkDirectionOfIntent();
        checkIfEditEmpty();
    }

    private void checkDirectionOfIntent() {
        String intentOrigin = getIntent().getStringExtra(Constants.ARGS_FROM);
        if (intentOrigin.equals(Constants.ARGS_CLINIC_APPOINTMENT)) {
            clinicAppt();
        } else if (intentOrigin.equals(Constants.ARGS_HOME_VISIT)) {
            homeVisitAppt();
        }
    }

    private boolean checkIfEditEmpty() {
        if (TextUtils.isEmpty(String.valueOf(userID)) ||
                TextUtils.isEmpty(etUserName.getText())) {
            etUserName.setError(getString(R.string.error_field_empty));
            return true;
        } else
            return false;
    }

    private boolean checkIfOkToGo() {
        return userID != 0 &&
                visitPrioritySpinner.getSelectedItemPosition() != 0 &&
                visitReturnTypeSpinner.getSelectedItemPosition() != 0;
    }

    private void getSharedPrefs() {
        prefs = getSharedPreferences(getString(R.string.app_name), MODE_PRIVATE);

        if (prefs != null && prefs.getBoolean(Constants.REUSE, false)) {
            userName = prefs.getString(Constants.NAME, null);
            userID = Integer.parseInt(prefs.getString(Constants.ID, ""));
            visitType = prefs.getString(Constants.VISIT_TYPE, null);
            hospitalNumber = prefs.getString(Constants.HOSPITAL_NUMBER, "");
            email = prefs.getString(Constants.EMAIL, "");
            sms = prefs.getString(Constants.MOBILE, "");

            etUserName.setText(userName);
        }
    }

    private void setPrioritySpinner() {
        visitPriorityAdapter = new AdapterSpinner(getApplicationContext(),
                R.array.visit_priority_list,
                R.layout.spinner_layout,
                R.id.tv_spinner_item);
        visitPriorityAdapter.setDropDownViewResource(R.layout.spinner_layout);
        visitPrioritySpinner.setAdapter(visitPriorityAdapter);
    }

    private void setReturnTypeSpinner() {
        returnTypeAdapter = new AdapterSpinner(getApplicationContext(),
                R.array.return_type_list,
                R.layout.spinner_layout,
                R.id.tv_spinner_item);
        returnTypeAdapter.setDropDownViewResource(R.layout.spinner_layout);
        visitReturnTypeSpinner.setAdapter(returnTypeAdapter);
    }

    private void showEmptyFieldDialog() {
        pd = new CustomDialogs().showProgressDialog(
                getApplicationContext(),
                getString(R.string.error_cannot_proceed_fields_empty));
        new CountDownTimer(2000, 1000) {
            @Override
            public void onFinish() {
                pd.dismiss();
            }

            @Override
            public void onTick(long millisUntilFinished) {
            }
        }.start();
    }

    private void searchPatient(String serviceUserName) {
        listName.clear();
        listDob.clear();
        listHospitalNumber.clear();

        SmartApiClient.getAuthorizedApiClient(getApplicationContext()).getServiceUserByName(
                serviceUserName,
                new Callback<BaseModel>() {
                    @Override
                    public void success(BaseModel baseModel, Response response) {
                        Timber.d("searchPatient success");
                        String name, hospitalNumber, dob;
                        if (baseModel.getServiceUsers().size() != 0) {
                            realm.beginTransaction();
                            realm.copyToRealmOrUpdate(baseModel.getServiceUsers());
                            realm.copyToRealmOrUpdate(baseModel.getBabies());
                            realm.copyToRealmOrUpdate(baseModel.getPregnancies());
                            realm.commitTransaction();

                            int size = baseModel.getServiceUsers().size();
                            for (int i = 0; i < size; i++) {
                                ServiceUser serviceUserItem = baseModel.getServiceUsers().get(i);
                                serviceUserList.add(serviceUserItem);
                                name = serviceUserItem.getPersonalFields().getName();
                                dob = serviceUserItem.getPersonalFields().getDob();
                                hospitalNumber = serviceUserItem.getHospitalNumber();

                                listName.add(name);
                                listDob.add(dob);
                                listHospitalNumber.add(hospitalNumber);
                            }
                            pd.dismiss();
                            userSearchDialog(getString(R.string.search_results));
                        } else {
                            pd.dismiss();
                            new CustomDialogs().showWarningDialog(
                                    getApplicationContext(),
                                    getString(R.string.no_search_results_found));
                        }
                    }

                    @Override
                    public void failure(RetrofitError error) {
                        Timber.d("retro failure error = " + error);
                        if (!error.getKind().equals(RetrofitError.Kind.NETWORK)) {
                            switch (error.getResponse().getStatus()) {
                                case 500:
                                    new CustomDialogs().showWarningDialog(
                                            getApplicationContext(),
                                            getString(R.string.no_search_results_found));
                                    break;
                            }
                        }
                        pd.dismiss();
                    }
                }
        );
    }

    private void userSearchDialog(String title) {
        LayoutInflater inflater = getLayoutInflater();
        alertDialog = new AlertDialog.Builder(getApplicationContext());
        View convertView = inflater.inflate(R.layout.dialog_list_only, null);
        ListView list = (ListView) convertView.findViewById(R.id.lv_dialog);

        list.setOnItemClickListener(this);

        TextView tvDialogTitle = (TextView) convertView.findViewById(R.id.tv_dialog_title);
        ImageView ivExit = (ImageView) convertView.findViewById(R.id.iv_exit_dialog);
        ivExit.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                ad.dismiss();
            }
        });

        alertDialog.setView(convertView);
        tvDialogTitle.setText(title);
        BaseAdapter baseAdapter = new AdapterListResults(
                getApplicationContext(),
                listName,
                listDob,
                listHospitalNumber);
        list.setAdapter(baseAdapter);
        ad = alertDialog.show();
    }

    private void postOrAnte() {
        visitType = Constants.ARGS_POST_NATAL;        //TODO: this need to be changed
    }

    private void makeAlertDialog() {
        String dateWords = dfDateMonthNameYear.format(daySelected);
        String dateDay = dfDayShort.format(daySelected);

        LayoutInflater inflater = getLayoutInflater();
        alertDialog = new AlertDialog.Builder(CreateAppointmentActivity.this);
        View convertView = inflater.inflate(R.layout.dialog_confirm_appointment, null);
        TextView tvConfirmUserName = (TextView) convertView.findViewById(R.id.tv_confirm_name);
        TextView tvConfirmLocation = (TextView) convertView.findViewById(R.id.tv_confirm_location);
        TextView tvConfirmDateTime = (TextView) convertView.findViewById(R.id.tv_confirm_time);
        TextView tvConfirmEmailTo = (TextView) convertView.findViewById(R.id.tv_confirm_email);
        TextView tvConfirmSmsTo = (TextView) convertView.findViewById(R.id.tv_confirm_sms);

        tvConfirmUserName.setText(String.format(Constants.FORMAT_TV_CONFIRM_USERNAME, userName, hospitalNumber));

        switch (priority) {
            case Constants.ARGS_HOME_VISIT:
                tvConfirmLocation.setText(address);
                tvConfirmDateTime.setText(String.format(Constants.FORMAT_TV_CONFIRM_DATE_TIME_1, dateDay, dateWords));
                break;
            case Constants.ARGS_SCHEDULED:
                tvConfirmLocation.setText(clinicName);
                tvConfirmDateTime.setText(String.format(Constants.FORMAT_TV_CONFIRM_DATE_TIME_2, time, dateWords));
                break;
        }

        tvConfirmEmailTo.setText(email);
        tvConfirmSmsTo.setText(sms);

        convertView.findViewById(R.id.btn_confirm_yes)
                .setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        pd = new CustomDialogs().showProgressDialog(
                                getApplicationContext(),
                                getString(R.string.booking_appointment));
                        postAppointment();
                    }
                });
        convertView.findViewById(R.id.btn_confirm_no)
                .setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        ad.cancel();
                    }
                });
        convertView.findViewById(R.id.iv_exit_dialog)
                .setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        ad.cancel();
                    }
                });

        alertDialog.setView(convertView);
        ad = alertDialog.show();
    }

    private void postAppointment() {
        final PostingData appointment = new PostingData();
        switch (priority) {
            case Constants.ARGS_HOME_VISIT:
                appointment.postAppointment(realm.where(Login.class).findFirst().getId(), apptDate, userID, priority, visitType, returnType, serviceOptionId);
                break;
            case Constants.ARGS_SCHEDULED:
                int clinicID = Integer.parseInt(getIntent().getStringExtra(Constants.ARGS_CLINIC_ID));
                appointment.postAppointment(realm.where(Login.class).findFirst().getId(), apptDate, time, userID, clinicID, priority, visitType, returnType);
                break;
        }

        SmartApiClient.getAuthorizedApiClient(getApplicationContext()).postAppointment(
                appointment,
                new Callback<BaseModel>() {
                    @Override
                    public void success(BaseModel baseModel, Response response) {
                        Timber.d("postAppointment success");

                        realm.beginTransaction();
                        realm.copyToRealmOrUpdate(baseModel.getAppointment());
                        realm.commitTransaction();

                        if (returnType.equals(Constants.ARGS_NEW)) {
                            getAppointmentById(baseModel.getAppointment().getId());
                        } else {
                            if (ad.isShowing())
                                ad.cancel();

                            if (pd.isShowing())
                                pd.dismiss();

                            switch (priority) {
                                case Constants.ARGS_HOME_VISIT:
                                    startActivity(intentHome);
                                    break;
                                case Constants.ARGS_SCHEDULED:
                                    startActivity(intentClinic);
                                    break;
                                case Constants.ARGS_DROP_IN:
                                    startActivity(intentClinic);
                                    break;
                            }
                        }
                    }

                    @Override
                    public void failure(RetrofitError error) {
                        Timber.d("retro failure error = " + error);
                        checkRetroError(error, getApplicationContext());
                        if (error.getKind() != RetrofitError.Kind.NETWORK) {
                            if (error.getResponse().getStatus() == 422) {
                                BaseModel body = (BaseModel) error.getBodyAs(BaseModel.class);
                                Toast.makeText(getApplicationContext(),
                                        body.getError().getAppointmentTaken(), Toast.LENGTH_LONG).show();
                            }
                        } else {
                            c = Calendar.getInstance();
                            String time = dfTimeWSec.format(c.getTime());
                            String prefsTag = String.format(Constants.FORMAT_PREFS_APPT_POST, time);
                            SharedPrefs prefsUstil = new SharedPrefs();
                            prefsUstil.setJsonPrefs(getApplicationContext(), appointment, prefsTag);
                            Toast.makeText(getApplicationContext(),
                                    getString(R.string.error_no_internet_booking_appointment),
                                    Toast.LENGTH_LONG).show();
                        }
                        if (ad.isShowing())
                            ad.cancel();
                        if (pd.isShowing())
                            pd.dismiss();
                    }
                }
        );
    }

    private void getAppointmentById(int apptId) {
        SmartApiClient.getAuthorizedApiClient(getApplicationContext()).getAppointmentById(
                apptId + 1,
                new Callback<BaseModel>() {
                    @Override
                    public void success(BaseModel baseModel, Response response) {
                        Timber.d("getAppointmentById success");
                        realm.beginTransaction();
                        realm.copyToRealmOrUpdate(baseModel.getAppointment());
                        realm.commitTransaction();

                        if (ad.isShowing())
                            ad.cancel();

                        if (pd.isShowing())
                            pd.dismiss();

                        switch (priority) {
                            case Constants.ARGS_HOME_VISIT:
                                startActivity(intentHome);
                                break;
                            case Constants.ARGS_SCHEDULED:
                                startActivity(intentClinic);
                                break;
                            case Constants.ARGS_DROP_IN:
                                startActivity(intentClinic);
                                break;
                        }
                    }

                    @Override
                    public void failure(RetrofitError error) {
                        Timber.d("getAppointmentById failure = " + error);
                        pd.dismiss();

                        if (ad.isShowing())
                            ad.cancel();

                        if (pd.isShowing())
                            pd.dismiss();
                    }
                }
        );
    }

    private void hideKeyboard() {
        InputMethodManager inputManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        inputManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
    }

    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_confirm_appointment:
                apptDate = dfDateOnly.format(myCalendar.getTime());
                passOptions.setDaySelected(myCalendar.getTime());

                switch (priority) {
                    case Constants.ARGS_HOME_VISIT:
                        if (!checkIfEditEmpty())
                            makeAlertDialog();
                        else
                            showEmptyFieldDialog();
                        break;
                    case Constants.ARGS_SCHEDULED:
                        if (checkIfOkToGo())
                            makeAlertDialog();
                        else
                            showEmptyFieldDialog();
                        break;
                }
                break;
            case R.id.btn_user_search:
                if (!checkIfEditEmpty()) {
                    hideKeyboard();
                    userID = 0;
                    userName = etUserName.getText().toString();
                    checkIfEditEmpty();
                    pd = new CustomDialogs().showProgressDialog(
                            getApplicationContext(),
                            getString(R.string.fetching_information));
                    searchPatient(userName);
                }
                break;
        }
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        switch (parent.getId()) {
            case R.id.spnr_visit_return_type:
                switch (position) {
                    case 0:
                        break;
                    case 1:
                        returnType = Constants.ARGS_RETURNING;
                        break;
                    case 2:
                        returnType = Constants.ARGS_NEW;
                        break;
                }
                break;
            case R.id.spnr_visit_priority:
                switch (position) {
                    case 0:
                        //Select Visit Priority
                        break;
                    case 1:
                        priority = Constants.ARGS_SCHEDULED;
                        //Scheduled
                        break;
                    case 2:
                        priority = Constants.ARGS_DROP_IN;
                        //Drop-In
                        break;
                }
                break;
            case R.id.list_dialog:
                break;
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> arg0) {
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        switch (parent.getId()) {
            case R.id.lv_dialog:
                hideKeyboard();
                ServiceUser serviceUser = serviceUserList.get(position);
                realm.beginTransaction();
                realm.copyToRealmOrUpdate(serviceUser);
                realm.commitTransaction();
                userName = serviceUser.getPersonalFields().getName();
                hospitalNumber = serviceUser.getHospitalNumber();
                email = serviceUser.getPersonalFields().getEmail();
                sms = serviceUser.getPersonalFields().getMobilePhone();
                address = serviceUser.getPersonalFields().getHomeAddress();

                etUserName.setText(userName);
                userID = serviceUser.getId();
                postOrAnte();
                ad.cancel();
                break;
        }
    }
}
