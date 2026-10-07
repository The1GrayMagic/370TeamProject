




import java.util.ArrayList;
import java.util.List;

public class Tab {

    String name;

    List<habit> habitList = new ArrayList<>();



    //    ----------------------------------------------------
    // create tab only via name
    public Tab (String nameNew) { name=nameNew; }

    // create tab if list is already made
    public Tab (String nameNew, List<habit> habitListNew) { habitList = habitListNew; }













    //add habit object to list
    public void addHabitToList (habit habitObj){ habitList.add(habitObj); }

    // remove habit form list
    public void  removeHabitFormList (habit habitObj ) { habitList.remove(habitObj); }

    //get list
    public List<habit> getHabitList() { return habitList; }
}
