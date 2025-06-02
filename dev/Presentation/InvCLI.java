package Presentation;

import Service.*;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

import java.util.Date;
import java.util.Scanner;

public class InvCLI {

    private ServiceFactory sf;

    private Scanner scanner;
    private boolean demoMode = false;

    public InvCLI(ServiceFactory sf) {
        this.sf = sf;
        this.scanner = new Scanner(System.in);
    }

    public void openInterface() {
        boolean isInterrupted = false;
        sf.getBranchService().deactivateDemo();
        sf.getProductService().deactivateDemo();
        demoMode = false;
        display("Welcome to inventory management system!");
        while (!isInterrupted) {
            printMainMenu();
            displayControlButtons();
            String choice = getTextFromUser();

            switch (choice) {
                case "0":
                    display("Return to main menu. Goodbye!");
                    isInterrupted = true;
                    return;
                case "1":
                    clearScreen();
                    openBranchesInterface();
                    break;
                case "2":
                    clearScreen();
                    openProductsInterface();
                    break;
                case "3":
                    clearScreen();
                    openItemsInterface();
                    break;
                case "4":
                    clearScreen();
                    openReportsInterface();
                    break;
                case "5":
                    clearScreen();
                    openDemoInterface();
                    break;
                default:
                    display("Invalid choice. Please try again.");
            }
        }
    }

    private void printMainMenu() {
        if(demoMode) display ("You are currently in demonstration mode, all changes here won't be saved in the database");
        display("1. Manage Branches.");
        display("2. Manage Products.");
        display("3. Manage Items.");
        display("4. Reports.");
        display("5. Enter demonstration mode.");
        display("0. Exit.");
    }

    private void displayControlButtons() {
        display("Press number to choose option");
    }

    private void display(String s) {
        System.out.println(s);
    }

    private String getTextFromUser() {
        return scanner.nextLine();
    }

    private void openDemoInterface()
    {
        display("Are you sure you want to enter demonstration mode?");
        display("All changes in demonstration mode will not be saved.");
        display("For enter demonstration mode press 5, else press 0");
        String choice = getTextFromUser();
        switch (choice) 
        {
            case "5":
                demoMode = true;
                sf.getBranchService().enterDemo();
                sf.getProductService().enterDemo();
                sf.activateDemo();
                break;
            case "0":
                break;
        }
        waitForUser();
    }
    private void openBranchesInterface() {
        BranchService BS = sf.getBranchService();

        boolean backToMainMenu = false;
        while (!backToMainMenu) {
            printBranchesMenu();
            displayControlButtons();
            String choice = getTextFromUser();
            switch (choice) {
                case "0":
                    clearScreen();
                    backToMainMenu = true;
                    break;
                case "1":
                    clearScreen();
                    display("Enter branch name: ");
                    String name = getTextFromUser();

                    display("Enter branch address: ");
                    String address = getTextFromUser();

                    Response response1 = BS.AddBranch(name, address);
                    if (response1.getErrorMessage() == null) {
                        display("Branch added successfully. ID: " + response1.getResponseValue());
                    } else {
                        display("Error: " + response1.getErrorMessage());
                    }
                    break;

                case "2":
                    clearScreen();
                    display("Enter branch ID to remove: ");
                    int removeId = getValidIntegerFromUser();

                    Response response2 = BS.RemoveBranch(removeId);
                    if (response2.getErrorMessage() == null) {
                        display(response2.getResponseValue());
                    } else {
                        display("Error: " + response2.getErrorMessage());
                    }
                    break;

                case "3":
                    clearScreen();
                    display("Enter branch ID to rename: ");
                    int renameId = getValidIntegerFromUser();
                    display("Enter new name: ");
                    String newName = getTextFromUser();

                    Response response3 = BS.ChangeBranchName(newName, renameId);
                    if (response3.getErrorMessage() == null) {
                        display(response3.getResponseValue());
                    } else {
                        display("Error: " + response3.getErrorMessage());
                    }
                    break;

                case "4":
                    clearScreen();
                    display("Enter branch ID to change address: ");
                    int addressId = getValidIntegerFromUser();
                    display("Enter new address: ");
                    String newAddress = getTextFromUser();

                    Response response4 = BS.ChangeBranchAddress(newAddress, addressId);
                    if (response4.getErrorMessage() == null) {
                        display(response4.getResponseValue());
                    } else {
                        display("Error: " + response4.getErrorMessage());
                    }
                    break;

                case "5":
                    clearScreen();
                    Response response5 = BS.GetAllBranches();
                    if (response5.getErrorMessage() == null) {
                        display("Branch list:");
                        display(response5.getResponseValue());
                    } else {
                        display("Error: " + response5.getErrorMessage());
                    }
                    break;

                default:
                    display("Invalid choice. Please try again.");
            }
            waitForUser();
        }
    }

