package DataLayer.DAOs;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class ShiftReqRolesDAO {

    private Connection connection;

    public ShiftReqRolesDAO(Connection connection) {
        this.connection = connection;
        try {
            initializeTable();
        } catch (SQLException e) {
            System.out.println("Error initializing ShiftReqRolesDAO: " + e.getMessage());
        }
    }

    private void initializeTable() throws SQLException {
        String createTableSQL = "CREATE TABLE IF NOT EXISTS shift_req_roles ("
                + "shiftId INTEGER NOT NULL, "
                + "role TEXT NOT NULL, "
                + "amount INTEGER NOT NULL, "
                + "PRIMARY KEY (shiftId, role), "
                + "FOREIGN KEY (shiftId) REFERENCES shifts(id)"
                + ")";
        try (Statement stmt = connection.createStatement()) {
            stmt.execute(createTableSQL);
        } catch (SQLException e) {
            System.out.println("Error creating shift_req_roles table: " + e.getMessage());
            throw e;
        }
    }

    public void addRequiredRole(int shiftId, String role, int amount) throws SQLException {
        String sql = "INSERT INTO shift_req_roles (shiftId, role, amount) VALUES (?, ?, ?)";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setInt(1, shiftId);
            pstmt.setString(2, role);
            pstmt.setInt(3, amount);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Error adding required role: " + e.getMessage());
            throw e;
        }
    }

    public void removeRequiredRole(int shiftId, String role) throws SQLException {
        String sql = "DELETE FROM shift_req_roles WHERE shiftId=? AND role=?";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setInt(1, shiftId);
            pstmt.setString(2, role);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Error removing required role: " + e.getMessage());
            throw e;
        }
    }

    public void updateRequiredRoleAmount(int shiftId, String role, int newAmount) throws SQLException {
        String sql = "UPDATE shift_req_roles SET amount=? WHERE shiftId=? AND role=?";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setInt(1, newAmount);
            pstmt.setInt(2, shiftId);
            pstmt.setString(3, role);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Error updating required role amount: " + e.getMessage());
            throw e;
        }
    }

    public ResultSet getRequiredRole(int shiftId, String role) throws SQLException {
        String sql = "SELECT * FROM shift_req_roles WHERE shiftId=? AND role=?";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setInt(1, shiftId);
            pstmt.setString(2, role);
            return pstmt.executeQuery();
        } catch (SQLException e) {
            System.out.println("Error retrieving required role: " + e.getMessage());
            throw e;
        }
    }

    public ResultSet getRequiredRolesForShift(int shiftId) throws SQLException {
        String sql = "SELECT * FROM shift_req_roles WHERE shiftId=?";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setInt(1, shiftId);
            return pstmt.executeQuery();
        } catch (SQLException e) {
            System.out.println("Error retrieving required roles for shift: " + e.getMessage());
            throw e;
        }
    }

    public void clearTable() throws SQLException {
        String sql = "DELETE FROM shift_req_roles";
        try (Statement stmt = connection.createStatement()) {
            stmt.executeUpdate(sql);
        } catch (SQLException e) {
            System.out.println("Error clearing shift_req_roles table: " + e.getMessage());
            throw e;
        }
    }
}
