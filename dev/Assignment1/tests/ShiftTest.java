package DomainLayer;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class ShiftTest {
    private Shift shift;
    private final int MANAGER_ID = 999;
    private final int EMPLOYEE_ID = 200;
    private final LocalDate SHIFT_DATE = LocalDate.now();

    @BeforeEach
    void setUp() {
        // Initialize a new shift before each test
        shift = new Shift(SHIFT_DATE, ShiftType.MORNING, MANAGER_ID);

        // Set some roles required for tests (can be adjusted in each test too)
        shift.setRequiredRoles(Role.CASHIER, 1);
        shift.setRequiredRoles(Role.STORE_KEEPER, 1);
    }

    @AfterEach
    void tearDown() {
        // Clear data to ensure a clean state between tests
        shift = null;
    }

    @Test
    void addEmployee_shouldSucceed() {
        Shift shift = new Shift(LocalDate.now(), ShiftType.MORNING, MANAGER_ID);
        shift.setRequiredRoles(Role.CASHIER, 1);
        String result = shift.addEmployee(EMPLOYEE_ID, Role.CASHIER);

        assertNull(result);
        assertTrue(shift.getAssignedEmployeesID().containsKey(EMPLOYEE_ID));
        assertEquals(Role.CASHIER, shift.getAssignedEmployeesID().get(EMPLOYEE_ID));
    }

    @Test
    void addEmployee_shouldFail_AlreadyAssigned() {
        Shift shift = new Shift(LocalDate.now(), ShiftType.MORNING, MANAGER_ID);
        shift.setRequiredRoles(Role.CASHIER, 1);
        shift.addEmployee(EMPLOYEE_ID, Role.CASHIER);
        String result = shift.addEmployee(EMPLOYEE_ID, Role.CASHIER);

        assertEquals("Employee already assigned to this shift.", result);
    }

    @Test
    void addEmployee_shouldFail_NoRequiredRoles() {
        Shift shift = new Shift(LocalDate.now(), ShiftType.MORNING, MANAGER_ID);
        shift.setRequiredRoles(Role.CASHIER, 0);
        String result = shift.addEmployee(EMPLOYEE_ID, Role.CASHIER);
        assertEquals("No more employees required for this role.", result);
    }


    @Test
    void removeEmployee_shouldSucceed() {
        Shift shift = new Shift(LocalDate.now(), ShiftType.EVENING, MANAGER_ID);
        shift.setRequiredRoles(Role.STORE_KEEPER, 1);
        shift.addEmployee(EMPLOYEE_ID, Role.STORE_KEEPER);
        String result = shift.removeEmployee(EMPLOYEE_ID);

        assertNull(result);
        assertFalse(shift.getAssignedEmployeesID().containsKey(EMPLOYEE_ID));
    }

    @Test
    void removeEmployee_shouldFail_NotAssigned() {
        Shift shift = new Shift(LocalDate.now(), ShiftType.EVENING, MANAGER_ID);
        String result = shift.removeEmployee(EMPLOYEE_ID);

        assertEquals("Employee not assigned to this shift.", result);
    }


    @Test
    void addPrefemployee_shouldSucceed() {
        Shift shift = new Shift(LocalDate.now(), ShiftType.MORNING, MANAGER_ID);
        shift.setRequiredRoles(Role.CASHIER, 1);
        String result = shift.addPrefemployee(EMPLOYEE_ID, Role.CASHIER);
        assertNull(result);
    }

    @Test
    void addPrefemployee_shouldFail_AlreadyAvailable() {
        Shift shift = new Shift(LocalDate.now(), ShiftType.MORNING, MANAGER_ID);
        shift.setRequiredRoles(Role.CASHIER, 1);
        shift.addPrefemployee(EMPLOYEE_ID, Role.CASHIER);
        String result = shift.addPrefemployee(EMPLOYEE_ID, Role.CASHIER);
        assertEquals("already available for this shift.", result);
    }


    @Test
    void removePrefemployee_shouldSucceed() {
        Shift shift = new Shift(LocalDate.now(), ShiftType.EVENING, MANAGER_ID);
        shift.setRequiredRoles(Role.CASHIER, 1);
        shift.addPrefemployee(EMPLOYEE_ID, Role.CASHIER);
        String result = shift.removePrefemployee(EMPLOYEE_ID);
        assertNull(result);
    }

    @Test
    void removePrefemployee_shouldFail_NotAvailable() {
        Shift shift = new Shift(LocalDate.now(), ShiftType.EVENING, MANAGER_ID);
        String result = shift.removePrefemployee(EMPLOYEE_ID);
        assertEquals("Employee not assigned to this shift.", result);
    }
}