package DomainLayer;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

public class EmployeeFacade { //employee related methods
    private Map<Integer, EmployeeManager> employeeManagers;
    private Map<Integer, ShiftEmployee> shiftEmployees;

    public EmployeeFacade() {
        this.employeeManagers = new HashMap<>();
        this.shiftEmployees = new HashMap<>();
    }
    public Employee login(int id, String password) {
        Employee e = getEmployee(id);
        if(e != null && e.login(password)) {
            return e;
        }
        return null;
    }

    public String logout(int id) {
        if(!isLoggedIn(id))
            return "You are not logged in";
        Employee e = getEmployee(id);
        e.logout();
        return null;
    }

    //employee manager methods
    public boolean isEmployeeManager(int id) {
        return employeeManagers.containsKey(id);
    }

    public boolean isShiftManager(int id) {
        ShiftEmployee shiftEmployee = shiftEmployees.get(id);
        return shiftEmployee.isShiftManager();
    } 

    public String removeEmployee(int employeeId, int empManagerId) {
        if(!isEmployeeManager(empManagerId))
            return "this action is allowed only for employee manager";
        if(!isLoggedIn(empManagerId))
            return "You are not logged in";
        EmployeeManager employeeManager = getEmployeeManager(empManagerId);
        return employeeManager.removeEmployee(employeeId);
    }

    public String hireEmployee(int employeeId, int empManagerId, String branch,String employeeName, String bankAccount,int salary, LocalDate startDate, int vacationDays, int sickDays,double educationFund, double socialBenefits, String employeePassword, Role role) {
        if(!isEmployeeManager(empManagerId))
            return "this action is allowed only for employee manager";
        if(!isLoggedIn(empManagerId)) 
            return "You are not logged in";
        EmployeeManager employeeManager = getEmployeeManager(empManagerId);
        employeeManager.hireEmployee(employeeId, employeeName, branch, bankAccount,salary, startDate, vacationDays, sickDays, educationFund, socialBenefits, employeePassword, role);
        ShiftEmployee shiftEmployee = shiftEmployees.get(employeeId);
        shiftEmployees.put(employeeId, shiftEmployee);
        return null;     
    }
    
    public String fireEmployee(int employeeId, int empManagerId) {
        if(!isEmployeeManager(empManagerId))
            return "this action is allowed only for employee manager";
        if(!isLoggedIn(empManagerId))
            return "You are not logged in";
        EmployeeManager employeeManager = getEmployeeManager(empManagerId);
        return employeeManager.fireEmployee(employeeId);
    }

    public String changeRoleToEmployee(int employeeId, int empManagerId, Role oldRole, Role newRole) {
        if(!isEmployeeManager(empManagerId))
            return "this action is allowed only for employee manager";
        if(!isLoggedIn(empManagerId))
            return "You are not logged in";
        EmployeeManager employeeManager = getEmployeeManager(empManagerId);
        return employeeManager.changeRoleToEmployee(employeeId, oldRole, newRole);
    }

    public String addRoleToEmployee(int employeeId, int empManagerId, Role newRole) {
        if(!isEmployeeManager(empManagerId))
            return "this action is allowed only for employee manager";
        if(!isLoggedIn(empManagerId))
            return "You are not logged in";
        EmployeeManager employeeManager = getEmployeeManager(empManagerId);
        return employeeManager.addRoleToEmployee(employeeId, newRole);
    }

    public String deleteRoleFromEmployee(int employeeId, int empManagerId, Role roleToDelete) {
        if(!isEmployeeManager(empManagerId))
            return "this action is allowed only for employee manager";
        if(!isLoggedIn(empManagerId))
            return "You are not logged in";
        EmployeeManager employeeManager = getEmployeeManager(empManagerId);
        return employeeManager.deleteRoleFromEmployee(employeeId, roleToDelete);
    }

    //update methods
    public String updateSalary(int employeeId, int empManagerId, int salary) {
        if(!isEmployeeManager(empManagerId))
            return "this action is allowed only for employee manager";
        if(!isLoggedIn(empManagerId))
            return "You are not logged in";
        EmployeeManager employeeManager = getEmployeeManager(empManagerId);
        return employeeManager.updateSalaryEmployee(employeeId, salary);
    }

    public String updateBankAccount(int employeeId, int empManagerId, String bankAccount) {
        if(!isEmployeeManager(empManagerId))
            return "this action is allowed only for employee manager";
        if(!isLoggedIn(empManagerId))
            return "You are not logged in";
        EmployeeManager employeeManager = getEmployeeManager(empManagerId);
        return employeeManager.updateBankAccountEmployee(employeeId, bankAccount);
    }

