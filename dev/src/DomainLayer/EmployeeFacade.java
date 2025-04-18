package DomainLayer;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

public class EmployeeFacade {
    private Map<Integer, EmployeeManager> employeeManagers;
    private Map<Integer, ShiftEmployee> shiftEmployees;

    public EmployeeFacade() {
        this.employeeManagers = new HashMap<>();
        this.shiftEmployees = new HashMap<>();
    }
    public Employee login(int id, String password) {
        Employee e = getEmployee(id);
        if(e != null && e.login(password))
            return e;
        return null;
    }

    public String fireEmployee(int employeeId, int empManagerId) {
        if(!isLoggedIn(empManagerId))
            return "You are not logged in";
        if(!checkEmployee(employeeId)) 
            return "Can't fire employee: No employee found with ID " + employeeId;
        EmployeeManager employeeManager = getEmployeeManager(empManagerId);
        return employeeManager.fireEmployee(employeeId);
    }

    public String hireEmployee(int employeeId, int empManagerId, String employeeName, String bankAccount,
                               int salary, LocalDate startDate, int vacationDays, int sickDays,
                               double educationFund, double socialBenefits, String employeePassword, Role role) {
        if(!isLoggedIn(empManagerId)) ////////nedd to check mikrey katze
            return "You are not logged in";
        if(checkEmployee(employeeId))
            return "Can't hire employee: " + employeeId + " already hired";
        else {
            EmployeeManager employeeManager = getEmployeeManager(empManagerId);
            employeeManager.hireEmployee(employeeId, employeeName, bankAccount,
                    salary, startDate, vacationDays, sickDays, educationFund, socialBenefits, employeePassword, role);
            ShiftEmployee shiftEmployee = shiftEmployees.get(employeeId);
            addShiftEmployee(employeeId, shiftEmployee);
            return null;
        }
    }

    public String changeRoleToEmployee(int employeeId, int empManagerId, Role oldRole, Role newRole) {
        if(!isLoggedIn(empManagerId))
            return "You are not logged in";
        if(!checkEmployee(employeeId))
            return employeeId + " doesn't exist";
        EmployeeManager employeeManager = getEmployeeManager(empManagerId);
        return employeeManager.changeRoleToEmployee(employeeId, oldRole, newRole);
    }

    public String addRoleToEmployee(int employeeId, int empManagerId, Role newRole) {
        if(!isLoggedIn(empManagerId))
            return "You are not logged in";
        if(!checkEmployee(employeeId))
            return employeeId + " doesn't exist";
        EmployeeManager employeeManager = getEmployeeManager(empManagerId);
        return employeeManager.addRoleToEmployee(employeeId, newRole);
    }

    public String deleteRoleFromEmployee(int employeeId, int empManagerId, Role roleToDelete) {
        if(!isLoggedIn(empManagerId))
            return "You are not logged in";
        if(!checkEmployee(employeeId))
            return employeeId + " doesn't exist";
        EmployeeManager employeeManager = getEmployeeManager(empManagerId);
        return employeeManager.deleteRoleFromEmployee(employeeId, roleToDelete);
    }

    public String updateSalary(int employeeId, int empManagerId, int salary) {
        if(!isLoggedIn(empManagerId))
            return "You are not logged in";
        if(!checkEmployee(employeeId))
            return employeeId + " doesn't exist";
        EmployeeManager employeeManager = getEmployeeManager(empManagerId);
        return employeeManager.updateSalaryEmployee(employeeId, salary);
    }

    public String updateBankAccount(int employeeId, int empManagerId, String bankAccount) {
        if(!isLoggedIn(empManagerId))
            return "You are not logged in";
        if(!checkEmployee(employeeId))
            return employeeId + " doesn't exist";
        EmployeeManager employeeManager = getEmployeeManager(empManagerId);
        return employeeManager.updateBankAccountEmployee(employeeId, bankAccount);
    }

    public String updateVacationDays(int employeeId, int empManagerId, int vacationDays) {
        if(!isLoggedIn(empManagerId))
            return "You are not logged in";
        if(!checkEmployee(employeeId))
            return employeeId + " doesn't exist";
        EmployeeManager employeeManager = getEmployeeManager(empManagerId);
        return employeeManager.updateVacationDaysEmployee(employeeId, vacationDays);
    }

    public String updateSickDays(int employeeId, int empManagerId, int sickDays) {
        if(!isLoggedIn(empManagerId))
            return "You are not logged in";
        if(!checkEmployee(employeeId))
            return employeeId + " doesn't exist";
        EmployeeManager employeeManager = getEmployeeManager(empManagerId);
        return employeeManager.updateSickDaysEmployee(employeeId, sickDays);
    }

    public String updateEducationFund(int employeeId, int empManagerId, double educationFund) {
        if(!isLoggedIn(empManagerId))
            return "You are not logged in";
        if(!checkEmployee(employeeId))
            return employeeId + " doesn't exist";
        EmployeeManager employeeManager = getEmployeeManager(empManagerId);
        return employeeManager.updateEducationFund(employeeId, educationFund);
    }

    public String updateSocialBenefits(int employeeId, int empManagerId, double socialBenefits) {
        if(!isLoggedIn(empManagerId))
            return "You are not logged in";
        if(!checkEmployee(employeeId))
            return employeeId + " doesn't exist";
        EmployeeManager employeeManager = getEmployeeManager(empManagerId);
        return employeeManager.updateSocialBenefits(employeeId, socialBenefits);
    }

    public String updatePassword(int employeeId, String oldPassword, String newPassword) {
        if(!isLoggedIn(employeeId))
            return "You are not logged in";
        if(!checkEmployee(employeeId))
            return employeeId + " doesn't exist";
        EmployeeManager employee = getEmployeeManager(employeeId);
        return employee.updatePassword(oldPassword, newPassword);
    }

    public void logout(int id) {
        Employee e = getEmployee(id);
        e.logout();
    }

    public String getPreferences(int empManagerId) {
        if(!isLoggedIn(empManagerId))
            return "You are not logged in";
        EmployeeManager employeeManager = getEmployeeManager(empManagerId);
        return employeeManager.getPrefAllemployees();
    }

    public String getPrefEmployee(int id) {
        if(!checkEmployee(id))
            return id + " doesn't exist";
        if(!isLoggedIn(id))
            return "You are not logged in";
            
        return "not finish yetttt";

    }

    private void addShiftEmployee(int employeeId, ShiftEmployee shiftEmployee) {
        shiftEmployees.put(employeeId, shiftEmployee);
    }

    private Employee getEmployee(int id) {
        Employee employee = employeeManagers.get(id);
        if(employee == null)
            employee = shiftEmployees.get(id);
        return employee;
    }

    private boolean isLoggedIn(int id) {
        return getEmployee(id).isLoggedIn();
    }

    private EmployeeManager getEmployeeManager(int id) {
        return employeeManagers.get(id);
    }

    private boolean checkEmployee(int id) {
        return shiftEmployees.containsKey(id);
    }

//    private Role convertStringToRole(String roleName) {
//        if (roleName == null) {
//            throw new IllegalArgumentException("Role name cannot be null");
//        }
//        try {
//            return Role.valueOf(roleName.toUpperCase());
//        } catch (IllegalArgumentException e) {
//            throw new IllegalArgumentException("Invalid role name: " + roleName);
//        }
//    }
}
