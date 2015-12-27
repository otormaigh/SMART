package ie.teamchile.smartapp.activities.Base;

import android.util.Log;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import ie.teamchile.smartapp.model.Baby;
import ie.teamchile.smartapp.model.Pregnancy;
import timber.log.Timber;

/**
 * Created by elliot on 27/12/2015.
 */
public class BasePresenterImp implements  BasePresenter {

    public BasePresenterImp() {
    }

    @Override
    public int getRecentPregnancy(List<Pregnancy> pregnancyList) {
        List<Long> asDate = new ArrayList<>();
        if (pregnancyList.size() > 1) {
            try {
                for (int i = 0; i < pregnancyList.size(); i++) {
                    if (pregnancyList.get(i).getEstimatedDeliveryDate() != null) {
                        asDate.add(pregnancyList.get(i).getEstimatedDeliveryDate().getTime());
                    } else {
                        asDate.add(0L);
                    }
                }
                return pregnancyList
                        .get(asDate.indexOf(Collections.max(asDate)))
                        .getId();
            } catch (NullPointerException e) {
                Timber.e(Log.getStackTraceString(e));
                return 0;
            }
        } else if (pregnancyList.isEmpty()) {
            return 0;
        } else {
            return pregnancyList.get(0).getId();
        }
    }

    @Override
    public int getRecentBaby(List<Baby> babyList) {
        List<Long> asDate = new ArrayList<>();
        if (babyList.size() > 1) {
            try {
                for (int i = 0; i < babyList.size(); i++) {
                    if (babyList.get(i).getDeliveryDateTime() != null) {
                        asDate.add(babyList.get(i).getDeliveryDateTime().getTime());
                    } else {
                        asDate.add(0L);
                    }
                }
                return babyList
                        .get(asDate.indexOf(Collections.max(asDate)))
                        .getId();
            } catch (NullPointerException e) {
                Timber.e(Log.getStackTraceString(e));
                return 0;
            }
        } else if (babyList.isEmpty()) {
            return 0;
        } else {
            return babyList.get(0).getId();
        }
    }
}
