package PresentationLayer;

import DomainLayer.*;

import javax.sound.midi.SysexMessage;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.time.temporal.ChronoUnit;
import java.time.temporal.TemporalAdjusters;
import java.util.Map;
import java.util.Scanner;

public class CLI {
    public static Scanner scanner = new Scanner(System.in);
    EmployeeFacade employeeFacade;
    private int userId;
    private String password;
    private LocalDate nowDate;
    private static final Integer[] MORNING_SHIFT_START_TIMES = {
            500, 530, 600, 630, 700, 730, 800, 830, 900, 930, 1000};
    private static final Integer[] MORNING_SHIFT_END_TIMES = {
            1300, 1330, 1400, 1430, 1500, 1530, 1600, 1630, 1700, 1730, 1800};
    private static final Integer[] EVENING_SHIFT_START_TIMES = {
            1400, 1430, 1500, 1530, 1600, 1630, 1700, 1730 ,1800, 1830, 1900, 1930, 2000, 2030, 2100};
    private static final Integer[] EVENING_SHIFT_END_TIMES = {
            2100, 2130, 2200, 2230, 2300, 2330, 2400, 30, 100, 130, 200, 230, 300, 330, 400, 430, 500};

    public CLI(EmployeeFacade employeeFacade) {
        this.employeeFacade = employeeFacade;
        nowDate = LocalDate.now();
        loginCLI();
    }

    private void loginCLI() {
        System.out.println("LOGIN:");
        userId = readInt("Please enter your ID:");
        password = readString("Please enter your password:");
       /* if (username.equals("abcde") && password.equals("12345"))
            addEmployeeManager();
        else {*/
        Employee emp = employeeFacade.login(userId, password);
        if (emp == null) {
            System.out.println("Can't log in, please try again");
            loginCLI();
        }
        else if (emp instanceof EmployeeManager)
            employeeManager();
        else if(employeeFacade.isShiftManager(userId))
            shiftManager();
        else
            shiftEmployee();
        //}
    }

    private void employeeManager() {
        String[] actions = {"Create Shifts", "Set Shifts", "Add Employee to Exist Shift", "Remove Employee From Exist Shift",
                "Fire Employee", "Hire Employee", "Change Employee's Role",
                "Add Role to Employee", "Change Shift Manager", "Replace Employee",
                "Delete Employee's Role", "Change Employee's Data", "Show Past Shifts",
                "Show Employee's shifts", "Logout"};
        String option = selectFromList("Select Employee Manager Action (Enter the number)", actions);

        switch (option) {
            case "Create Shifts" -> autoCreateShifts();
            case "Set Shifts" -> setShifts();
            case "Add Employee to Exist Shift" -> addEmployeeToExistingShift();
            case "Remove Employee From Exist Shift" -> removeEmployeeFromShift();
            case "Fire Employee" -> fireEmployee();
            case "Hire Employee" -> hireEmployee();
            case "Change Employee's Role" -> changeRoleToEmployee();
            case "Add Role to Employee" -> addRoleToEmployee();
            case "Change Shift Manager " -> changeShiftManager();
            case "Replace Employee" -> replaceEmployee();
            case "Delete Employee's Role" -> deleteRoleFromEmployee();
            case "Change Employee's Data" -> changeEmployeeData();
            case "Show Past Shifts" -> getPastShifts();
            //case "Show Employee's preferences" -> getPrefEmployee("employee manager");
            case "Show Employee's shifts" -> getEmployeeShiftsAsEmployeeManager();
            case "Logout" -> logout(userId);
            default -> {
                System.out.println("This is not a valid Employee Manager action");
                employeeManager();
            }
        }
    }

//    we will implement this probably in the next assignment
//    private void addEmployeeManager() {
//        System.out.println("Enter the details of the new Employee Manager");
//        System.out.println("ID:");
//        int id = scanner.nextInt();
//        System.out.println("username:");
//        username = String.valueOf(scanner.nextInt());
//        System.out.println("Please enter your password:");
//        password = scanner.nextLine();
//        //fill in the rest of the fields of Employee
//    }


    private void autoCreateShifts() {
        String result = employeeFacade.autoCreateShiftsForNextWeek(userId);
        if (result != null)
            System.out.println(result);
        else
            System.out.println("Shifts for next week created successfully.");
        employeeManager();
    }


