package DomainLayer;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import DataLayer.DriverController;
import org.junit.After;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import org.junit.Before;
import org.junit.Test;

public class EmployeeManagerTest {

    private EmployeeManager manager;
    private final int MANAGER_ID = 100;
    private final LocalDate START_DATE = LocalDate.now();
    private ShiftEmployee testEmployee;
    private final int EMPLOYEE_ID = 101;
    private LocationDL testBranch;


    @Before
    public void setUp() {
        testBranch = new LocationDL(1, "Test Branch", 1, "Test Street", "1234567890", "Test Contact", "Zone1");
        List<LocationDL> branches = new ArrayList<>();
        branches.add(testBranch);
        manager = new EmployeeManager(
                MANAGER_ID,
                "Test Manager",
                "111222",
                7000,
                START_DATE,
                20,
                5,
                100,
                100,
                "password123");

        testEmployee = manager.hireEmployee(
                EMPLOYEE_ID,
                "Test Employee",
                testBranch,
                "333444",
                5000,
                START_DATE,
                15,
                10,
                80,
                90,
                "emp123",
                Role.CASHIER, branches );
    }

    @After
    public void tearDown() {
        manager = null;
        testEmployee = null;
    }

    @Test
    public void hireEmployee_Success() throws Exception {
        testBranch = new LocationDL(1, "Test Branch", 1, "Test Street", "1234567890", "Test Contact", "Zone1");
        List<LocationDL> branches = new ArrayList<>();
        branches.add(testBranch);
        ShiftEmployee newEmployee = manager.hireEmployee(
                102,
                "New Employee",
                testBranch,
                "555666",
                5000,
                START_DATE,
                15,
                10,
                80,
                90,
                "pass123",
                Role.CASHIER, branches);

        assertNotNull(newEmployee);
        assertTrue(manager.checkEmployee(102));
    }

    @Test
    public void fireEmployee_Success() throws Exception {
        manager.fireEmployee(EMPLOYEE_ID);
        assertTrue(testEmployee.isFinishWorking());
    }

    @Test(expected = Exception.class)
    public void fireEmployee_NonExistentEmployee() throws Exception {
        manager.fireEmployee(999);
    }

    @Test
    public void updateBankAccountEmployee_Success() throws Exception {
        manager.updateBankAccountEmployee(EMPLOYEE_ID, "999888");
        assertEquals("999888", testEmployee.getBankAccount());
    }

    @Test(expected = Exception.class)
    public void updateBankAccountEmployee_NonExistentEmployee() throws Exception {
        manager.updateBankAccountEmployee(999, "999888");
    }

    @Test
    public void addRoleToEmployee_Success() throws Exception {
        manager.addRoleToEmployee(EMPLOYEE_ID, Role.DRIVER);
        assertTrue(testEmployee.getRoles().contains(Role.DRIVER));
    }

    @Test(expected = Exception.class)
    public void addRoleToEmployee_DuplicateRole() throws Exception {
        manager.addRoleToEmployee(EMPLOYEE_ID, Role.CASHIER);
    }

    @Test
    public void createShift_Success() throws Exception {
        // First add shift manager role to test employee
        manager.addRoleToEmployee(EMPLOYEE_ID, Role.SHIFT_MANAGER);

        manager.createShift(1, testBranch, START_DATE.plusDays(1), ShiftType.MORNING, EMPLOYEE_ID);

        Shift shift = manager.getShift(testBranch, START_DATE.plusDays(1), ShiftType.MORNING);
        assertNotNull(shift);
        assertEquals(EMPLOYEE_ID, shift.getShiftManagerId());
    }

    @Test(expected = Exception.class)
    public void createShift_InvalidShiftManager() throws Exception {
        manager.createShift(1, testBranch, START_DATE.plusDays(1), ShiftType.MORNING, EMPLOYEE_ID);
    }

