package connecttodb;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

import android.os.AsyncTask;
import android.util.Log;

public class Logout {
	private String encodeType = "ISO-8859-1";
	private String token;
    private static int responseCode = 0;
	private int ch;
	private HttpURLConnection httpcon;
	private InputStream is;
	private StringBuffer sb;
	
	public int doLogout(String token) {
		this.token = token;
        new LongOperation().execute((String[]) null);
		Log.d("MYLOG", "Token is: " + token);
        Log.d("MYLOG", "Response from logout: " + getResponseCode());
        return getResponseCode();
	}
    private void setResponseCode(int responseCode){
        this.responseCode = responseCode;
    }
    public static int getResponseCode() {
        return responseCode;
    }
    private class LongOperation extends AsyncTask<String, Void, String> {
        @Override
        protected void onPreExecute() {
        }
        protected String doInBackground(String... params) {
            try {
                httpcon = (HttpURLConnection) ((new URL(logoutURL).openConnection()));
                URLEncoder.encode(logoutURL, encodeType);
                httpcon.setDoOutput(true);
                httpcon.setRequestMethod("POST");
                httpcon.setRequestProperty("Api-Key", apiKey);
                httpcon.setRequestProperty("Auth-Token", token);
                httpcon.connect();

                is = httpcon.getInputStream();
                sb = new StringBuffer();
                while ((ch = is.read()) != -1) {
                    sb.append((char) ch);
                }
                responseCode = httpcon.getResponseCode();
                Log.d("MYLOG", "sb.toString(): " + sb.toString());
                Log.d("MYLOG", "Response Code in async logout: " + httpcon.getResponseCode());
                httpcon.disconnect();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }
        @Override
        protected void onProgressUpdate(Void... values) {
            Log.d("MYLOG", "On progress update");
        }
        @Override
        protected void onPostExecute(String result) {
            setResponseCode(responseCode);
/*            if(responseCode == 401) {
                Log.d("MYLOG", "logout successful");
            } else if (responseCode == 200){
                Log.d("MYLOG", "logout unsuccessful");
            }*/
        }
	}
}	