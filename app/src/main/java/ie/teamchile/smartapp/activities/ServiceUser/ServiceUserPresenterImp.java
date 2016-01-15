package ie.teamchile.smartapp.activities.ServiceUser;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.util.Log;

import java.lang.ref.WeakReference;
import java.util.Calendar;
import java.util.Date;

import ie.teamchile.smartapp.activities.Base.BaseModel;
import ie.teamchile.smartapp.activities.Base.BaseModelImp;
import ie.teamchile.smartapp.activities.Base.BasePresenterImp;
import ie.teamchile.smartapp.api.SmartApiClient;
import ie.teamchile.smartapp.model.ResponseBase;
import ie.teamchile.smartapp.model.PostingData;
import ie.teamchile.smartapp.util.Constants;
import ie.teamchile.smartapp.util.CustomDialogs;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;
import timber.log.Timber;

/**
 * Created by elliot on 28/12/2015.
 */
public class ServiceUserPresenterImp extends BasePresenterImp implements ServiceUserPresenter {
    private ServiceUserView view;
    private WeakReference<Activity> weakActivity;
    private BaseModel model;

    public ServiceUserPresenterImp(ServiceUserView view, WeakReference<Activity> weakActivity) {
        super(weakActivity);
        this.view = view;
        this.weakActivity = weakActivity;
        model = new BaseModelImp(this);

        updateViews();
    }

    private void updateViews() {
        view.updateTextViews(
                this,
                model.getServiceUser(),
                model.getBaby(),
                model.getPregnancy()
        );
    }

    @Override
    public void putAntiD(String putAntiD, final AlertDialog ad) {
        PostingData puttingAntiD = new PostingData();

        puttingAntiD.putAntiD(putAntiD, model.getServiceUser().getId());

        final ProgressDialog pd = new CustomDialogs().showProgressDialog(
                weakActivity.get(),
                "Updating Anti-D");

        SmartApiClient.getAuthorizedApiClient(getEncryptedRealm()).putAnitD(
                puttingAntiD,
                model.getPregnancy().getId(),
                new Callback<ResponseBase>() {
                    @Override
                    public void success(ResponseBase responseBase, Response response) {
                        Timber.d("put anti-d retro success");

                        model.updatePregnancy(responseBase.getResponsePregnancy());
                        model.updateAntiD();

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
                model.getServiceUser().getId());

        final ProgressDialog pd = new CustomDialogs().showProgressDialog(
                weakActivity.get(),
                "Updating Feeding");

        SmartApiClient.getAuthorizedApiClient(getEncryptedRealm()).putAnitD(
                puttingFeeding,
                model.getPregnancy().getId(),
                new Callback<ResponseBase>() {
                    @Override
                    public void success(ResponseBase responseBase, Response response) {
                        Timber.d("put feeding retro success");

                        model.updatePregnancy(responseBase.getResponsePregnancy());
                        model.updateFeeding();

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
        Timber.d("preg id = " + model.getPregnancy().getId());

        PostingData puttingVitK = new PostingData();
        puttingVitK.putVitK(
                putVitK,
                model.getServiceUser().getId(),
                model.getPregnancy().getId());

        final ProgressDialog pd = new CustomDialogs().showProgressDialog(
                weakActivity.get(),
                "Updating Vit-K");

        SmartApiClient.getAuthorizedApiClient(getEncryptedRealm()).putVitK(
                puttingVitK,
                model.getBaby().getId(),
                new Callback<ResponseBase>() {
                    @Override
                    public void success(ResponseBase responseBase, Response response) {
                        Timber.d("put vit k retro success");

                        model.updateBaby(responseBase.getResponseBaby());
                        model.updateVitK();

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
                model.getServiceUser().getId(),
                model.getPregnancy().getId());

        final ProgressDialog pd = new CustomDialogs().showProgressDialog(
                weakActivity.get(),
                "Updating Hearing");

        SmartApiClient.getAuthorizedApiClient(getEncryptedRealm()).putHearing(
                puttingHearing,
                model.getBaby().getId(),
                new Callback<ResponseBase>() {
                    @Override
                    public void success(ResponseBase responseBase, Response response) {
                        Timber.d("put hearing retro success");

                        model.updateBaby(responseBase.getResponseBaby());
                        model.updateHearing();

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
                model.getServiceUser().getId(),
                model.getPregnancy().getId());

        final ProgressDialog pd = new CustomDialogs().showProgressDialog(
                weakActivity.get(),
                "Updating NBST");

        SmartApiClient.getAuthorizedApiClient(getEncryptedRealm()).putNBST(
                puttingNbst,
                model.getBaby().getId(),
                new Callback<ResponseBase>() {
                    @Override
                    public void success(ResponseBase responseBase, Response response) {
                        Timber.d("put nbst retro success");

                        model.updateBaby(responseBase.getResponseBaby());
                        model.updateNbst();

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
    public void postPregnancyActions(String action) {
        PostingData postAction = new PostingData();
        postAction.postPregnancyAction(action);

        SmartApiClient.getAuthorizedApiClient(getEncryptedRealm()).postPregnancyAction(
                postAction,
                model.getPregnancy().getId(),
                new Callback<ResponseBase>() {
                    @Override
                    public void success(ResponseBase responseBase, Response response) {
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
        model.deleteServiceUserFromRealm();
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
