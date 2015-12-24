package ie.teamchile.smartapp.model;

import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by user on 5/26/15.
 */
public class ServiceOption extends RealmObject {
    private RealmList<RealmInteger> clinicIds;
    private boolean homeVisit;
    @PrimaryKey
    private int id;
    private String name;

    public RealmList<RealmInteger> getClinicIds() {
        return clinicIds;
    }

    public void setClinicIds(RealmList<RealmInteger> clinicIds) {
        this.clinicIds = clinicIds;
    }

    public boolean isHomeVisit() {
        return homeVisit;
    }

    public void setHomeVisit(boolean homeVisit) {
        this.homeVisit = homeVisit;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
