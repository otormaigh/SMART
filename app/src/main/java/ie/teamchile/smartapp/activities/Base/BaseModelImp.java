package ie.teamchile.smartapp.activities.Base;

import java.util.List;

import ie.teamchile.smartapp.model.Appointment;
import io.realm.Realm;

/**
 * Created by elliot on 28/12/2015.
 */
public class BaseModelImp implements BaseModel {
    private Realm realm;

    public BaseModelImp(Realm realm) {
        this.realm = realm;
    }

    @Override
    public void saveAppointmentsToRealm(List<Appointment> appointments) {
        realm.beginTransaction();
        realm.copyToRealmOrUpdate(appointments);
        realm.commitTransaction();
    }
}
