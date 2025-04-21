package PresentationLayer;

import ServiceLayer.BranchService;
import ServiceLayer.ReportService;
import ServiceLayer.Response;
import ServiceLayer.ServiceFactory;

import java.util.Scanner;

public class CLI
{

    private ServiceFactory sf;

    private Scanner scanner;

    public CLI(ServiceFactory sf)
    {
        this.sf = sf;
        this.scanner = new Scanner(System.in);
    }

    public void openInterface()
    {
        boolean isInterrupted = false;
        System.out.println("Welcome to inventory management system!");
        while(!isInterrupted)
        {
            printMainMenu();
            displayControlButtons();
            String choice = getTextFromUser();

            switch (choice) {
                case "0":
                    display("Exiting the system. Goodbye!");
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
                default:
                    display("Invalid choice. Please try again.");
            }
        }
    }

    private void printMainMenu()
    {
        System.out.println("1. Manage Branches.");
        System.out.println("2. Manage Products.");
        System.out.println("3. Manage Items.");
        System.out.println("4. Reports.");
        System.out.println("0. Exit.");
    }

    private void displayControlButtons()
    {
        System.out.println("Press number to choose option");
    }

    private void display(String s)
    {
        System.out.println(s);
    }

    private String getTextFromUser()
    {
        return scanner.nextLine();
    }

    private void openBranchesInterface()
    {
        BranchService BS = sf.getBranchService();

        boolean backToMainMenu = false;
        while (!backToMainMenu)
        {
            printBranchesMenu();
            displayControlButtons();
            String choice = getTextFromUser();
            switch (choice)
            {
                case "0":
                    clearScreen();
                    backToMainMenu = true;
                    break;
                case "1":
                    clearScreen();
                    System.out.print("Enter branch name: ");
                    String name = getTextFromUser();
                    System.out.print("Enter branch address: ");
                    String address = getTextFromUser();
                    Response response1 = BS.AddBranch(name, address);
                    if (response1.getErrorMessage() == null)
                    {
                        display("Branch added successfully. ID: " + response1.getResponseValue());
                    }
                    else
                    {
                        display("Error: " + response1.getErrorMessage());
                    }
                    break;

                case "2":
                    clearScreen();
                    System.out.print("Enter branch ID to remove: ");
                    int removeId = getValidIntegerFromUser();

                    Response response2 = BS.RemoveBranch(removeId);
                    if (response2.getErrorMessage() == null)
                    {
                        display(response2.getResponseValue());
                    }
                    else
                    {
                        display("Error: " + response2.getErrorMessage());
                    }
                    break;

                case "3":
                    clearScreen();
                    System.out.print("Enter branch ID to rename: ");
                    int renameId = getValidIntegerFromUser();
                    System.out.print("Enter new name: ");
                    String newName = getTextFromUser();

                    Response response3 = BS.ChangeBranchName(newName, renameId);
                    if (response3.getErrorMessage() == null)
                    {
                        display(response3.getResponseValue());
                    }
                    else
                    {
                        display("Error: " + response3.getErrorMessage());
                    }
                    break;

                case "4":
                    clearScreen();
                    System.out.print("Enter branch ID to change address: ");
                    int addressId = getValidIntegerFromUser();
                    System.out.print("Enter new address: ");
                    String newAddress = getTextFromUser();

                    Response response4 = BS.ChangeBranchAddress(newAddress, addressId);
                    if (response4.getErrorMessage() == null)
                    {
                        display(response4.getResponseValue());
                    }
                    else
                    {
                        display("Error: " + response4.getErrorMessage());
                    }
                    break;

                case "5":
                    clearScreen();
                    Response response5 = BS.GetAllBranches();
                    if (response5.getErrorMessage() == null)
                    {
                        display("Branch list:");
                        display(response5.getResponseValue());
                    }
                    else
                    {
                        display("Error: " + response5.getErrorMessage());
                    }
                    break;

                default:
                    display("Invalid choice. Please try again.");
            }
            waitForUser();
        }
    }


