package DataLayer;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConnection {

    private static Connection connection;

    public static void connect(String DB_NAME) {
        try {
            Path currentRelativePath = Paths.get("");
            String fullPath = currentRelativePath.toAbsolutePath().toString();
            String url = "jdbc:sqlite:" + fullPath + File.separator + DB_NAME;
            connection = DriverManager.getConnection(url);
        } catch (SQLException e) {
            throw new RuntimeException("Failed to connect to the database", e);
        }
    }

    public static Connection getConnection() {
        return connection;
    }

    public static void close() {
        if (connection != null) {
            try {
                connection.close();
            } catch (SQLException e) {
                throw new RuntimeException("Failed to close the database connection", e);
            }
        }
    }

    public static void open(String DB_NAME) {
        try {
            if (connection == null || connection.isClosed()) {
                connect(DB_NAME);
            }
        } catch (SQLException e) {
            throw new RuntimeException("Failed to open the database connection", e);
        }
    }
}
