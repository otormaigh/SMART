package ie.teamchile.smartapp.util;

import java.util.ArrayList;
import java.util.List;

import ie.teamchile.smartapp.model.Days;

/**
 * Created by user on 11/1/15.
 */
public class GeneralUtils {

    public List<String> getTrueDays(Days days) {
        List<String> trueDays = new ArrayList<>();
        if (days.isMonday())
            trueDays.add("Monday");
        if (days.isTuesday())
            trueDays.add("Tuesday");
        if (days.isWednesday())
            trueDays.add("Wednesday");
        if (days.isThursday())
            trueDays.add("Thursday");
        if (days.isFriday())
            trueDays.add("Friday");
        if (days.isSaturday())
            trueDays.add("Saturday");
        if (days.isSunday())
            trueDays.add("Sunday");
        return trueDays;
    }
}
