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
	
	public String getName() {
		String name = null;
		try {
			JSONObject jObj = (JSONObject) query.getJSONArray("service_users").get(0);
			name = ((JSONObject) jObj.get("personal_fields")).get("name").toString();	
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return name;
	}
	
	public String getAge() {
		String age = null;
		try {
			JSONObject jObj = (JSONObject) query.getJSONArray("service_users").get(0);
			age = ((JSONObject) jObj.get("personal_fields")).get("dob").toString();	
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return age;
	}

	
	public String getAddress() {
		String address = null;
		try {
			JSONObject jObj = (JSONObject) query.getJSONArray("service_users").get(0);
			address = ((JSONObject) jObj.get("personal_fields")).get("home_address").toString();	
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return address;
	}
	
	public String getCounty() {
		String county = null;
		try {
			JSONObject jObj = (JSONObject) query.getJSONArray("service_users").get(0);
			county = ((JSONObject) jObj.get("personal_fields")).get("home_county").toString();	
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return county;
	}
	
	public String getPostCode() {
		String postCode = null;
		try {
			JSONObject jObj = (JSONObject) query.getJSONArray("service_users").get(0);
			postCode = ((JSONObject) jObj.get("personal_fields")).get("home_post_code").toString();	
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return postCode;
	}
	
	public String getKinName() {
		String kinName = null;
		try {
			JSONObject jObj = (JSONObject) query.getJSONArray("service_users").get(0);
			kinName = ((JSONObject) jObj.get("personal_fields")).get("next_of_kin_name").toString();	
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return kinName;
	}
	
	public String getKinMobileNumber() {
		String kinMobileNumber = null;
		try {
			JSONObject jObj = (JSONObject) query.getJSONArray("service_users").get(0);
			kinMobileNumber = ((JSONObject) jObj.get("personal_fields")).get("next_of_kin_phone").toString();	
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return kinMobileNumber;
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
	
	public String getObstreticHistory() {
		String obh = null;
		try {
			JSONObject jObj = (JSONObject) query.getJSONArray("service_users").get(0);
			obh = ((JSONObject) jObj.get("clinical_fields")).get("previous_obstetric_history").toString();	
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return obh;
	}

	
	public String getRhesus() {
		String rhesus = null;
		try {
			JSONObject jObj = (JSONObject) query.getJSONArray("service_users").get(0);
			rhesus = ((JSONObject) jObj.get("clinical_fields")).get("rhesus").toString();	
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return rhesus;
	}
	
	public String getBloodGroup() {
		String bloodGroup = null;
		try {
			JSONObject jObj = (JSONObject) query.getJSONArray("service_users").get(0);
			bloodGroup = ((JSONObject) jObj.get("clinical_fields")).get("blood_group").toString();	
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return bloodGroup;
	}


	
	public String getGestation() {
		String gestation = null;

		try {
			JSONObject jObj = (JSONObject) query.getJSONArray("pregnancies").get(0);
			gestation = jObj.getString("gestation");

		} catch (JSONException e) {
			e.printStackTrace();
		}
		return gestation;
	}
	
	public String getEstimatedDeliveryDate() {
		String edd = null;

		try {
			JSONObject jObj = (JSONObject) query.getJSONArray("pregnancies").get(0);
			edd = jObj.getString("estimated_delivery_date");

		} catch (JSONException e) {
			e.printStackTrace();
		}
		return edd;
	}


}
