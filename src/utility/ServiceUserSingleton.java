package utility;

import android.annotation.SuppressLint;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
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
	
	public String getPerineum() {
		String perineum = null;

		try {
			JSONObject jObj = (JSONObject) query.getJSONArray("pregnancies").get(0);
			perineum = jObj.getString("perineum");

		} catch (JSONException e) {
			e.printStackTrace();
		}
		return perineum;
	}
	
	public String getBirthMode() {
		String birthMode = null;

		try {
			JSONObject jObj = (JSONObject) query.getJSONArray("pregnancies").get(0);
			birthMode = jObj.getString("birth_mode");
		} catch (JSONException e) {
			e.printStackTrace();
		}
		
		return birthMode;
	}
	
	public String getAntiD() {
		String anti = null;

		try {
			JSONObject jObj = (JSONObject) query.getJSONArray("pregnancies").get(0);
			anti = jObj.getString("anti_d");

		} catch (JSONException e) {
			e.printStackTrace();
		}
		return anti;
	}
	
	public String getFeeding() {
		String feeding = null;

		try {
			JSONObject jObj = (JSONObject) query.getJSONArray("pregnancies").get(0);
			feeding = jObj.getString("feeding");

		} catch (JSONException e) {
			e.printStackTrace();
		}
		return feeding;
	}

	public String getGender() {
		String gender = null;

		try {
			JSONObject jObj = (JSONObject) query.getJSONArray("babies").get(0);
			gender = jObj.getString("gender");

		} catch (JSONException e) {
			e.printStackTrace();
		}
		return gender;
	}
	
	public String getDeliveryDate(){
		String deliveryDate = null;
		try {
			JSONObject jObj = (JSONObject) query.getJSONArray("babies").get(0);
			deliveryDate = jObj.getString("delivery_date_time");
			
		} catch (JSONException e) {
			e.printStackTrace();
		}
		
		return deliveryDate;
	}
	

	public String getWeight() {
		String weight = null;

		try {
			JSONObject jObj = (JSONObject) query.getJSONArray("babies").get(0);
			weight = jObj.getString("weight");

		} catch (JSONException e) {
			e.printStackTrace();
		}
		return weight;
	}
	
	public String getVitaminK() {
		String vitamin = null;

		try {
			JSONObject jObj = (JSONObject) query.getJSONArray("babies").get(0);
			vitamin = jObj.getString("vitamin_k");

		} catch (JSONException e) {
			e.printStackTrace();
		}
		return vitamin;
	}
	
	public String getHearing() {
		String hearing = null;

		try {
			JSONObject jObj = (JSONObject) query.getJSONArray("babies").get(0);
			hearing = jObj.getString("hearing");

		} catch (JSONException e) {
			e.printStackTrace();
		}
		return hearing;
	}
	
	public String getNBST() {
		String nbst = null;

		try {
			JSONObject jObj = (JSONObject) query.getJSONArray("babies").get(0);
			nbst = jObj.getString("newborn_screening_test");

		} catch (JSONException e) {
			e.printStackTrace();
		}
		return nbst;
	}


}
