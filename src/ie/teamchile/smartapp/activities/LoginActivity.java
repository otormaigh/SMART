package ie.teamchile.smartapp.activities;

import ie.teamchile.smartapp.R;
import ie.teamchile.smartapp.connecttodb.GetToken;
import ie.teamchile.smartapp.utility.ServiceProviderSingleton;
import ie.teamchile.smartapp.utility.ToastAlert;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class LoginActivity extends Activity {
	private String username, password;
	private Button loginButton;
	private TextView usernameTextView, passwordTextView, about;
	private GetToken getToken = new GetToken();
	private Intent intent;
	private ProgressDialog pd;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);
				
		loginButton = (Button) findViewById(R.id.login);
		loginButton.setOnClickListener(new ButtonClick());
		usernameTextView = (TextView) findViewById(R.id.username);
		passwordTextView = (TextView) findViewById(R.id.password);		
		about = (TextView) findViewById(R.id.about);
	    about.setOnClickListener(new ButtonClick());
	}
	
    private class ButtonClick implements View.OnClickListener {
		public void onClick(View v) {
			switch (v.getId()) {
			case R.id.login:
				getCredentials();
				new LongOperation().execute();
				break;
			case R.id.about:
				Intent i = new Intent(Intent.ACTION_VIEW, 
					       Uri.parse("http://www.nmh.ie/about-us.8.html"));
					startActivity(i);
				break;
			}
		}
	}
    
	private class LongOperation extends AsyncTask<Void, Void, Void> {
		@Override
		protected void onPreExecute() {
			pd = new ProgressDialog(LoginActivity.this);
			pd.setMessage("Logging In");
			pd.show();
		}
		protected Void doInBackground(Void... params) {
			getToken.getToken(username, password);            
			return null;
		}
		@Override
		protected void onProgressUpdate(Void... values) { }
		@Override
        protected void onPostExecute(Void thing) {
            checkCredentials();
            pd.dismiss();
        }
	}
	
	private void getCredentials() {
		username = usernameTextView.getText().toString();
		password = passwordTextView.getText().toString();
	}
	
    private void checkCredentials(){    
    	if(getToken.getResponseCode() != null && !getToken.getResponseCode().isEmpty()) {
    		if (getToken.getResponseCode().equals("201")){  		
        		ServiceProviderSingleton.getInstance().setLoggedIn(true);
        		ServiceProviderSingleton.getInstance().setUsername(username);
        		ServiceProviderSingleton.getInstance().setPassword(password); 
        		intent = new Intent(LoginActivity.this, QuickMenuActivity.class); 
        		startActivity(intent);
        	} else
        		Toast.makeText(this, "Login Failed", Toast.LENGTH_LONG).show();
    	}else {
    		new ToastAlert(LoginActivity.this, "Poor Internet Activity \nPlease check your settings", true);
    	}
	}
    
    @Override
    public void onBackPressed() {
    	if(ServiceProviderSingleton.getInstance().isLoggedIn()) {
    		new ToastAlert(getBaseContext(), "Already logged in, \n  logout?", true);
    	}else { 
			finish();   		
    	}    	
    }
}
