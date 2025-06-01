package PresentationLayer;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.time.temporal.ChronoUnit;
import java.time.temporal.TemporalAdjusters;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

import DomainLayer.Employee;
import DomainLayer.EmployeeFacade;
import DomainLayer.EmployeeManager;
import DomainLayer.LocationDL;
import DomainLayer.Role;
import DomainLayer.Shift;
import DomainLayer.ShiftEmployee;
import DomainLayer.ShiftType;

public class EmployeeCLI {

    public static Scanner scanner = new Scanner(System.in);
    EmployeeFacade employeeFacade;
    private int userId;
    private String password;
    private LocalDate nowDate;

    private static final Integer[] MORNING_SHIFT_START_TIMES = {
        600, 630, 700, 730, 800, 830, 900, 930, 1000};
    private static final Integer[] MORNING_SHIFT_END_TIMES = {
        1300, 1330, 1400};
    private static final Integer[] EVENING_SHIFT_START_TIMES = {
        1400, 1430, 1500, 1530, 1600, 1630, 1700, 1730, 1800, 1830, 1900, 1930, 2000, 2030, 2100};
    private static final Integer[] EVENING_SHIFT_END_TIMES = {
        2100, 2130, 2200};
    List<LocationDL> branches = employeeFacade.getBranches();

    public EmployeeCLI(EmployeeFacade employeeFacade) {
        this.employeeFacade = employeeFacade;
        nowDate = LocalDate.now();
        loginCLI();
    }

    public void loginCLI() {
        while (true) {
            System.out.println("LOGIN:");
            userId = readInt("Please enter your ID:");
            password = readString("Please enter your password:");

            try {
                Employee emp = employeeFacade.login(userId, password);
                if (emp instanceof EmployeeManager) {
                    employeeManager();
                }
                if (employeeFacade.isShiftManager(userId)) {
                    shiftManager();
                }
                if (emp instanceof ShiftEmployee) {
                    shiftEmployee();
                } else {
                    employeeManager();
                }
            } catch (Exception e) {
                System.out.println("Login failed: " + e.getMessage());
                System.out.println("Please try again.");
                loginCLI();
            }
        }
    }

