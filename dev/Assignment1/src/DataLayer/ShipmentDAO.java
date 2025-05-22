package DataLayer;
import java.sql.*;
import DomainLayer.ShipmentDL;
import java.util.ArrayList;
import java.util.List;


public class ShipmentDAO {
    private Connection connection;

    public ShipmentDAO(Connection connection) {
        this.connection = connection;
    }

    public void addShipment(ShipmentDL shipment) throws SQLException {
        String sql = "INSERT INTO shipments (truckNumber, driver_id, origin_id, destination_id, items) VALUES (?, ?, ?, ?, ?)";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setInt(1, shipment.getTruckNumber()); 
            pstmt.setInt(2, shipment.getDriverId());
            pstmt.setInt(3, shipment.getOrigin().getId());
            pstmt.setInt(4, shipment.getDestinations().get(0).getId()); // placeholder - check how to do
            pstmt.setString(5, shipment.getItems().toString()); // place holder - check how to do
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Error adding shipment: " + e.getMessage());
            throw e; 
        }
    }

    
        public void updateShipment(ShipmentDL shipment) throws SQLException {
            String sql = "UPDATE shipments SET truck_id = ?, driver_id = ?, origin_id = ?, destination_id = ?, items = ? WHERE id = ?";
            try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
                pstmt.setInt(1, shipment.getTruck().getId());
                pstmt.setInt(2, shipment.getDriver().getId());
                pstmt.setInt(3, shipment.getOrigin().getId());
                pstmt.setInt(4, shipment.getDestinations().get(0).getId()); // Assuming only one destination for simplicity
                pstmt.setString(5, shipment.getItems().toString()); // Convert items to string representation
                pstmt.setInt(6, shipment.getId()); // Assuming ShipmentDL has a getId() method
                pstmt.executeUpdate();
            } catch (SQLException e) {
                System.out.println("Error updating shipment: " + e.getMessage());
                throw e; 
            }
        }

    public void deleteShipment(ShipmentDL shipment) throws SQLException {
        String sql = "DELETE FROM shipments WHERE id = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setInt(1, shipment.getId()); // Assuming ShipmentDL has a getId() method
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Error deleting shipment: " + e.getMessage());
            throw e; 
        }
    }

    public ShipmentDL getShipment(int id) throws SQLException {
        String sql = "SELECT * FROM shipments WHERE id = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return new ShipmentDL(rs.getInt("id"), rs.getInt("truck_id"), rs.getInt("driver_id"), rs.getInt("origin_id"), rs.getInt("destination_id"), rs.getString("items"));
            } else {
                return null; // No shipment found with the given ID
            }
        } catch (SQLException e) {
            System.out.println("Error retrieving shipment: " + e.getMessage());
            throw e; 
        }
    }

    public ArrayList<ShipmentDL> getAllShipments() throws SQLException {
        String sql = "SELECT * FROM shipments";
        Statement stmt = connection.createStatement();
        ResultSet rs = stmt.executeQuery(sql);
        ArrayList<ShipmentDL> shipments = new ArrayList<>();
        while (rs.next()) {
            shipments.add(new ShipmentDL(rs.getInt("id"), rs.getInt("truck_id"), rs.getInt("driver_id"), rs.getInt("origin_id"), rs.getInt("destination_id"), rs.getString("items")));
        }
        return shipments;
    }
}