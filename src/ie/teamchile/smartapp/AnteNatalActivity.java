package ie.teamchile.smartapp;

import utility.ServiceUserSingleton;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

public class AnteNatalActivity extends MenuInheritActivity {
	private TextView age;
	private TextView nameAntiNatal, gestationAntiNatal, parityAntiNatal
	                ,deliveryTime, bloodGroup, rhesus, obstetricHistory;
	private ImageView userImage;
	private String parity;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_ante_natal);
		
		userImage = (ImageView)findViewById(R.id.user_image_anti_natal);
		userImage.setOnClickListener(new AntiNatalOptions());
		
		age = (TextView)findViewById(R.id.age_ante_natal);
		nameAntiNatal = (TextView)findViewById(R.id.name_anti_natal);
		gestationAntiNatal = (TextView)findViewById(R.id.gestation);
		parityAntiNatal = (TextView)findViewById(R.id.parity);
		deliveryTime = (TextView)findViewById(R.id.deliveryTime);
		bloodGroup = (TextView)findViewById(R.id.blood_group);
		rhesus = (TextView)findViewById(R.id.rhesus);
		obstetricHistory = (TextView)findViewById(R.id.obstetric_history_ante_natal);

		parityAntiNatal.setText(ServiceUserSingleton.getInstance().getUserParity().get(0));
	}

	private class AntiNatalOptions implements View.OnClickListener {
		public void onClick(View v) {
			switch (v.getId()) {
			case R.id.user_image_anti_natal:
				Intent intent = new Intent(AnteNatalActivity.this, ServiceUserActivity.class);
				startActivity(intent);
				break;
			}
		}
	 }
}