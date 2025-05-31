package PresentationLayer;

import java.util.Scanner;
import PresentationLayer.EmployeeCLI;
import PresentationLayer.ShipmentCLI;
import DomainLayer.*;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        EmployeeFacade employeeFacade = new EmployeeFacade();
        EmployeeCLI employeeCLI = new EmployeeCLI(employeeFacade);
        ShipmentCLI shipmentCLI = new ShipmentCLI(employeeFacade);

        System.out.println("Hello! load data? answer y / n");
        String loadData = scanner.nextLine();
        if (loadData.equalsIgnoreCase("y")) {
            shipmentCLI.loadData();
            employeeCLI.loadData();
        } else if (!loadData.equalsIgnoreCase("n")) {
            System.out.println("Invalid input, please restart the application.");
            return;
        }

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