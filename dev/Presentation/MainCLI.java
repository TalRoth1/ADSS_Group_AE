package Presentation;

import java.util.Scanner;

import Domain.OrderFacade;
import Domain.SupplierFacade;
import Service.ServiceFactory;
import Utils.Globals;

public class MainCLI 
{
    private CLI suppCli;
    private InvCLI invCli;
    private boolean isInterrupted = false;
    private Scanner scanner;

    public MainCLI(OrderFacade of, SupplierFacade sf, ServiceFactory invSf)
    {
        this.scanner = new Scanner(System.in);
        suppCli = new CLI(sf, of);
        invCli = new InvCLI(invSf);
    }

    public void run()
    {
        isInterrupted = false;
        while (!isInterrupted) {
            printFalseChoice();
            String useFakeData = getTextFromUser();
            while(!useFakeData.equalsIgnoreCase("yes") && !useFakeData.equalsIgnoreCase("no")) 
            {
                display("Invalid input. Please enter 'yes' or 'no'.");
                useFakeData = getTextFromUser();
            }
            switch (useFakeData.toLowerCase()) 
            {
                case "yes":
                    display("Using false data.");
                    Globals.useFakeData = true;
                    break;
                case "no":
                    display("Using real data.");
                    Globals.useFakeData = false; 
                    break;
            }
            printMainMenu();
            displayControlButtons();
            String choice = getTextFromUser();
            switch (choice) 
            {
                case "0":
                    display("Exiting the system. Goodbye!");
                    isInterrupted = true;
                    return;
                case "1":
                    clearScreen();
                    suppCli.run();
                    break;
                case "2":
                    clearScreen();
                    invCli.openInterface();
                    break;
                default:
                    display("Invalid choice. Please try again.");
            }
        }
    }
    private void printFalseChoice()
    {
        display("Would you like to use false data? (yes/no)");
    }
    private void printMainMenu() 
    {
        display("1. Enter as supply manager.");
        display("2. Enter as inventory manager.");
        display("0. Exit.");
    }

    private void display(String s) 
    {
        System.out.println(s);
    }

    public void clearScreen() 
    {
        for (int i = 0; i < 200; i++) 
        {
            System.out.println();
        }
    }

    private void displayControlButtons() 
    {
        display("Press number to choose option");
    }

    private String getTextFromUser() 
    {
        return scanner.nextLine();
    }

}
