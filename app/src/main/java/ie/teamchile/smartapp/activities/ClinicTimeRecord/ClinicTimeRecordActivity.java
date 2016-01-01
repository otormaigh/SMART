package ie.teamchile.smartapp.activities.ClinicTimeRecord;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.os.Vibrator;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

import ie.teamchile.smartapp.R;
import ie.teamchile.smartapp.activities.Base.BaseActivity;
import ie.teamchile.smartapp.util.AppointmentHelper;
import ie.teamchile.smartapp.util.Constants;
import ie.teamchile.smartapp.util.SharedPrefs;

public class ClinicTimeRecordActivity extends BaseActivity implements ClinicTimeRecordView, OnClickListener, OnItemClickListener, OnItemLongClickListener {
    private List<String> clinicStartedName = new ArrayList<>();
    private List<String> clinicStoppedName = new ArrayList<>();
    private ListView lvNotStarted;
    private ListView lvStarted;
    private ListView lvStopped;
    private String now;
    private String then;
    private String date;
    private int clinicStartedId;
    private int clinicNotStartedId;
    private int clinicStoppedId;
    private Button btnStartClinic;
    private Button btnStopClinic;
    private Button btnResetRecord;
    private ArrayAdapter<String> adapterNotStart;
    private ArrayAdapter<String> adapterStart;
    private ArrayAdapter<String> adapterStop;
    private int recordIdForDelete;
    private int clinicIdForDelete;
    private SharedPrefs sharedPrefs = new SharedPrefs();
    private AppointmentHelper appointmentHelper;
    private Calendar c;
    private ClinicTimeRecordPresenter clinicTimeRecordPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentForNav(R.layout.activity_clinic_time_record);

        initViews();

        clinicTimeRecordPresenter = new ClinicTimeRecordPresenterImp(this, new WeakReference<Activity>(ClinicTimeRecordActivity.this));

        appointmentHelper = new AppointmentHelper(clinicTimeRecordPresenter.getEncryptedRealm());

        disableButtons();

        setActionBarTitle(getString(R.string.title_clinic_time_records));
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void initViews() {
        lvNotStarted = (ListView) findViewById(R.id.lv_clinics_not_started);
        lvNotStarted.setOnItemClickListener(this);
        lvStarted = (ListView) findViewById(R.id.lv_clinics_started);
        lvStarted.setOnItemClickListener(this);
        lvStopped = (ListView) findViewById(R.id.lv_clinics_stopped);
        lvStopped.setOnItemLongClickListener(this);
        btnStartClinic = (Button) findViewById(R.id.btn_start_clinic);
        btnStartClinic.setOnClickListener(this);
        btnStopClinic = (Button) findViewById(R.id.btn_stop_clinic);
        btnStopClinic.setOnClickListener(this);
        btnResetRecord = (Button) findViewById(R.id.btn_reset_clinic_record);
        btnResetRecord.setOnClickListener(this);
    }

    @Override
    public void disableButtons() {
        btnStartClinic.setEnabled(false);
        btnStopClinic.setEnabled(false);
        btnResetRecord.setEnabled(false);
    }

    private String formatClinicName(int clinicId, String clinicName) {
        switch (clinicId) {
            case 6:
                return String.format(Constants.FORMAT_DOMINO, clinicName);
            case 10:
                return String.format(Constants.FORMAT_SATELLITE, clinicName);
            default:
                return clinicName;
        }
    }

    @Override
    public void setNotStartedList() {
        clinicTimeRecordPresenter.updateRecordGetDone(1);
        String clinicName;
        List<String> clinicNotStartedName = new ArrayList<>();
        if (clinicTimeRecordPresenter.getClinicNotStarted().size() == 0) {
            clinicNotStartedName.add(getString(R.string.no_clinics_to_be_started));
            lvNotStarted.setEnabled(false);
        } else {
            lvNotStarted.setEnabled(true);
            int size = clinicTimeRecordPresenter.getClinicNotStarted().size();
            for (int i = 0; i < size; i++) {
                clinicName = clinicTimeRecordPresenter.getClinicIdMap().get(clinicTimeRecordPresenter.getClinicNotStarted().get(i)).getName();
                int clinicId = clinicTimeRecordPresenter.getClinicNotStarted().get(i);
                clinicNotStartedName.add(formatClinicName(clinicId, clinicName));
            }
        }

        adapterNotStart = new ArrayAdapter<>(
                getApplicationContext(),
                android.R.layout.simple_list_item_1,
                clinicNotStartedName);

        lvNotStarted.setAdapter(adapterNotStart);

        adapterNotStart.notifyDataSetChanged();
    }

