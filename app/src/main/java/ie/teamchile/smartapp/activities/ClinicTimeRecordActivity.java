package ie.teamchile.smartapp.activities;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import ie.teamchile.smartapp.R;
import ie.teamchile.smartapp.model.ApiRootModel;
import ie.teamchile.smartapp.model.Clinic;
import ie.teamchile.smartapp.model.ClinicTimeRecord;
import ie.teamchile.smartapp.model.PostingData;
import ie.teamchile.smartapp.util.SmartApi;
import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class ClinicTimeRecordActivity extends BaseActivity {
    private List<Integer> clinicStopped = new ArrayList<>();
    private List<Integer> clinicStarted = new ArrayList<>();
    private List<Integer> clinicNotStarted = new ArrayList<>();
    private List<Integer> idList = new ArrayList<>();
    private List<String> clinicNotStartedName = new ArrayList<>();
    private List<String> clinicStartedName = new ArrayList<>();
    private List<String> clinicStoppedName = new ArrayList<>();
    private Map<String, List<Integer>> clinicDayMap = new HashMap<>();
    private Map<Integer, Clinic> clinicIdMap = new HashMap<>();
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
    private Button btnStartClinic;
    private Button btnStopClinic;
    private ArrayAdapter adapterNotStart;
    private ArrayAdapter adapterStart;
    private ArrayAdapter adapterStop;
    private int recordId = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentForNav(R.layout.activity_clinic_time_record);
        c = Calendar.getInstance();
        todayDay = dfDayLong.format(c.getTime());
        todayDate = dfDateOnly.format(c.getTime());
        Log.d("SMART", "today = " + todayDay);
        lvNotStarted = (ListView) findViewById(R.id.lv_clinics_not_started);
        lvNotStarted.setOnItemClickListener(new ItemClicky());
        lvStarted = (ListView) findViewById(R.id.lv_clinics_started);
        lvStarted.setOnItemClickListener(new ItemClicky());
        lvStopped = (ListView) findViewById(R.id.lv_clinics_stopped);
        btnStartClinic = (Button) findViewById(R.id.btn_start_clinic);
        btnStartClinic.setOnClickListener(new ButtonClicky());
        btnStopClinic = (Button) findViewById(R.id.btn_stop_clinic);
        btnStopClinic.setOnClickListener(new ButtonClicky());

        setActionBarTitle("Start/Stop Clinics");

        setRecordsToList();
    }

    private void setRecordsToList() {
        List<Clinic> clinics = ApiRootModel.getInstance().getClinics();

        for (int i = 0; i < clinics.size(); i++) {
            List<String> trueDays = clinics.get(i).getTrueDays();
            for (int j = 0; j < trueDays.size(); j++) {
                Clinic clinic = clinics.get(i);
                int clinicId = clinics.get(i).getId();
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
        Log.d("Clinic", "Record map = " + clinicDayMap);

        idList = clinicDayMap.get(todayDay);

        for (int i = 0; i < idList.size(); i++) {
            getTimeRecords(idList.get(i), todayDate);
        }
    }

    private void getTimeRecords(final int clinicId, String date) {
        RestAdapter restAdapter = new RestAdapter.Builder()
                .setEndpoint(SmartApi.BASE_URL)
                .setLogLevel(RestAdapter.LogLevel.FULL)
                .build();

        api = restAdapter.create(SmartApi.class);

        api.getTimeRecords(
                clinicId,
                date,
                ApiRootModel.getInstance().getLogin().getToken(),
                SmartApi.API_KEY,
                new Callback<ApiRootModel>() {
                    @Override
                    public void success(ApiRootModel apiRootModel, Response response) {
                        Log.d("retro", "get time record success");
                        if (apiRootModel.getClinicTimeRecords().size() > 0) {
                            ApiRootModel.getInstance().addClinicTimeRecord(apiRootModel.getClinicTimeRecords().get(0));
                            if (apiRootModel.getClinicTimeRecords().get(0).getEndTime() == null)
                                clinicStarted.add(clinicId);
                            else
                                clinicStopped.add(clinicId);

                        } else {
                            clinicNotStarted.add(clinicId);
                            clinicStartedName.add(clinicIdMap.get(clinicId).getName());
                        }

                        setNotStartedList();
                        setStartedList();
                        setStoppedList();

                        Log.d("bugs", "clinicStopped = " + clinicStopped);
                        Log.d("bugs", "clinicStarted = " + clinicStarted);
                        Log.d("bugs", "clinicNotStarted = " + clinicNotStarted);
                    }

                    @Override
                    public void failure(RetrofitError error) {
                        Log.d("retro", "get time record failure = " + error);
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

        showProgressDialog(ClinicTimeRecordActivity.this, "Updating Clinic Time Records");

        RestAdapter restAdapter = new RestAdapter.Builder()
                .setEndpoint(SmartApi.BASE_URL)
                .setLogLevel(RestAdapter.LogLevel.FULL)
                .build();

        api = restAdapter.create(SmartApi.class);

        api.postTimeRecords(
                timeRecord,
                clinicId,
                ApiRootModel.getInstance().getLogin().getToken(),
                SmartApi.API_KEY,
                new Callback<ApiRootModel>() {
                    @Override
                    public void success(ApiRootModel apiRootModel, Response response) {
                        Log.d("SMART", "retro success");
                        ApiRootModel.getInstance().setClinicTimeRecords(apiRootModel.getClinicTimeRecords());
                        clinicNotStarted.remove(clinicNotStarted.indexOf(clinicId));
                        clinicStarted.add(clinicId);
                        setNotStartedList();
                        setStartedList();
                        pd.dismiss();
                    }

                    @Override
                    public void failure(RetrofitError error) {
                        Log.d("SMART", "retro error = " + error.getResponse());
                        pd.dismiss();
                    }
                });
    }

    private void putEndTime(String then, String date, int clinicId) {
        final List<ClinicTimeRecord> records = ApiRootModel.getInstance().getClinicTimeRecords();

        for (int i = 0; i < records.size(); i++) {
            if (records.get(i).getClinicId() == clinicId) {
                recordId = records.get(i).getId();
            }
            Log.d("bugs", "record id = " + recordId);
        }
        PostingData timeRecord = new PostingData();
        timeRecord.putEndTimeRecord(
                then,
                date,
                clinicId);

        showProgressDialog(ClinicTimeRecordActivity.this, "Updating Clinic Time Records");

        RestAdapter restAdapter = new RestAdapter.Builder()
                .setEndpoint(SmartApi.BASE_URL)
                .setLogLevel(RestAdapter.LogLevel.FULL)
                .build();

        api = restAdapter.create(SmartApi.class);

        api.putTimeRecords(
                timeRecord,
                clinicId,
                recordId,
                ApiRootModel.getInstance().getLogin().getToken(),
                SmartApi.API_KEY,
                new Callback<ApiRootModel>() {
                    @Override
                    public void success(ApiRootModel apiRootModel, Response response) {
                        Log.d("SMART", "retro success");
                        for (int i = 0; i < records.size(); i++) {
                            if (records.get(i).getId() == recordId) {
                                ApiRootModel.getInstance().getClinicTimeRecords().remove(i);
                            }
                        }
                        ApiRootModel.getInstance().setClinicTimeRecords(apiRootModel.getClinicTimeRecords());
                        pd.dismiss();
                    }

                    @Override
                    public void failure(RetrofitError error) {
                        Log.d("SMART", "retro error = " + error.getResponse());
                        pd.dismiss();
                    }
                });
    }

    private void setNotStartedList() {
        clinicNotStartedName = new ArrayList<>();
        if (clinicNotStarted.size() == 0) {
            clinicNotStartedName.add("No clinics to be started");
            lvNotStarted.setEnabled(false);
            btnStartClinic.setEnabled(false);
        } else {
            lvNotStarted.setEnabled(true);
            btnStartClinic.setEnabled(true);
            for (int i = 0; i < clinicNotStarted.size(); i++) {
                clinicNotStartedName.add(clinicIdMap.get(clinicNotStarted.get(i)).getName());
            }
        }

        adapterNotStart = new ArrayAdapter(
                ClinicTimeRecordActivity.this,
                android.R.layout.simple_list_item_1,
                clinicNotStartedName);

        adapterNotStart.notifyDataSetChanged();
        lvNotStarted.setAdapter(adapterNotStart);
    }

    private void setStartedList() {
        clinicStartedName = new ArrayList<>();
        if (clinicStarted.size() == 0) {
            clinicStartedName.add("No clinics currently started");
            lvStarted.setEnabled(false);
            btnStopClinic.setEnabled(false);
        } else {
            lvStarted.setEnabled(true);
            btnStopClinic.setEnabled(true);
            for (int i = 0; i < clinicStarted.size(); i++) {
                clinicStartedName.add(clinicIdMap.get(clinicStarted.get(i)).getName());
            }
        }

        adapterStart = new ArrayAdapter(
                ClinicTimeRecordActivity.this,
                android.R.layout.simple_list_item_1,
                clinicStartedName);

        adapterStart.notifyDataSetChanged();
        lvStarted.setAdapter(adapterStart);
    }

    private void setStoppedList() {
        clinicStoppedName = new ArrayList<>();

        if (clinicStopped.size() == 0) {
            clinicStoppedName.add("No clinics currently stopped");
        } else {
            for (int i = 0; i < clinicStopped.size(); i++) {
                clinicStoppedName.add(clinicIdMap.get(clinicStopped.get(i)).getName());
            }
        }

        adapterStop = new ArrayAdapter(
                ClinicTimeRecordActivity.this,
                android.R.layout.simple_list_item_1,
                clinicStoppedName);

        adapterStop.notifyDataSetChanged();
        lvStopped.setAdapter(adapterStop);
    }

    private class ItemClicky implements AdapterView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            switch (parent.getId()) {
                case R.id.lv_clinics_not_started:
                    clinicStartedId = 0;
                    adapterStart.notifyDataSetChanged();
                    lvStarted.setAdapter(adapterStart);
                    Log.d("bugs", "not started clinic list pressed");
                    clinicNotStartedId = clinicNotStarted.get(position);
                    Log.d("bugs", "clinicNotStartedId = " + clinicNotStartedId);
                    break;
                case R.id.lv_clinics_started:
                    clinicNotStartedId = 0;
                    adapterNotStart.notifyDataSetChanged();
                    lvNotStarted.setAdapter(adapterNotStart);
                    Log.d("bugs", "started clinic list pressed");
                    clinicStartedId = clinicStarted.get(position);
                    Log.d("bugs", "clinicStartedId = " + clinicStartedId);
                    break;
            }
        }
    }

    private class ButtonClicky implements View.OnClickListener {
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
                        Log.d("bugs", "start clinic btn pressed");
                        putStartTime(now, clinicNotStartedId);
                    }
                    break;
                case R.id.btn_stop_clinic:
                    if (clinicStartedId != 0) {
                        putEndTime(then, date, clinicStartedId);
                        Log.d("bugs", "stop clinic btn pressed");
                    }
                    break;
            }
            clinicNotStartedId = 0;
            clinicStartedId = 0;
        }
    }
}
