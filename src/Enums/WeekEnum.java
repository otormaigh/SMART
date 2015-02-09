package Enums;

public enum WeekEnum {
    WEEK_1("Week 1"),
    WEEK_2("Week 2"),
    WEEK_3("Week 3"),
    WEEK_4("Week 4"),
    WEEK_5("Week 5"),
    WEEK_6("Week 6");

    private String week;

    private WeekEnum(String week) {
        this.week = week;
    }

    public String toString() {
        return week;
    }
}