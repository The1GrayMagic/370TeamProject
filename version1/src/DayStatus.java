import java.util.HashMap;
import java.util.List;
import java.util.Set;
import java.util.Map;
import java.time.LocalDate;
import java.time.LocalTime;

public class DayStatus {

    LocalDate date;

    Map<LocalTime, Boolean> completions = new HashMap<LocalTime, Boolean>();


    //set up date and times
    public DayStatus(LocalDate d, List<LocalTime> lT) {
        date = d;
        for (LocalTime localTime : lT) {
            completions.put(localTime, false);
        }
    }

    //get date
    public LocalDate getDate() {
        return date;
    }

    // get a list as set of all times
    public Set<LocalTime> geTimes() {
        return completions.keySet();
    }

    //gets map of key and value
    public Map<LocalTime, Boolean> getCompletions() {
        return completions;
    }

    // input the update value, only if in the list
    public boolean updateInList(LocalTime lT, boolean updateValue) {
        if (completions.containsKey(lT)) {
            completions.put(lT, updateValue);
            return true;
        }
       return false;

    }

    // input the update value, but will also add new time if not listed
    public void updateOrAdd(LocalTime lT , boolean updateValue){
            completions.put(lT,updateValue);
    }

}