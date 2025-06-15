package DomainLayer;

import java.time.LocalDate;

import org.junit.After;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import org.junit.Before;
import org.junit.Test;

public class ShiftEmployeeTest {

    private ShiftEmployee employee;
    private Shift shift;
    private LocalDate testDate;
    private EmployeeManager manager;
    private final int MANAGER_ID = 100;
    private ShiftEmployee testEmployee;
    private final int EMPLOYEE_ID = 101;
    private final int EMPLOYEE_ID2 = 102;
    private final LocalDate START_DATE = LocalDate.now();
    private LocationDL testBranch;

    @Before
    public void setUp() {
        testBranch = new LocationDL(1, "Test Branch", 1, "Test Street", "1234567890", "Test Contact", "Zone1");

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

        testEmployee = new ShiftEmployee(
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
                Role.CASHIER);
    }

    @After
    public void tearDown() {
        if (employee != null && employee.getAssignedShifts() != null) {
            employee.getAssignedShifts().clear();
        }
        if (employee != null && employee.getPrefShifts() != null) {
            employee.getPrefShifts().clear();
        }
        employee = null;
        shift = null;
        testDate = null;
    }

    @Test
    public void addRole_shouldSucceed() throws Exception {
        testEmployee.addRole(Role.STORE_KEEPER);
        assertTrue(testEmployee.getRoles().contains(Role.STORE_KEEPER));
    }

    @Test(expected = Exception.class)
    public void addRole_shouldThrowException_WhenRoleExists() throws Exception {
        testEmployee.addRole(Role.CASHIER);
    }

    @Test
    public void changeRole_shouldSucceed() throws Exception {
        testEmployee.changeRole(Role.CASHIER, Role.STORE_KEEPER);
        assertTrue(testEmployee.getRoles().contains(Role.STORE_KEEPER));
        assertFalse(testEmployee.getRoles().contains(Role.CASHIER));
    }

    @Test
    public void addPreferredShift_shouldSucceed() throws Exception {
        Shift shift = new Shift(1, LocalDate.now().plusDays(1), ShiftType.MORNING, 0, testBranch);
        testEmployee.addPreferredShift(shift);
        assertTrue(testEmployee.getPrefShifts().contains(shift));
    }

    @Test
    public void removePreferredShift_shouldSucceed() throws Exception {
        Shift shift = new Shift(1, LocalDate.now().plusDays(2), ShiftType.EVENING, 0, testBranch);
        testEmployee.addPreferredShift(shift);
        testEmployee.removePreferredShift(shift);
        assertFalse(testEmployee.getPrefShifts().contains(shift));
    }

    @Test
    public void addAssignedShift_shouldSucceed() throws Exception {
        Shift shift = new Shift(1, LocalDate.now().plusDays(3), ShiftType.EVENING, 0, testBranch);
        testEmployee.addAssignedShift(shift, Role.CASHIER);
        assertTrue(testEmployee.getAssignedShifts().containsKey(shift));
        assertEquals(Role.CASHIER, testEmployee.getAssignedShifts().get(shift));
    }

    @Test
    public void removeAssignedShift_shouldSucceed() throws Exception {
        Shift shift = new Shift(1, LocalDate.now().plusDays(4), ShiftType.MORNING, 0, testBranch);
        testEmployee.addAssignedShift(shift, Role.CASHIER);
        testEmployee.removeAssignedShift(shift);
        assertFalse(testEmployee.getAssignedShifts().containsKey(shift));
    }

    @Test
    public void isAvailable_shouldReturnTrue_WhenShiftInPreferred() throws Exception {
        Shift shift = new Shift(1, LocalDate.now().plusDays(5), ShiftType.MORNING, 0, testBranch);
        testEmployee.addPreferredShift(shift);
        assertTrue(testEmployee.isAvailable(shift));
    }

    @Test(expected = Exception.class)
    public void isAvailable_shouldThrowException_WhenEmployeeFinished() throws Exception {
        Shift shift = new Shift(1, LocalDate.now().plusDays(6), ShiftType.MORNING, 0, testBranch);
        testEmployee.setFinishWorking(true);
        testEmployee.isAvailable(shift);
    }
}
