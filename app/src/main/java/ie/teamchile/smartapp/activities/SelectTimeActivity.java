package ie.teamchile.smartapp.activities;

import ie.teamchile.smartapp.R;
import android.os.Bundle;

public class SelectTimeActivity extends BaseActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentForNav(R.layout.activity_select_time);
	}
}