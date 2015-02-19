package connecttodb;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

import models.Login_model;
import Enums.CredentialsEnum;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

public class Logout {	
	private static String url = CredentialsEnum.URL.toString();
	private static String apiKey = CredentialsEnum.API_KEY.toString();
	private static String encodeType = CredentialsEnum.ENCODETYPE.toString();
	
	private String token, logoutUrl;
    private int responseCode = 0;
	private int ch;
	private HttpURLConnection httpcon;
	private InputStream is;
	private StringBuffer sb;
	private Login_model login = new Login_model();
	
	public int doLogout(String token) {
		this.token = token;
		logoutUrl = url.toString() + "logout";
		
        new LongOperation().execute();
		Log.d("MYLOG", "Token is: " + token);
        Log.d("MYLOG", "Response from logout: " + getResponseCode());
        return getResponseCode();
	}
    private void setResponseCode(int responseCode){
        this.responseCode = responseCode;
    }
    public int getResponseCode() {
        return responseCode;
    }
    private class LongOperation extends AsyncTask<Void, Void, Integer> {
        @Override
        protected void onPreExecute() {
        }
        protected Integer doInBackground(Void... params) {
            try {
                httpcon = (HttpURLConnection) ((new URL(logoutUrl).openConnection()));
                URLEncoder.encode(logoutUrl, encodeType);
                httpcon.setDoOutput(true);
                httpcon.setRequestMethod("POST");
                httpcon.setRequestProperty("Api-Key", apiKey);
                httpcon.setRequestProperty("Auth-Token", token);
                httpcon.connect();
                
                Log.d("MYLOG", "response code after connect: " + httpcon.getResponseCode());
                responseCode = httpcon.getResponseCode();
                
                is = httpcon.getInputStream();
                sb = new StringBuffer();
                while ((ch = is.read()) != -1) {
                    sb.append((char) ch);
                }
                if(httpcon.getResponseCode() == Integer.valueOf(401)){
                	Log.d("MYLOG", "Should return null now");
                	return null;
                }
                
                Log.d("MYLOG", "response code after after connect: " + httpcon.getResponseCode());       
                Log.d("MYLOG", "sb.toString(): " + sb.toString());
                Log.d("MYLOG", "Response Code in async logout: " + httpcon.getResponseCode());
                httpcon.disconnect();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return responseCode;
        }
        @Override
        protected void onProgressUpdate(Void... values) {
            Log.d("MYLOG", "On progress update");
        }
        @Override
        protected void onPostExecute(Integer result) {
            setResponseCode(result);               
            Log.d("MYLOG", "result from onPost: " + result);            
            Integer unauthCode = Integer.valueOf(401);            
            Integer authCodeOk = Integer.valueOf(200);
            if(unauthCode.equals(result)){
            	Log.d("MYLOG", "logout successful");
            	login.setToken("");
            } else if (authCodeOk.equals(result)){
                Log.d("MYLOG", "logout unsuccessful");
                new LongOperation().execute();	
            }
        }
	}
}	