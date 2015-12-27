package ie.teamchile.smartapp.activities.ServiceUserSearch;

import java.util.List;

import ie.teamchile.smartapp.activities.Base.BaseView;
import ie.teamchile.smartapp.model.ServiceUser;

/**
 * Created by elliot on 27/12/2015.
 */
public interface ServiceUserSearchView extends BaseView {
    void gotoServiceUserActivity();
    void updateServiceUserList(List<ServiceUser> serviceUsers);
    boolean shouldChangeActivity();
}
