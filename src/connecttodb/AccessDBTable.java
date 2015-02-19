package connecttodb;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import android.util.Log;

public class AccessDBTable {
    private String token, tableURL, inputLine;
    private int responseCode;
    private StringBuffer response;
    private URL obj;
    private HttpURLConnection con;
    private BufferedReader in;

	public String accessDB(String token, String tableURL){
		this.token = token;
        this.tableURL = tableURL;
		return accessDB();
	}
	private String accessDB() {
		//Log.d("MYLOG", "In AccessDB");
		//Log.d("MYLOG", "AccessDB Token: " + token);
		//Log.d("MYLOG", "AccessDB TableURL: " + tableURL);
		try {
			con = (HttpURLConnection) obj.openConnection();
			con.setRequestMethod("GET");

			// add request header
			con.setRequestProperty("Api-Key", apiKey);
			con.setRequestProperty("Auth-Token", token);

			responseCode = con.getResponseCode();
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