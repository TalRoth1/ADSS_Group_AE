package tests;

import java.time.LocalDate;

import org.junit.After;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import org.junit.Before;
import org.junit.Test;

import DomainLayer.LocationDL;
import DomainLayer.Role;
import DomainLayer.Shift;
import DomainLayer.ShiftType;

public class ShiftTest {

    private Shift shift;
    private final int MANAGER_ID = 999;
    private final int EMPLOYEE_ID = 200;
    private final LocalDate SHIFT_DATE = LocalDate.now();
    private LocationDL testBranch;

    @Before
    public void setUp() {
        testBranch = new LocationDL(1, "Test Branch", 1, "Test Street", "1234567890", "Test Contact", "Zone1");
        shift = new Shift(1, SHIFT_DATE, ShiftType.MORNING, MANAGER_ID, testBranch);
        try {
            shift.setRequiredRoles(Role.CASHIER, 1);
            shift.setRequiredRoles(Role.STORE_KEEPER, 1);
        } catch (Exception e) {
            fail("Failed to set up test: " + e.getMessage());
        }
    }

    @After
    public void tearDown() {
        shift = null;
    }

    @Test
    public void addEmployee_shouldSucceed() throws Exception {
        Shift shift = new Shift(1, LocalDate.now(), ShiftType.MORNING, MANAGER_ID, testBranch);
        shift.setRequiredRoles(Role.CASHIER, 1);
        shift.addEmployee(EMPLOYEE_ID, Role.CASHIER);
        assertTrue(shift.getAssignedEmployeesID().containsKey(EMPLOYEE_ID));
        assertEquals(Role.CASHIER, shift.getAssignedEmployeesID().get(EMPLOYEE_ID));
    }

    @Test(expected = Exception.class)
    public void addEmployee_shouldFail_AlreadyAssigned() throws Exception {
        Shift shift = new Shift(1, LocalDate.now(), ShiftType.MORNING, MANAGER_ID, testBranch);
        shift.setRequiredRoles(Role.CASHIER, 1);
        shift.addEmployee(EMPLOYEE_ID, Role.CASHIER);
        shift.addEmployee(EMPLOYEE_ID, Role.CASHIER);
    }

    @Test(expected = Exception.class)
    public void addEmployee_shouldFail_NoRequiredRoles() throws Exception {
        Shift shift = new Shift(1, LocalDate.now(), ShiftType.MORNING, MANAGER_ID, testBranch);
        shift.setRequiredRoles(Role.CASHIER, 0);
        shift.addEmployee(EMPLOYEE_ID, Role.CASHIER);
    }

    @Test
    public void removeEmployee_shouldSucceed() throws Exception {
        Shift shift = new Shift(1, LocalDate.now(), ShiftType.EVENING, MANAGER_ID, testBranch);
        shift.setRequiredRoles(Role.STORE_KEEPER, 1);
        shift.addEmployee(EMPLOYEE_ID, Role.STORE_KEEPER);
        shift.removeEmployee(EMPLOYEE_ID);
        assertFalse(shift.getAssignedEmployeesID().containsKey(EMPLOYEE_ID));
    }

    @Test(expected = Exception.class)
    public void removeEmployee_shouldFail_NotAssigned() throws Exception {
        Shift shift = new Shift(1, LocalDate.now(), ShiftType.EVENING, MANAGER_ID, testBranch);
        shift.removeEmployee(EMPLOYEE_ID);
    }

    @Test
    public void addPrefemployee_shouldSucceed() throws Exception {
        Shift shift = new Shift(1, LocalDate.now(), ShiftType.MORNING, MANAGER_ID, testBranch);
        shift.setRequiredRoles(Role.CASHIER, 1);
        shift.addPrefemployee(EMPLOYEE_ID, Role.CASHIER);
        assertTrue(shift.getAvailableEmployeesID().containsKey(EMPLOYEE_ID));
        assertEquals(Role.CASHIER, shift.getAvailableEmployeesID().get(EMPLOYEE_ID));
    }

    @Test(expected = Exception.class)
    public void addPrefemployee_shouldFail_AlreadyAvailable() throws Exception {
        Shift shift = new Shift(1, LocalDate.now(), ShiftType.MORNING, MANAGER_ID, testBranch);
        shift.setRequiredRoles(Role.CASHIER, 1);
        shift.addPrefemployee(EMPLOYEE_ID, Role.CASHIER);
        shift.addPrefemployee(EMPLOYEE_ID, Role.CASHIER);
    }

    @Test
    public void removePrefemployee_shouldSucceed() throws Exception {
        Shift shift = new Shift(1, LocalDate.now(), ShiftType.EVENING, MANAGER_ID, testBranch);
        shift.setRequiredRoles(Role.CASHIER, 1);
        shift.addPrefemployee(EMPLOYEE_ID, Role.CASHIER);
        shift.removePrefemployee(EMPLOYEE_ID);
        assertFalse(shift.getAvailableEmployeesID().containsKey(EMPLOYEE_ID));
    }

    @Test(expected = Exception.class)
    public void removePrefemployee_shouldFail_NotAvailable() throws Exception {
        Shift shift = new Shift(1, LocalDate.now(), ShiftType.EVENING, MANAGER_ID, testBranch);
        shift.removePrefemployee(EMPLOYEE_ID);
    }
}
