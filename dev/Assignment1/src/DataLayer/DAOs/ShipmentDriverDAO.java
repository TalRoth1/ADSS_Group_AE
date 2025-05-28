package DataLayer.DAOs;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;

public class ShipmentDriverDAO {
    private Connection connection;

    public ShipmentDriverDAO(Connection connection) {
        this.connection = connection;
        try {
            initializeTable();
        } catch (SQLException e) {
            System.out.println("Error initializing ShipmentDrivers table: " + e.getMessage());
            throw new RuntimeException("Failed to initialize ShipmentDrivers table", e);
        }
    }

    public void initializeTable() throws SQLException {
        String sql = "CREATE TABLE IF NOT EXISTS ShipmentDrivers ("
                + "id INT FOREIGN KEY REFERENCES Shipment(id), "
                + "id INT FOREIGN KEY REFERENCES Drivers(id), ";
        try (Statement stmt = connection.createStatement()) {
            stmt.execute(sql);
        } catch (SQLException e) {
            System.out.println("Error initializing trucks table: " + e.getMessage());
            throw e;
        }
    }

    public void createShipmentDriver(int shipmentId, int driverId) throws SQLException {
        String sql = "INSERT INTO ShipmentDrivers (shipmentId, driverId) VALUES (?, ?)";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setInt(1, shipmentId);
            pstmt.setInt(2, driverId);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Error adding truck: " + e.getMessage());
            throw e;
        }
    }

    public void updateShipmentDriver(int idShipment, int idnewDriver) throws SQLException {
        String sql = "UPDATE ShipmentDrivers SET driverId = ? WHERE shipmentId = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setInt(1, idnewDriver);
            pstmt.setInt(2, idShipment);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Error updating ShipmentDriver: " + e.getMessage());
            throw e;
        }

    }

    public void deleteShipmentDriver(int idSShipment) throws SQLException {
        String sql = "DELETE FROM ShipmentDrivers WHERE shipmentId = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setInt(1, idSShipment);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Error deleting ShipmentDriver: " + e.getMessage());
            throw e;
        }
    }

    public void getShipmentid(int idDriver) throws SQLException {
        String sql = "SELECT * FROM ShipmentDrivers WHERE driverId = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setInt(1, idDriver);
            pstmt.executeQuery();
            // Handle the result set as needed
        } catch (SQLException e) {
            System.out.println("Error retrieving ShipmentDriver by driverId: " + e.getMessage());
            throw e;
        }
    }

    public void getDriverid(int idShipement) throws SQLException {
        String sql = "SELECT * FROM ShipmentDrivers WHERE ShipmentId = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setInt(1, idShipement);
            pstmt.executeQuery();
            // Handle the result set as needed
        } catch (SQLException e) {
            System.out.println("Error retrieving ShipmentDriver by ShipmentId: " + e.getMessage());
            throw e;
        }
    }
}
