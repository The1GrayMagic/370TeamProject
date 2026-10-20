package habitPop.model.DataBase;

import habitPop.model.DayStatus;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.time.LocalTime;
import java.util.List;
import java.util.Map;

public class HistoryDatabase {

    private static final String Insert_History_Sql = """
        INSERT INTO history(habit_id, date, time, completed)
        VALUES (?, ?, ?, ?)
        """;

    public boolean insertHistory(
            List<DayStatus> history,
            int habitId) {

        try (
                Connection connection = Database.connectToDB();
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

    public boolean updateHabitHistory(int Id) {

        return false;
    }
}