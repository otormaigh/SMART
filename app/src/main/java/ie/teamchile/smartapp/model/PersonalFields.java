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

    /**
     * @return The directions
     */
    public String getDirections() {
        return directions;
    }

    /**
     * @param directions The directions
     */
    public void setDirections(String directions) {
        this.directions = directions;
    }

    /**
     * @return The dob
     */
    public String getDob() {
        return dob;
    }

    /**
     * @param dob The dob
     */
    public void setDob(String dob) {
        this.dob = dob;
    }

    /**
     * @return The email
     */
    public String getEmail() {
        return email;
    }

    /**
     * @param email The email
     */
    public void setEmail(String email) {
        this.email = email;
    }

    /**
     * @return The homeAddress
     */
    public String getHomeAddress() {
        return homeAddress;
    }

    /**
     * @param homeAddress The home_address
     */
    public void setHomeAddress(String homeAddress) {
        this.homeAddress = homeAddress;
    }

    /**
     * @return The homeCounty
     */
    public String getHomeCounty() {
        return homeCounty;
    }

    /**
     * @param homeCounty The home_county
     */
    public void setHomeCounty(String homeCounty) {
        this.homeCounty = homeCounty;
    }

    /**
     * @return The homePhone
     */
    public String getHomePhone() {
        return homePhone;
    }

    /**
     * @param homePhone The home_phone
     */
    public void setHomePhone(String homePhone) {
        this.homePhone = homePhone;
    }

    /**
     * @return The homePostCode
     */
    public String getHomePostCode() {
        return homePostCode;
    }

    /**
     * @param homePostCode The home_post_code
     */
    public void setHomePostCode(String homePostCode) {
        this.homePostCode = homePostCode;
    }

    /**
     * @return The homeType
     */
    public String getHomeType() {
        return homeType;
    }

    /**
     * @param homeType The home_type
     */
    public void setHomeType(String homeType) {
        this.homeType = homeType;
    }

    /**
     * @return The mobilePhone
     */
    public String getMobilePhone() {
        return mobilePhone;
    }

    /**
     * @param mobilePhone The mobile_phone
     */
    public void setMobilePhone(String mobilePhone) {
        this.mobilePhone = mobilePhone;
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

    /**
     * @return The nextOfKinName
     */
    public String getNextOfKinName() {
        return nextOfKinName;
    }

    /**
     * @param nextOfKinName The next_of_kin_name
     */
    public void setNextOfKinName(String nextOfKinName) {
        this.nextOfKinName = nextOfKinName;
    }

    /**
     * @return The nextOfKinPhone
     */
    public String getNextOfKinPhone() {
        return nextOfKinPhone;
    }

    /**
     * @param nextOfKinPhone The next_of_kin_phone
     */
    public void setNextOfKinPhone(String nextOfKinPhone) {
        this.nextOfKinPhone = nextOfKinPhone;
    }
}
