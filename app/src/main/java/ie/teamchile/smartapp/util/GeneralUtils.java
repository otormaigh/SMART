package ie.teamchile.smartapp.util;

import java.util.ArrayList;
import java.util.List;

import ie.teamchile.smartapp.model.ResponseDays;

/**
 * Created by user on 11/1/15.
 */
public class GeneralUtils {

    public List<String> getTrueDays(ResponseDays responseDays) {
        List<String> trueDays = new ArrayList<>();
        if (responseDays.isMonday())
            trueDays.add("Monday");
        if (responseDays.isTuesday())
            trueDays.add("Tuesday");
        if (responseDays.isWednesday())
            trueDays.add("Wednesday");
        if (responseDays.isThursday())
            trueDays.add("Thursday");
        if (responseDays.isFriday())
            trueDays.add("Friday");
        if (responseDays.isSaturday())
            trueDays.add("Saturday");
        if (responseDays.isSunday())
            trueDays.add("Sunday");
        return trueDays;
    }
}
