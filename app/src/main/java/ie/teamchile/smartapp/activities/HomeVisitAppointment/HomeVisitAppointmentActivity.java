package ie.teamchile.smartapp.activities.HomeVisitAppointment;

import android.app.Activity;
import android.app.DatePickerDialog;
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

import com.daimajia.swipe.SwipeLayout;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

import ie.teamchile.smartapp.R;
import ie.teamchile.smartapp.activities.Base.BaseActivity;
import ie.teamchile.smartapp.activities.CreateAppointment.CreateAppointmentActivity;
import ie.teamchile.smartapp.activities.ServiceUser.ServiceUserActivity;
import ie.teamchile.smartapp.model.RealmInteger;
import ie.teamchile.smartapp.model.ResponseAppointment;
import ie.teamchile.smartapp.model.ResponseServiceOption;
import ie.teamchile.smartapp.model.ResponseServiceUser;
import ie.teamchile.smartapp.util.Constants;
import io.realm.Realm;
import timber.log.Timber;

public class HomeVisitAppointmentActivity extends BaseActivity implements HomeVisitAppointmentView {
    public static Date daySelected;
    public static int visitOptionSelected;
    private Calendar c = Calendar.getInstance(), myCalendar = Calendar.getInstance();
    private Intent intent;
    private Button dateInList, btnPrevWeek, btnNextWeek;
    private BaseAdapter adapter;
    private Realm realm;
    private HomeVisitAppointmentPresenter presenter;
    private List<Integer> idList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentForNav(R.layout.activity_home_visit_appointment);

        initViews();

        presenter = new HomeVisitAppointmentPresenterImp(this, new WeakReference<Activity>(HomeVisitAppointmentActivity.this));

        realm = presenter.getEncryptedRealm();

        c.setTime(daySelected);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
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

