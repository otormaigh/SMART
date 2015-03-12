package connecttodb;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import org.json.JSONException;
import org.json.JSONObject;

import utility.ServiceProviderSingleton;
import Enums.CredentialsEnum;
import android.util.Log;

public class AccessDBTable {
	private static String url = CredentialsEnum.URL.toString();
	private static String apiKey = CredentialsEnum.API_KEY.toString();
    private String token, table, inputLine;
    private int responseCode;
    private StringBuffer response;
    private URL obj;
    private HttpURLConnection con;
    private BufferedReader in;

	public JSONObject accessDB(String table){
		token = ServiceProviderSingleton.getInstance().getToken();
        this.table = table;
		return accessDB();
	}
	private JSONObject accessDB() {
		try {
			obj = new URL(url + table);
			con = (HttpURLConnection) obj.openConnection();
			con.setRequestMethod("GET");

			// add request header
			con.setRequestProperty("Api-Key", apiKey);
			con.setRequestProperty("Auth-Token", token);

			responseCode = con.getResponseCode();
			Log.d("MYLOG", "\nSending 'GET' request to URL : " + url + table);
			Log.d("MYLOG", "Response Code from AccessDB: " + responseCode);

			in = new BufferedReader(new InputStreamReader(con.getInputStream()));
			response = new StringBuffer();

			while ((inputLine = in.readLine()) != null) {
				response.append(inputLine);
			}
			in.close();
			try {
				return new JSONObject(response.toString());
			} catch (JSONException e) {
				e.printStackTrace();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}
}