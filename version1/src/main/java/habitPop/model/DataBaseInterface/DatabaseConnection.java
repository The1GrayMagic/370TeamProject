package habitPop.model.DataBaseInterface;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class DatabaseConnection {

    // no need for private, only those in package habitPop.model.DataBaseInterface can access the Strings
    // Primary database location
    static final String DATA_LOCATION = "jdbc:sqlite:version1/data/habitpop.db";

    // Fallback database location
    private static final Path DB_PATH =
            Path.of("data", "habitpop.db").toAbsolutePath();

    public static Connection connectToDB() throws SQLException {

        Connection connection;

        try {
            // Try primary location first
            connection = DriverManager.getConnection(DATA_LOCATION);
            System.out.println("Connected to primary database.");

        } catch (SQLException e) {

            System.out.println("Primary database failed. Trying fallback...");

            // Ensure fallback directory exists
            try {
                Files.createDirectories(DB_PATH.getParent());
            } catch (IOException ex) {
                throw new SQLException("Failed to create database directory", ex);
            }

            // Connect to fallback database
            connection = DriverManager.getConnection(
                    "jdbc:sqlite:" + DB_PATH
            );

            System.out.println("Connected to fallback database: " + DB_PATH);
        }

        // Configure SQLite
        try (Statement statement = connection.createStatement()) {

            statement.execute("PRAGMA journal_mode = WAL;");
            statement.execute("PRAGMA synchronous = FULL;");
            statement.execute("PRAGMA foreign_keys = ON;");

        } catch (SQLException e) {
            try {
                connection.close();
            } catch (SQLException closeError) {
                e.addSuppressed(closeError);
            }
            throw e;
        }

        return connection;
    }
}