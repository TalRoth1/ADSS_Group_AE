package DomainLayer;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import static org.junit.jupiter.api.Assertions.*;

class ShipmentFacadeTest {
    private static ShipmentFacade facade;


    @BeforeEach
    void clearFacade() {
        facade = new ShipmentFacade();
    }

    @Test
    void testCreateShipment() throws Exception {
        TruckDL truck = facade.AddTruck(1, "TeslaX", "TypeA", 1000);
        DriverDL driver = facade.AddDriver("Joe Mama", List.of("TypeA"));
        LocationDL origin = facade.AddLocation("Main St", 1, "Neverland", "039000000000", "John Doe", "Zone1");
        LocationDL destination = facade.AddLocation("Second St", 2, "Neverland", "123", "Jane Doe", "Zone2");
        Map<LocationDL, Map<String, Integer>> items = Map.of(destination, Map.of("egg carton", 1, "milk", 2));
        facade.CreateShipment(truck, driver, origin, List.of(destination), items);
        assertTrue(facade.shipments.size() > 0);
    }

    @Test
    void testChangeStatus() throws Exception {
        TruckDL truck = facade.AddTruck(1, "TeslaX", "TypeA", 1000);
        DriverDL driver = facade.AddDriver("Joe Mama", List.of("TypeA"));
        LocationDL origin = facade.AddLocation("Main St", 1, "Neverland", "039000000000", "John Doe", "Zone1");
        LocationDL destination = facade.AddLocation("Second St", 2, "Neverland", "123", "Jane Doe", "Zone2");
        Map<LocationDL, Map<String, Integer>> items = Map.of(destination, Map.of("egg carton", 1, "milk", 2));
        //ShipmentDL shipment = new ShipmentDL(truck, driver, origin, List.of(destination), items);
        //facade.shipments.add(shipment);
        facade.CreateShipment(truck, driver, origin, List.of(destination), items);
        ShipmentDL shipment = facade.shipments.getFirst();
        shipment.ChangeStatus("SENT");
        assertEquals("SENT", shipment.Status.toString());
    }

    @Test
    //test for editing a field
    void editShipmentTest1() throws Exception {
        TruckDL truck = facade.AddTruck(1, "TeslaX", "TypeA", 1000);
        DriverDL driver = facade.AddDriver("Joe Mama", List.of("TypeA"));
        LocationDL origin = facade.AddLocation("Main St", 1, "Neverland", "039000000000", "John Doe", "Zone1");
        LocationDL destination = facade.AddLocation("Second St", 2, "Neverland", "123", "Jane Doe", "Zone2");
        Map<LocationDL, Map<String, Integer>> items = Map.of(destination, Map.of("egg carton", 1, "milk", 2));
        //ShipmentDL shipment = new ShipmentDL(truck, driver, origin, List.of(destination), items);
        //facade.shipments.add(shipment);
        facade.CreateShipment(truck, driver, origin, List.of(destination), items);
        ShipmentDL shipment = facade.shipments.getFirst();
        DriverDL driver2 = facade.AddDriver("Bob A", List.of("TypeA"));
        facade.EditShipement(shipment,null, driver2, null, null, null);
        assertEquals(driver2, shipment.DriverName);
    }

    @Test
    //test for trying to change the driver to one that does not have the right license
    //should fail
    void editShipmentTest2() throws Exception {
        TruckDL truck = facade.AddTruck(1, "TeslaX", "TypeA", 1000);
        DriverDL driver = facade.AddDriver("Joe Mama", List.of("TypeA"));
        LocationDL origin = facade.AddLocation("Main St", 1, "Neverland", "039000000000", "John Doe", "Zone1");
        LocationDL destination = facade.AddLocation("Second St", 2, "Neverland", "123", "Jane Doe", "Zone2");
        Map<LocationDL, Map<String, Integer>> items = Map.of(destination, Map.of("egg carton", 1, "milk", 2));
        //ShipmentDL shipment = new ShipmentDL(truck, driver, origin, List.of(destination), items);
        //facade.shipments.add(shipment);
        facade.CreateShipment(truck, driver, origin, List.of(destination), items);
        ShipmentDL shipment = facade.shipments.getFirst();
        DriverDL driver2 = facade.AddDriver("Bob A", List.of("TypeB"));
        Exception exception = assertThrows(Exception.class, () -> {
            facade.EditShipement(shipment, null, driver2, null, null, null);
        });
        String expectedMessage = "Driver does not have the right license for this truck";
        String actualMessage = exception.getMessage();
        assertTrue(actualMessage.contains(expectedMessage));
    }

    @Test
    // test for overweight truck
    void testCreateShipment2() throws Exception {
        TruckDL truck = facade.AddTruck(1, "TeslaX", "TypeA", 1);
        DriverDL driver = facade.AddDriver("Joe Mama", List.of("TypeA"));
        LocationDL origin = facade.AddLocation("Main St", 1, "Neverland", "039000000000", "John Doe", "Zone1");
        LocationDL destination = facade.AddLocation("Second St", 2, "Neverland", "123", "Jane Doe", "Zone2");
        Map<LocationDL, Map<String, Integer>> items = Map.of(destination, Map.of("egg carton", 1, "milk", 2));
        Exception exception = assertThrows(Exception.class, () -> {
            facade.CreateShipment(truck, driver, origin, List.of(destination), items);
        });
        String expectedMessage = "Truck is overweight";
        String actualMessage = exception.getMessage();
        assertTrue(actualMessage.contains(expectedMessage));
    }

