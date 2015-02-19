package connecttodb;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import models.Login_model;
import Enums.CredentialsEnum;
import android.os.AsyncTask;
import android.util.Log;

public class AccessDBTable {
	private static CredentialsEnum url = CredentialsEnum.URL;
	private static CredentialsEnum api = CredentialsEnum.API_KEY;
	private String tableUrl, apiKey;
    private String token, table, inputLine;
    private int responseCode;
    private StringBuffer response;
    private URL obj;
    private HttpURLConnection con;
    private BufferedReader in;

	public String accessDB(String token, String table){
		tableUrl = url.toString();
		apiKey = api.toString();
		
		this.token = token;
        this.table = table;
		return accessDB();
	}
	private class LongOperation extends AsyncTask<String, Void, String> {
		@Override
		protected void onPreExecute() {
		}
		protected String doInBackground(String... params) {
			return null;
		}
		@Override
		protected void onProgressUpdate(Void... values) {
		}
		@Override
        protected void onPostExecute(String result) {
        }
	}
	private String accessDB() {
		//Log.d("MYLOG", "In AccessDB");
		//Log.d("MYLOG", "AccessDB Token: " + token);
		//Log.d("MYLOG", "AccessDB TableURL: " + tableURL);
		try {
			obj = new URL(tableUrl + table);
			con = (HttpURLConnection) obj.openConnection();
			con.setRequestMethod("GET");

			// add request header
			con.setRequestProperty("Api-Key", apiKey);
			con.setRequestProperty("Auth-Token", token);

			responseCode = con.getResponseCode();
			Log.d("MYLOG", "\nSending 'GET' request to URL : " + api + table);
			Log.d("MYLOG", "Response Code from AccessDB: " + responseCode);

			in = new BufferedReader(new InputStreamReader(con.getInputStream()));
			response = new StringBuffer();

			while ((inputLine = in.readLine()) != null) {
				response.append(inputLine);
			}
			in.close();
			//Log.d("MYLOG", "response.toString(): " + response.toString());
			return response.toString();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}
}