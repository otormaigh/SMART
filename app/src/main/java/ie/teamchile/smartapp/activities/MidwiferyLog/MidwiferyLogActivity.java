package ie.teamchile.smartapp.activities.MidwiferyLog;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import ie.teamchile.smartapp.R;
import ie.teamchile.smartapp.activities.Base.BaseActivity;
import ie.teamchile.smartapp.model.PregnancyNote;
import ie.teamchile.smartapp.util.Constants;
import ie.teamchile.smartapp.util.CustomDialogs;

public class MidwiferyLogActivity extends BaseActivity implements MidwiferyLogView, OnClickListener {
    private List<PregnancyNote> pregnancyNotes = new ArrayList<>();
    private BaseAdapter adapter;
    private AlertDialog ad;
    private ProgressDialog pd;
    private MidwiferyLogPresenter midwiferyLogPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentForNav(R.layout.activity_midwifery_log);

        initViews();

        midwiferyLogPresenter = new MidwiferyLogPresenterImp(this, new WeakReference<Activity>(MidwiferyLogActivity.this));

        setActionBarTitle(getResources().getString(R.string.midwifery_log));
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void updatePregnancyList(List<PregnancyNote> pregnancyNotes) {
        Collections.sort(pregnancyNotes, new Comparator<PregnancyNote>() {

            @Override
            public int compare(PregnancyNote a, PregnancyNote b) {
                return ((Integer) a.getId()).compareTo(b.getId());
            }
        });

        this.pregnancyNotes = pregnancyNotes;

        adapter.notifyDataSetChanged();
    }

    @Override
    public void initViews() {
        findViewById(R.id.btn_add_midwifery_note).setOnClickListener(this);
        adapter = new MidwiferyNoteAdapter();
        ((ListView) findViewById(R.id.lv_midwifery_log)).setAdapter(adapter);
    }

    @Override
    public void showAddNoteDialog() {
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

        convertView.findViewById(R.id.iv_exit_dialog).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                ad.dismiss();
            }
        });

        convertView.findViewById(R.id.btn_save_note).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (TextUtils.isEmpty(etEnterNote.getText())) {
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
                    midwiferyLogPresenter.postNote(etEnterNote.getText().toString());
                    ad.dismiss();
                }
            }
        });

        AlertDialog.Builder alertDialog = new AlertDialog.Builder(MidwiferyLogActivity.this);
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
                showAddNoteDialog();
                break;
        }
    }

    private class MidwiferyNoteAdapter extends BaseAdapter {
        private LayoutInflater inflater;
        private ViewHolder holder;

        @Override
        public int getCount() {
            return pregnancyNotes.size();
        }

        @Override
        public PregnancyNote getItem(int position) {
            return pregnancyNotes.get(position);
        }

        @Override
        public long getItemId(int position) {
            return pregnancyNotes.get(position).hashCode();
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            inflater = getLayoutInflater();
            if (convertView == null) {
                convertView = inflater.inflate(R.layout.list_layout_midwife_note, null);
                holder = new ViewHolder(convertView);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }

            holder.tvDate.setText(Constants.DF_HUMAN_READABLE_DATE.format(getItem(position).getCreatedAt()));
            holder.tvName.setText(getItem(position).getServiceProviderName());
            holder.tvNote.setText(getItem(position).getNote());

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
