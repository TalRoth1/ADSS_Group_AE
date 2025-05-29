package DataLayer.DAOs;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.sql.Statement;

import DomainLayer.DriverDL;

public class DriverDAO {
    
    Connection connection;

    public DriverDAO(Connection connection) {
        this.connection = connection;
        try {
            InitializeDatabase();
        } catch (SQLException e) {
            System.out.println("Error initializing database: " + e.getMessage());
        }
    }

    public void InitializeDatabase() throws SQLException {
        String sql = "CREATE TABLE IF NOT EXISTS drivers ("
                + "id INTEGER PRIMARY KEY,"
                + "isBusy INT NOT NULL" // 0 = false, 1 = true
                + ", FOREIGN KEY (id) REFERENCES employees(id)"
                + ")";
        try (Statement stmt = connection.createStatement()) {
            stmt.execute(sql);
        } catch (SQLException e) {
            System.out.println("Error initializing database: " + e.getMessage());
            throw e; 
        }
    }

    public void addDriver(int id, int isBusy) throws SQLException {
        String sql = "INSERT INTO drivers (id, isBusy) VALUES (?, ?)";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            pstmt.setInt(2, isBusy);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Error adding driver: " + e.getMessage());
            throw e; 
        }
    }

    public ResultSet getDriver(int id) throws SQLException {
        String sql = "SELECT * FROM drivers WHERE id = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            return pstmt.executeQuery();
        } catch (SQLException e) {
            System.out.println("Error retrieving driver: " + e.getMessage());
            throw e; 
        }
    }

    public ResultSet getAllDrivers() throws SQLException {
        String sql = "SELECT * FROM drivers";
        try (Statement stmt = connection.createStatement()) {
            return stmt.executeQuery(sql);
        } catch (SQLException e) {
            System.out.println("Error retrieving all drivers: " + e.getMessage());
            throw e; 
        }
    }

    public void updateDriver(int id, int isBusy) throws SQLException {
        String sql = "UPDATE drivers SET isBusy = ? WHERE id = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setInt(1, isBusy);
            pstmt.setInt(2, id);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Error updating driver: " + e.getMessage());
            throw e; 
        }
    }

    public void deleteDriver(int id) throws SQLException {
        String sql = "DELETE FROM drivers WHERE id = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Error deleting driver: " + e.getMessage());
            throw e; 
        }
    }
}
