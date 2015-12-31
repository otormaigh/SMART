package ie.teamchile.smartapp.activities.ServiceUser;

import android.app.AlertDialog;

import java.util.Date;

import ie.teamchile.smartapp.activities.Base.BasePresenter;

/**
 * Created by elliot on 28/12/2015.
 */
public interface ServiceUserPresenter extends BasePresenter {
    void putAntiD(String putAntiD, AlertDialog ad);

    void putFeeding(String putFeeding, AlertDialog ad);

    void putVitK(String putVitK, AlertDialog ad);

    void putHearing(String putHearing, AlertDialog ad);

    void putNBST(String putNbst, AlertDialog ad);

    void postPregnancyActions(String action);

    void onLeaveView();

    String getEstimateDeliveryDate(Date date);

    String getDeliveryDate(Date date);

    String getDeliveryTime(Date date);

    String getNoOfDays(Date dateOfDelivery);
}