    private void setShifts() {
        ShowPrefAllEmployees();
        LocalDate dateOfShift = chooseDateForManager("please enter start date"); //choose date with helper method
        ShiftType shiftType = selectFromList("Select Shift Type: ", ShiftType.values());

        employeeFacade.createShift(dateOfShift, shiftType, userId, -1);
        Shift shift = new Shift(dateOfShift, shiftType, -1);

        //choose shift manager
        int shiftManagerId = selectEmployeeForRole(shift, Role.SHIFT_MANAGER);
        employeeFacade.setShiftManager(userId, shift, shiftManagerId);

        //choose number of employees for each role
        for (Role role : Role.values()) {
            if (role != Role.SHIFT_MANAGER)
                chooseNumOfEmployeesForShift(role, shift, userId);
        }
        employeeManager();
    }

    private void addEmployeeToExistingShift() {
        ShowPrefAllEmployees();
        LocalDate dateOfShift = chooseDateForAddEmployee("Please enter Date");
        ShiftType shiftType = selectFromList("Select Shift Type: ", ShiftType.values());
        Shift shift = employeeFacade.getShift(dateOfShift, shiftType, userId);
        if(shift == null) {
            System.out.println("Shift not found OR you are not connected. please try again");
            employeeManager();
        }

        int newEmployeeId = selectEmployeeForExistingShift(shift);
        if(newEmployeeId == -1) {
            System.out.println("Invalid employee choice, please try again.");
            employeeManager();
        }

        Role role = selectFromList("Choose a role:", Role.values());
        if(role == Role.SHIFT_MANAGER) {
            System.out.println("You can't add a shift manager to an existing shift, please choose another role.");
            employeeManager();
        }

        String response = employeeFacade.addEmployeeToShift(newEmployeeId, shift, role, userId);
        System.out.println(response);
        employeeManager();
    }

    private void removeEmployeeFromShift() {
        int employeeId = readInt("Please enter Employee's ID to remove: ");
        LocalDate dateOfShift = chooseDate("Please enter the date of the shift");
        ShiftType shiftType = selectFromList("Select Shift Type: ", ShiftType.values());
        Shift shift = employeeFacade.getShift(dateOfShift, shiftType, userId);
        if(shift == null) {
            System.out.println("Shift not found OR you are not connected. please try again");
            employeeManager();
        }
        String response = employeeFacade.removeEmployeeFromShift(employeeId, shift, userId);
        if(response != null)
            System.out.println(response);
        employeeManager();
    }

    private void fireEmployee() {
        int employeeId = readInt("Please enter Employee's ID to fire: ");
        String response = employeeFacade.fireEmployee(employeeId, userId);
        System.out.println(response);
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
        String branch = readString("Branch: ");

        //choose role
        Role selectedRole = selectFromList("Choose a role:", Role.values());
        String response = employeeFacade.hireEmployee(employeeID, userId, name, branch ,bankAccount, salary, startDate,
                vacationDays, sickDays, educationFund, socialBenefits, employeePassword, selectedRole);
        System.out.println(response);
        employeeManager();
    }

    private void changeRoleToEmployee()
    {
        int employeeID = readInt("Please enter employee ID: ");
        Role oldRole = selectFromList("Choose Role To Change:", Role.values());
        Role newRole = selectFromList("To What Role:", Role.values());

        String response = employeeFacade.changeRoleToEmployee(employeeID, userId, oldRole, newRole);
        if (response != null)
            System.out.println(response);
        employeeManager();
    }

    private void addRoleToEmployee() {
        int employeeID = readInt("Please enter employee ID: ");
        Role newRole = selectFromList("Choose Role To Add:", Role.values());
        String response = employeeFacade.addRoleToEmployee(employeeID, userId, newRole);
        if(response != null)
            System.out.println(response);
        employeeManager();
    }

