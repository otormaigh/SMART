package ie.teamchile.smartapp.activities.ClinicTimeRecord;

import java.util.List;
import java.util.Map;

import ie.teamchile.smartapp.activities.Base.BasePresenter;
import ie.teamchile.smartapp.model.ResponseClinic;
import ie.teamchile.smartapp.model.ResponseClinicTimeRecord;

/**
 * Created by elliot on 01/01/2016.
 */
public interface ClinicTimeRecordPresenter extends BasePresenter {
    void getTimeRecords(final int clinicId, String date);

    void putStartTime(String now, String date, final int clinicId);

    void putEndTime(String then, String date, final int clinicId);

    void deleteTimeRecord(final int clinicIdForDelete, final int recordIdForDelete);

    void getDataFromDb();

    Map<Integer, ResponseClinic> getClinicIdMap();

    List<ResponseClinicTimeRecord> getClinicTimeRecords();

    List<Integer> getClinicStopped();

    List<Integer> getClinicStarted();

    List<Integer> getClinicNotStarted();

    void updateRecordGetDone(int update);
}

