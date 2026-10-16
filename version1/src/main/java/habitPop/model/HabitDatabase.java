package habitPop.model;


import java.sql.*;


public class HabitDatabase {

    private static final String  DATA_LOCATION = "jdbc:sqlite:data/habitpop.db";



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
        String sql = """
        INSERT INTO habits(name, purpose, priority)
        VALUES (?, ?, ?)
        """; // this will get an id in db, this id will be used to identify it connection parts, its history, ect/

        try (
                Connection connection = connectToDB();
                PreparedStatement statement =
                        connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS )
        ) {

            statement.setString(1, habit.getName());
            statement.setString(2, habit.getPurpose());
            statement.setInt(3, habit.getPriority());



            statement.executeUpdate();

            ResultSet generatedKeys = statement.getGeneratedKeys();

            if (generatedKeys.next()) {
                int habitId = generatedKeys.getInt(1);
                System.out.println("New habit ID: " + habitId);
            }



        } catch (SQLException e) {
            e.printStackTrace();
        }

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

