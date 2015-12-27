package ie.teamchile.smartapp.activities;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Vibrator;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import ie.teamchile.smartapp.R;
import ie.teamchile.smartapp.api.SmartApiClient;
import ie.teamchile.smartapp.model.BaseModel;
import ie.teamchile.smartapp.model.Clinic;
import ie.teamchile.smartapp.model.ClinicTimeRecord;
import ie.teamchile.smartapp.model.PostingData;
import ie.teamchile.smartapp.util.AppointmentHelper;
import ie.teamchile.smartapp.util.Constants;
import ie.teamchile.smartapp.util.CustomDialogs;
import ie.teamchile.smartapp.util.GeneralUtils;
import ie.teamchile.smartapp.util.SharedPrefs;
import io.realm.Realm;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;
import timber.log.Timber;

public class ClinicTimeRecordActivity extends BaseActivity implements OnClickListener, OnItemClickListener, OnItemLongClickListener {
    private List<Integer> clinicStopped = new ArrayList<>();
    private List<Integer> clinicStarted = new ArrayList<>();
    private List<Integer> clinicNotStarted = new ArrayList<>();
    private List<Integer> idList = new ArrayList<>();
    private List<String> clinicNotStartedName = new ArrayList<>();
    private List<String> clinicStartedName = new ArrayList<>();
    private List<String> clinicStoppedName = new ArrayList<>();
    private Map<String, List<Integer>> clinicDayMap = new HashMap<>();
    private Map<Integer, Clinic> clinicIdMap = new HashMap<>();
    private List<ClinicTimeRecord> clinicTimeRecords = new ArrayList<>();
    private ListView lvNotStarted;
    private ListView lvStarted;
    private ListView lvStopped;
    private String todayDay;
    private String todayDate;
    private String now;
    private String then;
    private String date;
    private int clinicStartedId;
    private int clinicNotStartedId;
    private int clinicStoppedId;
    private Button btnStartClinic;
    private Button btnStopClinic;
    private Button btnResetRecord;
    private ArrayAdapter adapterNotStart;
    private ArrayAdapter adapterStart;
    private ArrayAdapter adapterStop;
    private int recordId = 0;
    private int recordIdForDelete;
    private int clinicIdForDelete;
    private int recordGetDone;
    private SharedPrefs sharedPrefs = new SharedPrefs();
    private AppointmentHelper appointmentHelper;
    private ProgressDialog pd;
    private Realm realm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentForNav(R.layout.activity_clinic_time_record);

        realm = Realm.getInstance(this);

        appointmentHelper = new AppointmentHelper(realm);

        c = Calendar.getInstance();
        todayDay = dfDayLong.format(c.getTime());
        todayDate = dfDateOnly.format(c.getTime());
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

        disableButtons();

        setActionBarTitle(getString(R.string.title_clinic_time_records));

