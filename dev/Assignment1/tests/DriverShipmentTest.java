import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.junit.After;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import org.junit.Before;
import org.junit.Test;

import DomainLayer.DriverDL;
import DomainLayer.EmployeeManager;
import DomainLayer.LocationDL;
import DomainLayer.Role;
import DomainLayer.Shift;
import DomainLayer.ShiftEmployee;
import DomainLayer.ShiftType;

public class DriverShipmentTest {

    private EmployeeManager employeeManager;
    private DriverDL driverTypeB;
    private DriverDL driverTypeC;
    private ShiftEmployee originStorekeeper;
    private LocationDL origin;
    private LocationDL destination;
    private final LocalDate TODAY = LocalDate.now();
    private final int MANAGER_ID = 100;
    private final int DRIVER_B_ID = 200;
    private final int DRIVER_C_ID = 201;
    private final int ORIGIN_STOREKEEPER_ID = 202;
    private final LocalDate TWO_WEEKS_LATER = TODAY.plusDays(14);

    @Before
    public void setUp() throws Exception {
        // Initialize manager
        employeeManager = new EmployeeManager(
                MANAGER_ID,
                "Test Manager",
                "111222",
                7000,
                TODAY,
                20,
                5,
                100,
                100,
                "password123");

        // Create test locations
        origin = new LocationDL(1, "Haifa", 1, "Haifa Street", "1234567890", "Test Contact", "Zone1");
        destination = new LocationDL(2, "Tel Aviv", 2, "Tel Aviv Street", "0987654321", "Test Contact", "Zone2");

        // Add locations to manager
        employeeManager.addBranch(origin);
        employeeManager.addBranch(destination);

        // Create and hire a driver with license type B
        ArrayList<String> licenseTypesB = new ArrayList<>();
        licenseTypesB.add("B");
        driverTypeB = employeeManager.hireDriver(
                DRIVER_B_ID,
                "Driver B",
                origin,
                "333444",
                5000,
                TODAY,
                15,
                10,
                80,
                90,
                "pass123",
                licenseTypesB);

        // Create and hire a driver with license type C
        ArrayList<String> licenseTypesC = new ArrayList<>();
        licenseTypesC.add("C");
        driverTypeC = employeeManager.hireDriver(
                DRIVER_C_ID,
                "Driver C",
                origin,
                "555666",
                5000,
                TODAY,
                15,
                10,
                80,
                90,
                "pass123",
                licenseTypesC);

        // Create and hire a storekeeper for origin
        originStorekeeper = employeeManager.hireEmployee(
                ORIGIN_STOREKEEPER_ID,
                "Origin Storekeeper",
                origin,
                "777888",
                5000,
                TODAY,
                15,
                10,
                80,
                90,
                "pass123",
                Role.STORE_KEEPER);
    }

    @After
    public void tearDown() {
        employeeManager = null;
        driverTypeB = null;
        driverTypeC = null;
        originStorekeeper = null;
        origin = null;
        destination = null;
    }

    @Test
    public void testSuccessfulShipmentAssignmentWithMatchingLicense() throws Exception {
        // Create shifts at both locations
        employeeManager.createShift(1, origin, TODAY, ShiftType.MORNING, MANAGER_ID);
        employeeManager.createShift(2, destination, TODAY, ShiftType.MORNING, MANAGER_ID);

        Shift originShift = employeeManager.getShift(origin, TODAY, ShiftType.MORNING);
        Shift destShift = employeeManager.getShift(destination, TODAY, ShiftType.MORNING);

        // Add driver and storekeeper to origin shift
        originShift.addEmployee(driverTypeB.getId(), Role.DRIVER);
        originShift.addEmployee(originStorekeeper.getId(), Role.STORE_KEEPER);

        // Create and add a storekeeper at destination
        ShiftEmployee destStorekeeper = employeeManager.hireEmployee(
                203,
                "Dest Storekeeper",
                destination,
                "999000",
                5000,
                TODAY,
                15,
                10,
                80,
                90,
                "pass123",
                Role.STORE_KEEPER);
        destShift.addEmployee(destStorekeeper.getId(), Role.STORE_KEEPER);

        // Try to assign a shipment requiring license type B
        List<LocationDL> destinations = List.of(destination);
        DriverDL assignedDriver = employeeManager.assignCheck(TODAY, ShiftType.MORNING, origin, destinations, "B");

        // Verify assignment
        assertNotNull("Driver should be assigned", assignedDriver);
        assertEquals("Correct driver should be assigned", driverTypeB.getId(), assignedDriver.getId());
        assertTrue("Origin shift should be marked as shipment shift", originShift.isShipmentShift());
        assertTrue("Destination shift should be marked as shipment shift", destShift.isShipmentShift());
    }

