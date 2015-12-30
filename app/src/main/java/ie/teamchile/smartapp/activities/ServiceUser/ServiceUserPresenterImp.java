package ie.teamchile.smartapp.activities.ServiceUser;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.util.Log;

import java.lang.ref.WeakReference;
import java.util.Calendar;
import java.util.Date;

import ie.teamchile.smartapp.activities.Base.BasePresenterImp;
import ie.teamchile.smartapp.api.SmartApiClient;
import ie.teamchile.smartapp.model.AntiDHistory;
import ie.teamchile.smartapp.model.BaseResponseModel;
import ie.teamchile.smartapp.model.FeedingHistory;
import ie.teamchile.smartapp.model.HearingHistory;
import ie.teamchile.smartapp.model.NbstHistory;
import ie.teamchile.smartapp.model.PostingData;
import ie.teamchile.smartapp.model.VitKHistory;
import ie.teamchile.smartapp.util.Constants;
import ie.teamchile.smartapp.util.CustomDialogs;
import io.realm.Realm;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;
import timber.log.Timber;

/**
 * Created by elliot on 28/12/2015.
 */
public class ServiceUserPresenterImp extends BasePresenterImp implements ServiceUserPresenter {
    private ServiceUserView serviceUserView;
    private WeakReference<Activity> weakActivity;
    private Realm realm;
    private ServiceUserModel serviceUserModel;

    public ServiceUserPresenterImp(ServiceUserView serviceUserView, WeakReference<Activity> weakActivity) {
        super(weakActivity);
        this.serviceUserView = serviceUserView;
        this.weakActivity = weakActivity;
        realm = getEncryptedRealm();
        serviceUserModel = new ServiceUserModelImp(this);

        updateViews();
    }

    private void updateViews() {
        serviceUserView.updateTextViews(
                this,
                serviceUserModel.getServiceUser(),
                serviceUserModel.getBaby(),
                serviceUserModel.getPregnancy()
        );
    }

    @Override
    public void putAntiD(String putAntiD, final AlertDialog ad) {
        PostingData puttingAntiD = new PostingData();

        puttingAntiD.putAntiD(putAntiD, serviceUserModel.getServiceUser().getId());

        final ProgressDialog pd = new CustomDialogs().showProgressDialog(
                weakActivity.get(),
                "Updating Anti-D");

        SmartApiClient.getAuthorizedApiClient(weakActivity.get()).putAnitD(
                puttingAntiD,
                serviceUserModel.getPregnancy().getId(),
                new Callback<BaseResponseModel>() {
                    @Override
                    public void success(BaseResponseModel baseResponseModel, Response response) {
                        Timber.d("put anti-d retro success");

                        AntiDHistory antiDHistory = new AntiDHistory();
                        antiDHistory.setAntiD(baseResponseModel.getPregnancy().getAntiD());
                        antiDHistory.setCreatedAt(Calendar.getInstance().getTime());
                        antiDHistory.setServiceProviderName(serviceUserModel.getServiceProvider().getName());

                        realm.beginTransaction();
                        realm.copyToRealmOrUpdate(baseResponseModel.getPregnancy());
                        //antiDHistoryList.add(antiDHistory);
                        serviceUserModel.updatePregnancy(baseResponseModel.getPregnancy());
                        realm.commitTransaction();

                        updateViews();

                        pd.dismiss();
                        ad.dismiss();
                    }

                    @Override
                    public void failure(RetrofitError error) {
                        Timber.d("put anti-d retro failure = " + error);
                        pd.dismiss();
                    }
                }
        );
    }

