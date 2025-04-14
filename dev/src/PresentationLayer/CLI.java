package PresentationLayer;

import DomainLayer.*;

import java.util.Scanner;

public class CLI {
    public static Scanner scanner = new Scanner(System.in);
    EmployeeFacade employeeFacade;
    private int id;
    private String password;

    public CLI() {

        employeeFacade = new EmployeeFacade();
        loginCLI();
    }

    private void loginCLI() {
        System.out.println("LOGIN:");
        System.out.println("Please enter your ID:");
        id = scanner.nextInt();
        scanner.nextLine();
        System.out.println("Please enter your password:");
        password = scanner.nextLine();
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
                changeRole();
                break;
            case 5:
                addRole();
                break;
            case 6:
                deleteRole();
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

   /* private void ShiftEmployee() {
        //TODO: implement
    }*/
    /*private void addEmployeeManager() {
        System.out.println("Enter the details of the new Employee Manager");
        System.out.println("ID:");
        int id = scanner.nextInt();
        System.out.println("username:");
        username = String.valueOf(scanner.nextInt());
        System.out.println("Please enter your password:");
        password = scanner.nextLine();
        //fill in the rest of the fields of Employee
    }*/

    /*private void setShifts() {

    }*/

    private void fireEmployee() {
        System.out.println("Please Enter Employee's ID to fire");
        int employeeId = scanner.nextInt();
        scanner.nextLine();
        String response = employeeFacade.fireEmployee(employeeId, id);
        if(response != null)
            System.out.println(response);
        EmployeeManager();
    }

    private void hireEmployee() {
        System.out.println("Please enter the new Employee details");
        System.out.println("ID:");
        int employeeID = scanner.nextInt();
        scanner.nextLine();
        System.out.println("Name:");
        String name = scanner.nextLine();
        System.out.println("Bank Account:");
        String bankAccount = scanner.nextLine();
        System.out.println("Salary:");
        int salary = scanner.nextInt();
        scanner.nextLine();
        System.out.println("Password:");
        String employeePassword = scanner.nextLine();

        //choose role
        Role[] roles = Role.values(); // Role.values() return the enum values
        System.out.println("Choose a role:");
        for (int i = 0; i < roles.length; i++)
            System.out.println((i + 1) + ". " + roles[i]);
        int choice = -1;
        while(choice < 1 || choice > roles.length) {
            System.out.println("Enter your choice: ");
            if (scanner.hasNextInt()) {
                choice = scanner.nextInt();
                scanner.nextLine();
            } else {
                scanner.nextLine();
                System.out.println("Invalid input, Please enter a number.");
            }
        }
        String role = roles[choice - 1].toString();
        String response = employeeFacade.hireEmployee(employeeID, id, name ,bankAccount, salary, employeePassword, role);
        if(response != null) {
            System.out.println(response);
        }
        EmployeeManager();
    }
    private void logout(int id) {
        employeeFacade.logout(id);
    }
}
