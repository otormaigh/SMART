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

    public Integer getClinicId() {
        return clinicId;
    }

    public void setClinicId(Integer clinicId) {
        this.clinicId = clinicId;
    }

    public Object getEndTime() {
        return endTime;
    }

    public void setEndTime(Object endTime) {
        this.endTime = endTime;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }
}
