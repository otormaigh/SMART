package utility;

import org.json.JSONException;
import org.json.JSONObject;

public class ServiceUserSingleton {
	private static ServiceUserSingleton singleInstance;
	private JSONObject query;

	private ServiceUserSingleton() {
	}
	public static ServiceUserSingleton getInstance() {
		if(singleInstance == null) {
			singleInstance = new ServiceUserSingleton();
		}
		return singleInstance;
	}
	public void setPatientInfo(JSONObject query){
		this.query = query;
	}
	public String getHospitalNumber() {
		String hospitalNumber = null;
		try {
			JSONObject jObj = (JSONObject) query.getJSONArray("service_users").get(0);
			hospitalNumber = jObj.get("hospital_number").toString();	
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return hospitalNumber;
	}
	public String getEmail() {
		String email = null;
		try {
			JSONObject jObj = (JSONObject) query.getJSONArray("service_users").get(0);
			email = ((JSONObject) jObj.get("personal_fields")).get("email").toString();	
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return email;
	}
	public String getMobileNumber() {
		String mobile = null;
		try {
			JSONObject jObj = (JSONObject) query.getJSONArray("service_users").get(0);
			mobile = ((JSONObject) jObj.get("personal_fields")).get("mobile_phone").toString();	
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return mobile;
		
	}
	
	public String getParity() {
		String parity = null;
		try {
			JSONObject jObj = (JSONObject) query.getJSONArray("service_users").get(0);
			parity = ((JSONObject) jObj.get("clinical_fields")).get("parity").toString();	
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return parity;
	}
}

