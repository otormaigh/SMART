package ie.teamchile.smartapp.model;

import com.google.gson.annotations.Expose;

/**
 * Created by user on 5/26/15.
 */
public class Login {
    @Expose
    private Integer id;
    @Expose
    private String token;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}