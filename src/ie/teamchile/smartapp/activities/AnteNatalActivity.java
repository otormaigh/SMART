package ie.teamchile.smartapp.activities;

import ie.teamchile.smartapp.R;
import ie.teamchile.smartapp.R.id;
import ie.teamchile.smartapp.R.layout;
import ie.teamchile.smartapp.utility.ServiceUserSingleton;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TableRow;
import android.widget.TextView;

public class AnteNatalActivity extends MenuInheritActivity {
	private TextView ageAnte;
	private TextView nameAntiNatal, gestationAntiNatal, parityAntiNatal
	                ,deliveryTime, bloodGroup, rhesus;
	private ImageView userImage, postNatal;
	private TableRow obstetricHistory, parity;


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_ante_natal);
		
		userImage = (ImageView)findViewById(R.id.user_image_anti_natal);
		userImage.setOnClickListener(new AntiNatalOptions());
		
		postNatal = (ImageView)findViewById(R.id.post_natal_from_ante_natal);
		postNatal.setOnClickListener(new AntiNatalOptions());
        parity = (TableRow)findViewById(R.id.button_parity);
        parity.setOnClickListener(new AntiNatalOptions());
		
		ageAnte = (TextView)findViewById(R.id.age_ante_natal);
		nameAntiNatal = (TextView)findViewById(R.id.name_anti_natal);
		gestationAntiNatal = (TextView)findViewById(R.id.gestation);
		parityAntiNatal = (TextView)findViewById(R.id.parity_ante_natal);
		deliveryTime = (TextView)findViewById(R.id.deliveryTime);
		bloodGroup = (TextView)findViewById(R.id.blood_group);
		rhesus = (TextView)findViewById(R.id.rhesus);
		
		obstetricHistory = (TableRow)findViewById(R.id.obstretic_history);
	
		obstetricHistory.setOnClickListener(new AntiNatalOptions());
	
		String name = ServiceUserSingleton.getInstance().getUserName().get(0);
		String parityStr = ServiceUserSingleton.getInstance().getUserParity().get(0);
		
		parityAntiNatal.setText(parityStr);
		gestationAntiNatal.setText(ServiceUserSingleton.getInstance().getPregnancyGestation().get(0));
		rhesus.setText(ServiceUserSingleton.getInstance().getUserRhesus().get(0));
		bloodGroup.setText(ServiceUserSingleton.getInstance().getUserBloodGroup().get(0));
		ServiceUserActivity aa = new ServiceUserActivity();
		String edd = ServiceUserSingleton.getInstance().getPregnancyEstimatedDeliveryDate().get(0);
		deliveryTime.setText(aa.getEstimateDeliveryDate(edd));

		String dob = ServiceUserSingleton.getInstance().getUserDOB().get(0);
		int anteNatalAge = aa.getAge(dob);
		String theAge = String.valueOf(anteNatalAge);

		ageAnte.setText(theAge);
		nameAntiNatal.setText(name);
		
	}
	
	public void onBackPressed(){
		Intent goToServiceUser = new Intent(AnteNatalActivity.this, ServiceUserActivity.class);
		startActivity(goToServiceUser);
		
	}

	private class AntiNatalOptions implements View.OnClickListener {
		public void onClick(View v) {
			switch (v.getId()) {
			case R.id.user_image_anti_natal:
				Intent intent = new Intent(AnteNatalActivity.this, ServiceUserActivity.class);
				startActivity(intent);
				break;
			case R.id.post_natal_from_ante_natal:
				Intent intent1 = new Intent(AnteNatalActivity.this, PostNatalActivity.class);
				startActivity(intent1);
				break;
			case R.id.obstretic_history:
				Intent intent2 = new Intent(AnteNatalActivity.this, ObstreticHistoryActivity.class);
				startActivity(intent2);
				break;
			case R.id.button_parity:
				Intent intent3 = new Intent(AnteNatalActivity.this, ParityDetailsActivity.class);
				startActivity(intent3);
			}
		}
	}
}
