package ie.teamchile.smartapp.activities;

import ie.teamchile.smartapp.R;
import ie.teamchile.smartapp.retrofit.ApiRootModel;
import ie.teamchile.smartapp.retrofit.Baby;

import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

public class ParityDetailsActivity extends MenuInheritActivity {
	private BaseAdapter adapter;
	private TextView name, babyName;
	private String patientName, patientParity;
	private String sex_male = "ale", sex_female = "emale";
	private List<String> nameBaby = new ArrayList<>();
	private List<String> hospitalNumber = new ArrayList<>();
	private List<String> dobBaby = new ArrayList<>();
	private List<String> genderBaby = new ArrayList<>();
	private List<String> gestationBaby = new ArrayList<>();
	private List<Integer> weightBaby = new ArrayList<>();
	private List<String> weightBabyInKg = new ArrayList<>();
	private List<String> birthMode = new ArrayList<>();
	private List<String> birthOutcome = new ArrayList<>();
	private List<String> dobStr = new ArrayList<>();
	private ListView parityList;
	private int orientation;
	private DateFormat sdfMonthFullName = new SimpleDateFormat("dd MMMM yyyy", Locale.getDefault());
	private DateFormat sdfDateTime = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZ", Locale.getDefault());
	private DateFormat sdfDate = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentForNav(R.layout.activity_parity_details);
		parityList = (ListView)findViewById(R.id.parity_list);

		patientName = ApiRootModel.getInstance().getServiceUsers().get(0).getPersonalFields().getName();
		patientParity = ApiRootModel.getInstance().getServiceUsers().get(0).getClinicalFields().getParity();
		setActionBarTitle(patientName + " (" + patientParity + ")");

		List<Baby> babyList = ApiRootModel.getInstance().getBabies();

		for(int i = 0; i < babyList.size(); i++){
			nameBaby.add(babyList.get(i).getName());
			hospitalNumber.add(babyList.get(i).getHospitalNumber());
			dobBaby.add(babyList.get(i).getDeliveryDateTime());
			genderBaby.add(babyList.get(i).getGender());
			gestationBaby.add(ApiRootModel.getInstance().getPregnancies().get(i).getGestation());
			weightBaby.add(babyList.get(i).getWeight());
			birthMode.add(putArrayToString(ApiRootModel.getInstance().getPregnancies().get(i).getBirthMode()));
			birthOutcome.add(babyList.get(i).getBirthOutcome());
		}

        dobStr = new ArrayList<>();
        Log.d("bugs", "gestation list oncreate = " + gestationBaby.toString());
		
		for(int i = 0; i < dobBaby.size(); i++){
			dobStr.add(dobBaby.get(i) + "\n");
		}
		  
		for(int i = 0; i < weightBaby.size(); i ++){
			String kg = getGramsToKg(weightBaby.get(i));
			weightBabyInKg.add(kg);
		}
		
