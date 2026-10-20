package habitPop.model.DataBase;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class Database {

    private static final String DATA_LOCATION =
            "jdbc:sqlite:data/habitpop.db";

    public static Connection connectToDB() throws SQLException {

        Connection connection =
                DriverManager.getConnection(DATA_LOCATION);

        try (Statement statement = connection.createStatement()) {
            statement.execute("PRAGMA journal_mode = WAL;");
            statement.execute("PRAGMA synchronous = FULL;");
            statement.execute("PRAGMA foreign_keys = ON;");
        }

        return connection;
    }
}