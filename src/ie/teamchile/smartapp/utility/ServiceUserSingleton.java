package ie.teamchile.smartapp.utility;

import ie.teamchile.smartapp.connecttodb.AccessDBTable;

import java.util.List;

import org.json.JSONObject;

import android.os.AsyncTask;

public class ServiceUserSingleton {
	private static ServiceUserSingleton singleInstance;
	private JSONObject query;
	private JsonParseHelper help = new JsonParseHelper();

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
	
	public List<String> getBabyBirthOutcome(){
		return help.jsonParseHelper(query, "service_users", "babies", "birth_outcome");
	}
	
	public List<String> getBabyDeliveryDateTime(){
		return help.jsonParseHelper(query, "service_users", "babies", "delivery_date_time");
	}
	
	public List<String> getBabyGender(){
		return help.jsonParseHelper(query, "service_users", "babies", "gender");
	}
	
	public List<String> getBabyHearing(){
		return help.jsonParseHelper(query, "service_users", "babies", "hearing");
	}
	
	public List<String> getBabyHospitalNumber(){
		return help.jsonParseHelper(query, "service_users", "babies", "hospital_number");
	}
	
	public List<String> getBabyID(){
		return help.jsonParseHelper(query, "service_users", "babies", "id");
	}
	
	public List<String> getBabyName(){
		return help.jsonParseHelper(query, "service_users", "babies", "name");
	}
	
	public List<String> getBabyNewBornScreeningTest(){
		return help.jsonParseHelper(query, "service_users", "babies", "newborn_screening_test");
	}
	
	public List<String> getBabyPregnancyID(){
		return help.jsonParseHelper(query, "service_users", "babies", "pregnancy_id");
	}
	
	public List<String> getBabyVitK(){
		return help.jsonParseHelper(query, "service_users", "babies", "vitamin_k");
	}
	
	public List<String> getBabyWeight(){
		return help.jsonParseHelper(query, "service_users", "babies", "weight");
	}
	
	public List<String> getPregnancyAdditionalInfo(){
		return help.jsonParseHelper(query, "service_users", "pregnancies", "additional_info");
	}
	
	public List<String> getPregnancyAntiD(){
		return help.jsonParseHelper(query, "service_users", "pregnancies", "anti_d");
	}
	
	public List<String> getPregnancyBabyIDs(){
		return help.jsonParseHelper(query, "service_users", "pregnancies", "baby_ids");
	}
	
	public List<String> getPregnancyBirthMode(){
		return help.jsonParseHelper(query, "service_users", "pregnancies", "birth_mode");
	}
	
	public List<String> getPregnancyCreatedAt(){
		return help.jsonParseHelper(query, "service_users", "pregnancies", "created_at");
	}
	
	public List<String> getPregnancyEstimatedDeliveryDate(){
		return help.jsonParseHelper(query, "service_users", "pregnancies", "estimated_delivery_date");
	}
	
	public List<String> getPregnancyFeeding(){
		return help.jsonParseHelper(query, "service_users", "pregnancies", "feeding");
	}
	
	public List<String> getPregnancyGestation(){
		return help.jsonParseHelper(query, "service_users", "pregnancies", "gestation");
	}
	
	public List<String> getPregnancyID(){
		return help.jsonParseHelper(query, "service_users", "pregnancies", "id");
	}
	
	public List<String> getPregnancyLastMenstrualPeriod(){
		return help.jsonParseHelper(query, "service_users", "pregnancies", "last_menstrual_period");
	}
	
	public List<String> getPregnancyPerineum(){
		return help.jsonParseHelper(query, "service_users", "pregnancies", "perineum");
	}
	
	public List<String> getPregnancyServiceUserID(){
		return help.jsonParseHelper(query, "service_users", "pregnancies", "service_user_id");
	}
	
	public List<String> getUserBabyIDs(){
		return help.jsonParseHelper(query, "service_users", "service_users", "baby_ids");
	}
	
