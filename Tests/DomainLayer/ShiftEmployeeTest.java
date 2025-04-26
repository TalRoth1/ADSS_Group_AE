package DomainLayer;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;


class ShiftEmployeeTest {
    private ShiftEmployee employee;
    private Shift shift;
    private LocalDate testDate;
    private EmployeeManager manager;
    private final int MANAGER_ID = 100;
    private ShiftEmployee testEmployee;
    private final int EMPLOYEE_ID = 101;
    private final int EMPLOYEE_ID2 = 102;
    private final LocalDate START_DATE = LocalDate.now();

    @BeforeEach
    void setUp() {
        manager = new EmployeeManager(
                MANAGER_ID,
                "Test Manager",
                "Branch1",
                "111222",
                7000,
                START_DATE,
                20,
                5,
                100,
                100,
                "password123"
        );

        testEmployee = manager.hireEmployee(
                EMPLOYEE_ID2,
                "Test Employee2",
                "Branch1",
                "333555",
                5000,
                START_DATE,
                15,
                10,
                80,
                90,
                "emp123",
                Role.SHIFT_MANAGER
        );

        // Create a test employee that we can use in multiple tests
        testEmployee = manager.hireEmployee(
                EMPLOYEE_ID,
                "Test Employee",
                "Branch1",
                "333444",
                5000,
                START_DATE,
                15,
                10,
                80,
                90,
                "emp123",
                Role.CASHIER
        );
    }

    @AfterEach
    void tearDown() {
        // Clear any assigned shifts
        if (employee != null && employee.getAssignedShifts() != null) {
            employee.getAssignedShifts().clear();
        }

        // Clear any preferred shifts
        if (employee != null && employee.getPrefShifts() != null) {
            employee.getPrefShifts().clear();
        }

        // Set objects to null to help garbage collection
        employee = null;
        shift = null;
        testDate = null;
    }

    @Test
    void getAssignedEmployeesInfo() {

    }

    @Test
    void changeRole_shouldChangeSuccessfully() {
        Role oldRole = Role.CASHIER;
        Role newRole = Role.STORE_KEEPER;
        String result = testEmployee.changeRole(oldRole, newRole);

        assertNull(result); // No error message = success
        assertTrue(testEmployee.getRoles().contains(newRole));
        assertFalse(testEmployee.getRoles().contains(oldRole));
    }

    @Test
    void addPreferredShift_shouldAddSuccessfully() {
        Shift shift = new Shift(LocalDate.now().plusDays(1), ShiftType.MORNING, EMPLOYEE_ID2);
        String result = testEmployee.addPreferredShift(shift);
        assertNull(result);
        assertTrue(testEmployee.getPrefShifts().contains(shift));
    }

    @Test
    void removePreferredShift_shouldRemoveSuccessfully() {
        Shift shift = new Shift(LocalDate.now().plusDays(2), ShiftType.EVENING, EMPLOYEE_ID2);
        testEmployee.addPreferredShift(shift); // Precondition
        String result = testEmployee.removePreferredShift(shift);

        assertNull(result);
        assertFalse(testEmployee.getPrefShifts().contains(shift));
    }

    @Test
    void addAssignedShift() {
        Shift shift = new Shift(LocalDate.now().plusDays(3), ShiftType.EVENING, EMPLOYEE_ID2);
        Role role = Role.CASHIER;
        String result = testEmployee.addAssignedShift(shift, role);

        assertNull(result);
        assertTrue(testEmployee.getAssignedShifts().containsKey(shift));
        assertEquals(role, testEmployee.getAssignedShifts().get(shift));
    }

    @Test
    void removeAssignedShift_shouldRemoveSuccessfully() {
        Shift shift = new Shift(LocalDate.now().plusDays(4), ShiftType.MORNING, EMPLOYEE_ID2);
        Role role = Role.CASHIER;
        testEmployee.addAssignedShift(shift, role); // Precondition

        String result = testEmployee.removeAssignedShift(shift);

        assertNull(result);
        assertFalse(testEmployee.getAssignedShifts().containsKey(shift));
    }
}