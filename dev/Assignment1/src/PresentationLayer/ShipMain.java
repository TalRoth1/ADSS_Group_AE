package PresentationLayer;

import DomainLayer.EmployeeFacade;
import java.time.LocalDate;
import java.util.Scanner;

public class ShipMain {

    public static void main(String[] args) {
        /*Scanner scanner = new Scanner(System.in);
        //LocalDate today = LocalDate.now();
        EmployeeFacade employeeFacade = new EmployeeFacade();
        EmployeeCLI cli = new EmployeeCLI(employeeFacade);
        ShipmentCLI shipCli = new ShipmentCLI(employeeFacade);


        System.out.println("Welcome to the Delivery Management System!");
        boolean exit = false;
        while (!exit) {
            System.out.println("Please select an option:");
            System.out.println("1. Create a new shipment");
            System.out.println("2. Edit an existing shipment");
            System.out.println("3. Add a new truck");
            //System.out.println("4. Add a new driver");
            System.out.println("5. Add a new location");
            System.out.println("6. Add a new item");
            System.out.println("7. Change shipment status");
            System.out.println("8. View a shipment document");
            System.out.println("9. Exit");
            boolean flag = true;
            int choice = 0;
            while (flag) {
                System.out.println("Please enter your choice (1-9): ");
                String input = scanner.nextLine();
                if (input.matches("[1-9]")) {
                    choice = Integer.parseInt(input);
                    flag = false;
                } else {
                    System.out.println("Invalid input. Please enter a number between 1 and 9.");
                }
            }

            switch (choice) {
                case 1:
                    shipCli.CreateShipment();
                    break;
                case 2:
                    shipCli.EditShipement();
                    break;
                case 3:
                    shipCli.AddTruck();
                    break;
                case 4:
                    //shipCli.AddDriver();
                    break;
                case 5:
                    shipCli.AddLocation();
                    break;
                case 6:
                    shipCli.AddItem();
                    break;
                case 7:
                    shipCli.ChangeStatus();
                    break;
                case 8:
                    shipCli.ShowDocuments();
                    break;
                case 9:
                    exit = true;
                    System.out.println("Exiting the program. Goodbye!");
                    break;
            }
        }*/
    }
}
