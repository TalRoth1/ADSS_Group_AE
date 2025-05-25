package DataLayer;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
// Import Role if it's in another package, or define it here if missing
import DomainLayer.Role;

public class EmployeeRoleDAO {

    private Connection connection;

    public EmployeeRoleDAO(Connection connection) {
        this.connection = connection;
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

    public List<Role> getRoles(int employeeId) throws SQLException {
        String sql = "SELECT role FROM employee_roles WHERE employeeId=?";
        List<Role> roles = new ArrayList<>();
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setInt(1, employeeId);
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                roles.add(Role.valueOf(rs.getString("role"))); // Convert String to Enum
            }
        }
        return roles;
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
