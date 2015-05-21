/*package ie.teamchile.smartapp.maiti;

import android.app.Application;

import com.riverbed.mobile.android.apmlib.PermissionsException;
import com.riverbed.mobile.android.apmlib.UserExperience;
import com.riverbed.mobile.android.apmlib.objects.SettingsObject;

public class MaitiApplication extends Application {
	private UserExperience apm = null;

	@Override
	public void onCreate() {
		super.onCreate();

		if (apm == null) {
			SettingsObject settings = new SettingsObject();
			settings.setDataCollector("postgresql68.cp.blacknight.com",
									  "5432", 
									  "db1058457_maitai", 
									  "u1058457_elliotbarry", 
									  "elliotbarry@2dev");
			
			settings.setDataCollector("url", false, 80);
			try {
				apm = new UserExperience("Blacknight_Test", "SMART", 
					  this.getApplicationContext(), settings);
			} catch (PermissionsException e) {
				e.printStackTrace();
				throw new RuntimeException();
			}
		}
	}

	public UserExperience getAppPerformanceMonitor() {
		return apm;
	}
}*/