package habitPop.model;


import java.sql.*;


public class HabitDatabase {

    private static final String  DATA_LOCATION = "jdbc:sqlite:data/habitpop.db";




    private Connection connectToDB() throws SQLException {
        return DriverManager.getConnection(DATA_LOCATION);
    }

    public void initializeDatabase(){



    }




    public void addHabit(Habit habit) throws SQLException {
        try (Connection connection = connectToDB()) {  // connect to db;
            // prepare INSERT here






        }catch (SQLException e){  // catch if db does not connect;
            System.out.println(e.getMessage());
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



    public static void main(String[] args)   // test function
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

