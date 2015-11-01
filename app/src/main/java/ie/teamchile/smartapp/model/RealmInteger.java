package ie.teamchile.smartapp.model;

import io.realm.RealmObject;

/**
 * Created by user on 11/1/15.
 */
public class RealmInteger extends RealmObject {
    private int value;

    public RealmInteger() {
    }

    public RealmInteger(int value) {
        this.value = value;
    }

    public int getValue() {
        return this.value;
    }

    public void setValue(int value) {
        this.value = value;
    }
}
