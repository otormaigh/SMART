package ie.teamchile.smartapp.activities;

import android.os.Bundle;

import ie.teamchile.smartapp.R;

public class MidwiferyLogActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentForNav(R.layout.activity_pregnancy_notes);

        setActionBarTitle(getResources().getString(R.string.midwifery_log));
    }
}
