package ie.teamchile.smartapp.activities;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
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
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TabHost;
import android.widget.TabHost.TabSpec;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import ie.teamchile.smartapp.R;
import ie.teamchile.smartapp.model.ApiRootModel;
import ie.teamchile.smartapp.model.PostingData;
import ie.teamchile.smartapp.util.SmartApi;
import ie.teamchile.smartapp.util.ToastAlert;
import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class ServiceUserActivity extends BaseActivity {
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
    private LinearLayout llUserContact, llUserAddress, llUserKinContact;
    private String dob = "", age = "", hospitalNumber, email, mobile, userName, kinName,
            kinMobile, road, county, postCode, gestation, parity, estimtedDelivery,
            perineum, birthMode, babyGender, babyWeightKg = "",
            vitK, hearing, antiD, feeding, nbst, deliveryDateTime, daysSinceBirth,
            userCall, userSMS, userEmail, lastPeriodDate;
    private Boolean rhesus;
    private int babyWeightGrams = 0;
    private List<Integer> babyID;
    private double grams = 0.0;
    private String sex_male = "ale";
    private String sex_female = "emale";
    private Dialog dialog;
    private Button bookAppointmentButton;
    private TableRow trParity, trAntiD, trMidwifeNotes;
    private Date dobAsDate = null;
    private Intent userCallIntent, userSmsIntent, userEmailIntent;
    private Calendar cal = Calendar.getInstance();
    private int b;        //position of most recent baby in list
    private int p;        //position of most recent pregnancy in list
    private int userId;
    private AlertDialog.Builder alertDialog;
    private AlertDialog ad;
    private List<String> antiDHistory = new ArrayList<>();
    private List<String> antiDDateTime = new ArrayList<>();
    private List<String> antiDProviderName = new ArrayList<>();
    private BaseAdapter adapter;
    private int antiDOptionSelected;
    private int pregnancyId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentForNav(R.layout.activity_service_user_tabhost);
        TabHost tabHost = (TabHost) findViewById(android.R.id.tabhost);
        tabHost.setup();

        TabSpec tab1 = tabHost.newTabSpec("Ante");
        tab1.setContent(R.id.tab_ante);
        tab1.setIndicator("Ante Natal");
        tabHost.addTab(tab1);

        TabSpec tab2 = tabHost.newTabSpec("Contact");
        tab2.setContent(R.id.tab_contact);
        tab2.setIndicator("Contact");
        tabHost.addTab(tab2);

        TabSpec tab3 = tabHost.newTabSpec("Post");
        tab3.setContent(R.id.tab_post);
        tab3.setIndicator("Post Natal");
        tabHost.addTab(tab3);

        tabHost.setCurrentTab(1);

        setAntiD();

        llUserContact = (LinearLayout) findViewById(R.id.ll_usr_contact);
        llUserContact.setOnClickListener(new ButtonClick());
        llUserAddress = (LinearLayout) findViewById(R.id.ll_usr_address);
        llUserAddress.setOnClickListener(new ButtonClick());
        llUserKinContact = (LinearLayout) findViewById(R.id.ll_usr_kin);
        llUserKinContact.setOnClickListener(new ButtonClick());

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

        bookAppointmentButton = (Button) findViewById(R.id.btn_usr_book_appointment);
        bookAppointmentButton.setOnClickListener(new ButtonClick());

        trParity = (TableRow) findViewById(R.id.tr_ante_parity);
        trParity.setOnClickListener(new ButtonClick());

        trAntiD = (TableRow) findViewById(R.id.tr_post_anti_d);
        trAntiD.setOnClickListener(new ButtonClick());

        trMidwifeNotes = (TableRow) findViewById(R.id.tr_midwife_notes);
        trMidwifeNotes.setOnClickListener(new ButtonClick());

        try {
            dob = ApiRootModel.getInstance().getServiceUsers().get(0).getPersonalFields().getDob();
            if (dob != null)
                age = getAge(dob);
            getRecentPregnancy();
            getRecentBaby();

            pregnancyId = ApiRootModel.getInstance().getPregnancies().get(p).getId();
            userId = ApiRootModel.getInstance().getServiceUsers().get(0).getId();
            hospitalNumber = ApiRootModel.getInstance().getServiceUsers().get(0).getHospitalNumber();
            email = ApiRootModel.getInstance().getServiceUsers().get(0).getPersonalFields().getEmail();
            mobile = ApiRootModel.getInstance().getServiceUsers().get(0).getPersonalFields().getMobilePhone();
            userName = ApiRootModel.getInstance().getServiceUsers().get(0).getPersonalFields().getName();
            kinName = ApiRootModel.getInstance().getServiceUsers().get(0).getPersonalFields().getNextOfKinName();
            kinMobile = ApiRootModel.getInstance().getServiceUsers().get(0).getPersonalFields().getNextOfKinPhone();
            road = ApiRootModel.getInstance().getServiceUsers().get(0).getPersonalFields().getHomeAddress();
            county = ApiRootModel.getInstance().getServiceUsers().get(0).getPersonalFields().getHomeCounty();
            postCode = ApiRootModel.getInstance().getServiceUsers().get(0).getPersonalFields().getHomePostCode();
            perineum = ApiRootModel.getInstance().getPregnancies().get(p).getPerineum();
            List<String> birthModeList = ApiRootModel.getInstance().getPregnancies().get(p).getBirthMode();
            if (birthModeList != null)
                birthMode = putArrayToString(ApiRootModel.getInstance().getPregnancies().get(p).getBirthMode());
            else
                birthMode = "";
            babyGender = ApiRootModel.getInstance().getBabies().get(b).getGender();
            if (ApiRootModel.getInstance().getBabies().get(b).getWeight() != null)
                babyWeightKg = getGramsToKg(ApiRootModel.getInstance().getBabies().get(b).getWeight());
            else
                babyWeightKg = "";
            babyID = ApiRootModel.getInstance().getServiceUsers().get(0).getBabyIds();
            gestation = ApiRootModel.getInstance().getPregnancies().get(p).getGestation();
            parity = ApiRootModel.getInstance().getServiceUsers().get(0).getClinicalFields().getParity();
            estimtedDelivery = ApiRootModel.getInstance().getPregnancies().get(p).getEstimatedDeliveryDate();
            lastPeriodDate = ApiRootModel.getInstance().getPregnancies().get(p).getLastMenstrualPeriod();
            vitK = ApiRootModel.getInstance().getBabies().get(b).getVitaminK();
            hearing = ApiRootModel.getInstance().getBabies().get(b).getHearing();
            antiD = ApiRootModel.getInstance().getPregnancies().get(p).getAntiD();
            feeding = ApiRootModel.getInstance().getPregnancies().get(p).getFeeding();
            rhesus = ApiRootModel.getInstance().getServiceUsers().get(0).getClinicalFields().getRhesus();
            if (rhesus)
                tvAnteRhesus.setText("Yes");
            else if (!rhesus)
                tvAnteRhesus.setText("No");
            if (feeding == null)
                feeding = "";
            nbst = ApiRootModel.getInstance().getBabies().get(b).getNewbornScreeningTest();
            deliveryDateTime = ApiRootModel.getInstance().getBabies().get(b).getDeliveryDateTime();
            if (deliveryDateTime != null)
                daysSinceBirth = getNoOfDays(deliveryDateTime);
            setActionBarTitle(userName);

            if (parity.equals("0 + 0"))
                trParity.setEnabled(false);
            tvAnteParity.setText(parity);
            tvAnteGestation.setText(gestation);
            tvAnteBloodGroup.setText(ApiRootModel.getInstance().getServiceUsers().get(0).getClinicalFields().getBloodGroup());
            if (estimtedDelivery != null)
                tvAnteDeliveryTime.setText(getEstimateDeliveryDate(estimtedDelivery));
            else
                tvAnteDeliveryTime.setText("");
            tvAnteAge.setText(age);

            tvUsrAge.setText(age);
            tvUsrHospitalNumber.setText(hospitalNumber);
            tvUsrEmail.setText(email);
            tvUsrMobileNumber.setText(mobile);
            tvUsrRoad.setText(road);
            tvUsrCounty.setText(county);
            tvUsrPostCode.setText(postCode);
            tvUsrNextOfKinName.setText(kinName);
            tvUsrKinContact.setText(kinMobile);
            if(gestation == null)
                tvUsrGestationTitle.setVisibility(View.GONE);
            else
                tvUsrGestation.setText(gestation);
            tvUsrParity.setText(parity);
            tvPostVitK.setText(vitK);
            tvPostHearing.setText(hearing);
            tvPostAntiD.setText(antiD);
            tvPostFeeding.setText(feeding);
            tvPostNBST.setText(nbst);
            tvPostDeliveryDate.setText(getDeliveryDate(deliveryDateTime));
            tvPostDeliveryTIme.setText(getDeliveryTime(deliveryDateTime));
            tvPostPerineum.setText(perineum);
            tvPostBirthMode.setText(birthMode);
            tvPostBirthWeight.setText(babyWeightKg);
            if (lastPeriodDate != null)
                tvAnteLastPeriod.setText(getLastPeriodDate(lastPeriodDate));
            if (babyGender.equalsIgnoreCase("M"))
                tvPostBabyGender.setText(babyGender + sex_male);
            else if (babyGender.equalsIgnoreCase("F"))
                tvPostBabyGender.setText(babyGender + sex_female);
            tvPostDaysSinceBirth.setText(daysSinceBirth);
        } catch (NullPointerException e) {
            e.printStackTrace();
        }
    }

    private void setAntiD(){
        for(int i = 0; i < ApiRootModel.getInstance().getAntiDHistories().size(); i++) {
            Date parsed;
            String formatted = "";
            try {
                parsed = dfDateTimeWMillisZone.parse(ApiRootModel.getInstance().getAntiDHistories().get(i).getCreatedAt());
                formatted = dfHumanReadableTimeDate.format(parsed);
            } catch (ParseException e) {
                e.printStackTrace();
            }

            antiDHistory.add(ApiRootModel.getInstance().getAntiDHistories().get(i).getAntiD());
            antiDDateTime.add(formatted);
            antiDProviderName.add(ApiRootModel.getInstance().getAntiDHistories().get(i).getServiceProviderName());
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
        userCall = "tel:" + tvUsrMobileNumber.getText().toString();
        userCallIntent = new Intent(Intent.ACTION_DIAL,
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
        userSMS = tvUsrMobileNumber.getText().toString();
        userSmsIntent = new Intent(Intent.ACTION_VIEW);
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
        userEmail = tvUsrEmail.getText().toString();
        userEmailIntent = new Intent(Intent.ACTION_SEND);
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
                .setNegativeButton(R.string.No, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialoginterface, int i) {
                    }
                })
                .setPositiveButton(R.string.Yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialoginterface, int i) {
                        String addr = tvUsrRoad.getText().toString() + ", "
                                + tvUsrCounty.getText().toString() + ", "
                                + tvUsrPostCode.getText().toString();
                        Log.d("bugs", "geoCode: " + getGeoCoodinates(addr + "?z=12"));
                        Uri uri = Uri.parse(getGeoCoodinates(addr)); //"geo:47.6,-122.3?z=18"
                        Log.d("bugs", "addr: " + addr);
                        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                        intent.setClassName("com.google.android.apps.maps", "com.google.android.maps.MapsActivity");
                        startActivity(intent);
                    }
                }).show();
    }

    private String getAge(String dob) {
        try {
            dobAsDate = dfDateOnly.parse(dob);
            cal.setTime(dobAsDate);
        } catch (ParseException | NullPointerException e) {
            e.printStackTrace();
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

    private String getEstimateDeliveryDate(String edd) {
        Date date;
        String ed = null;
        try {
            date = dfDateOnly.parse(edd);
            ed = dfMonthFullName.format(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return ed;
    }

    public String getLastPeriodDate(String edd) {
        Date date;
        String ed = null;
        try {
            date = dfDateOnly.parse(edd);
            ed = dfMonthFullName.format(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return ed;
    }

    protected String getDeliveryDate(String edd) {
        Date date;
        String dateOfDelivery = null;
        try {
            date = dfDateOnly.parse(edd);
            dateOfDelivery = dfMonthFullName.format(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return dateOfDelivery;
    }

    private String getDeliveryTime(String edd) {
        String deliveryTime = null;
        Date date;
        try {
            date = dfDateOnly.parse(edd);

            deliveryTime = dfAMPM.format(date);
            date = dfDateTimeWZone.parse(edd);

        } catch (ParseException e) {
            e.printStackTrace();
        }
        return deliveryTime;
    }

    private String getNoOfDays(String dateOfDelivery) {
        int numOfDays = 0;
        try {
            Date dodAsDate = dfDateTimeWZone.parse(deliveryDateTime);
            cal = Calendar.getInstance();
            Date now = cal.getTime();
            numOfDays = (int) ((now.getTime() - dodAsDate.getTime()) / (1000 * 60 * 60 * 24)) + 1;
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return String.valueOf(numOfDays);
    }

    private String getGramsToKg(int grams) {
        double kg = 0.0;
        kg = grams / 1000.0;
        return String.valueOf(kg);
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
        Log.d("bugs", "baby ids: " + babyID);
        SharedPreferences.Editor prefs = getSharedPreferences("SMART", MODE_PRIVATE).edit();
        if (babyID.get(p).equals("[]")) {
            prefs.putString("visit_type_str", "Antenatal");
            prefs.putString("visit_type", "ante-natal");
        } else {
            prefs.putString("visit_type_str", "Postnatal");
            prefs.putString("visit_type", "post-natal");
        }
        prefs.putString("name", userName);
        prefs.putString("id", String.valueOf(userId));
        prefs.putBoolean("reuse", true);
        prefs.commit();
    }

    private String getGeoCoodinates(String address) {
        Geocoder geocoder = new Geocoder(ServiceUserActivity.this);
        Log.d("The Address", address);
        List<Address> addresses = null;
        try {
            if (!address.isEmpty() && address != null)
                addresses = geocoder.getFromLocationName(address, 1);
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }

        if (addresses != null && addresses.size() > 0) {
            double latitude = addresses.get(0).getLatitude();
            double longitude = addresses.get(0).getLongitude();
            Log.d("Coordinates Found", String.valueOf(latitude));
            Log.d("Coordinates Found", String.valueOf(longitude));
            //"geo:47.6,-122.3?z=18"
            new ToastAlert(this, "geo:" + String.valueOf(latitude) + "," + String.valueOf(longitude) + "", false);
            return "geo:" + String.valueOf(latitude) + "," + String.valueOf(longitude) + "?z=12" +
                    "&q=" + String.valueOf(latitude) + "," + String.valueOf(longitude) +
                    "(" + userName + ")";
        }
        return "Not Found";
    }

    private void antiDAlertDialog() {
        LayoutInflater inflater = getLayoutInflater();
        alertDialog = new AlertDialog.Builder(ServiceUserActivity.this);
        View convertView = (View) inflater.inflate(R.layout.anti_d_dialog, null);
        ListView list = (ListView) convertView.findViewById(R.id.lv_anti_d);
        Button btnAntiD = (Button) convertView.findViewById(R.id.btn_anti_d);
        TextView tvDialogTitle = (TextView) convertView.findViewById(R.id.tv_anti_d_dialog_title);
        btnAntiD.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //String antiD = spnrAntiD.getSelectedItem().toString();
                        //putAntiD(antiD);
                        ad.dismiss();

                        updateAntiDAlertDialog();
                    }
                });

        alertDialog.setView(convertView);
        tvDialogTitle.setText("Anti-D History");
        adapter = new MyListAdapter(
                ServiceUserActivity.this,
                antiDHistory,
                antiDDateTime,
                antiDProviderName);
        list.setAdapter(adapter);
        ad = alertDialog.show();
    }

    private void updateAntiDAlertDialog() {
        String [] arrayFromXml = getResources().getStringArray(R.array.anti_d_list);
        final List<String> antiDOptions = Arrays.asList(arrayFromXml);

        LayoutInflater inflater = getLayoutInflater();
        View convertView = (View) inflater.inflate(R.layout.anti_d_update_dialog, null);
        final ListView list = (ListView) convertView.findViewById(R.id.lv_anti_d);
        Button btnAntiD = (Button) convertView.findViewById(R.id.btn_anti_d);
        TextView tvDialogTitle = (TextView) convertView.findViewById(R.id.tv_anti_d_dialog_title);

        btnAntiD.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if(list.isSelected()){
                            String antiD = antiDOptions.get(antiDOptionSelected);
                            putAntiD(antiD);
                            Log.d("bugs", "anti d position button = " + antiDOptionSelected);
                        } else
                            Toast.makeText(ServiceUserActivity.this, "Please Make A Selection First", Toast.LENGTH_LONG).show();
                    }
                });

        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                list.setSelected(true);
                antiDOptionSelected = position;
                Log.d("bugs", "anti d position list = " + position);
            }
        });

        alertDialog.setView(convertView);
        tvDialogTitle.setText("Select an Anti-D option");
        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                ServiceUserActivity.this,
                android.R.layout.simple_list_item_1,
                antiDOptions);
        list.setAdapter(adapter);
        ad = alertDialog.show();
    }

    private void putAntiD(String antiD) {
        c = Calendar.getInstance();
        PostingData puttingAntiD = new PostingData();

        puttingAntiD.putAntiD(antiD, userId);

        showProgressDialog(this, "Updating Anti-D");

        RestAdapter restAdapter = new RestAdapter.Builder()
                .setEndpoint(SmartApi.BASE_URL)
                .setLogLevel(RestAdapter.LogLevel.FULL)
                .build();

        System.setProperty("http.keepAlive", "false");

        api = restAdapter.create(SmartApi.class);

        api.putAnitD(
                puttingAntiD,
                ApiRootModel.getInstance().getPregnancies().get(p).getId(),
                ApiRootModel.getInstance().getLogin().getToken(),
                SmartApi.API_KEY,
                new Callback<ApiRootModel>() {
                    @Override
                    public void success(ApiRootModel apiRootModel, Response response) {
                        String antiD = apiRootModel.getPregnancy().getAntiD();
                        ad.dismiss();
                        tvPostAntiD.setText(antiD);
                        antiDHistory.add(0, antiD);
                        antiDDateTime.add(0, dfHumanReadableTimeDate.format(c.getTime()));
                        antiDProviderName.add(0, ApiRootModel.getInstance().getServiceProvider().getName());
                        ApiRootModel.getInstance().updatePregnancies(p, apiRootModel.getPregnancy());
                        Log.d("retro", "put anti-d retro success");
                        pd.dismiss();
                        ad.dismiss();
                    }

                    @Override
                    public void failure(RetrofitError error) {
                        Log.d("retro", "put anti-d retro failure = " + error);
                        pd.dismiss();
                    }
                }
        );
    }

    private void getAntiDHistory(){
        showProgressDialog(this, "Updating Anti-D");

        api.getAntiDHistoriesForPregnacy(
            ApiRootModel.getInstance().getPregnancies().get(0).getId(),
            ApiRootModel.getInstance().getLogin().getToken(),
            SmartApi.API_KEY,
            new Callback<ApiRootModel>() {
                @Override
                public void success(ApiRootModel apiRootModel, Response response) {
                    Log.d("retro", "get anti-d retro success");
                    ApiRootModel.getInstance().setAntiDHistories(apiRootModel.getAntiDHistories());
                    setAntiD();
                    adapter.notifyDataSetChanged();
                    pd.dismiss();
                }

                @Override
                public void failure(RetrofitError error) {
                    Log.d("retro", "get anti-d retro failure = " + error);
                    pd.dismiss();
                }
            }
        );
    }

    private class ButtonClick implements View.OnClickListener, DialogInterface {
        Intent intent;
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.btn_usr_book_appointment:
                    setSharedPrefs();
                    intent = new Intent(ServiceUserActivity.this, AppointmentTypeSpinnerActivity.class);
                    startActivity(intent);
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
                    startActivity(intent);
                    break;
                case R.id.tr_post_anti_d:
                    antiDAlertDialog();
                    break;
                case R.id.tr_midwife_notes:
                    intent = new Intent(ServiceUserActivity.this, MidwiferyLogActivity.class);
                    intent.putExtra("pregnancyId", String.valueOf(pregnancyId));
                    startActivity(intent);
                    break;
            }
        }

        @Override
        public void cancel() {
        }

        @Override
        public void dismiss() {
        }
    }

    private class MyListAdapter extends BaseAdapter {
        Context context;
        List<String> antiDHistory = new ArrayList<>();
        List<String> antiDDateTime = new ArrayList<>();
        List<String> antiDProviderName = new ArrayList<>();

        public MyListAdapter(Context context,
                             List<String> antiDHistory,
                             List<String> antiDDateTime,
                             List<String> antiDProviderName) {
            this.context = context;
            this.antiDHistory = antiDHistory;
            this.antiDDateTime = antiDDateTime;
            this.antiDProviderName = antiDProviderName;
        }

        @Override
        public int getCount() {
            return antiDDateTime.size();
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
            LayoutInflater inflater = getLayoutInflater();
            convertView = (View) inflater.inflate(R.layout.anti_d_list_layout, null);
            TextView tvHistory = (TextView) convertView.findViewById(R.id.tv_anti_d_option);
            TextView tvDateTime = (TextView) convertView.findViewById(R.id.tv_anti_d_date_time);
            TextView tvName = (TextView) convertView.findViewById(R.id.tv_anti_d_provider_name);

            tvHistory.setText(antiDHistory.get(position));
            tvDateTime.setText(antiDDateTime.get(position));
            tvName.setText(antiDProviderName.get(position));
            return convertView;
        }
    }
}