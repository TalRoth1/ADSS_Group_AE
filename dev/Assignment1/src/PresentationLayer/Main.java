package PresentationLayer;

import java.util.Scanner;

import DomainLayer.EmployeeFacade;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        EmployeeFacade employeeFacade = new EmployeeFacade();
        EmployeeCLI employeeCLI = new EmployeeCLI(employeeFacade);
        ShipmentCLI shipmentCLI = new ShipmentCLI(employeeFacade);

        System.out.println("Please choose what data you want to use:");
        boolean flag = true;
        while (flag) {
            System.out.println("1. Continue from previous session");
            System.out.println("2. Use predefined data");
            System.out.println("3. No data");
            System.out.print("Enter your choice: ");
            String choice = scanner.nextLine();
            switch (choice) {
                case "1":
                    // Continue from previous session - should just continue with what we already had
                    flag = false;
                    break;
                case "2":
                    //Load predefined data - use a method on both CLIs to load predefined data
                    flag = false;
                    employeeCLI.MakePredefinedData();
                    shipmentCLI.MakePredefinedData();
                    break;
                case "3":
                    //No data - use a method on both CLIs to delete all data
                    shipmentCLI.ClearDataBase();
                    employeeCLI.ClearDataBase();
                    flag = false;
                    break;
                default:
                    System.out.println("Invalid choice, please try again.");
            }
        }

        /*System.out.println("Hello! load data? answer y / n");
        String loadData = scanner.nextLine();
        if (loadData.equalsIgnoreCase("y")) {*/

        //loading data from the choice made above
            shipmentCLI.loadData();
            employeeCLI.loadData();


        /*} else if (!loadData.equalsIgnoreCase("n")) {
            System.out.println("Invalid input, please restart the application.");
            return;
        }*/

        boolean running = true;
        while (running) {
            System.out.println("Welcome! Please select the menu");
            System.out.println("1. Shipment Menu (shipment manager)");
            System.out.println("2. Employee Menu (employee manager or employee)");
            System.out.println("3. Exit");
            System.out.print("Enter your choice: ");

            int choice = scanner.nextInt();
            switch (choice) {
                case 1:
                    shipmentCLI.loginCLI();
                    break;
                case 2:
                    employeeCLI.loginCLI();
                    break;
                case 3:
                    running = false;
                    System.out.println("Exiting application.");
                    break;
                default:
                    System.out.println("Invalid choice. Please restart the application.");
            }
        }
    }
}