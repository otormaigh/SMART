package ie.teamchile.smartapp.activities.ClinicTimeRecord;

import ie.teamchile.smartapp.activities.Base.BaseViewSec;

/**
 * Created by elliot on 01/01/2016.
 */
public interface ClinicTimeRecordView extends BaseViewSec {
    void disableButtons();

    void setNotStartedList();

    void setStartedList();

    void setStoppedList();
}
