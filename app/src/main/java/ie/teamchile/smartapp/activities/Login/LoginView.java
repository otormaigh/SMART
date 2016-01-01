package ie.teamchile.smartapp.activities.Login;

import ie.teamchile.smartapp.activities.Base.BaseViewPri;

/**
 * Created by elliot on 27/12/2015.
 */
public interface LoginView extends BaseViewPri {
    String getUsername();

    String getPassword();

    void validateInput();

    void gotoQuickMenu();

    void showErrorToast(String error);
}
