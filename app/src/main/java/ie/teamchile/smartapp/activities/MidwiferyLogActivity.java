package ie.teamchile.smartapp.activities;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import ie.teamchile.smartapp.R;
import ie.teamchile.smartapp.model.ApiRootModel;
import ie.teamchile.smartapp.model.PostingData;
import ie.teamchile.smartapp.util.SmartApi;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class MidwiferyLogActivity extends BaseActivity {
    private ListView lvNotes;
    private Button btnAddNote;
    private EditText etNote;
    private String note;
    private int pregnancyId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentForNav(R.layout.activity_pregnancy_notes);

        setActionBarTitle(getResources().getString(R.string.midwifery_log));

        pregnancyId = Integer.parseInt(getIntent().getStringExtra("pregnancyId"));
        lvNotes = (ListView) findViewById(R.id.lv_midwifery_log);
        btnAddNote = (Button) findViewById(R.id.btn_add_midwifery_note);
        btnAddNote.setOnClickListener(new Clicky());
        etNote = (EditText) findViewById(R.id.et_midwifery_notes);
    }

    private void postNote(String note) {
        PostingData postNote = new PostingData();

        postNote.postNote(note);

        showProgressDialog(this, "Adding Note");

        api.postPregnancyNote(
                postNote,
                pregnancyId,
                ApiRootModel.getInstance().getLogin().getToken(),
                SmartApi.API_KEY,
                new Callback<ApiRootModel>() {
                    @Override
                    public void success(ApiRootModel apiRootModel, Response response) {
                        Log.d("retro", "retro success");
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
}
