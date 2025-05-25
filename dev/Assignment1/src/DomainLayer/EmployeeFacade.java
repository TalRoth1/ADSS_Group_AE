package DomainLayer;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.temporal.TemporalAdjusters;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class EmployeeFacade { //employee related methods

    private Map<Integer, EmployeeManager> employeeManagers;
    private Map<Integer, ShiftEmployee> shiftEmployees;

    //initalize employees
    EmployeeManager keren = new EmployeeManager(100, "Keren", "1", "111222", 7000,
            LocalDate.of(2025, 4, 10), 20, 5, 100, 100, "123");
    ShiftEmployee Liat = new ShiftEmployee(101, "Liat", "1", "111222", 7000, LocalDate.of(2025, 4, 10), 20, 5, 100, 100, "password", Role.SHIFT_MANAGER);
    ShiftEmployee Erez = new ShiftEmployee(102, "Erez", "1", "111222", 7000, LocalDate.of(2025, 4, 10), 20, 5, 100, 100, "password", Role.CASHIER);
    ShiftEmployee Elad = new ShiftEmployee(103, "Elad", "1", "111222", 7000, LocalDate.of(2025, 4, 10), 20, 5, 100, 100, "password", Role.DRIVER);
    ShiftEmployee Eylon = new ShiftEmployee(104, "Eylon", "1", "111222", 7000, LocalDate.of(2025, 4, 10), 20, 5, 100, 100, "password", Role.DRIVER);
    ShiftEmployee Tal = new ShiftEmployee(105, "Tal", "1", "111222", 7000, LocalDate.of(2025, 4, 10), 20, 5, 100, 100, "password", Role.CASHIER);
    ShiftEmployee Ofir = new ShiftEmployee(106, "Ofir", "1", "111222", 7000, LocalDate.of(2025, 4, 10), 20, 5, 100, 100, "password", Role.STORE_KEEPER);
    ShiftEmployee Kiril = new ShiftEmployee(107, "Kiril", "1", "111222", 7000, LocalDate.of(2025, 4, 10), 20, 5, 100, 100, "password", Role.STORE_KEEPER);
    ShiftEmployee Ofri = new ShiftEmployee(108, "Ofri", "1", "111222", 7000, LocalDate.of(2025, 4, 10), 20, 5, 100, 100, "password", Role.SHIFT_MANAGER);

    public EmployeeFacade() {
        this.employeeManagers = new HashMap<>();
        this.shiftEmployees = new HashMap<>();

        // Add the employee manager
        employeeManagers.put(keren.getId(), keren);
        // Add shift employees
        shiftEmployees.put(Liat.getId(), Liat);
        shiftEmployees.put(Erez.getId(), Erez);
        shiftEmployees.put(Elad.getId(), Elad);
        shiftEmployees.put(Eylon.getId(), Eylon);
        shiftEmployees.put(Tal.getId(), Tal);
        shiftEmployees.put(Ofir.getId(), Ofir);
        shiftEmployees.put(Kiril.getId(), Kiril);
        shiftEmployees.put(Ofri.getId(), Ofri);

        keren.addEmployee(Liat);
        keren.addEmployee(Erez);
        keren.addEmployee(Elad);
        keren.addEmployee(Eylon);
        keren.addEmployee(Tal);
        keren.addEmployee(Ofir);
        keren.addEmployee(Kiril);
        keren.addEmployee(Ofri);
    }

    public Employee login(int id, String password) throws Exception {
        Employee e = getEmployee(id);
        if (e == null) {
            throw new Exception("Employee not found.");
        }
        if (!e.login(password)) {
            throw new Exception("Invalid password or employee cannot log in.");
        }
        return e;
    }

    public String logout(int id) {
        if (!isLoggedIn(id)) {
            return "You are not logged in";
        }
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
        if (!isEmployeeManager(empManagerId)) {
            return "this action is allowed only for employee manager";
        }
        if (!isLoggedIn(empManagerId)) {
            return "You are not logged in";
        }
        EmployeeManager employeeManager = getEmployeeManager(empManagerId);
        ShiftEmployee shiftEmployee = shiftEmployees.get(employeeId);
        return employeeManager.removeEmployee(employeeId);
    }

    public String hireEmployee(int employeeId, int empManagerId, String branch, String employeeName, String bankAccount, int salary, LocalDate startDate, int vacationDays, int sickDays, double educationFund, double socialBenefits, String employeePassword, Role role) {
        if (!isEmployeeManager(empManagerId)) {
            return "this action is allowed only for employee manager";
        }
        if (!isLoggedIn(empManagerId)) {
            return "You are not logged in";
        }
        EmployeeManager employeeManager = getEmployeeManager(empManagerId);
        if (employeeManager.checkEmployee(employeeId)) {
            return "Employee: " + employeeId + " already hired";
        }
        ShiftEmployee shiftEmployee = employeeManager.hireEmployee(employeeId, employeeName, branch, bankAccount, salary, startDate, vacationDays, sickDays, educationFund, socialBenefits, employeePassword, role);
        //ShiftEmployee shiftEmployee = shiftEmployees.get(employeeId);

        shiftEmployees.put(employeeId, shiftEmployee);
        return "Employee: " + employeeId + " hired successfully";
    }

    public String fireEmployee(int employeeId, int empManagerId) {
        if (!isEmployeeManager(empManagerId)) {
            return "this action is allowed only for employee manager";
        }
        if (!isLoggedIn(empManagerId)) {
            return "You are not logged in";
        }
        EmployeeManager employeeManager = getEmployeeManager(empManagerId);
        return employeeManager.fireEmployee(employeeId);
    }

    public String changeRoleToEmployee(int employeeId, int empManagerId, Role oldRole, Role newRole) {
        if (!isEmployeeManager(empManagerId)) {
            return "this action is allowed only for employee manager";
        }
        if (!isLoggedIn(empManagerId)) {
            return "You are not logged in";
        }
        EmployeeManager employeeManager = getEmployeeManager(empManagerId);
        return employeeManager.changeRoleToEmployee(employeeId, oldRole, newRole);
    }

    public String addRoleToEmployee(int employeeId, int empManagerId, Role newRole) {
        if (!isEmployeeManager(empManagerId)) {
            return "this action is allowed only for employee manager";
        }
        if (!isLoggedIn(empManagerId)) {
            return "You are not logged in";
        }
        EmployeeManager employeeManager = getEmployeeManager(empManagerId);
        return employeeManager.addRoleToEmployee(employeeId, newRole);
    }

    public String deleteRoleFromEmployee(int employeeId, int empManagerId, Role roleToDelete) {
        if (!isEmployeeManager(empManagerId)) {
            return "this action is allowed only for employee manager";
        }
        if (!isLoggedIn(empManagerId)) {
            return "You are not logged in";
        }
        EmployeeManager employeeManager = getEmployeeManager(empManagerId);
        return employeeManager.deleteRoleFromEmployee(employeeId, roleToDelete);
    }

    //update methods
    public String updateSalary(int employeeId, int empManagerId, int salary) {
        if (!isEmployeeManager(empManagerId)) {
            return "this action is allowed only for employee manager";
        }
        if (!isLoggedIn(empManagerId)) {
            return "You are not logged in";
        }
        EmployeeManager employeeManager = getEmployeeManager(empManagerId);
        return employeeManager.updateSalaryEmployee(employeeId, salary);
    }

    public String updateBankAccount(int employeeId, int empManagerId, String bankAccount) {
        if (!isEmployeeManager(empManagerId)) {
            return "this action is allowed only for employee manager";
        }
        if (!isLoggedIn(empManagerId)) {
            return "You are not logged in";
        }
        EmployeeManager employeeManager = getEmployeeManager(empManagerId);
        return employeeManager.updateBankAccountEmployee(employeeId, bankAccount);
    }

    public String updateVacationDays(int employeeId, int empManagerId, int vacationDays) {
        if (!isEmployeeManager(empManagerId)) {
            return "this action is allowed only for employee manager";
        }
        if (!isLoggedIn(empManagerId)) {
            return "You are not logged in";
        }
        EmployeeManager employeeManager = getEmployeeManager(empManagerId);
        return employeeManager.updateVacationDaysEmployee(employeeId, vacationDays);
    }

    public String updateSickDays(int employeeId, int empManagerId, int sickDays) {
        if (!isEmployeeManager(empManagerId)) {
            return "this action is allowed only for employee manager";
        }
        if (!isLoggedIn(empManagerId)) {
            return "You are not logged in";
        }
        EmployeeManager employeeManager = getEmployeeManager(empManagerId);
        return employeeManager.updateSickDaysEmployee(employeeId, sickDays);
    }

    public String updateEducationFund(int employeeId, int empManagerId, double educationFund) {
        if (!isEmployeeManager(empManagerId)) {
            return "this action is allowed only for employee manager";
        }
        if (!isLoggedIn(empManagerId)) {
            return "You are not logged in";
        }
        EmployeeManager employeeManager = getEmployeeManager(empManagerId);
        return employeeManager.updateEducationFund(employeeId, educationFund);
    }

    public String updateSocialBenefits(int employeeId, int empManagerId, double socialBenefits) {
        if (!isEmployeeManager(empManagerId)) {
            return "this action is allowed only for employee manager";
        }
        if (!isLoggedIn(empManagerId)) {
            return "You are not logged in";
        }
        EmployeeManager employeeManager = getEmployeeManager(empManagerId);
        return employeeManager.updateSocialBenefits(employeeId, socialBenefits);
    }

    public Employee getEmployee(int id) {
        Employee employee = employeeManagers.get(id);
        if (employee == null) {
            employee = shiftEmployees.get(id);
        }
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
        if (shift == null) {
            return "Shift is null";
        }
        if (!isEmployeeManager(empManagerId)) {
            return "this action is allowed only for employee manager";
        }
        if (!isLoggedIn(empManagerId)) {
            return "You are not logged in";
        }

        EmployeeManager employeeManager = getEmployeeManager(empManagerId);
        //EmployeeManager employeeManager = getEmployeeManager(shift.getShiftManagerId());
        return employeeManager.getAvailableEmployees(shift, role);
    }

    // shift employee methods
    public String getPrefAllEmployees(int empManagerId) throws Exception { //get all employees' preferences, for employee manager
        if (!isEmployeeManager(empManagerId)) {
            throw new Exception("This action is allowed only for employee managers.");
        }
        if (!isLoggedIn(empManagerId)) {
            throw new Exception("You are not logged in.");
        }
        EmployeeManager employeeManager = getEmployeeManager(empManagerId);
        return employeeManager.getPrefAllEmployees();
    }

    public String getPreferredShiftsEmployee(int employeeId) { //employee's preferred shifts for shift employee only
        if (!isLoggedIn(employeeId)) {
            return "You are not logged in";
        }
        ShiftEmployee shiftEmployee = shiftEmployees.get(employeeId);
        return shiftEmployee.getPreferredShiftsToString();
    }

    public String getAssignedEmployeeShiftsEmployee(int employeeId) { //employee's assigned shifts for shift employee only
        if (!isLoggedIn(employeeId)) {
            return "You are not logged in";
        }
        ShiftEmployee shiftEmployee = shiftEmployees.get(employeeId);
        return shiftEmployee.getAssignedShiftsToString();
    }

    public String getAssignedEmployeeShiftsManager(int employeeId, int empManagerId) { //employee's assigned shifts for shift employee only
        if (!isEmployeeManager(empManagerId)) {
            return "this action is allowed only for employee manager";
        }
        if (!isLoggedIn(empManagerId)) {
            return "You are not logged in";
        }
        EmployeeManager employeeManager = getEmployeeManager(empManagerId);
        return employeeManager.getAssignedEmployeeShiftsManager(employeeId);
    }

    public String getAssignedEmployeeShiftsAsShiftManager(int employeeId, int shiftManagerId) { //employee's assigned shifts for shift employee only
        if (!isShiftManager(shiftManagerId)) {
            return "this action is allowed only for shift manager";
        }
        if (!isLoggedIn(shiftManagerId)) {
            return "You are not logged in";
        }
        EmployeeManager anyManager = getAnyEmployeeManager();
        if (anyManager == null) {
            return "No employee manager available to process request.";
        }

        return anyManager.getAssignedEmployeeShiftsManager(employeeId);
    }

    private EmployeeManager getAnyEmployeeManager() {
        return employeeManagers.values().stream().findFirst().orElse(null);
    }

    public String addRole(int id, Role role) {
        ShiftEmployee shiftEmployee = shiftEmployees.get(id);
        return shiftEmployee.addRole(role);
    }

    public String removeRole(int id, Role role) {
        ShiftEmployee shiftEmployee = shiftEmployees.get(id);
        return shiftEmployee.removeRole(role);
    }

    public String changeRole(int id, Role oldRole, Role newRole) {
        ShiftEmployee shiftEmployee = shiftEmployees.get(id);
        return shiftEmployee.changeRole(oldRole, newRole);
    }

    //shift methods
    public Shift getShift(LocalDate date, ShiftType shiftType, int id) throws Exception {
        if (!isLoggedIn(id)) {
            throw new Exception("You must be logged in.");
        }
        EmployeeManager employeeManager = getEmployeeManager(id);
        return employeeManager.getShift(date, shiftType);
    }

    public Shift getShiftForEmployee(LocalDate date, ShiftType shiftType) {
        for (EmployeeManager manager : employeeManagers.values()) {
            Shift shift = manager.getShift(date, shiftType);
            if (shift != null) {
                return shift;
            }
        }
        return null;
    }

    public String changeShiftManager(Shift shift, int oldShiftManagerId, int newShiftManagerId, int empManagerId) {
        if (!isEmployeeManager(empManagerId)) {
            return "this action is allowed only for employee manager";
        }
        if (!isLoggedIn(empManagerId)) {
            return "You are not logged in";
        }
        EmployeeManager employeeManager = getEmployeeManager(empManagerId);
        return employeeManager.changeShiftManager(shift, oldShiftManagerId, newShiftManagerId);
    }

    public String shiftReplacement(Shift shift, int employeeId, int empManagerId, int newEmployeeId) {
        if (!isEmployeeManager(empManagerId)) {
            return "this action is allowed only for employee manager";
        }
        if (!isLoggedIn(empManagerId)) {
            return "You are not logged in";
        }
        EmployeeManager employeeManager = getEmployeeManager(empManagerId);
        return employeeManager.shiftReplacement(shift, employeeId, newEmployeeId);
    }

    public String createShift(LocalDate date, ShiftType shiftType, int empManagerId, int shiftManagerId) {
        if (!isEmployeeManager(empManagerId)) {
            return "this action is allowed only for employee manager";
        }
        if (!isLoggedIn(empManagerId)) {
            return "You are not logged in";
        }
        EmployeeManager employeeManager = getEmployeeManager(empManagerId);
        return employeeManager.createShift(date, shiftType, shiftManagerId);
    }

    public void autoCreateShiftsForNextWeek(int empManagerId) throws Exception {
        if (!isEmployeeManager(empManagerId)) {
            throw new Exception("Only employee managers can create shifts.");
        }
        if (!isLoggedIn(empManagerId)) {
            throw new Exception("You must be logged in.");
        }

        EmployeeManager manager = getEmployeeManager(empManagerId);
        LocalDate nextSunday = LocalDate.now().with(TemporalAdjusters.next(DayOfWeek.SUNDAY));

        for (int i = 0; i < 6; i++) {
            LocalDate date = nextSunday.plusDays(i);
            for (ShiftType type : ShiftType.values()) {
                try {
                    manager.createDefaultShift(date, type);
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
        EmployeeManager employeeManager = getEmployeeManager(empManagerId);
        String response = employeeManager.addEmployeeToShift(employeeId, shift, role);
        if (response.equals("employee not exist.")) {
            throw new Exception("Failed to add employee to shift: Employee does not exist.");
        }
        if (response.equals("this employee is fired.")) {
            throw new Exception("Failed to add employee to shift: Employee is fired and cannot be added.");
        }
        if (response.equals("this employee does not have this role.")) {
            throw new Exception("Failed to add employee to shift: Employee does not have the required role.");
        }
        if (response.equals("this employee is already assigned to this shift.")) {
            throw new Exception("Failed to add employee to shift: Employee already has this shift assigned.");
        }
        if (response.equals("Role not required for this shift.")) {
            throw new Exception("Failed to add employee to shift: Role not required for this shift.");
        }
        if (response.equals("No more employees required for this role.")) {
            throw new Exception("Failed to add employee to shift: No more employees required for this role.");
        }
    }

    public String removeEmployeeFromShift(int employeeId, Shift shift, int empManagerId) {
        if (!isEmployeeManager(empManagerId)) {
            return "this action is allowed only for employee manager";
        }
        if (!isLoggedIn(empManagerId)) {
            return "You are not logged in";
        }
        EmployeeManager employeeManager = getEmployeeManager(empManagerId);
        return employeeManager.removeEmployeeFromShift(employeeId, shift);
    }

    public String getEmployeeInfo(int shiftManagerId, Shift shift) { //for shift manager OR shift employee
        if (!isLoggedIn(shiftManagerId)) {
            return "You are not logged in";
        }
        ShiftEmployee shiftEmployee = shiftEmployees.get(shiftManagerId);
        return shift.getEmployeesInfo();
    }

    public String getShiftInfo(int id, Shift shift) { //for shift manager OR shift employee
        if (!isLoggedIn(id)) {
            return "You are not logged in";
        }
        return shift.toString();
    }

    public String addEmployee(int employeeId, Shift shift, int empManagerId, Role role) {
        if (!isEmployeeManager(empManagerId)) {
            return "this action is allowed only for employee manager";
        }
        if (!isLoggedIn(empManagerId)) {
            return "You are not logged in";
        }
        return shift.addEmployee(employeeId, role);
    }

    public String removeEmployee(int employeeId, Shift shift, int empManagerId) {
        if (!isEmployeeManager(empManagerId)) {
            return "this action is allowed only for employee manager";
        }
        if (!isLoggedIn(empManagerId)) {
            return "You are not logged in";
        }
        return shift.removeEmployee(employeeId);
    }

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
        if (!shift.setRequiredRoles(role, num)) {
            throw new Exception("Role not required for this shift.");
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

    public String addPreferredShift(int id, Shift shift) {
        ShiftEmployee shiftEmployee = shiftEmployees.get(id);
        return shiftEmployee.addPreferredShift(shift);
    }

    public String removePreferredShift(int id, Shift shift) {
        ShiftEmployee shiftEmployee = shiftEmployees.get(id);
        return shiftEmployee.removePreferredShift(shift);
    }

    public void addAssignedShift(int id, Shift shift, Role role) throws Exception {
        if (role == null) {
            throw new Exception("Role cannot be null.");
        }
        if (shift == null) {
            throw new Exception("Shift cannot be null.");
        }
        ShiftEmployee shiftEmployee = shiftEmployees.get(id);
        String res = shiftEmployee.addAssignedShift(shift, role);
        if (res.equals("role not found")) {
            throw new Exception("Failed to add assigned shift: " + "Role does not exist in the list of roles.");
        }
        if (res.equals("finish working")) {
            throw new Exception("Failed to add assigned shift: " + "Employee has finished working and cannot add assigned shifts.");
        }
        if (res.equals("shift already assigned")) {
            throw new Exception("Failed to add assigned shift: " + "Shift already exists in the list of assigned shifts.");
        }
    }

    public String removeAssignedShift(int id, Shift shift) {
        ShiftEmployee shiftEmployee = shiftEmployees.get(id);
        return shiftEmployee.removeAssignedShift(shift);
    }

    public boolean isAvailable(int id, Shift shift) {
        ShiftEmployee shiftEmployee = shiftEmployees.get(id);
        return shiftEmployee != null && shiftEmployee.isAvailable(shift);

    }

    public void getAssignedEmployeesInfo(int managerId, int e, Shift shift) { //for shift manager OR shift employee
        ShiftEmployee shiftEmployee = shiftEmployees.get(managerId);
        shift.getEmployeesInfo();
    }

    public Map<LocalDate, Shift> getPastShifts(int empManagerId) {
        if (!isLoggedIn(empManagerId)) {
            return Collections.emptyMap();
        }
        EmployeeManager employeeManager = getEmployeeManager(empManagerId);
        return employeeManager.getPastShifts();
    }

    public String setTimes(int empManagerId, Shift shift, int startTime, int endTime) {
        if (!isEmployeeManager(empManagerId)) {
            return "this action is allowed only for employee manager";
        }
        if (!isLoggedIn(empManagerId)) {
            return "You are not logged in";
        }

        EmployeeManager employeeManager = getEmployeeManager(empManagerId);
        return employeeManager.setTimes(shift, startTime, endTime);
    }

    //just for Main
    public void addFirstEmployeeManager(int id, String name, String branch, String bankAccount, int salary,
            LocalDate startDate, int vacationDays, int sickDays,
            double educationFund, double socialBenefits, String password) {
        EmployeeManager manager = new EmployeeManager(id, name, branch, bankAccount, salary, startDate,
                vacationDays, sickDays, educationFund, socialBenefits, password);
        employeeManagers.put(id, manager);
        //  repository.addEmployee(manager);
    }
}