    @Test
    public void testShipmentAssignmentFailsWithoutDestinationStorekeeper() throws Exception {
        // Create shifts at both locations
        employeeManager.createShift(1, origin, TODAY, ShiftType.MORNING, MANAGER_ID);
        employeeManager.createShift(2, destination, TODAY, ShiftType.MORNING, MANAGER_ID);

        Shift originShift = employeeManager.getShift(origin, TODAY, ShiftType.MORNING);

        // Add driver and storekeeper to origin shift only
        originShift.addEmployee(driverTypeB.getId(), Role.DRIVER);
        originShift.addEmployee(originStorekeeper.getId(), Role.STORE_KEEPER);

        // Try to assign a shipment (should fail due to missing destination storekeeper)
        List<LocationDL> destinations = List.of(destination);
        DriverDL assignedDriver = employeeManager.assignCheck(TODAY, ShiftType.MORNING, origin, destinations, "B");

        // Verify assignment fails
        assertNull("Driver should not be assigned without destination storekeeper", assignedDriver);
        assertFalse("Origin shift should not be marked as shipment shift", originShift.isShipmentShift());
    }

    @Test
    public void testShipmentAssignmentFailsWithoutOriginStorekeeper() throws Exception {
        // Create shifts at both locations
        employeeManager.createShift(1, origin, TODAY, ShiftType.MORNING, MANAGER_ID);
        employeeManager.createShift(2, destination, TODAY, ShiftType.MORNING, MANAGER_ID);

        Shift originShift = employeeManager.getShift(origin, TODAY, ShiftType.MORNING);
        Shift destShift = employeeManager.getShift(destination, TODAY, ShiftType.MORNING);

        // Add only driver to origin shift (no storekeeper)
        originShift.addEmployee(driverTypeB.getId(), Role.DRIVER);

        // Add storekeeper at destination
        ShiftEmployee destStorekeeper = employeeManager.hireEmployee(
                203,
                "Dest Storekeeper",
                destination,
                "999000",
                5000,
                TODAY,
                15,
                10,
                80,
                90,
                "pass123",
                Role.STORE_KEEPER);
        destShift.addEmployee(destStorekeeper.getId(), Role.STORE_KEEPER);

        // Try to assign a shipment (should fail due to missing origin storekeeper)
        List<LocationDL> destinations = List.of(destination);
        DriverDL assignedDriver = employeeManager.assignCheck(TODAY, ShiftType.MORNING, origin, destinations, "B");

        // Verify assignment fails
        assertNull("Driver should not be assigned without origin storekeeper", assignedDriver);
        assertFalse("Origin shift should not be marked as shipment shift", originShift.isShipmentShift());
    }

    @Test
    public void testShipmentAssignmentWithMismatchedLicenseType() throws Exception {
        // Create shifts at both locations
        employeeManager.createShift(1, origin, TODAY, ShiftType.MORNING, MANAGER_ID);
        employeeManager.createShift(2, destination, TODAY, ShiftType.MORNING, MANAGER_ID);

        Shift originShift = employeeManager.getShift(origin, TODAY, ShiftType.MORNING);
        Shift destShift = employeeManager.getShift(destination, TODAY, ShiftType.MORNING);

        // Add type C driver and storekeeper to origin shift
        originShift.addEmployee(driverTypeC.getId(), Role.DRIVER);
        originShift.addEmployee(originStorekeeper.getId(), Role.STORE_KEEPER);

        // Add storekeeper at destination
        ShiftEmployee destStorekeeper = employeeManager.hireEmployee(
                203,
                "Dest Storekeeper",
                destination,
                "999000",
                5000,
                TODAY,
                15,
                10,
                80,
                90,
                "pass123",
                Role.STORE_KEEPER);
        destShift.addEmployee(destStorekeeper.getId(), Role.STORE_KEEPER);

        // Try to assign a shipment requiring license type B to a type C driver
        List<LocationDL> destinations = List.of(destination);
        DriverDL assignedDriver = employeeManager.assignCheck(TODAY, ShiftType.MORNING, origin, destinations, "B");

        // Verify assignment fails
        assertNull("Driver with wrong license type should not be assigned", assignedDriver);
        assertFalse("Origin shift should not be marked as shipment shift", originShift.isShipmentShift());
        assertFalse("Destination shift should not be marked as shipment shift", destShift.isShipmentShift());
    }

