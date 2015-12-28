package ie.teamchile.smartapp.activities;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import ie.teamchile.smartapp.R;
import ie.teamchile.smartapp.activities.Base.BaseActivity;
import ie.teamchile.smartapp.api.SmartApiClient;
import ie.teamchile.smartapp.model.BaseModel;
import ie.teamchile.smartapp.model.PostingData;
import ie.teamchile.smartapp.model.PregnancyNote;
import ie.teamchile.smartapp.model.ServiceUser;
import ie.teamchile.smartapp.util.Constants;
import ie.teamchile.smartapp.util.CustomDialogs;
import io.realm.Realm;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;
import timber.log.Timber;

public class MidwiferyLogActivity extends BaseActivity implements OnClickListener {
    private ListView lvNotes;
    private Button btnAddNote;
    private EditText etNote;
    private String note;
    private int pregnancyId;
    private List<String> dateList = new ArrayList<>();
    private List<String> authorList = new ArrayList<>();
    private List<String> notesList = new ArrayList<>();
    private BaseAdapter adapter;
    private AlertDialog.Builder alertDialog;
    private AlertDialog ad;
    private int size;
    private ProgressDialog pd;
    private Realm realm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentForNav(R.layout.activity_midwifery_log);

        realm = Realm.getInstance(getApplicationContext());

        setActionBarTitle(getResources().getString(R.string.midwifery_log));

        pregnancyId = Integer.parseInt(getIntent().getStringExtra(Constants.ARGS_PREGNANCY_ID));
        lvNotes = (ListView) findViewById(R.id.lv_midwifery_log);
        btnAddNote = (Button) findViewById(R.id.btn_add_midwifery_note);
        btnAddNote.setOnClickListener(this);
        etNote = (EditText) findViewById(R.id.et_midwifery_notes);

