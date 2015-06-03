package ie.teamchile.smartapp.activities;

import ie.teamchile.smartapp.R;
import ie.teamchile.smartapp.model.ApiRootModel;
import ie.teamchile.smartapp.model.Clinic;
import ie.teamchile.smartapp.model.PostingData;
import ie.teamchile.smartapp.utility.SmartApi;
import ie.teamchile.smartapp.utility.ToastAlert;
import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.Response;

import android.content.ComponentCallbacks2;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CalendarActivity extends BaseActivity {
    private List<Integer> idList = new ArrayList<>();
    private List<String> clinicDays = new ArrayList<>();
    private Map<String, List<String>> clinicMap = new HashMap<>();
    private Map<String, List<Integer>> clinicIdMap = new HashMap<>();
    private ListView lvTimeRecordClinics;
    private String today;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentForNav(R.layout.activity_calendar);
        c = Calendar.getInstance();
        today = dfDayLong.format(c.getTime());
        Log.d("SMART", "today = " + today);
        lvTimeRecordClinics = (ListView) findViewById(R.id.lv_time_record_clinics);
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
                CalendarActivity.this,
                android.R.layout.simple_list_item_1,
                clinicMap.get(today));

        lvTimeRecordClinics.setAdapter(adapter);
        lvTimeRecordClinics.setOnItemClickListener(new Clicky());
    }

    private class Clicky implements AdapterView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            int clinicId = clinicIdMap.get(today).get(position);
            c = Calendar.getInstance();
            String now =  dfDateTime.format(c.getTime());
            c.add(Calendar.HOUR, 1);
            String then = dfDateTime.format(c.getTime());

            PostingData timeRecord = new PostingData();
            timeRecord.updateTimeRecords(
                    now,
                    then,
                    clinicId);

            showProgressDialog(CalendarActivity.this, "Updating Clinic Time Records");

            RestAdapter restAdapter = new RestAdapter.Builder()
                    .setEndpoint(SmartApi.BASE_URL)
                    .setLogLevel(RestAdapter.LogLevel.FULL)
                    .build();

            api = restAdapter.create(SmartApi.class);

            api.putTimeRecords(timeRecord,
                    clinicId,
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
}