    @Test
    public void testMultiDestinationShipmentRequiresAllStorekeepers() throws Exception {
        // Create another destination
        LocationDL destination2 = new LocationDL(3, "Jerusalem", 3, "Jerusalem Street", "5555555", "Test Contact",
                "Zone3");
        employeeManager.addBranch(destination2);

        // Create shifts at all locations
        employeeManager.createShift(1, origin, TODAY, ShiftType.MORNING, MANAGER_ID);
        employeeManager.createShift(2, destination, TODAY, ShiftType.MORNING, MANAGER_ID);
        employeeManager.createShift(3, destination2, TODAY, ShiftType.MORNING, MANAGER_ID);

        Shift originShift = employeeManager.getShift(origin, TODAY, ShiftType.MORNING);
        Shift destShift1 = employeeManager.getShift(destination, TODAY, ShiftType.MORNING);
        Shift destShift2 = employeeManager.getShift(destination2, TODAY, ShiftType.MORNING);

        // Add driver and storekeeper to origin shift
        originShift.addEmployee(driverTypeB.getId(), Role.DRIVER);
        originShift.addEmployee(originStorekeeper.getId(), Role.STORE_KEEPER);

        // Add storekeeper only to first destination (missing second destination
        // storekeeper)
        ShiftEmployee destStorekeeper1 = employeeManager.hireEmployee(203, "Dest Storekeeper 1",
                destination, "999000", 5000, TODAY, 15, 10, 80, 90, "pass123", Role.STORE_KEEPER);
        destShift1.addEmployee(destStorekeeper1.getId(), Role.STORE_KEEPER);

        // Try to assign a shipment with multiple destinations (should fail due to
        // missing storekeeper)
        List<LocationDL> destinations = List.of(destination, destination2);
        DriverDL assignedDriver = employeeManager.assignCheck(TODAY, ShiftType.MORNING, origin, destinations, "B");

        // Verify assignment fails
        assertNull("Driver should not be assigned when destination is missing storekeeper", assignedDriver);
        assertFalse("Origin shift should not be marked as shipment shift", originShift.isShipmentShift());
        assertFalse("First destination shift should not be marked as shipment shift", destShift1.isShipmentShift());
        assertFalse("Second destination shift should not be marked as shipment shift", destShift2.isShipmentShift());
    }

    @Test
    public void testScheduleFutureShipment() throws Exception {
        // Create shifts for two weeks later
        employeeManager.createShift(1, origin, TWO_WEEKS_LATER, ShiftType.MORNING, MANAGER_ID);
        employeeManager.createShift(2, destination, TWO_WEEKS_LATER, ShiftType.MORNING, MANAGER_ID);

        Shift futureOriginShift = employeeManager.getShift(origin, TWO_WEEKS_LATER, ShiftType.MORNING);
        Shift futureDestShift = employeeManager.getShift(destination, TWO_WEEKS_LATER, ShiftType.MORNING);

        // Add driver and storekeeper to future origin shift
        futureOriginShift.addEmployee(driverTypeB.getId(), Role.DRIVER);
        futureOriginShift.addEmployee(originStorekeeper.getId(), Role.STORE_KEEPER);

        // Add storekeeper to future destination shift
        ShiftEmployee destStorekeeper = employeeManager.hireEmployee(
                203,
                "Future Dest Storekeeper",
                destination,
                "999000",
                5000,
                TODAY,
                15,
                10,
                80,
                90,
                "pass123",
                Role.STORE_KEEPER);
        futureDestShift.addEmployee(destStorekeeper.getId(), Role.STORE_KEEPER);

        // Schedule future shipment
        List<LocationDL> destinations = List.of(destination);
        DriverDL assignedDriver = employeeManager.assignCheck(TWO_WEEKS_LATER, ShiftType.MORNING, origin, destinations,
                "B");

        // Verify future assignment
        assertNotNull("Driver should be assigned for future shipment", assignedDriver);
        assertEquals("Correct driver should be assigned for future shipment", driverTypeB.getId(),
                assignedDriver.getId());
        assertTrue("Future origin shift should be marked as shipment shift", futureOriginShift.isShipmentShift());
        assertTrue("Future destination shift should be marked as shipment shift", futureDestShift.isShipmentShift());

        // Verify the shipment is saved and can be retrieved
        assertTrue("Future shipment should exist in origin shift",
                employeeManager.hasShipmentScheduled(origin, TWO_WEEKS_LATER, ShiftType.MORNING));
        assertTrue("Future shipment should exist in destination shift",
                employeeManager.hasShipmentScheduled(destination, TWO_WEEKS_LATER, ShiftType.MORNING));
    }

