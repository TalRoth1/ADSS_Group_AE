package tests;

import java.time.LocalDate;

import org.junit.After;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import org.junit.Before;
import org.junit.Test;

import DomainLayer.Employee;
import DomainLayer.EmployeeFacade;
import DomainLayer.LocationDL;
import DomainLayer.Role;

public class EmployeeFacadeTest {

    private EmployeeFacade employeeFacade;
    private final int MANAGER_ID = 100;
    private final String MANAGER_PASSWORD = "123";
    private final LocalDate START_DATE = LocalDate.now();
    private LocationDL testBranch;

    @Before
    public void setUp() throws Exception {
        employeeFacade = new EmployeeFacade();
        testBranch = new LocationDL(1, "Test Branch", 1, "Test Street", "1234567890", "Test Contact", "Zone1");
        employeeFacade.addBranch(testBranch);

        // Add a manager for testing
        employeeFacade.addFirstEmployeeManager(
                MANAGER_ID,
                "Keren",
                testBranch,
                "111222",
                7000,
                START_DATE,
                20,
                5,
                100,
                100,
                MANAGER_PASSWORD);
    }

    @After
    public void tearDown() {
        if (employeeFacade != null) {
            // Make sure any logged in users are logged out
            Employee emp = employeeFacade.getEmployee(MANAGER_ID);
            if (emp != null) {
                try {
                    employeeFacade.logout(MANAGER_ID);
                } catch (Exception e) {
                    // Ignore logout errors during cleanup
                }
            }
            employeeFacade = null;
        }
    }

    @Test
    public void hireEmployee_SuccessfulHire() throws Exception {
        // Login manager first
        employeeFacade.login(MANAGER_ID, MANAGER_PASSWORD);

        employeeFacade.hireEmployee(
                101, // employeeId
                MANAGER_ID, // managerId
                testBranch, // branch
                "John Doe", // name
                "333444", // bankAccount
                5000, // salary
                START_DATE,
                15, // vacationDays
                10, // sickDays
                80, // educationFund
                90, // socialBenefits
                "pass123", // password
                Role.CASHIER // role
        );

        Employee employee = employeeFacade.getEmployee(101);
        assertNotNull(employee);
        assertEquals(101, employee.getId());
    }

    @Test(expected = Exception.class)
    public void hireEmployee_ManagerNotLoggedIn() throws Exception {
        employeeFacade.hireEmployee(
                101,
                MANAGER_ID,
                testBranch,
                "John Doe",
                "333444",
                5000,
                START_DATE,
                15,
                10,
                80,
                90,
                "pass123",
                Role.CASHIER);
    }

    @Test
    public void login_SuccessfulLogin() throws Exception {
        Employee result = employeeFacade.login(MANAGER_ID, MANAGER_PASSWORD);
        assertNotNull(result);
    }

    @Test
    public void login_FailedLogin() throws Exception {
        try {
            employeeFacade.login(MANAGER_ID, "wrongPassword");
            fail("Expected login to fail with wrong password");
        } catch (Exception e) {
            assertTrue(e.getMessage().contains("Login failed"));
        }
    }

    @Test
    public void logout_SuccessfulLogout() throws Exception {
        employeeFacade.login(MANAGER_ID, MANAGER_PASSWORD);
        employeeFacade.logout(MANAGER_ID);
        try {
            employeeFacade.logout(MANAGER_ID);
            fail("Expected second logout to fail");
        } catch (Exception e) {
            assertTrue(e.getMessage().contains("not logged in"));
        }
    }

    @Test(expected = Exception.class)
    public void logout_NotLoggedIn() throws Exception {
        employeeFacade.logout(MANAGER_ID);
    }

    @Test
    public void updateSickDays_Success() throws Exception {
        // First login and hire an employee
        employeeFacade.login(MANAGER_ID, MANAGER_PASSWORD);
        employeeFacade.hireEmployee(
                101,
                MANAGER_ID,
                testBranch,
                "John Doe",
                "333444",
                5000,
                START_DATE,
                15,
                10,
                80,
                90,
                "pass123",
                Role.CASHIER);

        employeeFacade.updateSickDays(101, MANAGER_ID, 15);
        Employee employee = employeeFacade.getEmployee(101);
        assertEquals(15, employee.getSickDays());
    }

    @Test(expected = Exception.class)
    public void updateSickDays_ManagerNotLoggedIn() throws Exception {
        employeeFacade.updateSickDays(101, MANAGER_ID, 15);
    }
}
