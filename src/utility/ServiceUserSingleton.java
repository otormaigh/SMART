package utility;

import org.json.JSONException;
import org.json.JSONObject;

import connecttodb.AccessDBTable;

public class ServiceUserSingleton {
	private static ServiceUserSingleton singleInstance;

	private ServiceUserSingleton() {
	}
	public static ServiceUserSingleton getSingletonIntance() {
		if(singleInstance == null) {
			singleInstance = new ServiceUserSingleton();
		}
		return singleInstance;
	}
	/*public void updateLocal(){
		new LongOperation().execute("service_users");
	}
	public void getInfoByID(String id, Context context){
		new LongOperation(context).execute("service_users" + "/" + id);
	}
	public void getInfoByName(String name){		
		new LongOperation().execute("service_users" + "?name=" + name);
	}
	public void getInfoByDOB(String dob){		
		new LongOperation().execute("service_users" + "?dob=" + dob);
	}
	public void getInfoByHospitalNumber(String HospitalNumber){		
		new LongOperation().execute("service_users" + "?hospital_number=" + HospitalNumber);
	}
	private class LongOperation extends AsyncTask<String, Void, JSONObject> {
		private Context context;
		private ProgressDialog pd;
		public LongOperation(Context context){
			this.context = context;
		}
		@Override
		protected void onPreExecute() {
			pd = new ProgressDialog(context);
            pd.setMessage("Fetching Information");
            pd.show();
		}
		protected JSONObject doInBackground(String... params) {
			Log.d("singleton", "in service users updateLocal doInBackground");
			try {
				response = db.accessDB(params[0]);
				jsonNew = new JSONObject(response);
				//query = jsonNew.getJSONArray(params[0]);
			} catch (JSONException e) {
				e.printStackTrace();
			}
			Log.d("singleton", "query = " + jsonNew);
			return jsonNew;
		}
		@Override
		protected void onProgressUpdate(Void... values) {
		}
		@Override
        protected void onPostExecute(JSONObject result) {
			queryResult = result;
			Log.d("singleton", "result = " + result);
			pd.dismiss();
        }
	}*/
	public String getHospitalNumber(JSONObject query) {
		String hospitalNumber = null;
		try {
			JSONObject jObj = (JSONObject) query.getJSONArray("service_users").get(0);
			hospitalNumber = jObj.get("hospital_number").toString();	
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return hospitalNumber;
	}
	public String getEmail(JSONObject query) {
		String email = null;
		try {
			JSONObject jObj = (JSONObject) query.getJSONArray("service_users").get(0);
			email = ((JSONObject) jObj.get("personal_fields")).get("email").toString();	
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return email;
	}
	public String getMobileNumber(JSONObject query) {
		String mobile = null;
		try {
			JSONObject jObj = (JSONObject) query.getJSONArray("service_users").get(0);
			mobile = ((JSONObject) jObj.get("personal_fields")).get("mobile_phone").toString();	
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return mobile;
	}
}