    private void changeShiftManager() {
        int oldShiftManagerId = readInt("Please enter old shift manager ID: ");
        LocalDate dateOfShift = chooseDate("Please enter the date of the shift");
        ShiftType shiftType = selectFromList("Select Shift Type: ", ShiftType.values());
        Shift shift = employeeFacade.getShift(dateOfShift, shiftType, userId);
        if(shift == null) {
            System.out.println("Shift not found OR you are not connected. please try again");
            employeeManager();
        }
        int newShiftManagerId = selectEmployeeForExistingShift(shift);
        String response = employeeFacade.changeShiftManager(shift, oldShiftManagerId, newShiftManagerId, userId);
        if(response != null)
            System.out.println(response);
        employeeManager();
    }

    private void replaceEmployee() {
        ShowPrefAllEmployees();
        int oldEmployeeId = readInt("Please enter the ID of the employee you want to replace: ");
        LocalDate dateOfShift = chooseDate("Please enter the date of the shift");
        ShiftType shiftType = selectFromList("Select Shift Type: ", ShiftType.values());
        Shift shift = employeeFacade.getShift(dateOfShift, shiftType, userId);
        if(shift == null) {
            System.out.println("Shift not found OR you are not connected. please try again");
            employeeManager();
        }
        int newEmployeeId = selectEmployeeForExistingShift(shift);
        if(newEmployeeId == -1) {
            System.out.println("Invalid employee choice, please try again.");
            employeeManager();
        }
        String response = employeeFacade.shiftReplacement(shift, oldEmployeeId, userId, newEmployeeId);
        if(response != null)
            System.out.println(response);
        employeeManager();
    }

    private void deleteRoleFromEmployee() {
        int employeeID = readInt("Please enter employee ID: ");
        Role toDelete = selectFromList("Choose Role To Delete:", Role.values());
        String response = employeeFacade.deleteRoleFromEmployee(employeeID, userId, toDelete);
        if(response != null)
            System.out.println(response);
        employeeManager();
    }

    private void changeEmployeeData() {
        String[] labels = {"Salary", "Bank Account", "Vacation Days", "Sick Days", "Education Fund",
                "Social Benefits"};
        String option = selectFromList("Select Employee Data to change:", labels);
        switch (option) {
            case "Salary" -> updateSalary();
            case "Bank Account" -> updateBankAccount();
            case "Vacation Days" -> updateVacationDays();
            case "Sick Days" -> updateSickDays();
            case "Education Fund" -> updateEducationFund();
            case "Social Benefits" -> updateSocialBenefits();
            // case "Password" -> updatePassword();
        }
    }

    private void getPastShifts() {
        Map<LocalDate, Shift> shifts = employeeFacade.getPastShifts(userId);
        if (shifts.isEmpty())
            System.out.println("No past shifts found or you are not logged in.");
        else {
            System.out.println("Past Shifts:");
            for (Map.Entry<LocalDate, Shift> entry : shifts.entrySet()) {
                Shift shift = entry.getValue();
                System.out.println(shift.toString());}
        }
        employeeManager();
    }

//    private void getPrefEmployee(String type) {
//        int employeeId = readInt("Please enter Employee's ID: ");
//        String response = employeeFacade.getPreferredShiftsEmployee(userId, employeeId);
//        if (response != null)
//            System.out.println(response);
//        if(type.equals("shift manager"))
//            shiftManager();
//        employeeManager();//it's Employee Manager so get back to his menu
//    }

    private void getEmployeeShiftsAsEmployeeManager() {
        int employeeId = readInt("Please enter Employee's ID: ");
        String response = employeeFacade.getAssignedEmployeeShiftsManager(employeeId, userId);
        if (response != null)
            System.out.println(response);
        employeeManager();
    }

    private void logout(int id) {
        String response = employeeFacade.logout(id);
        if(response != null)
            System.out.println(response);
        loginCLI();
    }

    private void updateSalary() {
        int employeeId = readInt("Please enter employee ID: ");
        int salary = readInt("Enter the new Salary: ");
        String response = employeeFacade.updateSalary(employeeId, userId, salary);
        if(response != null)
            System.out.println(response);
        employeeManager();
    }

    private void updateBankAccount() {
        int employeeId = readInt("Please enter employee ID: ");
        String bankAccount = readString("Enter the new Bank Account: ");
        String response = employeeFacade.updateBankAccount(employeeId, userId, bankAccount);
        if(response != null)
            System.out.println(response);
        employeeManager();
    }
    private void updateVacationDays() {
        int employeeId = readInt("Please enter employee ID: ");
        int vacationDays = readInt("Enter the new Vacation Days: ");
        String response = employeeFacade.updateVacationDays(employeeId, userId, vacationDays);
        if(response != null)
            System.out.println(response);
        employeeManager();
    }

