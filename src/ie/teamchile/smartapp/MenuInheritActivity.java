
package ie.teamchile.smartapp;

import utility.UserSingleton;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
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
                        if (UserSingleton.getUserSingleton().getToken() == "") {
                            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP |
                                    Intent.FLAG_ACTIVITY_CLEAR_TASK |
                                    Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity(intent);
                        } else {
                            logout.doLogout(UserSingleton.getUserSingleton().getToken());
                            //login.setToken(null);
                            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP |
                                    		Intent.FLAG_ACTIVITY_CLEAR_TASK |
                                    		Intent.FLAG_ACTIVITY_NEW_TASK);
                            Toast.makeText(getApplicationContext(), "You are now logged out", Toast.LENGTH_SHORT).show();
                            startActivity(intent);
                        }
                    }
                }).show();
            case R.id.menu_item2:
                System.out.println("Item 2 was selected!");
        }
        return super.onOptionsItemSelected(item);
    }
}
