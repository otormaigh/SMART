package ie.teamchile.smartapp.activities.ClinicTimeRecord;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.CountDownTimer;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import ie.teamchile.smartapp.R;
import ie.teamchile.smartapp.activities.Base.BaseModel;
import ie.teamchile.smartapp.activities.Base.BaseModelImp;
import ie.teamchile.smartapp.activities.Base.BasePresenterImp;
import ie.teamchile.smartapp.api.SmartApiClient;
import ie.teamchile.smartapp.model.ResponseBase;
import ie.teamchile.smartapp.model.ResponseClinic;
import ie.teamchile.smartapp.model.ResponseClinicTimeRecord;
import ie.teamchile.smartapp.model.PostingData;
import ie.teamchile.smartapp.util.Constants;
import ie.teamchile.smartapp.util.CustomDialogs;
import ie.teamchile.smartapp.util.GeneralUtils;
import io.realm.Realm;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;
import timber.log.Timber;

/**
 * Created by elliot on 01/01/2016.
 */
public class ClinicTimeRecordPresenterImp extends BasePresenterImp implements ClinicTimeRecordPresenter {
    private ClinicTimeRecordView clinicTimeRecordView;
    private WeakReference<Activity> weakActivity;
    private Realm realm;
    private BaseModel baseModel;
    private CountDownTimer timer;
    private String todayDay;
    private String todayDate;
    private Map<Integer, ResponseClinic> clinicIdMap = new HashMap<>();
    private int recordId = 0;
    private int recordGetDone;
    private List<Integer> clinicStopped = new ArrayList<>();
    private List<Integer> clinicStarted = new ArrayList<>();
    private List<Integer> clinicNotStarted = new ArrayList<>();
    private ProgressDialog pd;

    public ClinicTimeRecordPresenterImp(ClinicTimeRecordView clinicTimeRecordView, WeakReference<Activity> weakActivity) {
        super(clinicTimeRecordView, weakActivity);
        this.clinicTimeRecordView = clinicTimeRecordView;
        this.weakActivity = weakActivity;
        realm = getEncryptedRealm();
        baseModel = new BaseModelImp(this, weakActivity);

        todayDay = Constants.DF_DAY_LONG.format(Calendar.getInstance().getTime());
        todayDate = Constants.DF_DATE_ONLY.format(Calendar.getInstance().getTime());

        if (!todayDay.equals(weakActivity.get().getString(R.string.saturday)) && !todayDay.equals(weakActivity.get().getString(R.string.sunday))) {
            getDataFromDb();
        } else {
            new CustomDialogs().showWarningDialog(weakActivity.get(), weakActivity.get().getString(R.string.no_clinics_opened));
        }
    }

    @Override
    public void getTimeRecords(final int clinicId, String date) {
        SmartApiClient.getAuthorizedApiClient(realm).getTimeRecords(
                clinicId,
                date,
                new Callback<ResponseBase>() {
                    @Override
                    public void success(ResponseBase responseBase, Response response) {
                        Timber.d("get time record success");
                        if (responseBase.getResponseClinicTimeRecords().size() > 0) {
                            baseModel.updateClinicTimeRecords(responseBase.getResponseClinicTimeRecords());

                            if (responseBase.getResponseClinicTimeRecords().get(0).getEndTime() == null) {
                                if (!clinicStarted.contains(clinicId))
                                    clinicStarted.add(clinicId);
                            } else if (responseBase.getResponseClinicTimeRecords().get(0).getEndTime() == null &&
                                    responseBase.getResponseClinicTimeRecords().get(0).getStartTime() == null) {
                                clinicNotStarted.add(clinicId);
                            } else {
                                if (!clinicStopped.contains(clinicId))
                                    clinicStopped.add(clinicId);
                            }
                        } else {
                            if (!clinicNotStarted.contains(clinicId))
                                clinicNotStarted.add(clinicId);
                        }

                        clinicTimeRecordView.setNotStartedList();
                        clinicTimeRecordView.setStartedList();
                        clinicTimeRecordView.setStoppedList();
                    }

                    @Override
                    public void failure(RetrofitError error) {
                        Timber.d("get time record failure = " + error);
                    }
                }
        );
    }

    @Override
    public void putStartTime(String now, String date, final int clinicId) {
        PostingData timeRecord = new PostingData();
        timeRecord.putStartTimeRecord(
                now,
                clinicId,
                date);

        final ProgressDialog pd = new CustomDialogs().showProgressDialog(
                weakActivity.get(),
                weakActivity.get().getString(R.string.updating_time_records));

        SmartApiClient.getAuthorizedApiClient(realm).postTimeRecords(
                timeRecord,
                clinicId,
                new Callback<ResponseBase>() {
                    @Override
                    public void success(ResponseBase responseBase, Response response) {
                        Timber.d("retro success");
                        clinicTimeRecordView.disableButtons();
                        baseModel.updateClinicTimeRecord(responseBase.getResponseClinicTimeRecord());

                        clinicNotStarted.remove(clinicNotStarted.indexOf(clinicId));
                        clinicStarted.add(clinicId);

                        clinicTimeRecordView.setNotStartedList();
                        clinicTimeRecordView.setStartedList();
                        pd.dismiss();
                    }

                    @Override
                    public void failure(RetrofitError error) {
                        Timber.d("retro error = " + error.getResponse());
                        pd.dismiss();
                        clinicTimeRecordView.disableButtons();
                    }
                });
    }

