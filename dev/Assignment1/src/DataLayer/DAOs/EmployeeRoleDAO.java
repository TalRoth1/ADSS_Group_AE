package DataLayer.DAOs;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class EmployeeRoleDAO {

    private Connection connection;

    public EmployeeRoleDAO(Connection connection) {
        this.connection = connection;
        try {
            initializeTable();
        } catch (SQLException e) {
            System.out.println("Error initializing employee_roleDAO: " + e.getMessage());
        }
    }

    private void initializeTable() throws SQLException {
        String createTableSQL = "CREATE TABLE IF NOT EXISTS employee_role ("
                + "employeeId INT NOT NULL, "
                + "role TEXT NOT NULL, "
                + "PRIMARY KEY (employeeId, role), "
                + "FOREIGN KEY (employeeId) REFERENCES employees(id)"
                + ")";
        try (Statement stmt = connection.createStatement()) {
            stmt.execute(createTableSQL);
        } catch (SQLException e) {
            System.out.println("Error creating shifts table: " + e.getMessage());
            throw e;
        }
    }

    public void addRole(int employeeId, String role) throws SQLException {
        String sql = "INSERT INTO employee_role (employeeId, role) VALUES (?, ?)";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setInt(1, employeeId);
            pstmt.setString(2, role);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Error adding role: " + e.getMessage());
            throw e;
        }
    }

    public void removeRole(int employeeId, String role) throws SQLException {
        String sql = "DELETE FROM employee_role WHERE employeeId=? AND role=?";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setInt(1, employeeId);
            pstmt.setString(2, role);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Error removing role: " + e.getMessage());
            throw e;
        }
    }

    public ResultSet getAllRoles() throws SQLException {
        String sql = "SELECT * FROM employee_role";
        Statement stmt = connection.createStatement();
        return stmt.executeQuery(sql);
    }

    public ResultSet getRolesForEmployee(int employeeId) throws SQLException { // get roles of a specific employee
        String sql = "SELECT role FROM employee_role WHERE employeeId=?";
        List<String> roles = new ArrayList<>();
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setInt(1, employeeId);
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                roles.add(rs.getString("role"));
            }
            return rs;
        } catch (SQLException e) {
            System.out.println("Error getting roles: " + e.getMessage());
            throw e;
        }

    }

    public void updateRole(int employeeId, String oldRole, String newRole) throws SQLException {
        String sql = "UPDATE employee_role SET role=? WHERE employeeId=? AND role=?";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setString(1, newRole);
            pstmt.setInt(2, employeeId);
            pstmt.setString(3, oldRole);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Error updating role: " + e.getMessage());
            throw e;
        }

    }

    public void removeEmployeeRoles(int employeeId) throws SQLException {
        String sql = "DELETE FROM employee_role WHERE employeeId=?";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setInt(1, employeeId);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Error removing all roles for employee: " + e.getMessage());
            throw e;
        }
    }

    public void clearTable() throws SQLException {
        try (Statement stmt = connection.createStatement()) {
            stmt.executeUpdate("DELETE FROM employee_role");
        } catch (SQLException e) {
            System.out.println("Error clearing employee_role table: " + e.getMessage());
            throw e;
        }
    }

}