    @Override
    public void initViews() {
        dateInList = (Button) findViewById(R.id.btn_date);
        dateInList.setOnClickListener(new ButtonClick());
        btnPrevWeek = (Button) findViewById(R.id.btn_prev);
        btnPrevWeek.setOnClickListener(new ButtonClick());
        btnNextWeek = (Button) findViewById(R.id.btn_next);
        btnNextWeek.setOnClickListener(new ButtonClick());

        adapter = new ListElementAdapter();
        ((ListView) findViewById(R.id.lv_home_visit)).setAdapter(adapter);
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
        DatePickerDialog.OnDateSetListener pickerDate = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                presenter.dismissProgressDialog();
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, monthOfYear);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                dateInList.setText(Constants.DF_DATE_W_MONTH_NAME.format(myCalendar.getTime()));
                newSetToList(myCalendar.getTime());
            }
        };
        new DatePickerDialog(HomeVisitAppointmentActivity.this, pickerDate, myCalendar
                .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                myCalendar.get(Calendar.DAY_OF_MONTH)).show();
    }

    private void newSetToList(Date dateSelected) {
        idList = new ArrayList<>();

        daySelected = dateSelected;

        String dateSelectedStr = Constants.DF_DATE_ONLY.format(dateSelected);
        dateInList.setText(Constants.DF_DATE_W_MONTH_NAME.format(dateSelected));
        String nameOfClinic = realm.where(ResponseServiceOption.class).equalTo(Constants.REALM_ID, visitOptionSelected).findFirst().getName();
        setActionBarTitle(nameOfClinic);

        List<ResponseAppointment> responseAppointmentList = realm.where(ResponseAppointment.class)
                .equalTo(Constants.REALM_DATE, dateSelectedStr)
                .equalTo(Constants.REALM_CLINIC_ID, 0)
                .findAll();

        if (!responseAppointmentList.isEmpty()) {
            for (ResponseAppointment responseAppointment : responseAppointmentList) {
                for (RealmInteger id : responseAppointment.getServiceOptionIds()) {
                    if (id.getValue() == visitOptionSelected)
                        idList.add(responseAppointment.getId());
                }
            }

            if (!idList.isEmpty()) {
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
                    newSetToList(c.getTime());
                    pauseButton();
                    break;
                case R.id.btn_next:
                    c.setTime(daySelected);
                    c.add(Calendar.DAY_OF_YEAR, 1);
                    daySelected = c.getTime();
                    myCalendar.setTime(daySelected);
                    newSetToList(c.getTime());
                    pauseButton();
                    break;
                case R.id.btn_date:
                    createDatePicker();
                    break;
            }
        }
    }

    private class ListElementAdapter extends BaseAdapter {
        private ViewHolder holder;
        private boolean attended = false;

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

        private ResponseAppointment getAppointment(int position) {
            return realm.where(ResponseAppointment.class).equalTo(Constants.REALM_ID, getItem(position)).findFirst();
        }

        private ResponseServiceUser getServiceUser(int serviceUserId) {
            return realm.where(ResponseServiceUser.class)
                    .equalTo(Constants.REALM_ID, serviceUserId).findFirst();
        }

        private String getUserName(int position) {
            /*if (getAppointment(position).getServiceUser().getName() != null)
                return getAppointment(position).getServiceUser().getName();
            else if (realm.where(ResponseServiceUser.class)
                    .equalTo(
                            Constants.REALM_ID,
                            getAppointment(position).getServiceUserId()).findFirst().getName() != null)
                return getServiceUser(getAppointment(position).getServiceUserId()).getName();
            else
                return "null";*/
            return getServiceUser(getAppointment(position).getServiceUserId()).getName();
        }

        private String getUserGestation(int position) {
            if (getAppointment(position).getServiceUser().getGestation() != null)
                return getAppointment(position).getServiceUser().getName();
            else
                return "null";
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            if (convertView == null) {
                LayoutInflater layoutInflater = LayoutInflater.from(HomeVisitAppointmentActivity.this);
                convertView = layoutInflater.inflate(R.layout.list_layout_home_visit_appointment, null);
                holder = new ViewHolder(convertView);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }

            if (getItem(position) == 0) {
                holder.nameText.setText("Book Home Visit");
                holder.gestText.setText(null);
                attended = false;
                holder.swipeLayout.setSwipeEnabled(false);
                holder.nameText.setTextColor(getResources().getColor(R.color.free_slot));
                holder.nameText.setTypeface(null, Typeface.ITALIC);
            } else {
                Timber.wtf("appointment = " + getAppointment(position));
                Timber.wtf("serviceUser = " + getAppointment(position).getServiceUser());
                Timber.wtf("username = " + getUserName(position));

                holder.nameText.setText(getUserName(position));
                holder.gestText.setText(getUserGestation(position));
                attended = getAppointment(position).isAttended();
                holder.swipeLayout.setSwipeEnabled(true);
                holder.nameText.setTextColor(holder.gestText.getTextColors().getDefaultColor());
                holder.nameText.setTypeface(null, Typeface.NORMAL);
            }

            if (attended) {
                holder.ivAttend.setBackgroundResource(R.color.attended);
                holder.btnChangeStatus.setText(getString(R.string.no));
            } else {
                holder.ivAttend.setBackgroundResource(R.color.unattended);
                holder.btnChangeStatus.setText(getString(R.string.yes));
            }

            holder.llApptListItem.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    if (getItem(position).equals(0)) {
                        intent = new Intent(HomeVisitAppointmentActivity.this, CreateAppointmentActivity.class);
                        intent.putExtra(Constants.ARGS_FROM, Constants.ARGS_HOME_VISIT);
                        intent.putExtra(Constants.ARGS_SERVICE_OPTION_ID, String.valueOf(visitOptionSelected));
                        startActivity(intent);
                    } else {
                        int serviceUserId = getAppointment(position).getServiceUserId();
                        intent = new Intent(HomeVisitAppointmentActivity.this, ServiceUserActivity.class);
                        presenter.searchServiceUser(serviceUserId, intent);
                    }
                    return true;
                }
            });

            holder.btnChangeStatus.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    holder.swipeLayout.close();

                    presenter.changeAttendStatus(!attended, idList.get(position));
                    realm.beginTransaction();
                    getAppointment(position).setAttended(!attended);
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