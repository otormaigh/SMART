package ie.teamchile.smartapp.activities.QuickMenu;

import ie.teamchile.smartapp.activities.Base.BasePresenter;

/**
 * Created by elliot on 27/12/2015.
 */
public interface QuickMenuPresenter extends BasePresenter {
    void updateData();

    boolean isLoggedIn();

    boolean isDataEmpty();
}
