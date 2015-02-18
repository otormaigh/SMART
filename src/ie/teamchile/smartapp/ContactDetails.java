package ie.teamchile.smartapp;

import models.Login_model;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import connecttodb.AccessDBTable;
import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;

public class ContactDetails extends Activity {

	private int arrayPos;
	private JSONArray query;
	private Object enteredSearch;
	private String seconaryNumber;
	private String primaryNumber;
	private Login_model login=new Login_model();
	private AccessDBTable database=new AccessDBTable();
	private String email;
	private String name;
	private JSONObject json;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_contact_details);
	}

	public class LongOperation extends AsyncTask<String, Void, String> {
		@Override
		protected void onPostExecute(String result) {
			super.onPostExecute(result);
		}
		@Override
		protected void onProgressUpdate(Void... values) {
			super.onProgressUpdate(values);
		}
		@Override
		protected void onPreExecute() {
		}
		
		
		protected String doInBackground(String... params) {
			Log.d("MYLOG", "ServiceProviderSearch DoInBackground");
			String dbQuery = database.accessDB(Login_model.getToken(), "service_providers");
			try {
				json = new JSONObject(dbQuery);
				query = json.getJSONArray("service_providers");
				arrayPos = getObjects(query, "id", "14");
				
				name = (String) ((JSONObject)query.get(arrayPos)).get("name");
				Log.d("Record Retrieved", name);

			} catch (JSONException e) {
				e.printStackTrace();
			}
			return null;
		}
	}
	
	public int getObjects(JSONArray query2, String string,
			String enteredSearch2) {
		return 0;
	}       
}
      