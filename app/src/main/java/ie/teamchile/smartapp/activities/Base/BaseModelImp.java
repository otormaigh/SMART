package ie.teamchile.smartapp.activities.Base;

import java.util.List;

import ie.teamchile.smartapp.model.Appointment;
import ie.teamchile.smartapp.model.BaseResponseModel;
import io.realm.Realm;

/**
 * Created by elliot on 28/12/2015.
 */
public class BaseModelImp implements BaseModel {
    private Realm realm;

    public BaseModelImp(BasePresenter basePresenter) {
        realm = basePresenter.getEncryptedRealm();
    }

    @Override
    public void saveAppointmentsToRealm(List<Appointment> appointments) {
        realm.beginTransaction();
        realm.copyToRealmOrUpdate(appointments);
        realm.commitTransaction();
    }

    @Override
    public void saveServiceUserToRealm(BaseResponseModel baseResponseModel) {
        realm.beginTransaction();
        realm.copyToRealmOrUpdate(baseResponseModel.getServiceUsers());
        realm.copyToRealmOrUpdate(baseResponseModel.getPregnancies());
        realm.copyToRealmOrUpdate(baseResponseModel.getBabies());
        realm.copyToRealmOrUpdate(baseResponseModel.getAntiDHistories());
        realm.commitTransaction();
    }
}
