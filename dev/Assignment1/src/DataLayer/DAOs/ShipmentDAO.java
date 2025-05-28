package DataLayer.DAOs;

import java.sql.*;
import DomainLayer.ShipmentDL;
import java.util.ArrayList;
import java.util.List;
import DTO.ShipmentDTO;

public class ShipmentDAO {
    private Connection connection;

    public ShipmentDAO(Connection connection) {
        this.connection = connection;
    }

    public void addShipment(ShipmentDTO shipment) throws SQLException {
        String sql = "INSERT INTO shipments (truck_id, driver_id, doc_id, status) VALUES (?, ?, ?, ?)";
        try (PreparedStatement pstmt = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            pstmt.setInt(1, shipment.getTruck().getId());
            pstmt.setInt(2, shipment.getDriver().getId());
            pstmt.setInt(3, shipment.getDocument().getId());
            pstmt.setString(4, shipment.getStatus().toString());
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Error adding shipment: " + e.getMessage());
            throw e;
        }
    }

    public void updateShipment(ShipmentDL shipment) throws SQLException {
        String sql = "UPDATE shipments SET truck_id =?, driver_id = ?, doc_id = ?, status = ? WHERE id = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setInt(1, shipment.getTruck().getId());
            pstmt.setInt(2, shipment.getDriver().getId());
            pstmt.setInt(3, shipment.getDocument().getId());
            pstmt.setString(4, shipment.getStatus().toString());
            pstmt.setInt(5, shipment.getId()); // Assuming ShipmentDL has a getId() method
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

    public ShipmentDTO getShipment(int id) throws SQLException {
        String sql = "SELECT * FROM shipments WHERE id = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return new ShipmentDTO(rs.getInt("id"), rs.getInt("truck_id"), rs.getInt("driver_id"),
                        rs.getInt("doc_id"), rs.getString("status")); // fix shipmentDTO
            } else {
                return null; // Shipment not found
            }
        } catch (SQLException e) {
            System.out.println("Error retrieving shipment: " + e.getMessage());
            throw e;
        }
    }

    public ArrayList<ShipmentDTO> getAllShipments() throws SQLException {
        String sql = "SELECT * FROM shipments";
        ArrayList<ShipmentDTO> shipments = new ArrayList<>();
        try (PreparedStatement pstmt = connection.prepareStatement(sql);
                ResultSet rs = pstmt.executeQuery()) {
            while (rs.next()) {
                ShipmentDTO shipment = new ShipmentDTO(rs.getInt("id"), rs.getInt("truck_id"),
                        rs.getInt("driver_id"), rs.getInt("doc_id"), rs.getString("status")); // fix shipmentDTO
                shipments.add(shipment);
            }
        } catch (SQLException e) {
            System.out.println("Error retrieving all shipments: " + e.getMessage());
            throw e;
        }
        return shipments;
    }
}