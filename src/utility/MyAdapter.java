package utility;

import ie.teamchile.smartapp.R;

import java.util.ArrayList;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
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


	public MyAdapter(Context context, ArrayList<String>appointments) {
		this.appointments = appointments;
		this.context = context;
		inflater = LayoutInflater.from(context);
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
			TextView text1 = (TextView)view.findViewById(R.id.name);
			TextView text2 = (TextView)view.findViewById(R.id.latest_message);
			ImageView iv = (ImageView)view.findViewById(R.drawable.ic_launcher);
			Button button = (Button)view.findViewById(R.id.detailBtn);
			button.setOnClickListener(new ButtonClickListener());
			String id = getItem(position);
			text1.setText("Appointment ID: " + id);
			//String theId = AppointmentSingleton.getSingletonIntance().getClinicID(id);
			//text2.setText();
		}else {
			view = convertView;
		}
		return view;
	}
	
	private class ButtonClickListener implements OnClickListener {

		@Override
		public void onClick(View v) {
			int id = v.getId();
			switch(id) {
			case R.id.detailBtn :
			}			
		}		
	}
	
}
