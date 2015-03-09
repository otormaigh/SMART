package utility;

import ie.teamchile.smartapp.R;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Locale;

import android.app.Dialog;
import android.content.Context;
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
	Dialog dialog;


	public MyAdapter(Context context, ArrayList<String>appointments) {
		this.appointments = appointments;
		this.context = context;
		inflater = LayoutInflater.from(context);	
		dialog = new Dialog(context);
		dialog.setContentView(R.layout.custom_alert);
		Button button = (Button)dialog.findViewById(R.id.buttonAlert);
		button.setText("Call");
		Button button2 = (Button)dialog.findViewById(R.id.buttonAlert2);
		button2.setText("Cancel");
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
			text2.setText(AppointmentSingleton.getInstance().getVisitType(appointments.get(position)));
			text1.setText(getReadableDate(AppointmentSingleton.getInstance().getDate(appointments).get(position)) + " at " + 
					AppointmentSingleton.getInstance().getTime(appointments).get(position));
			text3.setText(String.valueOf(UserSingleton.getUserSingleton().getID()));
			String myId = String.valueOf(UserSingleton.getUserSingleton().getID());
			ToastAlert ta = new ToastAlert(context, "Appointments for Service User: " + myId);

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
	
	private class ClickButtonListener implements OnClickListener {
		private int position;

		
		public ClickButtonListener(int position) {
			this.position = position;
		}

		@Override
		public void onClick(View v) {
			dialog.setTitle("Appointment ID " + appointments.get(position));
			dialog.show();						
		}		
	}
}
