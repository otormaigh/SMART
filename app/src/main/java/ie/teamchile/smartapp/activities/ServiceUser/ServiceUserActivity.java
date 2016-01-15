package ie.teamchile.smartapp.activities.ServiceUser;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.Address;
import android.location.Geocoder;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TabHost;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.daimajia.swipe.SwipeLayout;

import java.io.IOException;
import java.lang.ref.WeakReference;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import ie.teamchile.smartapp.R;
import ie.teamchile.smartapp.activities.AppointmentTypeSpinner.AppointmentTypeSpinnerActivity;
import ie.teamchile.smartapp.activities.Base.BaseActivity;
import ie.teamchile.smartapp.activities.MidwiferyLog.MidwiferyLogActivity;
import ie.teamchile.smartapp.activities.ParityDetails.ParityDetailsActivity;
import ie.teamchile.smartapp.model.ResponseAntiDHistory;
import ie.teamchile.smartapp.model.ResponseBaby;
import ie.teamchile.smartapp.model.ResponseClinicalFields;
import ie.teamchile.smartapp.model.ResponseFeedingHistory;
import ie.teamchile.smartapp.model.ResponseHearingHistory;
import ie.teamchile.smartapp.model.ResponseNbstHistory;
import ie.teamchile.smartapp.model.ResponsePersonalFields;
import ie.teamchile.smartapp.model.ResponsePregnancy;
import ie.teamchile.smartapp.model.RealmInteger;
import ie.teamchile.smartapp.model.ResponseServiceUser;
import ie.teamchile.smartapp.model.ResponseServiceUserAction;
import ie.teamchile.smartapp.model.ResponseVitKHistory;
import ie.teamchile.smartapp.util.Constants;
import io.realm.Realm;
import timber.log.Timber;

public class ServiceUserActivity extends BaseActivity implements ServiceUserView {
    private final CharSequence[] userContactList = {"Call Mobile", "Send SMS", "Send Email"};
    private final CharSequence[] kinContactList = {"Call Mobile", "Send SMS"};
    private TextView tvAnteAge, tvAnteGestation, tvAnteParity, tvAnteDeliveryTime,
            tvAnteBloodGroup, tvAnteRhesus, tvAnteLastPeriod;
    private TextView tvUsrHospitalNumber, tvUsrEmail, tvUsrMobileNumber, tvUsrRoad,
            tvUsrCounty, tvUsrPostCode, tvUsrNextOfKinName, tvUsrAge,
            tvUsrKinContact, tvUsrGestation, tvUsrGestationTitle, tvUsrParity;
    private TextView tvPostBirthMode, tvPostPerineum, tvPostAntiD, tvPostDeliveryDate, tvPostDeliveryTIme,
            tvPostDaysSinceBirth, tvPostBabyGender, tvPostBirthWeight, tvPostVitK, tvPostHearing,
            tvPostFeeding, tvPostNBST;
    private String hospitalNumber = "";
    private String age = "";
    private String email = "";
    private String mobile = "";
    private String userName = "";
    private String kinName = "";
    private String kinMobile = "";
    private String road = "";
    private String county = "";
    private String postCode = "";
    private String gestation = "";
    private String parity = "";
    private String perineum = "";
    private String babyGender = "";
    private String babyWeightKg = "";
    private String vitK = "";
    private String hearing = "";
    private String antiD = "";
    private String feeding = "";
    private String nbst = "";
    private String lastPeriodDate = "";
    private String estimtedDelivery = "";
    private Date deliveryDateTime;
    private String rhesus = "";
    private List<RealmInteger> babyID = new ArrayList<>();
    private TableRow trParity;
    private Calendar cal = Calendar.getInstance();
    private int userId = 0;
    private AlertDialog.Builder alertDialog;
    private AlertDialog ad;
    private List<String> historyList = new ArrayList<>();
    private List<String> dateTimeList = new ArrayList<>();
    private List<String> providerNameList = new ArrayList<>();
    private HistoriesAdapter historiesAdapter;
    private int optionPosition;
    private int pregnancyId;
    private List<String> historyOptions;
    private Realm realm;
    private List<ResponseAntiDHistory> antiDHistoryList = new ArrayList<>();
    private List<ResponseFeedingHistory> responseFeedingHistoryList = new ArrayList<>();
    private List<ResponseNbstHistory> responseNbstHistoryList = new ArrayList<>();
    private List<ResponseHearingHistory> responseHearingHistoryList = new ArrayList<>();
    private List<ResponseVitKHistory> responseVitKHistoryList = new ArrayList<>();
    private List<ResponseServiceUserAction> responseServiceUserActionList = new ArrayList<>();
    private ServiceUserPresenter presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentForNav(R.layout.activity_service_user_tabhost);

