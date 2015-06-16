package ie.teamchile.smartapp.activities;

import android.app.ActionBar;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import ie.teamchile.smartapp.R;
import ie.teamchile.smartapp.enums.CredentialsEnum;
import ie.teamchile.smartapp.model.ApiRootModel;
import ie.teamchile.smartapp.model.Appointment;
import ie.teamchile.smartapp.model.Baby;
import ie.teamchile.smartapp.model.Pregnancy;
import ie.teamchile.smartapp.util.SmartApi;
import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class BaseActivity extends AppCompatActivity {
    protected DateFormat dfDateTimeWZone = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZ", Locale.getDefault());
    protected DateFormat dfDateTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
    protected DateFormat dfDateOnly = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
    protected DateFormat dfTimeOnly = new SimpleDateFormat("HH:mm", Locale.getDefault());
    protected DateFormat dfTimeWSec = new SimpleDateFormat("HH:mm:ss", Locale.getDefault());
    protected DateFormat dfDateWMonthName = new SimpleDateFormat("dd MMM", Locale.getDefault());
    protected DateFormat dfDayShort = new SimpleDateFormat("E", Locale.getDefault());
    protected DateFormat dfDayLong = new SimpleDateFormat("EEEE", Locale.getDefault());
    protected DateFormat dfDowMonthDay = new SimpleDateFormat("EEE, d MMM", Locale.getDefault());
    protected DateFormat dfDateMonthNameYear = new SimpleDateFormat("dd MMM yyyy", Locale.getDefault());
    protected DateFormat dfMonthFullName = new SimpleDateFormat("dd MMMM yyyy", Locale.getDefault());
    protected DateFormat dfAMPM = new SimpleDateFormat("HH:mm a", Locale.getDefault());
    protected DateFormat dfDateTimeWMillisZone = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSSSSZ", Locale.getDefault());
    protected DateFormat dfHumanReadable = new SimpleDateFormat("HH:mm, dd/MM/yyyy", Locale.getDefault());
    protected Calendar c = Calendar.getInstance();

    protected ProgressDialog pd;
    protected DrawerLayout drawerLayout;
    protected ListView drawerList;
    protected ActionBarDrawerToggle drawerToggle;
    protected SmartApi api;
    protected int p = 0;
    protected int b = 0;
    protected int spinnerWarning;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.navigation_drawer_layout);

        spinnerWarning = getResources().getColor(R.color.lightBlue);

        //getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        //getActionBar().setCustomView(R.layout.action_bar_custom);
        //getSupportActionBar().setCustomView(R.layout.action_bar_custom);

        getSupportActionBar().setDisplayOptions(android.support.v7.app.ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setCustomView(R.layout.action_bar_custom);

        ActionBar.LayoutParams lp = new ActionBar.LayoutParams(
                ActionBar.LayoutParams.WRAP_CONTENT,
                ActionBar.LayoutParams.WRAP_CONTENT,
                Gravity.CENTER_HORIZONTAL);

        createNavDrawer();
        initRetrofit();
    }

    protected void showProgressDialog(Context context, String message) {
        pd = new ProgressDialog(context);
        pd.setMessage(message);
        pd.setCanceledOnTouchOutside(false);
        pd.setCancelable(false);
        pd.show();
    }

    protected void initRetrofit() {
        RestAdapter restAdapter = new RestAdapter.Builder()
                .setEndpoint(SmartApi.BASE_URL)
                        //.setLogLevel(RestAdapter.LogLevel.FULL)
                .build();

        api = restAdapter.create(SmartApi.class);
    }

    protected void setContentForNav(int layout) {
        LayoutInflater inflater = (LayoutInflater) this
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View contentView = inflater.inflate(layout, null, false);
        drawerLayout.addView(contentView, 0);
    }

    protected void setActionBarTitle(String title) {
        View v = getSupportActionBar().getCustomView();
        TextView titleTxtView = (TextView) v.findViewById(R.id.tv_action_bar);
        titleTxtView.setText(title);
    }

    protected void createNavDrawer() {
        String[] drawerItems = getResources().getStringArray(R.array.nav_drawer_items);
        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawerList = (ListView) findViewById(R.id.lv_nav_drawer);
        drawerList.setAdapter(new ArrayAdapter<>(this,
                R.layout.drawer_item_layout, drawerItems));
        drawerList.setOnItemClickListener(new DrawerItemClickListener());
        drawerToggle = new ActionBarDrawerToggle(this, drawerLayout,
                R.string.drawer_open, R.string.drawer_close);
        drawerLayout.setDrawerListener(drawerToggle);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        drawerToggle.syncState();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (drawerToggle.onOptionsItemSelected(item)) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void selectItem(int position) {
        Intent intent;
        //drawerLayout.closeDrawer(drawerList);
        switch (position) {
            case 0:         //Patient Search
                intent = new Intent(getApplicationContext(), ServiceUserSearchActivity.class);
                startActivity(intent);
                break;
            case 1:         //Book Appointment
                intent = new Intent(getApplicationContext(), AppointmentTypeSpinnerActivity.class);
                startActivity(intent);
                break;
            case 2:         //TimeRecords
                intent = new Intent(getApplicationContext(), ClinicTimeRecordActivity.class);
                startActivity(intent);
                break;
            case 3:         //Todays Appointments
                //intent = new Intent(getApplicationContext(), TodayAppointmentActivity.class);
                //startActivity(intent);
                break;
            case 4:         //Sync
                updateAppointment(this);
                break;
            case 5:         //Logout
                new AlertDialog.Builder(this)
                        .setTitle(R.string.Logout_title)
                        .setMessage(R.string.Logout_dialog_message)
                        .setNegativeButton(R.string.No, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialoginterface, int i) {
                            }
                        })
                        .setPositiveButton(R.string.Yes, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialoginterface, int i) {
                                Log.d("MYLOG", "Logout button pressed");
                                final Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP |
                                        Intent.FLAG_ACTIVITY_CLEAR_TASK |
                                        Intent.FLAG_ACTIVITY_NEW_TASK);
                                if (ApiRootModel.getInstance().getLoginStatus() == false) {
                                    startActivity(intent);
                                } else {
                                    doLogout(intent);
                                    pd = new ProgressDialog(BaseActivity.this);
                                    pd.setMessage("Logging Out");
                                    pd.setCanceledOnTouchOutside(false);
                                    pd.setCancelable(false);
                                    pd.show();
                                }
                            }
                        }).show();
                break;
            default:
        }
    }

    private void doLogout(final Intent intent) {
        api.postLogout(
                ApiRootModel.getInstance().getLogin().getToken(),
                SmartApi.API_KEY,
                new Callback<ApiRootModel>() {
                    @Override
                    public void success(ApiRootModel apiRootModel, Response response) {
                        switch (response.getStatus()) {
                            case 200:
                                Log.d("Retro", "in logout success 200");
                                doLogout(intent);
                                break;
                            default:
                                Log.d("Retro", "in logout success response = " + response.getStatus());
                        }
                    }

                    @Override
                    public void failure(RetrofitError error) {
                        Log.d("Retro", "in logout failure error = " + error);
                        if (error.getResponse().getStatus() == 401) {
                            ApiRootModel.getInstance().setLoginStatus(false);
                            Toast.makeText(getApplicationContext(),
                                    "You are now logged out",
                                    Toast.LENGTH_SHORT).show();
                            pd.dismiss();
                            startActivity(intent);
                        } else {
                            Toast.makeText(getApplicationContext(),
                                    "There was a problem with the logout, " +
                                            "\nPlease try again.",
                                    Toast.LENGTH_SHORT).show();
                            pd.dismiss();
                        }
                    }
                }
        );
    }

    private class DrawerItemClickListener implements ListView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView parent, View view, final int position, long id) {
            drawerLayout.closeDrawers();
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    selectItem(position);
                }
            },210);
        }
    }

    protected String putArrayToString(List<String> badList) {
        String listAsString = "";
        int listSize = badList.size();
        for (int i = 0; i < listSize; i++) {
            if (i == (listSize - 1))
                listAsString += badList.get(i);
            else
                listAsString += badList.get(i) + ", ";
        }
        return listAsString;
    }

    protected void getRecentPregnancy() {
        List<Pregnancy> pregnancyList = ApiRootModel.getInstance().getPregnancies();
        List<Date> asDate = new ArrayList<>();
        String edd;
        Log.d("Retro", "pregnancyList size = " + pregnancyList.size());
        if (pregnancyList.size() > 0) {
            try {
                for (int i = 0; i < pregnancyList.size(); i++) {
                    edd = pregnancyList.get(i).getEstimatedDeliveryDate();
                    asDate.add(dfDateOnly.parse(edd));
                }
                p = asDate.indexOf(Collections.max(asDate));
            } catch (ParseException | NullPointerException e) {
                e.printStackTrace();
            }
        } else
            p = 0;
    }

    protected void getRecentBaby() {
        List<Baby> babyList = ApiRootModel.getInstance().getBabies();
        List<Date> asDate = new ArrayList<>();
        if (babyList.size() != 1) {
            try {
                for (int i = 0; i < babyList.size(); i++) {
                    String deliveryDateTime = babyList.get(i).getDeliveryDateTime();
                    asDate.add(dfDateTimeWZone.parse(deliveryDateTime));
                }
                b = asDate.indexOf(Collections.max(asDate));
            } catch (ParseException | NullPointerException e) {
                e.printStackTrace();
            }
        } else
            b = 0;
    }

    protected void updateAppointment(Context context) {
        showProgressDialog(context, "Updating Appointments");
        api.getAllAppointments(
            ApiRootModel.getInstance().getLogin().getToken(),
            CredentialsEnum.API_KEY.toString(),
            new Callback<ApiRootModel>() {
                @Override
                public void success(ApiRootModel apiRootModel, Response response) {
                    ApiRootModel.getInstance().setAppointments(apiRootModel.getAppointments());
                    List<Integer> apptIdList;
                    Map<String, List<Integer>> dateApptIdMap;
                    Map<Integer, Map<String, List<Integer>>> clinicDateApptIdMap = new HashMap<>();
                    Map<Integer, Appointment> idApptMap = new HashMap<>();

                    for (int i = 0; i < apiRootModel.getAppointments().size(); i++) {
                        apptIdList = new ArrayList<>();
                        dateApptIdMap = new HashMap<>();
                        String apptDate = apiRootModel.getAppointments().get(i).getDate();
                        int apptId = apiRootModel.getAppointments().get(i).getId();
                        int clinicId = apiRootModel.getAppointments().get(i).getClinicId();
                        Appointment appt = apiRootModel.getAppointments().get(i);

                        if (clinicDateApptIdMap.get(clinicId) != null) {
                            dateApptIdMap = clinicDateApptIdMap.get(clinicId);
                            if (dateApptIdMap.get(apptDate) != null) {
                                apptIdList = dateApptIdMap.get(apptDate);
                            }
                        }
                        apptIdList.add(apptId);
                        dateApptIdMap.put(apptDate, apptIdList);

                        clinicDateApptIdMap.put(clinicId, dateApptIdMap);
                        idApptMap.put(apptId, appt);
                    }
                    ApiRootModel.getInstance().setClinicDateApptIdMap(clinicDateApptIdMap);
                    ApiRootModel.getInstance().setIdApptMap(idApptMap);
                    Log.d("Retrofit", "appointments finished");
                    pd.dismiss();
                }

                @Override
                public void failure(RetrofitError error) {
                    Log.d("Retrofit", "appointments retro failure " + error);
                    pd.dismiss();
                }
            });
    }
}