    @Test
    public void addEmployeeToShift_Success() throws Exception {
        // Create a shift first
        manager.addRoleToEmployee(EMPLOYEE_ID, Role.SHIFT_MANAGER);
        manager.createShift(1, testBranch, START_DATE.plusDays(1), ShiftType.MORNING, EMPLOYEE_ID);

        Shift shift = manager.getShift(testBranch, START_DATE.plusDays(1), ShiftType.MORNING);
        shift.setRequiredRoles(Role.CASHIER, 1);
        manager.addEmployeeToShift(testBranch, EMPLOYEE_ID, shift, Role.CASHIER);

        assertTrue(shift.getAssignedEmployeesID().containsKey(EMPLOYEE_ID));
    }

//    @Test
//    public void assignCheck_Success() throws Exception {
//        LocationDL origin = new LocationDL(2, "Haifa", 2, "Haifa Street", "1234567890", "Test Contact", "Zone2");
//        LocationDL destination1 = new LocationDL(3, "Karkur", 3, "Karkur Street", "1234567890", "Test Contact",
//                "Zone3");
//        List<LocationDL> destinations = List.of(destination1);
//
//
//        LocalDate date = LocalDate.now();
//        ShiftType shiftType = ShiftType.MORNING;
//        String licenceType = "B";
//
//
//        //manager.addBranch(origin);
//        //manager.addBranch(destination1);
//
//        // Create and add shifts for each location including storekeeper and driver
//        Shift originShift = new Shift(2, date, shiftType, 0, origin);
//        ShiftEmployee driver = new ShiftEmployee(200, "Yossi", origin, "acc1", 5000, date, 10, 5, 80, 90, "pass",
//                Role.DRIVER);
//        ShiftEmployee storekeeper = new ShiftEmployee(201, "Dan", origin, "acc2", 5000, date, 10, 5, 80, 90, "pass",
//                Role.STORE_KEEPER);
//        originShift.setRequiredRoles(Role.DRIVER, 5);
//        originShift.setRequiredRoles(Role.STORE_KEEPER, 5);
//        originShift.addEmployee(driver.getId(), Role.DRIVER);
//        originShift.addEmployee(storekeeper.getId(), Role.STORE_KEEPER);
//        manager.addDefaultShift(date, shiftType, driver.getId(), origin);
//
//        Shift destShift = new Shift(3, date, shiftType, 0, destination1);
//        destShift.setRequiredRoles(Role.STORE_KEEPER, 5);
//        destShift.setRequiredRoles(Role.DRIVER, 5);
//        ShiftEmployee destDriver = new ShiftEmployee(202, "Eli", destination1, "acc3", 5000, date, 10, 5, 80, 90,
//                "pass", Role.DRIVER);
//        ShiftEmployee destStorekeeper = new ShiftEmployee(203, "Moshe", destination1, "acc4", 5000, date, 10, 5, 80, 90,
//                "pass", Role.STORE_KEEPER);
//        destShift.addEmployee(destDriver.getId(), Role.DRIVER);
//        destShift.addEmployee(destStorekeeper.getId(), Role.STORE_KEEPER);
//        manager.addDefaultShift(date, shiftType, destDriver.getId(), destination1);
//
//        // Add employees to the manager
//        manager.addEmployee(driver);
//        manager.addEmployee(storekeeper);
//        manager.addEmployee(destDriver);
//        manager.addEmployee(destStorekeeper);
//
//        EmployeeFacade employeeFacade = new EmployeeFacade();
//
//        DriverDL assignedDriver = manager.assignCheck(date, shiftType, origin, destinations, licenceType, destinations, employeeFacade.getDriverController());
//
//        assertNotNull(assignedDriver);
//        assertEquals("Yossi", assignedDriver.getName());
//
//        // Check that the shifts have shipmentShift set to true
//        assertTrue(manager.getShift(origin, date, shiftType).isShipmentShift());
//        assertTrue(manager.getShift(destination1, date, shiftType).isShipmentShift());
//    }
}
