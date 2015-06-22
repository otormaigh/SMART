package ie.teamchile.smartapp.activities;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.daimajia.swipe.SwipeLayout;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import ie.teamchile.smartapp.R;
import ie.teamchile.smartapp.model.ApiRootModel;
import ie.teamchile.smartapp.model.Appointment;
import ie.teamchile.smartapp.model.PostingData;
import ie.teamchile.smartapp.util.SmartApi;
import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class HomeVisitAppointmentActivity extends BaseActivity {
    protected static Date daySelected;
    protected static int visitOptionSelected;
    private final int sdkVersion = Build.VERSION.SDK_INT;
    private Date openingAsDate, closingAsDate;
    private String dateSelectedStr, timeBefore, timeAfter, nameOfClinic;
    private String priority = "home-visit";
    private int dayOfWeek;
    private Calendar c = Calendar.getInstance(), myCalendar = Calendar.getInstance();
    private Intent intent;
    private List<String> nameList = new ArrayList<>();
    private List<String> gestList = new ArrayList<>();
    private List<Integer> idList = new ArrayList<>();
    private List<Boolean> attendedList = new ArrayList<>();
    private Button dateInList, btnPrevWeek, btnNextWeek;
    private BaseAdapter adapter;
    private ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentForNav(R.layout.activity_home_visit_appointment);

        dateInList = (Button) findViewById(R.id.btn_date);
        listView = (ListView) findViewById(R.id.lv_home_visit);
        btnPrevWeek = (Button) findViewById(R.id.btn_prev);
        btnPrevWeek.setOnClickListener(new ButtonClick());
        btnNextWeek = (Button) findViewById(R.id.btn_next);
        btnNextWeek.setOnClickListener(new ButtonClick());

        c.setTime(daySelected);
        dayOfWeek = c.get(Calendar.DAY_OF_WEEK);

        listView.setAdapter(null);
        newSetToList(daySelected);
        adapter.notifyDataSetChanged();
        createDatePicker();
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d("bugs", "in onResume");
        Log.d("bugs", "daySelected: " + daySelected);
        listView.setAdapter(null);
        adapter.notifyDataSetChanged();
        newSetToList(daySelected);
    }

    public void pauseButton() {
        btnNextWeek.setEnabled(false);
        btnPrevWeek.setEnabled(false);
        CountDownTimer nextTimer = new CountDownTimer(250, 250) {
            @Override
            public void onTick(long millisUntilFinished) {
            }

            @Override
            public void onFinish() {
                btnNextWeek.setEnabled(true);
                btnPrevWeek.setEnabled(true);
            }
        };
        nextTimer.start();
    }

    public List<Integer> removeZeros(List<Integer> badList) {
        if (badList != null)
            for (int i = 0; i < badList.size(); i++)
                if (badList.get(i).equals(0))
                    badList.remove(i);
        return badList;
    }

    private void createDatePicker() {
        myCalendar.setTime(daySelected);
        final DatePickerDialog.OnDateSetListener pickerDate = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                if (pd != null) {
                    pd.dismiss();
                }
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, monthOfYear);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                Log.d("postAppointment", "datePicker: " + myCalendar.getTime());
                Log.d("postAppointment", "datePicker formatted: " +
                        dfDateOnly.format(myCalendar.getTime()));
                Log.d("bugs", "c.getDay: " + dayOfWeek);
                Log.d("bugs", "myCalendar.getDay: " + myCalendar.get(Calendar.DAY_OF_WEEK));
                if (myCalendar.get(Calendar.DAY_OF_WEEK) == dayOfWeek) {
                    dateInList.setText(dfDateWMonthName.format(myCalendar.getTime()));
                    newSetToList(myCalendar.getTime());
                } else {
                    Log.d("bugs", "wrong date");
                    showProgressDialog(HomeVisitAppointmentActivity.this,
                            "Invalid day selected\nPlease choose another");
                    Handler mHandler = new Handler();
                    mHandler.postDelayed(new Runnable() {
                        public void run() {
                            pd.dismiss();
                            Log.d("bugs", "dismissed");
                            Log.d("bugs", "dismissed" + pd.isShowing());
                        }
                    }, 2000);
                }
            }
        };
        dateInList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new DatePickerDialog(HomeVisitAppointmentActivity.this, pickerDate, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });
    }

    private void newSetToList(Date dateSelected) {
        nameList = new ArrayList<>();
        gestList = new ArrayList<>();
        attendedList = new ArrayList<>();
        idList = new ArrayList<>();

        daySelected = dateSelected;

        dateSelectedStr = dfDateOnly.format(dateSelected);
        dateInList.setText(dfDateWMonthName.format(dateSelected));
        nameOfClinic = ApiRootModel.getInstance().getServiceOptionsHomeMap().get(visitOptionSelected).getName();
        setActionBarTitle(nameOfClinic);

        nameList.add("Free Slot");
        gestList.add("");
        attendedList.add(false);

        if (ApiRootModel.getInstance().getHomeVisitOptionDateApptIdMap().containsKey(visitOptionSelected)) {
            if(ApiRootModel.getInstance().getHomeVisitOptionDateApptIdMap().get(visitOptionSelected).containsKey(dateSelectedStr)) {
                idList = ApiRootModel.getInstance().getHomeVisitOptionDateApptIdMap().get(visitOptionSelected).get(dateSelectedStr);
                removeZeros(idList);
                for (int i = 0; i < idList.size(); i++) {
                    if (idList.get(i) != 0) {
                        Appointment appointment = ApiRootModel.getInstance().getHomeVisitIdApptMap().get(idList.get(i));
                        nameList.add(appointment.getServiceUser().getName());
                        gestList.add(appointment.getServiceUser().getGestation());

                        if (appointment.getAttended() == null)
                            attendedList.add(false);
                        else
                            attendedList.add(appointment.getAttended());
                    }
                }
            }
            idList.add(0, 0);
        } else {
            idList = new ArrayList<>();
            idList.add(0);
        }

        Log.d("bugs", "idList = " + idList);
        Log.d("bugs", "nameSingle = " + nameList);
        Log.d("bugs", "gestSingle = " + gestList);
        Log.d("bugs", "attendedSingle = " + attendedList);

        adapter = new ListElementAdapter(HomeVisitAppointmentActivity.this,
                idList, nameList, gestList, attendedList);
        adapter.notifyDataSetChanged();
        listView.setAdapter(adapter);
        adapter.notifyDataSetChanged();
    }

    private void changeAttendStatus(Boolean status, int position) {
        showProgressDialog(HomeVisitAppointmentActivity.this, "Changing Attended Status");
        attendedList.set(position, status);
        RestAdapter restAdapter = new RestAdapter.Builder()
                .setEndpoint(SmartApi.BASE_URL)
                .setLogLevel(RestAdapter.LogLevel.FULL)
                .build();

        api = restAdapter.create(SmartApi.class);

        PostingData attendedStatus = new PostingData();
        attendedStatus.putAppointmentStatus(
                status,
                0,
                ApiRootModel.getInstance().getLogin().getId(),
                ApiRootModel.getInstance().getHomeVisitIdApptMap().get(idList.get(position)).getServiceUserId());

        api.putAppointmentStatus(
                attendedStatus,
                idList.get(position),
                ApiRootModel.getInstance().getLogin().getToken(),
                SmartApi.API_KEY,
                new Callback<ApiRootModel>() {
                    @Override
                    public void success(ApiRootModel apiRootModel, Response response) {
                        Toast.makeText(HomeVisitAppointmentActivity.this,
                                "status changed", Toast.LENGTH_LONG).show();
                        pd.dismiss();
                    }

                    @Override
                    public void failure(RetrofitError error) {
                        Log.d("Attended", "retro error = " + error);
                        pd.dismiss();
                    }
                }
        );
    }

    private void searchServiceUser(int serviceUserId, final Intent intent) {
        api.getServiceUserById(serviceUserId,
                ApiRootModel.getInstance().getLogin().getToken(),
                SmartApi.API_KEY,
                new Callback<ApiRootModel>() {
                    @Override
                    public void success(ApiRootModel apiRootModel, Response response) {
                        ApiRootModel.getInstance().setServiceUsers(apiRootModel.getServiceUsers());
                        ApiRootModel.getInstance().setBabies(apiRootModel.getBabies());
                        ApiRootModel.getInstance().setPregnancies(apiRootModel.getPregnancies());
                        startActivity(intent);
                        pd.dismiss();
                    }

                    @Override
                    public void failure(RetrofitError error) {
                        pd.dismiss();
                        Toast.makeText(
                                HomeVisitAppointmentActivity.this,
                                "Error Search Patient: " + error,
                                Toast.LENGTH_LONG).show();
                    }
                });
    }

    public void setDaySelected(Date daySelected) {
        HomeVisitAppointmentActivity.daySelected = daySelected;
    }

    public void setVisitOption(int visitOptionSelected) {
        HomeVisitAppointmentActivity.visitOptionSelected = visitOptionSelected;
    }

    private class ButtonClick implements View.OnClickListener {
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.btn_prev:
                    c.setTime(daySelected);
                    c.add(Calendar.DAY_OF_YEAR, -7);
                    daySelected = c.getTime();
                    myCalendar.setTime(daySelected);
                    createDatePicker();
                    listView.setAdapter(null);
                    newSetToList(c.getTime());
                    pauseButton();
                    break;
                case R.id.btn_next:
                    c.setTime(daySelected);
                    c.add(Calendar.DAY_OF_YEAR, 7);
                    daySelected = c.getTime();
                    myCalendar.setTime(daySelected);
                    createDatePicker();
                    listView.setAdapter(null);
                    newSetToList(c.getTime());
                    pauseButton();
                    break;
            }
        }
    }

    private class ListElementAdapter extends BaseAdapter {
        LayoutInflater layoutInflater;
        List<Integer> apptId;
        List<String> aptName, aptGest;
        List<Boolean> attendedList;

        public ListElementAdapter(Context context,
                                  List<Integer> apptId,
                                  List<String> aptName,
                                  List<String> aptGest,
                                  List<Boolean> attendedList) {
            super();
            Log.d("MYLOG", "daySelected: " + daySelected);
            Log.d("MYLOG", "List Adapter Called");
            this.apptId = apptId;
            this.aptName = aptName;
            this.aptGest = aptGest;
            this.attendedList = attendedList;
            layoutInflater = LayoutInflater.from(context);
        }

        @Override
        public void notifyDataSetChanged() {
            super.notifyDataSetChanged();
        }

        @Override
        public int getCount() {
            return aptName.size();
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            convertView = layoutInflater.inflate(R.layout.list_home_visit_appointment_layout, null);
            TextView nameText = (TextView) convertView.findViewById(R.id.tv_name);
            TextView gestText = (TextView) convertView.findViewById(R.id.tv_gestation);
            final Button btnChangeStatus = (Button) convertView.findViewById(R.id.btn_change_status);
            final ImageView ivAttend = (ImageView) convertView.findViewById(R.id.img_attended);
            final SwipeLayout swipeLayout = (SwipeLayout) convertView.findViewById(R.id.swipe_home_visit_appt);
            LinearLayout llApptListItem = (LinearLayout) convertView.findViewById(R.id.ll_appt_list_item);
            llApptListItem.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    if (apptId.get(position).equals(0)) {
                        intent = new Intent(HomeVisitAppointmentActivity.this, CreateAppointmentActivity.class);
                        intent.putExtra("from", "home-visit");
                        intent.putExtra("serviceOptionId", String.valueOf(visitOptionSelected));
                        intent.putExtra("clinicID", "0");
                        startActivity(intent);
                    } else {
                        int serviceUserId = ApiRootModel.getInstance().getHomeVisitIdApptMap().get(apptId.get(position)).getServiceUserId();
                        Log.d("bugs", "db string: " + "service_users" + "/" + serviceUserId);
                        showProgressDialog(HomeVisitAppointmentActivity.this,
                                "Fetching Information");
                        intent = new Intent(HomeVisitAppointmentActivity.this, ServiceUserActivity.class);
                        searchServiceUser(serviceUserId, intent);
                    }
                    return true;
                }
            });

            btnChangeStatus.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.d("Button", "position = " + position);
                    swipeLayout.close();
                    if (!attendedList.get(position)) {
                        changeAttendStatus(true, position);
                        ivAttend.setBackgroundResource(R.color.attended);
                        notifyDataSetChanged();
                    } else if (attendedList.get(position)) {
                        changeAttendStatus(false, position);
                        ivAttend.setBackgroundResource(R.color.unattended);
                        notifyDataSetChanged();
                    }
                }
            });

            if (apptId.get(position).equals(0)) {
                nameText.setText(aptName.get(position));
                gestText.setText(aptGest.get(position));
                swipeLayout.setSwipeEnabled(false);

                nameText.setTextColor(getResources().getColor(R.color.free_slot));
                nameText.setTypeface(null, Typeface.ITALIC);
            } else {
                nameText.setText(aptName.get(position));
                gestText.setText(aptGest.get(position));
                swipeLayout.setSwipeEnabled(true);

                if (attendedList.get(position)) {
                    ivAttend.setBackgroundResource(R.color.attended);
                    btnChangeStatus.setText("No");
                    //btnChangeStatus.setEnabled(false);
                } else if (!attendedList.get(position)) {
                    ivAttend.setBackgroundResource(R.color.unattended);
                    btnChangeStatus.setText("Yes");
                    //btnChangeStatus.setEnabled(true);
                }
            }
            return convertView;
        }
    }
}