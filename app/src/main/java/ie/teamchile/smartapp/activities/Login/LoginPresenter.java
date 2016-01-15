package ie.teamchile.smartapp.activities.Login;

import android.app.ProgressDialog;

import ie.teamchile.smartapp.activities.Base.BasePresenter;

/**
 * Created by elliot on 27/12/2015.
 */
public interface LoginPresenter extends BasePresenter {
    void postLogin(ProgressDialog pd);
}
