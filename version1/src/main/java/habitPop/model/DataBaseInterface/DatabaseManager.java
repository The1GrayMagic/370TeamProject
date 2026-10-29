package habitPop.model.DataBaseInterface;

import habitPop.model.Habit;
import habitPop.model.Tab;

import java.sql.SQLException;

public class DatabaseManager {


    private final HabitDatabase habitDatabase;
    private final TabDatabase tabDatabase;
    private final HistoryDatabase historyDatabase;

    public DatabaseManager() {
        habitDatabase = new HabitDatabase();
        tabDatabase = new TabDatabase();
        historyDatabase = new HistoryDatabase();
    }

    public void addHabit(Habit habit) throws SQLException {
        if (habitDatabase.addHabit(habit))
            return;
    }

    public void addTab(Tab tab) throws SQLException {

    }



    public static void main(String[] args) {
        // this will use to test db

        DatabaseInitializer.initialize();


    }


}
