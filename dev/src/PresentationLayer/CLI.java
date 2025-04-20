package PresentationLayer;

import DomainLayer.*;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.time.temporal.ChronoUnit;
import java.util.Scanner;
//import java.util.regex.Matcher;
//import java.util.regex.Pattern;

public class CLI {
    public static Scanner scanner = new Scanner(System.in);
    EmployeeFacade employeeFacade;
    ShiftFacade shiftFacade;
    private int id;
    private String password;
    private LocalDate now;
    private static final Integer[] MORNING_SHIFT_START_TIMES = {
        500, 530, 600, 630, 700, 730, 800, 830, 900, 930, 1000};
    private static final Integer[] MORNING_SHIFT_END_TIMES = {
        1300, 1330, 1400, 1430, 1500, 1530, 1600, 1630, 1700, 1730, 1800};
    private static final Integer[] EVENING_SHIFT_START_TIMES = {
        1400, 1430, 1500, 1530, 1600, 1630, 1700, 1730 ,1800, 1830, 1900, 1930, 2000, 2030, 2100};
    private static final Integer[] EVENING_SHIFT_END_TIMES = {
        2100, 2130, 2200, 2230, 2300, 2330, 2400, 30, 100, 130, 200, 230, 300, 330, 400, 430, 500};    

    public CLI() {
        employeeFacade = new EmployeeFacade();
        now = LocalDate.now();
        loginCLI();
    }

    private void loginCLI() {
        System.out.println("LOGIN:");
        id = readInt("Please enter your ID:");
        password = readString("Please enter your password:");
       /* if (username.equals("abcde") && password.equals("12345"))
            addEmployeeManager();
        else {*/
            Employee emp = employeeFacade.login(id, password);
            if (emp == null) {
                System.out.println("Can't log in, please try again");
                loginCLI();
            } else if (emp instanceof EmployeeManager) {
                EmployeeManager();
            } else
                shiftEmployee();
        //}
    }

