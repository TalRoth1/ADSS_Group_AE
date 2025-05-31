package DataLayer;

import DomainLayer.Role;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import DTO.EmployeeRoleDTO;

import java.sql.Connection;
import java.sql.ResultSet;

import DataLayer.DAOs.EmployeeRoleDAO;

public class EmployeeRoleController {
    private DBConnection dbConnection = new DBConnection();
    private Connection connection;
    private EmployeeRoleDAO employeeRoleDAO;

    public EmployeeRoleController() {
        String DB_URL = "EmployeesRoles.db";
        DBConnection.connect(DB_URL);
        this.connection = DBConnection.getConnection();
        this.employeeRoleDAO = new EmployeeRoleDAO(connection);
    }

    public void addRole(int employeeId, Role role) {
        try {
            employeeRoleDAO.addRole(employeeId, role);
        } catch (SQLException e) {
            System.out.println("Error adding role: " + e.getMessage());
        }
    }

    public void removeRole(int employeeId, Role role) {
        try {
            employeeRoleDAO.removeRole(employeeId, role);
        } catch (SQLException e) {
            System.out.println("Error removing role: " + e.getMessage());
        }
    }

    public void removeAllRoles(int employeeId) {
        try {
            employeeRoleDAO.removeAllRoles(employeeId);
        } catch (SQLException e) {
            System.out.println("Error removing all roles: " + e.getMessage());
        }
    }

    public List<EmployeeRoleDTO> getAllRoles() {
        try {
            ResultSet rs = employeeRoleDAO.getAllRoles();
            List<EmployeeRoleDTO> roles = new ArrayList<>();
            while (rs.next()) {
                int employeeId = rs.getInt("employeeId");
                String role = rs.getString("role");
                roles.add(new EmployeeRoleDTO(employeeId, role));
            }
            return roles;
        } catch (SQLException e) {
            System.out.println("Error getting all roles: " + e.getMessage());
            return null;
        }
    }

    public List<String> getRoles(int employeeId) {
        try {
            ResultSet rs = employeeRoleDAO.getRolesForEmployee(employeeId);
            List<String> roles = new ArrayList<>();
            while (rs.next()) {
                roles.add(rs.getString("role"));
            }
            return roles;
        } catch (SQLException e) {
            System.out.println("Error getting roles: " + e.getMessage());
            return null;
        }
    }

    public List<String> getRolesForEmployee(int employeeId) {
        try {
            ResultSet rs = employeeRoleDAO.getRolesForEmployee(employeeId);
            List<String> roles = new ArrayList<>();
            while (rs.next()) {
                roles.add(rs.getString("role"));
            }
            return roles;
        } catch (SQLException e) {
            System.out.println("Error getting roles for employee: " + e.getMessage());
            return null;
        }
    }

    public void updateRole(int employeeId, Role oldRole, Role newRole) {
        try {
            employeeRoleDAO.updateRole(employeeId, oldRole, newRole);
        } catch (SQLException e) {
            System.out.println("Error updating role: " + e.getMessage());
        }
    }
}
