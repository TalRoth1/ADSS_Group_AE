package DataLayer.DAOs;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import DTO.PreferredShiftDTO;

public class PreferredShiftDAO {

    private Connection connection;

    public PreferredShiftDAO(Connection connection) {
        this.connection = connection;
    }

    private void initializeTable() throws SQLException {
        String createTableSQL = "CREATE TABLE IF NOT EXISTS preferred_shifts (" +
                "employeeId INT NOT NULL, " +
                "shiftDate DATE NOT NULL, " +
                "shiftType TEXT NOT NULL, " +
                "PRIMARY KEY (employeeId, shiftDate, shiftType), " +
                "FOREIGN KEY (employeeId) REFERENCES employees(id)" +
                ")";
        try (Statement stmt = connection.createStatement()) {
            stmt.execute(createTableSQL);
        } catch (SQLException e) {
            System.out.println("Error creating shifts table: " + e.getMessage());
            throw e;
        }
    }

    public void addPreferredShift(int employeeId, String shiftDate, String shiftType) throws SQLException {
        String sql = "INSERT INTO preferred_shifts (employeeId, shiftDate, shiftType) VALUES (?, ?, ?)";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setInt(1, employeeId);
            pstmt.setString(2, shiftDate);
            pstmt.setString(3, shiftType);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Error adding preferred shift: " + e.getMessage());
            throw e;
        }
    }

    public void removePreferredShift(int employeeId, String shiftDate, String shiftType) throws SQLException {
        String sql = "DELETE FROM preferred_shifts WHERE employeeId=? AND shiftDate=? AND shiftType=?";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setInt(1, employeeId);
            pstmt.setString(2, shiftDate);
            pstmt.setString(3, shiftType);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Error removing preferred shift: " + e.getMessage());
            throw e;
        }
    }

    // view all preferred shifts for an employee
    public ResultSet getPreferredShifts(int employeeId) throws SQLException {
        String sql = "SELECT * FROM preferred_shifts WHERE employeeId=?";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setInt(1, employeeId);
            return pstmt.executeQuery();
        } catch (SQLException e) {
            System.out.println("Error retrieving preferred shifts: " + e.getMessage());
            throw e;
        }
    }

    // view all employees that chose this preferred shift
    public ResultSet getPrefShiftEmployees(String shiftDate, String shiftType) throws SQLException {
        String sql = "SELECT * FROM preferred_shifts WHERE shiftDate=? AND shiftType=?";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setString(1, shiftDate);
            pstmt.setString(2, shiftType);
            return pstmt.executeQuery();
        } catch (SQLException e) {
            System.out.println("Error retrieving preferred shift employees: " + e.getMessage());
            throw e;
        }
    }

}
