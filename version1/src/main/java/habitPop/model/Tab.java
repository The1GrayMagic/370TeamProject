package habitPop.model;


import java.util.ArrayList;
import java.util.List;

public class Tab {

    String name;

    int id = -1; //default no id, used for db management


    List<Habit> habitList = new ArrayList<>();



    //    ----------------------------------------------------
    // create tab only via name
    public Tab (String nameNew) { this.name=nameNew; }

    // create tab if list is already made
    public Tab(String name, List<Habit> habits) {
        this.name = name;
        this.habitList = habits;
    }


    public String getName (){ return name; }

    public int getId (){ return id; }

    public void setId (int idInput){ id = idInput; }
    public void setId (){ id = -1; }





    //add Habit object to list
    public void addHabitToList (Habit habitObj){ habitList.add(habitObj); }

    // remove Habit form list
    public void  removeHabitFormList (Habit habitObj ) { habitList.remove(habitObj); }

    //get list
    public List<Habit> getHabitList() { return habitList; }
}
