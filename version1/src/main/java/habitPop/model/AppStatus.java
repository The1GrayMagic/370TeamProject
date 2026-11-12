package habitPop.model;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;


//Hold currently selected IDs, dates, and other shared UI state
public class AppStatus {

    private final ObservableList<Habit> habits;
    private final ObservableList<Tab> tabs;

    private Habit selectedHabit;

    public AppStatus() {

        habits = FXCollections.observableArrayList();
        tabs = FXCollections.observableArrayList();

        selectedHabit = null;
    }

    // ---------------- Habits ----------------

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
}
