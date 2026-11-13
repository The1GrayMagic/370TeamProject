package habitPop.model.DataBaseInterface;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import habitPop.model.Habit;

public class HabitDatabase {

    // no need for private, only those in package habitPop.model.DataBaseInterface can access the Strings
     static final String Insert_Habit_Sql = """
        INSERT INTO habits(name, purpose, priority)
        VALUES (?, ?, ?)
        """;

     static final String Update_Habit_Sql = """
        UPDATE habits
        SET name = ?, purpose = ?, priority = ?
        WHERE id = ?
        """;

     static final String Delete_Habit_Sql = """
        DELETE FROM habits
        WHERE id = ?
        """;

     static final String Select_Habit_Sql = """
        SELECT id, name, purpose, priority
        FROM habits
        WHERE id = ?
        """;

    static final String Select_All_Habits_Sql = """
        SELECT id, name, purpose, priority
        FROM habits
        """;


    int insertHabit(Habit habit) {

        try (
                Connection connection = DatabaseConnection.connectToDB();
                PreparedStatement statement = connection.prepareStatement(Insert_Habit_Sql, Statement.RETURN_GENERATED_KEYS)
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

    boolean updateHabit(int id, Habit habit) {

        try (
                Connection connection = DatabaseConnection.connectToDB();
                PreparedStatement statement = connection.prepareStatement(Update_Habit_Sql)
        ) {

            statement.setString(1, habit.getName());
            statement.setString(2, habit.getPurpose());
            statement.setInt(3, habit.getPriority());
            statement.setInt(4, id);

            int rowsUpdated = statement.executeUpdate();

            return rowsUpdated > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    boolean deleteHabit(int id) {

        try (
                Connection connection = DatabaseConnection.connectToDB();
                PreparedStatement statement = connection.prepareStatement(Delete_Habit_Sql)
        ) {

            statement.setInt(1, id);

            int rowsDeleted = statement.executeUpdate();

            return rowsDeleted > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    Habit getHabit(int id) {

        try (
                Connection connection = DatabaseConnection.connectToDB();

                PreparedStatement statement = connection.prepareStatement(Select_Habit_Sql)
        ) {

            statement.setInt(1, id);

            try (
                    ResultSet result = statement.executeQuery()
            ) {

                if (result.next()) {

                    String name = result.getString("name");

                    String purpose = result.getString("purpose");

                    int priority = result.getInt("priority");

                    return new Habit(name,purpose,priority);
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

    List<Habit> getHabits() {

        List<Habit> habits = new ArrayList<>();

        try (
                Connection connection = DatabaseConnection.connectToDB();
                PreparedStatement statement = connection.prepareStatement(Select_All_Habits_Sql);
                ResultSet result = statement.executeQuery()
        ) {

            while (result.next()) {

                int id = result.getInt("id");
                String name = result.getString("name");
                String purpose = result.getString("purpose");
                int priority = result.getInt("priority");
                Habit habit = new Habit(name, purpose, priority);
                habit.setId(id);
                habits.add(habit);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return habits;
    }


}