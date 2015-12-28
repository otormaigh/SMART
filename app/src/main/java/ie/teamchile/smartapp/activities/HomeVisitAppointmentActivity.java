package ie.teamchile.smartapp.activities;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.CountDownTimer;
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

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

import ie.teamchile.smartapp.R;
import ie.teamchile.smartapp.activities.Base.BaseActivity;
import ie.teamchile.smartapp.activities.ServiceUser.ServiceUserActivity;
import ie.teamchile.smartapp.api.SmartApiClient;
import ie.teamchile.smartapp.model.Appointment;
import ie.teamchile.smartapp.model.BaseResponseModel;
import ie.teamchile.smartapp.model.Login;
import ie.teamchile.smartapp.model.PostingData;
import ie.teamchile.smartapp.model.RealmInteger;
import ie.teamchile.smartapp.model.ServiceOption;
import ie.teamchile.smartapp.util.Constants;
import ie.teamchile.smartapp.util.CustomDialogs;
import io.realm.Realm;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;
import timber.log.Timber;

public class HomeVisitAppointmentActivity extends BaseActivity {
    protected static Date daySelected;
    protected static int visitOptionSelected;
    private String dateSelectedStr, nameOfClinic;
    private int dayOfWeek;
    private Calendar c = Calendar.getInstance(), myCalendar = Calendar.getInstance();
    private Intent intent;
    private List<Integer> idList = new ArrayList<>();
    private Button dateInList, btnPrevWeek, btnNextWeek;
    private BaseAdapter adapter;
    private ListView listView;
    private ProgressDialog pd;
    private Realm realm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentForNav(R.layout.activity_home_visit_appointment);

        realm = Realm.getInstance(this);

        dateInList = (Button) findViewById(R.id.btn_date);
        listView = (ListView) findViewById(R.id.lv_home_visit);
        btnPrevWeek = (Button) findViewById(R.id.btn_prev);
        btnPrevWeek.setOnClickListener(new ButtonClick());
        btnNextWeek = (Button) findViewById(R.id.btn_next);
        btnNextWeek.setOnClickListener(new ButtonClick());

        c.setTime(daySelected);
        dayOfWeek = c.get(Calendar.DAY_OF_WEEK);

        idList = new ArrayList<>();
        adapter = new ListElementAdapter();
        listView.setAdapter(adapter);

