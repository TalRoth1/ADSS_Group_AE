package PresentationLayer;
import DomainLayer.*;
import java.time.LocalDate;


public class SystemInitializer {
    public static void initializeSystem(EmployeeRepository employeeRepo) {
        // creating new employees
        EmployeeManager admin = new EmployeeManager(100, "Keren","1", "111222", 7000, LocalDate.of(2025,04,10), 20, 5, 100, 100, "123");
        ShiftEmployee Liat = new ShiftEmployee(101, "Liat", "1", "111222", 7000, LocalDate.of(2025,04,10), 20, 5, 100, 100, "password", Role.SHIFT_MANAGER);
        ShiftEmployee Erez = new ShiftEmployee(102, "Erez", "2", "222333", 8000, LocalDate.of(2025,04,10), 20, 5, 100, 100, "password", Role.CASHIER);
        ShiftEmployee Elad = new ShiftEmployee(103, "Elad", "3", "333444", 9000, LocalDate.of(2025,04,10), 20, 5, 100, 100, "password", Role.DRIVER);
        ShiftEmployee Eylon = new ShiftEmployee(104, "Eylon", "4", "444555", 10000, LocalDate.of(2025,04,10), 20, 5, 100, 100, "password", Role.DRIVER);
        ShiftEmployee Kiril = new ShiftEmployee(105, "Kiril", "5", "555666", 11000, LocalDate.of(2025,04,10), 20, 5, 100, 100, "password", Role.STORE_KEEPER);
        ShiftEmployee Ofir = new ShiftEmployee(106, "Ofir", "6", "666777", 12000, LocalDate.of(2025,04,10), 20, 5, 100, 100, "password", Role.STORE_KEEPER);
        ShiftEmployee Tal = new ShiftEmployee(107, "Tal", "7", "777888", 13000, LocalDate.of(2025,04,10), 20, 5, 100, 100, "password", Role.SHIFT_MANAGER);
        ShiftEmployee Ofri = new ShiftEmployee(108, "Ofri", "8", "888999", 14000, LocalDate.of(2025,04,10), 20, 5, 100, 100, "password", Role.CASHIER);

        employeeRepo.addEmployee(admin); // saving the employees to the repository
        employeeRepo.addEmployee(Liat);
        employeeRepo.addEmployee(Erez);
        employeeRepo.addEmployee(Elad);
        employeeRepo.addEmployee(Eylon);
        employeeRepo.addEmployee(Kiril);
        employeeRepo.addEmployee(Ofir);
        employeeRepo.addEmployee(Tal);
        employeeRepo.addEmployee(Ofri);
    }
}