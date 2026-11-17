package habitPop.model;

import habitPop.controller.UpcomingController;
import habitPop.model.DataBaseInterface.DatabaseManager;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.util.List;


//Hold currently selected IDs, dates, and other shared UI state
public class AppStatus {

    private final ObservableList<Habit> habits;
    private final ObservableList<Tab> tabs;
    private DatabaseManager databaseManager;


    private Habit selectedHabit;
    private Boolean UpcomingFrist = true;

    public AppStatus() {

        habits = FXCollections.observableArrayList();
        tabs = FXCollections.observableArrayList();
        UpcomingFrist =true;
        selectedHabit = null;

    }

    public boolean getHomePageUpcoming() {
        return UpcomingFrist;
    }
    public  void SwitchPageUpcoming() {
         this.UpcomingFrist = !UpcomingFrist;
    }



    // ---------------- Habits ----------------

    public void getHabitFormDB(){
        habits.setAll(databaseManager.getHabits());
    }

    public ObservableList<Habit> getHabits() {
        return habits;
    }


    public void setHabits(Iterable<Habit> loadedHabits) {

        habits.clear();

        for (Habit habit : loadedHabits) {
            habits.add(habit);
        }
    }


    public void addHabit(Habit habit) {
        habits.add(habit);
    }


    public void removeHabit(Habit habit) {
        habits.remove(habit);
    }



    // ---------------- Tabs ----------------

    public ObservableList<Tab> getTabs() {
        return tabs;
    }


    public void setTabs(Iterable<Tab> loadedTabs) {

        tabs.clear();

        for (Tab tab : loadedTabs) {
            tabs.add(tab);
        }
    }


    public void addTab(Tab tab) {
        tabs.add(tab);
    }


    public void removeTab(Tab tab) {
        tabs.remove(tab);
    }


    // ---------------- Selected Habit ----------------

    public Habit getSelectedHabit() {
        return selectedHabit;
    }

    public void setSelectedHabit(Habit selectedHabit) {
        this.selectedHabit = selectedHabit;
    }


    public void setDatabaseManager(DatabaseManager databaseManager) {
        this.databaseManager = databaseManager;
    }

}
