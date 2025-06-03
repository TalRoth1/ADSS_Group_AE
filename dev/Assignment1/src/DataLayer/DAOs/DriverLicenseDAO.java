package DataLayer.DAOs;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import DomainLayer.DriverDL;

public class DriverLicenseDAO {

    Connection connection;

    public DriverLicenseDAO(Connection connection) {
        this.connection = connection;
        try {
            InitializeDatabase();
        } catch (SQLException e) {
            System.out.println("Error initializing database: " + e.getMessage());
        }
    }

    public void InitializeDatabase() throws SQLException {
        String sql = "CREATE TABLE IF NOT EXISTS driver_license ("
                + "id INTEGER ,"
                + "LicenseType TEXT, "
                + "PRIMARY KEY (id, LicenseType) "
                + ")";
        try (Statement stmt = connection.createStatement()) {
            stmt.execute(sql);
        } catch (SQLException e) {
            System.out.println("Error initializing database: " + e.getMessage());
            throw e; 
        }
    }

    public void addDriver(int id, String LicenseType) throws SQLException {
        String sql = "INSERT INTO driver_license (id, LicenseType) VALUES (?, ?)";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            pstmt.setString(2, LicenseType);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Error adding driver: " + e.getMessage());
            throw e;
        }
    }

    //dont use this method, it is not needed
    private void updateDriver(int id, String licenseType) throws SQLException {
        String sql = "UPDATE driver_license SET licenseType = ? WHERE id = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setString(1, licenseType);
            pstmt.setInt(2, id);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Error updating driver: " + e.getMessage());
            throw e;
        }
    }

    public void deleteDriver(int id, String LicenseType) throws SQLException {
        String sql = "DELETE FROM driver_license WHERE id = ? AND LicenseType = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            pstmt.setString(2, LicenseType);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Error deleting driver: " + e.getMessage());
            throw e;
        }
    }

    public ResultSet getDriver(int id) throws SQLException {
        String sql = "SELECT * FROM driver_license WHERE id = ?";
        ArrayList<DriverDL> drivers = new ArrayList<>();
        PreparedStatement pstmt = connection.prepareStatement(sql);
        pstmt.setInt(1, id);
        return pstmt.executeQuery();
    }

    public ResultSet getAllDrivers() throws SQLException {
        String sql = "SELECT * FROM driver_license";
        ArrayList<DriverDL> drivers = new ArrayList<>();
        Statement stmt = connection.createStatement();
        return stmt.executeQuery(sql);
    }

    public void deleteAllLicenses(int id) throws SQLException {
        String sql = "DELETE FROM driver_license WHERE id = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Error deleting all licenses for driver: " + e.getMessage());
            throw e;
        }
    }

    public void clearDriverLicenses() throws SQLException {
        String sql = "DELETE FROM driver_license";
        try (Statement stmt = connection.createStatement()) {
            stmt.executeUpdate(sql);
        } catch (SQLException e) {
            System.out.println("Error clearing driver licenses: " + e.getMessage());
            throw e;
        }
    }
    
}
