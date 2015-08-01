package ie.teamchile.smartapp.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by user on 5/26/15.
 */
public class ServiceOption {
    private List<Integer> clinicIds = new ArrayList<>();
    private Boolean homeVisit;
    private Integer id;
    private String name;

    public List<Integer> getClinicIds() {
        return clinicIds;
    }

    public void setClinicIds(List<Integer> clinicIds) {
        this.clinicIds = clinicIds;
    }

    public Boolean getHomeVisit() {
        return homeVisit;
    }

    public void setHomeVisit(Boolean homeVisit) {
        this.homeVisit = homeVisit;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
