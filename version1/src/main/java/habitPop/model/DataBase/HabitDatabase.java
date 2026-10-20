package habitPop.model.DataBase;

import habitPop.model.Habit;

import java.sql.*;
import java.time.DayOfWeek;
import java.time.LocalTime;
import java.util.List;
import java.util.Map;

public class HabitDatabase {

    private static final String Insert_Habit_Sql = """
        INSERT INTO habits(name, purpose, priority)
        VALUES (?, ?, ?)
        """;

    private static final String Update_Habit_Sql = """
        UPDATE habits
        SET name = ?, purpose = ?, priority = ?
        WHERE id = ?
        """;

    private static final String Delete_Habit_Sql = """
        DELETE FROM habits
        WHERE id = ?
        """;

    private static final String Select_Habit_Sql = """
        SELECT id, name, purpose, priority
        FROM habits
        WHERE id = ?
        """;

    private static final String Insert_DaysOfWeek_Sql = """
        INSERT INTO history(habit_id, date, time)
        VALUES (?, ?, ?)
        """;

    public void addHabit(Habit habit) throws SQLException {
        int id = insertHabit(habit);
        insertDaysOfWeeks(habit.getTimesOfWeek(), id);
    }

    private int insertHabit(Habit habit) {

        try (
                Connection connection = Database.connectToDB();
                PreparedStatement statement =
                        connection.prepareStatement(
                                Insert_Habit_Sql,
                                Statement.RETURN_GENERATED_KEYS
                        )
        ) {

            statement.setString(1, habit.getName());
            statement.setString(2, habit.getPurpose());
            statement.setInt(3, habit.getPriority());

            statement.executeUpdate();

            try (ResultSet generatedKeys = statement.getGeneratedKeys()) {

                if (generatedKeys.next()) {
                    return generatedKeys.getInt(1);
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
            return -1;
        }

        return -1;
    }

    private boolean insertDaysOfWeeks(
            Map<DayOfWeek, List<LocalTime>> timesOfWeek,
            int habitId) {

        try (
                Connection connection = Database.connectToDB();
                PreparedStatement statement =
                        connection.prepareStatement(Insert_DaysOfWeek_Sql)
        ) {

            for (Map.Entry<DayOfWeek, List<LocalTime>> dayEntry
                    : timesOfWeek.entrySet()) {

                DayOfWeek day = dayEntry.getKey();

                for (LocalTime time : dayEntry.getValue()) {

                    statement.setInt(1, habitId);
                    statement.setString(2, day.toString());
                    statement.setString(3, time.toString());

                    statement.addBatch();
                }
            }

            statement.executeBatch();

            return true;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean updateHabit(int Id) {

        return false;
    }

    public boolean updateHabitDays(int Id) {

        return false;
    }

    public boolean deleteHabit() {

        return false;
    }

    public void getHabits() {

    }
}