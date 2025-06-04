package DomainLayer;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.temporal.TemporalAdjusters;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import DTO.DriverDTO;
import DTO.EmployeeDTO;
import DTO.LocationDTO;
import DataLayer.DriverController;
import DataLayer.EmployeeController;
import DataLayer.LocationController;
import DataLayer.Mappers.DriverMapper;
import DataLayer.Mappers.EmployeeMapper;
import DataLayer.Mappers.LocationMapper;
import DataLayer.Mappers.ShiftMapper;
import DataLayer.ShiftController;

public class EmployeeFacade {

    private EmployeeManager employeeManager;
    private Map<Integer, ShiftEmployee> shiftEmployees;
    private List<LocationDL> branches;
    private EmployeeController empController;
    private DriverController driverController;
    private LocationController locationController;
    private ShiftController shiftController;
    private EmployeeMapper employeeMapper;
    private DriverMapper driverMapper;
    private LocationMapper locationMapper;
    private ShiftMapper shiftMapper;

    public EmployeeFacade() {
        this.shiftEmployees = new HashMap<>();
        // this.branches = Collections.emptyList(); // Initialize branches as empty list
        this.branches = new ArrayList<>(); // Initialize branches as empty list
        this.locationController = new LocationController();
        this.shiftController = new ShiftController(locationController);
        this.empController = new EmployeeController(locationController, shiftController);
        this.driverController = new DriverController(empController);
        this.employeeMapper = new EmployeeMapper();
        this.driverMapper = new DriverMapper();
        this.locationMapper = new LocationMapper();
        this.shiftMapper = new ShiftMapper();
        // empController.setEmployeeMapper(employeeMapper);
        // driverController.setDriverMapper(driverMapper);
    }

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
        try {
            empController.openConnection();
            empController.login(id, password);
        } catch (Exception ex) {
            empController.closeConnection();
            throw new Exception("Login failed: " + ex.getMessage());
        } finally {
            empController.closeConnection();
        }
        return e;
    }

    public void logout(int id) throws Exception {
        if (!isLoggedIn(id)) {
            throw new Exception("You are not logged in.");
        }
        Employee e = getEmployee(id);
        try {
            empController.openConnection();
            empController.logout(id);
            e.logout();
        } catch (Exception ex) {
            throw new Exception("Logout failed: " + ex.getMessage());
        } finally {
            empController.closeConnection();
        }
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

    public DriverDL assignCheck(LocalDate sentDate, String shiftType, LocationDL origin, List<LocationDL> destinations,
            String licenceType, List<LocationDL> branches) {
        try {

            return employeeManager.assignCheck(sentDate, parseShiftType(shiftType),
                    origin, destinations, licenceType, branches, driverController);
        } catch (Exception e) {
            System.out.println("Error in assignCheck: " + e.getMessage());
            return null;
        }
    }

    public void hireEmployee(int employeeId, int empManagerId, LocationDL b, String employeeName,
            String bankAccount,
            int salary, LocalDate startDate, int vacationDays, int sickDays, float educationFund,
            float socialBenefits, String employeePassword, Role role) throws Exception {
        try {
            if (!isEmployeeManager(empManagerId)) {
                throw new Exception("This action is allowed only for employee manager.");
            }
            if (!isLoggedIn(empManagerId)) {
                throw new Exception("You are not logged in.");
            }
            if (branches == null || !branches.contains(b)) {
                throw new Exception("Branch not found.");
            }
            EmployeeManager employeeManager = getEmployeeManager();
            if (employeeManager.checkEmployee(employeeId)) {
                throw new Exception("Employee with ID " + employeeId + " already hired.");
            }

            ShiftEmployee shiftEmployee = employeeManager.hireEmployee(employeeId, employeeName, b, bankAccount,
                    salary, startDate, vacationDays, sickDays, educationFund, socialBenefits, employeePassword, role,
                    branches);

            try {
                empController.openConnection();
                empController.addEmployee(employeeMapper.toDTO(shiftEmployee), role.toString());
            } catch (Exception e) {
                empController.closeConnection();
                throw new Exception("Failed to add employee: " + e.getMessage());
            } finally {
                empController.closeConnection();
            }
            shiftEmployees.put(employeeId, shiftEmployee);

            System.out.println("Employee hired: " + shiftEmployee.getName() + " with ID: " + employeeId);
        } catch (Exception e) {
            throw e;
        }
    }

    public void hireDriver(int employeeId, int empManagerId, LocationDL b, String employeeName,
            String bankAccount, int salary, LocalDate startDate, int vacationDays, int sickDays,
            float educationFund, float socialBenefits, String employeePassword, ArrayList<String> licenceType)
            throws Exception {
        try {
            if (!isEmployeeManager(empManagerId)) {
                throw new Exception("This action is allowed only for employee manager.");
            }
            if (!isLoggedIn(empManagerId)) {
                throw new Exception("You are not logged in.");
            }
            if (branches == null || !branches.contains(b)) {
                throw new Exception("Branch not found.");
            }
            EmployeeManager employeeManager = getEmployeeManager();
            if (employeeManager.checkEmployee(employeeId)) {
                throw new Exception("Employee with ID " + employeeId + " already hired.");
            }

            DriverDL driver = employeeManager.hireDriver(employeeId, employeeName, b, bankAccount,
                    salary, startDate, vacationDays, sickDays, educationFund, socialBenefits,
                    employeePassword, licenceType, branches);

            try {
                empController.openConnection();
                driverController.openConnection();
                empController.addEmployee(driverMapper.toDTO(driver), Role.DRIVER.toString());
                driverController.addDriver(driverMapper.toDTO(driver));
            } catch (Exception e) {
                throw new Exception("Failed to add driver: " + e.getMessage());
            } finally {
                empController.closeConnection();
                driverController.closeConnection();
            }
            shiftEmployees.put(employeeId, driver);
            System.out.println("Driver hired: " + driver.getName() + " with ID: " + employeeId);
        } catch (Exception e) {
            throw e;
        }
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
            empController.openConnection();
            empController.removeEmployee(employeeId);
            employeeManager.fireEmployee(employeeId);
        } catch (Exception e) {
            throw new Exception("Failed to fire employee: " + e.getMessage());
        } finally {
            empController.closeConnection();
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
        ShiftEmployee employee = shiftEmployees.get(employeeId);
        if (employee == null) {
            throw new Exception("Employee not found.");
        }
        ShiftEmployee backup = (ShiftEmployee) employee.clone(); // backup before change

        try {
            empController.openConnection();
            empController.updateRole(employeeId, oldRole.toString(), newRole.toString());
            employeeManager.changeRoleToEmployee(employeeId, oldRole, newRole);
        } catch (Exception e) {
            shiftEmployees.put(employeeId, backup); // restore previous state
            throw new Exception("Failed to change role: " + e.getMessage());
        } finally {
            empController.closeConnection();
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
            empController.openConnection();
            empController.addRole(employeeId, newRole.toString());
            employeeManager.addRoleToEmployee(employeeId, newRole);
        } catch (Exception e) {
            throw new Exception("Failed to add role: " + e.getMessage());
        } finally {
            empController.closeConnection();
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
            empController.openConnection();
            empController.removeRole(employeeId, roleToDelete.toString());
            employeeManager.deleteRoleFromEmployee(employeeId, roleToDelete);
        } catch (Exception e) {
            throw new Exception("Failed to delete role: " + e.getMessage());
        } finally {
            empController.closeConnection();
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
        ShiftEmployee employee = shiftEmployees.get(employeeId);
        if (employee == null) {
            throw new Exception("Employee not found.");
        }
        ShiftEmployee backup = (ShiftEmployee) employee.clone();
        try {
            empController.openConnection();
            empController.updateEmployeeByField(employeeId, "salary", salary);
            employeeManager.updateSalaryEmployee(employeeId, salary);
            employee.setSalary(salary);
        } catch (Exception e) {
            shiftEmployees.put(employeeId, backup);
            throw new Exception("Failed to update salary: " + e.getMessage());
        } finally {
            empController.closeConnection();
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
        ShiftEmployee employee = shiftEmployees.get(employeeId);
        if (employee == null) {
            throw new Exception("Employee not found.");
        }
        ShiftEmployee backup = (ShiftEmployee) employee.clone();

        try {
            empController.openConnection();
            empController.updateEmployeeByField(employeeId, "bankAccount", bankAccount);
            employeeManager.updateBankAccountEmployee(employeeId, bankAccount);
            employee.setBankAccount(bankAccount);
        } catch (Exception e) {
            shiftEmployees.put(employeeId, backup);
            throw new Exception("Failed to update bank account: " + e.getMessage());
        } finally {
            empController.closeConnection();
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
        ShiftEmployee employee = shiftEmployees.get(employeeId);
        if (employee == null) {
            throw new Exception("Employee not found.");
        }
        ShiftEmployee backup = (ShiftEmployee) employee.clone();

        try {
            empController.openConnection();
            empController.updateEmployeeByField(employeeId, "vacationDays", vacationDays);
            employeeManager.updateVacationDaysEmployee(employeeId, vacationDays);
            employee.setVacationDays(vacationDays);
        } catch (Exception e) {
            shiftEmployees.put(employeeId, backup);
            throw new Exception("Failed to update vacation days: " + e.getMessage());
        } finally {
            empController.closeConnection();
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
        ShiftEmployee employee = shiftEmployees.get(employeeId);
        if (employee == null) {
            throw new Exception("Employee not found.");
        }
        ShiftEmployee backup = (ShiftEmployee) employee.clone();

        try {
            empController.openConnection();
            empController.updateEmployeeByField(employeeId, "sickDays", sickDays);
            employeeManager.updateSickDaysEmployee(employeeId, sickDays);
            employee.setSickDays(sickDays);
        } catch (Exception e) {
            shiftEmployees.put(employeeId, backup);
            throw new Exception("Failed to update sick days: " + e.getMessage());
        } finally {
            empController.closeConnection();
        }
    }

    public void updateEducationFund(int employeeId, int empManagerId, float educationFund) throws Exception {
        if (!isEmployeeManager(empManagerId)) {
            throw new Exception("This action is allowed only for employee manager.");
        }
        if (!isLoggedIn(empManagerId)) {
            throw new Exception("You are not logged in.");
        }
        EmployeeManager employeeManager = getEmployeeManager();
        ShiftEmployee employee = shiftEmployees.get(employeeId);
        if (employee == null) {
            throw new Exception("Employee not found.");
        }
        ShiftEmployee backup = (ShiftEmployee) employee.clone();

        try {
            empController.openConnection();
            empController.updateEmployeeByField(employeeId, "educationFund", educationFund);
            employeeManager.updateEducationFund(employeeId, educationFund);
            employee.setEducationFund(educationFund);
        } catch (Exception e) {
            shiftEmployees.put(employeeId, backup);
            throw new Exception("Failed to update education fund: " + e.getMessage());
        } finally {
            empController.closeConnection();
        }
    }

    public void updateSocialBenefits(int employeeId, int empManagerId, float socialBenefits) throws Exception {
        if (!isEmployeeManager(empManagerId)) {
            throw new Exception("This action is allowed only for employee manager.");
        }
        if (!isLoggedIn(empManagerId)) {
            throw new Exception("You are not logged in.");
        }
        EmployeeManager employeeManager = getEmployeeManager();
        ShiftEmployee employee = shiftEmployees.get(employeeId);
        if (employee == null) {
            throw new Exception("Employee not found.");
        }
        ShiftEmployee backup = (ShiftEmployee) employee.clone();

        try {
            empController.openConnection();
            empController.updateEmployeeByField(employeeId, "socialBenefits", socialBenefits);
            employeeManager.updateSocialBenefits(employeeId, socialBenefits);
            employee.setSocialBenefits(socialBenefits);
        } catch (Exception e) {
            shiftEmployees.put(employeeId, backup);
            throw new Exception("Failed to update social benefits: " + e.getMessage());
        } finally {
            empController.closeConnection();
        }
    }

    // maybe need to call the controller to get it from the DB?
    public Employee getEmployee(int id) {
        ShiftEmployee shiftEmployee = shiftEmployees.get(id);
        if (shiftEmployee != null) {
            return shiftEmployee;
        }
        if (employeeManager != null && employeeManager.getId() == id) {
            return employeeManager;
        }
        return null;
    }

    private boolean isLoggedIn(int id) {
        return getEmployee(id).isLoggedIn();
    }

    public EmployeeManager getEmployeeManager() {
        return employeeManager;
    }

    public void setEmployeeManager(EmployeeManager employeeManager) {
        this.employeeManager = employeeManager;
    }

    public void getAvailableEmployees(int empManagerId, LocationDL branch, Shift shift, Role role) throws Exception { // get
        // available
        // employees
        // for
        // a
        // shift
        // with
        // this
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
            employeeManager.changeShiftManager(shift, oldShiftManagerId, newShiftManagerId);
            shiftController.openConnection();
            shiftController.updateShift(shiftMapper.toDTO(shift), "shiftManagerId", newShiftManagerId);
        } catch (Exception e) {
            throw new Exception("Failed to change shift manager: " + e.getMessage());
        } finally {
            shiftController.closeConnection();
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
            Role role = shift.getAssignedEmployeesID().get(employeeId);
            employeeManager.shiftReplacement(shift.getBranch(), shift, employeeId, newEmployeeId);
            ShiftEmployee em = employeeManager.getAllEmployeesInBranch(shift.getBranch()).get(employeeId);
            shiftController.openConnection();
            shiftController.addShiftAssigned(EmployeeMapper.toDTO(em), shiftMapper.toDTO(shift), role.toString());
        } catch (Exception e) {
            throw new Exception("Failed to replace shift: " + e.getMessage());
        } finally {
            shiftController.closeConnection();
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
            System.out.println("Creating shifts for date: " + date);
            for (ShiftType type : ShiftType.values()) {
                try {
                    manager.createDefaultShift(branch, date, type);
                    shiftController.openConnection();
                    shiftController.addShift(shiftMapper.toDTO(manager.getShift(branch, date, type)));
                } catch (Exception e) {
                    if (!e.getMessage().contains("already exists")) {
                        System.out.println("Failed to create shift for " + date + " " + type + ": " + e.getMessage());
                    }
                } finally {
                    shiftController.closeConnection();
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
            shiftController.openConnection();
            shiftController.addShiftAssigned(employeeMapper.toDTO(shiftEmployees.get(employeeId)),
                    shiftMapper.toDTO(shift), role.toString());
            shiftController.updateShiftReqRoles(shiftMapper.toDTO(shift), role.toString(),
                    shift.getRequiredRoles().get(role));
            // no need of -1 because employeeManager.addEmployeeToShift already does that
        } catch (Exception e) {
            throw new Exception("Failed to add employee to shift: " + e.getMessage());
        } finally {
            shiftController.closeConnection();
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
            shiftController.openConnection();
            employeeManager.removeEmployeeFromShift(employeeId, shift);
            shiftController.deleteShiftAssigned(employeeMapper.toDTO(shiftEmployees.get(employeeId)),
                    shiftMapper.toDTO(shift));
            String role = shift.getAssignedEmployeesID().get(employeeId).toString();
            shiftController.updateShiftReqRoles(shiftMapper.toDTO(shift),
                    role, shift.getRequiredRoles().get(role));
        } catch (Exception e) {
            throw new Exception("Failed to remove employee from shift: " + e.getMessage());
        } finally {
            shiftController.closeConnection();
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
            shiftController.openConnection();
            shiftController.updateShiftReqRoles(shiftMapper.toDTO(shift), role.toString(), num);
        } catch (Exception e) {
            throw new Exception("Failed to set required roles: " + e.getMessage());
        } finally {
            shiftController.closeConnection();
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
        int managerIdToAssign = id;
        // If id == 0, find an existing shift manager
        if (id == 0) {
            boolean found = false;
            for (ShiftEmployee employee : shiftEmployees.values()) {
                if (employee.getRoles().contains(Role.SHIFT_MANAGER)) {
                    managerIdToAssign = employee.getId();
                    found = true;
                    break;
                }
            }
            if (!found) {
                throw new Exception("No shift manager available to assign.");
            }
        }

        if (!shift.setShiftManagerId(managerIdToAssign)) {
            throw new Exception("Shift Manager ID is invalid. ");
        }
        try {
            addAssignedShift(managerIdToAssign, shift, Role.SHIFT_MANAGER);
            ShiftEmployee emp = employeeManager.getEmployee(id);
            // shiftController.addShiftAssigned(employeeMapper.toDTO(emp),
            // shiftMapper.toDTO(shift),
            // Role.SHIFT_MANAGER.toString());
            shiftController.openConnection();
            shiftController.updateShiftReqRoles(shiftMapper.toDTO(shift), Role.SHIFT_MANAGER.toString(), 0);
        } catch (Exception e) {
            throw new Exception("Failed to set shift manager: " + e.getMessage());
        } finally {
            shiftController.closeConnection();
        }
    }

    public void addPreferredShift(int id, Shift shift) throws Exception {
        ShiftEmployee shiftEmployee = shiftEmployees.get(id);
        try {
            shiftEmployee.addPreferredShift(shift);
            String role = shiftEmployee.getRoles().get(0).toString();
            shiftController.openConnection();
            shiftController.addPreferredShift(employeeMapper.toDTO(shiftEmployee), shiftMapper.toDTO(shift), role);
        } catch (Exception e) {
            throw new Exception("Failed to add preferred shift: " + e.getMessage());
        } finally {
            shiftController.closeConnection();
        }
    }

    public void removePreferredShift(int id, Shift shift) throws Exception {
        ShiftEmployee shiftEmployee = shiftEmployees.get(id);
        try {
            shiftEmployee.removePreferredShift(shift);
            shiftController.openConnection();
            shiftController.deletePreferredShift(employeeMapper.toDTO(shiftEmployee), shiftMapper.toDTO(shift));
        } catch (Exception e) {
            throw new Exception("Failed to remove preferred shift: " + e.getMessage());
        } finally {
            shiftController.closeConnection();
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
            if (!shiftEmployee.getAssignedShifts().containsKey(shift)) {
                shiftController.openConnection();
                shiftEmployee.addAssignedShift(shift, role);
                shiftController.addShiftAssigned(employeeMapper.toDTO(shiftEmployee), shiftMapper.toDTO(shift),
                        role.toString());
            } else {
                return; // Shift already assigned, no need to add again
            }
        } catch (Exception e) {
            throw new Exception("Failed to add assigned shift: " + e.getMessage());
        } finally {
            shiftController.closeConnection();
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
        return (employeeManager.getMissingShift().get(location).get(date) != null);
    }

    // just for Main
    public void addFirstEmployeeManager(int id, String name, LocationDL branch, String bankAccount, int salary,
            LocalDate startDate, int vacationDays, int sickDays,
            float educationFund, float socialBenefits, String password) {
        employeeManager = new EmployeeManager(id, name, bankAccount, salary, startDate,
                vacationDays, sickDays, educationFund, socialBenefits, password);
    }

    public List<LocationDL> getBranches() {
        return branches;
    }

    public DriverController getDriverController() {
        return driverController;
    }

    public List<DriverDL> getAllDrivers() {
        try {
            List<DriverDL> drivers = new ArrayList<>();
            driverController.openConnection();
            List<DriverDTO> driverDTOs = driverController.getAllDrivers();
            for (DriverDTO dto : driverDTOs) {
                DriverMapper driverMapper = new DriverMapper();
                DriverDL driver = driverMapper.toDL(dto);
                drivers.add(driver);
            }
            return drivers;
        } catch (Exception e) {
            System.out.println("Error retrieving drivers: " + e.getMessage());
            return new ArrayList<>();
        } finally {
            driverController.closeConnection();
        }
    }

    public void addBranch(LocationDL branch) {
        if (branches == null) {
            branches = new ArrayList<>();
        }
        if (!branches.contains(branch)) {
            branches.add(branch);
            // employeeManager.addBranch(branch);
        } else {
            // System.out.println("Branch already exists.");
        }
    }

    public void MakePredefinedData() {
        // ClearDataBase();
        // this.employeeManager = new EmployeeManager(0, "Default Manager", "000000000",
        // 10000, LocalDate.now(), 30, 10,
        // 2000f, 1000f, "admin");
        LocationDL branch1 = branches.get(1);
        LocationDL branch2 = branches.get(2);
        addBranch(branch1);
        addBranch(branch2);

        try {
            login(0, "admin");
            hireEmployee(1, 0, branch1, "Alice", "123456789", 5000, LocalDate.now(), 20, 10, 1000f, 500f, "password123",
                    Role.STORE_KEEPER);
            hireEmployee(2, 0, branch2, "Bob", "987654321", 6000, LocalDate.now(), 15, 5, 1200f, 600f, "password456",
                    Role.CASHIER);
            hireEmployee(3, 0, branch1, "David", "555555555", 5500, LocalDate.now(), 18, 8, 1100f, 550f, "password789",
                    Role.CASHIER);
            hireEmployee(4, 0, branch2, "Eve", "444444444", 6500, LocalDate.now(), 22, 12, 1300f, 650f, "password101",
                    Role.STORE_KEEPER);
            hireEmployee(5, 0, branch1, "Frank", "333333333", 7000, LocalDate.now(), 25, 10, 1500f, 700f, "password102",
                    Role.SHIFT_MANAGER);
            hireEmployee(6, 0, branch2, "Grace", "222222222", 8000, LocalDate.now(), 30, 15, 1600f, 800f, "password103",
                    Role.SHIFT_MANAGER);
            hireDriver(7, 0, branch1, "Charlie", "555555555", 7000, LocalDate.now(), 25, 10, 1500f, 700f, "password789",
                    new ArrayList<>(List.of("B", "C")));
            hireDriver(8, 0, branch2, "Hannah", "666666666", 7500, LocalDate.now(), 20, 5, 1400f, 600f, "password104",
                    new ArrayList<>(List.of("A", "B")));

            // Shift shift1 = new Shift(1, 08-06-2022, ShiftType.MORNING, 5, branch1);
            // public Shift(int id, LocalDate date, ShiftType shiftType, int shiftManagerId,
            // LocationDL branch) {
            // addEmployeeToShift(1, null, null, 0);
            // addEmployeeToShift(int employeeId, Shift shift, Role role, int empManagerId)
        } catch (Exception e) {
            System.out.println("Error adding predefined data: " + e.getMessage());
        }
    }

    public void ClearDataBase() {
        try {
            empController.openConnection();
            driverController.openConnection();
            shiftController.openConnection();

            shiftController.clearAllShifts();
            driverController.clearTables();
            empController.clearAllEmployees();

            shiftEmployees.clear();
            branches.clear();
            employeeManager = null;
        } catch (Exception e) {
            System.out.println("Error clearing database: " + e.getMessage());
        } finally {
            empController.closeConnection();
            driverController.closeConnection();
            shiftController.closeConnection();
        }

    }

    public void loadData() {
        try {
            // Load branches (locations)
            locationController.openConnection();
            List<LocationDTO> locationDTOs = locationController.getAllLocations();
            List<LocationDL> loadedBranches = new ArrayList<>();
            for (LocationDTO locationDTO : locationDTOs) {
                LocationDL location = locationMapper.toDomain(locationDTO);
                loadedBranches.add(location);
            }
            this.branches = loadedBranches;

            empController.openConnection();
            // Load employees
            List<EmployeeDTO> employeeDTOs = empController.getAllEmployees();
            shiftEmployees.clear();
            for (EmployeeDTO employeeDTO : employeeDTOs) {
                if (employeeDTO.getId() == 0) {
                    employeeManager = employeeMapper.toDomain(employeeDTO);
                } else {
                    ShiftEmployee employee = employeeMapper.toDomain(employeeDTO, branches,
                            employeeDTO.getAssignedShifts(),
                            employeeDTO.getPrefShifts());
                    shiftEmployees.put(employee.getId(), employee);
                    employeeManager.addEmployeeToAllEmployees(employee);
                }
            }

            // Load drivers
            driverController.openConnection();
            List<DriverDTO> driverDTOs = driverController.getAllDrivers();
            for (DriverDTO driverDTO : driverDTOs) {
                DriverDL driver = driverMapper.toDL(driverDTO);
                shiftEmployees.put(driver.getId(), driver);
            }

            for (Employee employee : shiftEmployees.values()) {
                if (employee instanceof EmployeeManager) {
                    this.employeeManager = (EmployeeManager) employee;
                    break; // there's only one manager
                }
            }

            System.out.println("Employee data loaded from database.");
        } catch (Exception e) {
            System.out.println("Error loading employee data: " + e.getMessage());
            e.printStackTrace();
        } finally {
            empController.closeConnection();
            driverController.closeConnection();
            locationController.closeConnection();
        }
    }

    public void addFirstEmployeeManager() {
        if (employeeManager == null) {
            employeeManager = new EmployeeManager(0, "keren", "000000000", 10000, LocalDate.now(), 30, 10,
                    2000f, 1000f, "admin", branches.get(0));
            DriverDL driver = new DriverDL(-100, "Default Driver", branches.get(0), "000000000", 10000, LocalDate.now(),
                    30, 10,
                    2000f, 1000f, "admin", new ArrayList<>(List.of("A", "B")));
            empController.openConnection();
            try {
                empController.addFirstEmployeeManager(employeeMapper.toDTO(employeeManager));
                login(0, "admin");
                hireDriver(driver.getId(), 0, driver.getBranch(), driver.getName(),
                        driver.getBankAccount(), driver.getSalary(), driver.getStartDate(),
                        driver.getVacationDays(), driver.getSickDays(), driver.getEducationFund(),
                        driver.getSocialBenefits(), driver.getPassword(), new ArrayList<>(driver.getLicenceType()));

            } catch (Exception e) {
                System.out.println("Error logging in as default manager: " + e.getMessage());
            } finally {
                empController.closeConnection();
            }
        }
    }

}