        initViews();

        presenter = new ServiceUserPresenterImp(this, new WeakReference<Activity>(ServiceUserActivity.this));

        realm = presenter.getEncryptedRealm();

        antiDHistoryList.addAll(realm.where(ResponseAntiDHistory.class).findAll());

        responseFeedingHistoryList.addAll(realm.where(ResponseFeedingHistory.class).findAll());

        responseNbstHistoryList.addAll(realm.where(ResponseNbstHistory.class).findAll());

        responseHearingHistoryList.addAll(realm.where(ResponseHearingHistory.class).findAll());

        responseVitKHistoryList.addAll(realm.where(ResponseVitKHistory.class).findAll());

        responseServiceUserActionList.addAll(realm.where(ResponseServiceUserAction.class).findAll());
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        presenter.onLeaveView();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        presenter.onLeaveView();
    }

    private void setAntiD() {
        historyList = new ArrayList<>();
        dateTimeList = new ArrayList<>();
        providerNameList = new ArrayList<>();

        String[] arrayFromXml = getResources().getStringArray(R.array.anti_d_list);
        historyOptions = Arrays.asList(arrayFromXml);

        for (int i = 0; i < antiDHistoryList.size(); i++) {
            historyList.add(antiDHistoryList.get(i).getAntiD());
            dateTimeList.add(Constants.DF_HUMAN_READABLE_TIME_DATE.format(antiDHistoryList.get(i).getCreatedAt()));
            providerNameList.add(antiDHistoryList.get(i).getServiceProviderName());
        }
    }

    private void setFeeding() {
        historyList = new ArrayList<>();
        dateTimeList = new ArrayList<>();
        providerNameList = new ArrayList<>();

        String[] arrayFromXml = getResources().getStringArray(R.array.feeding_list);
        historyOptions = Arrays.asList(arrayFromXml);

        for (int i = 0; i < responseFeedingHistoryList.size(); i++) {
            historyList.add(responseFeedingHistoryList.get(i).getFeeding());
            dateTimeList.add(Constants.DF_HUMAN_READABLE_TIME_DATE.format(responseFeedingHistoryList.get(i).getCreatedAt()));
            providerNameList.add(responseFeedingHistoryList.get(i).getServiceProviderName());
        }
    }

    private void setVitK() {
        historyList = new ArrayList<>();
        dateTimeList = new ArrayList<>();
        providerNameList = new ArrayList<>();

        String[] arrayFromXml = getResources().getStringArray(R.array.vit_k_list);
        historyOptions = Arrays.asList(arrayFromXml);

        for (int i = 0; i < responseVitKHistoryList.size(); i++) {
            historyList.add(responseVitKHistoryList.get(i).getVitK());
            dateTimeList.add(Constants.DF_HUMAN_READABLE_TIME_DATE.format(responseVitKHistoryList.get(i).getCreatedAt()));
            providerNameList.add(responseVitKHistoryList.get(i).getServiceProviderName());
        }
    }

    private void setHearing() {
        historyList = new ArrayList<>();
        dateTimeList = new ArrayList<>();
        providerNameList = new ArrayList<>();

        String[] arrayFromXml = getResources().getStringArray(R.array.hearing_list);
        historyOptions = Arrays.asList(arrayFromXml);

        for (int i = 0; i < responseHearingHistoryList.size(); i++) {
            historyList.add(responseHearingHistoryList.get(i).getHearing());
            dateTimeList.add(Constants.DF_HUMAN_READABLE_TIME_DATE.format(responseHearingHistoryList.get(i).getCreatedAt()));
            providerNameList.add(responseHearingHistoryList.get(i).getServiceProviderName());
        }
    }

    private void setNBST() {
        historyList = new ArrayList<>();
        dateTimeList = new ArrayList<>();
        providerNameList = new ArrayList<>();

        String[] arrayFromXml = getResources().getStringArray(R.array.nbst_list);
        historyOptions = Arrays.asList(arrayFromXml);

        for (int i = 0; i < responseNbstHistoryList.size(); i++) {
            historyList.add(responseNbstHistoryList.get(i).getNbst());
            dateTimeList.add(Constants.DF_HUMAN_READABLE_TIME_DATE.format(responseNbstHistoryList.get(i).getCreatedAt()));
            providerNameList.add(responseNbstHistoryList.get(i).getServiceProviderName());
        }
    }

    private void dialogContact(final CharSequence[] items) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(R.string.contact_title);

        builder.setItems(items, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int item) {
                if (items[item].equals("Call Mobile"))
                    makeCall();
                if (items[item].equals("Send SMS"))
                    sendSms();
                if (items[item].equals("Send Email"))
                    sendEmail();
            }
        });
        AlertDialog alert = builder.create();
        alert.show();
        alert.setCanceledOnTouchOutside(true);
    }

    private void makeCall() {
        String userCall = "tel:" + tvUsrMobileNumber.getText().toString();
        Intent userCallIntent = new Intent(Intent.ACTION_DIAL,
                Uri.parse(userCall));
        try {
            startActivity(userCallIntent);
        } catch (android.content.ActivityNotFoundException ex) {
            Toast.makeText(ServiceUserActivity.this,
                    "Call failed, please try again later.",
                    Toast.LENGTH_LONG).show();
        }
    }

    private void sendSms() {
        String userSMS = tvUsrMobileNumber.getText().toString();
        Intent userSmsIntent = new Intent(Intent.ACTION_VIEW);
        userSmsIntent.setType("vnd.android-dir/mms-sms");
        userSmsIntent.putExtra("address", userSMS);
        try {
            startActivity(userSmsIntent);
        } catch (android.content.ActivityNotFoundException ex) {
            Toast.makeText(ServiceUserActivity.this,
                    "SMS failed, please try again later.",
                    Toast.LENGTH_LONG).show();
        }
    }

    private void sendEmail() {
        String userEmail = tvUsrEmail.getText().toString();
        Intent userEmailIntent = new Intent(Intent.ACTION_SEND);
        userEmailIntent.setClassName("com.google.android.gm", "com.google.android.gm.ComposeActivityGmail");
        userEmailIntent.setType("message/rfc822");
        userEmailIntent.putExtra(android.content.Intent.EXTRA_EMAIL, new String[]{userEmail});
        userEmailIntent.putExtra(Intent.EXTRA_SUBJECT, "");
        userEmailIntent.putExtra(Intent.EXTRA_TEXT, "");
        userEmailIntent.setType("plain/text");
        try {
            startActivity(userEmailIntent);
        } catch (android.content.ActivityNotFoundException ex) {
            Toast.makeText(ServiceUserActivity.this,
                    "Email failed, please try again later.",
                    Toast.LENGTH_LONG).show();
        }
    }

    private void gotoMaps() {
        new AlertDialog.Builder(this)
                .setTitle(R.string.user_address_title)
                .setMessage(R.string.user_address_message)
                .setNegativeButton(R.string.no, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialoginterface, int i) {
                    }
                })
                .setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialoginterface, int i) {
                        String addr = tvUsrRoad.getText().toString() + ", "
                                + tvUsrCounty.getText().toString() + ", "
                                + tvUsrPostCode.getText().toString();
                        Uri uri = Uri.parse(getGeoCoodinates(addr)); //"geo:47.6,-122.3?z=18"
                        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                        intent.setClassName("com.google.android.apps.maps", "com.google.android.maps.MapsActivity");
                        startActivity(intent);
                    }
                }).show();
    }

    private String getAge(String dob) {
        try {
            Date dobAsDate = Constants.DF_DATE_ONLY.parse(dob);
            cal.setTime(dobAsDate);
        } catch (ParseException | NullPointerException e) {
            Timber.e(Log.getStackTraceString(e));
        }
        int year = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH);
        int day = cal.get(Calendar.DAY_OF_MONTH);

        Date now = new Date();
        int nowMonth = now.getMonth() + 1;
        int nowYear = now.getYear() + 1900;
        int result = nowYear - year;

        if (month > nowMonth) {
            result--;
        } else if (month == nowMonth) {
            int nowDay = now.getDate();

            if (day > nowDay) {
                result--;
            }
        }
        return String.valueOf(result);
    }

    private String getGramsToKg(int grams) {
        return String.valueOf(grams / 1000.0);
    }

    private String formatArrayString(String toBeFormatted) {
        String formatedString = toBeFormatted
                .replace(",", ", ")  //remove the commas
                .replace("[", "")  //remove the right bracket
                .replace("]", "")  //remove the left bracket
                .replace("\"", "")
                .trim();
        return formatedString;
    }

    private void setSharedPrefs() {
        SharedPreferences.Editor prefs = getSharedPreferences(getString(R.string.app_name), MODE_PRIVATE).edit();
        if (babyID.get(getRecentPregnancy(realm.where(ResponsePregnancy.class).findAll())).equals("[]")) {
            prefs.putString("visit_type_str", "Antenatal");
            prefs.putString("visit_type", "ante-natal");
        } else {
            prefs.putString("visit_type_str", "Postnatal");
            prefs.putString("visit_type", "post-natal");
        }
        prefs.putString("name", userName);
        prefs.putString("hospitalNumber", hospitalNumber);
        prefs.putString("email", email);
        prefs.putString("mobile", mobile);
        prefs.putString("id", String.valueOf(userId));
        prefs.putBoolean("reuse", true);

        prefs.commit();
    }

    private String getGeoCoodinates(String address) {
        Geocoder geocoder = new Geocoder(ServiceUserActivity.this);
        List<Address> addresses = null;
        try {
            if (!address.isEmpty() && address != null)
                addresses = geocoder.getFromLocationName(address, 1);
        } catch (IOException e) {
            Timber.e(Log.getStackTraceString(e));
        }

        if (addresses != null && addresses.size() > 0) {
            double latitude = addresses.get(0).getLatitude();
            double longitude = addresses.get(0).getLongitude();
            return "geo:" + String.valueOf(latitude) + "," + String.valueOf(longitude) + "?z=12" +
                    "&q=" + String.valueOf(latitude) + "," + String.valueOf(longitude) +
                    "(" + userName + ")";
        }
        return "Not Found";
    }

    private void historyAlertDialog(final String title) {
        LayoutInflater inflater = getLayoutInflater();
        alertDialog = new AlertDialog.Builder(ServiceUserActivity.this);
        View convertView = inflater.inflate(R.layout.dialog_history, null);
        ListView list = (ListView) convertView.findViewById(R.id.lv_history);
        Button btnHistories = (Button) convertView.findViewById(R.id.btn_add_history);
        TextView tvDialogTitle = (TextView) convertView.findViewById(R.id.tv_history_dialog_title);
        ImageView ivExit = (ImageView) convertView.findViewById(R.id.iv_exit_dialog);

        ivExit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ad.dismiss();
            }
        });

        btnHistories.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        ad.dismiss();
                        updateHistoriesAlertDialog(title);
                    }
                });

        alertDialog.setView(convertView);
        tvDialogTitle.setText(title + " History");
        list.setAdapter(historiesAdapter);
        historiesAdapter.notifyDataSetChanged();
        ad = alertDialog.show();
    }

    private void updateHistoriesAlertDialog(final String title) {
        LayoutInflater inflater = getLayoutInflater();
        View convertView = inflater.inflate(R.layout.dialog_update_history, null);
        final ListView list = (ListView) convertView.findViewById(R.id.lv_history_options);
        Button btnAntiD = (Button) convertView.findViewById(R.id.btn_add_history);
        TextView tvDialogTitle = (TextView) convertView.findViewById(R.id.tv_history_dialog_title);
        ImageView ivExit = (ImageView) convertView.findViewById(R.id.iv_exit_dialog);
        ivExit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ad.dismiss();
            }
        });

        btnAntiD.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (list.isSelected()) {
                            String optionSelected = historyOptions.get(optionPosition);
                            if (title.equals("Anti-D"))
                                presenter.putAntiD(optionSelected, ad);
                            else if (title.equals("Vit-K"))
                                presenter.putVitK(optionSelected, ad);
                            else if (title.equals("Hearing"))
                                presenter.putHearing(optionSelected, ad);
                            else if (title.equals("NBST"))
                                presenter.putNBST(optionSelected, ad);
                            else if (title.equals("Feeding"))
                                presenter.putFeeding(optionSelected, ad);
                        } else
                            Toast.makeText(ServiceUserActivity.this, "Please Make A Selection First", Toast.LENGTH_LONG).show();
                    }
                });

        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                list.setSelected(true);
                optionPosition = position;
            }
        });

        alertDialog.setView(convertView);
        tvDialogTitle.setText("Select " + title + " option");
        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                ServiceUserActivity.this,
                android.R.layout.simple_list_item_1,
                historyOptions);
        list.setAdapter(adapter);
        ad = alertDialog.show();
    }

    private void showActionsDialog() {
        List<String> listServiceActions = new ArrayList<>();
        for (int i = 0; i < responseServiceUserActionList.size(); i++) {
            String shortCode = responseServiceUserActionList.get(i).getShortCode();
            listServiceActions.add(shortCode);
        }

        LayoutInflater inflater = getLayoutInflater();
        alertDialog = new AlertDialog.Builder(ServiceUserActivity.this);
        View convertView = inflater.inflate(R.layout.dialog_list_button, null);
        ListView list = (ListView) convertView.findViewById(R.id.lv_dialog);
        TextView tvDialogTitle = (TextView) convertView.findViewById(R.id.tv_dialog_title);
        ImageView ivDialogExit = (ImageView) convertView.findViewById(R.id.iv_dialog_exit);
        Button btnDialog = (Button) convertView.findViewById(R.id.btn_dialog);
        ivDialogExit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ad.dismiss();
            }
        });

        btnDialog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ad.dismiss();
            }
        });

        alertDialog.setView(convertView);
        //tvDialogTitle.setText(title + " History");
        ActionsBaseAdapter adapter = new ActionsBaseAdapter(listServiceActions);
        list.setAdapter(adapter);
        ad = alertDialog.show();
    }

    @Override
    public void initViews() {
        findViewById(R.id.btn_usr_book_appointment).setOnClickListener(new ButtonClick());
        findViewById(R.id.btn_usr_actions).setOnClickListener(new ButtonClick());

        findViewById(R.id.ll_usr_contact).setOnClickListener(new ButtonClick());
        findViewById(R.id.ll_usr_address).setOnClickListener(new ButtonClick());
        findViewById(R.id.ll_usr_kin).setOnClickListener(new ButtonClick());

        tvAnteAge = (TextView) findViewById(R.id.tv_ante_age);
        tvAnteGestation = (TextView) findViewById(R.id.tv_ante_gestation);
        tvAnteParity = (TextView) findViewById(R.id.tv_ante_parity);
        tvAnteDeliveryTime = (TextView) findViewById(R.id.tv_ante_edd);
        tvAnteBloodGroup = (TextView) findViewById(R.id.tv_ante_blood);
        tvAnteRhesus = (TextView) findViewById(R.id.tv_ante_rhesus);
        tvAnteLastPeriod = (TextView) findViewById(R.id.tv_ante_last_period);

        tvUsrHospitalNumber = (TextView) findViewById(R.id.tv_usr_hospital_number);
        tvUsrAge = (TextView) findViewById(R.id.tv_usr_age);
        tvUsrEmail = (TextView) findViewById(R.id.tv_usr_email);
        tvUsrMobileNumber = (TextView) findViewById(R.id.tv_usr_mobile);
        tvUsrRoad = (TextView) findViewById(R.id.tv_usr_road);
        tvUsrCounty = (TextView) findViewById(R.id.tv_usr_county);
        tvUsrPostCode = (TextView) findViewById(R.id.tv_usr_post_code);
        tvUsrNextOfKinName = (TextView) findViewById(R.id.tv_usr_kin_name);
        tvUsrKinContact = (TextView) findViewById(R.id.tv_usr_kin_contact);
        tvUsrGestation = (TextView) findViewById(R.id.tv_usr_gestation);
        tvUsrGestationTitle = (TextView) findViewById(R.id.tv_usr_gestation_title);
        tvUsrParity = (TextView) findViewById(R.id.tv_usr_partiy);

        tvPostBirthMode = (TextView) findViewById(R.id.tv_post_mode);
        tvPostPerineum = (TextView) findViewById(R.id.tv_post_perineum);
        tvPostAntiD = (TextView) findViewById(R.id.tv_post_anti_d);
        tvPostDeliveryDate = (TextView) findViewById(R.id.tv_post_delivery_date);
        tvPostDeliveryTIme = (TextView) findViewById(R.id.tv_post_delivery_time);
        tvPostDaysSinceBirth = (TextView) findViewById(R.id.tv_post_days_since_birth);
        tvPostBabyGender = (TextView) findViewById(R.id.tv_post_gender);
        tvPostBirthWeight = (TextView) findViewById(R.id.tv_post_weight);
        tvPostVitK = (TextView) findViewById(R.id.tv_post_vit_k);
        tvPostHearing = (TextView) findViewById(R.id.tv_post_hearing);
        tvPostFeeding = (TextView) findViewById(R.id.tv_post_feeding);
        tvPostNBST = (TextView) findViewById(R.id.tv_post_nbst);

        trParity = (TableRow) findViewById(R.id.tr_ante_parity);
        trParity.setOnClickListener(new ButtonClick());
        findViewById(R.id.iv_anti_d_history_icon).setOnClickListener(new ButtonClick());
        findViewById(R.id.iv_post_midwives_notes).setOnClickListener(new ButtonClick());
        findViewById(R.id.iv_vit_k_history_icon).setOnClickListener(new ButtonClick());
        findViewById(R.id.iv_hearing_history_icon).setOnClickListener(new ButtonClick());
        findViewById(R.id.iv_nbst_history_icon).setOnClickListener(new ButtonClick());
        findViewById(R.id.iv_feeding_history_icon).setOnClickListener(new ButtonClick());

        findViewById(R.id.tr_post_anti_d).setOnClickListener(new ButtonClick());
        findViewById(R.id.tr_midwife_notes).setOnClickListener(new ButtonClick());
        findViewById(R.id.tr_vit_k).setOnClickListener(new ButtonClick());
        findViewById(R.id.tr_hearing).setOnClickListener(new ButtonClick());
        findViewById(R.id.tr_nbst).setOnClickListener(new ButtonClick());
        findViewById(R.id.tr_feeding).setOnClickListener(new ButtonClick());

        historiesAdapter = new HistoriesAdapter();

        TabHost tabHost = (TabHost) findViewById(android.R.id.tabhost);
        tabHost.setup();

        TabHost.TabSpec tab1 = tabHost.newTabSpec("Ante");
        tab1.setContent(R.id.tab_ante);
        tab1.setIndicator("Ante Natal");
        tabHost.addTab(tab1);

        TabHost.TabSpec tab2 = tabHost.newTabSpec("Contact");
        tab2.setContent(R.id.tab_contact);
        tab2.setIndicator("Contact");
        tabHost.addTab(tab2);

        TabHost.TabSpec tab3 = tabHost.newTabSpec("Post");
        tab3.setContent(R.id.tab_post);
        tab3.setIndicator("Post Natal");
        tabHost.addTab(tab3);

        tabHost.setCurrentTab(1);
    }

    @Override
    public void updateTextViews(ServiceUserPresenter serviceUserPresenter, ResponseServiceUser responseServiceUser, ResponseBaby responseBaby, ResponsePregnancy responsePregnancy) {
        String bloodGroup = "";
        String birthMode = "";

        if (responseServiceUser != null) {

            if (responseServiceUser.getHospitalNumber() != null)
                hospitalNumber = responseServiceUser.getHospitalNumber();

            if (responseServiceUser.getId() != 0)
                userId = responseServiceUser.getId();

            if (!responseServiceUser.getBabyIds().isEmpty())
                babyID = responseServiceUser.getBabyIds();

            if (responseServiceUser.getPersonalFields() != null) {
                ResponsePersonalFields pf = responseServiceUser.getPersonalFields();

                if (pf.getDob() != null)
                    age = getAge(pf.getDob());

                if (pf.getEmail() != null)
                    email = pf.getEmail();

                if (pf.getMobilePhone() != null)
                    mobile = pf.getMobilePhone();

                if (pf.getName() != null)
                    userName = pf.getName();

                if (pf.getNextOfKinName() != null)
                    kinName = pf.getNextOfKinName();

                if (pf.getNextOfKinPhone() != null)
                    kinMobile = pf.getNextOfKinPhone();

                if (pf.getHomeAddress() != null)
                    road = pf.getHomeAddress();

                if (pf.getHomeCounty() != null)
                    county = pf.getHomeCounty();

                if (pf.getHomePostCode() != null)
                    postCode = pf.getHomePostCode();
            }

            if (responseServiceUser.getClinicalFields() != null) {
                ResponseClinicalFields cf = responseServiceUser.getClinicalFields();

                if (cf.getParity() != null)
                    parity = cf.getParity();

                if (cf.getBloodGroup() != null)
                    bloodGroup = cf.getBloodGroup();

                if (cf.isRhesus())
                    rhesus = "Yes";
                else
                    rhesus = "No";
            }
        }

        if (responseBaby != null) {
            if (responseBaby.getGender() != null) {
                if (responseBaby.getGender().equalsIgnoreCase("M")) {
                    babyGender = "Male";
                } else if (responseBaby.getGender().equalsIgnoreCase("F")) {
                    babyGender = "Female";
                }
            }

            if (responseBaby.getWeight() != 0)
                babyWeightKg = getGramsToKg(responseBaby.getWeight());

            if (responseBaby.getVitK() != null)
                vitK = responseBaby.getVitK();

            if (responseBaby.getHearing() != null)
                hearing = responseBaby.getHearing();

            if (responseBaby.getNbst() != null)
                nbst = responseBaby.getNbst();

            if (responseBaby.getDeliveryDateTime() != null)
                deliveryDateTime = responseBaby.getDeliveryDateTime();
        }

        if (responsePregnancy != null) {
            if (responsePregnancy.getId() != 0)
                pregnancyId = responsePregnancy.getId();

            if (responsePregnancy.getPerineum() != null)
                perineum = responsePregnancy.getPerineum();

            /*List<RealmString> birthModeList = responsePregnancy.getBirthMode();
            if (!birthModeList.isEmpty())
                birthMode = putArrayToString(responsePregnancy.getBirthMode());
            else
                birthMode = "";*/

            if (responsePregnancy.getGestation() != null)
                gestation = responsePregnancy.getGestation();

            if (responsePregnancy.getEstimatedDeliveryDate() != null)
                estimtedDelivery = serviceUserPresenter.getEstimateDeliveryDate(responsePregnancy.getEstimatedDeliveryDate());

            if (responsePregnancy.getLastMenstrualPeriod() != null)
                lastPeriodDate = Constants.DF_MONTH_FULL_NAME.format(responsePregnancy.getLastMenstrualPeriod());

            if (responsePregnancy.getAntiD() != null)
                antiD = responsePregnancy.getAntiD();

            if (responsePregnancy.getFeeding() != null)
                feeding = responsePregnancy.getFeeding();
        }


        setActionBarTitle(userName);
        tvUsrHospitalNumber.setText(hospitalNumber);
        tvAnteRhesus.setText(rhesus);

        if (parity.equals("0 + 0"))
            trParity.setEnabled(false);

        tvAnteParity.setText(parity);
        tvAnteGestation.setText(gestation);
        tvAnteBloodGroup.setText(bloodGroup);
        tvAnteDeliveryTime.setText(estimtedDelivery);
        tvAnteAge.setText(age);
        tvUsrAge.setText(age);
        tvUsrEmail.setText(email);
        tvUsrMobileNumber.setText(mobile);
        tvUsrRoad.setText(road);
        tvUsrCounty.setText(county);
        tvUsrPostCode.setText(postCode);
        tvUsrNextOfKinName.setText(kinName);
        tvUsrKinContact.setText(kinMobile);

        if (gestation == null)
            tvUsrGestationTitle.setVisibility(View.GONE);
        else
            tvUsrGestation.setText(gestation);

        tvUsrParity.setText(parity);
        tvPostVitK.setText(vitK);
        tvPostHearing.setText(hearing);
        tvPostAntiD.setText(antiD);
        tvPostFeeding.setText(feeding);
        tvPostNBST.setText(nbst);
        tvPostDeliveryDate.setText(serviceUserPresenter.getDeliveryDate(deliveryDateTime));
        tvPostDeliveryTIme.setText(serviceUserPresenter.getDeliveryTime(deliveryDateTime));
        tvPostPerineum.setText(perineum);
        tvPostBirthMode.setText(birthMode);
        tvPostBirthWeight.setText(babyWeightKg);
        tvAnteLastPeriod.setText(lastPeriodDate);
        tvPostBabyGender.setText(babyGender);
        tvPostDaysSinceBirth.setText(serviceUserPresenter.getNoOfDays(deliveryDateTime));
    }

    private class ActionsBaseAdapter extends BaseAdapter {
        List<String> listServiceActions = new ArrayList<>();

        public ActionsBaseAdapter(List<String> listServiceActions) {
            this.listServiceActions = listServiceActions;
        }

        @Override
        public void notifyDataSetChanged() {
            super.notifyDataSetChanged();
        }

        @Override
        public int getCount() {
            return listServiceActions.size();
        }

        @Override
        public String getItem(int position) {
            return listServiceActions.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            LayoutInflater inflater = getLayoutInflater();
            convertView = inflater.inflate(R.layout.list_layout_actions, null);
            TextView tvAction = (TextView) convertView.findViewById(R.id.tv_action);
            final Button btnBookAction = (Button) convertView.findViewById(R.id.btn_book_action);
            final ImageView ivStatus = (ImageView) convertView.findViewById(R.id.img_status);
            final SwipeLayout swipeLayout = (SwipeLayout) convertView.findViewById(R.id.swipe_action_list);
            RelativeLayout llActionListItem = (RelativeLayout) convertView.findViewById(R.id.rl_action_list_item);

            btnBookAction.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ivStatus.setBackgroundResource(R.color.attended);
                    swipeLayout.close();
                    //actionIdList.add(responseServiceUserActionList.get(position).getId());
                    presenter.postPregnancyActions(getItem(position));
                }
            });

            tvAction.setText(getItem(position));

            return convertView;
        }
    }

    private class ButtonClick implements View.OnClickListener {
        Intent intent = null;

        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.btn_usr_book_appointment:
                    setSharedPrefs();
                    intent = new Intent(ServiceUserActivity.this, AppointmentTypeSpinnerActivity.class);
                    intent.putExtra("userId", String.valueOf(userId));
                    startActivity(intent);
                    break;
                case R.id.btn_usr_actions:
                    showActionsDialog();
                    break;
                case R.id.ll_usr_contact:
                    dialogContact(userContactList);
                    break;
                case R.id.ll_usr_kin:
                    dialogContact(kinContactList);
                    break;
                case R.id.ll_usr_address:
                    gotoMaps();
                    break;
                case R.id.tr_ante_parity:
                    intent = new Intent(ServiceUserActivity.this, ParityDetailsActivity.class);
                    break;
                case R.id.tr_post_anti_d:
                case R.id.iv_anti_d_history_icon:
                    setAntiD();
                    historyAlertDialog("Anti-D");
                    break;
                case R.id.tr_midwife_notes:
                case R.id.iv_post_midwives_notes:
                    intent = new Intent(ServiceUserActivity.this, MidwiferyLogActivity.class);
                    intent.putExtra("pregnancyId", String.valueOf(pregnancyId));
                    break;
                case R.id.tr_vit_k:
                case R.id.iv_vit_k_history_icon:
                    setVitK();
                    historyAlertDialog("Vit-K");
                    break;
                case R.id.tr_hearing:
                case R.id.iv_hearing_history_icon:
                    setHearing();
                    historyAlertDialog("Hearing");
                    break;
                case R.id.tr_nbst:
                case R.id.iv_nbst_history_icon:
                    setNBST();
                    historyAlertDialog("NBST");
                    break;
                case R.id.tr_feeding:
                case R.id.iv_feeding_history_icon:
                    setFeeding();
                    historyAlertDialog("Feeding");
                    break;
            }
            if (intent != null)
                startActivity(intent);
        }
    }

    private class HistoriesAdapter extends BaseAdapter {
        @Override
        public int getCount() {
            return dateTimeList.size();
        }

        @Override
        public String getItem(int position) {
            return dateTimeList.get(position);
        }

        @Override
        public long getItemId(int position) {
            return dateTimeList.get(position).hashCode();
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            LayoutInflater inflater = getLayoutInflater();
            convertView = inflater.inflate(R.layout.list_layout_history, null);
            TextView tvHistory = (TextView) convertView.findViewById(R.id.tv_anti_d_option);
            TextView tvDateTime = (TextView) convertView.findViewById(R.id.tv_anti_d_date_time);
            TextView tvName = (TextView) convertView.findViewById(R.id.tv_anti_d_provider_name);

            tvHistory.setText(historyList.get(position));
            tvDateTime.setText(dateTimeList.get(position));
            tvName.setText(providerNameList.get(position));
            return convertView;
        }
    }
}
