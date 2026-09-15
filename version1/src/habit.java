
//imports

import java.util.*;
import java.time.*;


// class to classify habits

public class habit {

    //name of habit
    String name;
    //description of the habit
    String purpose;


    // map (day of the week) to a (time of expected time to complete the habit)
    // the list allow use to change time or have more than one
    Map<DayOfWeek, List<LocalTime>> timesOfWeek  = new HashMap<>();

    //List of days
    List<DayStatus> history =new ArrayList<>();;


//---------------------------------------------------------------------------------------------------



    public habit() {



    }

    // adds to the week array the time
    // to call name.addTime(DayOfWeek.MONDAY, LocalTime.of(8, 30));
    //inputs like
    //MONDAY    → [08:30, 17:15]
    //WEDNESDAY → [10:00]
    public void addTime(DayOfWeek day, LocalTime time) {

        if (!timesOfWeek.containsKey(day)) {
            timesOfWeek.put(day, new ArrayList<>());
        }

        timesOfWeek.get(day).add(time);
    }

    //get a list of days
    public boolean[] getDays(){

        boolean[] days = new boolean[7];

        days[0] = timesOfWeek.containsKey(DayOfWeek.SUNDAY);
        days[1] = timesOfWeek.containsKey(DayOfWeek.MONDAY);
        days[2] = timesOfWeek.containsKey(DayOfWeek.TUESDAY);
        days[3] = timesOfWeek.containsKey(DayOfWeek.WEDNESDAY);
        days[4] = timesOfWeek.containsKey(DayOfWeek.THURSDAY);
        days[5] = timesOfWeek.containsKey(DayOfWeek.FRIDAY);
        days[6] = timesOfWeek.containsKey(DayOfWeek.SATURDAY);

        return days;
    }


    //get the name
    public String getName() {
        return name;
    }
    //get the purpose
    public String getPurpose() {
        return purpose;
    }



}