    private void printBranchesMenu() {
        display("1. Add new Branch.");
        display("2. Remove Branch.");
        display("3. Change Branch Name.");
        display("4. Change Branch Address.");
        display("5. Show All Branches.");
        display("0. Return to main menu.");
    }

    private void openProductsInterface() {
        ProductService PS = sf.getProductService();

        boolean backToMainMenu = false;
        while (!backToMainMenu) {
            printProductsMenu();
            displayControlButtons();
            String choice = getTextFromUser();
            switch (choice) {
                case "0":
                    clearScreen();
                    backToMainMenu = true;
                    break;

                case "1":
                    clearScreen();
                    display("Enter product name: ");
                    String name = getTextFromUser();

                    // display("Enter cost price: ");
                    // double costPrice = getValidDoubleFromUser();

                    display("Enter selling price: ");
                    double sellingPrice = getValidDoubleFromUser();

                    display("Enter discount (%): ");
                    int discount = getValidIntegerFromUser();

                    display("Enter producer ID: ");
                    int producerID = getValidIntegerFromUser();

                    display("Enter categories (comma-separated): ");
                    String[] categories = getTextFromUser().split(",");

                    Response addResponse = PS.AddProduct(name, sellingPrice, discount, producerID,
                            categories);
                    if (addResponse.getErrorMessage() == null) 
                    {
                        display("Product added successfully. ID: " + addResponse.getResponseValue());
                    } 
                    else 
                    {
                        display("Error: " + addResponse.getErrorMessage());
                    }
                    break;

                case "2":
                    clearScreen();
                    display("Enter product ID to remove: ");
                    int removeID = getValidIntegerFromUser();

                    Response removeResponse = PS.RemoveProduct(removeID);
                    if (removeResponse.getErrorMessage() == null) {
                        display(removeResponse.getResponseValue());
                    } else {
                        display("Error: " + removeResponse.getErrorMessage());
                    }
                    break;

                case "3":
                    clearScreen();
                    display("Enter product ID to update: ");
                    int productID = getValidIntegerFromUser();

                    display("Enter new product name: ");
                    String newName = getTextFromUser();

                    display("Enter new cost price: ");
                    double newCost = getValidDoubleFromUser();

                    display("Enter new selling price: ");
                    double newSelling = getValidDoubleFromUser();

                    display("Enter new discount (%): ");
                    int newDiscount = getValidIntegerFromUser();

                    display("Enter new producer ID: ");
                    int newProducer = getValidIntegerFromUser();

                    display("Enter new categories (comma-separated): ");
                    String[] newCategories = getTextFromUser().split(",");

                    Response updateResponse = PS.UpdateProduct(productID, newName, newCost, newSelling, newDiscount, newProducer, newCategories);
                    if (updateResponse.getErrorMessage() == null) {
                        display(updateResponse.getResponseValue());
                    }
                    else
                    {
                        display("Error: " + updateResponse.getErrorMessage());
                    }
                    break;
                case "4":
                    clearScreen();
                    Response allProductsResponse = PS.GetAllProducts();
                    if (allProductsResponse.getErrorMessage() == null) {
                        display("Product list:");
                        display(allProductsResponse.getResponseValue());
                    } else {
                        display("Error: " + allProductsResponse.getErrorMessage());
                    }
                    break;
                case "5":
                    clearScreen();
                    display("Enter product ID to update minimal quantity: ");
                    int productID2 = getValidIntegerFromUser();

                    display("Enter branch to change minimal quantity: ");
                    int branchid2 = getValidIntegerFromUser();

                    display("Enter new minimal quantity: ");
                    int minQuantity = getValidIntegerFromUser();

                    Response updateQuantityResponse = PS.setMinQuantity(productID2, branchid2, minQuantity);
                    if (updateQuantityResponse.getErrorMessage() == null)
                    {
                        display(updateQuantityResponse.getResponseValue());
                    }
                    else
                    {
                        display("Oops, something went wrong and cake is a lie. Error: " + updateQuantityResponse.getErrorMessage());
                    }
                    break;
                default:
                    display("Invalid choice. Please try again.");
            }
            waitForUser();
        }
    }

