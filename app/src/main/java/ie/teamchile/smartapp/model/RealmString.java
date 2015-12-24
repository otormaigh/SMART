package ie.teamchile.smartapp.model;

import io.realm.RealmObject;

/**
 * Created by user on 11/2/15.
 */
public class RealmString extends RealmObject {
    private String value;

    public RealmString() {
    }

    public RealmString(String value) {
        this.value = value;
    }

    public String getValue() {
        return this.value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
