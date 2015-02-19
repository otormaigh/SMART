package Enums;

public enum DaysEnum {
    MONDAY("Monday"),
    TUESDAY("Tuesday"),
    WEDNESDAY("Wednesday"),
    THURSDAY("Thursday"),
    FRIDAY("Friday"),
    SATURDAY("Saturday"),
    SUNDAY("Sunday");

    private String day;

    private DaysEnum(String day) {
        this.day = day;
    }

    public String toString() {
        return day;
    }
}