		switch(getScreenOrientation()){
		case 1:				//ORIENTATION_PORTRAIT
			portraitCode();
			break;
		case 2:				//ORIENTATION_LANDSCAPE
			landscapeCode();
			break;
		}		
	}
	
	@Override
	protected void onNewIntent(Intent intent) {
	    super.onNewIntent(intent);
	    setIntent(intent);
	}

	private int getScreenOrientation() {
	    Display getOrient = this.getWindowManager().getDefaultDisplay();
	    int orientation = Configuration.ORIENTATION_UNDEFINED;
	    if(getOrient.getWidth()==getOrient.getHeight()){
	        orientation = Configuration.ORIENTATION_SQUARE;
	    } else{ 
	        if(getOrient.getWidth() < getOrient.getHeight()){
	            orientation = Configuration.ORIENTATION_PORTRAIT;
	        }else { 
	             orientation = Configuration.ORIENTATION_LANDSCAPE;
	        }
	    }
	    this.orientation = orientation;
	    return orientation;
	}
	
	private void portraitCode(){
		babyName = (TextView)findViewById(R.id.baby_name_parity);
/*		parity =(TextView)findViewById(R.id.parity_info);
		
		parity.setText(patientParity);*/
		
		adapter = new ListAdapter(ParityDetailsActivity.this, 
				 orientation, nameBaby, hospitalNumber, dobStr, 
				 genderBaby, gestationBaby, weightBabyInKg,
				 birthMode, birthOutcome);
		parityList.setAdapter(adapter);
        //listView.setOnItemClickListener(new OnItemListener());		
	}
	
	private void landscapeCode(){
		adapter = new ListAdapter(ParityDetailsActivity.this, 
				 orientation, dobStr, genderBaby, gestationBaby,
				weightBabyInKg, birthMode, birthOutcome);
		parityList.setAdapter(adapter);
	}
	
    public String getGramsToKg(int grams){
    	double kg;
    	kg = grams/1000.0;
    	return String.valueOf(kg);
    }
	
	private class ListAdapter extends BaseAdapter {
		Context context;
		int position, orientation;
		LayoutInflater layoutInflater;
		List<String> babyDob, hospitalNumber, babyGender, gestation,
				 	 weight, name, birthMode, birthOutcome; 	

		@Override
		public void notifyDataSetChanged() {
			super.notifyDataSetChanged();
		}
		public ListAdapter(Context context, int orientation, List<String> name,  
				List<String>hospitalNumber, List<String>babyDob, List<String>babyGender, 
				List<String>gestation, List<String>weight, List<String>birthMode, List<String>birthOutcome) {
			super();
			
			this.context = context;
			this.orientation = orientation;
			this.babyDob = babyDob;
			this.hospitalNumber = hospitalNumber;
			this.name = name;
			this.babyGender = babyGender;
			this.gestation = gestation;
			this.weight = weight;
			this.birthMode = birthMode;
			this.birthOutcome = birthOutcome;
			layoutInflater = LayoutInflater.from(context);
		}
		
		public ListAdapter(Context context, int orientation, List<String>babyDob, 
				List<String>babyGender, List<String>gestation, List<String>weight, 
				List<String>birthMode, List<String>birthOutcome) {
			super();
			
			this.context = context;
			this.orientation = orientation;
			this.babyDob = babyDob;
			this.gestation = gestation;
			this.babyGender = babyGender;
			this.weight = weight;
			this.birthMode = birthMode;
			this.birthOutcome = birthOutcome;
			layoutInflater = LayoutInflater.from(context);
		}
		
		@Override
		public int getCount() {
			return gestation.size();
		}
		@Override
		public Object getItem(int position) {
			return null;
		}
		@Override
		public long getItemId(int position) {
			return position;
		}
		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			//ButterKnife.inject(this, convertView);
			this.position = position;
			
			convertView = layoutInflater.inflate(R.layout.parity_list_layout, parent, false);
			TextView dobText = (TextView) convertView.findViewById(R.id.dob_parity);
			TextView genderText = (TextView) convertView.findViewById(R.id.gender_parity);
			TextView gestationText = (TextView) convertView.findViewById(R.id.gestation_parity);
			TextView weightText = (TextView) convertView.findViewById(R.id.weight_parity);
			TextView modeText = (TextView) convertView.findViewById(R.id.mode_of_birth_parity);
			TextView birthOutcomeParity = (TextView) convertView.findViewById(R.id.birth_outcome_parity);

			if(babyGender.get(position).equalsIgnoreCase("M")){
				genderText.setText(babyGender.get(position) + sex_male);
			}
			else if (babyGender.get(position).equalsIgnoreCase("F")){
				genderText.setText(babyGender.get(position) + sex_female);
			}	
			gestationText.setText(gestation.get(position));			
			weightText.setText(formatWeight(weight.get(position)));
			modeText.setText(formatArrayString(birthMode.get(position)));
			birthOutcomeParity.setText(birthOutcome.get(position));
			
			switch(orientation){
			case 1:				//ORIENTATION_PORTRAIT
				TextView nameOfBaby = (TextView) convertView.findViewById(R.id.baby_name_parity);
				TextView hospitalNo = (TextView) convertView.findViewById(R.id.hospital_number_parity);			
				
				nameOfBaby.setText(name.get(position));
				hospitalNo.setText(hospitalNumber.get(position));
				dobText.setText(getDeliveryDate(babyDob.get(position), 0));
				break;
			case 2:				//ORIENTATION_LANDSCAPE
				dobText.setText(getDeliveryDate(babyDob.get(position), 1));
				break;
			}			
			
			return convertView;			
		}
	}
	
	private String getDeliveryDate(String edd, int option) {
		Date date = null;
		String dateOfDevelivery = null;
		try {
			date = sdfDateTime.parse(edd);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		switch (option) {
		case 0:
			dateOfDevelivery = sdfMonthFullName.format(date);
			break;
		case 1:
			dateOfDevelivery = sdfDate.format(date);
			break;
		}

		return dateOfDevelivery;
	}
	
    private String formatArrayString(String toBeFormatted){
    	String formatedString = toBeFormatted
    		    .replace(",", "")  //remove the commas
    		    .replace("[", "")  //remove the right bracket
    		    .replace("]", "")  //remove the left bracket
    		    .replace("\"", "")
    		    .trim(); 
    	return formatedString;
    }
    
    private String formatWeight(String weightStr){
    	DecimalFormat df = new DecimalFormat("#.00"); 
    	double weight = Double.parseDouble(weightStr);
    	
    	return df.format(weight);
    }
}