    @Override
    public void putFeeding(String putFeeding, final AlertDialog ad) {
        PostingData puttingFeeding = new PostingData();

        puttingFeeding.putFeeding(
                putFeeding,
                serviceUserModel.getServiceUser().getId());

        final ProgressDialog pd = new CustomDialogs().showProgressDialog(
                weakActivity.get(),
                "Updating Feeding");

        SmartApiClient.getAuthorizedApiClient(weakActivity.get()).putAnitD(
                puttingFeeding,
                serviceUserModel.getPregnancy().getId(),
                new Callback<BaseResponseModel>() {
                    @Override
                    public void success(BaseResponseModel baseResponseModel, Response response) {
                        Timber.d("put feeding retro success");

                        FeedingHistory feedingHistory = new FeedingHistory();
                        feedingHistory.setFeeding(baseResponseModel.getPregnancy().getFeeding());
                        feedingHistory.setCreatedAt(Calendar.getInstance().getTime());
                        feedingHistory.setServiceProviderName(serviceUserModel.getServiceProvider().getName());

                        realm.beginTransaction();
                        realm.copyToRealmOrUpdate(baseResponseModel.getPregnancy());
                        //feedingHistoryList.add(feedingHistory);
                        serviceUserModel.updatePregnancy(baseResponseModel.getPregnancy());
                        realm.commitTransaction();

                        updateViews();

                        pd.dismiss();
                        ad.dismiss();
                    }

                    @Override
                    public void failure(RetrofitError error) {
                        Timber.d("put feeding retro failure = " + error);
                        pd.dismiss();
                    }
                }
        );
    }

    @Override
    public void putVitK(String putVitK, final AlertDialog ad) {
        Timber.d("preg id = " + serviceUserModel.getPregnancy().getId());

        PostingData puttingVitK = new PostingData();
        puttingVitK.putVitK(
                putVitK,
                serviceUserModel.getServiceUser().getId(),
                serviceUserModel.getPregnancy().getId());

        final ProgressDialog pd = new CustomDialogs().showProgressDialog(
                weakActivity.get(),
                "Updating Vit-K");

        SmartApiClient.getAuthorizedApiClient(weakActivity.get()).putVitK(
                puttingVitK,
                serviceUserModel.getBaby().getId(),
                new Callback<BaseResponseModel>() {
                    @Override
                    public void success(BaseResponseModel baseResponseModel, Response response) {
                        Timber.d("put vit k retro success");

                        VitKHistory vitKHistory = new VitKHistory();
                        vitKHistory.setVitK(baseResponseModel.getBaby().getVitK());
                        vitKHistory.setCreatedAt(Calendar.getInstance().getTime());
                        vitKHistory.setServiceProviderName(serviceUserModel.getServiceProvider().getName());

                        realm.beginTransaction();
                        realm.copyToRealmOrUpdate(baseResponseModel.getBaby());
                        //vitKHistoryList.add(vitKHistory);
                        serviceUserModel.updateBaby(baseResponseModel.getBaby());
                        realm.commitTransaction();

                        updateViews();

                        pd.dismiss();
                        ad.dismiss();
                    }

                    @Override
                    public void failure(RetrofitError error) {
                        Timber.d("put vit k retro failure = " + error);
                        pd.dismiss();
                    }
                }
        );
    }

    @Override
    public void putHearing(String putHearing, final AlertDialog ad) {
        PostingData puttingHearing = new PostingData();

        puttingHearing.putHearing(
                putHearing,
                serviceUserModel.getServiceUser().getId(),
                serviceUserModel.getPregnancy().getId());

        final ProgressDialog pd = new CustomDialogs().showProgressDialog(
                weakActivity.get(),
                "Updating Hearing");

        SmartApiClient.getAuthorizedApiClient(weakActivity.get()).putHearing(
                puttingHearing,
                serviceUserModel.getBaby().getId(),
                new Callback<BaseResponseModel>() {
                    @Override
                    public void success(BaseResponseModel baseResponseModel, Response response) {
                        Timber.d("put hearing retro success");

                        HearingHistory hearingHistory = new HearingHistory();
                        hearingHistory.setHearing(baseResponseModel.getBaby().getHearing());
                        hearingHistory.setCreatedAt(Calendar.getInstance().getTime());
                        hearingHistory.setServiceProviderName(serviceUserModel.getServiceProvider().getName());

                        realm.beginTransaction();
                        realm.copyToRealmOrUpdate(baseResponseModel.getBaby());
                        //hearingHistoryList.add(hearingHistory);
                        serviceUserModel.updateBaby(baseResponseModel.getBaby());
                        realm.commitTransaction();

                        updateViews();

                        pd.dismiss();
                        ad.dismiss();
                    }

                    @Override
                    public void failure(RetrofitError error) {
                        Timber.d("put hearing retro failure = " + error);
                        pd.dismiss();
                    }
                }
        );
    }

