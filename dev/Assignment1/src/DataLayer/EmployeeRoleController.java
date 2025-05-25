package DataLayer;

import DomainLayer.Role;
import java.sql.SQLException;
import java.util.List;

public class EmployeeRoleController {

    private EmployeeRoleDAO employeeRoleDAO;

    public EmployeeRoleController(EmployeeRoleDAO employeeRoleDAO) {
        this.employeeRoleDAO = employeeRoleDAO;
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

    public List<Role> getRoles(int employeeId) {
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
