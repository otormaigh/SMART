package ie.teamchile.smartapp;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TableRow;

public class PostNatalActivity extends MenuInheritActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_post_natal);
		TableRow row5 = (TableRow) findViewById(R.id.button_parity);
		TableRow row6 = (TableRow) findViewById(R.id.button_obshistory);
        
	}
}