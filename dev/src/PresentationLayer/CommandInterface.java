package PresentationLayer;

import DomainLayer.*;

import java.util.Scanner;

public class CommandInterface {
    public static Scanner scanner = new Scanner(System.in);
    private int id;
    private String password;

    public CommandInterface() {
        loginCLI();
    }

    private void loginCLI() {
        System.out.println("LOGIN:");
        System.out.println("Please enter your ID:");
        id = scanner.nextInt();
        scanner.nextLine();
        System.out.println("Please enter your password:");
        password = scanner.nextLine();
    }

}
