package DomainLayer;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class EmployeeFacadeTest {

    private EmployeeFacade employeeFacade;
    private final int MANAGER_ID = 100;
    private final String MANAGER_PASSWORD = "123";
    private final LocalDate START_DATE = LocalDate.now();

    @BeforeEach
    void setUp() {
        employeeFacade = new EmployeeFacade();
        // Add a manager for testing
        employeeFacade.addFirstEmployeeManager(
                MANAGER_ID,
                "Keren",
                "Branch1",
                "111222",
                7000,
                START_DATE,
                20,
                5,
                100,
                100,
                MANAGER_PASSWORD
        );
    }

    @AfterEach
    void tearDown() {
        if (employeeFacade != null) {
            // Make sure any logged in users are logged out
            if (employeeFacade.getEmployee(MANAGER_ID) != null) {
                employeeFacade.logout(MANAGER_ID);
            }
            employeeFacade = null;
        }

    }

    @Test
    void hireEmployee_SuccessfulHire() {
        // Login manager first
        employeeFacade.login(MANAGER_ID, MANAGER_PASSWORD);

        String result = employeeFacade.hireEmployee(
                101, // employeeId
                MANAGER_ID, // managerId
                "Branch1", // branch
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

        assertEquals("Employee: 101 hired successfully", result);
    }

    @Test
    void hireEmployee_SuccessfulHire2() {
        // Login manager first
        employeeFacade.login(MANAGER_ID, MANAGER_PASSWORD);

        String result = employeeFacade.hireEmployee(
                101, // employeeId
                MANAGER_ID, // managerId
                "Branch1", // branch
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

        assertEquals("Employee: 101 hired successfully", result);
    }

    @Test
    void hireEmployee_ManagerNotLoggedIn() {
        String result = employeeFacade.hireEmployee(
                101,
                MANAGER_ID,
                "Branch1",
                "John Doe",
                "333444",
                5000,
                START_DATE,
                15,
                10,
                80,
                90,
                "pass123",
                Role.CASHIER
        );

        assertEquals("You are not logged in", result);
    }

    @Test
    void login_SuccessfulLogin() {
        Employee result = employeeFacade.login(MANAGER_ID, MANAGER_PASSWORD);
        assertNotNull(result);
    }

    @Test
    void login_FailedLogin() {
        Employee result = employeeFacade.login(MANAGER_ID, "wrongPassword");
        assertNull(result);
    }

    @Test
    void logout_SuccessfulLogout() {
        employeeFacade.login(MANAGER_ID, MANAGER_PASSWORD);
        String result = employeeFacade.logout(MANAGER_ID);
        assertNull(result);
    }

    @Test
    void logout_NotLoggedIn() {
        String result = employeeFacade.logout(MANAGER_ID);
        assertEquals("You are not logged in", result);
    }

    @Test
    void updateSickDays_Success() {
        // First login and hire an employee
        employeeFacade.login(MANAGER_ID, MANAGER_PASSWORD);
        employeeFacade.hireEmployee(101, MANAGER_ID, "Branch1", "John Doe", "333444",
                5000, START_DATE, 15, 10, 80, 90, "pass123", Role.CASHIER);

        String result = employeeFacade.updateSickDays(101, MANAGER_ID, 15);
        assertNull(result); // Successful update returns null
    }

    @Test
    void updateSickDays_ManagerNotLoggedIn() {
        String result = employeeFacade.updateSickDays(101, MANAGER_ID, 15);
        assertEquals("You are not logged in", result);
    }


}

