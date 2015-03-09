package connecttodb;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

import org.json.JSONException;
import org.json.JSONObject;

import utility.ServiceProviderSingleton;
import Enums.CredentialsEnum;
import android.util.Log;

public class GetToken {
	private String username, password, strToken, st, id;
	private String loginUrl, encodeType;
	private String responseCode = null;
	private int ch;
	private HttpURLConnection httpcon;
	private JSONObject credentials = new JSONObject();
	private JSONObject jsonLogin = new JSONObject();
	private JSONObject json = new JSONObject();
	private InputStream is;
	private OutputStream os;
	private StringBuffer sb;
	private static CredentialsEnum url = CredentialsEnum.URL;
	private static CredentialsEnum encode = CredentialsEnum.ENCODETYPE;
	
	public String getToken(String username, String password){
		loginUrl = url.toString() + "login";
		encodeType = encode.toString();
		
		this.username = username;
		this.password = password;
		return getToken();
	}	
	private String getToken(){
		try {
			credentials.put("username", username);
			credentials.put("password", password);
			jsonLogin.put("login", credentials);
			Log.d("MYLOG", "jsonLogin: " + jsonLogin);
			
			httpcon = (HttpURLConnection) ((new URL(loginUrl).openConnection()));
			URLEncoder.encode(loginUrl, encodeType);
			httpcon.setDoOutput(true);
			httpcon.setRequestMethod("POST");
			httpcon.setRequestProperty("Content-Type", "application/json");
			httpcon.setRequestProperty("Accept", "application/json");
			httpcon.connect();
			
			// form request
			byte[] inputBytes = jsonLogin.toString().getBytes(encodeType);

			os = httpcon.getOutputStream();
			os.write(inputBytes);
			os.flush();
			os.close();
			
			// grab the response
			is = httpcon.getInputStream();
			sb = new StringBuffer();
			while ((ch = is.read()) != -1) {
				sb.append((char) ch);
			}
			st = sb.toString();

			// create JSON Object to get Token using token key
			json = new JSONObject(st);
			strToken = (String) ((JSONObject) json.get("login")).get("token");
			id = String.valueOf((Integer) ((JSONObject) json.get("login")).get("id"));
			ServiceProviderSingleton.getInstance().setID(id);
			ServiceProviderSingleton.getInstance().setToken(strToken);
			
			Log.d("MYLOG", "id: " + ServiceProviderSingleton.getInstance().getID());
			Log.d("MYLOG", "token: " + ServiceProviderSingleton.getInstance().getToken());
			Log.d("MYLOG", "sb.toString(): " + sb.toString());
			Log.d("MYLOG", "Response Code: " + httpcon.getResponseCode());
			
			httpcon.disconnect();
		} catch (IOException | JSONException e) {
			e.printStackTrace();
		}
		return null;
	}	
	public String getResponseCode(){		
		try {
			responseCode = String.valueOf(httpcon.getResponseCode());
			Log.d("MYLOG", "Response Code: " + httpcon.getResponseCode());
		} catch (IOException e) {
			e.printStackTrace();
		}
		return responseCode;
	}
}