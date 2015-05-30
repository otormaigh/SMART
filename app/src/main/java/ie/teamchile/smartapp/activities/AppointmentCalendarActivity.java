package ie.teamchile.smartapp.activities;

import ie.teamchile.smartapp.R;
import ie.teamchile.smartapp.model.ApiRootModel;
import ie.teamchile.smartapp.utility.RecyclerViewAdapter;
import ie.teamchile.smartapp.utility.SmartApi;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.daimajia.swipe.util.Attributes;

public class AppointmentCalendarActivity extends BaseActivity {
	private final int sdkVersion = Build.VERSION.SDK_INT;
	private static int serviceOptionSelected, weekSelected, clinicSelected;
	protected static Date daySelected;
	private Date openingAsDate, closingAsDate;
	private String clinicOpening, clinicClosing, closingMinusInterval,
				   dateSelectedStr, timeBefore, timeAfter, nameOfClinic;
	private int appointmentInterval, dayOfWeek;
	private List<String> timeSingle, gestSingle, nameSingle;
	private List<Integer> listOfApptId = new ArrayList<>();
	private Calendar c = Calendar.getInstance(), myCalendar = Calendar.getInstance();
	//private BaseAdapter adapter;
	private Intent intent;
	private ProgressDialog pd;
	private List<String> timeList = new ArrayList<>();
	private List<String> nameList = new ArrayList<>();
	private List<String> gestList = new ArrayList<>();
	private List<Integer> idList = new ArrayList<>();
	private Button dateInList, prevWeek, nextWeek;
	//private ListView listView;
	private RecyclerView recyclerView;
	private RecyclerView.Adapter recyclerAdapter;
	
	@Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
		setContentForNav(R.layout.activity_appointment_calendar);
        
        dateInList = (Button) findViewById(R.id.date_button);
        //listView = (ListView) findViewById(R.id.list);
        prevWeek = (Button) findViewById(R.id.prev_button);
        prevWeek.setOnClickListener(new ButtonClick());
        nextWeek = (Button) findViewById(R.id.next_button);
        nextWeek.setOnClickListener(new ButtonClick());

		recyclerView = (RecyclerView) findViewById(R.id.rv_appointment_list);
		recyclerView.setLayoutManager(new LinearLayoutManager(this));

		clinicOpening = ApiRootModel.getInstance().getClinicsMap().get(clinicSelected).getOpeningTime();
		clinicClosing = ApiRootModel.getInstance().getClinicsMap().get(clinicSelected).getClosingTime();
		appointmentInterval = ApiRootModel.getInstance().getClinicsMap().get(clinicSelected).getAppointmentInterval();
		try {
			openingAsDate = dfTimeOnly.parse(String.valueOf(clinicOpening));
			closingAsDate = dfTimeOnly.parse(String.valueOf(clinicClosing));
		} catch (ParseException e) {
			e.printStackTrace();
		}
				
		myCalendar.setTime(closingAsDate);
		myCalendar.add(Calendar.MINUTE, (-appointmentInterval));
		closingMinusInterval = dfTimeOnly.format(myCalendar.getTime());
       
		c.setTime(daySelected);
        dayOfWeek = c.get(Calendar.DAY_OF_WEEK);
        c.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
		
