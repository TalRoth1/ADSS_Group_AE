package DataLayer.DAOs;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class ShiftDAO {

    private Connection connection;

    public ShiftDAO(Connection connection) {
        this.connection = connection;
        try {
            initializeTable();
        } catch (SQLException e) {
            System.out.println("Error initializing shifts table: " + e.getMessage());
        }
    }

    private void initializeTable() throws SQLException {
        String createTableSQL = "CREATE TABLE IF NOT EXISTS shifts (" +
                "id INT NOT NULL, " +
                "date TEXT NOT NULL, " +
                "shiftType TEXT NOT NULL, " +
                "startTime INT NOT NULL, " +
                "endTime INT NOT NULL, " +
                "shiftManagerId INT NOT NULL, " +
                "isShipment INT NOT NULL, " + // 0 = false, 1 = true
                "locationId INT NOT NULL, " +
                "PRIMARY KEY (id), " +
                "FOREIGN KEY (shiftManagerId) REFERENCES employees(id) ON DELETE CASCADE, " +
                "FOREIGN KEY (locationId) REFERENCES locations(id) ON DELETE CASCADE" +
                ")";
        try (Statement stmt = connection.createStatement()) {
            stmt.execute("PRAGMA foreign_keys = ON");
            stmt.execute(createTableSQL);
        } catch (SQLException e) {
            System.out.println("Error creating shifts table: " + e.getMessage());
            throw e;
        }
    }

    public void addShift(int id, String date, String shiftType, int locationId, int startTime, int endTime,
            int shiftManagerId, int isShipment)
            throws SQLException {
        String sql = "INSERT INTO shifts (id, date, shiftType, startTime, endTime, shiftManagerId, isShipment, locationId) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            pstmt.setString(2, date);
            pstmt.setString(3, shiftType);
            pstmt.setInt(4, startTime);
            pstmt.setInt(5, endTime);
            pstmt.setInt(6, shiftManagerId);
            pstmt.setInt(7, isShipment);
            pstmt.setInt(8, locationId);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Error adding shift: " + e.getMessage());
            throw e;
        }
    }

    public void deleteShift(String date, String shiftType, int locationId) throws SQLException {
        String sql = "DELETE FROM shifts WHERE date = ? AND shiftType = ? AND locationId = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setString(1, date);
            pstmt.setString(2, shiftType);
            pstmt.setInt(3, locationId);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Error deleting shift: " + e.getMessage());
            throw e;
        }
    }

    public void deleteShift(int id) throws SQLException {
        String sql = "DELETE FROM shifts WHERE id = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Error deleting shift by ID: " + e.getMessage());
            throw e;
        }
    }

    public ResultSet getShift(String date, String shiftType, int locationId) throws SQLException {
        String sql = "SELECT * FROM shifts WHERE date = ? AND shiftType = ? AND locationId = ?";
        PreparedStatement pstmt = connection.prepareStatement(sql);
        pstmt.setString(1, date);
        pstmt.setString(2, shiftType);
        pstmt.setInt(3, locationId);
        return pstmt.executeQuery();

    }

    public ResultSet getShift(int id) throws SQLException {
        String sql = "SELECT * FROM shifts WHERE id = ?";
        PreparedStatement pstmt = connection.prepareStatement(sql);
        pstmt.setInt(1, id);
        return pstmt.executeQuery();
    }

    public ResultSet getAllShifts() throws SQLException {
        String sql = "SELECT * FROM shifts";
        Statement stmt = connection.createStatement();
        return stmt.executeQuery(sql);
    }

    public ResultSet getRole(String date, String shiftType, int employeeId) throws SQLException {
        String sql = "SELECT role FROM shift_assigned WHERE date = ? AND shiftType = ? AND employeeId = ?";
        PreparedStatement pstmt = connection.prepareStatement(sql);
        pstmt.setString(1, date);
        pstmt.setString(2, shiftType);
        pstmt.setInt(3, employeeId);
        return pstmt.executeQuery(sql);

    }

    public ResultSet getShiftField(int id, String fieldName) throws SQLException {
        String sql = "SELECT " + fieldName + " FROM shifts WHERE id = ?";
        PreparedStatement pstmt = connection.prepareStatement(sql);
        pstmt.setInt(1, id);
        return pstmt.executeQuery();

    }

    // erez change the type of newValue to int because it is used to update an
    // integer field
    public void setShiftField(int id, String fieldName, int newValue) throws SQLException {
        String sql = "UPDATE shifts SET " + fieldName + " = ? WHERE id = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setInt(1, newValue);
            pstmt.setInt(2, id);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Error updating shift field: " + e.getMessage());
            throw e;
        }
    }

    public void setShiftField(int id, String fieldName, String newValue) throws SQLException {
        String sql = "UPDATE shifts SET " + fieldName + " = ? WHERE id = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setString(1, newValue);
            pstmt.setInt(2, id);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Error updating shift field: " + e.getMessage());
            throw e;
        }
    }

    public void clearTable() throws SQLException {
        String sql = "DELETE FROM shifts";
        try (Statement stmt = connection.createStatement()) {
            stmt.executeUpdate(sql);
        } catch (SQLException e) {
            System.out.println("Error clearing shifts table: " + e.getMessage());
            throw e;
        }
    }
}
