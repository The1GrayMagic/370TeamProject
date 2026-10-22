package habitPop.model.DataBaseInterface;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

public class DatabaseInitializer {

    /*Initialize The database and set predefined structures */
    public static void initialize() {

        String createHabits = """
            CREATE TABLE IF NOT EXISTS habits (
                id INTEGER PRIMARY KEY AUTOINCREMENT,
                name TEXT NOT NULL,
                purpose TEXT,
                priority INTEGER
            );
            """;

        String createTabs = """
            CREATE TABLE IF NOT EXISTS tabs (
                id INTEGER PRIMARY KEY AUTOINCREMENT,
                name TEXT NOT NULL UNIQUE
            );
            """;

        String createHabitTabs = """
            CREATE TABLE IF NOT EXISTS habit_tabs (
                habit_id INTEGER NOT NULL,
                tab_id INTEGER NOT NULL,

                FOREIGN KEY (habit_id)
                    REFERENCES habits(id),

                FOREIGN KEY (tab_id)
                    REFERENCES tabs(id)
            );
            """;

        String createSchedules = """
            CREATE TABLE IF NOT EXISTS schedules (
                id INTEGER PRIMARY KEY AUTOINCREMENT,
                habit_id INTEGER NOT NULL,
                day TEXT NOT NULL,
                time TEXT NOT NULL,

                FOREIGN KEY (habit_id)
                    REFERENCES habits(id)
            );
            """;

        String createHistory = """
            CREATE TABLE IF NOT EXISTS history (
                id INTEGER PRIMARY KEY AUTOINCREMENT,
                habit_id INTEGER NOT NULL,
                date TEXT NOT NULL,
                time TEXT NOT NULL,
                completed INTEGER NOT NULL,

                FOREIGN KEY (habit_id)
                    REFERENCES habits(id)
            );
            """;

        try (
                Connection connection = DatabaseConnection.connectToDB();   // connect to db
                Statement statement = connection.createStatement()
        ) {

            statement.execute(createHabits); // define habits
            statement.execute(createTabs);  // define Tabs
            statement.execute(createHabitTabs); // define habit tabs relations
            statement.execute(createSchedules); // define day of the week relation to habit
            statement.execute(createHistory); // define history relation for habits

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}