package DataLayer;
import DomainLayer.DriverDL;
import java.sql.*;
import java.util.ArrayList;

public class DriverDAO {

    Connection connection;

    public DriverDAO(Connection connection) {
        this.connection = connection;
    }

    public void addDriver(int id, String LicenseType) throws SQLException {
        String sql = "INSERT INTO drivers (id, LicenseType) VALUES (?, ?)";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            pstmt.setString(2, LicenseType);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Error adding driver: " + e.getMessage());
            throw e; 
        }
    }

    public void updateDriver(int id, String licenseType) throws SQLException {
        String sql = "UPDATE drivers SET licenseType = ? WHERE id = ?";
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
        String sql = "DELETE FROM drivers WHERE id = ? AND LicenseType = ?";
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
        String sql = "SELECT * FROM drivers WHERE id = ?";
        ArrayList<DriverDL> drivers = new ArrayList<>();
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            return pstmt.executeQuery();
        } catch (SQLException e) {
            System.out.println("Error getting driver: " + e.getMessage());
            throw e; 
        }
    }

    public ResultSet getAllDrivers() throws SQLException {
        String sql = "SELECT * FROM drivers";
        ArrayList<DriverDL> drivers = new ArrayList<>();
        try (Statement stmt = connection.createStatement()) {
            return stmt.executeQuery(sql);
        } catch (SQLException e) {
            System.out.println("Error getting all drivers: " + e.getMessage());
            throw e; 
        }
    }
}
