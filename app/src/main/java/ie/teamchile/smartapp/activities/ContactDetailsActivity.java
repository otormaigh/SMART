package ie.teamchile.smartapp.activities;

import ie.teamchile.smartapp.R;
import ie.teamchile.smartapp.connecttodb.AccessDBTable;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

public class ContactDetailsActivity extends BaseActivity {

	private int arrayPos;
	private JSONArray query;
	private TextView nameTextView;
	private AccessDBTable database=new AccessDBTable();
	private String name;
	private JSONObject json;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentForNav(R.layout.activity_contact_details);
		
		nameTextView = (TextView)findViewById(R.id.Midwife_Name);
		Log.d("MYLOG", "in oncreate: ");
		new LongOperation().execute();		
	}

	public class LongOperation extends AsyncTask<Void, Integer, String> {
		@Override
		protected void onPostExecute(String result) {
			Log.d("Record Retrieved", "result: " + result);
			nameTextView.setText(name);
			super.onPostExecute(result);
		}
		@Override
		protected void onProgressUpdate(Integer... values) {
			super.onProgressUpdate(values);
		}
		@Override
		protected void onPreExecute() {
		}
		@Override
		protected String doInBackground(Void... params) {
			Log.d("MYLOG", "ServiceProviderSearch DoInBackground");
			try {
				json = database.accessDB("service_providers");
				query = json.getJSONArray("service_providers");
				arrayPos = getObjects(query, "id", "14");
				
				name = (String) ((JSONObject)query.get(arrayPos)).get("name");
				Log.d("MYLOG", name);

			} catch (JSONException e) {
				e.printStackTrace();
			}
			return null;
		}
	}	
	public int getObjects(JSONArray query2, String string, String enteredSearch2) {
		return 0;
	}       
}