    private void printProductsMenu() {
        display("1. Add new Product.");
        display("2. Remove Product.");
        display("3. Update Product.");
        display("4. Show All Products.");
        display("5. Update minimal quantity for Product");
        display("0. Return to main menu.");
    }

    private void openItemsInterface() {
        ItemService IS = sf.getItemService();
        boolean backToMainMenu = false;

        while (!backToMainMenu) {
            printItemsMenu();
            displayControlButtons();
            String choice = getTextFromUser();

            switch (choice) {
                case "0":
                    clearScreen();
                    backToMainMenu = true;
                    break;

                case "1":
                    clearScreen();
                    display("Enter product ID: ");
                    int productID = getValidIntegerFromUser();

                    display("Enter item name: ");
                    String name = getTextFromUser();

                    display("Is the item defective? (true/false): ");
                    boolean isDef = Boolean.parseBoolean(getTextFromUser());

                    display("Enter expiration date (yyyy-MM-dd): ");
                    Date expirationDate;
                    while (true) {
                        try {
                            String dateInput = getTextFromUser();
                            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                            sdf.setLenient(false);
                            expirationDate = sdf.parse(dateInput);
                            break;
                        } catch (Exception e) {
                            display("Invalid format. Please use yyyy-MM-dd.");
                        }
                    }

                    display("Enter branch ID: ");
                    int branchID = getValidIntegerFromUser();

                    display("Enter location segments (comma-separated): ");
                    String[] location = getTextFromUser().split(",");

                    Response addResponse = IS.AddItem(productID, name, isDef, expirationDate, branchID, location);
                    if (addResponse.getErrorMessage() == null) {
                        display("Item added successfully. ID: " + addResponse.getResponseValue());
                    } else {
                        display("Error: " + addResponse.getErrorMessage());
                    }
                    break;

                case "2":
                    clearScreen();
                    display("Enter item ID to remove: ");
                    int removeID = getValidIntegerFromUser();

                    Response removeResponse = IS.RemoveItem(removeID);
                    if (removeResponse.getErrorMessage() == null) {
                        display(removeResponse.getResponseValue());
                    } else {
                        display("Error: " + removeResponse.getErrorMessage());
                    }
                    break;

                case "3":
                    clearScreen();
                    display("Enter item ID to purchase: ");
                    int purchaseID = getValidIntegerFromUser();

                    Response purchaseResponse = IS.PurchaseItem(purchaseID);
                    if (purchaseResponse.getErrorMessage() == null) {
                        display(purchaseResponse.getResponseValue());
                    } else {
                        display("Error: " + purchaseResponse.getErrorMessage());
                    }
                    break;

                case "4":
                    clearScreen();
                    display("Enter item ID to update: ");
                    int itemID = getValidIntegerFromUser();

                    display("Enter new name: ");
                    String newName = getTextFromUser();

                    display("Is the item defective? (true/false): ");
                    boolean newIsDef = Boolean.parseBoolean(getTextFromUser());

                    display("Enter new branch ID: ");
                    int newBranchID = getValidIntegerFromUser();

                    display("Enter new location segments (comma-separated): ");
                    String[] newLocation = getTextFromUser().split(",");

                    Response updateResponse = IS.UpdateItem(itemID, newName, newIsDef, newBranchID, newLocation);
                    if (updateResponse.getErrorMessage() == null) {
                        display(updateResponse.getResponseValue());
                    } else {
                        display("Error: " + updateResponse.getErrorMessage());
                    }
                    break;
                case "5":
                    clearScreen();
                    Response allItemsResponse = IS.GetAllItems();
                    if (allItemsResponse.getErrorMessage() == null) {
                        display("Item list:");
                        display(allItemsResponse.getResponseValue());
                    } else {
                        display("Error: " + allItemsResponse.getErrorMessage());
                    }
                    break;
                default:
                    display("Invalid choice. Please try again.");
            }

            waitForUser();
        }
    }

