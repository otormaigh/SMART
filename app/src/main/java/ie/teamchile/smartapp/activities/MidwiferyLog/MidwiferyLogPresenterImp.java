package ie.teamchile.smartapp.activities.MidwiferyLog;

import android.app.Activity;
import android.app.ProgressDialog;

import java.lang.ref.WeakReference;
import java.util.ArrayList;

import ie.teamchile.smartapp.R;
import ie.teamchile.smartapp.activities.Base.BaseModel;
import ie.teamchile.smartapp.activities.Base.BaseModelImp;
import ie.teamchile.smartapp.activities.Base.BasePresenterImp;
import ie.teamchile.smartapp.api.SmartApiClient;
import ie.teamchile.smartapp.model.BaseResponseModel;
import ie.teamchile.smartapp.model.PostingData;
import ie.teamchile.smartapp.util.CustomDialogs;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;
import timber.log.Timber;

/**
 * Created by elliot on 31/12/2015.
 */
public class MidwiferyLogPresenterImp extends BasePresenterImp implements MidwiferyLogPresenter {
    private MidwiferyLogView midwiferyLogView;
    private WeakReference<Activity> weakActivity;
    private BaseModel baseModel;

    public MidwiferyLogPresenterImp(MidwiferyLogView midwiferyLogView, WeakReference<Activity> weakActivity) {
        this.midwiferyLogView = midwiferyLogView;
        this.weakActivity = weakActivity;
        baseModel = new BaseModelImp(this);

        midwiferyLogView.updatePregnancyList(new ArrayList<>(baseModel.getPregnancyNotes()));
    }

    @Override
    public void postNote(String note) {
        PostingData postNote = new PostingData();
        postNote.postNote(note);

        final ProgressDialog pd = new CustomDialogs().showProgressDialog(
                weakActivity.get(),
                weakActivity.get().getString(R.string.adding_note));

        SmartApiClient.getAuthorizedApiClient(weakActivity.get()).postPregnancyNote(
                postNote,
                baseModel.getPregnancy().getId(),
                new Callback<BaseResponseModel>() {
                    @Override
                    public void success(BaseResponseModel baseResponseModel, Response response) {
                        Timber.d("postNote success");

                        baseModel.updatePregnancyNote(baseResponseModel.getPregnancyNote());
                        midwiferyLogView.updatePregnancyList(new ArrayList<>(baseModel.getPregnancyNotes()));

                        pd.dismiss();
                    }

                    @Override
                    public void failure(RetrofitError error) {
                        Timber.d("postNote failure = " + error);
                        pd.dismiss();
                    }
                }
        );
    }
}
