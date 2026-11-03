package habitPop.model.DataBaseInterface;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import java.time.DayOfWeek;
import java.time.LocalTime;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class ScheduleDatabase {

    static final String INSERT_SCHEDULE_SQL = """
        INSERT INTO schedules(habit_id, day, time)
        VALUES (?, ?, ?)
        """;

    static final String FIND_SCHEDULE_SQL = """
        SELECT day, time
        FROM schedules
        WHERE habit_id = ?
        """;

    static final String DELETE_SCHEDULE_SQL = """
        DELETE FROM schedules
        WHERE habit_id = ?
        """;

    boolean insertSchedule(Map<DayOfWeek, List<LocalTime>> timesOfWeek, int habitId) {

        try (
                Connection connection = DatabaseConnection.connectToDB();
                PreparedStatement statement = connection.prepareStatement(INSERT_SCHEDULE_SQL)
        ) {
            for (Map.Entry<DayOfWeek, List<LocalTime>> dayEntry : timesOfWeek.entrySet()) {

                DayOfWeek day = dayEntry.getKey();

                for (LocalTime time : dayEntry.getValue()) {

                    statement.setInt(1,habitId);

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

    boolean updateSchedule(int habitId, Map<DayOfWeek, List<LocalTime>> timesOfWeek) {

        if (!deleteSchedule(habitId)) {
            return false;
        }

        return insertSchedule(timesOfWeek, habitId);
    }

    boolean deleteSchedule(int habitId) {

        try (
                Connection connection = DatabaseConnection.connectToDB();
                PreparedStatement statement = connection.prepareStatement(DELETE_SCHEDULE_SQL)
        ) {

            statement.setInt(1, habitId);

            statement.executeUpdate();

            return true;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    Map<DayOfWeek, List<LocalTime>> getSchedule(int habitId) {

        Map<DayOfWeek, List<LocalTime>> timesOfWeek = new HashMap<>();

        try (
                Connection connection = DatabaseConnection.connectToDB();
                PreparedStatement statement = connection.prepareStatement(FIND_SCHEDULE_SQL)
        ) {

            statement.setInt(1, habitId);

            try (
                    ResultSet result = statement.executeQuery()
            ) {

                while (result.next()) {

                    DayOfWeek day = DayOfWeek.valueOf(result.getString("day"));

                    LocalTime time = LocalTime.parse(result.getString("time"));

                    if (!timesOfWeek.containsKey(day)) {
                        timesOfWeek.put(day, new ArrayList<>());
                    }
                    timesOfWeek.get(day).add(time);
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return timesOfWeek;
    }

}