        newSetToList(daySelected);
        createDatePicker();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        if (realm != null)
            realm.close();
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
    }

    @Override
    protected void onResume() {
        super.onResume();
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
                dateInList.setText(Constants.DF_DATE_W_MONTH_NAME.format(myCalendar.getTime()));
                newSetToList(myCalendar.getTime());
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
        idList = new ArrayList<>();

        daySelected = dateSelected;

        dateSelectedStr = Constants.DF_DATE_ONLY.format(dateSelected);
        dateInList.setText(Constants.DF_DATE_W_MONTH_NAME.format(dateSelected));
        nameOfClinic = realm.where(ServiceOption.class).equalTo(Constants.REALM_ID, visitOptionSelected).findFirst().getName();
        setActionBarTitle(nameOfClinic);

        List<Appointment> appointmentList = realm.where(Appointment.class)
                .equalTo(Constants.REALM_DATE, dateSelectedStr)
                .findAll();

        if (!appointmentList.isEmpty()) {
            for (Appointment appointment : appointmentList) {
                for (RealmInteger id : appointment.getServiceOptionIds()) {
                    if (id.getValue() == visitOptionSelected)
                        idList.add(appointment.getId());
                }
            }

            if (idList.size() == 0) {
                idList.add(0);
            } else {
                Collections.sort(idList, new Comparator<Integer>() {
                    @Override
                    public int compare(Integer a, Integer b) {
                        int valA;
                        int valB;

                        valA = a;
                        valB = b;

                        return ((Integer) valA).compareTo(valB);
                    }
                });
            }
        }

        idList.add(0);
        adapter.notifyDataSetChanged();
    }

    private void changeAttendStatus(Boolean status, int position) {
        pd = new CustomDialogs().showProgressDialog(
                HomeVisitAppointmentActivity.this,
                "Changing Attended Status");

        PostingData attendedStatus = new PostingData();
        attendedStatus.putAppointmentStatus(
                status,
                0,
                realm.where(Login.class).findFirst().getId(),
                realm.where(Appointment.class).equalTo(Constants.REALM_ID, idList.get(position)).findFirst().getServiceUserId());

        SmartApiClient.getAuthorizedApiClient(this).putAppointmentStatus(
                attendedStatus,
                idList.get(position),
                new Callback<BaseResponseModel>() {
                    @Override
                    public void success(BaseResponseModel baseResponseModel, Response response) {
                        Timber.d("changeAttendStatus success");

                        realm.beginTransaction();
                        realm.copyToRealmOrUpdate(baseResponseModel.getAppointment());
                        realm.commitTransaction();

                        Toast.makeText(HomeVisitAppointmentActivity.this,
                                "Status changed", Toast.LENGTH_LONG).show();
                        pd.dismiss();
                    }

                    @Override
                    public void failure(RetrofitError error) {
                        Timber.d("changeAttendStatus error = " + error);
                        pd.dismiss();
                    }
                }
        );
    }

    private void searchServiceUser(int serviceUserId, final Intent intent) {
        SmartApiClient.getAuthorizedApiClient(this).getServiceUserById(serviceUserId,
                new Callback<BaseResponseModel>() {
                    @Override
                    public void success(BaseResponseModel baseResponseModel, Response response) {
                        realm.beginTransaction();
                        realm.copyToRealmOrUpdate(baseResponseModel.getServiceUsers());
                        realm.copyToRealmOrUpdate(baseResponseModel.getBabies());
                        realm.copyToRealmOrUpdate(baseResponseModel.getPregnancies());
                        realm.commitTransaction();
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
                    c.add(Calendar.DAY_OF_YEAR, -1);
                    daySelected = c.getTime();
                    myCalendar.setTime(daySelected);
                    createDatePicker();
                    newSetToList(c.getTime());
                    pauseButton();
                    break;
                case R.id.btn_next:
                    c.setTime(daySelected);
                    c.add(Calendar.DAY_OF_YEAR, 1);
                    daySelected = c.getTime();
                    myCalendar.setTime(daySelected);
                    createDatePicker();
                    newSetToList(c.getTime());
                    pauseButton();
                    break;
            }
        }
    }

    private class ListElementAdapter extends BaseAdapter {
        @Override
        public int getCount() {
            return idList.size();
        }

        @Override
        public Integer getItem(int position) {
            return idList.get(position);
        }

        @Override
        public long getItemId(int position) {
            return idList.get(position).hashCode();
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            final Appointment appointment = realm.where(Appointment.class).equalTo(Constants.REALM_ID, getItem(position)).findFirst();
            final ViewHolder holder;
            final Boolean attended;

            if (convertView == null) {
                LayoutInflater layoutInflater = LayoutInflater.from(HomeVisitAppointmentActivity.this);
                convertView = layoutInflater.inflate(R.layout.list_layout_home_visit_appointment, null);
                holder = new ViewHolder(convertView);
                convertView.setTag(holder);
            } else
                holder = (ViewHolder) convertView.getTag();

            if(getItem(position) == 0){
                holder.nameText.setText("Book Home Visit");
                holder.gestText.setText("");
                attended = false;
                holder.swipeLayout.setSwipeEnabled(false);
                holder.nameText.setTextColor(getResources().getColor(R.color.free_slot));
                holder.nameText.setTypeface(null, Typeface.ITALIC);
            } else {
                holder.nameText.setText(appointment.getServiceUser().getName());
                holder.gestText.setText(appointment.getServiceUser().getGestation());
                attended = appointment.isAttended();
                holder.swipeLayout.setSwipeEnabled(true);
                holder.nameText.setTextColor(holder.gestText.getTextColors().getDefaultColor());
                holder.nameText.setTypeface(null, Typeface.NORMAL);
            }

            if (attended) {
                holder.ivAttend.setBackgroundResource(R.color.attended);
                holder.btnChangeStatus.setText("No");
            } else {
                holder.ivAttend.setBackgroundResource(R.color.unattended);
                holder.btnChangeStatus.setText("Yes");
            }

            holder.llApptListItem.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    if (getItem(position).equals(0)) {
                        intent = new Intent(HomeVisitAppointmentActivity.this, CreateAppointmentActivity.class);
                        intent.putExtra("from", "home-visit");
                        intent.putExtra("serviceOptionId", String.valueOf(visitOptionSelected));
                        startActivity(intent);
                    } else {
                        int serviceUserId = appointment.getServiceUserId();
                        pd = new CustomDialogs().showProgressDialog(
                                HomeVisitAppointmentActivity.this,
                                "Fetching Information");
                        intent = new Intent(HomeVisitAppointmentActivity.this, ServiceUserActivity.class);
                        searchServiceUser(serviceUserId, intent);
                    }
                    return true;
                }
            });

            holder.btnChangeStatus.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    holder.swipeLayout.close();

                    changeAttendStatus(!attended, position);
                    realm.beginTransaction();
                    appointment.setAttended(!attended);
                    realm.commitTransaction();
                    notifyDataSetChanged();
                }
            });

            return convertView;
        }

        private class ViewHolder {
            private TextView nameText;
            private TextView gestText;
            private Button btnChangeStatus;
            private ImageView ivAttend;
            private SwipeLayout swipeLayout;
            private LinearLayout llApptListItem;

            public ViewHolder(View view) {
                nameText = (TextView) view.findViewById(R.id.tv_name);
                gestText = (TextView) view.findViewById(R.id.tv_gestation);
                btnChangeStatus = (Button) view.findViewById(R.id.btn_change_status);
                ivAttend = (ImageView) view.findViewById(R.id.img_attended);
                swipeLayout = (SwipeLayout) view.findViewById(R.id.swipe_home_visit_appt);
                llApptListItem = (LinearLayout) view.findViewById(R.id.ll_appt_list_item);
            }
        }
    }
}