package DataLayer.DAOs;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import DomainLayer.Role;

public class EmployeeRoleDAO {

    private Connection connection;

    public EmployeeRoleDAO(Connection connection) {
        this.connection = connection;
    }

    private void initializeTable() throws SQLException {
        String createTableSQL = "CREATE TABLE IF NOT EXISTS employee_roles (" +
            "employeeId INT NOT NULL, " +
            "role TEXT NOT NULL, " +
            "PRIMARY KEY (employeeId, role), " +
            "FOREIGN KEY (employeeId) REFERENCES employees(id)" +
            ")";
        try (Statement stmt = connection.createStatement()) {
            stmt.execute(createTableSQL);
        } catch (SQLException e) {
            System.out.println("Error creating shifts table: " + e.getMessage());
            throw e;
        }
    }


    public void addRole(int employeeId, Role role) throws SQLException {
        String sql = "INSERT INTO employee_roles (employeeId, role) VALUES (?, ?)";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setInt(1, employeeId);
            pstmt.setString(2, role.name()); // Store enum as String
            pstmt.executeUpdate();
        }
    }

    public void removeRole(int employeeId, Role role) throws SQLException {
        String sql = "DELETE FROM employee_roles WHERE employeeId=? AND role=?";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setInt(1, employeeId);
            pstmt.setString(2, role.name());
            pstmt.executeUpdate();
        }
    }

    public List<String> getRoles(int employeeId) throws SQLException {
        String sql = "SELECT role FROM employee_roles WHERE employeeId=?";
        List<String> roles = new ArrayList<>();
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setInt(1, employeeId);
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                roles.add(rs.getString("role"));

            }
            return roles;
        }
    }
    //

    public void updateRole(int employeeId, Role oldRole, Role newRole) throws SQLException {
        String sql = "UPDATE employee_roles SET role=? WHERE employeeId=? AND role=?";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setString(1, newRole.name());
            pstmt.setInt(2, employeeId);
            pstmt.setString(3, oldRole.name());
            pstmt.executeUpdate();
        }

    }
}