    @Override
    public void putNBST(String putNbst, final AlertDialog ad) {
        PostingData puttingNbst = new PostingData();

        puttingNbst.putNBST(
                putNbst,
                serviceUserModel.getServiceUser().getId(),
                serviceUserModel.getPregnancy().getId());

        final ProgressDialog pd = new CustomDialogs().showProgressDialog(
                weakActivity.get(),
                "Updating NBST");

        SmartApiClient.getAuthorizedApiClient(weakActivity.get()).putNBST(
                puttingNbst,
                serviceUserModel.getBaby().getId(),
                new Callback<BaseResponseModel>() {
                    @Override
                    public void success(BaseResponseModel baseResponseModel, Response response) {
                        Timber.d("put nbst retro success");

                        NbstHistory nbstHistory = new NbstHistory();
                        nbstHistory.setNbst(baseResponseModel.getBaby().getNbst());
                        nbstHistory.setCreatedAt(Calendar.getInstance().getTime());
                        nbstHistory.setServiceProviderName(serviceUserModel.getServiceProvider().getName());

                        realm.beginTransaction();
                        realm.copyToRealmOrUpdate(baseResponseModel.getBaby());
                        //nbstHistoryList.add(nbstHistory);
                        serviceUserModel.updateBaby(baseResponseModel.getBaby());
                        realm.commitTransaction();

                        updateViews();

                        pd.dismiss();
                        ad.dismiss();
                    }

                    @Override
                    public void failure(RetrofitError error) {
                        Timber.d("put nbst retro failure = " + error);
                        pd.dismiss();
                    }
                }
        );
    }

    @Override
    public void getMidwiferyNotes() {
        final ProgressDialog pd = new CustomDialogs().showProgressDialog(
                weakActivity.get(),
                "Adding Actions");

        SmartApiClient.getAuthorizedApiClient(weakActivity.get()).getPregnancyNotes(
                serviceUserModel.getPregnancy().getId(),
                new Callback<BaseResponseModel>() {
                    @Override
                    public void success(BaseResponseModel baseResponseModel, Response response) {
                        Timber.d("put getMidwiferyNotes retro success");
                        realm.beginTransaction();
                        realm.copyToRealmOrUpdate(baseResponseModel.getPregnancyNotes());
                        realm.commitTransaction();

                        pd.dismiss();
                    }

                    @Override
                    public void failure(RetrofitError error) {
                        Timber.d("put getMidwiferyNotes retro failure = " + error);
                        pd.dismiss();
                    }
                }
        );
    }

    @Override
    public void postPregnancyActions(String action) {
        PostingData postAction = new PostingData();
        postAction.postPregnancyAction(action);

        SmartApiClient.getAuthorizedApiClient(weakActivity.get()).postPregnancyAction(
                postAction,
                serviceUserModel.getPregnancy().getId(),
                new Callback<BaseResponseModel>() {
                    @Override
                    public void success(BaseResponseModel baseResponseModel, Response response) {
                        Timber.d("post pregnancy action retro success");
                    }

                    @Override
                    public void failure(RetrofitError error) {
                        Timber.d("post pregnancy action retro failure = " + error);
                    }
                }
        );
    }

    @Override
    public void onLeaveView() {
        serviceUserModel.deleteServiceUserFromRealm();
    }

    @Override
    public String getEstimateDeliveryDate(Date date) {
        try {
            return Constants.DF_MONTH_FULL_NAME.format(date);
        } catch (NullPointerException e) {
            Timber.e(Log.getStackTraceString(e));
            return "";
        }
    }

    @Override
    public String getDeliveryDate(Date date) {
        try {
            return Constants.DF_MONTH_FULL_NAME.format(date);
        } catch (NullPointerException e) {
            Timber.e(Log.getStackTraceString(e));
            return "";
        }
    }

    @Override
    public String getDeliveryTime(Date date) {
        try {
            return Constants.DF_AM_PM.format(date);
        } catch (NullPointerException e) {
            Timber.e(Log.getStackTraceString(e));
            return "";
        }
    }

    @Override
    public String getNoOfDays(Date dateOfDelivery) {
        try {
            Date now = Calendar.getInstance().getTime();
            int numOfDays = (int) ((now.getTime() - dateOfDelivery.getTime()) / (1000 * 60 * 60 * 24)) + 1;

            return String.valueOf(numOfDays);
        } catch (NullPointerException e) {
            Timber.e(Log.getStackTraceString(e));
            return null;
        }
    }
}
