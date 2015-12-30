package ie.teamchile.smartapp.activities.Base;

import java.util.List;

import ie.teamchile.smartapp.model.Appointment;

/**
 * Created by elliot on 28/12/2015.
 */
public class BaseModelImp implements BaseModel {
    private BasePresenter basePresenter;

    public BaseModelImp(BasePresenter basePresenter) {
        this.basePresenter = basePresenter;
    }

    @Override
    public void saveAppointmentsToRealm(List<Appointment> appointments) {
        basePresenter.getEncryptedRealm().beginTransaction();
        basePresenter.getEncryptedRealm().copyToRealmOrUpdate(appointments);
        basePresenter.getEncryptedRealm().commitTransaction();
    }
}