    @Override
    public void setStartedList() {
        clinicTimeRecordPresenter.updateRecordGetDone(1);
        clinicStartedName = new ArrayList<>();
        if (clinicTimeRecordPresenter.getClinicStarted().size() == 0) {
            clinicStartedName.add(getString(R.string.no_clinics_currently_started));
            lvStarted.setEnabled(false);
        } else {
            lvStarted.setEnabled(true);
            String clinicName;
            int clinicId;
            int size = clinicTimeRecordPresenter.getClinicStarted().size();
            for (int i = 0; i < size; i++) {
                clinicName = clinicTimeRecordPresenter.getClinicIdMap().get(clinicTimeRecordPresenter.getClinicStarted().get(i)).getName();
                clinicId = clinicTimeRecordPresenter.getClinicStarted().get(i);
                clinicStartedName.add(formatClinicName(clinicId, clinicName));
            }
        }

        adapterStart = new ArrayAdapter<>(
                getApplicationContext(),
                android.R.layout.simple_list_item_1,
                clinicStartedName);

        lvStarted.setAdapter(adapterStart);

        adapterStart.notifyDataSetChanged();
    }

    @Override
    public void setStoppedList() {
        clinicTimeRecordPresenter.updateRecordGetDone(1);
        clinicStoppedName = new ArrayList<>();

        if (clinicTimeRecordPresenter.getClinicStopped().size() == 0) {
            clinicStoppedName.add(getString(R.string.no_clinics_currently_stopped));
            lvStopped.setEnabled(false);
        } else {
            lvStopped.setEnabled(true);
            String clinicName;
            int clinicId;
            int size = clinicTimeRecordPresenter.getClinicStopped().size();
            for (int i = 0; i < size; i++) {
                clinicName = clinicTimeRecordPresenter.getClinicIdMap().get(clinicTimeRecordPresenter.getClinicStopped().get(i)).getName();
                clinicId = clinicTimeRecordPresenter.getClinicStopped().get(i);
                clinicStoppedName.add(formatClinicName(clinicId, clinicName));
            }
        }

        adapterStop = new ArrayAdapter<>(
                getApplicationContext(),
                android.R.layout.simple_list_item_1,
                clinicStoppedName);

        lvStopped.setAdapter(adapterStop);

        adapterStop.notifyDataSetChanged();
    }

    private void sortListAlpha(List<String> badList) {
        Collections.sort(badList, new Comparator<String>() {
            @Override
            public int compare(String lhs, String rhs) {
                return lhs.compareToIgnoreCase(rhs);
            }
        });
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        switch (parent.getId()) {
            case R.id.lv_clinics_not_started:
                btnStartClinic.setEnabled(true);
                clinicStartedId = 0;
                adapterStart.notifyDataSetChanged();
                lvStarted.setAdapter(adapterStart);
                clinicNotStartedId = clinicTimeRecordPresenter.getClinicNotStarted().get(position);
                break;
            case R.id.lv_clinics_started:
                btnStopClinic.setEnabled(true);
                clinicNotStartedId = 0;
                adapterNotStart.notifyDataSetChanged();
                lvNotStarted.setAdapter(adapterNotStart);
                clinicStartedId = clinicTimeRecordPresenter.getClinicStarted().get(position);
                break;
        }
    }

    @Override
    public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
        Vibrator vibe = (Vibrator) ClinicTimeRecordActivity.this.getSystemService(Context.VIBRATOR_SERVICE);
        switch (parent.getId()) {
            case R.id.lv_clinics_stopped:
                btnResetRecord.setEnabled(true);
                vibe.vibrate(50);
                clinicIdForDelete = clinicTimeRecordPresenter.getClinicStopped().get(position);
                int size = clinicTimeRecordPresenter.getClinicTimeRecords().size();
                for (int i = 0; i < size; i++) {
                    if (clinicTimeRecordPresenter.getClinicTimeRecords().get(i).getClinicId() == clinicIdForDelete) {
                        recordIdForDelete = clinicTimeRecordPresenter.getClinicTimeRecords().get(i).getId();
                    }
                }
                break;
        }
        return false;
    }

    @Override
    public void onClick(View v) {
        c = Calendar.getInstance();
        now = Constants.DF_DATE_TIME.format(c.getTime());
        c.add(Calendar.HOUR, 1);
        then = Constants.DF_DATE_TIME.format(c.getTime());
        date = Constants.DF_DATE_ONLY.format(c.getTime());

        switch (v.getId()) {
            case R.id.btn_start_clinic:
                if (clinicNotStartedId != 0) {
                    sharedPrefs.overWriteStringPrefs(getApplicationContext(),
                            Constants.CLINIC_STARTED, String.valueOf(clinicNotStartedId));
                    sharedPrefs.addToStringSetPrefs(ClinicTimeRecordActivity.this,
                            Constants.APPTS_GOT, String.valueOf(clinicNotStartedId));
                    clinicTimeRecordPresenter.putStartTime(now, date, clinicNotStartedId);
                    c = Calendar.getInstance();
                    Date todayDate = c.getTime();
                    appointmentHelper.weekDateLooper(todayDate, clinicNotStartedId);
                }
                break;
            case R.id.btn_stop_clinic:
                if (clinicStartedId != 0)
                    clinicTimeRecordPresenter.putEndTime(then, date, clinicStartedId);
                break;
            case R.id.btn_reset_clinic_record:
                if (clinicIdForDelete != 0)
                    clinicTimeRecordPresenter.deleteTimeRecord(clinicIdForDelete, recordIdForDelete);
                break;
        }
        clinicNotStartedId = 0;
        clinicStartedId = 0;
    }
}
