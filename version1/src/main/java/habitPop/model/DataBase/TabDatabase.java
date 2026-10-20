package habitPop.model.DataBase;

import habitPop.model.Tab;

import java.sql.*;

public class TabDatabase {

    private static final String Insert_tab_Sql = """
        INSERT INTO history(name)
        VALUES (?)
        """;

    private static final String Insert_Tab_Habit_Sql = """
        INSERT INTO habit_tabs(habit_id, tab_id)
        VALUES (?, ?)
        """;

    public void addTab(Tab tab) throws SQLException {
        int id = insertTab(tab);
    }

    private int insertTab(Tab tab) {

        int test = getTabIdFromDb(tab.getName());

        if (test != -1) {
            return test;
        }

        try (
                Connection connection = Database.connectToDB();
                PreparedStatement statement =
                        connection.prepareStatement(
                                Insert_tab_Sql,
                                Statement.RETURN_GENERATED_KEYS
                        )
        ) {

            statement.setString(1, tab.getName());

            statement.executeUpdate();

            try (ResultSet generatedKeys =
                         statement.getGeneratedKeys()) {

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

    private int getTabIdFromDb(String name) {

        final String SELECT_TAB_SQL = """
            SELECT id, name
            FROM tabs
            WHERE name = ?
            """;

        try (
                Connection connection = Database.connectToDB();
                PreparedStatement statement =
                        connection.prepareStatement(SELECT_TAB_SQL)
        ) {

            statement.setString(1, name);

            try (ResultSet result = statement.executeQuery()) {

                if (result.next()) {
                    return result.getInt("id");
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return -1;
    }
}