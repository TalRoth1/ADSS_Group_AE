package DataLayer;

import DTO.EmployeeDTO;
import DTO.EmployeeRoleDTO;
import DTO.EmployeeShiftDTO;
import DTO.LocationDTO;
import DTO.PreferredShiftDTO;
import DataLayer.DAOs.EmployeeDAO;
import DataLayer.DAOs.EmployeeRoleDAO;
import DataLayer.DAOs.ShiftAssignedDAO;
import DataLayer.DAOs.ShiftPreferredDAO;
import DataLayer.DAOs.ShiftDAO;
import DomainLayer.LocationDL;
import DomainLayer.Role;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import DomainLayer.Employee;

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

    public void addEmployee(EmployeeDTO employee) {
        try {
            employeeDAO.addEmployee(employee.getId(), employee.getName(), employee.getBranch().getId(),
                    employee.getBankAccount(), employee.getSalary(), new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssXXX").format(employee.getStartDate()),
                    employee.getVacationDays(), employee.getSickDays(), employee.getEducationFund(),
                    employee.getSocialBenefits(), employee.getPassword());

            employeeRoleDAO.addRole(employee.getId(), employee.getRoles().get(0));
        } catch (SQLException e) {
            System.out.println("Error adding employee: " + e.getMessage());
        }
    }

    public void removeEmployee(int id) {
        try {
            employeeRoleDAO.removeEmployeeRoles(id);
            employeeDAO.removeEmployee(id);
        } catch (SQLException e) {
            System.out.println("Error removing employee: " + e.getMessage());
        }
    }

    public void updateEmployeeByField(int employeeId, String fieldName, Object newValue) {
        try {
            employeeDAO.updateEmployeeByField(employeeId, fieldName, newValue);
        } catch (SQLException e) {
            System.out.println("Error updating " + fieldName + ": " + e.getMessage());
        }
    }

    public void addRole(int employeeId, String role) {
        try {
            employeeRoleDAO.addRole(employeeId, role);
        } catch (SQLException e) {
            System.out.println("Error adding role: " + e.getMessage());
        }
    }

    public void removeRole(int employeeId, String role) {
        try {
            employeeRoleDAO.removeRole(employeeId, role);
        } catch (SQLException e) {
            System.out.println("Error removing role: " + e.getMessage());
        }
    }

    public void removeEmployeeRoles(int employeeId) {
        try {
            employeeRoleDAO.removeEmployeeRoles(employeeId);
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

    // update employee.isFinishedWorking to true
    public void fireEmployee(int employeeId) {
        try {
            employeeDAO.fireEmployee(employeeId);
        } catch (SQLException e) {
            System.out.println("Error firing employee: " + e.getMessage());
        }
    }

    public EmployeeDTO getEmployee(int employeeId) {
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

    public List<EmployeeDTO> getAllEmployees() {
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

    public void checkEmployee(int employeeId) {
        try {
            employeeDAO.checkEmployee(employeeId);
        } catch (SQLException e) {
            System.out.println("Error checking employee: " + e.getMessage());
        }
    }

    public void getAllEmployeesInBranch(int branch) {
        try {
            employeeDAO.getAllEmployeesInBranch(branch);
        } catch (SQLException e) {
            System.out.println("Error getting employees by branch: " + e.getMessage());
        }
    }

    // helper function to build EmployeeDTO for getEmployee and getAllEmployees method
    private EmployeeDTO buildEmployeeDTO(ResultSet rst) throws SQLException {
        int id = rst.getInt("id");
        String name = rst.getString("name");
        int branchid = rst.getInt("branchid");
        LocationDTO branch = locationController.getLocation(branchid);
        String bankAccount = rst.getString("bankAccount");
        int salary = rst.getInt("salary");
        String startDate = rst.getString("startDate");
        int vacationDays = rst.getInt("vacationDays");
        int sickDays = rst.getInt("sickDays");
        float educationFund = rst.getFloat("educationFund");
        float socialBenefits = rst.getFloat("socialBenefits");
        String password = rst.getString("password");
        Boolean isFinishedWorking = rst.getBoolean("isFinishedWorking");

        //change for list of strings
        ResultSet roles = employeeRoleDAO.getRolesForEmployee(id);
        List<EmployeeRoleDTO> rolesList = new ArrayList<>();
        while (roles.next()) {
            String roleName = roles.getString("roleName");
            int roleId = roles.getInt("roleId");
            String role = roleName;
            EmployeeRoleDTO roleDTO = new EmployeeRoleDTO(roleId, role);
            rolesList.add(roleDTO);
        }

        //change so we will use the shiftcontroller to get the shifts
        ResultSet assignedResult = employeeShiftDAO.getEmployeeShifts(id);
        List<ShiftAssignedDTO> assignedshifts = new ArrayList<>();
        while (assignedResult.next()) {
            String date = assignedResult.getDate("date").toString();
            String shiftType = assignedResult.getString("shiftType");
            String role = assignedResult.getString("role");
            EmployeeShiftDTO shift = new EmployeeShiftDTO(id, date, shiftType, role);
            assignedshifts.add(shift);
        }

        ResultSet prefResult = preferredShiftDAO.getPreferredShifts(id);
        List<ShiftPreferredDTO> prefShifts = new ArrayList<>();
        while (prefResult.next()) {
            String date = prefResult.getDate("date").toString();
            String shiftType = prefResult.getString("shiftType");
            PreferredShiftDTO shiftDTO = new PreferredShiftDTO(id, date, shiftType);
            prefShifts.add(shiftDTO);
        }

        return new EmployeeDTO(id, name, branch, bankAccount, salary, startDate, vacationDays, sickDays,
                educationFund, socialBenefits, password, isFinishedWorking, roles, assignedshifts, prefShifts);
    }

}
