package ie.teamchile.smartapp.activities;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
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
import ie.teamchile.smartapp.utility.SmartApi;
import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class ClinicTimeRecordActivity extends BaseActivity {
    private List<Integer> idList = new ArrayList<>();
    private List<String> clinicDays = new ArrayList<>();
    private Map<String, List<String>> clinicMap = new HashMap<>();
    private Map<String, List<Integer>> clinicIdMap = new HashMap<>();
    private ListView lvTimeRecordClinics;
    private String today;
    private String now;
    private String then;
    private String date;
    private int clinicId;
    private Button btnCheckIn;
    private Button btnCheckOut;
    private EditText etRecordId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentForNav(R.layout.activity_clinic_time_record);
        c = Calendar.getInstance();
        today = dfDayLong.format(c.getTime());
        Log.d("SMART", "today = " + today);
        lvTimeRecordClinics = (ListView) findViewById(R.id.lv_time_record_clinics);
        btnCheckIn = (Button) findViewById(R.id.btn_check_in);
        btnCheckIn.setOnClickListener(new Clicky());
        btnCheckOut = (Button) findViewById(R.id.btn_check_out);
        btnCheckOut.setOnClickListener(new Clicky());
        etRecordId = (EditText) findViewById(R.id.et_record_id);

        setActionBarTitle("Clinic Time");

        thing();
    }

    private void thing() {
        List<Clinic> clinic = ApiRootModel.getInstance().getClinics();

        for (int i = 0; i < clinic.size(); i++) {
            List<String> trueDays = clinic.get(i).getTrueDays();
            for (int j = 0; j < trueDays.size(); j++) {
                String clinicName = clinic.get(i).getName();
                int clinicId = clinic.get(i).getId();
                switch (clinic.get(i).getId()) {
                    case 6:
                        clinicName += " (Domino)";
                        break;
                    case 10:
                        clinicName += " (Satellite)";
                        break;
                }

                if (clinicMap.get(trueDays.get(j)) != null) {
                    clinicDays = new ArrayList<>();
                    idList = new ArrayList<>();
                    clinicDays = clinicMap.get(trueDays.get(j));
                    idList = clinicIdMap.get(trueDays.get(j));
                    clinicDays.add(clinicName);
                    idList.add(clinicId);
                    clinicMap.put(trueDays.get(j), clinicDays);
                    clinicIdMap.put(trueDays.get(j), idList);
                } else {
                    clinicDays = new ArrayList<>();
                    idList = new ArrayList<>();
                    clinicDays.add(clinicName);
                    idList.add(clinicId);
                    clinicMap.put(trueDays.get(j), clinicDays);
                    clinicIdMap.put(trueDays.get(j), idList);
                }
            }
        }
        Log.d("Clinic", "Record map = " + clinicMap);

        ArrayAdapter adapter = new ArrayAdapter(
                ClinicTimeRecordActivity.this,
                android.R.layout.simple_list_item_1,
                clinicMap.get(today));

        lvTimeRecordClinics.setAdapter(adapter);
        lvTimeRecordClinics.setOnItemClickListener(new ItemClicky());
    }

    private class ItemClicky implements AdapterView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            clinicId = clinicIdMap.get(today).get(position);
        }
    }

    private class Clicky implements View.OnClickListener{

        @Override
        public void onClick(View v) {
            c = Calendar.getInstance();
            now =  dfDateTime.format(c.getTime());
            c.add(Calendar.HOUR, 1);
            then = dfDateTime.format(c.getTime());
            Log.d("Checkin", "now = " + now + " then = " + then + " clinic id = " + clinicId);
            date = dfDateOnly.format(c.getTime());
            switch(v.getId()){

                case R.id.btn_check_in:
                    putStartTime(now, clinicId);
                    break;
                case R.id.btn_check_out:
                    putEndTime(then, date, clinicId);
                    break;
            }
        }
    }

    private void putStartTime(String now, int clinicId){
        PostingData timeRecord = new PostingData();
        timeRecord.updateTimeRecords(
                now,
                clinicId,
                date);

        showProgressDialog(ClinicTimeRecordActivity.this, "Updating Clinic Time Records");

        RestAdapter restAdapter = new RestAdapter.Builder()
                .setEndpoint(SmartApi.BASE_URL)
                .setLogLevel(RestAdapter.LogLevel.FULL)
                .build();

        api = restAdapter.create(SmartApi.class);

        api.putTimeRecords(
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

    private void putEndTime(String then, String date, int clinicId){
          int recordId = Integer.parseInt(etRecordId.getText().toString());
        //int recordId = ApiRootModel.getInstance().getClinicTimeRecords().get(0).getId();
        PostingData timeRecord = new PostingData();
        timeRecord.updateTimeRecords(then, date, clinicId);

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
                        pd.dismiss();
                    }

                    @Override
                    public void failure(RetrofitError error) {
                        Log.d("SMART", "retro error = " + error);
                        pd.dismiss();
                    }
                });
    }
}
