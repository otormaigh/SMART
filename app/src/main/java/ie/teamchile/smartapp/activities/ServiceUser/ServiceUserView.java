package ie.teamchile.smartapp.activities.ServiceUser;

import ie.teamchile.smartapp.activities.Base.BaseView;
import ie.teamchile.smartapp.model.Baby;
import ie.teamchile.smartapp.model.Pregnancy;
import ie.teamchile.smartapp.model.ServiceUser;

/**
 * Created by elliot on 28/12/2015.
 */
public interface ServiceUserView extends BaseView {
    void updateTextViews(ServiceUserPresenter serviceUserPresenter, ServiceUser serviceUser, Baby baby, Pregnancy pregnancy);
}
