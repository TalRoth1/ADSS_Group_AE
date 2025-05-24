package DataLayer;
import java.sql.*;

public class LocationDAO {
    private Connection connection;


    public LocationDAO(Connection connection) {
        this.connection = connection;

    }

    public void addLocation(int id , String street, int streetNumber, String city, String contactNumber, String contactName, String zone) throws SQLException {
        String sql = "INSERT INTO locations (id, street, street_number, city, contact_number, contact_name, zone) VALUES (?, ?, ?, ?, ?, ?)";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            pstmt.setString(2, street);
            pstmt.setInt(3, streetNumber);
            pstmt.setString(4, city);
            pstmt.setString(5, contactNumber);
            pstmt.setString(6, contactName);
            pstmt.setString(7, zone);
            pstmt.executeUpdate();
        }
        catch (SQLException e) {
            System.out.println("Error adding location: " + e.getMessage());
            throw e; 
        }
    }
 
    public void updateLocation(String street, int streetNumber, String city, String contactNumber, String contactName, String zone) throws SQLException {
        String sql = "UPDATE locations SET street = ?, street_number = ?, city = ?, contact_number = ?, contact_name = ?, zone = ? WHERE id = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setString(1, street);
            pstmt.setInt(2, streetNumber);
            pstmt.setString(3, city);
            pstmt.setString(4, contactNumber);
            pstmt.setString(5, contactName);
            pstmt.setString(6, zone);
            pstmt.executeUpdate();
        }
        catch (SQLException e) {
            System.out.println("Error updating location: " + e.getMessage());
            throw e; 
        }
    }
        

    public void updateLocationField(int id, String fieldName, String newValue) throws SQLException {
        String sql = "UPDATE locations SET " + fieldName + " = ? WHERE id = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setString(1, newValue);
            pstmt.setInt(2, id);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Error updating location field: " + e.getMessage());
            throw e; 
        }
    }

    public void updateLocationField(int id, String fieldName, int newValue) throws SQLException {
        String sql = "UPDATE locations SET " + fieldName + " = ? WHERE id = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setInt(1, newValue);
            pstmt.setInt(2, id);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Error updating location field: " + e.getMessage());
            throw e; 
        }
    }

    public void deleteLocation(String street, int streetNumber, String city) throws SQLException {
        String sql = "DELETE FROM locations WHERE street = ? AND street_number = ? AND city = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setString(1, street);
            pstmt.setInt(2, streetNumber);
            pstmt.setString(3, city);
            pstmt.executeUpdate();
        }
        catch (SQLException e) {
            System.out.println("Error deleting location: " + e.getMessage());
            throw e; 
        }
    }
    public void deleteLocation(int id) throws SQLException {
        String sql = "DELETE FROM locations WHERE id = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Error deleting location: " + e.getMessage());
            throw e; 
        }
    }
    public ResultSet getLocation(String street, int streetNumber, String city) throws SQLException {
        String sql = "SELECT * FROM locations WHERE street = ? AND street_number = ? AND city = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setString(1, street);
            pstmt.setInt(2, streetNumber);
            pstmt.setString(3, city);
            return pstmt.executeQuery(); 
        } catch (SQLException e) {
            System.out.println("Error retrieving location: " + e.getMessage());
            throw e;
        }
    }

    public ResultSet getLocation(int id) throws SQLException {
        String sql = "SELECT * FROM locations WHERE id = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            return pstmt.executeQuery();
        } catch (SQLException e) {
            System.out.println("Error retrieving location: " + e.getMessage());
            throw e; 
        }
    }

        

    //also return id for each location?
    public ResultSet getAllLocations() throws SQLException {
        String sql = "SELECT * FROM locations";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            return pstmt.executeQuery();
        } catch (SQLException e) {
            System.out.println("Error retrieving all locations: " + e.getMessage());
            throw e; 
        }
        
    }
}