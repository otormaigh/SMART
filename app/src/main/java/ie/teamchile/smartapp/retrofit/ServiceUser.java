package ie.teamchile.smartapp.retrofit;

/**
 * Created by user on 5/26/15.
 */

import com.google.gson.annotations.Expose;

public class ServiceUser {
    @Expose
    private Object gestation;
    @Expose
    private Integer id;
    @Expose
    private String name;

    /**
     * @return The gestation
     */
    public Object getGestation() {
        return gestation;
    }

    /**
     * @param gestation The gestation
     */
    public void setGestation(Object gestation) {
        this.gestation = gestation;
    }

    public ServiceUser withGestation(Object gestation) {
        this.gestation = gestation;
        return this;
    }

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

    public ServiceUser withId(Integer id) {
        this.id = id;
        return this;
    }

    /**
     * @return The name
     */
    public String getName() {
        return name;
    }

    /**
     * @param name The name
     */
    public void setName(String name) {
        this.name = name;
    }

    public ServiceUser withName(String name) {
        this.name = name;
        return this;
    }

}
