package ie.teamchile.smartapp;


import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TableRow;
import android.widget.TextView;

public class AnteNatalActivity extends MenuInheritActivity {
	TableRow row1;
	TableRow row2;
	TableRow row3;
	TableRow row4;
	TableRow row5;
	TableRow row6;
	private TextView age;
	private TextView nameAntiNatal;
	


	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_ante_natal);
		row5 = (TableRow) findViewById(R.id.button_parity);
		row6 = (TableRow) findViewById(R.id.button_obshistory);
		age = (TextView)findViewById(R.id.age);
		nameAntiNatal = (TextView)findViewById(R.id.name_anti_natal);

		age.setText(getIntent().getStringExtra("age"));
		nameAntiNatal.setText(getIntent().getStringExtra("name"));
	


        row5.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
               
        	  }
        });
        
        row6.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
        	  }
        });
	}

}