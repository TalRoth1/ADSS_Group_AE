package DomainLayer;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class EmployeeManager extends Employee {

    private Map<Integer, ShiftEmployee> allEmployees;
    private Map<LocationDL, Map<LocalDate, Shift>> morningShifts;
    private Map<LocationDL, Map<LocalDate, Shift>> eveningShifts;
    private Map<LocalDate, Shift> pastShifts;
    private ArrayList<LocationDL> branches;
    private Map<LocationDL, Map<LocalDate, Set<ShiftType>>> missingShiftMap;
    private static final LocationDL EMPTY_LOCATION = new LocationDL("Empty", 0, "Empty", "Empty", "Empty", "Empty");

    public EmployeeManager(int id, String name, String bankAccount, int salary, LocalDate startDate,
            int vacationDays, int sickDays, double educationFund, double socialBenefits,
            String password) {
        super(id, name, EMPTY_LOCATION, bankAccount, salary, startDate, vacationDays, sickDays, educationFund, socialBenefits,
                password);
        allEmployees = new HashMap<>();
        morningShifts = new HashMap<>();
        eveningShifts = new HashMap<>();
        pastShifts = new HashMap<>();
        branches = new ArrayList<>();
        missingShiftMap = new HashMap<>();
    }

    // methods
    public void addBranch(LocationDL branch) {
        if (branch == null) {
            throw new IllegalArgumentException("Branch cannot be null");
        }
        branches.add(branch);
    }

    public DriverDL assignCheck(LocalDate sentDate, ShiftType shiftType, LocationDL origin, List<LocationDL> destinations,
            String licenceType) {
        List<LocationDL> allLocations = new ArrayList<>(destinations);
        allLocations.add(origin);

        DriverDL selectedDriver = null;

        for (LocationDL location : allLocations) {
            // 1. Check location exists
            if (!branches.contains(location)) {
                System.out.println("Unknown location: " + location);
                return null;
            }

            Shift shift = getValidShift(location, shiftType, sentDate);
            if (shift == null) {
                System.out.println("No valid shift found for " + sentDate + " at location: " + location);
                recordMissingShift(location, sentDate, shiftType);
                return null;
            }

            // 4. Check for storekeeper
            boolean hasStorekeeper = shift.getAssignedEmployeesID()
                    .values()
                    .stream()
                    .anyMatch(role -> role == Role.STORE_KEEPER);

            // 5. Check for driver with required licence
            boolean hasDriverWithLicence = false;
            for (Map.Entry<Integer, Role> entry : shift.getAssignedEmployeesID().entrySet()) {
                if (entry.getValue() == Role.DRIVER) {
                    ShiftEmployee emp = allEmployees.get(entry.getKey());
                    if (emp instanceof DriverDL driver) {
                        if (driver.getLicenceType().contains(licenceType)) {
                            hasDriverWithLicence = true;
                            if (selectedDriver == null) {
                                selectedDriver = driver;
                            }
                            break; // Found a matching driver, no need to check more
                        }
                    }
                }
            }

            // 6. If missing storekeeper or driver with licence, add to missingShiftMap (if date is future)
            if (!hasStorekeeper || !hasDriverWithLicence) {
                recordMissingShift(location, sentDate, shiftType);
                return null;
            }
        }

        return selectedDriver; // could be null if no driver with the required license found
    }

    public void removeEmployee(int id) { // delete employee from the system, currently not used
        allEmployees.remove(id);
    }

    public Shift getShift(LocationDL location, LocalDate date, ShiftType shiftType) {
        if (shiftType == ShiftType.MORNING) {
            return morningShifts.get(location).get(date);
        }
        return eveningShifts.get(location).get(date);
    }

    public boolean checkEmployee(int id) {
        return allEmployees.containsKey(id);
    }

    // update methods
    public void updateBankAccountEmployee(int employeeId, String bankAccount) throws Exception {
        if (!checkEmployee(employeeId)) {
            throw new Exception(employeeId + " not exist");
        }
        ShiftEmployee employee = allEmployees.get(employeeId);
        employee.setBankAccount(bankAccount);
    }

    public void updateSalaryEmployee(int employeeId, int salary) throws Exception {
        if (!checkEmployee(employeeId)) {
            throw new Exception(employeeId + " not exist");
        }
        if (salary < 0) {
            throw new Exception("invalid salary");
        }
        ShiftEmployee employee = allEmployees.get(employeeId);
        employee.setSalary(salary);
    }

    public void updateVacationDaysEmployee(int employeeId, int vacationDays) throws Exception {
        if (!checkEmployee(employeeId)) {
            throw new Exception(employeeId + " not exist");
        }
        if (vacationDays < 0) {
            throw new Exception("invalid vacationDays");
        }
        ShiftEmployee employee = allEmployees.get(employeeId);
        employee.setVacationDays(vacationDays);
    }

    public void updateSickDaysEmployee(int employeeId, int sickDays) throws Exception {
        if (!checkEmployee(employeeId)) {
            throw new Exception(employeeId + " not exist");
        }
        if (sickDays < 0) {
            throw new Exception("invalid sickDays");
        }
        ShiftEmployee employee = allEmployees.get(employeeId);
        employee.setSickDays(sickDays);
    }

    public void updateEducationFund(int employeeId, double educationFund) throws Exception {
        if (!checkEmployee(employeeId)) {
            throw new Exception(employeeId + " not exist");
        }
        if (educationFund < 0) {
            throw new Exception("invalid educationFund");
        }
        ShiftEmployee employee = allEmployees.get(employeeId);
        employee.setEducationFund(educationFund);
    }

    public void updateSocialBenefits(int employeeId, double socialBenefits) throws Exception {
        if (!checkEmployee(employeeId)) {
            throw new Exception(employeeId + " not exist");
        }
        if (socialBenefits < 0) {
            throw new Exception("invalid socialBenefits");
        }
        ShiftEmployee employee = allEmployees.get(employeeId);
        employee.setSocialBenefits(socialBenefits);
    }

    // methods for employees
    public void fireEmployee(int id) throws Exception {
        if (!checkEmployee(id)) {
            throw new Exception(id + " doesn't exist");
        }
        ShiftEmployee employee = allEmployees.get(id);
        if (employee.isFinishWorking()) {
            throw new Exception("Employee is already fired");
        }
        employee.setFinishWorking(true);
        for (Shift shift : employee.getAssignedShifts().keySet()) {
            try {
                shift.removeEmployee(id);
            } catch (Exception e) {
                throw new Exception("Error removing employee from shift: " + e.getMessage());
            }
        }
        employee.setPrefShifts(null);
        employee.setAssignedShifts(null);
        System.out.println("employee " + id + " is fired");
    }

    public ShiftEmployee hireEmployee(int employeeId, String employeeName, LocationDL branch, String bankAccount,
            int salary,
            LocalDate startDate, int vacationDays, int sickDays, double educationFund, double socialBenefits,
            String employeePassword, Role role) {
        if (!branches.contains(branch)) {
            throw new IllegalArgumentException("Branch does not exist in the system");
        }
        ShiftEmployee shiftEmployee = new ShiftEmployee(employeeId, employeeName, branch, bankAccount, salary,
                startDate, vacationDays, sickDays, educationFund, socialBenefits, employeePassword, role);
        allEmployees.put(employeeId, shiftEmployee);
        return shiftEmployee;
    }

    public void addRoleToEmployee(int employeeID, Role role) throws Exception {
        if (!checkEmployee(employeeID)) {
            throw new Exception(employeeID + " doesn't exist");
        }
        ShiftEmployee employee = allEmployees.get(employeeID);
        if (employee.isFinishWorking()) {
            throw new Exception("Employee is fired, you can't add a role to him");
        }
        try {
            employee.addRole(role);
        } catch (Exception e) {
            throw new Exception("Error adding role: " + e.getMessage());
        }
    }

    public void changeRoleToEmployee(int employeeID, Role oldRole, Role newRole) throws Exception {
        if (!checkEmployee(employeeID)) {
            throw new Exception(employeeID + " doesn't exist");
        }
        ShiftEmployee employee = allEmployees.get(employeeID);
        if (employee.isFinishWorking()) {
            throw new Exception("Employee is fired, you can't change a role to him");
        }
        try {
            employee.changeRole(oldRole, newRole);
        } catch (Exception e) {
            throw new Exception("Error removing old role: " + e.getMessage());
        }
    }

    public void deleteRoleFromEmployee(int employeeID, Role role) throws Exception {
        if (!checkEmployee(employeeID)) {
            throw new Exception(employeeID + " doesn't exist");
        }
        ShiftEmployee employee = allEmployees.get(employeeID);
        if (employee.isFinishWorking()) {
            throw new Exception("Employee is fired, you can't delete a role from him");
        }
        try {
            employee.removeRole(role);
        } catch (Exception e) {
            throw new Exception("Error removing role: " + e.getMessage());
        }
    }

    public void addTrainingToEmployee(int employeeID, Training training) throws Exception {
        if (!checkEmployee(employeeID)) {
            throw new Exception(employeeID + " doesn't exist");
        }
        ShiftEmployee employee = allEmployees.get(employeeID);
        try {
            employee.addTraining(training);

        } catch (Exception e) {
            throw new Exception("Error adding training: " + e.getMessage());
        }
    }

    public void removeTrainingFromEmployee(int employeeID, Training training) throws Exception {
        if (!checkEmployee(employeeID)) {
            throw new Exception(employeeID + " doesn't exist");
        }
        ShiftEmployee employee = allEmployees.get(employeeID);
        try {
            employee.removeTraining(training);
        } catch (Exception e) {
            throw new Exception("Error removing training: " + e.getMessage());
        }
    }

    public void changeShiftManager(Shift shift, int oldManager, int newManager) throws Exception { // switch the
        // shiftmanager to
        // another employee
        ShiftEmployee oldManagerE = allEmployees.get(oldManager);
        ShiftEmployee newManagerE = allEmployees.get(newManager);
        if (!checkEmployee(newManager) || !checkEmployee(oldManager)) {
            throw new Exception("employee not found in the system");
        }
        if (!newManagerE.getRoles().contains(Role.SHIFT_MANAGER)) {
            throw new Exception("new manager does not have the role of shift manager");
        }
        if (shift.getAssignedEmployeesID().containsKey(newManager)) {
            throw new Exception("new manager is already assigned to this shift");
        }
        if (!shift.getAssignedEmployeesID().containsKey(oldManager)) {
            throw new Exception("old manager is not assigned to this shift");
        }
        if (shift.getShiftManagerId() != oldManager) {
            throw new Exception("old manager is not the shift manager of this shift");
        }
        shift.setShiftManagerId(newManager);
        try {
            oldManagerE.removeAssignedShift(shift);
            newManagerE.addAssignedShift(shift, Role.SHIFT_MANAGER);
            shift.removeEmployee(oldManager);
            shift.addEmployee(newManager, Role.SHIFT_MANAGER);
        } catch (Exception e) {
            throw new Exception("Error changing shift manager: " + e.getMessage());
        }
    }

    public void shiftReplacement(LocationDL location, Shift shift, int empID, int replacementID) throws Exception { // replace employee in the shift with another employee
        if (!checkEmployee(empID) || !checkEmployee(replacementID)) {
            throw new Exception("employee not found in the system");
        }
        if (replacementID == empID) {
            throw new Exception("replacement employee cannot be the same as the original employee");
        }
        if (!shift.getAssignedEmployeesID().containsKey(empID)) {
            throw new Exception("original employee not assigned to this shift");
        }
        if (shift.getAssignedEmployeesID().containsKey(replacementID)) {
            throw new Exception("replacement employee already assigned to this shift");
        }
        ShiftEmployee employee = allEmployees.get(empID);
        ShiftEmployee replacement = allEmployees.get(replacementID);
        Role role = shift.getAssignedEmployeesID().get(empID);
        if (employee.isFinishWorking() || replacement.isFinishWorking()) {
            throw new Exception("one of the employees is fired, you can't replace him");
        }
        if (shift.getShiftManagerId() == empID || shift.getShiftManagerId() == replacementID) {
            throw new Exception(
                    "shift manager cannot be replaced, there is a different option to change the shift manager");
        }
        if (!replacement.getRoles().contains(shift.getAssignedEmployeesID().get(empID))) {
            throw new Exception("replacement employee does not have the same role as the original employee");
        }
        try {
            shift.removeEmployee(empID);
            employee.removeAssignedShift(shift);
            shift.addEmployee(replacementID, role);
            replacement.addAssignedShift(shift, role);

            if (shift.getShiftType() == ShiftType.EVENING) {
                eveningShifts.get(location).put(shift.getDate(), shift);
            } else {
                morningShifts.get(location).put(shift.getDate(), shift);
            }
        } catch (Exception e) {
            throw new Exception("Error replacing employee in shift: " + e.getMessage());
        }
    }

    public void createDefaultShift(LocationDL location, LocalDate date, ShiftType shiftType) throws Exception { // create a default shift,
        // used for testing
        if (date == null || shiftType == null) {
            throw new Exception("invalid date or shift type");
        }
        if (shiftType == ShiftType.MORNING && morningShifts.containsKey(date)) {
            throw new Exception("morning shift already exists for this date");
        } else if (shiftType == ShiftType.EVENING && eveningShifts.containsKey(date)) {
            throw new Exception("evening shift already exists for this date");
        }
        Shift shift = new Shift(date, shiftType, -1, null); // -1 for shift manager id, false for shipment shift
        if (shiftType == ShiftType.MORNING) {
            Map<LocalDate, Shift> dateShiftMap = new HashMap<>();
            dateShiftMap.put(date, shift);
            morningShifts.put(location, dateShiftMap);
        } else if (shiftType == ShiftType.EVENING) {
            Map<LocalDate, Shift> dateShiftMap = new HashMap<>();
            dateShiftMap.put(date, shift);
            eveningShifts.put(location, dateShiftMap);
        }
    }

    public void createShift(LocationDL location, LocalDate date, ShiftType shiftType, int shiftManagerId) throws Exception { // create a new
        // shift,
        // definig the
        // date, type
        // and
        // shiftmanager
        if (date == null || shiftType == null) {
            throw new Exception("invalid date or shift type");
        }
        if (!checkEmployee(shiftManagerId)) {
            throw new Exception("shift manager does not exist");
        }
        if (!allEmployees.get(shiftManagerId).getRoles().contains(Role.SHIFT_MANAGER)) {
            throw new Exception("shift manager does not have the role of shift manager");
        }

        if (shiftType == ShiftType.MORNING && morningShifts.containsKey(date)) {
            throw new Exception("morning shift already exists for this date");
        }
        if (shiftType == ShiftType.EVENING && eveningShifts.containsKey(date)) {
            throw new Exception("evening shift already exists for this date");
        }
        Shift shift = new Shift(date, shiftType, shiftManagerId, location);
        if (shiftType == ShiftType.MORNING) {
            Map<LocalDate, Shift> dateShiftMap = new HashMap<>();
            dateShiftMap.put(date, shift);
            morningShifts.put(location, dateShiftMap);
        } else if (shiftType == ShiftType.EVENING) {
            Map<LocalDate, Shift> dateShiftMap = new HashMap<>();
            dateShiftMap.put(date, shift);
            eveningShifts.put(location, dateShiftMap);
        }
        pastShifts.put(date, shift);
    }

    public void addEmployeeToShift(LocationDL location, int id, Shift shift, Role role) throws Exception { // add employee to shift
        if (!checkEmployee(id)) {
            throw new Exception("employee not exist");
        }
        ShiftEmployee employee = allEmployees.get(id);
        if (employee.isFinishWorking()) {
            throw new Exception("this employee is fired");
        }
        if (!employee.getRoles().contains(role)) {
            throw new Exception("this employee does not have the role " + role);
        }
        // if (!employee.isAvailable(shift)) {
        // return "this employee is not available for this shift";
        // }
        if (shift.getAssignedEmployeesID().containsKey(id)) {
            throw new Exception("this employee is already assigned to this shift");
        }
        try {
            shift.addEmployee(id, role);
        } catch (Exception e) {
            throw new Exception(e.getMessage());
        }
        employee.addAssignedShift(shift, role);
        if (shift.getShiftType() == ShiftType.MORNING) {
            morningShifts.get(location).put(shift.getDate(), shift);
        } else if (shift.getShiftType() == ShiftType.EVENING) {
            eveningShifts.get(location).put(shift.getDate(), shift);
        }
    }

    public void removeEmployeeFromShift(int id, Shift shift) throws Exception {
        if (!checkEmployee(id)) {
            throw new Exception("employee not exist");
        }
        ShiftEmployee employee = allEmployees.get(id);
        if (employee.isFinishWorking()) {
            throw new Exception("this employee is fired");
        }
        if (!shift.getAssignedEmployeesID().containsKey(id)) {
            throw new Exception("this employee is not assigned to this shift");
        }
        try {
            shift.removeEmployee(id);
            employee.removeAssignedShift(shift);
        } catch (Exception e) {
            throw new Exception(e.getMessage());
        }
    }

    // getters and setters
    public void setTimes(Shift shift, int start, int end) throws Exception { // set start time and end time of the shift
        if (start < 0 || end < 0 || start >= end) {
            throw new Exception("invalid start or end time");
        }
        if (shift == null) {
            throw new Exception("shift not exist");
        }
        if ((shift.getShiftType() == ShiftType.MORNING && (end > 1400 || start < 600))
                || shift.getShiftType() == ShiftType.EVENING && (start < 1400 || end > 2200)) {
            throw new Exception("invalid start or end time for this shift type");
        }
        shift.setStartTime(start);
        shift.setEndTime(end);
    }

    public void setRequiredRole(Shift shift, Role role, int numOfEmployees) throws Exception { // set the amount of
        // employess of
        // thisspecific role for
        // the shift
        if (shift.isShipmentShift() && role == role.DRIVER && numOfEmployees < 1) {
            throw new Exception("For shipment shifts, you must have at least one driver.");
        }
        if (shift.isShipmentShift() && role == role.STORE_KEEPER && numOfEmployees < 1) {
            throw new Exception("For shipment shifts, you must have at least one store keeper.");
        }
        if (shift != null) {
            try {
                shift.setRequiredRoles(role, numOfEmployees);
            } catch (Exception e) {
                throw new Exception("Error setting required roles: " + e.getMessage());
            }
        }
    }

    public void setShiftManager(Shift shift, int id) throws Exception { // set the shift manager of the shift
        if (!checkEmployee(id)) {
            throw new Exception("employee not exist");
        }
        if (allEmployees.get(id).isFinishWorking()) {
            throw new Exception("this employee is fired");
        }
        if (!allEmployees.get(id).getRoles().contains(Role.SHIFT_MANAGER)) {
            throw new Exception("this employee does not have the role of shift manager");
        }
        if (shift.getShiftManagerId() == id) {
            throw new Exception("this employee is already the shift manager");
        }
        if (shift.getAssignedEmployeesID().containsKey(id)) {
            throw new Exception("this employee is already assigned to this shift");
        }
        shift.setShiftManagerId(id);
    }

    public String getPrefAllEmployees() { // all the shifts of all employees
        StringBuilder sb = new StringBuilder();
        for (ShiftEmployee e : allEmployees.values()) {
            if (!e.isFinishWorking()) {
                sb.append(e.getName()).append(" ").append(e.getId()).append(" Roles: ").append(e.getRoles())
                        .append("\n next week shiftspref:\n").append(e.getPrefShifts()).append("\n");
            }
        }
        return sb.toString();
    }

    public void getAssignedEmployeeShiftsManager(int employeeID) throws Exception { // all the assigned shifts of the employee
        if (!checkEmployee(employeeID)) {
            throw new Exception("employee not exist");
        }
        ShiftEmployee employee = allEmployees.get(employeeID);
        String morning = "Morning Shifts:\n";
        String evening = "Evening Shifts:\n";

        for (Map.Entry<LocationDL, Map<LocalDate, Shift>> entry : morningShifts.entrySet()) {
            LocationDL location = entry.getKey();
            Map<LocalDate, Shift> shifts = entry.getValue();
            for (LocalDate d : shifts.keySet()) {
                Shift s = shifts.get(d);
                if (s.getAssignedEmployeesID().containsKey(employeeID)) {
                    try {
                        String role = s.getRole(employeeID);
                        morning += "Location: " + location + ", Date: " + s.getDate() + ", Role: " + role + "\n";
                    } catch (Exception e) {
                        throw new Exception("Error getting role for employee: " + e.getMessage());
                    }
                }
            }
        }
        for (Map.Entry<LocationDL, Map<LocalDate, Shift>> entry : eveningShifts.entrySet()) {
            LocationDL location = entry.getKey();
            Map<LocalDate, Shift> shifts = entry.getValue();
            for (LocalDate d : shifts.keySet()) {
                Shift s = shifts.get(d);
                if (s.getAssignedEmployeesID().containsKey(employeeID)) {
                    try {
                        String role = s.getRole(employeeID);
                        evening += "Location: " + location + ", Date: " + s.getDate() + ", Role: " + role + "\n";
                    } catch (Exception e) {
                        throw new Exception("Error getting role for employee: " + e.getMessage());
                    }
                }
            }
        }
        System.out.println("Employee: " + employee.getName() + " " + employeeID + "\n" + morning + "\n" + evening);
    }

    public void getAvailableEmployees(Shift shift, Role role) { // all the employees that can work in this shift and
        // have this role
        try {
            String res = "Available employees for this shift and role: " + role.toString() + "\n";
            for (ShiftEmployee employee : allEmployees.values()) {
                if (employee.getRoles().contains(role) && employee.isAvailable(shift)) {
                    res += employee.getName() + " " + employee.getId() + "\n";
                }
            }
            System.out.println(res);
        } catch (Exception e) {
            System.out.println("Error getting available employees: " + e.getMessage());
        }
    }

    public void archiveWeeklyForAllEmployees() {
        LocalDate today = LocalDate.now();
        for (ShiftEmployee employee : allEmployees.values()) {
            employee.archiveOldShiftsWeekly(today);
        }
    }

    public ShiftEmployee getEmployee(int id) {
        return allEmployees.get(id);
    }

    public Map<LocalDate, Shift> getPastShifts() {
        return pastShifts;
    }

    public void addEmployee(ShiftEmployee employee) {

        allEmployees.put(employee.getId(), employee);
    }

    private Shift getValidShift(LocationDL location, ShiftType shiftType, LocalDate sentDate) {
        Map<LocalDate, Shift> shiftMap = (shiftType == ShiftType.MORNING)
                ? morningShifts.get(location)
                : eveningShifts.get(location);

        if (shiftMap == null) {
            System.out.println("No shifts initialized at location: " + location);
            return null; // Valid location but no shifts map
        }

        if (!shiftMap.containsKey(sentDate)) {
            System.out.println("No shift scheduled on " + sentDate + " at location: " + location);
            return null; // Valid shift map, but no shift on this date
        }

        return shiftMap.get(sentDate); // Everything is valid
    }

    private void recordMissingShift(LocationDL location, LocalDate sentDate, ShiftType shiftType) {
        LocalDate now = LocalDate.now();
        LocalDate startOfThisWeek = now.with(java.time.DayOfWeek.MONDAY);

        // Only track missing shifts if the date is next week or later
        if (sentDate.isAfter(now) && !sentDate.isBefore(startOfThisWeek)) {
            missingShiftMap
                    .computeIfAbsent(location, loc -> new HashMap<>())
                    .computeIfAbsent(sentDate, date -> new HashSet<>())
                    .add(shiftType);
        }
    }

    public List<ShiftEmployee> getAllEmployeesInBranch(LocationDL branch) {
        List<ShiftEmployee> employeesInBranch = new ArrayList<>();
        for (ShiftEmployee employee : allEmployees.values()) {
            if (employee.getBranch().equals(branch)) {
                employeesInBranch.add(employee);
            }
        }
        return employeesInBranch;
    }

    //add to dao?
    public void checkStorekeeperExistInBranch(List<LocationDL> locations, Shift shift) throws Exception { //all loacations must have a storekeeper in the shift
        for (LocationDL location : locations) {
            Map<LocalDate, Shift> shiftMap = (shift.getShiftType() == ShiftType.MORNING)
                    ? morningShifts.get(location)
                    : eveningShifts.get(location);
            if (shiftMap == null || !shiftMap.containsKey(shift.getDate())) {
                throw new Exception("No shift found at " + location + " on " + shift.getDate());
            }
            Shift currentShift = shiftMap.get(shift.getDate());
            boolean hasStorekeeper = currentShift.getAssignedEmployeesID()
                    .values()
                    .stream()
                    .anyMatch(role -> role == Role.STORE_KEEPER);
            if (!hasStorekeeper) {
                throw new Exception("No storekeeper assigned to the shift at " + location + " on " + shift.getDate());
            }
        }
    }

    public void checkDriverExistInBranch(List<LocationDL> locations) throws Exception { // check if there is at least one driver in 2 branches of the shipment 
        for (LocationDL location : locations) {
            Map<LocalDate, Shift> shiftMap = eveningShifts.get(location);
            if (shiftMap == null || shiftMap.isEmpty()) {
                throw new Exception("No evening shifts found at " + location);
            }
            boolean hasDriver = false;
            for (Shift shift : shiftMap.values()) {
                for (Role role : shift.getAssignedEmployeesID().values()) {
                    if (role == Role.DRIVER) {
                        hasDriver = true;
                        break;
                    }
                }
                if (hasDriver) {
                    break;
                }
            }
            if (!hasDriver) {
                throw new Exception("No driver assigned to the evening shifts at " + location);
            }
        }
    }

}
