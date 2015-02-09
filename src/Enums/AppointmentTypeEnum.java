package Enums;

public enum AppointmentTypeEnum {
    DOMINO_DUBLIN("Domino Dublin"),
    NMH_OPD("NMH OPD");

    private String region;
    private String hospital;

    private AppointmentTypeEnum(String region) {
        this.region = region;
    }

    public String regionToString() {
        return region;
    }
    public String hospitalToString() {
        return hospital;
    }
}