package habitPop.model;


import java.sql.*;
import java.time.DayOfWeek;
import java.time.LocalTime;
import java.util.List;
import java.util.Map;


public class HabitDatabase {

    private static final String  DATA_LOCATION = "jdbc:sqlite:data/habitpop.db";

    //------------------------------- Habits sql presentment ----------------------------------------
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

    //------------------------------------- History sql presentment ---------------------------------------------
    private static final String Insert_History_Sql = """
    INSERT INTO history(habit_id, date, time, completed)
    VALUES (?, ?, ?, ?)
    """;

    //------------------------------------- DaysOfTheWeek sql presentment ---------------------------------------------
    private static final String Insert_DaysOfWeek_Sql = """
    INSERT INTO history(habit_id, date, time)
    VALUES (?, ?, ?)
    """;

    //------------------------------------- Tab sql presentment ---------------------------------------------
    // this just create the tab in the db
    private static final String Insert_tab_Sql = """
    INSERT INTO history(name)
    VALUES (?)
    """;

    //this associate the relations of tab to habit,     many to many
    private static final String Insert_Tab_Habit_Sql = """
    INSERT INTO habit_tabs(habit_id, tab_id)
    VALUES (?, ?)
    """;



    /*Connects to db, and enables PRAGMA synchronous,PRAGMA foreign key, and PRAGMA synchronous */
    private Connection connectToDB() throws SQLException {
        Connection connection = DriverManager.getConnection(DATA_LOCATION);
        try (Statement statement = connection.createStatement()) {

            statement.execute("PRAGMA journal_mode = WAL;");
            // This enables the feature to uses a write-ahead log.
            // Helps with crash/power-failure recovery and allows better read/write concurrency.

            statement.execute("PRAGMA synchronous = FULL;");
            // Makes SQLite wait for important writes to be flushed more safely.
            // Improves durability if the app or system loses power.

            statement.execute("PRAGMA foreign_keys = ON;");
            // Enforces relationships between tables so invalid/orphaned reference are rejected.
            // probably have tab home page by default for all habits so dost reject form db
        }

        return connection;
    }


    public void initializeDatabase(){



    }





    // inputting data into db is different from just the habit object. sqlite is base on relational types, this allows easier searches
    public void addHabit(Habit habit) throws SQLException {
        int id = insertHabit(habit);
        insertHistory(habit.getHistory(), id);
        insertDaysOfWeeks(habit.timesOfWeek, id);
    }

    public void addTab(Tab tab) throws SQLException {





    }


    private int insertHabit(Habit habit){
        try (
                Connection connection = connectToDB();
                PreparedStatement statement =
                        connection.prepareStatement(Insert_Habit_Sql, Statement.RETURN_GENERATED_KEYS )
        ) {

            statement.setString(1, habit.getName());
            statement.setString(2, habit.getPurpose());
            statement.setInt(3, habit.getPriority());



            statement.executeUpdate();

            try (ResultSet generatedKeys = statement.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    int habitId = generatedKeys.getInt(1);
                    return habitId;
                }
            }


        } catch (SQLException e) {
            e.printStackTrace();
            return -1;
        }
        return -1;
    }

    private boolean insertHistory(List<DayStatus> history, int habitId){
        try (
                Connection connection = connectToDB();
                PreparedStatement statement =
                        connection.prepareStatement(Insert_History_Sql )
        ) {

            for (DayStatus dayStatus : history) {

                for (Map.Entry<LocalTime, Boolean> entry
                        : dayStatus.getCompletions().entrySet()) {

                    statement.setInt(1, habitId);
                    statement.setString(2, dayStatus.getDate().toString());
                    statement.setString(3, entry.getKey().toString());
                    statement.setBoolean(4, entry.getValue());

                    statement.executeUpdate(); // push to the table in db
                }
            }
            return true;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }


    }

    private boolean insertDaysOfWeeks(Map<DayOfWeek, List<LocalTime>> timesOfWeek, int habitId){
        try (
                Connection connection = connectToDB();
                PreparedStatement statement =
                        connection.prepareStatement(Insert_History_Sql )
        ) {


                for (Map.Entry<DayOfWeek, List<LocalTime>> dayEntry : timesOfWeek.entrySet()) {

                    DayOfWeek day = dayEntry.getKey();

                    for (LocalTime time : dayEntry.getValue()) {

                        statement.setInt(1, habitId);
                        statement.setString(2, day.toString());
                        statement.setString(3, time.toString());

                        statement.addBatch(); // set it as a batch
                    }
                }

                statement.executeBatch();  // which uploads as a group

            return true;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }


    }

    private int insertTab(Tab tab){
        int test = getTabIdFormDb(tab.getName()); // tabs are  s
        if (test != -1){return test;}  // if item exits then return id of tab
        //create new item
        try (
                Connection connection = connectToDB();
                PreparedStatement statement =
                        connection.prepareStatement(Insert_tab_Sql, Statement.RETURN_GENERATED_KEYS )
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


    private int getTabIdFormDb(String name){
          final String SELECT_TAB_SQL = """
    SELECT id, name
    FROM tabs
    WHERE name = ?
    """;
        try (
                Connection connection = connectToDB();
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

    private int getHabitIdFormDb(String name){
        final String SELECT_HABIT_SQL = """
    SELECT id, name, purpose, priority
    FROM habits
    WHERE id = ?
    """;
        try (
                Connection connection = connectToDB();
                PreparedStatement statement =
                        connection.prepareStatement(SELECT_HABIT_SQL)
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






    public boolean updateHabit(){

        return false;
    }

    public boolean deleteHabit(){

        return false;
    }

    public void  getHabits(){


    }



    public static void main(String[] args)   // test function, from an example on GitHub
    {
        // NOTE: Connection and Statement are AutoCloseable.
        //       Don't forget to close them both in order to avoid leaks.
        try
                (
                        // create a database connection  if their no db, it will create one.
                        Connection connection = DriverManager.getConnection(DATA_LOCATION);
                        Statement statement = connection.createStatement();
                )
        {
            statement.setQueryTimeout(30);  // set timeout to 30 sec.

            statement.executeUpdate("drop table if exists person");
            statement.executeUpdate("create table person (id integer, name string)");
            statement.executeUpdate("insert into person values(1, 'leo')");
            statement.executeUpdate("insert into person values(2, 'yui')");
            ResultSet rs = statement.executeQuery("select * from person");
            while(rs.next())
            {
                // read the result set
                System.out.println("name = " + rs.getString("name"));
                System.out.println("id = " + rs.getInt("id"));
            }
        }
        catch(SQLException e)
        {
            // if the error message is "out of memory",
            // it probably means no database file is found
            e.printStackTrace(System.err);
        }
    }
}

