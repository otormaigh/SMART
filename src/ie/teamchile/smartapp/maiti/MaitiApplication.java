package ie.teamchile.smartapp.maiti;

import com.riverbed.mobile.android.apmlib.UserExperience;
import com.riverbed.mobile.android.apmlib.PermissionsException;
import com.riverbed.mobile.android.apmlib.objects.SettingsObject;

import android.app.Application;

public class MaitiApplication extends Application {
	private UserExperience apm = null;

	@Override
	public void onCreate() {
		super.onCreate();

		if (apm == null) {
			SettingsObject settings = new SettingsObject();
			settings.setDataCollector("192.168.1.201", false, 80);
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
}