    private void EmployeeManager() {
        String[] actions = {"Set Shifts", "Add Employee to Exist Shift", "Fire Employee", "Hire Employee",
                            "Change Employee's Role", "Add Role to Employee", "Delete Employee's Role",
                            "Change Employee's Data", "Logout"};
        String option = selectFromList("Select Employee Manager Action:", actions);

        switch (option) {
            case "Set Shifts" -> setShifts();
            case "Add Employee to Exist Shift" -> addEmployeeToExistShift();
            case "Fire Employee" -> fireEmployee();
            case "Hire Employee" -> hireEmployee();
            case "Change Employee's Role" -> changeRoleToEmployee();
            case "Add Role to Employee" -> addRoleToEmployee();
            case "Delete Employee's Role" -> deleteRoleFromEmployee();
            case "Change Employee's Data" -> changeEmployeeData();
            case "Logout" -> {
                logout(id);
                loginCLI();
            }
            default -> {
                System.out.println("This is not a valid Employee Manager action");
                EmployeeManager();
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


    

    private void setShifts() {
        ShowPrefAllEmployees();
        LocalDate dateOfShift = chooseDate(); //choose date with helper method
        if(dateOfShift == null) 
            EmployeeManager(); // If date is invalid, return to EmployeeManager

        //choose shift type and hours with helper method
        ShiftType shiftType = selectFromList("Select Shift Type: ", ShiftType.values());
        int[] shiftTimes = getValidShiftTimes(shiftType);
        int startTime = shiftTimes[0];
        int endTime = shiftTimes[1];
    
        shiftFacade.createShift(dateOfShift, shiftType, startTime, endTime, id, -1);
        Shift shift = new Shift(dateOfShift, shiftType, startTime, endTime, -1);

        //choose shift manager
        int shiftManagerId = getEmployeeForShift(shift, Role.SHIFT_MANAGER);
        shiftFacade.setShiftManager(id, shift, shiftManagerId);

        //choose number of employees for each role
        for (Role role : Role.values()) {
            if (role != Role.SHIFT_MANAGER) 
                chooseNumOfEmployeesForShift(role, shift, id);
        }
        EmployeeManager(); 
    }

    private void addEmployeeToExistShift() {
        ShowPrefAllEmployees();
        LocalDate dateOfShift = chooseDate(); //choose date with helper method
        if(dateOfShift == null) 
            EmployeeManager(); // If date is invalid, return to EmployeeManager
        ShiftType shiftType = selectFromList("Select Shift Type: ", ShiftType.values());
        Shift shift = shiftFacade.getShift(dateOfShift, shiftType, id);
        if(shift == null) {
            System.out.println("Shift not found OR you are not connected. please try again");
            EmployeeManager();
        }
        Role role = selectFromList("Choose a role:", Role.values());
        int employeeId = getEmployeeForShift(shift, role);
        String response = shiftFacade.addEmployeeToShift(employeeId, shift, role, id);
        if(response != null)
            System.out.println(response);
        EmployeeManager();
     }

    private void fireEmployee() {
        int employeeId = readInt("Please enter Employee's ID to fire: ");
        String response = employeeFacade.fireEmployee(employeeId, id);
        if(response != null)
            System.out.println(response);
        EmployeeManager();
    }

    private void hireEmployee() {
        System.out.println("Please enter the new Employee details");
        int employeeID = readInt("ID: ");
        String name = readString("Name");
        String bankAccount = readString("Bank Account: ");
        int salary = readInt("Salary: ");
        LocalDate startDate = readDate("Start Date: ");
        int vacationDays = readInt("Vacation Days");
        int sickDays = readInt("Sick Days");
        double educationFund = readDouble("Education fund: ");
        double socialBenefits = readDouble("Social Benefits: ");
        String employeePassword = readString("Password: ");
        String branch = readString("Branch: ");

        //choose role
        Role selectedRole = selectFromList("Choose a role:", Role.values());
        String response = employeeFacade.hireEmployee(employeeID, id, name, branch ,bankAccount, salary, startDate,
                vacationDays, sickDays, educationFund, socialBenefits, employeePassword, selectedRole);
        if(response != null)
            System.out.println(response);
        EmployeeManager();
    }

    private void changeRoleToEmployee()
    {
        int employeeID = readInt("Please enter employee ID: ");
        Role oldRole = selectFromList("Choose Role To Change:", Role.values());
        Role newRole = selectFromList("To What Role:", Role.values());

        String response = employeeFacade.changeRoleToEmployee(employeeID, id, oldRole, newRole);
        if (response != null)
            System.out.println(response);
        EmployeeManager();
    }

    private void addRoleToEmployee() {
        int employeeID = readInt("Please enter employee ID: ");
        Role newRole = selectFromList("Choose Role To Add:", Role.values());
        String response = employeeFacade.addRoleToEmployee(employeeID, id, newRole);
        if(response != null)
            System.out.println(response);
        EmployeeManager();
    }

    private void deleteRoleFromEmployee() {
        int employeeID = readInt("Please enter employee ID: ");
        Role toDelete = selectFromList("Choose Role To Delete:", Role.values());
        String response = employeeFacade.deleteRoleFromEmployee(employeeID, id, toDelete);
        if(response != null)
            System.out.println(response);
        EmployeeManager();
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

    private void updateSalary() {
        int employeeId = readInt("Please enter employee ID: ");
        int salary = readInt("Enter the new Salary: ");
        String response = employeeFacade.updateSalary(employeeId, id, salary);
        if(response != null)
            System.out.println(response);
        EmployeeManager();
    }

    private void updateBankAccount() {
        int employeeId = readInt("Please enter employee ID: ");
        String bankAccount = readString("Enter the new Bank Account: ");
        String response = employeeFacade.updateBankAccount(employeeId, id, bankAccount);
        if(response != null)
            System.out.println(response);
        EmployeeManager();
    }
    private void updateVacationDays() {
        int employeeId = readInt("Please enter employee ID: ");
        int vacationDays = readInt("Enter the new Vacation Days: ");
        String response = employeeFacade.updateVacationDays(employeeId, id, vacationDays);
        if(response != null)
            System.out.println(response);
        EmployeeManager();
    }

    private void updateSickDays() {
        int employeeId = readInt("Please enter employee ID: ");
        int sickDays = readInt("Enter the new Sick Days: ");
        String response = employeeFacade.updateSickDays(employeeId, id, sickDays);
        if(response != null)
            System.out.println(response);
        EmployeeManager();
    }

    private void updateEducationFund() {
        int employeeId = readInt("Please enter employee ID: ");
        double educationFund = readDouble("Enter the new Education Fund: ");
        String response = employeeFacade.updateEducationFund(employeeId, id, educationFund);
        if(response != null)
            System.out.println(response);
        EmployeeManager();
    }

    private void updateSocialBenefits() {
        int employeeId = readInt("Please enter employee ID: ");
        double socialBenefits = readDouble("Enter the new Education Fund: ");
        String response = employeeFacade.updateSocialBenefits(employeeId, id, socialBenefits);
        if(response != null)
            System.out.println(response);
        EmployeeManager();

    }

    // private void updatePassword() {
    //     int employeeId = readInt("Please enter employee ID: ");
    //     String password = readString("Enter the new Password: ");
    //     String response = employeeFacade.updatePassword(employeeId, id, password);
    //     if(response != null)
    //         System.out.println(response);
    //     EmployeeManager();
    // }

    private void logout(int id) {
        employeeFacade.logout(id);
    }

    private void shiftEmployee() {
        String[] actions = {"Set Preferences", "Get Preferences", "Logout"};
        String option = selectFromList("Select Shift Employee Action:", actions);

        switch (option) {
            case "Set Preferences" -> setPreferences();
            case "Get Preferences" -> getPrefEmployee();
            case "Logout" -> {
                logout(id);
                loginCLI();
            }
            default -> {
                System.out.println("This is not a valid Shift Employee action");
                shiftEmployee();
            }
        }
    }
    private void getPrefEmployee() {
        System.out.println(employeeFacade.getPrefEmployee(id));
        shiftEmployee();
    }

    

    //assistant methods

    private void ShowPrefAllEmployees() {
        System.out.println("First, you will see all employees along with their shift preferences.");
        System.out.println("Later, when assigning roles, you'll be shown only employees who are available.");
        System.out.println("You may still choose unavailable employees if needed — the system will alert you about that.");
        System.out.println(employeeFacade.getPrefAllEmployees(id));
    }

    private void chooseNumOfEmployeesForShift(Role role, Shift shift, int empManagerId) {
        while (true) {
            int numOfEmployees = readInt("Please enter the number of employees for " + role + ": ");
            String response = shiftFacade.setRequiredRoles(empManagerId, shift, role, numOfEmployees);
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
                int employeeId = getEmployeeForShift(shift, role);
                String res = shiftFacade.addEmployeeToShift(employeeId, shift, role, empManagerId);
                if(res != null) {
                    System.out.println(res);
                    continue; //ask for the employee again
                }
                break; //employee added successfully, move to the next one
            }
        }
    }

    private int getEmployeeForShift(Shift shift, Role role) {
    while (true) {
        System.out.println("Please choose " + role + " for the shift from the following available employees:");
        System.out.println(employeeFacade.getAvailableEmployees(shift, role));
        int employeeId = readInt("Please enter the ID of the employee: ");

        if (!shiftFacade.isAvailable(employeeId, shift)) {
            System.out.println("This employee is not available for this shift.");
            int choice = readInt("If you still want to add them, enter 1. Otherwise, enter 0: ");
            if (choice == 1) {
                return employeeId; // confirmed override
            } else {
                System.out.println("Please choose another employee.");
                continue;
            }
        }
        return employeeId; // available employee
        }
    }

    private LocalDate chooseDate() {
        LocalDate dateOfShift = readDate("Please enter the date of the shift ");
        DayOfWeek today = now.getDayOfWeek();

        //check if it's Thursday/Friday/Saturday
        if (today.getValue() < DayOfWeek.THURSDAY.getValue() || today.getValue() > DayOfWeek.SATURDAY.getValue()) {
            System.out.println("You can only set shifts from Thursday to Saturday");
            return null; // Return null to indicate invalid date
        }
        //check if date is in the past
        if(dateOfShift.isBefore(now)) {
            System.out.println("You can't set shifts to the past, choose again");
            return null; 
        }
         //allow shifts only for next week
        //now.with(DayOfWeek.SUNDAY) gets the most recent Sunday before/equal to today.
        long weeksBetween = ChronoUnit.WEEKS.between(now.with(DayOfWeek.SUNDAY), dateOfShift.with(DayOfWeek.SUNDAY));
        if (weeksBetween != 1) {
            System.out.println("You can only set shift for NEXT week.");
            return null;
        }

        //don't allow shifts on SHABBAT
        if(dateOfShift.getDayOfWeek().getValue() == 7) {
            System.out.println("Shabbat is rest day, please choose again");
            return null;
        }
        return dateOfShift;
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

    private LocalDate readDate(String prompt) {
        String input = "";
        String pattern = "^(0[1-9]|[1-2][0-9]|3[01])-(0[1-9]|1[0-2])-(\\d{4})$";
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");

        while (true) {
            System.out.print(prompt + "(in format of dd-mm-yyyy)");
            input = scanner.nextLine();
            if (input.matches(pattern)) {
                try {
                    return LocalDate.parse(input, formatter);
                } catch (DateTimeParseException e) {
                    // just in case regex filters didnt catch the error
                    System.out.println("Invalid date format. Please try again.");
                }
            } else
                System.out.println("Invalid format. Please use dd-mm-yyyy.");
        }
    }
}
