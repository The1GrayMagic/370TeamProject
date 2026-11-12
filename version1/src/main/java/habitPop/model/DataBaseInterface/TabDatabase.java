package habitPop.model.DataBaseInterface;

import habitPop.model.Habit;
import habitPop.model.Tab;

import java.sql.*;
import java.util.List;

public class TabDatabase {

    static final String Insert_tab_Sql = """
            INSERT INTO tabs(name)
            VALUES (?)
            """;

    static final String Insert_Tab_Habit_Sql = """
            INSERT INTO habit_tabs(habit_id, tab_id)
            VALUES (?, ?)
            """;

    static final String SELECT_TAB_SQL = """
            SELECT id, name
            FROM tabs
            WHERE name = ?
            """;

    static final String UPDATE_TAB = """
            UPDATE tabs
            SET name = ?
            WHERE id = ?
            """;

    static final String DELETE_TAB = """
            DELETE FROM tabs
            WHERE id = ?;
            """;

    static final String DELETE_TAB_HABIT = """
            DELETE FROM habit_tabs
            WHERE tab_id = ?;
            """;


    public boolean addTab(Tab tab) throws SQLException {
        int id = insertTab(tab);
        return id != -1;
    }

    int insertTab(Tab tab) {

        int test = getTabIdFromDb(tab.getName());

        if (test != -1) {
            return test;
        }

        try (
                Connection connection = DatabaseConnection.connectToDB();
                PreparedStatement statement = connection.prepareStatement(Insert_tab_Sql, Statement.RETURN_GENERATED_KEYS)
        ) {

            statement.setString(1, tab.getName());

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

    int getTabIdFromDb(String name) {

        try (
                Connection connection = DatabaseConnection.connectToDB();
                PreparedStatement statement = connection.prepareStatement(SELECT_TAB_SQL)
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

    boolean insertHabitTabs(Tab tab) {

        int tabId = getTabIdFromDb(tab.getName());

        if (tabId == -1) {
            return false;
        }

        try (
                Connection connection = DatabaseConnection.connectToDB();
                PreparedStatement statement = connection.prepareStatement(Insert_Tab_Habit_Sql)
        ) {

            List<Habit> habits = tab.getHabitList();

            for (Habit habit : habits) {

                int habitId = habit.getId();

                statement.setInt(1, habitId);
                statement.setInt(2, tabId);

                statement.addBatch();
            }

            statement.executeBatch();

            return true;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    boolean changeTabName(String OldName, String NewName) {

        int tabId = getTabIdFromDb(OldName);

        if (tabId == -1) {
            return false;
        }


        try (
                Connection connection = DatabaseConnection.connectToDB();
                PreparedStatement statement = connection.prepareStatement(UPDATE_TAB)
        ) {

            statement.setString(1, NewName);
            statement.setInt(2, tabId);

            statement.executeUpdate();
            return true;

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    boolean deleteTab(Tab tab) {

        int tabId = getTabIdFromDb(tab.getName());

        if (tabId == -1) {
            return true; // already doesn't exist
        }

        try (
                Connection connection = DatabaseConnection.connectToDB();
                PreparedStatement statementTabHibit = connection.prepareStatement(DELETE_TAB_HABIT);
                PreparedStatement statementTab = connection.prepareStatement(DELETE_TAB);
        ) {


            statementTabHibit.setInt(1, tabId);
            int relationsDeleted = statementTabHibit.executeUpdate();
            statementTab.setInt(1, tabId);
            int tabsDeleted = statementTab.executeUpdate();
            return tabsDeleted > 0 && relationsDeleted > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

}