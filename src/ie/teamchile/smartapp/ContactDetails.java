package ie.teamchile.smartapp;

import ie.teamchile.smartapp.R.drawable;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;

public class ContactDetails extends Activity {

	public Object arrayPos;
	public JSONArray query;
	public Object enteredSearch;
	public String SeconaryNumber;
	public String PrimaryNumber;
	public String Email;
	public String Name;
	public JSONObject json;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_contact_details);
	}
	public class LongOperation extends AsyncTask<String, Void, String> {
		@Override
		protected void onPreExecute() {
		}
		protected String doInBackground(String... params) {
			Log.d("MYLOG", "ServiceProviderSearch DoInBackground");
			String dbQuery = drawable.accessDB("id", "service_providers");
			try {
				json = new JSONObject(dbQuery);
				query = json.getJSONArray("service_providers");
				arrayPos = getObjects(query, "id", enteredSearch);
				
				Name = (((JSONObject) ((JSONObject) query.get((String) arrayPos)).get("Midwife_Name")).get("Name")).toString();
				Email = ((JSONArray) ((JSONObject) query.get((String) arrayPos)).get("Midwife_Email")).get("Email")).toString();
                PrimaryNumber = (((JSONObject) ((JSONObject) query.get((String) arrayPos)).get("Primary_Number")).get("PrimaryNumber")).toString();
                SeconaryNumber = (((JSONObject) ((JSONObject) query.get((String) arrayPos)).get("Secondary_Number")).get("SecondaryNumber")).toString();
	;

			} catch (JSONException e) {
				e.printStackTrace();
			}
			return null;
}
}
	public Object getObjects(JSONArray query2, String string,
			Object enteredSearch2) {
		return null;
	}       
}