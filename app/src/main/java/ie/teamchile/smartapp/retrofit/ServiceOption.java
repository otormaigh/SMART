package ie.teamchile.smartapp.retrofit;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by user on 5/26/15.
 */
public class ServiceOption {
    @SerializedName("clinic_ids")
    @Expose
    private List<Integer> clinicIds = new ArrayList<Integer>();
    @Expose
    private Integer id;
    @Expose
    private String name;

    /**
     * @return The clinicIds
     */
    public List<Integer> getClinicIds() {
        return clinicIds;
    }

    /**
     * @param clinicIds The clinic_ids
     */
    public void setClinicIds(List<Integer> clinicIds) {
        this.clinicIds = clinicIds;
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
}