    private void printItemsMenu() {
        display("1. Add new Item.");
        display("2. Remove Item.");
        display("3. Purchase Item.");
        display("4. Update Item.");
        display("5. Show All Items.");
        display("0. Return to main menu.");
    }

    private void openReportsInterface() {
        ReportService RS = sf.getReportService();
        boolean backToMainMenu = false;

        while (!backToMainMenu) {
            printReportsMenu();
            displayControlButtons();
            String choice = getTextFromUser();

            switch (choice) {
                case "0":
                    clearScreen();
                    backToMainMenu = true;
                    break;

                case "1":
                    clearScreen();
                    Response deficiency = RS.DeficiencyReport();
                    if (deficiency.getErrorMessage() == null) {
                        display("Deficiency Report:\n" + deficiency.getResponseValue());
                    } else {
                        display("Error: " + deficiency.getErrorMessage());
                    }
                    break;

                case "2":
                    clearScreen();
                    Response sales = RS.SalesReport();
                    if (sales.getErrorMessage() == null) {
                        display("Sales Report:\n" + sales.getResponseValue());
                    } else {
                        display("Error: " + sales.getErrorMessage());
                    }
                    break;

                case "3":
                    clearScreen();
                    Response defected = RS.DefectedReport();
                    if (defected.getErrorMessage() == null) {
                        display("Defected Items Report:\n" + defected.getResponseValue());
                    } else {
                        display("Error: " + defected.getErrorMessage());
                    }
                    break;

                case "4":
                    clearScreen();
                    Response expired = RS.ExpiredReport();
                    if (expired.getErrorMessage() == null) {
                        display("Expired Items Report:\n" + expired.getResponseValue());
                    } else {
                        display("Error: " + expired.getErrorMessage());
                    }
                    break;

                default:
                    display("Invalid choice. Please try again.");
            }

            waitForUser();
        }
    }

    private void printReportsMenu() {
        display("1. View Deficiency Report.");
        display("2. View Sales Report.");
        display("3. View Defected Items Report.");
        display("4. View Expired Items Report.");
        display("0. Return to main menu.");
    }

    public void clearScreen() {
        for (int i = 0; i < 200; i++) {
            System.out.println();
        }
    }

    private int getValidIntegerFromUser() {
        while (true) {
            String input = getTextFromUser();
            try {
                return Integer.parseInt(input);
            } catch (NumberFormatException e) {
                display("Invalid input. Please enter a valid number.");
            }
        }
    }

    private double getValidDoubleFromUser() {
        while (true) {
            String input = getTextFromUser();
            try {
                return Double.parseDouble(input);
            } catch (NumberFormatException e) {
                display("Invalid input. Please enter a valid decimal number.");
            }
        }
    }

    private void waitForUser() {
        display("Press any key to continue.");
        getTextFromUser();
        clearScreen();
    }
}