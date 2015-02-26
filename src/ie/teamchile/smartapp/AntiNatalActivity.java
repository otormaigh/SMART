package ie.teamchile.smartapp;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TableRow;


public class AntiNatalActivity extends MenuInheritActivity {
	TableRow row1;
	TableRow row2;
	TableRow row3;
	TableRow row4;
	TableRow row5;
	TableRow row6;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_anti_natal);
		row5 = (TableRow) findViewById(R.id.button_parity);
		row6 = (TableRow) findViewById(R.id.button_obshistory);
        
        row5.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                Log.e("Click ", "Row 5");
        	  }
        });
        
        row6.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                Log.e("Click ", "Row 6");
        	  }
        });
	}
}