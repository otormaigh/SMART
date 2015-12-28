package ie.teamchile.smartapp.activities.Base;

import android.app.ActivityManager;
import android.app.AlertDialog;
import android.app.NotificationManager;
import android.app.Service;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.IBinder;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.net.ConnectException;
import java.net.UnknownHostException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import javax.net.ssl.SSLHandshakeException;

import ie.teamchile.smartapp.R;
import ie.teamchile.smartapp.activities.AppointmentTypeSpinnerActivity;
import ie.teamchile.smartapp.activities.ClinicTimeRecordActivity;
import ie.teamchile.smartapp.activities.Login.LoginActivity;
import ie.teamchile.smartapp.activities.QuickMenu.QuickMenuActivity;
import ie.teamchile.smartapp.activities.ServiceUserSearch.ServiceUserSearchActivity;
import ie.teamchile.smartapp.model.Baby;
import ie.teamchile.smartapp.model.Login;
import ie.teamchile.smartapp.model.Pregnancy;
import ie.teamchile.smartapp.util.CustomDialogs;
import ie.teamchile.smartapp.util.ToastAlert;
import io.realm.Realm;
import retrofit.RetrofitError;
import timber.log.Timber;

public class BaseActivity extends AppCompatActivity implements BaseView {
    protected static CountDownTimer timer;
    protected DateFormat dfDateOnly = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
    protected DateFormat dfTimeOnly = new SimpleDateFormat("HH:mm", Locale.getDefault());
    protected DateFormat dfTimeWSec = new SimpleDateFormat("HH:mm:ss", Locale.getDefault());
    protected DateFormat dfDateWMonthName = new SimpleDateFormat("dd MMM", Locale.getDefault());
    protected DateFormat dfDayShort = new SimpleDateFormat("E", Locale.getDefault());
    protected DateFormat dfHumanReadableTimeDate = new SimpleDateFormat("HH:mm, dd/MM/yyyy", Locale.getDefault());
    protected DrawerLayout drawerLayout;
    protected ListView drawerList;
    protected ActionBarDrawerToggle drawerToggle;
    protected int spinnerWarning;
    protected int thingALing = 0;
    protected LogoutService logServ;
    protected NotificationManager notificationManager;
    protected static int apptDone;
    private Realm realm;
    private BasePresenter basePresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        disableScreenshot();
        setContentView(R.layout.navigation_drawer_layout);

        realm = Realm.getInstance(this);

        basePresenter = new BasePresenterImp(BaseActivity.this, realm);

