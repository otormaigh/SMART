package connecttodb;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import android.util.Log;

public class AccessDBTable {
    private String apiKey = "1e7db54d-df97-407a-868b-9dc50dce7883";
    private String token;
    private String tableURL;
    URL obj;
    HttpURLConnection con;

	public String accessDB(String token, String tableURL){
		this.token = token;
        this.tableURL = tableURL;
		return accessDB();
	}
	private String accessDB() {
		Log.d("MYLOG", "In AccessDB");
		Log.d("MYLOG", "AccessDB Token: " + token);
		Log.d("MYLOG", "AccessDB TableURL: " + tableURL);
		try {
			obj = new URL("http://54.72.7.91:8888/" + tableURL);
			con = (HttpURLConnection) obj.openConnection();
			con.setRequestMethod("GET");

			// add request header
			con.setRequestProperty("Api-Key", apiKey);
			con.setRequestProperty("Auth-Token", token);

			int responseCode = con.getResponseCode();
			Log.d("MYLOG", "\nSending 'GET' request to URL : " + "http://54.72.7.91:8888/" + tableURL);
			Log.d("MYLOG", "Response Code from AccessDB: " + responseCode);

			BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
			String inputLine;
			StringBuffer response = new StringBuffer();

			while ((inputLine = in.readLine()) != null) {
				response.append(inputLine);
			}
			in.close();
			Log.d("MYLOG", "response.toString(): " + response.toString());
			return response.toString();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}
}