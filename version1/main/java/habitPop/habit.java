
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

    //what tab it listed on
    String tabName;


//---------------------------------------------------------------------------------------------------


    // call with just name and description
    public habit(String nameV,String purposeV) {
        name = nameV;
        purpose = purposeV;
    }
 habit(String nameV,String purposeV,List<DayOfWeek> daylist,List<LocalTime> timeList) {
        name = nameV;
        purpose = purposeV;

        for (DayOfWeek day : daylist) {
            timesOfWeek.put(day,timeList);
        }
    }


    //way to import old history
    public void addPreviousHistory(List<DayStatus> oldHistory){
        history = oldHistory;
    }

    //add new day
    public void addDay(DayStatus newDay){
        history.add(newDay);
    }

    //get history - probably need only one day
    public List<DayStatus> getHistory() {
        return history;
    }
    public DayStatus getDay(LocalDate date){

        for (DayStatus dateFind : history){
            if (dateFind.getDate() == date) {
                return dateFind;
            }
        }
        return null;
    }

    //delete an old day
    public void deleteHistoryOffDate(LocalDate dateToDelete ){
        history.removeIf(dayFind -> dayFind.getDate() == dateToDelete);
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

    // remove time that user wants to do
    public void removeTime(DayOfWeek day, LocalTime time) {

        if (timesOfWeek.containsKey(day)) {
            timesOfWeek.get(day).remove(time);
        }
    }

    //get a list of days
    // 0 is Sunday,1 is Monday, ... 6 is Saturday
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
    public String getName()  { return name; }
    //get the purpose
    public String getPurpose() { return purpose; }

    //Update the description of habit
    public void updatePurpose(String newP) { purpose=newP; }

    // set new name, not sure if this is needed. Like make new habit?
    public void newName(String newNJustMakeANewHabit) { name = newNJustMakeANewHabit; }

    //get which tab
    public String getTabName() { return tabName; }

    //change tab
    public void changeTabName(String newTab) { tabName = newTab; }
}
