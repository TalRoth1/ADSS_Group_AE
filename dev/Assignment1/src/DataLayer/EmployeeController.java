package DataLayer;

import java.sql.ResultSet;
import java.sql.SQLException;

public class EmployeeController {

    private EmployeeDAO employeeDAO;

    public EmployeeController(EmployeeDAO employeeDAO) {
        this.employeeDAO = employeeDAO;
    }

    public void addEmployee(int id, String name, String branch, String bankAccount, int salary, String startDate,
            int vacationDays, int sickDays, double educationFund, double socialBenefits, String password) {
        try {
            employeeDAO.addEmployee(id, name, branch, bankAccount, salary, startDate, vacationDays, sickDays, educationFund, socialBenefits, password);
        } catch (SQLException e) {
            System.out.println("Error adding employee: " + e.getMessage());
        }
    }

    public void removeEmployee(int id) {
        try {
            employeeDAO.removeEmployee(id);
        } catch (SQLException e) {
            System.out.println("Error removing employee: " + e.getMessage());
        }
    }

    public void fireEmployee(int employeeId) {
        try {
            employeeDAO.fireEmployee(employeeId);
        } catch (SQLException e) {
            System.out.println("Error firing employee: " + e.getMessage());
        }
    }

    public ResultSet getEmployee(int employeeId) {
        try {
            return employeeDAO.getEmployee(employeeId);
        } catch (SQLException e) {
            System.out.println("Error getting employee: " + e.getMessage());
            return null;
        }
    }

    public void updateBranch(int employeeId, String newBranch) {
        try {
            employeeDAO.updateBranch(employeeId, newBranch);
        } catch (SQLException e) {
            System.out.println("Error updating branch: " + e.getMessage());
        }
    }

    public void updateSalary(int employeeId, int newSalary) {
        try {
            employeeDAO.updateSalary(employeeId, newSalary);
        } catch (SQLException e) {
            System.out.println("Error updating salary: " + e.getMessage());
        }
    }

    public void updateBankAccount(int employeeId, String newBankAccount) {
        try {
            employeeDAO.updateBankAccount(employeeId, newBankAccount);
        } catch (SQLException e) {
            System.out.println("Error updating bank account: " + e.getMessage());
        }
    }

    public void updateVacationDays(int employeeId, int newVacationDays) {
        try {
            employeeDAO.updateVacationDays(employeeId, newVacationDays);
        } catch (SQLException e) {
            System.out.println("Error updating vacation days: " + e.getMessage());
        }
    }

    public void updateSickDays(int employeeId, int newSickDays) {
        try {
            employeeDAO.updateSickDays(employeeId, newSickDays);
        } catch (SQLException e) {
            System.out.println("Error updating sick days: " + e.getMessage());
        }
    }

    public void updateEducationFund(int employeeId, double newEducationFund) {
        try {
            employeeDAO.updateEducationFund(employeeId, newEducationFund);
        } catch (SQLException e) {
            System.out.println("Error updating education fund: " + e.getMessage());
        }
    }

    public void updateSocialBenefits(int employeeId, double newSocialBenefits) {
        try {
            employeeDAO.updateSocialBenefits(employeeId, newSocialBenefits);
        } catch (SQLException e) {
            System.out.println("Error updating social benefits: " + e.getMessage());
        }
    }

    public void updatePassword(int employeeId, String newPassword) {
        try {
            employeeDAO.updatePassword(employeeId, newPassword);
        } catch (SQLException e) {
            System.out.println("Error updating password: " + e.getMessage());
        }
    }

    public void addRole(int employeeId, String role) {
        try {
            employeeDAO.addRole(employeeId, role);
        } catch (SQLException e) {
            System.out.println("Error adding role: " + e.getMessage());
        }
    }

    public void removeRole(int employeeId, String role) {
        try {
            employeeDAO.removeRole(employeeId, role);
        } catch (SQLException e) {
            System.out.println("Error removing role: " + e.getMessage());
        }
    }

    public void changeRole(int employeeId, String oldRole, String newRole) {
        try {
            employeeDAO.changeRole(employeeId, oldRole, newRole);
        } catch (SQLException e) {
            System.out.println("Error changing role: " + e.getMessage());
        }
    }

    public ResultSet getRoles(int employeeId) {
        try {
            return employeeDAO.getRoles(employeeId);
        } catch (SQLException e) {
            System.out.println("Error getting roles: " + e.getMessage());
            return null;
        }
    }

    public ResultSet getAllEmployees() {
        try {
            return employeeDAO.getAllEmployees();
        } catch (SQLException e) {
            System.out.println("Error getting all employees: " + e.getMessage());
            return null;
        }
    }

    public void checkEmployee(int employeeId) {
        try {
            employeeDAO.checkEmployee(employeeId);
        } catch (SQLException e) {
            System.out.println("Error checking employee: " + e.getMessage());
        }
    }
}