    private void updateSickDays() {
        int employeeId = readInt("Please enter employee ID: ");
        int sickDays = readInt("Enter the new Sick Days: ");
        String response = employeeFacade.updateSickDays(employeeId, userId, sickDays);
        if(response != null)
            System.out.println(response);
        employeeManager();
    }

    private void updateEducationFund() {
        int employeeId = readInt("Please enter employee ID: ");
        double educationFund = readDouble("Enter the new Education Fund: ");
        String response = employeeFacade.updateEducationFund(employeeId, userId, educationFund);
        if(response != null)
            System.out.println(response);
        employeeManager();
    }

    private void updateSocialBenefits() {
        int employeeId = readInt("Please enter employee ID: ");
        double socialBenefits = readDouble("Enter the new Education Fund: ");
        String response = employeeFacade.updateSocialBenefits(employeeId, userId, socialBenefits);
        if(response != null)
            System.out.println(response);
        employeeManager();

    }

    // private void updatePassword() {
    //     int employeeId = readInt("Please enter employee ID: ");
    //     String password = readString("Enter the new Password: ");
    //     String response = employeeFacade.updatePassword(employeeId, id, password);
    //     if(response != null)
    //         System.out.println(response);
    //     EmployeeManager();
    // }

    private void shiftManager() {
        String[] actions = {"Add Preferred Shift", "Remove Preferred Shift",
                "Show Employee's shifts", "Show Shift Information" , "Show my Preferences",
                "Show my Assigned Shifts", "Logout"};
        String option = selectFromList("Select Shift Manager Action:", actions);

        switch (option) {
            case "Add Preferred Shift" -> addPreferredShift("shift manager");
            case "Remove Preferred Shift" -> removePreferredShift("shift manager");
            //case "Show Employee's preferences" -> getPrefEmployee("shift manager");
            case "Show Employee's shifts" -> getEmployeeShiftsAsShiftManager();
            case "Show Shift Information" -> getShiftInfo("shift manager");
            case "Show my Preferences" -> getMyPreferences("shift manager");
            case "Show my Assigned Shifts" -> getMyAssignedShifts("shift manager");
            case "Logout" -> logout(userId);
            default -> {
                System.out.println("This is not a valid Shift Manager action");
                shiftManager();
            }
        }
    }

    private void getEmployeeShiftsAsShiftManager() {
        int employeeId = readInt("Please enter Employee's ID: ");
        String response = employeeFacade.getAssignedEmployeeShiftsAsShiftManager(employeeId, userId);
        if (response != null)
            System.out.println(response);
        shiftManager();
    }

    private void getMyAssignedShifts(String type) {
        System.out.println(employeeFacade.getAssignedEmployeeShiftsEmployee(userId));
        if(type.equals("shift manager"))
            shiftManager();
        shiftEmployee(); //it's shift Employee so get back to his menu
    }

    private void getMyPreferences(String type) {
        System.out.println(employeeFacade.getPreferredShiftsEmployee(userId));
        if(type.equals("shift manager"))
            shiftManager();
        shiftEmployee(); //it's shift Employee so get back to his menu
    }

    private void getShiftInfo(String type) { //for shift manager OR shift employee
        LocalDate dateOfShift = chooseDate("Please enter the date of the shift");
        ShiftType shiftType = selectFromList("Select Shift Type: ", ShiftType.values());
        Shift shift = employeeFacade.getShiftForEmployee(dateOfShift, shiftType);
        if(shift == null) {
            System.out.println("Shift not found OR you are not connected. please try again");
            shiftManager();
        }
        String response = employeeFacade.getShiftInfo(userId, shift);
        System.out.println(response);
        if(type.equals("shift manager"))
            shiftManager();
        shiftEmployee(); //it's shift Employee so get back to his menu
    }

