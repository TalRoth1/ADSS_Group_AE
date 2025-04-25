package DomainLayer;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class EmployeeManagerTest {
    private EmployeeManager manager;
    private final int MANAGER_ID = 100;
    private final LocalDate START_DATE = LocalDate.now();
    private ShiftEmployee testEmployee;
    private final int EMPLOYEE_ID = 101;

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
        manager = null;
        testEmployee = null;
    }

    @Test
    void hireEmployee_Success() {
        ShiftEmployee newEmployee = manager.hireEmployee(
                102,
                "New Employee",
                "Branch1",
                "555666",
                5000,
                START_DATE,
                15,
                10,
                80,
                90,
                "pass123",
                Role.CASHIER
        );

        assertNotNull(newEmployee);
        assertTrue(manager.checkEmployee(102));
    }

    @Test
    void fireEmployee_Success() {
        String result = manager.fireEmployee(EMPLOYEE_ID);
        assertEquals("Employee: 101 fired successfully", result);
        assertTrue(testEmployee.isFinishWorking());
    }

    @Test
    void fireEmployee_NonExistentEmployee() {
        String result = manager.fireEmployee(999);
        assertEquals("999 not exist", result);
    }

    @Test
    void updateBankAccountEmployee_Success() {
        String result = manager.updateBankAccountEmployee(EMPLOYEE_ID, "999888");
        assertNull(result);
        assertEquals("999888", testEmployee.getBankAccount());
    }

    @Test
    void updateBankAccountEmployee_NonExistentEmployee() {
        String result = manager.updateBankAccountEmployee(999, "999888");
        assertEquals("999 not exist", result);
    }

    @Test
    void addRoleToEmployee_Success() {
        String result = manager.addRoleToEmployee(EMPLOYEE_ID, Role.DRIVER);
        assertNull(result);
        assertTrue(testEmployee.getRoles().contains(Role.DRIVER));
    }

    @Test
    void addRoleToEmployee_DuplicateRole() {
        String result = manager.addRoleToEmployee(EMPLOYEE_ID, Role.CASHIER);
        assertEquals("Test Employee is already " + Role.CASHIER.toString(), result);
    }

    @Test
    void createShift_Success() {
        // First add shift manager role to test employee
        manager.addRoleToEmployee(EMPLOYEE_ID, Role.SHIFT_MANAGER);

        String result = manager.createShift(
                START_DATE.plusDays(1),
                ShiftType.MORNING,
                EMPLOYEE_ID
        );

        assertNull(result);
    }

    @Test
    void createShift_InvalidShiftManager() {
        String result = manager.createShift(
                START_DATE.plusDays(1),
                ShiftType.MORNING,
                EMPLOYEE_ID // Employee doesn't have SHIFT_MANAGER role
        );

        assertEquals("this employee can't be a shift manager", result);
    }

    @Test
    void addEmployeeToShift_Success() {
        // Create a shift first
        manager.addRoleToEmployee(EMPLOYEE_ID, Role.SHIFT_MANAGER);
        manager.createShift(START_DATE.plusDays(1), ShiftType.MORNING, EMPLOYEE_ID);

        Shift shift = manager.getShift(START_DATE.plusDays(1), ShiftType.MORNING);
        manager.setRequiredRole(shift, Role.CASHIER, 1);
        String result = manager.addEmployeeToShift(EMPLOYEE_ID, shift, Role.CASHIER);

        assertNull(result);
        assertTrue(shift.getAssignedEmployeesID().containsKey(EMPLOYEE_ID));
    }
}