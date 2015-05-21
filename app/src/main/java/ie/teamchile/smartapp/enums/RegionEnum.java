package ie.teamchile.smartapp.enums;

public enum RegionEnum {
    DOMINO_DUBLIN("Domino Dublin"),
    DOMINO_Wicklow("Domino Wicklow"),
    ETH_DUBLIN("ETH Dublin"),
    ETH_WICKLOW("ETH Wicklow"),
    SATELLITE("Satellite");

    private String region;

    private RegionEnum(String region) {
        this.region = region;
    }
    public String toString() {
        return region;
    }
}