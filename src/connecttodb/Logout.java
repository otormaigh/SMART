package connecttodb;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

import android.util.Log;

public class Logout {
	private String logoutURL = "http://54.72.7.91:8888/logout";
	private String encodeType = "ISO-8859-1";
	private String token;
	private String apiKey = "1e7db54d-df97-407a-868b-9dc50dce7883";
	private int ch;
	private HttpURLConnection httpcon;
	private InputStream is;
	private StringBuffer sb;
	
	public void doLogout(String token) {
		this.token = token;
		Log.d("MYLOG", "Token is: " + token);
		doLogout();
	}
	
	private void doLogout(){try {
			httpcon = (HttpURLConnection) ((new URL(logoutURL).openConnection()));
			URLEncoder.encode(logoutURL, encodeType);
			httpcon.setDoOutput(true);
			httpcon.setRequestMethod("POST");
			httpcon.setRequestProperty("Api-Key", apiKey);
			httpcon.setRequestProperty("Auth-Token", token);
			httpcon.connect();

			is = httpcon.getInputStream();
			sb = new StringBuffer();
			while ((ch = is.read()) != -1) {
				sb.append((char) ch);
			}
			Log.d("MYLOG", "sb.toString(): " + sb.toString());
			Log.d("MYLOG", "Response Code: " + httpcon.getResponseCode());
			
			httpcon.disconnect();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}	