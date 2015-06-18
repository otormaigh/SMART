package ie.teamchile.smartapp.activities;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

import ie.teamchile.smartapp.R;
import ie.teamchile.smartapp.model.ApiRootModel;
import ie.teamchile.smartapp.model.PostingData;
import ie.teamchile.smartapp.model.PregnancyNote;
import ie.teamchile.smartapp.util.SmartApi;
import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class MidwiferyLogActivity extends BaseActivity {
    private ListView lvNotes;
    private Button btnAddNote;
    private EditText etNote;
    private String note;
    private int pregnancyId;
    private List<String> dateList = new ArrayList<>();
    private List<String> authorList = new ArrayList<>();
    private List<String> notesList = new ArrayList<>();
    private BaseAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentForNav(R.layout.activity_midwifery_log);

        setActionBarTitle(getResources().getString(R.string.midwifery_log));

        pregnancyId = Integer.parseInt(getIntent().getStringExtra("pregnancyId"));
        lvNotes = (ListView) findViewById(R.id.lv_midwifery_log);
        btnAddNote = (Button) findViewById(R.id.btn_add_midwifery_note);
        btnAddNote.setOnClickListener(new Clicky());
        etNote = (EditText) findViewById(R.id.et_midwifery_notes);

        ApiRootModel.getInstance().setPregnancyNotes(
                ApiRootModel.getInstance().getPregnancies().get(p).getPregnancyNotes());

        setData();
    }

    private void setData(){
        dateList = new ArrayList<>();
        authorList = new ArrayList<>();
        notesList = new ArrayList<>();

        int size = ApiRootModel.getInstance().getPregnancyNotes().size();

        Log.d("bugs", "size = " + size);

        for(int i = size - 1; i > 0; i--){
            PregnancyNote theNote = ApiRootModel.getInstance().getPregnancyNotes().get(i);
            try {
                dateList.add(dfHumanReadableDate.format(dfDateOnly.parse(theNote.getCreatedAt())));
            } catch (ParseException e) {
                e.printStackTrace();
            }
            authorList.add(theNote.getServiceProviderName());
            notesList.add(theNote.getNote());
        }

        Log.d("bugs", "notesList = " + notesList);
        adapter = new MyAdapter(
                this,
                dateList,
                authorList,
                notesList);
        lvNotes.setAdapter(adapter);
    }

    private void postNote(String note) {
        PostingData postNote = new PostingData();

        postNote.postNote(note);

        showProgressDialog(this, "Adding Note");

        RestAdapter restAdapter = new RestAdapter.Builder()
                .setEndpoint(SmartApi.BASE_URL)
                .setLogLevel(RestAdapter.LogLevel.FULL)
                .build();

        api = restAdapter.create(SmartApi.class);

        api.postPregnancyNote(
                postNote,
                pregnancyId,
                ApiRootModel.getInstance().getLogin().getToken(),
                SmartApi.API_KEY,
                new Callback<ApiRootModel>() {
                    @Override
                    public void success(ApiRootModel apiRootModel, Response response) {
                        Log.d("retro", "retro success");
                        ApiRootModel.getInstance().addPregnancyNote(apiRootModel.getPregnancyNote());

                        Log.d("bugs", "note = " + apiRootModel.getPregnancyNote().getNote());

                        adapter.notifyDataSetChanged();
                        setData();
                        pd.dismiss();
                    }

                    @Override
                    public void failure(RetrofitError error) {
                        Log.d("retro", "retro failure = " + error);
                        pd.dismiss();
                    }
                }
        );
    }

    private class Clicky implements View.OnClickListener {

        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.btn_add_midwifery_note:
                    note = etNote.getText().toString();
                    postNote(note);
                    break;
            }
        }
    }

    private class MyAdapter extends BaseAdapter {
        private Context context;
        private List<String> dateList;
        private List<String> authorList;
        private List<String> notesList;

        public MyAdapter(Context context,
                         List<String> dateList,
                         List<String> authorList,
                         List<String> notesList){
            this.context = context;
            this.dateList = dateList;
            this.authorList = authorList;
            this.notesList = notesList;
        }

        @Override
        public int getCount() {
            return notesList.size();
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            LayoutInflater inflater = getLayoutInflater();
            convertView = (View) inflater.inflate(R.layout.midwife_note_list_layout, null);
            TextView tvDate = (TextView) convertView.findViewById(R.id.tv_note_date);
            TextView tvName = (TextView) convertView.findViewById(R.id.tv_note_provider_name);
            TextView tvNote = (TextView) convertView.findViewById(R.id.tv_note);

            tvDate.setText(dateList.get(position));
            tvName.setText(authorList.get(position));
            tvNote.setText(notesList.get(position));
            return convertView;
        }
    }
}
