package ie.teamchile.smartapp.activities.ServiceUserSearch;

import ie.teamchile.smartapp.activities.Base.BasePresenter;

/**
 * Created by elliot on 27/12/2015.
 */
public interface ServiceUserSearchPresenter extends BasePresenter {
    void getHistories(int pregnancyId, int babyId);
    void searchForPatient(String name, String hospitalNumber, String dob);
    void onLeaveView();
}