    @Test
    public void testModifyFutureShipment() throws Exception {
        // First schedule a future shipment
        employeeManager.createShift(1, origin, TWO_WEEKS_LATER, ShiftType.MORNING, MANAGER_ID);
        employeeManager.createShift(2, destination, TWO_WEEKS_LATER, ShiftType.MORNING, MANAGER_ID);

        Shift futureOriginShift = employeeManager.getShift(origin, TWO_WEEKS_LATER, ShiftType.MORNING);
        Shift futureDestShift = employeeManager.getShift(destination, TWO_WEEKS_LATER, ShiftType.MORNING);

        // Add initial driver and storekeepers
        futureOriginShift.addEmployee(driverTypeB.getId(), Role.DRIVER);
        futureOriginShift.addEmployee(originStorekeeper.getId(), Role.STORE_KEEPER);

        ShiftEmployee destStorekeeper = employeeManager.hireEmployee(
                203,
                "Future Dest Storekeeper",
                destination,
                "999000",
                5000,
                TODAY,
                15,
                10,
                80,
                90,
                "pass123",
                Role.STORE_KEEPER);
        futureDestShift.addEmployee(destStorekeeper.getId(), Role.STORE_KEEPER);

        // Schedule initial shipment
        List<LocationDL> destinations = List.of(destination);
        DriverDL initialDriver = employeeManager.assignCheck(TWO_WEEKS_LATER, ShiftType.MORNING, origin, destinations,
                "B");
        assertNotNull("Initial driver should be assigned", initialDriver);

        // Now modify the shipment by changing the driver
        futureOriginShift.removeEmployee(driverTypeB.getId());
        futureOriginShift.addEmployee(driverTypeC.getId(), Role.DRIVER);

        // Try to reassign with type C driver for a type B shipment (should fail)
        DriverDL newDriver = employeeManager.assignCheck(TWO_WEEKS_LATER, ShiftType.MORNING, origin, destinations, "B");
        assertNull("Incompatible driver should not be assigned", newDriver);

        // Verify the original shipment was cancelled
        assertFalse("Origin shift should not be marked as shipment shift after failed reassignment",
                futureOriginShift.isShipmentShift());
        assertFalse("Destination shift should not be marked as shipment shift after failed reassignment",
                futureDestShift.isShipmentShift());
    }

    @Test
    public void testShipmentWithoutAvailableDriver() throws Exception {
        // Create shifts for two weeks later
        employeeManager.createShift(1, origin, TWO_WEEKS_LATER, ShiftType.MORNING, MANAGER_ID);
        employeeManager.createShift(2, destination, TWO_WEEKS_LATER, ShiftType.MORNING, MANAGER_ID);

        Shift futureOriginShift = employeeManager.getShift(origin, TWO_WEEKS_LATER, ShiftType.MORNING);
        Shift futureDestShift = employeeManager.getShift(destination, TWO_WEEKS_LATER, ShiftType.MORNING);

        // Add only storekeepers to shifts (no driver)
        futureOriginShift.addEmployee(originStorekeeper.getId(), Role.STORE_KEEPER);

        ShiftEmployee destStorekeeper = employeeManager.hireEmployee(
                203,
                "Future Dest Storekeeper",
                destination,
                "999000",
                5000,
                TODAY,
                15,
                10,
                80,
                90,
                "pass123",
                Role.STORE_KEEPER);
        futureDestShift.addEmployee(destStorekeeper.getId(), Role.STORE_KEEPER);

        // Try to schedule shipment without any driver in the shift
        List<LocationDL> destinations = List.of(destination);
        DriverDL assignedDriver = employeeManager.assignCheck(TWO_WEEKS_LATER, ShiftType.MORNING, origin, destinations,
                "B");

        // Verify assignment fails
        assertNull("Driver should not be assigned when no driver is available", assignedDriver);
        assertFalse("Origin shift should not be marked as shipment shift without driver",
                futureOriginShift.isShipmentShift());
        assertFalse("Destination shift should not be marked as shipment shift without driver",
                futureDestShift.isShipmentShift());

        // Now add a driver but with wrong license type
        futureOriginShift.addEmployee(driverTypeC.getId(), Role.DRIVER);

        // Try to schedule shipment with wrong license type driver
        assignedDriver = employeeManager.assignCheck(TWO_WEEKS_LATER, ShiftType.MORNING, origin, destinations, "B");

        // Verify assignment still fails
        assertNull("Driver should not be assigned when no driver with correct license is available", assignedDriver);
        assertFalse("Origin shift should not be marked as shipment shift with wrong license driver",
                futureOriginShift.isShipmentShift());
        assertFalse("Destination shift should not be marked as shipment shift with wrong license driver",
                futureDestShift.isShipmentShift());

        // Add correct driver but mark them as finished working (fired/unavailable)
        futureOriginShift.removeEmployee(driverTypeC.getId());
        employeeManager.fireEmployee(driverTypeB.getId());
        futureOriginShift.addEmployee(driverTypeB.getId(), Role.DRIVER);

        // Try to schedule shipment with fired driver
        assignedDriver = employeeManager.assignCheck(TWO_WEEKS_LATER, ShiftType.MORNING, origin, destinations, "B");

        // Verify assignment still fails
        assertNull("Driver should not be assigned when only available driver is fired", assignedDriver);
        assertFalse("Origin shift should not be marked as shipment shift with fired driver",
                futureOriginShift.isShipmentShift());
        assertFalse("Destination shift should not be marked as shipment shift with fired driver",
                futureDestShift.isShipmentShift());
    }

}
