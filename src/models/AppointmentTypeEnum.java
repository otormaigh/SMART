package models;

public enum AppointmentTypeEnum {
    RESIDENT("Resident");

    private String jobLevel;

    private AppointmentTypeEnum(String jobLevel) {
        this.jobLevel = jobLevel;
    }

    public String toString() {
        return jobLevel;
    }
}