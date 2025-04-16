package PresentationLayer;

import DomainLayer.*;

import java.time.LocalDate;
import java.time.DayOfWeek;
import java.time.temporal.ChronoUnit;
import java.time.format.DateTimeFormatter;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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
        //TODO: maybe use the assistant method selectFromList
        System.out.println("Select Employee Manager Action:");
        System.out.println("1. Set Shifts");
        System.out.println("2. Fire Employee");
        System.out.println("3. Hire Employee");
        System.out.println("4. change Employee's role");
        System.out.println("5. add role to employee");
        System.out.println("6. delete Employee's role");
        System.out.println("7. change Employee's data");
        System.out.println("8. logout");

        int option = scanner.nextInt();
        scanner.nextLine();

        switch (option) {
            case 1:
                setShifts();
                break;
            case 2:
                fireEmployee();
                break;
            case 3:
                hireEmployee();
                break;
            case 4:
                changeRoleToEmployee();
                break;
            case 5:
                addRoleToEmployee();
                break;
            case 6:
                deleteRoleFromEmployee();
                break;
            case 7:
                changeEmployeeData();
                break;
            case 8:
                logout(id);
                loginCLI();
            default:
                System.out.println("This is not a valid Employee Manager action");
                EmployeeManager();
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
        String pref = employeeFacade.getPreferences(id);
        String stringDate = "";
        String pattern = "^(0[1-9]|[1-2][0-9]|3[01])-(0[1-9]|1[0-2])-(\\d{4})$";
        while (!isValidDate(stringDate, pattern)) {
            System.out.println("Please enter the date of the shift in format of dd-mm-yyyy");
            stringDate = scanner.nextLine();
        }

        LocalDate date = convertStringToDate(stringDate);
        DayOfWeek today = now.getDayOfWeek();

        //check if it's Thursday or later
        if (today.getValue() < DayOfWeek.THURSDAY.getValue()) {
            System.out.println("You can only set shifts starting from Thursday.");
            EmployeeManager(); //TODO: maybe call setShifts() instead, ask Liat
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
        System.out.println(pref);
        createShift(date);
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
        String employeePassword = readString("Password: ");

        //choose role
        Role selectedRole = selectFromList("Choose a role:", Role.values());
        String response = employeeFacade.hireEmployee(employeeID, id, name ,bankAccount, salary, employeePassword, selectedRole);
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
        System.out.println("Select Shift Employee Action:");
        System.out.println("1. Set preferences");
        System.out.println("2. Get preferences");
        System.out.println("3. logout");
        int option = scanner.nextInt();
        scanner.nextLine();

        switch (option) {
            case 1:
                setPreferences();
                break;
            case 2:
                getPrefEmployee();
            case 3:
                logout(id);
                loginCLI();
            default:
                System.out.println("This is not a valid Shift Employee action");
                shiftEmployee();
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

    private LocalDate convertStringToDate(String s) {//only works for dd-mm-yyyy
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
        return LocalDate.parse(s, formatter);
    }

    private static boolean isValidDate(String date, String pattern) {
        // Compile the regex pattern
        Pattern compiledPattern = Pattern.compile(pattern);
        // Create a matcher for the input date
        Matcher matcher = compiledPattern.matcher(date);
        // Check if the date matches the pattern
        return matcher.matches();
    }
}
