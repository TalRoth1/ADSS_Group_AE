package DataLayer.DAOs;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class ShipmentDAO {
    private Connection connection;

    public ShipmentDAO(Connection connection) {
        this.connection = connection;
        try {
            initializeTable();
        } catch (SQLException e) {
            System.out.println("Error initializing locations table: " + e.getMessage());
        }
    }

    public void initializeTable() throws SQLException {
        String sql = "CREATE TABLE IF NOT EXISTS shipments (" +
                "id INT PRIMARY KEY, " +
                "dateCreated TEXT, " +
                "dateSent TEXT, " +
                "truck_id INT, " +
                "driver_id INT, " +
                "doc_id INT, " +
                "status TEXT NOT NULL, " +
                "shiftType TEXT NOT NULL, " +
                "FOREIGN KEY (truck_id) REFERENCES trucks(id) ON DELETE CASCADE, " +
                "FOREIGN KEY (driver_id) REFERENCES drivers(id) ON DELETE CASCADE, " +
                "FOREIGN KEY (doc_id) REFERENCES documents(id) ON DELETE CASCADE" +
                ")";
        try (Statement stmt = connection.createStatement()) {
            stmt.execute("PRAGMA foreign_keys = ON"); // Enables enforcement & cascading
            stmt.execute(sql);
        } catch (SQLException e) {
            System.out.println("Error initializing shipments table: " + e.getMessage());
            throw e;
        }
    }

    public void addShipment(int shipmentID, String dateCreated, String dateSent, int truckID, int driverID, int docID,
            String status, String shiftType) throws SQLException {
        String sql = "INSERT INTO shipments (id, dateCreated, dateSent, truck_id, driver_id, doc_id, status, shiftType) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setInt(1, shipmentID);
            pstmt.setString(2, dateCreated);
            pstmt.setString(3, dateSent);
            pstmt.setInt(4, truckID);
            pstmt.setInt(5, driverID);
            pstmt.setInt(6, docID);
            pstmt.setString(7, status);
            pstmt.setString(8, shiftType);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Error adding shipment: " + e.getMessage());
            throw e;
        }
    }

    public ResultSet getShipment(int shipmentID) throws SQLException {
        String sql = "SELECT * FROM shipments WHERE id = ?";
        PreparedStatement pstmt = connection.prepareStatement(sql);
        pstmt.setInt(1, shipmentID);
        return pstmt.executeQuery();
    }

    public ResultSet getAllShipments() throws SQLException {
        String sql = "SELECT * FROM shipments";
        Statement stmt = connection.createStatement();
        return stmt.executeQuery(sql);
    }

    public void updateShipment(int shipmentID, String dateCreated, String dateSent, int truckID, int driverID,
            int docID, String status, String shiftType) throws SQLException {
        String sql = "UPDATE shipments SET dateCreated = ?, dateSent = ?, truck_id = ?, driver_id = ?, doc_id = ?, status = ?, shiftType = ? WHERE id = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setString(1, dateCreated);
            pstmt.setString(2, dateSent);
            pstmt.setInt(3, truckID);
            pstmt.setInt(4, driverID);
            pstmt.setInt(5, docID);
            pstmt.setString(6, status);
            pstmt.setString(7, shiftType);
            pstmt.setInt(8, shipmentID);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Error updating shipment: " + e.getMessage());
            throw e;
        }
    }

    public void deleteShipment(int shipmentID) throws SQLException {
        String sql = "DELETE FROM shipments WHERE id = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setInt(1, shipmentID);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Error deleting shipment: " + e.getMessage());
            throw e;
        }
    }

    public void updateShipmentByField(int shipmentID, String fieldName, Object value) throws SQLException {
        String sql = "UPDATE shipments SET " + fieldName + " = ? WHERE id = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            if (value instanceof String) {
                pstmt.setString(1, (String) value);
            } else if (value instanceof Integer) {
                pstmt.setInt(1, (Integer) value);
            } else {
                throw new IllegalArgumentException("Unsupported value type: " + value.getClass().getName());
            }
            pstmt.setInt(2, shipmentID);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Error updating shipment field: " + e.getMessage());
            throw e;
        }
    }

    public void clearTable() throws SQLException {
        try (Statement stmt = connection.createStatement()) {
            stmt.executeUpdate("DELETE FROM shipments");
        } catch (SQLException e) {
            System.out.println("Error clearing shipments table: " + e.getMessage());
            throw e;
        }
    }

}