    private void shiftEmployee() {
        String[] actions = {"Add Preferred Shift", "Remove Preferred Shift", "Show Shift Information",
                "Show my Preferences", "Show my Shifts", "Logout"};
        String option = selectFromList("Select Shift Employee Action:", actions);

        switch (option) {
            case "Add Preferred Shift" -> addPreferredShift("shift employee");
            case "Remove Preferred Shift" -> removePreferredShift("shift employee");
            case "Show Shift Information" -> getShiftInfo("shift employee");
            case "Show my Preferences" -> getMyPreferences("shift employee");
            case "Show my Shifts" -> getMyAssignedShifts("shift employee");
            case "Logout" -> logout(userId);
            default -> {
                System.out.println("This is not a valid Shift Employee action");
                shiftEmployee();
            }
        }
    }

    private void addPreferredShift(String type) {
        LocalDate dateOfShift = chooseDateForEmployee("please enter start date"); //choose date with helper method
        ShiftType shiftType = selectFromList("Select Shift Type: ", ShiftType.values());
        Shift shift = employeeFacade.getShiftForEmployee(dateOfShift, shiftType);
        String response = employeeFacade.addPreferredShift(userId, shift);
        if(response != null)
            System.out.println(response);
        if(type.equals("shift manager"))
            shiftManager();
        shiftEmployee(); //it's shift Employee so get back to his menu
    }

    private void removePreferredShift(String type) {
        LocalDate dateOfShift = chooseDateForEmployee("please enter start date"); //choose date with helper method
        ShiftType shiftType = selectFromList("Select Shift Type: ", ShiftType.values());
        Shift shift = employeeFacade.getShiftForEmployee(dateOfShift, shiftType);
        if(shift == null) {
            System.out.println("Shift not found OR you are not connected. please try again");
            shiftEmployee();
        }
        String response = employeeFacade.removePreferredShift(userId, shift);
        if(response != null)
            System.out.println(response);
        if(type.equals("shift manager"))
            shiftManager();
        shiftEmployee(); //it's shift Employee so get back to his menu
    }

    //assistant methods

    private void ShowPrefAllEmployees() {
        System.out.println("First, you will see all employees along with their shift preferences.");
        System.out.println("Later, when assigning roles, you'll be shown only employees who are available.");
        System.out.println("You may still choose unavailable employees if needed, the system will alert you about that.");
        System.out.println(employeeFacade.getPrefAllEmployees(userId));
    }

    private void chooseNumOfEmployeesForShift(Role role, Shift shift, int empManagerId) {
        while (true) {
            int numOfEmployees = readInt("Please enter the number of employees for " + role + ": ");
            String response = employeeFacade.setRequiredRoles(empManagerId, shift, role, numOfEmployees);
            if (response != null) {
                System.out.println(response);
                continue;
            }
            addEmployeesWithSameRoleToShift(shift, role, empManagerId, numOfEmployees);
            break;
        }
    }

    private void addEmployeesWithSameRoleToShift(Shift shift, Role role, int empManagerId, int numOfEmployees) {
        for (int i = 0; i < numOfEmployees; i++) {
            while (true) {
                int employeeId = selectEmployeeForRole(shift, role);
                String res = employeeFacade.addEmployeeToShift(employeeId, shift, role, empManagerId);
                if(!res.equals("Employee added to shift successfully")) {
                    System.out.println(res);
                    continue; //ask for the employee again
                }
                break; //employee added successfully, move to the next one
            }
        }
    }

    private int selectEmployeeForExistingShift(Shift shift) { //method to replaceEmployee(), addEmployeeToExistShift(), changeShiftManager()
        int employeeId = readInt("Please enter the ID of the employee who will join the shift: ");
        if(!employeeFacade.isAvailable(employeeId, shift)) {
            System.out.println("This employee is not available for this shift.");
            int choice = readInt("If you still want to add them, enter 1. Otherwise, enter any number other than 1: ");
            if (choice == 1)
                return employeeId; // confirmed override
            else
                return -1; // invalid choice, return -1 to indicate no valid employee chosen
        }
        return employeeId; // available employee
    }

