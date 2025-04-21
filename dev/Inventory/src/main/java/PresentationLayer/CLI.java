package PresentationLayer;

import ServiceLayer.BranchService;
import ServiceLayer.ReportService;
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
        BranchService branchService = sf.getBranchService();

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
                    var response1 = branchService.AddBranch(name, address);
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

                    var response2 = branchService.RemoveBranch(removeId);
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

                    var response3 = branchService.ChangeBranchName(newName, renameId);
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

                    var response4 = branchService.ChangeBranchAddress(newAddress, addressId);
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
                    var response5 = branchService.GetAllBranches();
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


    private void openProductsInterface()
    {

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

    private void waitForUser()
    {
        display("Press any key to continue.");
        getTextFromUser();
        clearScreen();
    }
}
