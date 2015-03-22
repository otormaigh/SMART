package ie.teamchile.smartapp.utility;

/**
 * Custom Base Adapter Class
 * Barry Dempsey 10.03.15
 */

import ie.teamchile.smartapp.R;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Locale;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.CountDownTimer;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class MyAdapter extends BaseAdapter {
	private ArrayList<String>appointments;
	private LayoutInflater inflater;
	private Context context;
	private Button callBtn, cancelBtn;
	Dialog dialog;


	public MyAdapter(Context context, ArrayList<String>appointments) {
		this.appointments = appointments;
		this.context = context;
		inflater = LayoutInflater.from(context);	
		dialog = new Dialog(context);
		dialog.setContentView(R.layout.custom_alert);
		callBtn = (Button)dialog.findViewById(R.id.buttonAlert);
		callBtn.setText("Call");
		cancelBtn = (Button)dialog.findViewById(R.id.buttonAlert2);
		cancelBtn.setText("Cancel");
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return appointments.size();
	}

	@Override
	public String getItem(int position) {
		// TODO Auto-generated method stub
		return appointments.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		View view = null;
		if(convertView == null) {
			view = inflater.inflate(R.layout.row_layout, parent, false);
			view.setOnClickListener(new ClickButtonListener(position));
			TextView text1 = (TextView)view.findViewById(R.id.name);
			TextView text2 = (TextView)view.findViewById(R.id.appt_type);
			TextView text3 = (TextView)view.findViewById(R.id.appt_info);
			ImageView iv = (ImageView)view.findViewById(R.drawable.ic_launcher);
			String id = getItem(position);
			text2.setText(AppointmentSingleton.getInstance().getName(appointments.get(position)));
			text1.setText(getReadableDate(AppointmentSingleton.getInstance().getDate(appointments).get(position)) + " at " + 
					removeTheSeconds(AppointmentSingleton.getInstance().getTime(appointments).get(position)));
			text3.setText(AppointmentSingleton.getInstance().getGestation(appointments.get(position)));

		}else {
			view = convertView;
		}
		return view;
	}	
	
	private String getReadableDate(String date) {
		SimpleDateFormat reqFormat = new SimpleDateFormat("dd MMM, yyyy", Locale.UK);
		SimpleDateFormat currentFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.UK);
		String reformattedStr = null;
		try {

		     reformattedStr = reqFormat.format(currentFormat.parse(date));
		} catch (java.text.ParseException e) {
		    e.printStackTrace();
		}
		return reformattedStr;
	}
	
	private String removeTheSeconds(String time) {
		String truncTime = time.substring(0, time.length() - 3);
		return truncTime;
	}
	
	private class ClickButtonListener implements OnClickListener {
		private int position;
		
		public ClickButtonListener(int position) {
			this.position = position;
		}

		@Override
		public void onClick(final View v) {
			ServiceUserSingleton.getInstance().getPatientInfo(appointments.get(position));
			ToastAlert ta = new ToastAlert(context, "Loading data. . . ", false);
			v.setBackgroundColor(Color.GRAY);
			
			CountDownTimer timer = new CountDownTimer(1000, 1000) {

				@Override
				public void onTick(long millisUntilFinished) {			
				}

				@Override
				public void onFinish() {
					dialog.setTitle(removeTheBrackets(ServiceUserSingleton.getInstance().getUserHomeAddress().toString()));
					callBtn.setOnClickListener(new OnClickListener() {

						@Override
						public void onClick(View v) {
							Intent i = new Intent(Intent.ACTION_CALL);
							String phone = ServiceUserSingleton.getInstance().getUserMobilePhone().get(0);
							i.setData(Uri.parse("tel: " + phone));
							context.startActivity(i);							
						}
					});
					dialog.show();		
					v.setBackgroundColor(Color.TRANSPARENT);
				}				
			}.start();
		}		
	}
	
	private String removeTheBrackets(String address) {
		return address.substring(1, address.length()-1);
	}
}