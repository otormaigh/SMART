package ie.teamchile.smartapp;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import utility.ServiceUserSingleton;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;


public class PostNatalActivity extends MenuInheritActivity {
	
	private TextView name, birth_mode, perineum, anti_d, deliveryDate, deliveryTime, daysSinceBirth, sex_of_baby, 
	        birth_weight, vitK, hearing, feeding, nbst;
	private ImageView userImage, anteNatal;
	SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZ");
	ServiceUserActivity ab = new ServiceUserActivity();
	Date dateOfDelivery = null;
	Date currentDate = null;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_post_natal);
	    name = (TextView)findViewById(R.id.name_post_natal);
		userImage = (ImageView)findViewById(R.id.user_image_post_natal);
		userImage.setOnClickListener(new postNatalOptions());
		anteNatal = (ImageView)findViewById(R.id.ante_natal_from_post_natal);
		anteNatal.setOnClickListener(new postNatalOptions());
		birth_mode = (TextView)findViewById(R.id.birth_mode);
		perineum = (TextView)findViewById(R.id.perineum);
		anti_d = (TextView)findViewById(R.id.anti_d);
		deliveryDate = (TextView)findViewById(R.id.date_of_delivery);
		deliveryTime = (TextView)findViewById(R.id.time_of_delivery);
		daysSinceBirth = (TextView)findViewById(R.id.days_since_birth);
		sex_of_baby = (TextView)findViewById(R.id.sex_of_baby);
		birth_weight = (TextView)findViewById(R.id.birth_weight);
		vitK = (TextView)findViewById(R.id.vitk);
		hearing = (TextView)findViewById(R.id.hearing);
		feeding = (TextView)findViewById(R.id.feeding);
		nbst = (TextView)findViewById(R.id.nbst);
		
		String perineumStr = ServiceUserSingleton.getInstance().getPerineum();
		perineum.setText(perineumStr);
		
		
		String birthModeStr = ServiceUserSingleton.getInstance().getBirthMode();
		birth_mode.setText(birthModeStr);
		
		String genderStr = ServiceUserSingleton.getInstance().getGender();
		sex_of_baby.setText(genderStr);
		
		String weightStr = ServiceUserSingleton.getInstance().getWeight();
		birth_weight.setText(weightStr);
		
		String vitaminStr = ServiceUserSingleton.getInstance().getVitaminK();
		vitK.setText(vitaminStr);
		
		String hearingStr = ServiceUserSingleton.getInstance().getHearing();
		hearing.setText(hearingStr);
		
		String anti_dStr = ServiceUserSingleton.getInstance().getAntiD();
		anti_d.setText(anti_dStr);
		
		String feedingStr = ServiceUserSingleton.getInstance().getFeeding();
		feeding.setText(feedingStr);
		
		String nameStr = ServiceUserSingleton.getInstance().getName();
		name.setText(nameStr);
		
		String nbstStr = ServiceUserSingleton.getInstance().getNBST();
		nbst.setText(nbstStr);
		
		String deliveryDateStr = ServiceUserSingleton.getInstance().getDeliveryDate();
		deliveryDate.setText(ab.getDeliveryDate(deliveryDateStr));
		
		String deliveryTimeStr = ServiceUserSingleton.getInstance().getDeliveryDate();
		deliveryTime.setText(ab.getDeliveryTime(deliveryTimeStr));
	
		String noOfDaysStr = ServiceUserSingleton.getInstance().getDeliveryDate();
		
		try {
			dateOfDelivery = formatter.parse(noOfDaysStr);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		int days = ab.getNoOfDays(currentDate, dateOfDelivery);
		String noOfDays  = String.valueOf(days);
		daysSinceBirth.setText(noOfDays);
	}
	
	private class postNatalOptions implements View.OnClickListener {
		public void onClick(View v) {
			switch (v.getId()) {
			case R.id.user_image_post_natal:
				Intent intent = new Intent(PostNatalActivity.this, ServiceUserActivity.class);
				startActivity(intent);
				break;
			case R.id.ante_natal_from_post_natal:
				Intent intent2 = new Intent(PostNatalActivity.this, AnteNatalActivity.class);
				startActivity(intent2);
			}
		}
	}
}