        pd = new CustomDialogs().showProgressDialog(
                MidwiferyLogActivity.this,
                getString(R.string.updating_notes));
        getMidwiferyNotes();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        if (realm != null)
            realm.close();
    }

    private void getMidwiferyNotes() {
        SmartApiClient.getAuthorizedApiClient(this).getPregnancyNotes(
                realm.where(ServiceUser.class).findFirst().getPregnancyIds().get(p).getValue(),
                new Callback<BaseModel>() {
                    @Override
                    public void success(BaseModel baseModel, Response response) {
                        Timber.d("put getMidwiferyNotes retro success");
                        realm.beginTransaction();
                        realm.copyToRealmOrUpdate(baseModel.getPregnancyNotes());
                        realm.commitTransaction();
                        pd.dismiss();
                        setData();
                    }

                    @Override
                    public void failure(RetrofitError error) {
                        Timber.d("put getMidwiferyNotes retro failure = " + error);
                        pd.dismiss();
                    }
                }
        );
    }

    private void setData() {
        dateList = new ArrayList<>();
        authorList = new ArrayList<>();
        notesList = new ArrayList<>();

        size = realm.where(PregnancyNote.class).findAll().size();

        realm.beginTransaction();
        Collections.sort(realm.where(PregnancyNote.class).findAll(), new Comparator<PregnancyNote>() {

            @Override
            public int compare(PregnancyNote a, PregnancyNote b) {
                int valA;
                int valB;

                valA = a.getId();
                valB = b.getId();

                return ((Integer) valA).compareTo(valB);
            }
        });
        realm.commitTransaction();

        for (int i = size - 1; i >= 0; i--) {
            PregnancyNote theNote = realm.where(PregnancyNote.class).findAll().get(i);
            try {
                dateList.add(Constants.DF_HUMAN_READABLE_DATE.format(dfDateOnly.parse(theNote.getCreatedAt())));
            } catch (ParseException e) {
                Timber.e(Log.getStackTraceString(e));
            }
            authorList.add(theNote.getServiceProviderName());
            notesList.add(theNote.getNote());
        }

        adapter = new MyAdapter(
                dateList,
                authorList,
                notesList);
        lvNotes.setAdapter(adapter);
    }

    private void postNote(String note) {
        PostingData postNote = new PostingData();

        postNote.postNote(note);

        pd = new CustomDialogs().showProgressDialog(
                this,
                getString(R.string.adding_note));

        SmartApiClient.getAuthorizedApiClient(this).postPregnancyNote(
                postNote,
                pregnancyId,
                new Callback<BaseModel>() {
                    @Override
                    public void success(BaseModel baseModel, Response response) {
                        Timber.d("postNote success");
                        realm.beginTransaction();
                        realm.copyToRealmOrUpdate(baseModel.getPregnancyNote());
                        realm.commitTransaction();

                        adapter.notifyDataSetChanged();
                        lvNotes.setAdapter(null);
                        setData();
                        pd.dismiss();
                    }

                    @Override
                    public void failure(RetrofitError error) {
                        Timber.d("postNote failure = " + error);
                        pd.dismiss();
                    }
                }
        );
    }

    private void addNoteDialog() {
        LayoutInflater inflater = getLayoutInflater();
        View convertView = inflater.inflate(R.layout.dialog_add_note, null);
        final TextView tvCharCount = (TextView) convertView.findViewById(R.id.tv_note_char_count);
        TextView tvDialogTitle = (TextView) convertView.findViewById(R.id.tv_dialog_title);
        final EditText etEnterNote = (EditText) convertView.findViewById(R.id.et_midwifery_notes);
        final int max = 140;
        tvCharCount.setText(String.format(Constants.FORMAT_TV_CHAR_COUNT, etEnterNote.getText().length(), max));
        etEnterNote.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                tvCharCount.setText(String.format(Constants.FORMAT_TV_CHAR_COUNT, s.length(), max));
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.length() > max)
                    etEnterNote.setError(getString(R.string.error_char_limit_exceeded));
            }
        });
        ImageView ivExit = (ImageView) convertView.findViewById(R.id.iv_exit_dialog);
        ivExit.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                ad.dismiss();
            }
        });
        Button btnSaveNote = (Button) convertView.findViewById(R.id.btn_save_note);
        btnSaveNote.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (etEnterNote.getText().toString().equals("")) {
                    etEnterNote.setError(getString(R.string.error_cannot_be_empty));
                } else if (etEnterNote.getText().length() > max) {
                    pd = new CustomDialogs().showProgressDialog(
                            MidwiferyLogActivity.this,
                            getString(R.string.error_char_limit_exceeded));
                    new CountDownTimer(2000, 1000) {

                        @Override
                        public void onTick(long millisUntilFinished) {

                        }

                        @Override
                        public void onFinish() {
                            pd.dismiss();
                        }
                    }.start();

                } else {
                    postNote(etEnterNote.getText().toString());
                    ad.dismiss();
                }
            }
        });

        alertDialog = new AlertDialog.Builder(MidwiferyLogActivity.this);
        alertDialog.setView(convertView);
        tvDialogTitle.setText(getString(R.string.add_note_below));
        ad = alertDialog.create();
        ad.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
        ad.show();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_add_midwifery_note:
                addNoteDialog();
                break;
        }
    }

    private class MyAdapter extends BaseAdapter {
        private List<String> dateList;
        private List<String> authorList;
        private List<String> notesList;

        public MyAdapter(List<String> dateList,
                         List<String> authorList,
                         List<String> notesList) {
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
            ViewHolder holder;
            if (convertView == null) {
                convertView = inflater.inflate(R.layout.list_layout_midwife_note, null);
                holder = new ViewHolder(convertView);
                convertView.setTag(holder);
            } else
                holder = (ViewHolder) convertView.getTag();

            holder.tvDate.setText(dateList.get(position));
            holder.tvName.setText(authorList.get(position));
            holder.tvNote.setText(notesList.get(position));
            return convertView;
        }

        private class ViewHolder {
            TextView tvDate;
            TextView tvName;
            TextView tvNote;

            public ViewHolder(View view) {
                tvDate = (TextView) view.findViewById(R.id.tv_note_date);
                tvName = (TextView) view.findViewById(R.id.tv_note_provider_name);
                tvNote = (TextView) view.findViewById(R.id.tv_note);
            }
        }
    }
}
