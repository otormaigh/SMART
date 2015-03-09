package ie.teamchile.smartapp;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

import utility.AppointmentSingleton;
import utility.MyAdapter;
import android.app.Dialog;
import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;

public class TodayAppointmentActivity extends ListActivity {
	private ArrayList<String>values;
	private Calendar cal;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		cal = Calendar.getInstance();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.UK);
		String date = sdf.format(cal.getTime());
		
		values = new ArrayList<String>();		
		values = AppointmentSingleton.getInstance().getListOfIDs("4", "2015-03-05");

		if(values.size() == 0 || values == null) {
			values.add("Not appointments today");
		}
		//printMap(hash);
		MyAdapter adapter = new MyAdapter(this, values);
		setListAdapter(adapter);
		
		//buildTheDialog();
	}	
	
	private void buildTheDialog() {
		
		Dialog myDialog = new Dialog(this);
		myDialog.setContentView(R.layout.custom_alert);
		myDialog.setTitle("My Custom Dialog");
		Button posButton = (Button)myDialog.findViewById(R.id.buttonAlert);
		ImageView iv = (ImageView) myDialog.findViewById(R.id.icon);
		posButton.setText("Make the phone call?");
		posButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				
			}
			
		});
		myDialog.show();
	}
	
	private class ButtonClicker implements OnClickListener {

		@Override
		public void onClick(View v) {
			Intent i = new Intent(Intent.ACTION_DIAL);
			
		}		
	}
}