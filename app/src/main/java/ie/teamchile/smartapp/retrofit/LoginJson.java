package ie.teamchile.smartapp.retrofit;

import com.google.gson.annotations.Expose;

/**
 * Created by user on 5/26/15.
 */
public class LoginJson {
    @Expose
    private Integer id;
    @Expose
    private String token;

    /**
     * @return The id
     */
    public Integer getId() {
        return id;
    }

    /**
     * @param id The id
     */
    public void setId(Integer id) {
        this.id = id;
    }

    /**
     * @return The token
     */
    public String getToken() {
        return token;
    }

    /**
     * @param token The token
     */
    public void setToken(String token) {
        this.token = token;
    }
}