package habitPop.model.DataBaseInterface;

import habitPop.model.Tab;

import java.sql.*;

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

     static final String DELETE_Tab = """
        DELETE FROM tabs
        WHERE tab_id = ?;
        """ ;

     static final String DELETE_TAB_HABIT = """
        DELETE FROM habit_tabs
        WHERE tab_id = ?;
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
                Connection connection = DatabaseConnection.connectToDB();
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

        try (
                Connection connection = DatabaseConnection.connectToDB();
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

    public boolean changeTabName(String OldName, String NewName){

        int test = getTabIdFromDb(OldName);

        if (test == -1){
            return false;
        }









        return false;
    }

    public boolean deleteTab(Tab tab){

        int test = getTabIdFromDb(tab.getName());

        if (test == -1) {return true; }  // it no item


        try (
                Connection connection = DatabaseConnection.connectToDB();
                PreparedStatement statement =
                        connection.prepareStatement(
                                DELETE_Tab
                        )
        ) {

            statement.setString(1, tab.getName());


            try (ResultSet generatedKeys =
                         statement.getGeneratedKeys()) {

                if (generatedKeys.next()) {
                    return false;
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }

        return false;




    }



}