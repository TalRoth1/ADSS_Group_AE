package PresentationLayer;

import DomainLayer.*;

import java.util.Scanner;

public class CLI {
    public static Scanner scanner = new Scanner(System.in);
    EmployeeFacade employeeFacade;
    private String username;
    private String password;

    public CLI() {

        employeeFacade = new EmployeeFacade();
        loginCLI();
    }

    private void loginCLI() {
        System.out.println("LOGIN:");
        System.out.println("Please enter your username:");
        username = String.valueOf(scanner.nextInt());
        scanner.nextLine();
        System.out.println("Please enter your password:");
        password = scanner.nextLine();
       /* if (username.equals("abcde") && password.equals("12345"))
            addEmployeeManager();
        else {*/
            Employee emp = employeeFacade.login(username, password);
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
                fire();
                break;
            case 3:
                hire();
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
                employeeFacade.logout(id);
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

}
