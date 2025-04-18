package PresentationLayer;

public class Main {
    public static void main(String[] args) {
        UIController uiController = new UIController();

        System.out.println("Welcome to the Delivery Management System!");
        boolean exit = false;
        while (!exit) {
            System.out.println("Please select an option:");
            System.out.println("1. Create a new shipment");
            System.out.println("2. Edit an existing shipment");
            System.out.println("3. Add a new truck");
            System.out.println("4. Add a new driver");
            System.out.println("5. Add a new location");
            System.out.println("6. Exit");
            boolean flag = true;
            int choice = 0;
            while(flag) {
                System.out.println("Please enter your choice (1-6): ");
                String input = System.console().readLine();
                if (input.matches("[1-6]")) {
                    choice = Integer.parseInt(input);
                    flag = false;
                } else {
                    System.out.println("Invalid input. Please enter a number between 1 and 5.");
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
                    System.out.println("Exiting the program. Goodbye!");
                    exit = true;
                    break;
            }
        } 
    }  
}
