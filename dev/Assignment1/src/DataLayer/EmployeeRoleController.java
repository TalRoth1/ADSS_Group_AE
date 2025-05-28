package DataLayer;

import DomainLayer.Role;
import java.sql.SQLException;
import java.util.List;
import java.sql.Connection;


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

    public List<String> getRoles(int employeeId) {
        try {
            return employeeRoleDAO.getRoles(employeeId);
        } catch (SQLException e) {
            System.out.println("Error getting roles: " + e.getMessage());
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
