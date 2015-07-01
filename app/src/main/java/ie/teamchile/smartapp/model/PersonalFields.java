package ie.teamchile.smartapp.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by user on 5/27/15.
 */
public class PersonalFields {
    @Expose
    private String directions;
    @Expose
    private String dob;
    @Expose
    private String email;
    @SerializedName("home_address_line1")
    @Expose
    private String homeAddress;
    @SerializedName("home_county")
    @Expose
    private String homeCounty;
    @SerializedName("home_city")
    @Expose
    private String homeCity;
    @SerializedName("home_phone")
    @Expose
    private String homePhone;
    @SerializedName("home_post_code")
    @Expose
    private String homePostCode;
    @SerializedName("home_type")
    @Expose
    private String homeType;
    @SerializedName("mobile_phone")
    @Expose
    private String mobilePhone;
    @Expose
    private String name;
    @SerializedName("next_of_kin_name")
    @Expose
    private String nextOfKinName;
    @SerializedName("next_of_kin_phone")
    @Expose
    private String nextOfKinPhone;

    public String getDirections() {
        return directions;
    }

    public void setDirections(String directions) {
        this.directions = directions;
    }

    public String getDob() {
        return dob;
    }

    public void setDob(String dob) {
        this.dob = dob;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getHomeAddress() {
        return homeAddress;
    }

    public void setHomeAddress(String homeAddress) {
        this.homeAddress = homeAddress;
    }

    public String getHomeCounty() {
        return homeCounty;
    }

    public void setHomeCounty(String homeCounty) {
        this.homeCounty = homeCounty;
    }

    public String getHomeCity() {
        return homeCity;
    }

    public void setHomeCity(String homeCity) {
        this.homeCity = homeCity;
    }

    public String getHomePhone() {
        return homePhone;
    }

    public void setHomePhone(String homePhone) {
        this.homePhone = homePhone;
    }

    public String getHomePostCode() {
        return homePostCode;
    }

    public void setHomePostCode(String homePostCode) {
        this.homePostCode = homePostCode;
    }

    public String getHomeType() {
        return homeType;
    }

    public void setHomeType(String homeType) {
        this.homeType = homeType;
    }

    public String getMobilePhone() {
        return mobilePhone;
    }

    public void setMobilePhone(String mobilePhone) {
        this.mobilePhone = mobilePhone;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNextOfKinName() {
        return nextOfKinName;
    }

    public void setNextOfKinName(String nextOfKinName) {
        this.nextOfKinName = nextOfKinName;
    }

    public String getNextOfKinPhone() {
        return nextOfKinPhone;
    }

    public void setNextOfKinPhone(String nextOfKinPhone) {
        this.nextOfKinPhone = nextOfKinPhone;
    }
}
