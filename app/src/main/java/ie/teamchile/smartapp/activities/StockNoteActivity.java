package ie.teamchile.smartapp.activities;

import ie.teamchile.smartapp.R;
import android.app.Activity;
import android.os.Bundle;

public class StockNoteActivity extends MenuInheritActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentForNav(R.layout.activity_stock_note);
	}
}
