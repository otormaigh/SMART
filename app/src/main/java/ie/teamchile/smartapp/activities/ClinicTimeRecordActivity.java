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
import ie.teamchile.smartapp.model.PostingData;
import ie.teamchile.smartapp.util.SmartApi;
import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class ClinicTimeRecordActivity extends BaseActivity {
    private List<Integer> clinicStarted = new ArrayList<>();
    private List<Integer> clinicNotStarted = new ArrayList<>();
    private List<Integer> idList = new ArrayList<>();
    private List<String> clinicNotStartedName = new ArrayList<>();
    private List<String> clinicStartedName = new ArrayList<>();
    private Map<String, List<Integer>> clinicDayMap = new HashMap<>();
    private Map<Integer, Clinic> clinicIdMap = new HashMap<>();
    private ListView lvNotStarted;
    private ListView lvStarted;
    private String todayDay;
    private String todayDate;
    private String now;
    private String then;
    private String date;
    private int clinicId;
    private Button btnStartClinic;
    private Button btnStopClinic;

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
                String clinicName = clinics.get(i).getName();
                int clinicId = clinics.get(i).getId();
                clinicIdMap.put(clinicId, clinic);

                switch (clinics.get(i).getId()) {
                    case 6:
                        clinicName += " (Domino)";
                        break;
                    case 10:
                        clinicName += " (Satellite)";
                        break;
                }

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
            getTimeRecords(idList.get(i), todayDate, i);
        }
    }

    private void getTimeRecords(final int clinicId, String date, final int position) {
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
                            clinicStarted.add(clinicId);

                        } else {
                            clinicNotStarted.add(clinicId);
                            clinicStartedName.add(clinicIdMap.get(clinicId).getName());
                        }

                        setNotStartedList();
                        setStartedList();

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
        for (int i = 0; i < clinicNotStarted.size(); i++) {
            clinicNotStartedName.add(clinicIdMap.get(clinicNotStarted.get(i)).getName());
        }

        ArrayAdapter adapterNotStart = new ArrayAdapter(
                ClinicTimeRecordActivity.this,
                android.R.layout.simple_list_item_1,
                clinicNotStartedName);

        adapterNotStart.notifyDataSetChanged();
        lvNotStarted.setAdapter(adapterNotStart);
    }

    private void setStartedList() {
            clinicStartedName = new ArrayList<>();
            for (int i = 0; i < clinicStarted.size(); i++) {
                clinicStartedName.add(clinicIdMap.get(clinicStarted.get(i)).getName());
            }

            ArrayAdapter adapterStart = new ArrayAdapter(
                    ClinicTimeRecordActivity.this,
                    android.R.layout.simple_list_item_1,
                    clinicStartedName);

            adapterStart.notifyDataSetChanged();
            lvStarted.setAdapter(adapterStart);
    }

    private class ItemClicky implements AdapterView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            switch (parent.getId()) {
                case R.id.lv_clinics_not_started:
                    Log.d("bugs", "not started clinic list pressed");
                    clinicId = clinicNotStarted.get(position);
                    Log.d("bugs", "clinicId = " + clinicId);
                    break;
                case R.id.lv_clinics_started:
                    Log.d("bugs", "started clinic list pressed");
                    clinicId = clinicStarted.get(position);
                    Log.d("bugs", "clinicId = " + clinicId);
                    break;
            }
        }
    }

    private class ButtonClicky implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.btn_start_clinic:
                    Log.d("bugs", "start clinic btn pressed");
                    break;
                case R.id.btn_stop_clinic:
                    Log.d("bugs", "stop clinic btn pressed");
                    break;
            }
            if (clinicId != 0) {
                c = Calendar.getInstance();
                now = dfDateTime.format(c.getTime());
                c.add(Calendar.HOUR, 1);
                then = dfDateTime.format(c.getTime());
                Log.d("Checkin", "now = " + now + " then = " + then + " clinic id = " + clinicId);
                date = dfDateOnly.format(c.getTime());
                switch (v.getId()) {
                    case R.id.btn_start_clinic:
                        Log.d("bugs", "start clinic btn pressed");
                        putStartTime(now, clinicId);
                        break;
                    case R.id.btn_stop_clinic:
                        putEndTime(then, date, clinicId);
                        Log.d("bugs", "stop clinic btn pressed");
                        break;
                }
            }
            clinicId = 0;
        }
    }
}