    private void printBranchesMenu()
    {
        System.out.println("1. Add new Branch.");
        System.out.println("2. Remove Branch.");
        System.out.println("3. Change Branch Name.");
        System.out.println("4. Change Branch Address.");
        System.out.println("5. Show All Branches.");
        System.out.println("0. Return to main menu.");
    }


// ... (rest of the code remains unchanged)

    private void openProductsInterface()
    {
        ProductService PS = sf.getProductService();

        boolean backToMainMenu = false;
        while (!backToMainMenu)
        {
            printProductsMenu();
            displayControlButtons();
            String choice = getTextFromUser();
            switch (choice)
            {
                case "0":
                    clearScreen();
                    backToMainMenu = true;
                    break;

                case "1":
                    clearScreen();
                    System.out.print("Enter product name: ");
                    String name = getTextFromUser();
                    System.out.print("Enter cost price: ");
                    double costPrice = getValidDoubleFromUser();
                    System.out.print("Enter selling price: ");
                    double sellingPrice = getValidDoubleFromUser();
                    System.out.print("Enter discount (%): ");
                    int discount = getValidIntegerFromUser();
                    System.out.print("Enter producer ID: ");
                    long producerID = getValidIntegerFromUser();
                    System.out.print("Enter categories (comma-separated): ");
                    String[] categories = getTextFromUser().split(",");

                    Response addResponse = PS.AddProduct(name, costPrice, sellingPrice, discount, producerID, categories);
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
                    System.out.print("Enter product ID to remove: ");
                    long removeID = getValidIntegerFromUser();

                    Response removeResponse = PS.RemoveProduct(removeID);
                    if (removeResponse.getErrorMessage() == null)
                    {
                        display(removeResponse.getResponseValue());
                    }
                    else
                    {
                        display("Error: " + removeResponse.getErrorMessage());
                    }
                    break;

                case "3":
                    clearScreen();
                    System.out.print("Enter product ID to update: ");
                    int productID = getValidIntegerFromUser();
                    System.out.print("Enter new product name: ");
                    String newName = getTextFromUser();
                    System.out.print("Enter new cost price: ");
                    double newCost = getValidDoubleFromUser();
                    System.out.print("Enter new selling price: ");
                    double newSelling = getValidDoubleFromUser();
                    System.out.print("Enter new discount (%): ");
                    int newDiscount = getValidIntegerFromUser();
                    System.out.print("Enter new producer ID: ");
                    int newProducer = getValidIntegerFromUser();
                    System.out.print("Enter new categories (comma-separated): ");
                    String[] newCategories = getTextFromUser().split(",");

                    Response updateResponse = PS.UpdateProduct(productID, newName, newCost, newSelling, newDiscount, newProducer, newCategories);
                    if (updateResponse.getErrorMessage() == null)
                    {
                        display(updateResponse.getResponseValue());
                    }
                    else
                    {
                        display("Error: " + updateResponse.getErrorMessage());
                    }
                    break;

                default:
                    display("Invalid choice. Please try again.");
            }
            waitForUser();
        }
    }



    private void printProductsMenu()
    {
        System.out.println("1. Add new Product.");
        System.out.println("2. Remove Product.");
        System.out.println("3. Update Product.");
        System.out.println("0. Return to main menu.");
    }


    private void openItemsInterface()
    {

    }

    private void openReportsInterface()
    {

    }

    public void clearScreen()
    {
        for (int i = 0; i < 200; i++)
        {
            System.out.println();
        }
    }

    private int getValidIntegerFromUser()
    {
        while (true)
        {
            String input = getTextFromUser();
            try
            {
                return Integer.parseInt(input);
            }
            catch (NumberFormatException e)
            {
                display("Invalid input. Please enter a valid number.");
            }
        }
    }

    private double getValidDoubleFromUser()
    {
        while (true)
        {
            String input = getTextFromUser();
            try
            {
                return Double.parseDouble(input);
            }
            catch (NumberFormatException e)
            {
                display("Invalid input. Please enter a valid decimal number.");
            }
        }
    }

    private void waitForUser()
    {
        display("Press any key to continue.");
        getTextFromUser();
        clearScreen();
    }
}
