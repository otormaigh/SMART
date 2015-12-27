package ie.teamchile.smartapp.activities.Login;

import android.app.ProgressDialog;

/**
 * Created by elliot on 27/12/2015.
 */
public interface LoginPresenter {
    void postLogin(ProgressDialog pd);

    void onBackPressed();
}
