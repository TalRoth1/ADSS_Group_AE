package DomainLayer;

import DataLayer.*;
import DataLayer.DAOs.EmployeeDAO;
import DataLayer.DAOs.EmployeeRoleDAO;
import DataLayer.DAOs.EmployeeShiftDAO;

import java.sql.Connection;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.temporal.TemporalAdjusters;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class EmployeeFacade { // employee related methods

    private EmployeeManager employeeManager;
    private Map<Integer, ShiftEmployee> shiftEmployees;
    private List<LocationDL> branches;
    private Connection connection;
    private EmployeeDAO empDAO;
    private EmployeeController empController;
    private EmployeeRoleDAO empRoleDAO;
    private EmployeeRoleController empRoleController;
    private EmployeeShiftDAO empShiftDAO;
    private EmployeeController empShiftController;

    // initalize employees
    /*
     * EmployeeManager keren = new EmployeeManager(100, "Keren", "1", "111222",
     * 7000,
     * LocalDate.of(2025, 4, 10), 20, 5, 100, 100, "123");
     * ShiftEmployee Liat = new ShiftEmployee(101, "Liat", "1", "111222", 7000,
     * LocalDate.of(2025, 4, 10), 20, 5, 100, 100,
     * "password", Role.SHIFT_MANAGER);
     * ShiftEmployee Erez = new ShiftEmployee(102, "Erez", "1", "111222", 7000,
     * LocalDate.of(2025, 4, 10), 20, 5, 100, 100,
     * "password", Role.CASHIER);
     * ShiftEmployee Elad = new ShiftEmployee(103, "Elad", "1", "111222", 7000,
     * LocalDate.of(2025, 4, 10), 20, 5, 100, 100,
     * "password", Role.DRIVER);
     * ShiftEmployee Eylon = new ShiftEmployee(104, "Eylon", "1", "111222", 7000,
     * LocalDate.of(2025, 4, 10), 20, 5, 100,
     * 100, "password", Role.DRIVER);
     * ShiftEmployee Tal = new ShiftEmployee(105, "Tal", "1", "111222", 7000,
     * LocalDate.of(2025, 4, 10), 20, 5, 100, 100,
     * "password", Role.CASHIER);
     * ShiftEmployee Ofir = new ShiftEmployee(106, "Ofir", "1", "111222", 7000,
     * LocalDate.of(2025, 4, 10), 20, 5, 100, 100,
     * "password", Role.STORE_KEEPER);
     * ShiftEmployee Kiril = new ShiftEmployee(107, "Kiril", "1", "111222", 7000,
     * LocalDate.of(2025, 4, 10), 20, 5, 100,
     * 100, "password", Role.STORE_KEEPER);
     * ShiftEmployee Ofri = new ShiftEmployee(108, "Ofri", "1", "111222", 7000,
     * LocalDate.of(2025, 4, 10), 20, 5, 100, 100,
     * "password", Role.SHIFT_MANAGER);
     * 
     * public EmployeeFacade() {
     * this.employeeManagers = new HashMap<>();
     * this.shiftEmployees = new HashMap<>();
     * this.branches = Collections.emptyList(); // Initialize branches as empty list
     * 
     * // Add the employee manager
     * employeeManagers.put(keren.getId(), keren);
     * // Add shift employees
     * shiftEmployees.put(Liat.getId(), Liat);
     * shiftEmployees.put(Erez.getId(), Erez);
     * shiftEmployees.put(Elad.getId(), Elad);
     * shiftEmployees.put(Eylon.getId(), Eylon);
     * shiftEmployees.put(Tal.getId(), Tal);
     * shiftEmployees.put(Ofir.getId(), Ofir);
     * shiftEmployees.put(Kiril.getId(), Kiril);
     * shiftEmployees.put(Ofri.getId(), Ofri);
     * 
     * keren.addEmployee(Liat);
     * keren.addEmployee(Erez);
     * keren.addEmployee(Elad);
     * keren.addEmployee(Eylon);
     * keren.addEmployee(Tal);
     * keren.addEmployee(Ofir);
     * keren.addEmployee(Kiril);
     * keren.addEmployee(Ofri);
     * }
     */
    public Employee login(int id, String password) throws Exception {
        Employee e = getEmployee(id);
        if (e == null) {
            throw new Exception("Employee not found.");
        }
        try {
            e.login(password);
        } catch (Exception ex) {
            throw new Exception("Login failed: " + ex.getMessage());
        }
        empController.login(id, password);
        return e;
    }

    public void logout(int id) throws Exception {
        if (!isLoggedIn(id)) {
            throw new Exception("You are not logged in.");
        }
        Employee e = getEmployee(id);
        empController.logout(id);
        e.logout();
        System.out.println("Employee with ID " + id + " has logged out.");
    }

    // employee manager methods
    public boolean isEmployeeManager(int id) {
        return employeeManager.getId() == id;
    }

    public boolean isShiftManager(int id) {
        ShiftEmployee shiftEmployee = shiftEmployees.get(id);
        return shiftEmployee.isShiftManager();
    }

    public void removeEmployee(int employeeId, int empManagerId) {
        if (!isEmployeeManager(empManagerId)) {
            throw new RuntimeException("This action is allowed only for employee manager.");
        }
        if (!isLoggedIn(empManagerId)) {
            throw new RuntimeException("You are not logged in.");
        }
        EmployeeManager employeeManager = getEmployeeManager();
        ShiftEmployee shiftEmployee = shiftEmployees.get(employeeId);
        employeeManager.removeEmployee(employeeId);
    }

    // check if there is a driver available for delivery and there is store kepper
    // in each branch of the delivery
    public DriverDL assignCheck(LocalDate sentDate, String shiftType, LocationDL origin, List<LocationDL> destinations,
            String licenceType) {
        try {
            return employeeManager.assignCheck(sentDate, parseShiftType(shiftType), origin, destinations, licenceType);
        } catch (Exception e) {
            System.out.println("Error in assignCheck: " + e.getMessage());
            return null;
        }
    }

    public void hireEmployee(int employeeId, int empManagerId, LocationDL branch, String employeeName,
            String bankAccount,
            int salary, LocalDate startDate, int vacationDays, int sickDays, double educationFund,
            double socialBenefits, String employeePassword, Role role) throws Exception {
        if (!isEmployeeManager(empManagerId)) {
            throw new Exception("This action is allowed only for employee manager.");
        }
        if (!isLoggedIn(empManagerId)) {
            throw new Exception("You are not logged in.");
        }
        if (branches == null || !branches.contains(branch)) {
            throw new Exception("Branch not found.");
        }
        EmployeeManager employeeManager = getEmployeeManager();
        if (employeeManager.checkEmployee(employeeId)) {
            throw new Exception("Employee with ID " + employeeId + " already hired.");
        }

        ShiftEmployee shiftEmployee = employeeManager.hireEmployee(employeeId, employeeName, branch, bankAccount,
                salary, startDate, vacationDays, sickDays, educationFund, socialBenefits, employeePassword, role);

        empController.addEmployee(sickDays, employeeName, bankAccount, bankAccount, salary, bankAccount, vacationDays,
                sickDays, educationFund, socialBenefits, employeePassword);
        shiftEmployees.put(employeeId, shiftEmployee);
        System.out.println("Employee hired: " + shiftEmployee.getName() + " with ID: " + employeeId);
    }

    public void fireEmployee(int employeeId, int empManagerId) throws Exception {
        if (!isEmployeeManager(empManagerId)) {
            throw new Exception("This action is allowed only for employee managers.");
        }
        if (!isLoggedIn(empManagerId)) {
            throw new Exception("You are not logged in.");
        }
        EmployeeManager employeeManager = getEmployeeManager();
        try {
            empController.removeEmployee(employeeId);
            employeeManager.fireEmployee(employeeId);
        } catch (Exception e) {
            throw new Exception("Failed to fire employee: " + e.getMessage());
        }
    }

    public void changeRoleToEmployee(int employeeId, int empManagerId, Role oldRole, Role newRole) throws Exception {
        if (!isEmployeeManager(empManagerId)) {
            throw new Exception("This action is allowed only for employee manager.");
        }
        if (!isLoggedIn(empManagerId)) {
            throw new Exception("You are not logged in.");
        }
        EmployeeManager employeeManager = getEmployeeManager();
        try {
            empRoleController.updateRole(employeeId, oldRole, newRole);
            employeeManager.changeRoleToEmployee(employeeId, oldRole, newRole);
        } catch (Exception e) {
            throw new Exception("Failed to change role: " + e.getMessage());
        }
    }

    public void addRoleToEmployee(int employeeId, int empManagerId, Role newRole) throws Exception {
        if (!isEmployeeManager(empManagerId)) {
            throw new Exception("This action is allowed only for employee manager.");
        }
        if (!isLoggedIn(empManagerId)) {
            throw new Exception("You are not logged in.");
        }
        EmployeeManager employeeManager = getEmployeeManager();
        try {
            empRoleController.addRole(employeeId, newRole);
            employeeManager.addRoleToEmployee(employeeId, newRole);
        } catch (Exception e) {
            throw new Exception("Failed to add role: " + e.getMessage());
        }
    }

    public void deleteRoleFromEmployee(int employeeId, int empManagerId, Role roleToDelete) throws Exception {
        if (!isEmployeeManager(empManagerId)) {
            throw new Exception("This action is allowed only for employee manager.");
        }
        if (!isLoggedIn(empManagerId)) {
            throw new Exception("You are not logged in.");
        }
        EmployeeManager employeeManager = getEmployeeManager();
        try {
            empRoleController.removeRole(employeeId, roleToDelete);
            employeeManager.deleteRoleFromEmployee(employeeId, roleToDelete);
        } catch (Exception e) {
            throw new Exception("Failed to delete role: " + e.getMessage());
        }
    }

    // update methods
    public void updateSalary(int employeeId, int empManagerId, int salary) throws Exception {
        if (!isEmployeeManager(empManagerId)) {
            throw new Exception("This action is allowed only for employee manager.");
        }
        if (!isLoggedIn(empManagerId)) {
            throw new Exception("You are not logged in.");
        }
        EmployeeManager employeeManager = getEmployeeManager();
        try {
            empController.updateSalary(employeeId, salary);
            employeeManager.updateSalaryEmployee(employeeId, salary);
        } catch (Exception e) {
            throw new Exception("Failed to update salary: " + e.getMessage());
        }
    }

    public void updateBankAccount(int employeeId, int empManagerId, String bankAccount) throws Exception {
        if (!isEmployeeManager(empManagerId)) {
            throw new Exception("This action is allowed only for employee manager.");
        }
        if (!isLoggedIn(empManagerId)) {
            throw new Exception("You are not logged in.");
        }
        EmployeeManager employeeManager = getEmployeeManager();
        try {
            empController.updateBankAccount(employeeId, bankAccount);
            employeeManager.updateBankAccountEmployee(employeeId, bankAccount);
        } catch (Exception e) {
            throw new Exception("Failed to update bank account: " + e.getMessage());
        }
    }

    public void updateVacationDays(int employeeId, int empManagerId, int vacationDays) throws Exception {
        if (!isEmployeeManager(empManagerId)) {
            throw new Exception("This action is allowed only for employee manager.");
        }
        if (!isLoggedIn(empManagerId)) {
            throw new Exception("You are not logged in.");
        }
        EmployeeManager employeeManager = getEmployeeManager();
        try {
            empController.updateVacationDays(employeeId, vacationDays);
            employeeManager.updateVacationDaysEmployee(employeeId, vacationDays);
        } catch (Exception e) {
            throw new Exception("Failed to update vacation days: " + e.getMessage());
        }
    }

    public void updateSickDays(int employeeId, int empManagerId, int sickDays) throws Exception {
        if (!isEmployeeManager(empManagerId)) {
            throw new Exception("This action is allowed only for employee manager.");
        }
        if (!isLoggedIn(empManagerId)) {
            throw new Exception("You are not logged in.");
        }
        EmployeeManager employeeManager = getEmployeeManager();
        try {
            empController.updateSickDays(employeeId, sickDays);
            employeeManager.updateSickDaysEmployee(employeeId, sickDays);
        } catch (Exception e) {
            throw new Exception("Failed to update sick days: " + e.getMessage());
        }
    }

    public void updateEducationFund(int employeeId, int empManagerId, double educationFund) throws Exception {
        if (!isEmployeeManager(empManagerId)) {
            throw new Exception("This action is allowed only for employee manager.");
        }
        if (!isLoggedIn(empManagerId)) {
            throw new Exception("You are not logged in.");
        }
        EmployeeManager employeeManager = getEmployeeManager();
        try {
            empController.updateEducationFund(employeeId, educationFund);
            employeeManager.updateEducationFund(employeeId, educationFund);
        } catch (Exception e) {
            throw new Exception("Failed to update education fund: " + e.getMessage());
        }
    }

    public void updateSocialBenefits(int employeeId, int empManagerId, double socialBenefits) throws Exception {
        if (!isEmployeeManager(empManagerId)) {
            throw new Exception("This action is allowed only for employee manager.");
        }
        if (!isLoggedIn(empManagerId)) {
            throw new Exception("You are not logged in.");
        }
        EmployeeManager employeeManager = getEmployeeManager();
        try {
            empController.updateSocialBenefits(employeeId, socialBenefits);
            employeeManager.updateSocialBenefits(employeeId, socialBenefits);
        } catch (Exception e) {
            throw new Exception("Failed to update social benefits: " + e.getMessage());
        }
    }

    public Employee getEmployee(int id) {
        boolean manager = isEmployeeManager(id);
        if (!manager) {
            return shiftEmployees.get(id);
        }
        return employeeManager;
    }

    private boolean isLoggedIn(int id) {
        return getEmployee(id).isLoggedIn();
    }

    private EmployeeManager getEmployeeManager() {
        return employeeManager;
    }

    public void getAvailableEmployees(int empManagerId, LocationDL branch, Shift shift, Role role) throws Exception { // get
                                                                                                                      // available
        // employees for a
        // shift with this
        // role
        if (branch == null || !branches.contains(branch)) {
            throw new Exception("Branch does not exist.");
        }
        if (shift == null) {
            throw new Exception("Shift cannot be null.");
        }
        if (!isEmployeeManager(empManagerId)) {
            throw new Exception("This action is allowed only for employee manager.");
        }
        if (!isLoggedIn(empManagerId)) {
            throw new Exception("You are not logged in.");
        }

        EmployeeManager employeeManager = getEmployeeManager();

        employeeManager.getAvailableEmployees(shift, role);
    }

    // shift employee methods
    public String getPrefAllEmployees(int empManagerId) throws Exception { // get all employees' preferences, for
        // employee manager
        if (!isEmployeeManager(empManagerId)) {
            throw new Exception("This action is allowed only for employee managers.");
        }
        if (!isLoggedIn(empManagerId)) {
            throw new Exception("You are not logged in.");
        }
        EmployeeManager employeeManager = getEmployeeManager();
        return employeeManager.getPrefAllEmployees();
    }

    public void getPreferredShiftsEmployee(int employeeId) throws Exception { // employee's preferred shifts for shift
        // employee only
        if (!isLoggedIn(employeeId)) {
            throw new Exception("You are not logged in.");
        }
        ShiftEmployee shiftEmployee = shiftEmployees.get(employeeId);
        try {
            shiftEmployee.getPreferredShiftsToString();
        } catch (Exception e) {
            throw new Exception("Failed to get preferred shifts: " + e.getMessage());
        }
    }

    public void getAssignedEmployeeShiftsEmployee(int employeeId) throws Exception { // employee's assigned shifts for
        // shift employee only
        if (!isLoggedIn(employeeId)) {
            throw new Exception("You are not logged in.");
        }
        ShiftEmployee shiftEmployee = shiftEmployees.get(employeeId);
        try {
            shiftEmployee.getAssignedShiftsToString();
        } catch (Exception e) {
            throw new Exception("Failed to get assigned employee shifts: " + e.getMessage());
        }
    }

    public void getAssignedEmployeeShiftsManager(int employeeId, int empManagerId) throws Exception { // employee's
        // assigned shifts
        // for shift
        // employee only
        if (!isEmployeeManager(empManagerId)) {
            throw new Exception("This action is allowed only for employee manager.");
        }
        if (!isLoggedIn(empManagerId)) {
            throw new Exception("You are not logged in.");
        }
        EmployeeManager employeeManager = getEmployeeManager();
        try {
            employeeManager.getAssignedEmployeeShiftsManager(employeeId);
        } catch (Exception e) {
            throw new Exception("Failed to get assigned employee shifts: " + e.getMessage());
        }
    }

    // public void getAssignedEmployeeShiftsAsShiftManager(int employeeId, int
    // shiftManagerId) throws Exception { // employee's assigned shifts for shift
    // employee only
    // if (!isShiftManager(shiftManagerId)) {
    // throw new Exception("This action is allowed only for shift manager.");
    // }
    // if (!isLoggedIn(shiftManagerId)) {
    // throw new Exception("You are not logged in.");
    // }
    // EmployeeManager anyManager = getAnyEmployeeManager();
    // if (anyManager == null) {
    // System.out.println("No employee manager available to process request.");
    // }
    // return anyManager.getAssignedEmployeeShiftsManager(employeeId);
    // }
    // private EmployeeManager getAnyEmployeeManager() {
    // return employeeManagers.values().stream().findFirst().orElse(null);
    // }

    // public String addRole(int id, Role role) {
    // ShiftEmployee shiftEmployee = shiftEmployees.get(id);
    // return shiftEmployee.addRole(role);
    // }
    // public String removeRole(int id, Role role) {
    // ShiftEmployee shiftEmployee = shiftEmployees.get(id);
    // return shiftEmployee.removeRole(role);
    // }
    // public String changeRole(int id, Role oldRole, Role newRole) {
    // ShiftEmployee shiftEmployee = shiftEmployees.get(id);
    // return shiftEmployee.changeRole(oldRole, newRole);
    // }
    // shift methods
    public Shift getShift(LocationDL branch, LocalDate date, ShiftType shiftType, int id) throws Exception {
        if (!isLoggedIn(id)) {
            throw new Exception("You must be logged in.");
        }
        EmployeeManager employeeManager = getEmployeeManager();
        return employeeManager.getShift(branch, date, shiftType);
    }

    public Shift getShiftForEmployee(LocationDL branch, LocalDate date, ShiftType shiftType) throws Exception {
        Shift shift = getEmployeeManager().getShift(branch, date, shiftType);
        if (shift != null) {
            return shift;
        }
        throw new Exception("Shift not found for date: " + date + " and type: " + shiftType);
    }

    public void changeShiftManager(Shift shift, int oldShiftManagerId, int newShiftManagerId, int empManagerId)
            throws Exception {
        if (!isEmployeeManager(empManagerId)) {
            throw new Exception("This action is allowed only for employee managers.");
        }
        if (!isLoggedIn(empManagerId)) {
            throw new Exception("You are not logged in.");
        }
        EmployeeManager employeeManager = getEmployeeManager();
        try {
            // empController.changeShiftManager(oldShiftManagerId, newShiftManagerId,
            // shift.getDate(), shift.getShiftType(), shift.getBranch());
            employeeManager.changeShiftManager(shift, oldShiftManagerId, newShiftManagerId);
        } catch (Exception e) {
            throw new Exception("Failed to change shift manager: " + e.getMessage());
        }
    }

    public void shiftReplacement(Shift shift, int employeeId, int empManagerId, int newEmployeeId) throws Exception {
        if (!isEmployeeManager(empManagerId)) {
            throw new Exception("This action is allowed only for employee managers.");
        }
        if (!isLoggedIn(empManagerId)) {
            throw new Exception("You are not logged in.");
        }
        EmployeeManager employeeManager = getEmployeeManager();
        try {
            employeeManager.shiftReplacement(shift.getBranch(), shift, employeeId, newEmployeeId); // add getBranch to
                                                                                                   // the map
        } catch (Exception e) {
            throw new Exception("Failed to replace shift: " + e.getMessage());
        }
    }

    // public String createShift(LocalDate date, ShiftType shiftType, int
    // empManagerId, int shiftManagerId) {
    // if (!isEmployeeManager(empManagerId)) {
    // return "this action is allowed only for employee manager";
    // }
    // if (!isLoggedIn(empManagerId)) {
    // return "You are not logged in";
    // }
    // EmployeeManager employeeManager = getEmployeeManager(empManagerId);
    // return employeeManager.createShift(date, shiftType, shiftManagerId);
    // }
    public void autoCreateShiftsForNextWeek(int empManagerId, LocationDL branch) throws Exception {
        if (!isEmployeeManager(empManagerId)) {
            throw new Exception("Only employee managers can create shifts.");
        }
        if (!isLoggedIn(empManagerId)) {
            throw new Exception("You must be logged in.");
        }

        EmployeeManager manager = getEmployeeManager();
        LocalDate nextSunday = LocalDate.now().with(TemporalAdjusters.next(DayOfWeek.SUNDAY));

        for (int i = 0; i < 6; i++) {
            LocalDate date = nextSunday.plusDays(i);
            for (ShiftType type : ShiftType.values()) {
                try {
                    manager.createDefaultShift(branch, date, type);
                } catch (Exception e) {
                    if (!e.getMessage().contains("already exists")) {
                        System.out.println("Failed to create shift for " + date + " " + type + ": " + e.getMessage());
                    }
                }
            }
        }
    }

    public void addEmployeeToShift(int employeeId, Shift shift, Role role, int empManagerId) throws Exception {
        if (!isEmployeeManager(empManagerId)) {
            throw new Exception("This action is allowed only for employee managers.");
        }
        if (!isLoggedIn(empManagerId)) {
            throw new Exception("You must be logged in.");
        }
        EmployeeManager employeeManager = getEmployeeManager();
        try {
            employeeManager.addEmployeeToShift(shift.getBranch(), employeeId, shift, role);
        } catch (Exception e) {
            throw new Exception("Failed to add employee to shift: " + e.getMessage());
        }
    }

    public void removeEmployeeFromShift(int employeeId, Shift shift, int empManagerId) throws Exception {
        if (!isEmployeeManager(empManagerId)) {
            throw new Exception("This action is allowed only for employee managers.");
        }
        if (!isLoggedIn(empManagerId)) {
            throw new Exception("You are not logged in");
        }
        EmployeeManager employeeManager = getEmployeeManager();
        try {
            employeeManager.removeEmployeeFromShift(employeeId, shift);
        } catch (Exception e) {
            throw new Exception("Failed to remove employee from shift: " + e.getMessage());
        }
    }

    public String getEmployeeInfo(int shiftManagerId, Shift shift) { // for shift manager OR shift employee
        if (!isLoggedIn(shiftManagerId)) {
            return "You are not logged in";
        }
        ShiftEmployee shiftEmployee = shiftEmployees.get(shiftManagerId);
        return shift.getEmployeesInfo();
    }

    public void getShiftInfo(int id, Shift shift) throws Exception { // for shift manager OR shift employee
        if (!isLoggedIn(id)) {
            throw new Exception("You are not logged in");
        }
        System.out.println("Shift Information: " + shift.toString());
    }

    // public String addEmployee(int employeeId, Shift shift, int empManagerId, Role
    // role) {
    // if (!isEmployeeManager(empManagerId)) {
    // return "this action is allowed only for employee manager";
    // }
    // if (!isLoggedIn(empManagerId)) {
    // return "You are not logged in";
    // }
    // return shift.addEmployee(employeeId, role);
    // }
    // public String removeEmployee(int employeeId, Shift shift, int empManagerId) {
    // if (!isEmployeeManager(empManagerId)) {
    // return "this action is allowed only for employee manager";
    // }
    // if (!isLoggedIn(empManagerId)) {
    // return "You are not logged in";
    // }
    // return shift.removeEmployee(employeeId);
    // }
    public void setRequiredRoles(int empManagerId, Shift shift, Role role, int num) throws Exception {
        if (!isEmployeeManager(empManagerId)) {
            throw new Exception("This action is allowed only for employee managers.");
        }
        if (!isLoggedIn(empManagerId)) {
            throw new Exception("You must be logged in.");
        }
        if (num < 0) {
            throw new Exception("Number of employees cannot be negative.");
        }
        try {
            shift.setRequiredRoles(role, num);
        } catch (Exception e) {
            throw new Exception("Failed to set required roles: " + e.getMessage());
        }
    }

    public String getShiftString(int empManagerId, Shift shift) {
        if (!isLoggedIn(empManagerId)) {
            return "You are not logged in";
        }
        return shift.getShiftString();
    }

    public void setShiftManager(int empManagerId, Shift shift, int id) throws Exception {
        if (!isEmployeeManager(empManagerId)) {
            throw new Exception("This action is allowed only for employee managers.");
        }
        if (!isLoggedIn(empManagerId)) {
            throw new Exception("You must be logged in.");
        }
        if (!shift.setShiftManagerId(id)) {
            throw new Exception("Shift Manager ID is invalid. ");
        }
        try {
            addAssignedShift(id, shift, Role.SHIFT_MANAGER);
        } catch (Exception e) {
            throw new Exception("Failed to set shift manager: " + e.getMessage());
        }
    }

    public void addPreferredShift(int id, Shift shift) throws Exception {
        ShiftEmployee shiftEmployee = shiftEmployees.get(id);
        try {
            shiftEmployee.addPreferredShift(shift);
        } catch (Exception e) {
            throw new Exception("Failed to add preferred shift: " + e.getMessage());
        }
    }

    public void removePreferredShift(int id, Shift shift) throws Exception {
        ShiftEmployee shiftEmployee = shiftEmployees.get(id);
        try {
            shiftEmployee.removePreferredShift(shift);
        } catch (Exception e) {
            throw new Exception("Failed to remove preferred shift: " + e.getMessage());
        }
    }

    public void addAssignedShift(int id, Shift shift, Role role) throws Exception {
        if (role == null) {
            throw new Exception("Role cannot be null.");
        }
        if (shift == null) {
            throw new Exception("Shift cannot be null.");
        }
        ShiftEmployee shiftEmployee = shiftEmployees.get(id);
        try {
            shiftEmployee.addAssignedShift(shift, role);
        } catch (Exception e) {
            throw new Exception("Failed to add assigned shift: " + e.getMessage());
        }
    }

    // public String removeAssignedShift(int id, Shift shift) {
    // ShiftEmployee shiftEmployee = shiftEmployees.get(id);
    // return shiftEmployee.removeAssignedShift(shift);
    // }
    public boolean isAvailable(int id, Shift shift) throws Exception {
        ShiftEmployee shiftEmployee = shiftEmployees.get(id);
        if (shiftEmployee == null) {
            throw new Exception("Shift employee not found.");
        }
        try {
            return shiftEmployee.isAvailable(shift);
        } catch (Exception e) {
            throw new Exception("Failed to check availability: " + e.getMessage());
        }
    }

    public void getAssignedEmployeesInfo(int managerId, int e, Shift shift) { // for shift manager OR shift employee
        ShiftEmployee shiftEmployee = shiftEmployees.get(managerId);
        shift.getEmployeesInfo();
    }

    public Map<LocalDate, Shift> getPastShifts(int empManagerId) throws Exception {
        if (!isLoggedIn(empManagerId)) {
            throw new Exception("You are not logged in");
        }
        EmployeeManager employeeManager = getEmployeeManager();
        return employeeManager.getPastShifts();
    }

    public void setTimes(int empManagerId, Shift shift, int startTime, int endTime) throws Exception {
        if (!isEmployeeManager(empManagerId)) {
            throw new Exception("This action is allowed only for employee manager.");
        }
        if (!isLoggedIn(empManagerId)) {
            throw new Exception("You are not logged in.");
        }
        EmployeeManager employeeManager = getEmployeeManager();
        try {
            employeeManager.setTimes(shift, startTime, endTime);
        } catch (Exception e) {
            throw new Exception("Failed to set times: " + e.getMessage());
        }
    }

    public ShiftType parseShiftType(String type) throws Exception {
        try {
            return ShiftType.valueOf(type.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new Exception("Invalid shift type: " + type);
        }
    }

    public boolean checkPendingShipment(LocationDL location, LocalDate date, ShiftType type) {
        return employeeManager.checkPendingShipment(location, date, type);
    }

    // just for Main
    public void addFirstEmployeeManager(int id, String name, LocationDL branch, String bankAccount, int salary,
            LocalDate startDate, int vacationDays, int sickDays,
            double educationFund, double socialBenefits, String password) {
        employeeManager = new EmployeeManager(id, name, bankAccount, salary, startDate,
                vacationDays, sickDays, educationFund, socialBenefits, password);
        // employeeManagers.put(id, manager);
        // repository.addEmployee(manager);
    }

    public List<LocationDL> getBranches() {
        return branches;
    }
}
