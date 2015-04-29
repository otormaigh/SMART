package ie.teamchile.smartapp.activities;

import ie.teamchile.smartapp.R;
import ie.teamchile.smartapp.connecttodb.Logout;
import ie.teamchile.smartapp.utility.AppointmentSingleton;
import ie.teamchile.smartapp.utility.ServiceProviderSingleton;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

public class MenuInheritActivity extends Activity {

	private Logout logout = new Logout();
	private CountDownTimer timer;
	private ProgressDialog pd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        getActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
    	
        switch (item.getItemId()) {
            case R.id.menu_item1:
                //logout
                new AlertDialog.Builder(this)
                        .setTitle(R.string.Logout_title)
                        .setMessage(R.string.Logout_dialog_message)
                        .setNegativeButton(R.string.No, new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialoginterface, int i) {
                	
                }}).setPositiveButton(R.string.Yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialoginterface, int i) {
                        Log.d("MYLOG", "Logout button pressed");
                        final Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                    	intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP |
                        				Intent.FLAG_ACTIVITY_CLEAR_TASK |
                        				Intent.FLAG_ACTIVITY_NEW_TASK);
                        if (ServiceProviderSingleton.getInstance().isLoggedIn() == false) {                            
                            startActivity(intent);
                        } else {
                            logout.doLogout(ServiceProviderSingleton.getInstance().getToken());
                            pd = new ProgressDialog(MenuInheritActivity.this);
                			pd.setMessage("Logging Out");
                			pd.show();
                            
                            timer = new CountDownTimer(1000, 1000){
								@Override
								public void onFinish() {
									if(logout.getIsLoggedOut()){
										Toast.makeText(getApplicationContext(), 
												"You are now logged out", 
												Toast.LENGTH_SHORT).show();
										startActivity(intent);
										pd.dismiss();
									} else timer.start();
								}
								@Override
								public void onTick(long millisUntilFinished) {
								}
                            }.start();
                        }
                    }
                }).show();
                break;
            case R.id.menu_item2:
                AppointmentSingleton.getInstance().updateLocal(this);
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}