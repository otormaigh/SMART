package ie.teamchile.smartapp.activities;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.daimajia.swipe.SwipeLayout;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import ie.teamchile.smartapp.R;
import ie.teamchile.smartapp.model.BaseModel;
import ie.teamchile.smartapp.model.Appointment;
import ie.teamchile.smartapp.model.PostingData;
import ie.teamchile.smartapp.util.NotKeys;
import ie.teamchile.smartapp.util.SmartApi;
import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class AppointmentCalendarActivity extends BaseActivity {
	private final int sdkVersion = Build.VERSION.SDK_INT;
	private static int serviceOptionSelected, weekSelected, clinicSelected, visitOptionSelected;
	protected static Date daySelected;
	private Date openingAsDate, closingAsDate;
	private String clinicOpening, clinicClosing, closingMinusInterval,
				   dateSelectedStr, timeBefore, timeAfter, nameOfClinic;
	private int appointmentInterval, dayOfWeek;
	private List<String> timeSingle, gestSingle, nameSingle;
	private List<Integer> listOfApptId = new ArrayList<>();
	private Calendar c = Calendar.getInstance(), myCalendar = Calendar.getInstance();
	private Intent intent;
	private List<String> timeList = new ArrayList<>();
	private List<String> nameList = new ArrayList<>();
	private List<String> gestList = new ArrayList<>();
	private List<Integer> idList = new ArrayList<>();
	private int serviceOptionId;
	private List<Boolean> attendedList = new ArrayList<>();
	private List<Boolean> attendedSingle = new ArrayList<>();
	private Button dateInList, btnPrevWeek, btnNextWeek;
	private BaseAdapter adapter;
	private ListView listView;

	@Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
		setContentForNav(R.layout.activity_appointment_calendar);

        Log.d("bugs", "clinicSelected = " + clinicSelected);
        
        dateInList = (Button) findViewById(R.id.btn_date);
        listView = (ListView) findViewById(R.id.lv_appointment_list);
        btnPrevWeek = (Button) findViewById(R.id.btn_prev);
        btnPrevWeek.setOnClickListener(new ButtonClick());
        btnNextWeek = (Button) findViewById(R.id.btn_next);
        btnNextWeek.setOnClickListener(new ButtonClick());

        serviceOptionId = BaseModel.getInstance().getClinicMap().get(clinicSelected).getServiceOptionIds().get(0);
		clinicOpening = BaseModel.getInstance().getClinicMap().get(clinicSelected).getOpeningTime();
		clinicClosing = BaseModel.getInstance().getClinicMap().get(clinicSelected).getClosingTime();
		appointmentInterval = BaseModel.getInstance().getClinicMap().get(clinicSelected).getAppointmentInterval();
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
		
		listView.setAdapter(null);
        newSetToList(daySelected);
        adapter.notifyDataSetChanged();
        createDatePicker();
	}

    @Override
	protected void onNewIntent(Intent intent) { super.onNewIntent(intent); }

	@Override
	protected void onResume() {
		super.onResume();
		Log.d("bugs", "in onResume");
		Log.d("bugs", "daySelected: " + daySelected);
		listView.setAdapter(null);
		adapter.notifyDataSetChanged();
		newSetToList(daySelected);
	}

	private class ButtonClick implements View.OnClickListener {
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.btn_prev:
                	c.setTime(daySelected);
                	c.add(Calendar.DAY_OF_YEAR, -7);
                	daySelected = c.getTime();
                	myCalendar.setTime(daySelected);
                	createDatePicker();
                	listView.setAdapter(null);
                	newSetToList(c.getTime());
                	pauseButton();
                	break;
                case R.id.btn_next:
                	c.setTime(daySelected);
                	c.add(Calendar.DAY_OF_YEAR, 7);
                	daySelected = c.getTime();
                	myCalendar.setTime(daySelected);
                	createDatePicker();
                	listView.setAdapter(null);
                	newSetToList(c.getTime());
                	pauseButton();
                    break;
            }
        }
    }
    
    public void pauseButton(){
    	btnNextWeek.setEnabled(false);
    	btnPrevWeek.setEnabled(false);
    	CountDownTimer nextTimer = new CountDownTimer(250, 250) {						
			@Override
			public void onTick(long millisUntilFinished) {								
			}						
			@Override
			public void onFinish() {	
				btnNextWeek.setEnabled(true);
				btnPrevWeek.setEnabled(true);
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
                if(pd != null){
                  pd.dismiss();
                }
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
                    Log.d("bugs", "wrong date");
					showProgressDialog(AppointmentCalendarActivity.this,
                            "Invalid day selected\nPlease choose another");
                    Handler mHandler = new Handler();
                    mHandler.postDelayed(new Runnable() {
                        public void run() {
                            pd.dismiss();
                            Log.d("bugs", "dismissed");
                            Log.d("bugs", "dismissed" + pd.isShowing());
                        }
                    }, 2000);
                    /*new CountDownTimer(2000, 1000){
						@Override
						public void onFinish() {
                            Log.d("bugs", "time finished");
							//pd.dismiss();
						}
						@Override
						public void onTick(long millisUntilFinished) {
						}        				
        			}.start();*/
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
		attendedSingle = new ArrayList<>();
    	listOfApptId = new ArrayList<>();

		timeList = new ArrayList<>();
		nameList = new ArrayList<>();
		gestList = new ArrayList<>();
		attendedList = new ArrayList<>();
		idList = new ArrayList<>();
		
		Date apptTime = openingAsDate;		
		daySelected = dateSelected;
		
		dateSelectedStr = dfDateOnly.format(dateSelected);		
		dateInList.setText(dfDateWMonthName.format(dateSelected));
    	nameOfClinic = BaseModel.getInstance().getClinicMap().get(clinicSelected).getName();
		setActionBarTitle(nameOfClinic);

		if(BaseModel.getInstance().getClinicVisitClinicDateApptIdMap().containsKey(clinicSelected)){
			listOfApptId = BaseModel.getInstance().getClinicVisitClinicDateApptIdMap().get(clinicSelected).get(dateSelectedStr);
			//listOfApptId = removeZeros(listOfApptId);
		} else
			listOfApptId = new ArrayList<>();

		while(!closingAsDate.before(apptTime)){
			Log.d("appointment", "Free Slot Here");
			timeList.add(dfTimeOnly.format(apptTime));
			nameList.add("Free Slot");
			gestList.add("");
			attendedList.add(false);
			idList.add(0);
			c.setTime(apptTime);
			c.add(Calendar.MINUTE, appointmentInterval);
			apptTime = c.getTime();
		}
		if (listOfApptId != null) {
			for(int i = 0; i < listOfApptId.size(); i++) {
				Appointment appointment = BaseModel.getInstance().getClinicVisitIdApptMap().get(listOfApptId.get(i));
				String timeOfAppt = "";
				try {
					timeOfAppt = dfTimeOnly.format(dfTimeWSec.parse(appointment.getTime()));
				} catch (ParseException e) {
					e.printStackTrace();
				}
				timeSingle.add(timeOfAppt);
				nameSingle.add(appointment.getServiceUser().getName());
				gestSingle.add(appointment.getServiceUser().getGestation());
				if(appointment.getAttended() == null)
					attendedSingle.add(false);
				else
					attendedSingle.add(appointment.getAttended());
			}

			for(int i = 0; i < timeSingle.size(); i++){
				if(timeList.contains(timeSingle.get(i))){
					int x = timeList.indexOf(timeSingle.get(i));
					if(nameList.get(x).equals("Free Slot")){
						idList.set(x, listOfApptId.get(i));
						timeList.set(x, timeSingle.get(i));
						nameList.set(x, nameSingle.get(i));
						gestList.set(x, gestSingle.get(i));
						attendedList.set(x, attendedSingle.get(i));
					} else {
						idList.add(x, listOfApptId.get(i));
						timeList.add(x, timeSingle.get(i));
						nameList.add(x, nameSingle.get(i));
						gestList.add(x, gestSingle.get(i));
						attendedList.add(x, attendedSingle.get(i));
					}
				}
			}
		}

		adapter = new ListElementAdapter (AppointmentCalendarActivity.this,
					timeList, nameList, gestList, attendedList);
		adapter.notifyDataSetChanged();
        listView.setAdapter(adapter);
        adapter.notifyDataSetChanged();
	}

    private class ListElementAdapter extends BaseAdapter {
		LayoutInflater layoutInflater;
		List<String> aptTime, aptName, aptGest;
		List<Boolean> attendedList;

		@Override
		public void notifyDataSetChanged() { super.notifyDataSetChanged(); }
		
		public ListElementAdapter(Context context, List<String> aptTime, 
								  List<String> aptName, List<String> aptGest,
								  List<Boolean> attendedList) {
			super();
			Log.d("MYLOG", "daySelected: " + daySelected);
			Log.d("MYLOG", "List Adapter Called");
			this.aptTime = aptTime;
			this.aptName = aptName;
			this.aptGest = aptGest;
			this.attendedList = attendedList;
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
			convertView = layoutInflater.inflate(R.layout.list_layout_appointment, null);
			TextView timeText = (TextView) convertView.findViewById(R.id.tv_time);
			TextView nameText = (TextView) convertView.findViewById(R.id.tv_name);
			TextView gestText = (TextView) convertView.findViewById(R.id.tv_gestation);
			final Button btnChangeStatus = (Button) convertView.findViewById(R.id.btn_change_status);
			final ImageView ivAttend = (ImageView) convertView.findViewById(R.id.img_attended);
			final SwipeLayout swipeLayout =  (SwipeLayout) convertView.findViewById(R.id.swipe_appt_list);
			LinearLayout llApptListItem = (LinearLayout) convertView.findViewById(R.id.ll_appt_list_item);
			llApptListItem.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    if (idList.get(position).equals(0)) {
                        intent = new Intent(AppointmentCalendarActivity.this, CreateAppointmentActivity.class);
                        intent.putExtra("from", "clinic-appointment");
                        intent.putExtra("time", timeList.get(position));
                        intent.putExtra("clinicID", String.valueOf(clinicSelected));
                        intent.putExtra("serviceOptionId", String.valueOf(serviceOptionId));
                        startActivity(intent);
                    } else {
                        int serviceUserId = BaseModel.getInstance().getClinicVisitIdApptMap().get(idList.get(position)).getServiceUserId();
                        Log.d("bugs", "db string: " + "service_users" + "/" + serviceUserId);
                        showProgressDialog(AppointmentCalendarActivity.this,
                                "Fetching Information");
                        intent = new Intent(AppointmentCalendarActivity.this, ServiceUserActivity.class);
                        searchServiceUser(serviceUserId, intent);
                    }
                    return true;
                }
            });

			btnChangeStatus.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {

					Log.d("Button", "position = " + position);
					swipeLayout.close();
					if (!attendedList.get(position)) {
						changeAttendStatus(true, position);
						ivAttend.setBackgroundResource(R.color.attended);
						notifyDataSetChanged();
					} else if (attendedList.get(position)) {
						changeAttendStatus(false, position);
						ivAttend.setBackgroundResource(R.color.unattended);
                        notifyDataSetChanged();
					}
				}
			});

			if(idList.get(position).equals(0)){
				timeText.setText(aptTime.get(position));
				nameText.setText(aptName.get(position));
				gestText.setText(aptGest.get(position));
				swipeLayout.setSwipeEnabled(false);
				
				nameText.setTextColor(getResources().getColor(R.color.free_slot));
				nameText.setTypeface(null, Typeface.ITALIC);
			} else {
				timeText.setText(aptTime.get(position));
				nameText.setText(aptName.get(position));
				gestText.setText(aptGest.get(position));

				if(attendedList.get(position)) {
                    ivAttend.setBackgroundResource(R.color.attended);
                    btnChangeStatus.setText("No");
                    //btnChangeStatus.setEnabled(false);
                } else if (!attendedList.get(position)) {
                    ivAttend.setBackgroundResource(R.color.unattended);
                    btnChangeStatus.setText("Yes");
                    //btnChangeStatus.setEnabled(true);
                }
			}
			return convertView;
		}
	}

	private void changeAttendStatus(Boolean status, int position){
		showProgressDialog(AppointmentCalendarActivity.this, "Changing Attended Status");
		attendedList.set(position, status);

		PostingData attendedStatus = new PostingData();
		attendedStatus.putAppointmentStatus(
				status,
				clinicSelected,
				BaseModel.getInstance().getLogin().getId(),
				BaseModel.getInstance().getClinicVisitIdApptMap().get(idList.get(position)).getServiceUserId());

		api.putAppointmentStatus(
				attendedStatus,
				idList.get(position),
				BaseModel.getInstance().getLogin().getToken(),
				NotKeys.API_KEY,
				new Callback<BaseModel>() {
					@Override
					public void success(BaseModel baseModel, Response response) {
						Toast.makeText(AppointmentCalendarActivity.this,
								"status changed", Toast.LENGTH_LONG).show();
						pd.dismiss();
					}

					@Override
					public void failure(RetrofitError error) {
						Log.d("Attended", "retro error = " + error);
						pd.dismiss();
					}
				}
		);
	}

	private void searchServiceUser(int serviceUserId, final Intent intent) {
		api.getServiceUserById(serviceUserId,
				BaseModel.getInstance().getLogin().getToken(),
				NotKeys.API_KEY,
				new Callback<BaseModel>() {
					@Override
					public void success(BaseModel baseModel, Response response) {
						BaseModel.getInstance().setServiceUsers(baseModel.getServiceUsers());
						BaseModel.getInstance().setBabies(baseModel.getBabies());
						BaseModel.getInstance().setPregnancies(baseModel.getPregnancies());
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

    public void setVisitOption(int visitOptionSelected){
        AppointmentCalendarActivity.visitOptionSelected = visitOptionSelected;
    }
}