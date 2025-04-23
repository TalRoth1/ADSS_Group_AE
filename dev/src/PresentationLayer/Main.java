package PresentationLayer;

import DomainLayer.*;
import java.time.LocalDate;

public class Main {
    public static void main(String[] args) {
        LocalDate today = LocalDate.now();
        EmployeeFacade employeeFacade = new EmployeeFacade();
        EmployeeRepository repo = new EmployeeRepository();
        SystemInitializer.initializeSystem(repo);
        //EmployeeManager manager = new EmployeeManager(100, "keren","1", "111222", 7000, LocalDate.of(2025,04,10), 20, 5, 100, 100, "password");
        // if (today.getDayOfWeek() == DayOfWeek.SUNDAY) {
        //     manager.archiveWeeklyForAllEmployees();
        // }

        employeeFacade.addFirstEmployeeManager(100, "keren", "1", "111222", 7000, today, 0, 0, 0, 0, "123");

        //employeeFacade.hireEmployee(101, 100, "1", "Liat", "1111", 0, today, 0, 0, 0, 0, "liat1", Role.SHIFT_MANAGER);
        // Hire employees (these are actual employees being added to the system)
        // manager.hireEmployee(101, "Liat", "1", "111222", 7000, LocalDate.of(2025,04,10), 20, 5, 100, 100, "password", Role.SHIFT_MANAGER);
        // manager.hireEmployee(102, "Erez", "2", "222333", 8000, LocalDate.of(2025,04,10), 20, 5, 100, 100, "password", Role.CASHIER);
        // manager.hireEmployee(103, "Elad", "3", "333444", 9000, LocalDate.of(2025,04,10), 20, 5, 100, 100, "password", Role.DRIVER);
        // manager.hireEmployee(104, "Eylon", "4", "444555", 10000, LocalDate.of(2025,04,10), 20, 5, 100, 100, "password", Role.DRIVER);
        // manager.hireEmployee(105, "Kiril", "5", "555666", 11000, LocalDate.of(2025,04,10), 20, 5, 100, 100, "password", Role.STORE_KEEPER);
        // manager.hireEmployee(106, "Ofir", "6", "666777", 12000, LocalDate.of(2025,04,10), 20, 5, 100, 100, "password", Role.STORE_KEEPER);
        // manager.hireEmployee(107, "Tal", "7", "777888", 13000, LocalDate.of(2025,04,10), 20, 5, 100, 100, "password", Role.SHIFT_MANAGER);
        // manager.hireEmployee(108, "Ofri", "8", "888999", 14000, LocalDate.of(2025,04,10), 20, 5, 100, 100, "password", Role.CASHIER);

        CLI cli = new CLI(employeeFacade);

       // manager.addRoleToEmployee(103, Role.CASHIER);
       // ShiftEmployee em103 = manager.getEmployee(103);


        // Create some shifts and assign them to employees (these are past shifts)
        Shift sundayMorning = new Shift(LocalDate.now().minusDays(10), ShiftType.MORNING, -1); 
        Shift mondayMorning = new Shift(LocalDate.now().minusDays(20), ShiftType.MORNING, -1);  
        Shift tuesdayMorning = new Shift(LocalDate.now().minusDays(30), ShiftType.MORNING, -1); 
        Shift wednesdayMorning = new Shift(LocalDate.now().minusDays(40), ShiftType.MORNING, -1); 
        Shift thursdayMorning = new Shift(LocalDate.now().minusDays(50), ShiftType.MORNING, -1); 
        Shift fridayMorning = new Shift(LocalDate.now().minusDays(60), ShiftType.MORNING, -1); 
        Shift saturdayMorning = new Shift(LocalDate.now().minusDays(70), ShiftType.MORNING, -1); 

        Shift sundayEvening = new Shift(LocalDate.now().minusDays(80), ShiftType.EVENING, -1); 
        Shift mondayEvening = new Shift(LocalDate.now().minusDays(90), ShiftType.EVENING, -1); 
        Shift tuesdayEvening = new Shift(LocalDate.now().minusDays(100), ShiftType.EVENING, -1); 
        Shift wednesdayEvening = new Shift(LocalDate.now().minusDays(110), ShiftType.EVENING, -1); 
        Shift thursdayEvening = new Shift(LocalDate.now().minusDays(120), ShiftType.EVENING, -1); 
        Shift fridayEvening = new Shift(LocalDate.now().minusDays(130), ShiftType.EVENING, -1); 
        Shift saturdayEvening = new Shift(LocalDate.now().minusDays(140), ShiftType.EVENING, -1); 


        // Assign shifts to employees
       // manager.addEmployeeToShift(101, sundayMorning, Role.CASHIER);
      //  manager.addEmployeeToShift(102, mondayMorning, Role.SHIFT_MANAGER);

    }
}