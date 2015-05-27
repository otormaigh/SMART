package ie.teamchile.smartapp.retrofit;

import com.google.gson.annotations.Expose;

/**
 * Created by user on 5/27/15.
 */
public class Error {
    @Expose
    private String Error;

    /**
     * @return The Error
     */
    public String getError() {
        return Error;
    }

    /**
     * @param Error The Error
     */
    public void setError(String Error) {
        this.Error = Error;
    }

}
