package DataLayer;

import DTO.EmployeeDTO;
import DTO.EmployeeRoleDTO;
import DataLayer.DAOs.EmployeeDAO;
import DataLayer.DAOs.EmployeeRoleDAO;
import DataLayer.DAOs.EmployeeShiftDAO;
import DataLayer.DAOs.PreferredShiftDAO;
import DomainLayer.Role;
import DomainLayer.Shift;
import DomainLayer.ShiftType;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class EmployeeController {

    private DBConnection dbConnection = new DBConnection();
    private Connection connection;
    private EmployeeDAO employeeDAO;
    private EmployeeRoleDAO employeeRoleDAO;
    private EmployeeShiftDAO employeeShiftDAO;
    private PreferredShiftDAO preferredShiftDAO;

    public EmployeeController() {
        String DB_URL = "Employees.db";
        DBConnection.connect(DB_URL);
        this.connection = DBConnection.getConnection();
        this.employeeDAO = new EmployeeDAO(connection);
        this.employeeRoleDAO = new EmployeeRoleDAO(connection);
        this.employeeShiftDAO = new EmployeeShiftDAO(connection);
        this.preferredShiftDAO = new PreferredShiftDAO(connection);
    }

    public EmployeeDAO getEmployeeDAO() {
        return employeeDAO;
    }

    public void addEmployee(int id, String name, LocationDL b, String bankAccount, int salary, String startDate,
            int vacationDays, int sickDays, double educationFund, double socialBenefits, String password) {
        try {
            int branchid = b.getId();
            employeeDAO.addEmployee(id, name, branchid, bankAccount, salary, startDate, vacationDays, sickDays,
                    educationFund, socialBenefits, password);
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

    public EmployeeDTO getEmployee(int employeeId) {
        try {
            ResultSet rst = employeeDAO.getEmployee(employeeId);
            if (rst.next()) {
                int id = rst.getInt("id");
                String name = rst.getString("name");
                int branchid = rst.getInt("branchid");
                String bankAccount = rst.getString("bankAccount");
                int salary = rst.getInt("salary");
                String startDate = rst.getString("startDate");
                int vacationDays = rst.getInt("vacationDays");
                int sickDays = rst.getInt("sickDays");
                double educationFund = rst.getDouble("educationFund");
                double socialBenefits = rst.getDouble("socialBenefits");
                String password = rst.getString("password");
                Boolean isFinishedWorking = rst.getBoolean("isFinishedWorking");
                ResultSet rolesResult = employeeRoleDAO.getRoles(employeeId);
                List<Role> roles = new ArrayList<>();
                while (rolesResult.next()) {
                    String roleName = rolesResult.getString("role");
                    roles.add(Role.valueOf(roleName.toUpperCase()));
                }

                ResultSet assignedResult = employeeShiftDAO.getEmployeeShifts(employeeId);
                List<Shift> assignedshifts = new ArrayList<>();
                while (assignedResult.next()) {
                    LocalDate date = assignedResult.getDate("date").toLocalDate();
                    ShiftType shiftType = ShiftType.valueOf(assignedResult.getString("shiftType"));
                    //צריך להביא פה איכשהו את האיידי של מנהל המשמרת
                    //וגם צריך להחליט אם שומרים אובייקט של לוקיישן או רק איידי
                    Shift shift = new Shift(date, shiftType,  ?  ?  ?, branchid);
                    assignedshifts.add(shift);
                }
                ResultSet prefResult = preferredShiftDAO.getPreferredShifts(employeeId);
                List<Shift> prefShifts = new ArrayList<>();
                while (prefResult.next()) {
                    LocalDate date = prefResult.getDate("date").toLocalDate();
                    ShiftType shiftType = ShiftType.valueOf(prefResult.getString("shiftType"));
                    //צריך להביא פה איכשהו את האיידי של מנהל המשמרת
                    //וגם צריך להחליט אם שומרים אובייקט של לוקיישן או רק איידי
                    Shift shift = new Shift(date, shiftType,  ?  ?  ?, branchid);
                    prefShifts.add(shift);
                }
                return new EmployeeDTO(id, name, branchid, bankAccount, salary, startDate, vacationDays, sickDays,
                        educationFund, socialBenefits, password, isFinishedWorking, roles, assignedshifts, prefShifts);
            }
            return null;
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

    public List<EmployeeDTO> getAllEmployees() {
        try {
            ResultSet rst = employeeDAO.getAllEmployees();
            List<EmployeeDTO> employees = new ArrayList<>();
            while (rst.next()) {
                int id = rst.getInt("id");
                String name = rst.getString("name");
                int branchid = rst.getInt("branchid");
                String bankAccount = rst.getString("bankAccount");
                int salary = rst.getInt("salary");
                String startDate = rst.getString("startDate");
                int vacationDays = rst.getInt("vacationDays");
                int sickDays = rst.getInt("sickDays");
                double educationFund = rst.getDouble("educationFund");
                double socialBenefits = rst.getDouble("socialBenefits");
                String password = rst.getString("password");
                Boolean isFinishedWorking = rst.getBoolean("isFinishedWorking");
                employees.add(new EmployeeDTO(id, name, branchid, bankAccount, salary, startDate, vacationDays,
                        sickDays, educationFund, socialBenefits, password, isFinishedWorking));
            }
            return employees;
        } catch (SQLException e) {
            System.out.println("Error getting all employees: " + e.getMessage());
            return null;
        }
    }

    public List<EmployeeRoleDTO> getAllEmployeeRoles() {
        try {
            ResultSet rst = employeeRoleDAO.getAllRoles();
            List<EmployeeRoleDTO> roles = new ArrayList<>();
            while (rst.next()) {
                int id = rst.getInt("id");
                String roleName = rst.getString("roleName");
                roles.add(new EmployeeRoleDTO(id, roleName));
            }
            return roles;
        } catch (SQLException e) {
            System.out.println("Error getting all employee roles: " + e.getMessage());
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

    public void login(int employeeId, String password) {
        try {
            employeeDAO.setloginEmployee(employeeId, true);
        } catch (SQLException e) {
            System.out.println("Error logging in: " + e.getMessage());
        }
    }

    public void logout(int employeeId) {
        try {
            employeeDAO.setloginEmployee(employeeId, false);
        } catch (SQLException e) {
            System.out.println("Error logging out: " + e.getMessage());
        }
    }

    public void getAllEmployeesInBranch(int branch) {
        try {
            employeeDAO.getAllEmployeesInBranch(branch);
        } catch (SQLException e) {
            System.out.println("Error getting employees by branch: " + e.getMessage());
        }
    }

}
