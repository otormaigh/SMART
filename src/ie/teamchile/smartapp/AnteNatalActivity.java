package ie.teamchile.smartapp;


import utility.ServiceUserSingleton;
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
	private TableRow obstetricHistory;


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_ante_natal);
		
		userImage = (ImageView)findViewById(R.id.user_image_anti_natal);
		userImage.setOnClickListener(new AntiNatalOptions());
		
		postNatal = (ImageView)findViewById(R.id.post_natal_from_ante_natal);
		postNatal.setOnClickListener(new AntiNatalOptions());

		
		ageAnte = (TextView)findViewById(R.id.age_ante_natal);
		nameAntiNatal = (TextView)findViewById(R.id.name_anti_natal);
		gestationAntiNatal = (TextView)findViewById(R.id.gestation);
		parityAntiNatal = (TextView)findViewById(R.id.parity);
		deliveryTime = (TextView)findViewById(R.id.deliveryTime);
		bloodGroup = (TextView)findViewById(R.id.blood_group);
		rhesus = (TextView)findViewById(R.id.rhesus);
		
		obstetricHistory = (TableRow)findViewById(R.id.obstretic_history);
	
		obstetricHistory.setOnClickListener(new AntiNatalOptions());
	
		String name = ServiceUserSingleton.getInstance().getName();
		parityAntiNatal.setText(ServiceUserSingleton.getInstance().getParity());
		gestationAntiNatal.setText(ServiceUserSingleton.getInstance().getGestation());
		rhesus.setText(ServiceUserSingleton.getInstance().getRhesus());
		bloodGroup.setText(ServiceUserSingleton.getInstance().getBloodGroup());
		ServiceUserActivity aa = new ServiceUserActivity();
		String edd = ServiceUserSingleton.getInstance().getEstimatedDeliveryDate();
		deliveryTime.setText(aa.getEstimateDeliveryDate(edd));

		String age = ServiceUserSingleton.getInstance().getAge();
		int anteNatalAge = aa.getAge(age);
		String theAge = String.valueOf(anteNatalAge);

		ageAnte.setText(theAge);
		nameAntiNatal.setText(name);

		
		//gestationAntiNatal.setText(getIntent().getStringExtra("gestation"));

		parityAntiNatal.setText(ServiceUserSingleton.getInstance().getParity());
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
			}
		}
	}
}
