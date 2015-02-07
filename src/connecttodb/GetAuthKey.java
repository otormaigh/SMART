package connecttodb;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.HashMap;
import models.Login_model;
import org.json.JSONException;
import org.json.JSONObject;
import android.util.Log;

public class GetAuthKey {
	private HttpURLConnection httpcon;
	private String loginURL = "http://54.72.7.91:8888/login";
	public Login_model login = new Login_model();
	JSONObject credentials = new JSONObject();
	JSONObject jsonLogin = new JSONObject();
	private String encodeType = "ISO-8859-1";
	
	public String getAuthKey(String username, String password){
		return getToken(username, password);
	}
	
	private String getToken(String username, String password){
		try {
			credentials.put("username", username);
			credentials.put("password", password);
			jsonLogin.put("login", credentials);
			
			httpcon = (HttpURLConnection) ((new URL(loginURL).openConnection()));
			URLEncoder.encode(loginURL, encodeType);
			httpcon.setDoOutput(true);
			httpcon.setRequestMethod("POST");
			httpcon.setRequestProperty("Content-Type", "application/json");
			httpcon.setRequestProperty("Accept", "application/json");
			httpcon.connect();
			
			// form request
			byte[] inputBytes = jsonLogin.toString().getBytes(encodeType);

			OutputStream os = httpcon.getOutputStream();
			os.write(inputBytes);
			os.flush();
			os.close();
			
			// grab the response
			InputStream is = httpcon.getInputStream();
			int ch;
			StringBuffer sb = new StringBuffer();
			while ((ch = is.read()) != -1) {
				sb.append((char) ch);
			}
			String st = sb.toString();

			// create JSON Object to get Token using token key
			JSONObject json = new JSONObject(st);
			String strToken = (String) ((JSONObject) json.get("login")).get("token");
			
			Log.d("MYLOG", "sb.toString(): " + sb.toString());
			Log.d("MYLOG", "Response Code: " + httpcon.getResponseCode());
			
			httpcon.disconnect();
			return strToken;
		} catch (IOException | JSONException e) {
			e.printStackTrace();
		}
		return null;
	}	
	public String getResponseCode(){
		String responseCode = null;
		try {
			responseCode = String.valueOf(httpcon.getResponseCode());
			Log.d("MYLOG", "Response Code: " + httpcon.getResponseCode());
		} catch (IOException e) {
			e.printStackTrace();
		}
		return responseCode;
	}
}