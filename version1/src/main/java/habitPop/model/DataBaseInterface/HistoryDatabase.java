package habitPop.model.DataBaseInterface;

import habitPop.model.DayStatus;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;


public class HistoryDatabase {

    static final String Insert_History_Sql = """
        INSERT INTO history(habit_id, date, time, completed)
        VALUES (?, ?, ?, ?)
        """;

    static final String FIND_HISTORY_SQL = """
        SELECT habit_id, date, time, completed
        FROM history
        WHERE habit_id = ?
        """;

    static final String FIND_HISTORY_DAY_SQL = """
        SELECT habit_id, date, time, completed
        FROM history
        WHERE habit_id = ?
        AND date = ?
        """;

    static final String UPDATE_COMPLETED_HISTORY_SQL = """
        UPDATE history
        SET completed = ?
        WHERE habit_id = ?
        AND date = ?
        AND time = ?
        """;

    static final String DELETE_HISTORY_DAY_SQL = """
        DELETE FROM history
        WHERE habit_id = ?
        AND date = ?
        """;

    static final String DELETE_ALL_HISTORY_SQL = """
        DELETE FROM history
        WHERE habit_id = ?
        """;



    public boolean insertHistory(List<DayStatus> history, int habitId) {

        try (
                Connection connection = DatabaseConnection.connectToDB();
                PreparedStatement statement =
                        connection.prepareStatement(Insert_History_Sql)
        ) {

            for (DayStatus dayStatus : history) {

                for (Map.Entry<LocalTime, Boolean> entry
                        : dayStatus.getCompletions().entrySet()) {

                    statement.setInt(1, habitId);
                    statement.setString(
                            2,
                            dayStatus.getDate().toString()
                    );
                    statement.setString(
                            3,
                            entry.getKey().toString()
                    );
                    statement.setBoolean(
                            4,
                            entry.getValue()
                    );

                    statement.executeUpdate();
                }
            }

            return true;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean addDayHabitHistory(int habitId, DayStatus day) {
        try (
                Connection connection = DatabaseConnection.connectToDB();
                PreparedStatement statement =
                        connection.prepareStatement(Insert_History_Sql)
        ) {


            for (Map.Entry<LocalTime, Boolean> entry : day.getCompletions().entrySet()) {

                statement.setInt(1, habitId);
                statement.setString(
                        2,
                        day.getDate().toString()
                );
                statement.setString(
                        3,
                        entry.getKey().toString()
                );
                statement.setBoolean(
                        4,
                        entry.getValue()
                );

                statement.executeUpdate();
            }
            return true;


        }catch (SQLException e) {
            e.printStackTrace();
        }


        return false;
    }

    public List<DayStatus> updateHabitHistoryDay(int habit_id,DayStatus day ) throws SQLException {
        try (
                Connection connection = DatabaseConnection.connectToDB();
                PreparedStatement statement =
                        connection.prepareStatement(FIND_HISTORY_SQL);
        ) {


            statement.setInt(1, habit_id);
            statement.setString(2, day.getDate().toString());

            try (ResultSet result = statement.executeQuery()) {
                while (result.next()){





                }
            }



        }catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }



    public List<DayStatus> getHabitHistory(int habit_id) throws SQLException {
        try (
                Connection connection = DatabaseConnection.connectToDB();
                PreparedStatement statement =
                        connection.prepareStatement(FIND_HISTORY_SQL);
        ) {


        statement.setInt(1, habit_id);
            try (ResultSet result = statement.executeQuery()) {
                List<DayStatus> history = new ArrayList<>();

                 createList(history, result);
                 return history;
           }

        }catch (SQLException e) {
        e.printStackTrace();
    }

    return null;
    }

    private void createList(List<DayStatus> history, ResultSet result) throws SQLException {

        while (result.next()) {

            LocalDate date =
                    LocalDate.parse(result.getString("date"));

            LocalTime time =
                    LocalTime.parse(result.getString("time"));

            boolean completed =
                    result.getBoolean("completed");

            DayStatus existingDay = null;

            for (DayStatus dayStatus : history) {
                if (dayStatus.getDate().equals(date)) {
                    existingDay = dayStatus;
                    break;
                }
            }

            if (existingDay != null) {

                existingDay.updateOrAdd(time, completed);

            } else {

                DayStatus newDay =
                        new DayStatus(date, List.of(time));

                newDay.updateInList(time, completed);

                history.add(newDay);
            }
        }
    }


}