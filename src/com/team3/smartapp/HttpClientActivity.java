package com.team3.smartapp;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONObject;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;

import android.widget.TextView;

public class HttpClientActivity extends Activity {
	TextView resultView;
	private InputStream isr;
	private StringBuilder sb;
	private String result;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_http_client);
		 resultView = (TextView) findViewById(R.id.resultView);
		    
		 new LongOperation().execute((String[]) null);
	}

	private class LongOperation extends AsyncTask<String, Void, String> {
		protected String doInBackground(String... params) {
		try{
	        DefaultHttpClient httpclient = new DefaultHttpClient();
	        HttpPost httppost = new HttpPost("http://10.15.1.80/team3/service_users.php"); 
	        HttpResponse response = httpclient.execute(httppost);
	        HttpEntity entity = response.getEntity();
	        isr = entity.getContent();
	}
	catch(Exception e){
	        Log.e("log_tag", "Error in http connection "+e.toString());
	        resultView.setText("Couldnt connect to database");
	}
		
	//convert response to string
	try{
	        BufferedReader reader = new BufferedReader(new InputStreamReader(isr,"iso-8859-1"),8);
	        StringBuilder sb = new StringBuilder();
	        String line = null;
	        while ((line = reader.readLine()) != null) {
	                sb.append(line + "\n");
	        }
	        isr.close();
	 
	        result=sb.toString();
	}
	catch(Exception e){
	        Log.e("log_tag", "Error  converting result "+e.toString());
	}	
	 
		//parse json data
		try {
		   String s = "";
		   JSONArray jArray = new JSONArray(result);
		   
		   for(int i=0; i<jArray.length();i++){
			   JSONObject json = jArray.getJSONObject(i);
			   s = s + 
					   "Hospital Number : "+json.getString("hospital_number")+" \n"+
					   "Home Address : "+json.getString("home_address")+"\n"+
					   "Home Type : "+json.getString("home_type")+"\n\n";
		   }
		   
		   resultView.setText(s);
		
		} catch (Exception e) {
		// TODO: handle exception
		   Log.e("log_tag", "Error Parsing Data "+e.toString());
		}
		return null;
	
	}
     @Override
	 protected void onPostExecute(String result) {
     }
    
	 @Override			   
	 protected void onPreExecute() {
	 }   
				
	 @Override
	 protected void onProgressUpdate(Void... values) {
	 }
	}
}
	

