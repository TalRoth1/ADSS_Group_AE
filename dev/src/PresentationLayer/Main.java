package PresentationLayer;

import DomainLayer.*;
import java.time.LocalDate;
import DomainLayer.*;
import java.time.LocalDate;

public class Main {
    public static void main(String[] args) {
        LocalDate today = LocalDate.now();
        EmployeeFacade employeeFacade = new EmployeeFacade();
        CLI cli = new CLI(employeeFacade);
    }
}