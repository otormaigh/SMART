package ie.teamchile.smartapp.activities;

import ie.teamchile.smartapp.R;
import android.app.Activity;
import android.os.Bundle;

public class SubMenuActivity extends MenuInheritActivity {
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentForNav(R.layout.activity_login);
	}
}