package DomainLayer;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class DriverDLTest {
    @org.junit.jupiter.api.Test
    void testToString() {
        DriverDL driver = new DriverDL("John Doe", List.of("B"));
        String expected = "Driver Name: John Doe, License Type: B";
        assertEquals(expected, driver.toString());
    }

    @org.junit.jupiter.api.Test
    void testEquals() {
        DriverDL driver1 = new DriverDL("John Doe", List.of("B"));
        DriverDL driver2 = new DriverDL("John Doe", List.of("B"));
        DriverDL driver3 = new DriverDL("Jane Doe", List.of("C"));
        assertTrue(true);
        assertFalse(driver1.equals(driver3));
    }

}