        spinnerWarning = ContextCompat.getColor(getApplicationContext(), R.color.teal);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayOptions(android.support.v7.app.ActionBar.DISPLAY_SHOW_CUSTOM);
            getSupportActionBar().setCustomView(R.layout.action_bar_custom);
        }

        createNavDrawer();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (realm.where(Login.class).findFirst() == null) {
            startActivity(new Intent(getApplicationContext(), LoginActivity.class));
            new CustomDialogs().showWarningDialog(BaseActivity.this, getString(R.string.error_please_login));
        }

        while (thingALing != 0) {
            thingALing--;
            logServ.startTimer(false);
        }
        if (notificationManager != null)
            notificationManager.cancelAll();
    }

    @Override
    protected void onStop() {
        super.onStop();

        if (isMyServiceRunning()) {
            new ToastAlert(getBaseContext(), getString(R.string.view_is_hidden), false);
            thingALing++;
            basePresenter.showNotification(getString(R.string.app_name), getString(R.string.warning_logged_out_soon), QuickMenuActivity.class);
            logServ = new LogoutService();
            logServ.startTimer(true);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        if (realm != null)
            realm.close();
    }

    @Override
    public void disableScreenshot() {
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_SECURE, WindowManager.LayoutParams.FLAG_SECURE);
    }

    @Override
    public void initViews() {

    }

    private boolean isMyServiceRunning() {
        ActivityManager am = (ActivityManager) this.getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningTaskInfo> tasks = am.getRunningTasks(1);
        if (!tasks.isEmpty()) {
            ComponentName topActivity = tasks.get(0).topActivity;
            if (!topActivity.getPackageName().equals(this.getPackageName())) {
                return true;
            }
        }
        return false;
    }

    protected void checkRetroError(RetrofitError error, Context context) {
        String time = dfTimeWSec.format(Calendar.getInstance().getTime());
        try {
            throw (error.getCause());
        } catch (UnknownHostException e) {
            Timber.e(Log.getStackTraceString(e));
            Timber.e("UnknownHostException");
            // unknown host
        } catch (SSLHandshakeException e) {
            Timber.e(Log.getStackTraceString(e));
            Timber.e("SSLHandshakeException");
        } catch (ConnectException e) {
            Timber.e(Log.getStackTraceString(e));
            Timber.e("ConnectException");
            /*if(!checkIfConnected(context)){
                new SharedPrefs().setJsonPrefs(data, time);
            }*/
        } catch (Throwable throwable) {
            Timber.e(Log.getStackTraceString(throwable));
            Timber.e("Throwable");
        }
        Timber.d(error.toString());
    }

    protected boolean checkIfConnected(Context context) {
        ConnectivityManager conMgr = (ConnectivityManager) getApplicationContext()
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = conMgr.getActiveNetworkInfo();

        if (netInfo == null) {
            Toast.makeText(getApplicationContext(), getString(R.string.no_internet), Toast.LENGTH_LONG).show();
            Timber.d("no internet");
            return false;
        } else if (!netInfo.isConnected() || !netInfo.isAvailable()) {
            Toast.makeText(getApplicationContext(), getString(R.string.no_internet), Toast.LENGTH_LONG).show();
            Timber.d("no internet");
            return false;
        }
        return true;
    }

    @Override
    public void setContentForNav(int layout) {
        LayoutInflater inflater = (LayoutInflater) this
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View contentView = inflater.inflate(layout, null, false);
        drawerLayout.addView(contentView, 0);
    }

    @Override
    public void setActionBarTitle(String title) {
        if (getSupportActionBar() != null) {
            View v = getSupportActionBar().getCustomView();
            TextView titleTxtView = (TextView) v.findViewById(R.id.tv_action_bar);
            titleTxtView.setText(title);
        }
    }

    @Override
    public void createNavDrawer() {
        String[] drawerItems = getResources().getStringArray(R.array.nav_drawer_items);
        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawerList = (ListView) findViewById(R.id.lv_nav_drawer);
        drawerList.setAdapter(new ArrayAdapter<>(this,
                R.layout.drawer_item_layout, drawerItems));
        drawerList.setOnItemClickListener(new DrawerItemClickListener());
        drawerToggle = new ActionBarDrawerToggle(this, drawerLayout,
                R.string.drawer_open, R.string.drawer_close);
        drawerLayout.setDrawerListener(drawerToggle);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setHomeButtonEnabled(true);
        }
        drawerToggle.syncState();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return drawerToggle.onOptionsItemSelected(item) || super.onOptionsItemSelected(item);
    }

    private void selectItem(int position) {
        Intent intent;
        switch (position) {
            case 0:         //Home
                intent = new Intent(getApplicationContext(), QuickMenuActivity.class);
                startActivity(intent);
                break;
            case 1:         //Patient Search
                intent = new Intent(getApplicationContext(), ServiceUserSearchActivity.class);
                startActivity(intent);
                break;
            case 2:         //Book Appointment
                intent = new Intent(getApplicationContext(), AppointmentTypeSpinnerActivity.class);
                startActivity(intent);
                break;
            case 3:         //TimeRecords
                intent = new Intent(getApplicationContext(), ClinicTimeRecordActivity.class);
                startActivity(intent);
                break;
            case 4:         //Todays Appointments
                //intent = new Intent(getApplicationContext(), TodayAppointmentActivity.class);
                //startActivity(intent);
                break;
            case 5:         //Sync
                basePresenter.getAllAppointments();
                break;
            case 6:         //Logout
                showLogoutDialog();
                break;
            default:
        }
    }

    protected void showLogoutDialog() {
        new AlertDialog.Builder(this)
                .setTitle(R.string.logout_title)
                .setMessage(R.string.logout_dialog_message)
                .setNegativeButton(R.string.no, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialoginterface, int i) {
                    }
                })
                .setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialoginterface, int i) {
                        final Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP |
                                Intent.FLAG_ACTIVITY_CLEAR_TASK |
                                Intent.FLAG_ACTIVITY_NEW_TASK);
                        if (!realm.where(Login.class).findFirst().isLoggedIn()) {
                            startActivity(intent);
                        } else {
                            basePresenter.doLogout(intent);
                        }
                    }
                }).show();
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

    protected int getRecentPregnancy(List<Pregnancy> pregnancyList) {   //TODO: Remove
        return basePresenter.getRecentPregnancy(pregnancyList);
    }

    protected int getRecentBaby(List<Baby> babyList) {                  //TODO: Remove
        return basePresenter.getRecentBaby(babyList);
    }

    public class LogoutService extends Service {
        @Override
        public void onCreate() {
            super.onCreate();
            Timber.d("LogoutService onCreate()");
        }

        public void startTimer(Boolean startTimer) {
            if (startTimer) {
                Timber.d("timer started");
                timerThing();
                timer.start();
            } else {
                if (timer != null) {
                    Timber.d("timer stopped");
                            timer.cancel();
                }
            }
        }

        public void timerThing() {
            timer = new CountDownTimer(300 * 1000, 1000) {
                public void onTick(long millisUntilFinished) {
                }

                public void onFinish() {
                    Timber.d("Call Logout by Service");
                    stopSelf();
                    basePresenter.doLogoutWithoutIntent();
                }
            };
        }

        @Override
        public IBinder onBind(Intent intent) {
            // TODO Auto-generated method stub
            return null;
        }
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
            }, 210);
        }
    }
}