    private void employeeManager() {
        try {
            EmployeeManager emp = employeeFacade.getEmployeeManager();
            LocalDate now = LocalDate.now();
            LocalDate thisSunday = now
                    .with(java.time.temporal.TemporalAdjusters.previousOrSame(java.time.DayOfWeek.SUNDAY));
            Iterator<Map.Entry<LocationDL, Map<LocalDate, Shift>>> branchIterator2 = emp.getMissingShift().entrySet()
                    .iterator();
            LocalDate endOfNextWeek = thisSunday.plusWeeks(2).minusDays(1);

            while (branchIterator2.hasNext()) { // check if there are any branches with missing shifts
                Map.Entry<LocationDL, Map<LocalDate, Shift>> branchEntry = branchIterator2.next();
                Map<LocalDate, Shift> shiftsByDate = branchEntry.getValue();
                Iterator<Map.Entry<LocalDate, Shift>> shiftIterator = shiftsByDate.entrySet().iterator();
                while (shiftIterator.hasNext()) {
                    Map.Entry<LocalDate, Shift> shiftEntry = shiftIterator.next();
                    LocalDate shiftDate = shiftEntry.getKey();
                    Shift shift = shiftEntry.getValue();

                    if (!shiftDate.isBefore(now) && !shiftDate.isAfter(endOfNextWeek)) {
                        emp.getToCompleteShifts()
                                .computeIfAbsent(branchEntry.getKey(), k -> new HashMap<>())
                                .put(shiftDate, shift);
                    }
                }
            }
            // looping through the toCompleteShifts
            while (emp != null && emp.getToCompleteShifts() != null && !emp.getToCompleteShifts().isEmpty()) {
                System.out.println("You have shifts to complete. Please complete them before proceeding.");
                Iterator<Map.Entry<LocationDL, Map<LocalDate, Shift>>> branchIterator = emp.getToCompleteShifts()
                        .entrySet().iterator();
                while (branchIterator.hasNext()) {
                    Map.Entry<LocationDL, Map<LocalDate, Shift>> branchEntry = branchIterator.next();
                    Map<LocalDate, Shift> shiftsByDate = branchEntry.getValue();
                    Iterator<Map.Entry<LocalDate, Shift>> shiftIterator = shiftsByDate.entrySet().iterator();
                    while (shiftIterator.hasNext()) {
                        Map.Entry<LocalDate, Shift> shiftEntry = shiftIterator.next();
                        Shift shift = shiftEntry.getValue();
                        System.out.println(
                                "Shift on " + shift.getDate() + " in the " + shift.getShiftType().toString() + ":");
                        int shiftManagerId = selectEmployeeForRole(shift, Role.SHIFT_MANAGER);
                        employeeFacade.setShiftManager(emp.getId(), shift, shiftManagerId);
                        for (Role role : Role.values()) {
                            if (role != Role.SHIFT_MANAGER) {
                                chooseNumOfEmployeesForShift(role, shift, userId);
                            }
                        }
                        shiftIterator.remove(); // remove the shift from the list
                    }
                }
            }
        } catch (Exception e) {
            System.out.println("Error retrieving Employee Manager: " + e.getMessage());
        }

        String[] actions = {"Create Shifts", "Set Shifts", "Add Employee to Exist Shift",
            "Remove Employee From Exist Shift",
            "Fire Employee", "Hire Employee", "Change Employee's Role",
            "Add Role to Employee", "Change Shift Manager", "Replace Employee",
            "Delete Employee's Role", "Change Employee's Data", "Show Shift Information",
            "Show Past Shifts", "Show Employee's shifts", "Change Shift Hours", "Logout"};
        String option = selectFromList("Select Employee Manager Action (Enter the number)", actions);
        switch (option) {
            case "Create Shifts":
                autoCreateShifts();
                break;
            case "Set Shifts":
                setShifts();
                break;
            case "Add Employee to Exist Shift":
                addEmployeeToExistingShift();
                break;
            case "Remove Employee From Exist Shift":
                removeEmployeeFromShift();
                break;
            case "Fire Employee":
                fireEmployee();
                break;
            case "Hire Employee":
                hireEmployee();
                break;
            case "Change Employee's Role":
                changeRoleToEmployee();
                break;
            case "Add Role to Employee":
                addRoleToEmployee();
                break;
            case "Change Shift Manager":
                changeShiftManager();
                break;
            case "Replace Employee":
                replaceEmployee();
                break;
            case "Delete Employee's Role":
                deleteRoleFromEmployee();
                break;
            case "Change Employee's Data":
                changeEmployeeData();
                break;
            case "Show Shift Information":
                getShiftInfo("employeeManager");
                break;
            case "Show Past Shifts":
                getPastShifts();
                break;
            case "Show Employee's shifts":
                getEmployeeShiftsAsEmployeeManager();
                break;
            case "Change Shift Hours":
                setTimes();
                break;
            case "Logout":
                logout(userId);
                break;
            default:
                System.out.println("This is not a valid Employee Manager action");
                employeeManager();
                break;
        }
    }

    private void autoCreateShifts() {
        try {
            System.out.println("Please select a branch from the following list:");
            LocationDL branch = selectFromList("Select the Branch you want: ", branches.toArray(new LocationDL[0]));
            employeeFacade.autoCreateShiftsForNextWeek(userId, branch);
            System.out.println("Shifts for next week created successfully.");
        } catch (Exception e) {
            System.out.println("Failed to auto-create shifts: " + e.getMessage());
        }
        employeeManager();
    }