    @Override
    public void putEndTime(String then, String date, final int clinicId) {
        for (int i = 0; i < getClinicTimeRecords().size(); i++) {
            if (getClinicTimeRecords().get(i).getClinicId() == clinicId)
                recordId = getClinicTimeRecords().get(i).getId();
        }

        PostingData timeRecord = new PostingData();
        timeRecord.putEndTimeRecord(
                then,
                date,
                clinicId);

        final ProgressDialog pd = new CustomDialogs().showProgressDialog(
                weakActivity.get(),
                weakActivity.get().getString(R.string.updating_time_records));

        SmartApiClient.getAuthorizedApiClient(realm).putTimeRecords(
                timeRecord,
                clinicId,
                recordId,
                new Callback<ResponseBase>() {
                    @Override
                    public void success(ResponseBase responseBase, Response response) {
                        Timber.d("retro success");
                        clinicTimeRecordView.disableButtons();
                        baseModel.updateClinicTimeRecord(responseBase.getResponseClinicTimeRecord());

                        int size = getClinicTimeRecords().size();
                        for (int i = 0; i < size; i++) {
                            if (getClinicTimeRecords().get(i).getId() == recordId) {
                                clinicStarted.remove(clinicStarted.indexOf(clinicId));
                                clinicStopped.add(clinicId);

                                clinicTimeRecordView.setStartedList();
                                clinicTimeRecordView.setStoppedList();
                            }
                        }
                        pd.dismiss();
                    }

                    @Override
                    public void failure(RetrofitError error) {
                        Timber.d("retro error = " + error.getResponse());
                        pd.dismiss();
                        clinicTimeRecordView.disableButtons();
                    }
                });
    }

    @Override
    public void deleteTimeRecord(final int clinicIdForDelete, final int recordIdForDelete) {
        final ProgressDialog pd = new CustomDialogs().showProgressDialog(
                weakActivity.get(),
                weakActivity.get().getString(R.string.deleting_time_record));

        SmartApiClient.getAuthorizedApiClient(realm).deleteTimeRecordById(
                clinicIdForDelete,
                recordIdForDelete,
                new Callback<ResponseBase>() {
                    @Override
                    public void success(ResponseBase responseBase, Response response) {
                        Timber.d("deleteTimeRecord success");
                        clinicTimeRecordView.disableButtons();
                        baseModel.updateClinicTimeRecord(responseBase.getResponseClinicTimeRecord());

                        int size = getClinicTimeRecords().size();
                        for (int i = 0; i < size; i++) {
                            if (getClinicTimeRecords().get(i).getId() == recordIdForDelete) {
                                clinicStopped.remove(clinicStopped.indexOf(clinicIdForDelete));
                                clinicNotStarted.add(clinicIdForDelete);

                                clinicTimeRecordView.setStartedList();
                                clinicTimeRecordView.setStoppedList();
                                clinicTimeRecordView.setNotStartedList();
                            }
                        }
                        pd.dismiss();
                    }

                    @Override
                    public void failure(RetrofitError error) {
                        Timber.d("deleteTimeRecord failure = " + error);
                        pd.dismiss();
                        clinicTimeRecordView.disableButtons();
                    }
                }
        );
    }

    @Override
    public void getDataFromDb() {
        recordGetDone = 0;

        Map<String, List<Integer>> clinicDayMap = new HashMap<>();
        List<ResponseClinic> responseClinics = baseModel.getClinics();
        List<String> trueDays;

        int clinicSize = responseClinics.size();
        int trueDaySize;
        List<Integer> idList;
        for (int i = 0; i < clinicSize; i++) {
            trueDays = new GeneralUtils().getTrueDays(
                    responseClinics.get(i).getDays());
            trueDaySize = trueDays.size();
            for (int j = 0; j < trueDaySize; j++) {
                clinicIdMap.put(responseClinics.get(i).getId(), responseClinics.get(i));

                idList = new ArrayList<>();
                if (clinicDayMap.get(trueDays.get(j)) != null) {
                    idList = clinicDayMap.get(trueDays.get(j));
                    idList.add(responseClinics.get(i).getId());
                    clinicDayMap.put(trueDays.get(j), idList);
                } else {
                    idList.add(responseClinics.get(i).getId());
                    clinicDayMap.put(trueDays.get(j), idList);
                }
            }
        }
        idList = clinicDayMap.get(todayDay);

        if (clinicDayMap.containsKey(todayDay)) {
            pd = new CustomDialogs().showProgressDialog(
                    weakActivity.get(),
                    weakActivity.get().getString(R.string.updating_time_records));

            int size = idList.size();
            for (int i = 0; i < size; i++) {
                getTimeRecords(idList.get(i), todayDate);
            }
        } else {
            clinicTimeRecordView.setNotStartedList();
            clinicTimeRecordView.setStartedList();
            clinicTimeRecordView.setStoppedList();
        }

        timer = new CountDownTimer(200, 100) {
            @Override
            public void onTick(long millisUntilFinished) {
            }

            @Override
            public void onFinish() {
                if (pd != null) {
                    if (recordGetDone >= 3)
                        pd.dismiss();
                    else
                        timer.start();
                }
            }
        }.start();
    }

    @Override
    public Map<Integer, ResponseClinic> getClinicIdMap() {
        return clinicIdMap;
    }

    @Override
    public List<ResponseClinicTimeRecord> getClinicTimeRecords() {
        return baseModel.getClinicTimeRecords();
    }

    @Override
    public List<Integer> getClinicStopped() {
        return clinicStopped;
    }

    @Override
    public List<Integer> getClinicStarted() {
        return clinicStarted;
    }

    @Override
    public List<Integer> getClinicNotStarted() {
        return clinicNotStarted;
    }

    @Override
    public void updateRecordGetDone(int update) {
        recordGetDone += update;
    }
}
