package DataLayer;
import java.sql.*;
import DomainLayer.DriverDL;
import java.util.ArrayList;

public class DriverDAO {

    Connection connection;

    public DriverDAO(Connection connection) {
        this.connection = connection;
    }

    public void addDriver(int id, String name, String contactNumber) throws SQLException {
        String sql = "INSERT INTO drivers (id, name, contact_number) VALUES (?, ?, ?)";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            pstmt.setString(2, name);
            pstmt.setString(3, contactNumber);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Error adding driver: " + e.getMessage());
            throw e; 
        }
    }

    public void updateDriver(int id, String name, String contactNumber) throws SQLException {
        String sql = "UPDATE drivers SET name = ?, contact_number = ? WHERE id = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setString(1, name);
            pstmt.setString(2, contactNumber);
            pstmt.setInt(3, id);
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

    public DriverDL getDriver(int id) throws SQLException {
        String sql = "SELECT * FROM drivers WHERE id = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return new DriverDL(rs.getInt("id"), rs.getString("name"), rs.getString("contact_number"));
            } else {
                return null;
            }
        } catch (SQLException e) {
            System.out.println("Error getting driver: " + e.getMessage());
            throw e; 
        }
    }

    public ArrayList<DriverDL> getAllDrivers() throws SQLException {
        String sql = "SELECT * FROM drivers";
        ArrayList<DriverDL> drivers = new ArrayList<>();
        try (Statement stmt = connection.createStatement()) {
            ResultSet rs = stmt.executeQuery(sql);
            while (rs.next()) {
                drivers.add(new DriverDL(rs.getInt("id"), rs.getString("name"), rs.getString("contact_number")));
            }
        } catch (SQLException e) {
            System.out.println("Error getting all drivers: " + e.getMessage());
            throw e; 
        }
        return drivers;
    }



}
