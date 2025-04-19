package PresentationLayer;

import DomainLayer.*;

import java.time.LocalDate;
import java.time.DayOfWeek;
import java.time.format.DateTimeParseException;
import java.time.temporal.ChronoUnit;
import java.time.format.DateTimeFormatter;
import java.util.Scanner;
//import java.util.regex.Matcher;
//import java.util.regex.Pattern;

public class CLI {
    public static Scanner scanner = new Scanner(System.in);
    EmployeeFacade employeeFacade;
    private int id;
    private String password;
    private LocalDate now;
    

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
        String[] actions = {"Set Shifts", "Fire Employee", "Hire Employee", "Change Employee's Role",
                "Add Role to Employee", "Delete Employee's Role", "Change Employee's Data", "Logout"};
        String option = selectFromList("Select Employee Manager Action:", actions);

        switch (option) {
            case "Set Shifts" -> setShifts();
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
        //String pref = employeeFacade.getPreferences(id); not sure we need id
        LocalDate date = readDate("Please enter the date of the shift ");
        DayOfWeek today = now.getDayOfWeek();

        //check if it's Thursday or later
        if (today.getValue() < DayOfWeek.THURSDAY.getValue()) {
            System.out.println("You can only set shifts starting from Thursday.");
            EmployeeManager(); 
        }
        //check if date is in the past
        if(date.isBefore(now)) {
            System.out.println("You can't set shifts to the past, choose again");
            EmployeeManager(); 
        }

        //allow shifts only for next week
        //now.with(DayOfWeek.SUNDAY) gets the most recent Sunday before/equal to today.
        long weeksBetween = ChronoUnit.WEEKS.between(now.with(DayOfWeek.SUNDAY), date.with(DayOfWeek.SUNDAY));
        if (weeksBetween != 1) {
            System.out.println("You can only set shift for NEXT week.");
            EmployeeManager(); 
        }

        //don't allow shifts on SHABBAT
        if(date.getDayOfWeek().getValue() == 7) {
            System.out.println("Shabbat is rest day, please choose again");
            EmployeeManager();
        }
        //System.out.println(pref); not sure we need it

        ShiftType shiftType = selectFromList("Select Shift Type:", ShiftType.values());
        int startTime = readInt("Please enter the start time of the shift , 24-hour format: e.g. 9:00 AM = 900: ");
        int endTime = readInt("Please enter the end time of the shift , 24-hour format: e.g. 5:00 PM = 1700: ");
        employeeFacade.createShift(date, shiftType, startTime, endTime, id, -1);
        System.out.println("Please choose Shift Manager ");
        Shift shift = new Shift(date, shiftType, startTime, endTime, -1);
        System.out.println(employeeFacade.getAvailableEmployees(shift, Role.SHIFT_MANAGER));
        int shiftManagerId = readInt("Please enter the ID of the Shift Manager: ");
        employeeFacade.setShiftManager(id, shift, shiftManagerId);
        for (Role role : Role.values()) {
            if (role != Role.SHIFT_MANAGER) {
                int numOfEmployees = readInt("Please enter the number of employees for " + role + ": ");
                String response = employeeFacade.setRequiredRoles(id, shift, role, numOfEmployees);
                if(response != null) {
                    System.out.println(response);
                    EmployeeManager(); //לבדוק איך להחזיר לשורה 123.5
                    }
                for(int i = 0; i < numOfEmployees; i++) {
                    System.out.println("Please choose " + role + " for the shift from the following employees: ");
                    System.out.println(employeeFacade.getAvailableEmployees(shift, role));
                    int employeeId = readInt("Please enter the ID of the employee: ");
                    String res = employeeFacade.addEmployeeToShift(employeeId, shift, role, date, id);
                    if(res != null) {
                        System.out.println(res);
                        EmployeeManager();
                    }
                }
            }
        }
        EmployeeManager();
    }

//     private void addEmployeeToShift(LocalDate date, String shiftType) {
//         System.err.println(employeeFacade.getAvailableEmployees(date, shiftType, id));
//         //מוצג למסך רשימה של הקופאיות שזמינות למשמרת
//         //מנהל כ"א בוחר קופאית מתוך הרשימה
// // לעבור על כל התפקידים ולבחור עבור קופאית כמה אתה רוצה שיהיו
// // ואז המספר הה נשלח לפונקציה set required rol
// // נגיד קופאית, 3

//         //לקרוא לפונקציה שמשבצת עובדים לפי תפקידים
//     }



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

        //choose role
        Role selectedRole = selectFromList("Choose a role:", Role.values());
        String response = employeeFacade.hireEmployee(employeeID, id, name ,bankAccount, salary, startDate,
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
                "Social Benefits", "Password"};
        String option = selectFromList("Select Employee Data to change:", labels);
        switch (option) {
            case "Salary" -> updateSalary();
            case "Bank Account" -> updateBankAccount();
            case "Vacation Days" -> updateVacationDays();
            case "Sick Days" -> updateSickDays();
            case "Education Fund" -> updateEducationFund();
            case "Social Benefits" -> updateSocialBenefits();
            case "Password" -> updatePassword();
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

    private void updatePassword() {
        int employeeId = readInt("Please enter employee ID: ");
        String password = readString("Enter the new Password: ");
        String response = employeeFacade.updatePassword(employeeId, id, password);
        if(response != null)
            System.out.println(response);
        EmployeeManager();
    }

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
