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
                "id INTEGER PRIMARY KEY, " + // represents truck num
                "license_plate TEXT NOT NULL, " + // model
                "type TEXT NOT NULL, " +
                "status INTEGER NOT NULL, " + // BOOLEAN as INTEGER: 0 = false, 1 = true; represents isBusy
                "max_weight FLOAT NOT NULL" +
                ")";
        try (Statement stmt = connection.createStatement()) {
            stmt.execute(sql);
        } catch (SQLException e) {
            System.out.println("Error initializing trucks table: " + e.getMessage());
            throw e;
        }
    }

    public void addTruck(int id, String licensePlate, float max_weight, int status, String type) throws SQLException {
        String sql = "INSERT INTO trucks (id, license_plate, max_weight, status, type) VALUES (?, ?, ?, ?, ?)";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            pstmt.setString(2, licensePlate);
            pstmt.setFloat(3, max_weight);
            pstmt.setInt(4, status);
            pstmt.setString(5, type);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Error adding truck: " + e.getMessage());
            throw e;
        }
    }

    public void updateTruck(int id, String licensePlate, float max_weight, int status, String type)
            throws SQLException {
        String sql = "UPDATE trucks SET license_plate = ?, max_weight = ?, status = ?, type = ? WHERE id = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setString(1, licensePlate);
            pstmt.setFloat(2, max_weight);
            pstmt.setInt(3, status);
            pstmt.setString(4, type);
            pstmt.setInt(5, id);
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
        PreparedStatement pstmt = connection.prepareStatement(sql);
        pstmt.setInt(1, id);
        return pstmt.executeQuery();

    }

    public ResultSet getAllTrucks() throws SQLException {
        String sql = "SELECT * FROM trucks";
        Statement stmt = connection.createStatement();
        return stmt.executeQuery(sql);
    }

    public void clearTable() throws SQLException {
        String sql = "DELETE FROM trucks";
        try (Statement stmt = connection.createStatement()) {
            stmt.executeUpdate(sql);
        } catch (SQLException e) {
            System.out.println("Error clearing trucks table: " + e.getMessage());
            throw e;
        }
    }

}
