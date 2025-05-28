package DataLayer.DAOs;

import java.sql.*;

public class TruckDAO {
    Connection connection;

    public TruckDAO(Connection connection) {
        this.connection = connection;
        try {
            initializeTable();
        } catch (SQLException e) {
            System.out.println("Error initializing TruckDAO: " + e.getMessage());
        }
    }

    public void initializeTable() throws SQLException {
        String sql = "CREATE TABLE IF NOT EXISTS trucks (" +
                "id INTEGER PRIMARY KEY, " +
                "license_plate TEXT NOT NULL, " +
                "type TEXT NOT NULL, " +
                "status INTEGER NOT NULL, " + // BOOLEAN as INTEGER: 0 = false, 1 = true
                "weight FLOAT NOT NULL, " +
                "max_weight FLOAT NOT NULL" +
                ")";
        try (Statement stmt = connection.createStatement()) {
            stmt.execute(sql);
        } catch (SQLException e) {
            System.out.println("Error initializing trucks table: " + e.getMessage());
            throw e;
        }
    }

    public void addTruck(int id, String licensePlate, double weight) throws SQLException {
        String sql = "INSERT INTO trucks (id, license_plate, weight) VALUES (?, ?, ?)";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            pstmt.setString(2, licensePlate);
            pstmt.setDouble(3, weight);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Error adding truck: " + e.getMessage());
            throw e;
        }
    }

    public void updateTruck(int id, String licensePlate, float weight, float max_weight, int status, String type)
            throws SQLException {
        String sql = "UPDATE trucks SET license_plate = ?, weight = ?, max_weight = ?, status = ?, type = ? WHERE id = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setString(1, licensePlate);
            pstmt.setFloat(2, weight);
            pstmt.setFloat(3, max_weight);
            pstmt.setInt(4, status);
            pstmt.setString(5, type);
            pstmt.setInt(6, id);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Error updating truck: " + e.getMessage());
            throw e;
        }
    }

    public void updateTruckField(int id, String fieldName, String newValue) throws SQLException {
        String sql = "UPDATE trucks SET " + fieldName + " = ? WHERE id = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setString(1, newValue);
            pstmt.setInt(2, id);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Error updating truck field: " + e.getMessage());
            throw e;
        }
    }

    public void deleteTruck(int id) throws SQLException {
        String sql = "DELETE FROM trucks WHERE id = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Error deleting truck: " + e.getMessage());
            throw e;
        }
    }

    public ResultSet getTruck(int id) throws SQLException {
        String sql = "SELECT * FROM trucks WHERE id = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            return pstmt.executeQuery();
        } catch (SQLException e) {
            System.out.println("Error getting truck: " + e.getMessage());
            throw e;
        }
    }

    public ResultSet getAllTrucks() throws SQLException {
        String sql = "SELECT * FROM trucks";
        Statement stmt = connection.createStatement();
        return stmt.executeQuery(sql);
    }
}
