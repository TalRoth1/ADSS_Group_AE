package DataLayer.DAOs;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class ShiftPreferredDAO {

    private Connection connection;

    public ShiftPreferredDAO(Connection connection) {
        this.connection = connection;
        try {
            initializeTable();
        } catch (SQLException e) {
            System.out.println("Error initializing ShiftPreferredDAO: " + e.getMessage());
        }
    }

    private void initializeTable() throws SQLException {
        String createTableSQL = "CREATE TABLE IF NOT EXISTS preferred_shifts ("
                + "shiftId INTEGER , "
                + "employeeId INT , "
                + "role TEXT NOT NULL, "
                + "PRIMARY KEY (shiftId, employeeId), "
                + "FOREIGN KEY (employeeId) REFERENCES employees(id), "
                + "FOREIGN KEY (shiftId) REFERENCES shifts(id)"
                + ")";
        try (Statement stmt = connection.createStatement()) {
            stmt.execute(createTableSQL);
        } catch (SQLException e) {
            System.out.println("Error creating preferred_shifts table: " + e.getMessage());
            throw e;
        }
    }

    public void addPreferredShift(int shiftId, int employeeId, String role) throws SQLException {
        String sql = "INSERT INTO preferred_shifts (shiftId, employeeId, role) VALUES (?, ?, ?)";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setInt(1, shiftId);
            pstmt.setInt(2, employeeId);
            pstmt.setString(3, role);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Error adding preferred shift: " + e.getMessage());
            throw e;
        }
    }

    public void removePreferredShift(int shiftId, int employeeId) throws SQLException {
        String sql = "DELETE FROM preferred_shifts WHERE shiftId=? AND employeeId=?";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setInt(1, shiftId);
            pstmt.setInt(2, employeeId);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Error removing preferred shift: " + e.getMessage());
            throw e;
        }
    }

    public void updatePreferredShiftRole(int shiftId, int employeeId, String newRole) throws SQLException {
        String sql = "UPDATE preferred_shifts SET role=? WHERE shiftId=? AND employeeId=?";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setString(1, newRole);
            pstmt.setInt(2, shiftId);
            pstmt.setInt(3, employeeId);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Error updating preferred shift role: " + e.getMessage());
            throw e;
        }
    }

    public ResultSet getPreferredShift(int shiftId, int employeeId) throws SQLException {
        String sql = "SELECT * FROM preferred_shifts WHERE shiftId=? AND employeeId=?";
        PreparedStatement pstmt = connection.prepareStatement(sql);
        pstmt.setInt(1, shiftId);
        pstmt.setInt(2, employeeId);
        return pstmt.executeQuery();
        
    }

    public ResultSet getPreferredShiftsForEmployee(int employeeId) throws SQLException {
        String sql = "SELECT * FROM preferred_shifts WHERE employeeId=?";
        PreparedStatement pstmt = connection.prepareStatement(sql);
        pstmt.setInt(1, employeeId);
        return pstmt.executeQuery();
        
    }

    public ResultSet getPreferredShiftsForShift(int shiftId) throws SQLException {
        String sql = "SELECT * FROM preferred_shifts WHERE shiftId=?";
        PreparedStatement pstmt = connection.prepareStatement(sql);
        pstmt.setInt(1, shiftId);
        return pstmt.executeQuery();
        
    }

    public void clearTable() throws SQLException {
        String sql = "DELETE FROM preferred_shifts";
        try (Statement stmt = connection.createStatement()) {
            stmt.executeUpdate(sql);
        } catch (SQLException e) {
            System.out.println("Error clearing preferred_shifts table: " + e.getMessage());
            throw e;
        }
    }
}
