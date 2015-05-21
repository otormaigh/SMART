package ie.teamchile.smartapp.utility;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.NetworkInfo.State;
import android.net.wifi.WifiManager;
import android.widget.Toast;

public class ConnectivityTester {
	private Context context;
	private State mobile;
	private State wifi;
	private WifiManager wifiManager;
	private ConnectivityManager conMan;

	public ConnectivityTester(Context context) {
		this.context = context;
	}	
	public void testTheNetworkConnection() {
		conMan = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
		wifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE); 

	    //mobile
	    mobile = conMan.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState();

	    //wifi
	    wifi = conMan.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState();
	    
	}	
	public boolean isWifiConnected() {
		if (wifi == NetworkInfo.State.CONNECTED || wifi == NetworkInfo.State.CONNECTING) {
	        Toast.makeText(context,"Wifi is Enabled  :( .... \nTurning off WiFi",Toast.LENGTH_LONG).show();
	        wifiManager.setWifiEnabled(false);
	        return true;
	    }else
	    	return false;		
	}	
	public boolean is3GConnected() {
		if (mobile == NetworkInfo.State.CONNECTED || mobile == NetworkInfo.State.CONNECTING) {
	        Toast.makeText(context,"Mobile is Enabled :) ....",Toast.LENGTH_LONG).show();
	        return true;
	    }else
	    	return false;
	}
}