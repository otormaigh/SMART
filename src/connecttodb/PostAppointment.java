package connecttodb;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
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
	private String name, clinicName, clinicID = null, date, time, duration, 
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
	
	public String postAppointment(String name, String clinicName, String date, String time, 
								  String duration, String priority, String visitType){
		encodeType = encode.toString();
		
		this.name = name;
		this.date = date;
		this.time = time;
		this.duration = duration;
		this.serviceProviderID = String.valueOf(UserSingleton.getUserSingleton().getID());
		this.priority = priority;
		this.visitType = visitType;
		
		this.clinicID = ClinicSingleton.getInstance().getIDFromName(clinicName);
		Log.d("appoitnment", "clinicID: " + clinicID);
		
		return PostAppointment();
	}	
	private String PostAppointment(){
		try {
			infoJson.put("date", date);
			infoJson.put("time", time);
			infoJson.put("service_provider_id", serviceProviderID);
			infoJson.put("service_user_id", "1");
			infoJson.put("clinic_id", clinicID);
			infoJson.put("priority", priority);
			infoJson.put("visit_type", visitType);
			appointmentJson.put("appointment", infoJson);
			Log.d("appointment", "appointmentJson: " + appointmentJson);
	
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return null;
	}/*
			
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
			id = (Integer) ((JSONObject) json.get("login")).get("id");
			UserSingleton.getUserSingleton().setID(id);
			UserSingleton.getUserSingleton().setToken(strToken);
			
			Log.d("MYLOG", "id: " + UserSingleton.getUserSingleton().getID());
			Log.d("MYLOG", "token: " + UserSingleton.getUserSingleton().getToken());
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
	}*/
			
}