        if (!todayDay.equals(getString(R.string.saturday)) && !todayDay.equals(getString(R.string.sunday))) {
            getDataFromDb();
        } else {
            new CustomDialogs().showWarningDialog(this, getString(R.string.no_clinics_opened));
        }
    }

    @Override
    protected void onPause() {
        super.onPause();

        realm.beginTransaction();
        realm.copyToRealmOrUpdate(clinicTimeRecords);
        realm.commitTransaction();

        BaseModel.getInstance().setClinicTimeRecords(clinicTimeRecords);
        BaseModel.getInstance().setClinicStopped(clinicStopped);
        BaseModel.getInstance().setClinicStarted(clinicStarted);
        BaseModel.getInstance().setClinicNotStarted(clinicNotStarted);
        BaseModel.getInstance().setClinicMap(clinicIdMap);
        BaseModel.getInstance().setClinicDayMap(clinicDayMap);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        if (realm != null)
            realm.close();
    }

    private void disableButtons() {
        btnStartClinic.setEnabled(false);
        btnStopClinic.setEnabled(false);
        btnResetRecord.setEnabled(false);
    }

    private void getDataFromDb() {
        recordGetDone = 0;

        List<Clinic> clinics = realm.where(Clinic.class).findAll();
        List<String> trueDays;
        Clinic clinic;
        int clinicId;

        int clinicSize = clinics.size();
        int trueDaySize;
        for (int i = 0; i < clinicSize; i++) {
            trueDays = new GeneralUtils().getTrueDays(
                    clinics.get(i).getDays());
            trueDaySize = trueDays.size();
            for (int j = 0; j < trueDaySize; j++) {
                clinic = clinics.get(i);
                clinicId = clinics.get(i).getId();
                clinicIdMap.put(clinicId, clinic);

                if (clinicDayMap.get(trueDays.get(j)) != null) {
                    idList = new ArrayList<>();
                    idList = clinicDayMap.get(trueDays.get(j));
                    idList.add(clinicId);
                    clinicDayMap.put(trueDays.get(j), idList);
                } else {
                    idList = new ArrayList<>();
                    idList.add(clinicId);
                    clinicDayMap.put(trueDays.get(j), idList);
                }
            }
        }
        idList = clinicDayMap.get(todayDay);

        if (clinicDayMap.containsKey(todayDay)) {
            pd = new CustomDialogs().showProgressDialog(
                    ClinicTimeRecordActivity.this,
                    getString(R.string.updating_time_records));
            int size = idList.size();
            for (int i = 0; i < size; i++) {
                getTimeRecords(idList.get(i), todayDate);
            }
        } else {
            setNotStartedList();
            setStartedList();
            setStoppedList();
        }
        timer = new CountDownTimer(200, 100) {
            @Override
            public void onTick(long millisUntilFinished) {
            }

            @Override
            public void onFinish() {
                if (pd != null) {
                    if (recordGetDone >= 3)
                        pd.dismiss();
                    else
                        timer.start();
                }
            }
        }.start();
    }

    private void getTimeRecords(final int clinicId, String date) {
        SmartApiClient.getAuthorizedApiClient(this).getTimeRecords(
                clinicId,
                date,
                new Callback<BaseModel>() {
                    @Override
                    public void success(BaseModel baseModel, Response response) {
                        Timber.d("get time record success");
                        if (baseModel.getClinicTimeRecords().size() > 0) {
                            clinicTimeRecords.add(baseModel.getClinicTimeRecords().get(0));
                            if (baseModel.getClinicTimeRecords().get(0).getEndTime() == null) {
                                if (!clinicStarted.contains(clinicId))
                                    clinicStarted.add(clinicId);
                            } else if (baseModel.getClinicTimeRecords().get(0).getEndTime() == null &&
                                    baseModel.getClinicTimeRecords().get(0).getStartTime() == null) {
                                clinicNotStarted.add(clinicId);
                            } else {
                                if (!clinicStopped.contains(clinicId))
                                    clinicStopped.add(clinicId);
                            }
                        } else {
                            if (!clinicNotStarted.contains(clinicId))
                                clinicNotStarted.add(clinicId);
                        }

                        setNotStartedList();
                        setStartedList();
                        setStoppedList();
                    }

                    @Override
                    public void failure(RetrofitError error) {
                        Timber.d("get time record failure = " + error);
                    }
                }
        );
    }

    private void putStartTime(String now, final int clinicId) {
        PostingData timeRecord = new PostingData();
        timeRecord.putStartTimeRecord(
                now,
                clinicId,
                date);

        pd = new CustomDialogs().showProgressDialog(
                ClinicTimeRecordActivity.this,
                getString(R.string.updating_time_records));

        SmartApiClient.getAuthorizedApiClient(this).postTimeRecords(
                timeRecord,
                clinicId,
                new Callback<BaseModel>() {
                    @Override
                    public void success(BaseModel baseModel, Response response) {
                        Timber.d("retro success");
                        disableButtons();

                        clinicTimeRecords.add(baseModel.getClinicTimeRecord());
                        clinicNotStarted.remove(clinicNotStarted.indexOf(clinicId));
                        clinicStarted.add(clinicId);
                        setNotStartedList();
                        setStartedList();
                        pd.dismiss();
                    }

                    @Override
                    public void failure(RetrofitError error) {
                        Timber.d("retro error = " + error.getResponse());
                        pd.dismiss();
                        disableButtons();
                    }
                });
    }

    private void putEndTime(String then, String date, final int clinicId) {
        for (int i = 0; i < clinicTimeRecords.size(); i++) {
            if (clinicTimeRecords.get(i).getClinicId() == clinicId)
                recordId = clinicTimeRecords.get(i).getId();
        }

        PostingData timeRecord = new PostingData();
        timeRecord.putEndTimeRecord(
                then,
                date,
                clinicId);

        pd = new CustomDialogs().showProgressDialog(
                ClinicTimeRecordActivity.this,
                getString(R.string.updating_time_records));

        SmartApiClient.getAuthorizedApiClient(this).putTimeRecords(
                timeRecord,
                clinicId,
                recordId,
                new Callback<BaseModel>() {
                    @Override
                    public void success(BaseModel baseModel, Response response) {
                        Timber.d("retro success");
                        disableButtons();

                        int size = clinicTimeRecords.size();
                        for (int i = 0; i < size; i++) {
                            if (clinicTimeRecords.get(i).getId() == recordId) {
                                clinicTimeRecords.remove(i);
                                clinicStarted.remove(clinicStarted.indexOf(clinicId));
                                clinicStopped.add(clinicId);
                                setStartedList();
                                setStoppedList();
                            }
                        }
                        clinicTimeRecords.add(baseModel.getClinicTimeRecord());
                        pd.dismiss();
                    }

                    @Override
                    public void failure(RetrofitError error) {
                        Timber.d("retro error = " + error.getResponse());
                        pd.dismiss();
                        disableButtons();
                    }
                });
    }

    private void deleteTimeRecord(final int clinicIdForDelete, final int recordIdForDelete) {
        pd = new CustomDialogs().showProgressDialog(
                ClinicTimeRecordActivity.this,
                getString(R.string.deleting_time_record));

        SmartApiClient.getAuthorizedApiClient(this).deleteTimeRecordById(
                clinicIdForDelete,
                recordIdForDelete,
                new Callback<BaseModel>() {
                    @Override
                    public void success(BaseModel baseModel, Response response) {
                        Timber.d("deleteTimeRecord success");
                        disableButtons();

                        int size = clinicTimeRecords.size();
                        for (int i = 0; i < size; i++) {
                            if (clinicTimeRecords.get(i).getId() == recordIdForDelete) {
                                clinicTimeRecords.remove(i);
                                clinicStopped.remove(clinicStopped.indexOf(clinicIdForDelete));
                                clinicNotStarted.add(clinicIdForDelete);
                                setStartedList();
                                setStoppedList();
                                setNotStartedList();
                            }
                        }
                        pd.dismiss();
                    }

                    @Override
                    public void failure(RetrofitError error) {
                        Timber.d("deleteTimeRecord failure = " + error);
                        pd.dismiss();
                        disableButtons();
                    }
                }
        );
    }


    private void setNotStartedList() {
        recordGetDone++;
        String clinicName;
        clinicNotStartedName = new ArrayList<>();
        if (clinicNotStarted.size() == 0) {
            clinicNotStartedName.add(getString(R.string.no_clinics_to_be_started));
            lvNotStarted.setEnabled(false);
        } else {
            lvNotStarted.setEnabled(true);
            int size = clinicNotStarted.size();
            for (int i = 0; i < size; i++) {
                clinicName = clinicIdMap.get(clinicNotStarted.get(i)).getName();
                int clinicId = clinicNotStarted.get(i);
                clinicNotStartedName.add(formatClinicName(clinicId, clinicName));
            }
        }

        adapterNotStart = new ArrayAdapter(
                getApplicationContext(),
                android.R.layout.simple_list_item_1,
                clinicNotStartedName);

        lvNotStarted.setAdapter(adapterNotStart);
        adapterNotStart.notifyDataSetChanged();
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

    private void setStartedList() {
        recordGetDone++;
        clinicStartedName = new ArrayList<>();
        if (clinicStarted.size() == 0) {
            clinicStartedName.add(getString(R.string.no_clinics_currently_started));
            lvStarted.setEnabled(false);
        } else {
            lvStarted.setEnabled(true);
            String clinicName;
            int clinicId;
            int size = clinicStarted.size();
            for (int i = 0; i < size; i++) {
                clinicName = clinicIdMap.get(clinicStarted.get(i)).getName();
                clinicId = clinicStarted.get(i);
                clinicStartedName.add(formatClinicName(clinicId, clinicName));
            }
        }

        adapterStart = new ArrayAdapter(
                getApplicationContext(),
                android.R.layout.simple_list_item_1,
                clinicStartedName);

        lvStarted.setAdapter(adapterStart);
        adapterStart.notifyDataSetChanged();
    }

    private void setStoppedList() {
        recordGetDone++;
        clinicStoppedName = new ArrayList<>();

        if (clinicStopped.size() == 0) {
            clinicStoppedName.add(getString(R.string.no_clinics_currently_stopped));
            lvStopped.setEnabled(false);
        } else {
            lvStopped.setEnabled(true);
            String clinicName;
            int clinicId;
            int size = clinicStopped.size();
            for (int i = 0; i < size; i++) {
                clinicName = clinicIdMap.get(clinicStopped.get(i)).getName();
                clinicId = clinicStopped.get(i);
                clinicStoppedName.add(formatClinicName(clinicId, clinicName));
            }
        }

        adapterStop = new ArrayAdapter(
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
                clinicNotStartedId = clinicNotStarted.get(position);
                break;
            case R.id.lv_clinics_started:
                btnStopClinic.setEnabled(true);
                clinicNotStartedId = 0;
                adapterNotStart.notifyDataSetChanged();
                lvNotStarted.setAdapter(adapterNotStart);
                clinicStartedId = clinicStarted.get(position);
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
                clinicIdForDelete = clinicStopped.get(position);
                int size = clinicTimeRecords.size();
                for (int i = 0; i < size; i++) {
                    if (clinicTimeRecords.get(i).getClinicId() == clinicIdForDelete) {
                        recordIdForDelete = clinicTimeRecords.get(i).getId();
                    }
                }
                break;
        }
        return false;
    }

    @Override
    public void onClick(View v) {
        c = Calendar.getInstance();
        now = dfDateTime.format(c.getTime());
        c.add(Calendar.HOUR, 1);
        then = dfDateTime.format(c.getTime());
        date = dfDateOnly.format(c.getTime());
        switch (v.getId()) {
            case R.id.btn_start_clinic:
                if (clinicNotStartedId != 0) {
                    sharedPrefs.overWriteStringPrefs(getApplicationContext(),
                            Constants.CLINIC_STARTED, String.valueOf(clinicNotStartedId));
                    sharedPrefs.addToStringSetPrefs(ClinicTimeRecordActivity.this,
                            Constants.APPTS_GOT, String.valueOf(clinicNotStartedId));
                    putStartTime(now, clinicNotStartedId);
                    c = Calendar.getInstance();
                    Date todayDate = c.getTime();
                    appointmentHelper.weekDateLooper(todayDate, clinicNotStartedId);
                }
                break;
            case R.id.btn_stop_clinic:
                if (clinicStartedId != 0)
                    putEndTime(then, date, clinicStartedId);
                break;
            case R.id.btn_reset_clinic_record:
                if (clinicIdForDelete != 0)
                    deleteTimeRecord(clinicIdForDelete, recordIdForDelete);
                break;
        }
        clinicNotStartedId = 0;
        clinicStartedId = 0;
    }
}