		//listView.setAdapter(null);
		recyclerView.setAdapter(null);
        newSetToList(daySelected);
        //adapter.notifyDataSetChanged();
		recyclerAdapter.notifyDataSetChanged();
        createDatePicker();

    }

    @Override
	protected void onNewIntent(Intent intent) { super.onNewIntent(intent); }

	@Override
	protected void onResume() {
		super.onResume();
		Log.d("bugs", "in onResume");
		Log.d("bugs", "daySelected: " + daySelected);
		//listView.setAdapter(null);
		//adapter.notifyDataSetChanged();
		recyclerView.setAdapter(null);
		recyclerAdapter.notifyDataSetChanged();
		newSetToList(daySelected);
	}

	private class ButtonClick implements View.OnClickListener {
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.prev_button:
                	c.setTime(daySelected);
                	c.add(Calendar.DAY_OF_YEAR, -7);
                	daySelected = c.getTime();
                	myCalendar.setTime(daySelected);
                	createDatePicker();
                	//listView.setAdapter(null);
					recyclerView.setAdapter(null);
                	newSetToList(c.getTime());
                	pauseButton();
                	break;
                case R.id.next_button:
                	c.setTime(daySelected);
                	c.add(Calendar.DAY_OF_YEAR, 7);
                	daySelected = c.getTime();
                	myCalendar.setTime(daySelected);
                	createDatePicker();
                	//listView.setAdapter(null);
					recyclerView.setAdapter(null);
                	newSetToList(c.getTime());
                	pauseButton();
                    break;
            }
        }
    }
    
    public void pauseButton(){
    	nextWeek.setEnabled(false);
    	prevWeek.setEnabled(false);
    	CountDownTimer nextTimer = new CountDownTimer(250, 250) {						
			@Override
			public void onTick(long millisUntilFinished) {								
			}						
			@Override
			public void onFinish() {	
				nextWeek.setEnabled(true);
				prevWeek.setEnabled(true);
			}
		};
		nextTimer.start();
    }
        
    public List<Integer> removeZeros(List<Integer> badList){
    	if(badList != null)
	    	for(int i = 0; i < badList.size(); i ++)
				if(badList.get(i).equals(0))
					badList.remove(i);
    	return badList;
    }
    
	private void createDatePicker() {
		myCalendar.setTime(daySelected);
		final DatePickerDialog.OnDateSetListener pickerDate = new DatePickerDialog.OnDateSetListener() {
			@Override
			public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
				myCalendar.set(Calendar.YEAR, year);
				myCalendar.set(Calendar.MONTH, monthOfYear);
				myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
				Log.d("postAppointment", "datePicker: " + myCalendar.getTime());
				Log.d("postAppointment", "datePicker formatted: " + 
						dfDateOnly.format(myCalendar.getTime()));
				Log.d("bugs", "c.getDay: " + dayOfWeek);
				Log.d("bugs", "myCalendar.getDay: " + myCalendar.get(Calendar.DAY_OF_WEEK));
				if(myCalendar.get(Calendar.DAY_OF_WEEK) == dayOfWeek){
					dateInList.setText(dfDateWMonthName.format(myCalendar.getTime()));
					newSetToList(myCalendar.getTime());					
				} else {
					pd = new ProgressDialog(AppointmentCalendarActivity.this);
					pd.setMessage("Invalid day selected\nPlease choose another");
					pd.show();
					new CountDownTimer(2000, 1000){
						@Override
						public void onFinish() {
							pd.cancel();
						}
						@Override
						public void onTick(long millisUntilFinished) {
						}        				
        			}.start();
				}
			}
		};
		dateInList.setOnClickListener(new OnClickListener() {
	        @Override
	        public void onClick(View v) {
	            new DatePickerDialog(AppointmentCalendarActivity.this, pickerDate, myCalendar
	                    .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
	                    myCalendar.get(Calendar.DAY_OF_MONTH)).show();	            
	        }
	    });
	}
	
	private void newSetToList(Date dateSelected){	
		timeSingle = new ArrayList<>();
    	nameSingle = new ArrayList<>();
    	gestSingle = new ArrayList<>();
    	listOfApptId = new ArrayList<>();

		timeList = new ArrayList<>();
		nameList = new ArrayList<>();
		gestList = new ArrayList<>();
		idList = new ArrayList<>();
		
		Date apptTime = openingAsDate;		
		daySelected = dateSelected;
		
		dateSelectedStr = dfDateOnly.format(dateSelected);		
		dateInList.setText(dfDateWMonthName.format(dateSelected));
    	nameOfClinic = ApiRootModel.getInstance().getClinicsMap().get(clinicSelected).getName();
		setActionBarTitle(nameOfClinic);

		if(ApiRootModel.getInstance().getClinicDateApptIdMap().containsKey(clinicSelected)){
			listOfApptId = ApiRootModel.getInstance().getClinicDateApptIdMap().get(clinicSelected).get(dateSelectedStr);
			//listOfApptId = removeZeros(listOfApptId);
		} else
			listOfApptId = new ArrayList<>();

		while(!closingAsDate.before(apptTime)){
			Log.d("appointment", "Free Slot Here");
			timeList.add(dfTimeOnly.format(apptTime));
			nameList.add("Free Slot");
			gestList.add("---------");
			idList.add(0);
			c.setTime(apptTime);
			c.add(Calendar.MINUTE, appointmentInterval);
			apptTime = c.getTime();
		}
		
		if (listOfApptId != null) {
			for(int i = 0; i < listOfApptId.size(); i++) {
				String timeOfAppt = "";
				try {
					timeOfAppt = dfTimeOnly.format(
							     	dfTimeWSec.parse(
                                    	ApiRootModel.getInstance().getIdApptMap().get(listOfApptId.get(i)).getTime()));
				} catch (ParseException e) {
					e.printStackTrace();
				}
				timeSingle.add(timeOfAppt);
				nameSingle.add(ApiRootModel.getInstance().getIdApptMap().get(listOfApptId.get(i)).getServiceUser().getName());
				gestSingle.add(ApiRootModel.getInstance().getIdApptMap().get(listOfApptId.get(i)).getServiceUser().getGestation());
			}

			Log.d("Retro", "timeSingle = " + timeSingle);

			for(int i = 0; i < timeSingle.size(); i++){
				if(timeList.contains(timeSingle.get(i))){
					int x = timeList.indexOf(timeSingle.get(i));
					if(nameList.get(x).equals("Free Slot")){
						idList.set(x, listOfApptId.get(i));
						timeList.set(x, timeSingle.get(i));
						nameList.set(x, nameSingle.get(i));
						gestList.set(x, gestSingle.get(i));
					} else {
						idList.add(x, listOfApptId.get(i));
						timeList.add(x, timeSingle.get(i));
						nameList.add(x, nameSingle.get(i));
						gestList.add(x, gestSingle.get(i));
					}
				}
			}
			Log.d("Retro", "timeLinst = " + timeList);
		}

		RecyclerView.OnScrollListener onScrollListener = new RecyclerView.OnScrollListener() {
			@Override
			public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
				super.onScrollStateChanged(recyclerView, newState);
				Log.e("ListView", "onScrollStateChanged");
			}

			@Override
			public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
				super.onScrolled(recyclerView, dx, dy);
			}
		};

		recyclerAdapter = new RecyclerViewAdapter(this, timeList, nameList, gestList, idList);
		((RecyclerViewAdapter) recyclerAdapter).setMode(Attributes.Mode.Single);
		recyclerView.setAdapter(recyclerAdapter);

        /* Listeners */
		recyclerView.setOnScrollListener(onScrollListener);

		/*adapter = new ListElementAdapter (AppointmentCalendarActivity.this,
					timeList, nameList, gestList);
		adapter.notifyDataSetChanged();
        listView.setAdapter(adapter);
        adapter.notifyDataSetChanged();
        listView.setOnItemClickListener(new OnItemListener());*/
	}
    
    /*private class ListElementAdapter extends BaseAdapter {
		LayoutInflater layoutInflater;
		List<String> aptTime, aptName, aptGest;

		@Override
		public void notifyDataSetChanged() { super.notifyDataSetChanged(); }
		
		public ListElementAdapter(Context context, List<String> aptTime, 
								  List<String> aptName, List<String> aptGest) {
			super();
			Log.d("MYLOG", "daySelected: " + daySelected);
			Log.d("MYLOG", "List Adapter Called");
			this.aptTime = aptTime;
			this.aptName = aptName;
			this.aptGest = aptGest;
			layoutInflater = LayoutInflater.from(context);
		}
		
		@Override
		public int getCount() { return aptTime.size(); }		
		@Override
		public Object getItem(int position) { return null; }		
		
		@Override
		public long getItemId(int position) { return position; }
		
		@Override
		public View getView(final int position, View convertView, ViewGroup parent) {
			convertView = layoutInflater.inflate(R.layout.list_appointment_layout, null);
			TextView timeText = (TextView) convertView.findViewById(R.id.time);
			TextView nameText = (TextView) convertView.findViewById(R.id.name);
			TextView gestText = (TextView) convertView.findViewById(R.id.gestation);			
			
			if(idList.get(position).equals(0)){
				timeText.setText(aptTime.get(position));
				nameText.setText(aptName.get(position));
				gestText.setText(aptGest.get(position));
				
				nameText.setTextColor(getResources().getColor(R.color.green));
				nameText.setShadowLayer(1, 0, 0, getResources().getColor(R.color.black));
			} else {
				timeText.setText(aptTime.get(position));
				nameText.setText(aptName.get(position));
				gestText.setText(aptGest.get(position));
			}
			return convertView;
		}
	}*/
    
    private class OnItemListener implements OnItemClickListener {
		@Override
		public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3) {
			if(idList.get(position).equals(0)){
				intent = new Intent(AppointmentCalendarActivity.this, CreateAppointmentActivity.class);
				intent.putExtra("from", "appointment");
				intent.putExtra("time", timeList.get(position));
				intent.putExtra("clinicID", String.valueOf(clinicSelected));
				startActivity(intent);				
			} else {
				int serviceUserId = ApiRootModel.getInstance().getIdApptMap().get(idList.get(position)).getServiceUserId();
				Log.d("bugs", "db string: " + "service_users" + "/" + serviceUserId);
				pd = new ProgressDialog(AppointmentCalendarActivity.this);
				pd.setMessage("Fetching Information");
				pd.setCanceledOnTouchOutside(false);
				pd.setCancelable(false);
				pd.show();
				intent = new Intent(AppointmentCalendarActivity.this, ServiceUserActivity.class);
				searchServiceUser(serviceUserId, intent);
				//new LongOperation(AppointmentCalendarActivity.this).execute("service_users" + "/" + serviceUserId);

			}
		}		    	
    }

	private void searchServiceUser(int serviceUserId, final Intent intent) {
		api.getServiceUserById(serviceUserId,
				ApiRootModel.getInstance().getLogin().getToken(),
				SmartApi.API_KEY,
				new Callback<ApiRootModel>() {
					@Override
					public void success(ApiRootModel apiRootModel, Response response) {
						ApiRootModel.getInstance().setServiceUsers(apiRootModel.getServiceUsers());
						ApiRootModel.getInstance().setBabies(apiRootModel.getBabies());
						ApiRootModel.getInstance().setPregnancies(apiRootModel.getPregnancies());
						startActivity(intent);
						pd.dismiss();
					}

					@Override
					public void failure(RetrofitError error) {
						pd.dismiss();
						Toast.makeText(
								AppointmentCalendarActivity.this,
								"Error Search Patient: " + error,
								Toast.LENGTH_LONG).show();
					}
				});
	}
    
    public void setServiceOptionSelected(int serviceOptionSelected){
    	AppointmentCalendarActivity.serviceOptionSelected = serviceOptionSelected;
    }

    public void setClinicSelected(int clinicSelected){
    	AppointmentCalendarActivity.clinicSelected = clinicSelected;
    }

    public void setWeekSelected(int weekSelected){
    	AppointmentCalendarActivity.weekSelected = weekSelected;
    }

	public void setDaySelected(Date daySelected){
    	AppointmentCalendarActivity.daySelected = daySelected;
    }
}