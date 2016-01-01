package ie.teamchile.smartapp.activities.ClinicTimeRecord;

import ie.teamchile.smartapp.activities.Base.BaseView;

/**
 * Created by elliot on 01/01/2016.
 */
public interface ClinicTimeRecordView extends BaseView {
    void disableButtons();

    void setNotStartedList();

    void setStartedList();

    void setStoppedList();
}
