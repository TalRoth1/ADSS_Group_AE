import DomainLayer.*;
import PresentationLayer.CLI;
import java.time.DayOfWeek;
import java.time.LocalDate;

public class Main {
    public static void main(String[] args) {
        CLI cli = new CLI();
        LocalDate today = LocalDate.now();
        EmployeeManager manager = new EmployeeManager(1, "admin", "123456789", 50000, LocalDate.of(2025, 10, 4), 20, 10, 1000.0, 200.0, "123");
        if (today.getDayOfWeek() == DayOfWeek.SUNDAY) {
            manager.archiveWeeklyForAllEmployees();
        }

        // Hire employees (these are actual employees being added to the system)
        manager.hireEmployee(101, "John Doe", "9876543210", 3000, LocalDate.now(), 10, 5, 500, 300, "password123", Role.CASHIER);
        manager.hireEmployee(102, "Jane Smith", "1234567890", 3200, LocalDate.now(), 15, 8, 600, 400, "password456", Role.SHIFT_MANAGER);
        manager.hireEmployee(103, "Alice Johnson", "2345678901", 3100, LocalDate.now(), 12, 6, 550, 350, "password789", Role.DRIVER);
        manager.hireEmployee(104, "Bob Brown", "3456789012", 3300, LocalDate.now(), 14, 7, 580, 370, "password012", Role.STORE_KEEPER);

       // manager.addRoleToEmployee(103, Role.CASHIER);
       // ShiftEmployee em103 = manager.getEmployee(103);


        // Create some shifts and assign them to employees (these are past shifts)
        Shift sundayMorning = new Shift(LocalDate.now().minusDays(10), ShiftType.MORNING, 8, 16, 101); 
        Shift mondayMorning = new Shift(LocalDate.now().minusDays(20), ShiftType.MORNING, 16, 24, 101);  
        Shift tuesdayMorning = new Shift(LocalDate.now().minusDays(30), ShiftType.MORNING, 8, 16, 101); 
        Shift wednesdayMorning = new Shift(LocalDate.now().minusDays(40), ShiftType.MORNING, 16, 24, 101); 
        Shift thursdayMorning = new Shift(LocalDate.now().minusDays(50), ShiftType.MORNING, 8, 16, 101); 
        Shift fridayMorning = new Shift(LocalDate.now().minusDays(60), ShiftType.MORNING, 16, 24, 101); 
        Shift saturdayMorning = new Shift(LocalDate.now().minusDays(70), ShiftType.MORNING, 8, 16, 101); 

        Shift sundayEvening = new Shift(LocalDate.now().minusDays(80), ShiftType.EVENING, 16, 24, 101); 
        Shift mondayEvening = new Shift(LocalDate.now().minusDays(90), ShiftType.EVENING, 8, 16, 101); 
        Shift tuesdayEvening = new Shift(LocalDate.now().minusDays(100), ShiftType.EVENING, 16, 24, 101); 
        Shift wednesdayEvening = new Shift(LocalDate.now().minusDays(110), ShiftType.EVENING, 8, 16, 101); 
        Shift thursdayEvening = new Shift(LocalDate.now().minusDays(120), ShiftType.EVENING, 16, 24, 101); 
        Shift fridayEvening = new Shift(LocalDate.now().minusDays(130), ShiftType.EVENING, 8, 16, 101); 
        Shift saturdayEvening = new Shift(LocalDate.now().minusDays(140), ShiftType.EVENING, 16, 24, 101); 

        // Assign shifts to employees
       // manager.addEmployeeToShift(101, sundayMorning, Role.CASHIER);
      //  manager.addEmployeeToShift(102, mondayMorning, Role.SHIFT_MANAGER);

    }
}