	public List<String> getUserBloodGroup(){
		return help.jsonParseHelper(query, "service_users", "service_users", "clinical_fields", "blood_group");
	}
	
	public List<String> getUserBMI(){
		return help.jsonParseHelper(query, "service_users", "service_users", "clinical_fields", "bmi");
	}
	
	public List<String> getUserParity(){
		return help.jsonParseHelper(query, "service_users", "service_users", "clinical_fields", "parity");
	}
	
	public List<String> getUserPreviousObstetricHistory(){
		return help.jsonParseHelper(query, "service_users", "service_users", "clinical_fields", "previous_obstetric_history");
	}
	
	public List<String> getUserRhesus(){
		return help.jsonParseHelper(query, "service_users", "service_users", "clinical_fields", "rhesus");
	}
	
	public List<String> getUserHospitalNumber(){
		return help.jsonParseHelper(query, "service_users", "service_users", "hospital_number");
	}
	
	public List<String> getUserID(){
		return help.jsonParseHelper(query, "service_users", "service_users", "id");
	}
	
	public List<String> getUserDirections(){
		return help.jsonParseHelper(query, "service_users", "service_users", "personal_fields", "directions");
	}
	
	public List<String> getUserDOB(){
		return help.jsonParseHelper(query, "service_users", "service_users", "personal_fields", "dob");
	}
	
	public List<String> getUserEmail(){
		return help.jsonParseHelper(query, "service_users", "service_users", "personal_fields", "email");
	}
	
	public List<String> getUserHomeAddress(){
		return help.jsonParseHelper(query, "service_users", "service_users", "personal_fields", "home_address");
	}
	
	public List<String> getUserHomeCounty(){
		return help.jsonParseHelper(query, "service_users", "service_users", "personal_fields", "home_county");
	}
	
	public List<String> getUserHomePhone(){
		return help.jsonParseHelper(query, "service_users", "service_users", "personal_fields", "home_phone");
	}
	
	public List<String> getUserHomePostCode(){
		return help.jsonParseHelper(query, "service_users", "service_users", "personal_fields", "home_post_code");
	}
	
	public List<String> getUserHomeType(){
		return help.jsonParseHelper(query, "service_users", "service_users", "personal_fields", "home_type");
	}
	
	public List<String> getUserMobilePhone(){
		return help.jsonParseHelper(query, "service_users", "service_users", "personal_fields", "mobile_phone");
	}
	
	public List<String> getUserName(){
		return help.jsonParseHelper(query, "service_users", "service_users", "personal_fields", "name");
	}
	
	public List<String> getUserNextOfKinName(){
		return help.jsonParseHelper(query, "service_users", "service_users", "personal_fields", "next_of_kin_name");
	}
	
	public List<String> getUserNextOfKinPhone(){
		return help.jsonParseHelper(query, "service_users", "service_users", "personal_fields", "next_of_kin_phone");
	}
	
	public List<String> getPregnancyIDs(){
		return help.jsonParseHelper(query, "service_users", "service_users", "pregnancy_ids");
	}
	
	public void getPatientInfo(String apptId) {
		String id = AppointmentSingleton.getInstance().getServiceUserID(apptId);
		String tableName = "service_users/" + id;
		new ServiceUserGetter().execute(tableName);
	}
	
	private class ServiceUserGetter extends AsyncTask<String, Void, JSONObject> {

		@Override
		protected JSONObject doInBackground(String... params) {
			AccessDBTable access = new AccessDBTable();
			return access.accessDB(params[0]);
		}

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
		}

		@Override
		protected void onPostExecute(JSONObject result) {
			super.onPostExecute(result);
			ServiceUserSingleton.getInstance().setPatientInfo(result);
		}

		@Override
		protected void onProgressUpdate(Void... values) {
			super.onProgressUpdate(values);
		}		
	}
}