    private int selectEmployeeForRole(Shift shift, Role role) { //method to setShifts()
        while (true) {
            System.out.println("Please choose " + role + " for the shift from the following available employees:");
            System.out.println(employeeFacade.getAvailableEmployees(userId, shift, role));
            int employeeId = readInt("Please enter the ID of the employee: ");

            if (!employeeFacade.isAvailable(employeeId, shift)) {
                System.out.println("This employee is not available for this shift.");
                int choice = readInt("If you still want to add them, enter 1. Otherwise, enter 0: ");
                if (choice == 1) {
                    return employeeId; // confirmed override
                } else {
                    continue;
                }
            }
            return employeeId; // available employee
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

//    private Role selectRole(String prompt) {
//        System.out.println(prompt);
//        Role[] roles = Role.values(); //Role.values() return the enum values
//
//        for (int i = 0; i < roles.length; i++)
//            System.out.println((i + 1) + ". " + roles[i].toString());
//
//        int choice = -1;
//        while (choice < 1 || choice > roles.length) {
//            System.out.print("Enter choice (1-" + roles.length + "): ");
//            if (scanner.hasNextInt())
//                choice = scanner.nextInt();
//            else {
//                scanner.next();
//                System.out.println("Invalid input, please enter a number.");
//            }
//        }
//        scanner.nextLine();
//        return roles[choice - 1];
//    }


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
            }
            else {
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
        LocalTime nowTime = LocalTime.now();
        DayOfWeek today = nowDate.getDayOfWeek();

        boolean isAllowedTime =
                (today == DayOfWeek.THURSDAY && nowTime.isAfter(LocalTime.of(17, 30))) ||
                        (today == DayOfWeek.FRIDAY) ||
                        (today == DayOfWeek.SATURDAY);
        if(!isAllowedTime) {
            System.out.println("You allow to do this action from Thursday 17:30 and all day Friday and Saturday");
            System.out.println("Now, you will return to the main menu");
            employeeManager();
        }

        //check if date is in the past OR SHABBAT
        if(!isValidDate(date))
            return chooseDateForManager(prompt);

        if(!isNextWeek(date))
            return chooseDateForManager(prompt);

        return date;
    }

    private LocalDate chooseDateForEmployee(String prompt) {
        LocalDate date = chooseDate(prompt);
        LocalTime nowTime = LocalTime.now();
        DayOfWeek today = nowDate.getDayOfWeek();

        //זמני כדי שנוכל להמשיך לעבוד היום, מחר אשנה את זה לשבת ובסוף אחזיר לחמישי ב17:00
//        boolean isAllowedTime =
//                (today.getValue() >= DayOfWeek.SUNDAY.getValue() &&
//                        today.getValue() <= DayOfWeek.WEDNESDAY.getValue()) ||
//                        (today == DayOfWeek.THURSDAY && nowTime.isBefore(LocalTime.of(17, 0)));
        boolean isAllowedTime = today == DayOfWeek.FRIDAY;
        if (!isAllowedTime) {
            System.out.println("You are only allowed to do this action from Sunday until Thursday 17:00.");
            System.out.println("Now, you will return to the main menu.");
            shiftEmployee();
        }

        //check if date is in the past OR SHABBAT
        if(!isValidDate(date))
            return chooseDateForEmployee(prompt);

        if(!isNextWeek(date))
            return chooseDateForEmployee(prompt);

        return date;
    }

    private LocalDate chooseDateForAddEmployee(String prompt) {
        LocalDate date = chooseDate(prompt);
        if(!isValidDate(date))
            return chooseDateForAddEmployee(prompt);
        else
            return date;
    }

    private LocalDate chooseDateForHire(String prompt) {
        LocalDate date = chooseDate(prompt);
        LocalDate oneMonthLater = nowDate.plusMonths(1);
        LocalDate oneMonthEarlier = nowDate.minusMonths(1);
        if (date.isAfter(oneMonthLater)) {
            System.out.println("Start date can't be more than 1 month in the future.");
            chooseDateForHire(prompt);
        }
        else if(date.isBefore(oneMonthEarlier)) {
            System.out.println("Start date can't be more than 1 month in the past.");
            chooseDateForHire(prompt);
        }
        return date;
    }

    private boolean isValidDate(LocalDate date) {
        //check if date is in the past
        if(date.isBefore(nowDate)) {
            System.out.println("You can't choose a past date, please choose again");
            return false;
        }

        //don't allow shifts on SHABBAT
        if(date.getDayOfWeek().getValue() == 6) {
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

}