    @Test
    // test for adding items to shipment
    void testAddItems() throws Exception {
        TruckDL truck = facade.AddTruck(1, "TeslaX", "TypeA", 1000);
        DriverDL driver = facade.AddDriver("Joe Mama", List.of("TypeA"));
        LocationDL origin = facade.AddLocation("Main St", 1, "Neverland", "039000000000", "John Doe", "Zone1");
        LocationDL destination = facade.AddLocation("Second St", 2, "Neverland", "123", "Jane Doe", "Zone2");
        Map<LocationDL, Map<String, Integer>> items = new HashMap<>(Map.of(destination, new HashMap<>(Map.of("egg carton", 1, "milk", 2))));
        //ShipmentDL shipment = new ShipmentDL(truck, driver, origin, List.of(destination), items);
        //facade.shipments.add(shipment);
        facade.CreateShipment(truck, driver, origin, List.of(destination), items);
        ShipmentDL shipment = facade.shipments.getFirst();
        Map<LocationDL, Map<String, Integer>> newItems = new HashMap<>(Map.of(destination, new HashMap<>(Map.of("bread", 1))));
        facade.EditShipement(shipment, null, null, null, null, newItems);
        assertTrue(shipment.getDocument().getItemsMap().get(destination).containsKey("bread"));
    }

    @Test
        // test for removing items to shipment
    void testRemoveItems() throws Exception {
        TruckDL truck = facade.AddTruck(1, "TeslaX", "TypeA", 1000);
        DriverDL driver = facade.AddDriver("Joe Mama", List.of("TypeA"));
        LocationDL origin = facade.AddLocation("Main St", 1, "Neverland", "039000000000", "John Doe", "Zone1");
        LocationDL destination = facade.AddLocation("Second St", 2, "Neverland", "123", "Jane Doe", "Zone2");
        Map<LocationDL, Map<String, Integer>> items = new HashMap<>(Map.of(destination, new HashMap<>(Map.of("egg carton", 1, "milk", 2))));
        //ShipmentDL shipment = new ShipmentDL(truck, driver, origin, List.of(destination), items);
        //facade.shipments.add(shipment);
        facade.CreateShipment(truck, driver, origin, List.of(destination), items);
        ShipmentDL shipment = facade.shipments.getFirst();
        Map<LocationDL, Map<String, Integer>> newItems = new HashMap<>(Map.of(destination, new HashMap<>(Map.of("egg carton", 0))));
        facade.EditShipement(shipment, null, null, null, null, newItems);
        assertFalse(shipment.getDocument().getItemsMap().get(destination).containsKey("egg carton"));
    }

    @Test
        // test for adding too many items to truck
        // should fail
    void testAddItemsFail() throws Exception {
        TruckDL truck = facade.AddTruck(1, "TeslaX", "TypeA", 10);
        DriverDL driver = facade.AddDriver("Joe Mama", List.of("TypeA"));
        LocationDL origin = facade.AddLocation("Main St", 1, "Neverland", "039000000000", "John Doe", "Zone1");
        LocationDL destination = facade.AddLocation("Second St", 2, "Neverland", "123", "Jane Doe", "Zone2");
        Map<LocationDL, Map<String, Integer>> items = new HashMap<>(Map.of(destination, new HashMap<>(Map.of("egg carton", 1, "milk", 2))));
        //ShipmentDL shipment = new ShipmentDL(truck, driver, origin, List.of(destination), items);
        //facade.shipments.add(shipment);
        facade.CreateShipment(truck, driver, origin, List.of(destination), items);
        ShipmentDL shipment = facade.shipments.getFirst();
        Map<LocationDL, Map<String, Integer>> newItems = new HashMap<>(Map.of(destination, new HashMap<>(Map.of("bread", 100))));
        Exception exception = assertThrows(Exception.class, () -> {
            facade.EditShipement(shipment, null, null, null, null, newItems);
        });
        String expectedMessage = "Truck cannot support the new weight";
        String actualMessage = exception.getMessage();
        assertTrue(actualMessage.contains(expectedMessage));
    }

    @Test
    // test for adding an item to the database
    void testAddItem() {
        String itemName = "test";
        float weight = 1.0f;
        facade.AddItem(itemName, weight);
        assertTrue(facade.Items.containsKey(itemName));
        assertEquals(weight, facade.Items.get(itemName));
    }

    @Test
    // test for checking locations zones
    void testLocationByZone() {
        String zone = "Zone1";
        LocationDL location1 = facade.AddLocation("Main St", 1, "Neverland", "039000000000", "John Doe", zone);
        LocationDL location2 = facade.AddLocation("Second St", 2, "Neverland", "123", "Jane Doe", "Zone2");
        List<LocationDL> locations = facade.LocationByZone(zone);
        assertTrue(locations.contains(location1));
        assertFalse(locations.contains(location2));
    }












}