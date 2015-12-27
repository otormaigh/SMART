package ie.teamchile.smartapp.activities.Login;

/**
 * Created by elliot on 27/12/2015.
 */
public interface LoginView {
    void initViews();

    String getUsername();

    String getPassword();

    void validateInput();

    void gotoQuickMenu();

    void showErrorToast(String error);
}
