package habitPop.model.DataBaseInterface;

import habitPop.model.DayStatus;
import habitPop.model.Habit;
import habitPop.model.Tab;

import java.sql.SQLException;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Map;

public class DatabaseManager {


    private final HabitDatabase habitDatabase;
    private final ScheduleDatabase scheduleDatabase;
    private final TabDatabase tabDatabase;
    private final HistoryDatabase historyDatabase;

    public DatabaseManager() {

        habitDatabase = new HabitDatabase();
        scheduleDatabase = new ScheduleDatabase();
        tabDatabase = new TabDatabase();
        historyDatabase = new HistoryDatabase();
    }

// ------------- Habit ----------------------

    public boolean addHabit(Habit habit) {

        // 1. Insert basic habit
        int habitId = habitDatabase.insertHabit(habit);

        if (habitId == -1) {
            return false;
        }

        // Store generated database ID in the object
        habit.setId(habitId);

        // 2. Insert schedule
        boolean scheduleSuccess = scheduleDatabase.insertSchedule(habit.getTimesOfWeek(),habitId);

        if (!scheduleSuccess) { return false; }

        //if (habit)
        /*
         * Later:
         *
         * if Habit contains tabs:
         *
         * tabDatabase.addHabitTabs(...)
         */


        return true;
    }

    public Habit getHabit(int id) {

        // Basic habit information
        Habit habit = habitDatabase.getHabit(id);

        if (habit == null) {
            return null;
        }


        // Load its schedule
        Map<DayOfWeek, List<LocalTime>> schedule = scheduleDatabase.getSchedule(id);


        /*
         * Depending on your Habit class,
         * you need some way to put this schedule
         * into the Habit object.
         *
         * Example idea:
         *
         * habit.setTimesOfWeek(schedule);
         *
         * OR loop through it and call whatever
         * schedule methods your Habit class has.
         */


        return habit;
    }


    // ---------------------------------------------------------
    // GET ALL HABITS
    // ---------------------------------------------------------

    public List<Habit> getHabits() {

        /*
         * This returns the basic habits.
         *
         * You do NOT necessarily need to load
         * all history here.
         */

        return habitDatabase.getHabits();
    }


    // ---------------------------------------------------------
    // UPDATE HABIT
    // ---------------------------------------------------------

    public boolean updateHabit(Habit habit) {

        if (habit.getId() == -1) {
            return false;
        }


        // Update basic information
        boolean habitSuccess =
                habitDatabase.updateHabit(
                        habit.getId(),
                        habit
                );

        if (!habitSuccess) {
            return false;
        }


        // Update schedule
        boolean scheduleSuccess =
                scheduleDatabase.updateSchedule(
                        habit.getId(),
                        habit.getTimesOfWeek()
                );

        if (!scheduleSuccess) {
            return false;
        }


        /*
         * Later:
         *
         * update habit-tab relationships here
         * if those are stored inside Habit.
         */


        return true;
    }


    // ---------------------------------------------------------
    // DELETE HABIT
    // ---------------------------------------------------------

    public boolean deleteHabit(Habit habit) {

        if (habit == null) {
            return false;
        }

        if (habit.getId() == -1) {
            return false;
        }

        int habitId =
                habit.getId();


        /*
         * Delete related data first.
         *
         * Eventually SQLite ON DELETE CASCADE
         * could do some of this automatically.
         */


        // Delete history
        historyDatabase.deleteHabitHistory(
                habitId
        );


        // Delete schedule
        scheduleDatabase.deleteSchedule(
                habitId
        );


        /*
         * Later:
         *
         * tabDatabase.removeHabitFromAllTabs(habitId);
         */


        // Finally delete the actual habit
        return habitDatabase.deleteHabit(
                habitId
        );
    }


    // =========================================================
    // SCHEDULE
    // =========================================================

    public Map<DayOfWeek, List<LocalTime>>
    getHabitSchedule(int habitId) {

        return scheduleDatabase.getSchedule(
                habitId
        );
    }


    public boolean updateHabitSchedule(
            Habit habit) {

        if (habit == null ||
                habit.getId() == -1) {

            return false;
        }

        return scheduleDatabase.updateSchedule(
                habit.getId(),
                habit.getTimesOfWeek()
        );
    }


    // =========================================================
    // TAB
    // =========================================================

    public int addTab(Tab tab) {

        if (tab == null) {
            return -1;
        }

        /*
         * Assuming your TabDatabase.insertTab()
         * returns the tab ID.
         */

        int tabId =
                tabDatabase.insertTab(tab);


        /*
         * If your Tab model has an ID:
         *
         * if (tabId != -1) {
         *     tab.setId(tabId);
         * }
         */


        return tabId;
    }


    public boolean updateTab(Tab tab) {

        if (tab == null) {
            return false;
        }

        /*
         * This assumes Tab now stores its DB ID.
         *
         * If your Tab class doesn't yet,
         * you should add that just like Habit.
         */
return false;
       // return tabDatabase.updateTab(
         //       tab
        //);
    }


    public boolean deleteTab(Tab tab) {

        if (tab == null) {
            return false;
        }

        return tabDatabase.deleteTab(
                tab
        );
    }


    // =========================================================
    // HISTORY
    // =========================================================

    public boolean addHistory(
            Habit habit,
            List<DayStatus> history) {

        if (habit == null ||
                habit.getId() == -1) {

            return false;
        }

        return historyDatabase.insertHistory(
                history,
                habit.getId()
        );
    }


    // ---------------------------------------------------------
    // ADD ONE DAY OF HISTORY
    // ---------------------------------------------------------

    public boolean addHistoryDay(
            Habit habit,
            DayStatus day) {

        if (habit == null ||
                day == null ||
                habit.getId() == -1) {

            return false;
        }

        return historyDatabase.addDayHabitHistory(
                habit.getId(),
                day
        );
    }


    // ---------------------------------------------------------
    // GET HISTORY
    // ---------------------------------------------------------

    public List<DayStatus> getHabitHistory(
            Habit habit) {

        if (habit == null ||
                habit.getId() == -1) {

            return null;
        }

      //  return historyDatabase.getHabitHistory(
       //         habit.getId()
       // );
        return null;
    }


    // ---------------------------------------------------------
    // GET ONE HISTORY DAY
    // ---------------------------------------------------------

    public DayStatus getHabitHistoryDay(
            Habit habit,
            LocalDate date) {

        if (habit == null ||
                date == null ||
                habit.getId() == -1) {

            return null;
        }

        return null;
     //   return historyDatabase.getHabitHistoryDay(
       //         habit.getId(),
         //       date
        //);
    }



    public boolean updateHabitHistoryDay(Habit habit, DayStatus day) {

        if (habit == null || day == null || habit.getId() == -1) {
            return false;
        }
        return false;
        //return historyDatabase.updateHabitHistoryDay(habit.getId(), day);
    }

    public boolean deleteHistoryDay(Habit habit, LocalDate date) {

        if (habit == null || date == null || habit.getId() == -1) {
            return false;
        }
        return true;
}
    public static void main(String[] args) {
        // this will use to test db

        DatabaseInitializer.initialize();


    }


}
