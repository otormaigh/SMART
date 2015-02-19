package ie.teamchile.smartapp;

import models.Login_model;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;
import connecttodb.Logout;

public class MenuInheritActivity extends Activity {
    private Logout logout = new Logout();
    private Login_model login = new Login_model();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()) {
            case R.id.menu_item1 :		//logout
                Log.d("MYLOG", "Logout button pressed");
                if(Login_model.getToken() == ""){
        			Intent intent = new Intent(getApplicationContext(), MainActivity.class);
        			intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | 
                            		Intent.FLAG_ACTIVITY_CLEAR_TASK |
                            		Intent.FLAG_ACTIVITY_NEW_TASK);
        			Toast.makeText(this, "You are already logged out!!", Toast.LENGTH_LONG).show();
        			startActivity(intent);        			
        		}else {
        			logout.doLogout(Login_model.getToken());
        			//login.setToken(null);        			
        			Intent intent = new Intent(getApplicationContext(), MainActivity.class);
        			intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | 
		                    		Intent.FLAG_ACTIVITY_CLEAR_TASK |
		                    		Intent.FLAG_ACTIVITY_NEW_TASK);
        			Toast.makeText(this, "Logout Successful", Toast.LENGTH_LONG).show();
        			startActivity(intent);        			
        		}
                break;
            case R.id.menu_item2 :
                System.out.println("Item 2 was selected!");
        }
        return super.onOptionsItemSelected(item);
    }
}