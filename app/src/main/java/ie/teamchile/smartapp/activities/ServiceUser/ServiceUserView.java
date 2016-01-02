package ie.teamchile.smartapp.activities.ServiceUser;

import ie.teamchile.smartapp.activities.Base.BaseViewSec;
import ie.teamchile.smartapp.model.ResponseBaby;
import ie.teamchile.smartapp.model.ResponsePregnancy;
import ie.teamchile.smartapp.model.ResponseServiceUser;

/**
 * Created by elliot on 28/12/2015.
 */
public interface ServiceUserView extends BaseViewSec {
    void updateTextViews(ServiceUserPresenter serviceUserPresenter, ResponseServiceUser responseServiceUser, ResponseBaby responseBaby, ResponsePregnancy responsePregnancy);
}
