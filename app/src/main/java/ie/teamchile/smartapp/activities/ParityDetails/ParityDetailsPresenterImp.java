package ie.teamchile.smartapp.activities.ParityDetails;

import android.app.Activity;

import java.lang.ref.WeakReference;

import ie.teamchile.smartapp.activities.Base.BasePresenterImp;

/**
 * Created by elliot on 01/01/2016.
 */
public class ParityDetailsPresenterImp extends BasePresenterImp implements ParityDetailsPresenter {
    private ParityDetailsView parityDetailsView;
    private WeakReference<Activity> weakActivity;

    public ParityDetailsPresenterImp(ParityDetailsView parityDetailsView, WeakReference<Activity> weakActivity) {
        this.parityDetailsView = parityDetailsView;
        this.weakActivity = weakActivity;
    }
}