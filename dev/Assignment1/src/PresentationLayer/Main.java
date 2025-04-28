package PresentationLayer;

import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        UIController uiController = new UIController();
        Scanner scanner = new Scanner(System.in);
        LocalDate today = LocalDate.now();
        EmployeeFacade employeeFacade = new EmployeeFacade();
        CLI cli = new CLI(employeeFacade);

        System.out.println("Welcome to the Delivery Management System!");
        boolean exit = false;
        while (!exit) {
            System.out.println("Please select an option:");
            System.out.println("1. Create a new shipment");
            System.out.println("2. Edit an existing shipment");
            System.out.println("3. Add a new truck");
            System.out.println("4. Add a new driver");
            System.out.println("5. Add a new location");
            System.out.println("6. Add a new item");
            System.out.println("7. Change shipment status");
            System.out.println("8. View a shipment document");
            System.out.println("9. Exit");
            boolean flag = true;
            int choice = 0;
            while(flag) {
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
                    uiController.CreateShipment();
                    break;
                case 2:
                    uiController.EditShipement();
                    break;
                case 3:
                    uiController.AddTruck();
                    break;
                case 4:
                    uiController.AddDriver();
                    break;
                case 5:
                    uiController.AddLocation();
                    break;
                case 6:
                    uiController.AddItem();
                    break;
                case 7:
                    uiController.ChangeStatus();
                    break;
                case 8:
                    uiController.ShowDocuments();
                    break;
                case 9:
                    exit = true;
                    System.out.println("Exiting the program. Goodbye!");
                    break;
            }
        } 
    }  
}