    public String updateVacationDays(int employeeId, int empManagerId, int vacationDays) {
        if(!isEmployeeManager(empManagerId))
            return "this action is allowed only for employee manager";
        if(!isLoggedIn(empManagerId))
            return "You are not logged in";
        EmployeeManager employeeManager = getEmployeeManager(empManagerId);
        return employeeManager.updateVacationDaysEmployee(employeeId, vacationDays);
    }

    public String updateSickDays(int employeeId, int empManagerId, int sickDays) {
        if(!isEmployeeManager(empManagerId))
            return "this action is allowed only for employee manager";
        if(!isLoggedIn(empManagerId))
            return "You are not logged in";
        EmployeeManager employeeManager = getEmployeeManager(empManagerId);
        return employeeManager.updateSickDaysEmployee(employeeId, sickDays);
    }

    public String updateEducationFund(int employeeId, int empManagerId, double educationFund) {
        if(!isEmployeeManager(empManagerId))
            return "this action is allowed only for employee manager";
        if(!isLoggedIn(empManagerId))
            return "You are not logged in";
        EmployeeManager employeeManager = getEmployeeManager(empManagerId);
        return employeeManager.updateEducationFund(employeeId, educationFund);
    }

    public String updateSocialBenefits(int employeeId, int empManagerId, double socialBenefits) {
        if(!isEmployeeManager(empManagerId))
            return "this action is allowed only for employee manager";
        if(!isLoggedIn(empManagerId))
            return "You are not logged in";
        EmployeeManager employeeManager = getEmployeeManager(empManagerId);
        return employeeManager.updateSocialBenefits(employeeId, socialBenefits);
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

  /*  public String addTrainingToEmployee(int employeeId, int empManagerId, Training training) {
        if(!isLoggedIn(empManagerId))
            return "You are not logged in";
        EmployeeManager employeeManager = getEmployeeManager(empManagerId);
        return employeeManager.addTrainingToEmployee(employeeId, training);
    }
    public String removeTrainingFromEmployee(int employeeId, int empManagerId, Training training) {
        if(!isLoggedIn(empManagerId))
            return "You are not logged in";
        EmployeeManager employeeManager = getEmployeeManager(empManagerId);
        return employeeManager.removeTrainingFromEmployee(employeeId, training);
    } */

    public String getAvailableEmployees(int empManagerId, Shift shift, Role role) { //get available employees for a shift with this role
        if(shift == null) {
            return "Shift is null";
        }
        if(!isEmployeeManager(empManagerId))
            return "this action is allowed only for employee manager";
        if(!isLoggedIn(empManagerId))
            return "You are not logged in";
        EmployeeManager employeeManager = getEmployeeManager(shift.getShiftManagerId());
        return employeeManager.getAvailableEmployees(shift, role);
    }

    // shift employee methods

    public String getPrefAllEmployees(int empManagerId) { //get all employees' preferences
    if(!isEmployeeManager(empManagerId))
        return "this action is allowed only for employee manager";
    if(!isLoggedIn(empManagerId))
        return "You are not logged in";
    EmployeeManager employeeManager = getEmployeeManager(empManagerId);
    return employeeManager.getPrefAllEmployees();
    }

    public String getPreferredShiftsEmployee(int empManagerId, int employeeId) { //employee's preferred shifts for shift employee only
        if(!isEmployeeManager(empManagerId))
            return "this action is allowed only for employee manager";
        if(!isLoggedIn(empManagerId))
            return "You are not logged in";
        ShiftEmployee shiftEmployee = shiftEmployees.get(employeeId);
        return shiftEmployee.getPreferredShiftsToString();
    }

    public String getAssignedEmployeeShiftsEmployee(int employeeId, int empManagerId) { //employee's assigned shifts for shift employee only
    if(!isEmployeeManager(empManagerId))
        return "this action is allowed only for employee manager";
    if(!isLoggedIn(empManagerId))
        return "You are not logged in";
    ShiftEmployee shiftEmployee = shiftEmployees.get(employeeId);
    return shiftEmployee.getAssignedShiftsToString();
    }

    public String getAssignedEmployeeShifts(int employeeId, int empManagerId) { //employee's assigned shifts for shift employee only
        if(!isEmployeeManager(empManagerId))
            return "this action is allowed only for employee manager";
        if(!isLoggedIn(empManagerId))
            return "You are not logged in";
        EmployeeManager employeeManager = getEmployeeManager(empManagerId);
        return employeeManager.getAssignedEmployeeShifts(employeeId);
    }


    public String addRole(int id, Role role){
        ShiftEmployee shiftEmployee = shiftEmployees.get(id);
        return shiftEmployee.addRole(role);
    }

    public String removeRole(int id, Role role){
        ShiftEmployee shiftEmployee = shiftEmployees.get(id);
        return shiftEmployee.removeRole(role);
    }

    public String changeRole(int id,Role oldRole, Role newRole){
        ShiftEmployee shiftEmployee = shiftEmployees.get(id);
        return shiftEmployee.changeRole(oldRole, newRole);
    }


}
