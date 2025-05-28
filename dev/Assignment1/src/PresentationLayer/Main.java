package PresentationLayer;

import java.util.Scanner;
import PresentationLayer.EmployeeCLI;
import PresentationLayer.ShipmentCLI;
import PresentationLayer.ShipMain;
import DomainLayer.*;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        EmployeeFacade employeeFacade = new EmployeeFacade();
        EmployeeCLI employeeCLI = new EmployeeCLI(employeeFacade);
        ShipMain shipcli;

        System.out.println("Welcome! Please select the menu");
        System.out.println("1. Shipment Menu (shipment manager)");
        System.out.println("2. Employee Menu (employee manager or employee)");
        System.out.print("Enter your choice: ");

        int choice = scanner.nextInt();
        switch (choice) {
            case 1:
                shipcli = new ShipMain();
                break;
            case 2:
                employeeCLI.loginCLI();
                break;
            default:
                System.out.println("Invalid choice. Please restart the application.");
        }
    }
}