    private void setShifts() {
        ShowPrefAllEmployees();
        if (branches.isEmpty()) {
            System.out.println("No branches available. Please create a branch first.");
            employeeManager();
        }
        System.out.println("Please select a branch from the following list:");
        LocationDL branch = selectFromList("Select the Branch you want: ", branches.toArray(new LocationDL[0]));
        LocalDate dateOfShift = chooseDateForManager("please enter start date"); // choose date with helper method
        System.out.println("Shift morning hours: 06:00 - 14:00");
        System.out.println("Evening morning hours: 14:00 - 22:00");
        System.out.println("if you want to change shift hours, you have option for this in the main menu");
        ShiftType shiftType = selectFromList("Select Shift Type: ", ShiftType.values());
        if (employeeFacade.checkPendingShipment(branch, dateOfShift, shiftType)) {
            System.out.println("This shift has a pending shipment ");
            System.out.println("Make sure to assign store keeper and driver in this shift setup.");
        }

        try {
            Shift shift = employeeFacade.getShift(branch, dateOfShift, shiftType, userId);
            // choose shift manager
            int shiftManagerId = selectEmployeeForRole(shift, Role.SHIFT_MANAGER);
            employeeFacade.setShiftManager(userId, shift, shiftManagerId);
            // choose number of employees for each role
            for (Role role : Role.values()) {
                if (role != Role.SHIFT_MANAGER) {
                    chooseNumOfEmployeesForShift(role, shift, userId);
                }
            }
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
        employeeManager();
    }

    private void addEmployeeToExistingShift() {
        ShowPrefAllEmployees();
        LocalDate dateOfShift = chooseDateForAddEmployee("Please enter Date");
        ShiftType shiftType = selectFromList("Select Shift Type: ", ShiftType.values());
        LocationDL branch = selectFromList("Select the Branch you want: ", branches.toArray(new LocationDL[0]));
        try {
            Shift shift = employeeFacade.getShift(branch, dateOfShift, shiftType, userId);
            int newEmployeeId = selectEmployeeForExistingShift(shift);
            if (newEmployeeId == -1) {
                System.out.println("Invalid employee choice, please try again.");
                employeeManager();
            }

            Role role = selectFromList("Choose a role:", Role.values());
            if (role == Role.SHIFT_MANAGER) {
                System.out.println("You can't add a shift manager to an existing shift, please choose another role.");
                employeeManager();
            }
            employeeFacade.addEmployeeToShift(newEmployeeId, shift, role, userId);
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
            employeeManager();
        }
    }

    private void removeEmployeeFromShift() {
        int employeeId = readInt("Please enter Employee's ID to remove: ");
        LocalDate dateOfShift = chooseDate("Please enter the date of the shift");
        ShiftType shiftType = selectFromList("Select Shift Type: ", ShiftType.values());
        LocationDL branch = selectFromList("Select the Branch you want: ", branches.toArray(new LocationDL[2]));
        try {
            Shift shift = employeeFacade.getShift(branch, dateOfShift, shiftType, userId);
            if (shift.getShiftManagerId() == employeeId) {
                System.out.println("You can't remove shift Manager.");
                System.out.println("if you want to change shift manager, you have this option in the menu.");
                employeeManager();
            }
            employeeFacade.removeEmployeeFromShift(employeeId, shift, userId);
            employeeManager();
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
            employeeManager();
        }
    }

    private void fireEmployee() {
        int employeeId = readInt("Please enter Employee's ID to fire: ");
        try {
            employeeFacade.fireEmployee(employeeId, userId);
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
        employeeManager();
    }

    private void hireEmployee() {
        System.out.println("Please enter the new Employee details");
        int employeeID = readInt("ID: ");
        String name = readString("Name");
        String bankAccount = readString("Bank Account: ");
        int salary = readInt("Salary: ");
        LocalDate startDate = chooseDateForHire("Start Date: ");
        int vacationDays = readInt("Vacation Days");
        int sickDays = readInt("Sick Days");
        double educationFund = readDouble("Education fund: ");
        double socialBenefits = readDouble("Social Benefits: ");
        String employeePassword = readString("Password: ");
        LocationDL branch = selectFromList("Select the Branch you want: ", branches.toArray(new LocationDL[2]));
        branches = employeeFacade.getBranches();
        if (branches.isEmpty()) {
            System.out.println("No branches available. Please create a branch first.");
            employeeManager();
        }

        // choose role
        Role selectedRole = selectFromList("Choose a role:", Role.values());
        try {
            if (selectedRole == Role.DRIVER) {
                boolean flag = true;
                ArrayList<String> licenses = new ArrayList<>();
                while (flag) {
                    System.out.println("Please enter the driver license types or finish to end: ");
                    String licenseType = scanner.nextLine();
                    if (licenseType.equalsIgnoreCase("Finish")) {
                        flag = false;
                    } else {
                        licenses.add(licenseType);
                    }
                }
                employeeFacade.hireDriver(employeeID, userId, branch, name, bankAccount, salary, startDate,
                        vacationDays, sickDays, educationFund, socialBenefits, employeePassword, licenses);
            }
            employeeFacade.hireEmployee(employeeID, userId, branch, name, bankAccount, salary, startDate,
                    vacationDays, sickDays, educationFund, socialBenefits, employeePassword, selectedRole);
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
        employeeManager();
    }

    private void changeRoleToEmployee() {
        int employeeID = readInt("Please enter employee ID: ");
        Role oldRole = selectFromList("Choose Role To Change:", Role.values());
        Role newRole = selectFromList("To What Role:", Role.values());
        try {
            employeeFacade.changeRoleToEmployee(employeeID, userId, oldRole, newRole);
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
        employeeManager();
    }

    private void addRoleToEmployee() {
        int employeeID = readInt("Please enter employee ID: ");
        Role newRole = selectFromList("Choose Role To Add:", Role.values());
        try {
            employeeFacade.addRoleToEmployee(employeeID, userId, newRole);
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
        employeeManager();
    }

    private void changeShiftManager() {
        int oldShiftManagerId = readInt("Please enter old shift manager ID: ");
        LocationDL branch = selectFromList("Select the Branch you want: ", branches.toArray(new LocationDL[0]));
        LocalDate dateOfShift = chooseDate("Please enter the date of the shift");
        ShiftType shiftType = selectFromList("Select Shift Type: ", ShiftType.values());
        try {
            Shift shift = employeeFacade.getShift(branch, dateOfShift, shiftType, userId);
            int newShiftManagerId = selectEmployeeForExistingShift(shift);
            employeeFacade.changeShiftManager(shift, oldShiftManagerId, newShiftManagerId, userId);
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
        employeeManager();
    }

    private void replaceEmployee() {
        ShowPrefAllEmployees();
        LocationDL branch = selectFromList("Select the Branch you want: ", branches.toArray(new LocationDL[0]));
        int oldEmployeeId = readInt("Please enter the ID of the employee you want to replace: ");
        LocalDate dateOfShift = chooseDate("Please enter the date of the shift");
        ShiftType shiftType = selectFromList("Select Shift Type: ", ShiftType.values());
        try {
            Shift shift = employeeFacade.getShift(branch, dateOfShift, shiftType, userId);
            int newEmployeeId = selectEmployeeForExistingShift(shift);
            if (newEmployeeId == -1) {
                System.out.println("Invalid employee choice, please try again.");
                employeeManager();
            }
            employeeFacade.shiftReplacement(shift, oldEmployeeId, userId, newEmployeeId);
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
        employeeManager();
    }

    private void deleteRoleFromEmployee() {
        int employeeID = readInt("Please enter employee ID: ");
        Role toDelete = selectFromList("Choose Role To Delete:", Role.values());
        try {
            employeeFacade.deleteRoleFromEmployee(employeeID, userId, toDelete);
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
        employeeManager();
    }

    private void changeEmployeeData() {
        String[] labels = {"Salary", "Bank Account", "Vacation Days", "Sick Days", "Education Fund",
            "Social Benefits"};
        String option = selectFromList("Select Employee Data to change:", labels);
        switch (option) {
            case "salary":
                updateSalary();
                break;
            case "Bank Account":
                updateBankAccount();
                break;
            case "Vacation Days":
                updateVacationDays();
                break;
            case "Sick Days":
                updateSickDays();
                break;
            case "Education Fund":
                updateEducationFund();
                break;
            case "Social Benefits":
                updateSocialBenefits();
                break;
            default:
                System.out.println("Invalid option selected. Please try again.");
                employeeManager();
                break;
        }

    }

    private void getPastShifts() {
        try {
            Map<LocalDate, Shift> shifts = employeeFacade.getPastShifts(userId);
            System.out.println("Past Shifts:");
            for (Map.Entry<LocalDate, Shift> entry : shifts.entrySet()) {
                Shift shift = entry.getValue();
                System.out.println(shift.toString());
            }
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
        employeeManager();
    }

    private void getEmployeeShiftsAsEmployeeManager() {
        int employeeId = readInt("Please enter Employee's ID: ");
        try {
            employeeFacade.getAssignedEmployeeShiftsManager(employeeId, userId);
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
        employeeManager();
    }

    private void setTimes() {
        LocationDL branch = selectFromList("Select the Branch you want: ", branches.toArray(new LocationDL[0]));
        LocalDate date = chooseDate("Please enter the date of the shift");
        ShiftType shiftType = selectFromList("Select Shift Type: ", ShiftType.values());
        int[] times = getValidShiftTimes(shiftType);
        try {
            Shift shift = employeeFacade.getShift(branch, date, shiftType, userId);
            employeeFacade.setTimes(userId, shift, times[0], times[1]);
            System.out.println("Shift times updated successfully!");
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
        employeeManager();
    }

    private void logout(int id) {
        try {
            employeeFacade.logout(id);
        } catch (Exception e) {
            System.out.println("Error during logout: " + e.getMessage());
        }
    }

    private void updateSalary() {
        int employeeId = readInt("Please enter employee ID: ");
        int salary = readInt("Enter the new Salary: ");
        try {
            employeeFacade.updateSalary(employeeId, userId, salary);
            System.out.println("Salary updated successfully!");
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
        employeeManager();
    }

    private void updateBankAccount() {
        int employeeId = readInt("Please enter employee ID: ");
        String bankAccount = readString("Enter the new Bank Account: ");
        try {
            employeeFacade.updateBankAccount(employeeId, userId, bankAccount);
            System.out.println("Bank Account updated successfully!");
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
        employeeManager();
    }

    private void updateVacationDays() {
        int employeeId = readInt("Please enter employee ID: ");
        int vacationDays = readInt("Enter the new Vacation Days: ");
        try {
            employeeFacade.updateVacationDays(employeeId, userId, vacationDays);
            System.out.println("Vacation Days updated successfully!");
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
        employeeManager();
    }

    private void updateSickDays() {
        int employeeId = readInt("Please enter employee ID: ");
        int sickDays = readInt("Enter the new Sick Days: ");
        try {
            employeeFacade.updateSickDays(employeeId, userId, sickDays);
            System.out.println("Sick Days updated successfully!");
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
        employeeManager();
    }

    private void updateEducationFund() {
        int employeeId = readInt("Please enter employee ID: ");
        double educationFund = readDouble("Enter the new Education Fund: ");
        try {
            employeeFacade.updateEducationFund(employeeId, userId, educationFund);
            System.out.println("Education Fund updated successfully!");
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
        employeeManager();
    }

    private void updateSocialBenefits() {
        int employeeId = readInt("Please enter employee ID: ");
        double socialBenefits = readDouble("Enter the new Education Fund: ");
        try {
            employeeFacade.updateSocialBenefits(employeeId, userId, socialBenefits);
            System.out.println("Social Benefits updated successfully!");
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
        employeeManager();

    }

    // private void updatePassword() {
    // int employeeId = readInt("Please enter employee ID: ");
    // String password = readString("Enter the new Password: ");
    // String response = employeeFacade.updatePassword(employeeId, id, password);
    // if(response != null)
    // System.out.println(response);
    // EmployeeManager();
    // }
    private void shiftManager() {
        String[] actions = {"Add Preferred Shift", "Remove Preferred Shift",
            "Show Employee's shifts", "Show Shift Information", "Show my Preferences",
            "Show my Assigned Shifts", "Logout"};
        String option = selectFromList("Select Shift Manager Action:", actions);

        switch (option) {
            case "Add Preferred Shift":
                addPreferredShift("shiftManager");
                break;
            case "Remove Preferred Shift":
                removePreferredShift("shiftManager");
                break;
            case "Show Employee's shifts":
                getEmployeeShiftsAsShiftManager();
                break;
            case "Show Shift Information":
                getShiftInfo("shiftManager");
                break;
            case "Show my Preferences":
                getMyPreferences("shiftManager");
                break;
            case "Show my Assigned Shifts":
                getMyAssignedShifts("shiftManager");
                break;
            case "Logout":
                logout(userId);
                break;
            default:
                System.out.println("This is not a valid Shift Manager action");
                shiftManager();
                break;
        }
    }

    private void getEmployeeShiftsAsShiftManager() {
        int employeeId = readInt("Please enter Employee's ID: ");
        try {
            employeeFacade.getAssignedEmployeeShiftsManager(employeeId, userId);
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
        shiftManager();
    }

    private void getMyAssignedShifts(String type) {
        try {
            employeeFacade.getAssignedEmployeeShiftsEmployee(userId);
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
        if (type.equals("shiftManager")) {
            shiftManager();
        }
        shiftEmployee(); // it's shift Employee so get back to his menu
    }

    private void getMyPreferences(String type) {
        try {
            employeeFacade.getPreferredShiftsEmployee(userId);
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
        if (type.equals("shiftManager")) {
            shiftManager();
        }
        shiftEmployee(); // it's shift Employee so get back to his menu
    }

    private void getShiftInfo(String type) { // for shift manager OR shift employee OR shift manager
        LocationDL branch = selectFromList("Select the Branch you want: ", branches.toArray(new LocationDL[0]));
        LocalDate dateOfShift = chooseDate("Please enter the date of the shift");
        ShiftType shiftType = selectFromList("Select Shift Type: ", ShiftType.values());
        try {
            Shift shift = employeeFacade.getShiftForEmployee(branch, dateOfShift, shiftType);
            employeeFacade.getShiftInfo(userId, shift);
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
        if (type.equals("shiftManager")) {
            shiftManager();
        } else if (type.equals("employeeManager")) {
            employeeManager();
        }
        shiftEmployee(); // it's shift Employee so get back to his menu
    }

    private void shiftEmployee() {
        String[] actions = {"Add Preferred Shift", "Remove Preferred Shift", "Show Shift Information",
            "Show my Preferences", "Show my Assigned Shifts", "Logout"};
        String option = selectFromList("Select Shift Employee Action:", actions);

        switch (option) {
            case "Add Preferred Shift":
                addPreferredShift("shiftEmployee");
                break;
            case "Remove Preferred Shift":
                removePreferredShift("shiftEmployee");
                break;
            case "Show Employee's shifts":
                getEmployeeShiftsAsShiftManager();
                break;
            case "Show Shift Information":
                getShiftInfo("shiftEmployee");
                break;
            case "Show my Preferences":
                getMyPreferences("shiftEmployee");
                break;
            case "Show my Assigned Shifts":
                getMyAssignedShifts("shiftEmployee");
                break;
            case "Logout":
                logout(userId);
                break;
            default:
                System.out.println("This is not a valid Shift Employee action");
                shiftEmployee();
                break;
        }
    }

    private void addPreferredShift(String type) {
        LocationDL branch = selectFromList("Select the Branch you want: ", branches.toArray(new LocationDL[0]));
        LocalDate dateOfShift = chooseDateForEmployee("please enter start date"); // choose date with helper method
        ShiftType shiftType = selectFromList("Select Shift Type: ", ShiftType.values());
        try {
            Shift shift = employeeFacade.getShiftForEmployee(branch, dateOfShift, shiftType);
            System.out.println("Shift from " + shift.getStartTime() + " until " + shift.getEndTime());
            employeeFacade.addPreferredShift(userId, shift);
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
        if (type.equals("shiftManager")) {
            shiftManager();
        }
        shiftEmployee(); // it's shift Employee so get back to his menu
    }

    private void removePreferredShift(String type) {
        LocationDL branch = selectFromList("Select the Branch you want: ", branches.toArray(new LocationDL[0]));
        LocalDate dateOfShift = chooseDateForEmployee("please enter start date"); // choose date with helper method
        ShiftType shiftType = selectFromList("Select Shift Type: ", ShiftType.values());
        try {
            Shift shift = employeeFacade.getShiftForEmployee(branch, dateOfShift, shiftType);
            employeeFacade.removePreferredShift(userId, shift);
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
        if (type.equals("shiftManager")) {
            shiftManager();
        }
        shiftEmployee(); // it's shift Employee so get back to his menu
    }

    // assistant methods
    private void ShowPrefAllEmployees() {
        System.out.println("First, you will see all employees along with their shift preferences.");
        System.out.println("Later, when assigning roles, you'll be shown only employees who are available.");
        System.out
                .println("You may still choose unavailable employees if needed, the system will alert you about that.");
        try {
            String result = employeeFacade.getPrefAllEmployees(userId);
            System.out.println(result);
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    private void chooseNumOfEmployeesForShift(Role role, Shift shift, int empManagerId) {
        while (true) {
            int numOfEmployees = readInt("Please enter the number of employees for " + role + ": ");
            try {
                employeeFacade.setRequiredRoles(empManagerId, shift, role, numOfEmployees);
                if (numOfEmployees == 0) {
                    return;
                }
                addEmployeesWithSameRoleToShift(shift, role, empManagerId, numOfEmployees);
                break;
            } catch (Exception e) {
                System.out.println("Failed to set number of employees for role " + role + ": " + e.getMessage());
            }
        }
    }

    private void addEmployeesWithSameRoleToShift(Shift shift, Role role, int empManagerId, int numOfEmployees) {
        for (int i = 0; i < numOfEmployees; i++) {
            while (true) {
                int employeeId = selectEmployeeForRole(shift, role);
                try {
                    employeeFacade.addEmployeeToShift(employeeId, shift, role, empManagerId);
                    break; // employee added successfully, move to the next one
                } catch (Exception e) {
                    System.out.println("Failed to add employee to shift: " + e.getMessage());
                    continue;
                }
            }
        }
    }

    private int selectEmployeeForExistingShift(Shift shift) { // method to replaceEmployee(), addEmployeeToExistShift(),
        // changeShiftManager()
        int employeeId = readInt("Please enter the ID of the employee who will join the shift: ");
        try {
            if (!employeeFacade.isAvailable(employeeId, shift)) {
                System.out.println("This employee is not available for this shift.");
                int choice = readInt(
                        "If you still want to add them, enter 1. Otherwise, enter any number other than 1: ");
                if (choice == 1) {
                    return employeeId; // confirmed override
                } else {
                    return -1; // invalid choice, return -1 to indicate no valid employee chosen
                }
            }
            return employeeId; // available employee
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
            return -1; // invalid employee choice, return -1 to indicate no valid employee chosen
        }
    }

    private int selectEmployeeForRole(Shift shift, Role role) { // method to setShifts()
        while (true) {
            System.out.println("Please choose " + role + " for the shift from the following available employees:");
            try {
                LocationDL branch = selectFromList("Select the Branch you want: ", branches.toArray(new LocationDL[0]));

                employeeFacade.getAvailableEmployees(userId, branch, shift, role);
                int employeeId = readInt("Please enter the ID of the employee: ");

                if (!employeeFacade.isAvailable(employeeId, shift)) {
                    System.out.println("This employee is not available for this shift.");
                    int choice = readInt(
                            "If you still want to add them, enter 1. Otherwise, enter any number other than 1: ");
                    if (choice == 1) {
                        return employeeId; // confirmed override
                    } else {
                        return -1; // invalid choice, return -1 to indicate no valid employee chosen
                    }
                }
                return employeeId; // available employee
            } catch (Exception e) {
                System.out.println("Error: " + e.getMessage());
                return -1; // invalid employee choice, return -1 to indicate no valid employee chosen
            }
        }
    }

    private int[] getValidShiftTimes(ShiftType shiftType) {
        Integer[] startTimeOptions = (shiftType == ShiftType.MORNING) ? MORNING_SHIFT_START_TIMES
                : EVENING_SHIFT_START_TIMES;

        Integer[] endTimeOptions = (shiftType == ShiftType.MORNING) ? MORNING_SHIFT_END_TIMES
                : EVENING_SHIFT_END_TIMES;

        // Start time selection
        int startTime = selectFromList("Select start time (24-hour format):", startTimeOptions);

        // End time selection with validation
        int endTime = selectFromList("Select end time (must be after start):", endTimeOptions);
        while (endTime <= startTime) {
            System.out.println("End time must be after start time. Please choose again.");
            endTime = selectFromList("Select end time (must be after start):", endTimeOptions);
        }

        return new int[]{startTime, endTime};
    }

    private <T> T selectFromList(String title, T[] options) {
        int choice = -1;
        while (choice < 1 || choice > options.length) {
            System.out.println(title);
            for (int i = 0; i < options.length; i++) {
                System.out.println((i + 1) + ". " + options[i].toString());
            }

            System.out.print("Enter choice (1-" + options.length + "): ");
            if (scanner.hasNextInt()) {
                choice = scanner.nextInt();
            } else {
                scanner.next();
                System.out.println("Invalid input, please enter a number.");
            }
            scanner.nextLine();
        }

        return options[choice - 1];
    }

    // private Role selectRole(String prompt) {
    // System.out.println(prompt);
    // Role[] roles = Role.values(); //Role.values() return the enum values
    //
    // for (int i = 0; i < roles.length; i++)
    // System.out.println((i + 1) + ". " + roles[i].toString());
    //
    // int choice = -1;
    // while (choice < 1 || choice > roles.length) {
    // System.out.print("Enter choice (1-" + roles.length + "): ");
    // if (scanner.hasNextInt())
    // choice = scanner.nextInt();
    // else {
    // scanner.next();
    // System.out.println("Invalid input, please enter a number.");
    // }
    // }
    // scanner.nextLine();
    // return roles[choice - 1];
    // }
    private int readInt(String prompt) {
        int value;
        while (true) {
            System.out.println(prompt);
            if (scanner.hasNextInt()) {
                value = scanner.nextInt();
                scanner.nextLine();
                return value;
            } else {
                System.out.println("Invalid input. Please enter a number.");
                scanner.nextLine();
            }
        }
    }

    private double readDouble(String prompt) {
        double value;
        while (true) {
            System.out.println(prompt);
            if (scanner.hasNextDouble()) {
                value = scanner.nextDouble();
                scanner.nextLine();
                return value;
            } else {
                System.out.println("Invalid input. Please enter a number.");
                scanner.nextLine();
            }
        }
    }

    private String readString(String prompt) {
        System.out.println(prompt);
        return scanner.nextLine();
    }

    private LocalDate chooseDate(String prompt) {
        String input = "";
        String pattern = "^(0[1-9]|[1-2][0-9]|3[01])-(0[1-9]|1[0-2])-(\\d{4})$";
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");

        while (true) {
            System.out.print(prompt + "(in format of dd-mm-yyyy) : ");
            input = scanner.nextLine();
            if (input.matches(pattern)) {
                try {
                    return LocalDate.parse(input, formatter);
                } catch (DateTimeParseException e) {
                    System.out.println("Invalid date format. Please try again.");
                }
            } else {
                System.out.println("Invalid format. Please use dd-mm-yyyy.");
            }
        }
    }

    private LocalDate chooseDateForManager(String prompt) {
        LocalDate date = chooseDate(prompt);
        if (!isValidDate(date)) {
            return chooseDateForManager(prompt);
        }

        if (!isNextWeek(date)) {
            return chooseDateForManager(prompt);
        }

        return date;
    }

    private LocalDate chooseDateForEmployee(String prompt) {
        LocalDate date = chooseDate(prompt);
        if (!isValidDate(date)) {
            return chooseDateForEmployee(prompt);
        }

        if (!isNextWeek(date)) {
            return chooseDateForEmployee(prompt);
        }

        return date;
    }

    private LocalDate chooseDateForAddEmployee(String prompt) {
        LocalDate date = chooseDate(prompt);
        if (!isValidDate(date)) {
            return chooseDateForAddEmployee(prompt);
        } else {
            return date;
        }
    }

    private LocalDate chooseDateForHire(String prompt) {
        LocalDate date = chooseDate(prompt);
        LocalDate oneMonthLater = nowDate.plusMonths(1);
        LocalDate oneMonthEarlier = nowDate.minusMonths(1);
        if (date.isAfter(oneMonthLater)) {
            System.out.println("Start date can't be more than 1 month in the future.");
            chooseDateForHire(prompt);
        } else if (date.isBefore(oneMonthEarlier)) {
            System.out.println("Start date can't be more than 1 month in the past.");
            chooseDateForHire(prompt);
        }
        return date;
    }

    private boolean isValidDate(LocalDate date) {
        // check if date is in the past
        if (date.isBefore(nowDate)) {
            System.out.println("You can't choose a past date, please choose again");
            return false;
        }

        // don't allow shifts on SHABBAT
        if (date.getDayOfWeek().getValue() == 6) {
            System.out.println("Shabbat is rest day, please choose again");
            return false;
        }
        return true;
    }

    private boolean isNextWeek(LocalDate date) {
        // Get start of current week
        LocalDate thisSunday = nowDate.with(TemporalAdjusters.previousOrSame(DayOfWeek.SUNDAY));
        LocalDate nextWeekSunday = thisSunday.plusWeeks(1);

        // Get the Sunday of the week the input date falls in
        LocalDate inputWeekSunday = date.with(TemporalAdjusters.previousOrSame(DayOfWeek.SUNDAY));

        long weeksBetween = ChronoUnit.WEEKS.between(thisSunday, inputWeekSunday);

        if (weeksBetween != 1) {
            System.out.println("You can only choose shift for NEXT week.");
            return false;
        }
        return true;
    }

    public void MakePredefinedData() {
        try {
            employeeFacade.MakePredefinedData();
            System.out.println("Predefined data created successfully.");
        } catch (Exception e) {
            System.out.println("Error creating predefined data: " + e.getMessage());
        }
    }

    public void ClearDataBase() {
        try {
            employeeFacade.ClearDataBase();
            System.out.println("Database cleared successfully.");
        } catch (Exception e) {
            System.out.println("Error clearing database: " + e.getMessage());
        }
    }

    public void loadData() {
        try {
            employeeFacade.loadData();
            System.out.println("Data loaded successfully.");
        } catch (Exception e) {
            System.out.println("Error loading data: " + e.getMessage());
        }
    }

}
