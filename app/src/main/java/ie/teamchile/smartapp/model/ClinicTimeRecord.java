package ie.teamchile.smartapp.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by user on 6/3/15.
 */
public class ClinicTimeRecord {

    @SerializedName("clinic_id")
    @Expose
    private Integer clinicId;
    @SerializedName("end_time")
    @Expose
    private Object endTime;
    @Expose
    private Integer id;
    @SerializedName("start_time")
    @Expose
    private String startTime;

    /**
     * @return The clinicId
     */
    public Integer getClinicId() {
        return clinicId;
    }

    /**
     * @param clinicId The clinic_id
     */
    public void setClinicId(Integer clinicId) {
        this.clinicId = clinicId;
    }

    /**
     * @return The endTime
     */
    public Object getEndTime() {
        return endTime;
    }

    /**
     * @param endTime The end_time
     */
    public void setEndTime(Object endTime) {
        this.endTime = endTime;
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
     * @return The startTime
     */
    public String getStartTime() {
        return startTime;
    }

    /**
     * @param startTime The start_time
     */
    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }
}
