package DataLayer.DAOs;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class ShiftAssignedDAO {

    private Connection connection;

    public ShiftAssignedDAO(Connection connection) {
        this.connection = connection;
        try {
            initializeTable();
        } catch (SQLException e) {
            System.out.println("Error initializing ShiftAssignedDAO: " + e.getMessage());
        }
    }

    private void initializeTable() throws SQLException {
        String createTableSQL = "CREATE TABLE IF NOT EXISTS shift_assigned (" +
                "shiftId INTEGER NOT NULL, " +
                "employeeId INT NOT NULL, " +
                "role TEXT NOT NULL, " +
                "PRIMARY KEY (shiftId, employeeId), " +
                "FOREIGN KEY (shiftId) REFERENCES shifts(id) ON DELETE CASCADE, " +
                "FOREIGN KEY (employeeId) REFERENCES employees(id) ON DELETE CASCADE" +
                ")";
        try (Statement stmt = connection.createStatement()) {
            stmt.execute("PRAGMA foreign_keys = ON");
            stmt.execute(createTableSQL);
        } catch (SQLException e) {
            System.out.println("Error creating shift_assigned table: " + e.getMessage());
            throw e;
        }
    }

    public void addAssignedShift(int shiftId, int employeeId, String role) throws SQLException {
        String sql = "INSERT INTO shift_assigned (shiftId, employeeId, role) VALUES (?, ?, ?)";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setInt(1, shiftId);
            pstmt.setInt(2, employeeId);
            pstmt.setString(3, role);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Error adding assigned shift: " + e.getMessage());
            throw e;
        }
    }

    public void removeAssignedShift(int shiftId, int employeeId) throws SQLException {
        String sql = "DELETE FROM shift_assigned WHERE shiftId=? AND employeeId=?";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setInt(1, shiftId);
            pstmt.setInt(2, employeeId);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Error removing assigned shift: " + e.getMessage());
            throw e;
        }
    }

    public void updateAssignedShiftRole(int shiftId, int employeeId, String newRole) throws SQLException {
        String sql = "UPDATE shift_assigned SET role=? WHERE shiftId=? AND employeeId=?";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setString(1, newRole);
            pstmt.setInt(2, shiftId);
            pstmt.setInt(3, employeeId);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Error updating assigned shift role: " + e.getMessage());
            throw e;
        }
    }

    public ResultSet getAssignedShift(int shiftId, int employeeId) throws SQLException {
        String sql = "SELECT * FROM shift_assigned WHERE shiftId=? AND employeeId=?";
        PreparedStatement pstmt = connection.prepareStatement(sql);
        pstmt.setInt(1, shiftId);
        pstmt.setInt(2, employeeId);
        return pstmt.executeQuery();

    }

    public ResultSet getAssignedShifts(int employeeId) throws SQLException {
        String sql = "SELECT * FROM shift_assigned WHERE employeeId=?";
        PreparedStatement pstmt = connection.prepareStatement(sql);
        pstmt.setInt(1, employeeId);
        return pstmt.executeQuery();

    }

    // view all employees of a shift
    public ResultSet getShiftEmployees(int id) throws SQLException {
        String sql = "SELECT * FROM shift_assigned WHERE shiftId=?";
        PreparedStatement pstmt = connection.prepareStatement(sql);
        pstmt.setInt(1, id);
        return pstmt.executeQuery();

    }

    public String getRole(int shiftId, int employeeId) throws SQLException {
        String sql = "SELECT role FROM shift_assigned WHERE shiftId=? AND employeeId=?";
        PreparedStatement pstmt = connection.prepareStatement(sql);
        pstmt.setInt(1, shiftId);
        pstmt.setInt(2, employeeId);
        ResultSet rs = pstmt.executeQuery();
        if (rs.next()) {
            return rs.getString("role");
        } else {
            return null; // No role found
        }

    }

    public void clearTable() throws SQLException {
        String sql = "DELETE FROM shift_assigned";
        try (Statement stmt = connection.createStatement()) {
            stmt.executeUpdate(sql);
        } catch (SQLException e) {
            System.out.println("Error clearing shift_assigned table: " + e.getMessage());
            throw e;
        }
    }
}
