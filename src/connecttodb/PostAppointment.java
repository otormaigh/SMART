package connecttodb;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

import org.json.JSONException;
import org.json.JSONObject;

import utility.ClinicSingleton;
import utility.UserSingleton;
import Enums.CredentialsEnum;
import android.util.Log;

public class PostAppointment {
	private String ServiceUserID, clinicName, clinicID = null, date, time, duration, 
				   serviceProviderID, priority, visitType, strToken, st;
	private String postUrl, encodeType;
	private String responseCode = null;
	private int id, ch;
	private HttpURLConnection httpcon;
	private JSONObject appointmentJson = new JSONObject();
	private JSONObject infoJson = new JSONObject();
	private JSONObject json = new JSONObject();
	private InputStream is;
	private OutputStream os;
	private StringBuffer sb;
	private static String url = CredentialsEnum.URL.toString() + "appointments";
	private static CredentialsEnum encode = CredentialsEnum.ENCODETYPE;
	private static String apiKey = CredentialsEnum.API_KEY.toString();
	private String token;
	
	/*
	 * {"appointment":
	 * {"date":"2015-03-05",
	 *  "time":"10:00:00",
	 *  "service_provider_id":"14",
	 *  "service_user_id":"1",
	 *  "clinic_id":"4",
	 *  "priority":"scheduled",
	 *  "visit_type":"ante-natal"}}
	 */
	
	public String postAppointment(String ServiceUserID, String clinicID, String date, String time, 
								  String duration, String priority, String visitType){
		encodeType = encode.toString();
		token = UserSingleton.getUserSingleton().getToken();
		this.ServiceUserID = ServiceUserID;
		this.date = date;
		this.time = time;
		this.duration = duration;
		this.serviceProviderID = String.valueOf(UserSingleton.getUserSingleton().getID());
		this.priority = priority;
		this.visitType = visitType;
		this.clinicID = clinicID;
		
		return postAppointment();
	}	

	private String postAppointment() {
		try {
			infoJson.put("date", date);
			infoJson.put("time", time);
			infoJson.put("service_provider_id", serviceProviderID);
			infoJson.put("service_user_id", ServiceUserID);
			infoJson.put("clinic_id", clinicID);
			infoJson.put("priority", priority);
			infoJson.put("visit_type", visitType);
			appointmentJson.put("appointment", infoJson);
			Log.d("postAppointment", "appointmentJson: " + appointmentJson);
			
			Log.d("postAppointment", "appointmentJson: " + appointmentJson.toString());

			httpcon = (HttpURLConnection) ((new URL(url).openConnection()));
			URLEncoder.encode(url, encodeType);
			httpcon.setDoOutput(true);
			httpcon.setRequestMethod("POST");
			httpcon.setRequestProperty("Auth-Token", token);
			httpcon.setRequestProperty("Content-Type", "application/json");
			httpcon.setRequestProperty("Api-Key", apiKey);
			httpcon.connect();
			
			// form request
			byte[] inputBytes;
			inputBytes = appointmentJson.toString().getBytes(encodeType);

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
			Log.d("postAppointment", "returned JSON: " + json.toString());
			Log.d("postAppointment", "Response Code: " + httpcon.getResponseCode());
			
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