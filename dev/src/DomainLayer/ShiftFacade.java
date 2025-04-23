package DomainLayer;

import java.time.LocalDate;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class ShiftFacade { //shift related methods

    private Map<Integer, EmployeeManager> employeeManagers;
    private Map<Integer, ShiftEmployee> shiftEmployees;

    public ShiftFacade() {
        this.employeeManagers = new HashMap<>();
        this.shiftEmployees = new HashMap<>();
    }

    public Shift getShift(LocalDate date, ShiftType shiftType, int empManagerId) {
        if(!isLoggedIn(empManagerId))
            return null;
        EmployeeManager employeeManager = getEmployeeManager(empManagerId);
        return employeeManager.getShift(date, shiftType);
    }
    
    public Employee login(int id, String password) {
        Employee e = getEmployee(id);
        if(e != null && e.login(password))
            return e;
        return null;
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

    public String changeShiftManager(Shift shift, int oldShiftManagerId, int newShiftManagerId, int empManagerId) {
        if(!isLoggedIn(empManagerId))
            return "You are not logged in";
        EmployeeManager employeeManager = getEmployeeManager(empManagerId);
        return employeeManager.changeShiftManager(shift, oldShiftManagerId, newShiftManagerId);
    }

    public String shiftReplacement(Shift shift, int employeeId, int empManagerId, int newEmployeeId) {
        if(!isLoggedIn(empManagerId))
            return "You are not logged in";
        EmployeeManager employeeManager = getEmployeeManager(empManagerId);
        return employeeManager.shiftReplacement(shift, employeeId, newEmployeeId);
    }

    public String createShift(LocalDate date, ShiftType shiftType,int empManagerId, int shiftManagerId) {
        if(!isLoggedIn(empManagerId))
            return "You are not logged in";
        EmployeeManager employeeManager = getEmployeeManager(empManagerId);
        return employeeManager.createShift(date, shiftType, shiftManagerId);
    }

    public String addEmployeeToShift(int employeeId, Shift shift,Role role,int empManagerId) {
        if(!isLoggedIn(empManagerId))
            return "You are not logged in";
        EmployeeManager employeeManager = getEmployeeManager(empManagerId);
        return employeeManager.addEmployeeToShift(employeeId, shift, role);
    }

    public String removeEmployeeFromShift(int employeeId, Shift shift, int empManagerId) {
        if(!isLoggedIn(empManagerId))
            return "You are not logged in";
        EmployeeManager employeeManager = getEmployeeManager(empManagerId);
        return employeeManager.removeEmployeeFromShift(employeeId, shift);
    }


    // shift methods
    public String getEmployeeInfo(int shiftManagerId, Shift shift) {
        if(!isLoggedIn(shiftManagerId))
            return "You are not logged in";
        ShiftEmployee shiftEmployee = shiftEmployees.get(shiftManagerId);
        return shift.getEmployeesInfo();
    }

    public String getShiftInfo(int id, Shift shift) { //for shift manager OR shift employee
        if(!isLoggedIn(id))
            return "You are not logged in";
        return shift.toString();
    }

    public String addEmployee(int employeeId, Shift shift, int empManagerId, Role role) {
        if(!isLoggedIn(empManagerId))
            return "You are not logged in";
        return shift.addEmployee(employeeId, role);
    }

    public String removeEmployee(int employeeId, Shift shift, int empManagerId) {
        if(!isLoggedIn(empManagerId))
            return "You are not logged in";
        return shift.removeEmployee(employeeId);
    }
    public String setRequiredRoles(int empManagerId, Shift shift, Role role, int num) {
        if(!isLoggedIn(empManagerId))
            return "You are not logged in";
        return shift.setRequiredRoles(role, num);
    }

    public String getShiftString(int empManagerId, Shift shift) {
        if(!isLoggedIn(empManagerId))
            return "You are not logged in";
        return shift.getShiftString();
    }

    public String setShiftManager(int empManagerId, Shift shift, int id){
        if(!isLoggedIn(empManagerId))
            return "You are not logged in";
        return shift.setShiftManagerId(id);

    }

    public String addPreferredShift(int id, Shift shift) {
        ShiftEmployee shiftEmployee = shiftEmployees.get(id);
        return shiftEmployee.addPreferredShift(shift);
    }

    public String removePreferredShift(int id, Shift shift) {
        ShiftEmployee shiftEmployee = shiftEmployees.get(id);
        return shiftEmployee.removePreferredShift(shift);
    }

    public String addAssignedShift(int id, Shift shift, Role role) {
        ShiftEmployee shiftEmployee = shiftEmployees.get(id);
        return shiftEmployee.addAssignedShift(shift, role);
    }

    public String removeAssignedShift(int id, Shift shift) {
        ShiftEmployee shiftEmployee = shiftEmployees.get(id);
        return shiftEmployee.removeAssignedShift(shift);
    }

    public boolean isAvailable(int id, Shift shift) {
        ShiftEmployee shiftEmployee = shiftEmployees.get(id);
        return shiftEmployee.isAvailable(shift);
    }

    public void getAssignedEmployeesInfo(int managerId, Shift shift) {
        ShiftEmployee shiftEmployee = shiftEmployees.get(managerId);
        shift.getEmployeesInfo();
    }

    public Set<Shift> getPastShifts(int empManagerId) {
        if(!isLoggedIn(empManagerId))
            return Collections.emptySet(); 
        ShiftEmployee shiftEmployee = shiftEmployees.get(empManagerId);
        return shiftEmployee.getPastShifts();
    }


}