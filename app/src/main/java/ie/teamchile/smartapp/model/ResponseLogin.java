package ie.teamchile.smartapp.model;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by user on 5/26/15.
 */
public class ResponseLogin extends RealmObject {
    @PrimaryKey
    private int id;
    private String token;
    private boolean loggedIn = false;

    public ResponseLogin() {}

    public ResponseLogin(ResponseLogin login) {
        setId(login.getId());
        setToken(login.getToken());
        setLoggedIn(login.isLoggedIn());
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public boolean isLoggedIn() {
        return loggedIn;
    }

    public void setLoggedIn(boolean loggedIn) {
        this.loggedIn = loggedIn;
    }
}