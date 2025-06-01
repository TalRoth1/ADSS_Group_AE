package DataLayer;

import DTO.EmployeeDTO;
import DTO.LocationDTO;
import DTO.ShiftDTO;
import DataLayer.DAOs.EmployeeDAO;
import DataLayer.DAOs.EmployeeRoleDAO;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

public class EmployeeController {

    private DBConnection dbConnection = new DBConnection();
    private DBConnection dbConnection2 = new DBConnection();
    private EmployeeDAO employeeDAO;
    private EmployeeRoleDAO employeeRoleDAO;
    private LocationController locationController;
    private ShiftController shiftController;

    public EmployeeController(LocationController locationController, ShiftController shiftController) {
        this.dbConnection.connect("employees.db");
        this.dbConnection2.connect("employee_role.db");
        this.employeeDAO = new EmployeeDAO(dbConnection.getConnection());
        this.employeeRoleDAO = new EmployeeRoleDAO(dbConnection2.getConnection());
        this.locationController = locationController;
        this.shiftController = shiftController;
    }

    public void addEmployee(EmployeeDTO employee, String role) throws SQLException {
        try {
            employeeDAO.addEmployee(employee.getId(), employee.getName(), employee.getBranch().getId(),
                    employee.getBankAccount(), employee.getSalary(),
                    new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssXXX").format(employee.getStartDate()),
                    employee.getVacationDays(), employee.getSickDays(), employee.getEducationFund(),
                    employee.getSocialBenefits(), employee.getPassword());

            employeeRoleDAO.addRole(employee.getId(), role);
        } catch (SQLException e) {
            System.out.println("Error adding employee: " + e.getMessage());
            throw e;
        }
    }

    public void addRole(int employeeId, String role) throws SQLException {
        try {
            employeeRoleDAO.addRole(employeeId, role);
        } catch (SQLException e) {
            System.out.println("Error adding role: " + e.getMessage());
            throw e;
        }
    }

    public void updateEmployeeByField(int employeeId, String fieldName, Object newValue) throws SQLException {
        try {
            employeeDAO.updateEmployeeByField(employeeId, fieldName, newValue);
        } catch (SQLException e) {
            System.out.println("Error updating " + fieldName + ": " + e.getMessage());
            throw e;
        }
    }

    public void updateRole(int employeeId, String oldRole, String newRole) throws SQLException {
        try {
            employeeRoleDAO.updateRole(employeeId, oldRole, newRole);
        } catch (SQLException e) {
            System.out.println("Error updating role: " + e.getMessage());
            throw e;
        }
    }

    public void removeEmployee(int id) throws SQLException {
        try {
            employeeRoleDAO.removeEmployeeRoles(id);
            employeeDAO.removeEmployee(id);
        } catch (SQLException e) {
            System.out.println("Error removing employee: " + e.getMessage());
            throw e;
        }
    }

    public void removeRole(int employeeId, String role) throws SQLException {
        try {
            employeeRoleDAO.removeRole(employeeId, role);
        } catch (SQLException e) {
            System.out.println("Error removing role: " + e.getMessage());
            throw e;
        }
    }

    // not sure if we need this, but it is here for now
    public void removeEmployeeRoles(int employeeId) throws SQLException {
        try {
            employeeRoleDAO.removeEmployeeRoles(employeeId);
        } catch (SQLException e) {
            System.out.println("Error removing all roles: " + e.getMessage());
            throw e;
        }
    }

    public EmployeeDTO getEmployee(int employeeId) throws SQLException {
        try {
            ResultSet rst = employeeDAO.getEmployee(employeeId);
            if (rst.next()) {
                return buildEmployeeDTO(rst);
            }
            return null;
        } catch (SQLException e) {
            System.out.println("Error getting employee: " + e.getMessage());
            return null;
        }
    }

    public List<EmployeeDTO> getAllEmployees() throws SQLException {
        try {
            ResultSet rst = employeeDAO.getAllEmployees();
            List<EmployeeDTO> employees = new ArrayList<>();
            while (rst.next()) {
                employees.add(buildEmployeeDTO(rst));
            }
            return employees;
        } catch (SQLException e) {
            System.out.println("Error getting all employees: " + e.getMessage());
            return null;
        }
    }

    // not sure if we need this, but it is here for now
    public List<String> getRolesForEmployee(int employeeId) throws SQLException {
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

    // maybe dont need because we have updateEmployeeByField
    // update employee.isFinishedWorking to true
    public void fireEmployee(int employeeId) {
        try {
        employeeDAO.fireEmployee(employeeId);
        } catch (SQLException e) {
        System.out.println("Error firing employee: " + e.getMessage());
        }
    }

    // not sure if we need this, but it is here for now
    public void checkEmployee(int employeeId) throws SQLException {
        try {
            employeeDAO.checkEmployee(employeeId);
        } catch (SQLException e) {
            System.out.println("Error checking employee: " + e.getMessage());
            throw e;
        }
    }

    // not sure if we need this, but it is here for now
    public void getAllEmployeesInBranch(int branch) throws SQLException {
        try {
            employeeDAO.getAllEmployeesInBranch(branch);
        } catch (SQLException e) {
            System.out.println("Error getting employees by branch: " + e.getMessage());
            throw e;
        }
    }

    public void clearAllEmployees() throws SQLException {
        try {
            employeeRoleDAO.clearTable();
            employeeDAO.clearTable();
        } catch (SQLException e) {
            System.out.println("Error clearing all employees: " + e.getMessage());
            throw e;
        }
    }

    // helper function to build EmployeeDTO for getEmployee and getAllEmployees
    // method
    private EmployeeDTO buildEmployeeDTO(ResultSet rst) throws SQLException {
        int id = rst.getInt("id");
        String name = rst.getString("name");
        int branchid = rst.getInt("branchid");
        LocationDTO branch = locationController.getLocation(branchid);
        String bankAccount = rst.getString("bankAccount");
        int salary = rst.getInt("salary");
        Date startDate = rst.getDate("startDate");
        int vacationDays = rst.getInt("vacationDays");
        int sickDays = rst.getInt("sickDays");
        float educationFund = rst.getFloat("educationFund");
        float socialBenefits = rst.getFloat("socialBenefits");
        String password = rst.getString("password");
        Boolean isFinishedWorking = rst.getBoolean("isFinishedWorking");

        ResultSet rolesResult = employeeRoleDAO.getRolesForEmployee(id);
        List<String> rolesList = new ArrayList<>();
        while (rolesResult.next()) {
            rolesList.add(rolesResult.getString("role"));
        }

        Map<ShiftDTO, String> assignedShifts = shiftController.getAssignedShiftsForEmployee(id);
        List<ShiftDTO> prefShifts = shiftController.getAllPrefShifts(id);

        return new EmployeeDTO(id, name, branch, bankAccount, salary, startDate, vacationDays, sickDays,
                educationFund, socialBenefits, password, isFinishedWorking, prefShifts, assignedShifts, rolesList);
    }

}
