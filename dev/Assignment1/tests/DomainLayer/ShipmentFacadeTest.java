package DomainLayer;

import DataLayer.Mappers.ShipmentMapper;
import DataLayer.Mappers.TruckMapper;
import DataLayer.*;
import DTO.*;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.util.HashMap;
import java.util.Date;
import java.util.List;
import java.util.Map;
import static org.junit.jupiter.api.Assertions.*;


class ShipmentFacadeTest {
    private static ShipmentFacade facade;
    private static EmployeeFacade employeeFacade;

    @BeforeEach
    void clearFacade() {
        employeeFacade = new EmployeeFacade();
        facade = new ShipmentFacade(employeeFacade);
        facade.ClearDataBase();
        employeeFacade.ClearDataBase();
        facade.MakePredefinedData();
        employeeFacade.MakePredefinedData();
        facade.LoadData();
        employeeFacade.loadData();
    }

    @Test
    void testCreateShipment() throws Exception {
        TruckDL truck = facade.trucks.get(0);
        LocationDL origin = facade.locations.get(0);
        LocationDL destination = facade.locations.get(1);

        Map<LocationDL, Map<String, Integer>> items = Map.of(destination, Map.of("Milk", 1, "Cola", 2));
        facade.CreateShipment(truck, origin, List.of(destination), items, "Morning", new Date());
        assertTrue(facade.shipments.size() > 0);
    }

    @Test
    void testChangeStatus() throws Exception {
        TruckDL truck = facade.AddTruck(1, "TeslaX", "TypeA", 1000);
        LocationDL origin = facade.AddLocation("Main St", 1, "Neverland", "039000000000", "John Doe", "Zone1");
        LocationDL destination = facade.AddLocation("Second St", 2, "Neverland", "123", "Jane Doe", "Zone2");
        facade.AddItem("egg carton", 1.0f);
        facade.AddItem("milk", 2.0f);
        facade.AddItem("bread", 0.5f);
        Map<LocationDL, Map<String, Integer>> items = Map.of(destination, Map.of("egg carton", 1, "milk", 2));
        // ShipmentDL shipment = new ShipmentDL(truck, driver, origin,
        // List.of(destination), items);
        // facade.shipments.add(shipment);
        facade.CreateShipment(truck, origin, List.of(destination), items, "Morning", new Date());
        ShipmentDL shipment = facade.shipments.get(0);
        shipment.ChangeStatus("SENT");
        assertEquals("SENT", shipment.Status.toString());
    }

    //@Test
    // test for editing a field
//    void editShipmentTest1() throws Exception {
//        TruckDL truck = facade.AddTruck(1, "TeslaX", "TypeA", 1000);
//        LocationDL origin = facade.AddLocation("Main St", 1, "Neverland", "039000000000", "John Doe", "Zone1");
//        LocationDL destination = facade.AddLocation("Second St", 2, "Neverland", "123", "Jane Doe", "Zone2");
//        facade.AddItem("egg carton", 1.0f);
//        facade.AddItem("milk", 2.0f);
//        facade.AddItem("bread", 0.5f);
//        Map<LocationDL, Map<String, Integer>> items = Map.of(destination, Map.of("egg carton", 1, "milk", 2));
//        // ShipmentDL shipment = new ShipmentDL(truck, driver, origin,
//        // List.of(destination), items);
//        // facade.shipments.add(shipment);
//        facade.CreateShipment(truck, origin, List.of(destination), items, "Morning", new Date());
//        ShipmentDL shipment = facade.shipments.getFirst();
//        facade.EditShipement(shipment, null, driver2, null, null, null);
//        assertEquals(driver2, shipment.DriverName);
//    }

//    @Test
//    // test for trying to change the driver to one that does not have the right
//    // license
//    // should fail
//    void editShipmentTest2() throws Exception {
//        TruckDL truck = facade.AddTruck(1, "TeslaX", "TypeA", 1000);
//        DriverDL driver = facade.AddDriver("Joe Mama", List.of("TypeA"));
//        LocationDL origin = facade.AddLocation("Main St", 1, "Neverland", "039000000000", "John Doe", "Zone1");
//        LocationDL destination = facade.AddLocation("Second St", 2, "Neverland", "123", "Jane Doe", "Zone2");
//        facade.AddItem("egg carton", 1.0f);
//        facade.AddItem("milk", 2.0f);
//        facade.AddItem("bread", 0.5f);
//        Map<LocationDL, Map<String, Integer>> items = Map.of(destination, Map.of("egg carton", 1, "milk", 2));
//        // ShipmentDL shipment = new ShipmentDL(truck, driver, origin,
//        // List.of(destination), items);
//        // facade.shipments.add(shipment);
//        facade.CreateShipment(truck, driver, origin, List.of(destination), items);
//        ShipmentDL shipment = facade.shipments.getFirst();
//        DriverDL driver2 = facade.AddDriver("Bob A", List.of("TypeB"));
//        Exception exception = assertThrows(Exception.class, () -> {
//            facade.EditShipement(shipment, null, driver2, null, null, null);
//        });
//        String expectedMessage = "Driver does not have the right license for this truck";
//        String actualMessage = exception.getMessage();
//        assertTrue(actualMessage.contains(expectedMessage));
//    }

    @Test
    // test for overweight truck
    void testCreateShipment2() throws Exception {
        TruckDL truck = facade.AddTruck(1, "TeslaX", "TypeA", 1);
        LocationDL origin = facade.AddLocation("Main St", 1, "Neverland", "039000000000", "John Doe", "Zone1");
        LocationDL destination = facade.AddLocation("Second St", 2, "Neverland", "123", "Jane Doe", "Zone2");
        facade.AddItem("egg carton", 1.0f);
        facade.AddItem("milk", 2.0f);
        facade.AddItem("bread", 0.5f);
        Map<LocationDL, Map<String, Integer>> items = Map.of(destination, Map.of("egg carton", 1, "milk", 2));
        Exception exception = assertThrows(Exception.class, () -> {
            facade.CreateShipment(truck, origin, List.of(destination), items, "Morning", new Date());
        });
        String expectedMessage = "Truck is overweight";
        String actualMessage = exception.getMessage();
        assertTrue(actualMessage.contains(expectedMessage));
    }

    @Test
    // test for adding items to shipment
    void testAddItems() throws Exception {
        TruckDL truck = facade.AddTruck(1, "TeslaX", "TypeA", 1000);
        LocationDL origin = facade.AddLocation("Main St", 1, "Neverland", "039000000000", "John Doe", "Zone1");
        LocationDL destination = facade.AddLocation("Second St", 2, "Neverland", "123", "Jane Doe", "Zone2");
        facade.AddItem("egg carton", 1.0f);
        facade.AddItem("milk", 2.0f);
        facade.AddItem("bread", 0.5f);
        Map<LocationDL, Map<String, Integer>> items = new HashMap<>(
                Map.of(destination, new HashMap<>(Map.of("egg carton", 1, "milk", 2))));
        // ShipmentDL shipment = new ShipmentDL(truck, driver, origin,
        // List.of(destination), items);
        // facade.shipments.add(shipment);
        facade.CreateShipment(truck, origin, List.of(destination), items, "Morning", new Date());
        ShipmentDL shipment = facade.shipments.get(0);
        Map<LocationDL, Map<String, Integer>> newItems = new HashMap<>(
                Map.of(destination, new HashMap<>(Map.of("bread", 1))));
        facade.EditShipement(shipment, null, null, null, null, newItems, null,null);
        assertTrue(shipment.getDocument().getItemsMap().get(destination).containsKey("bread"));
    }

    @Test
    // test for removing items to shipment
    void testRemoveItems() throws Exception {
        TruckDL truck = facade.AddTruck(1, "TeslaX", "TypeA", 1000);
        LocationDL origin = facade.AddLocation("Main St", 1, "Neverland", "039000000000", "John Doe", "Zone1");
        LocationDL destination = facade.AddLocation("Second St", 2, "Neverland", "123", "Jane Doe", "Zone2");
        facade.AddItem("egg carton", 1.0f);
        facade.AddItem("milk", 2.0f);
        facade.AddItem("bread", 0.5f);
        Map<LocationDL, Map<String, Integer>> items = new HashMap<>(
                Map.of(destination, new HashMap<>(Map.of("egg carton", 1, "milk", 2))));
        // ShipmentDL shipment = new ShipmentDL(truck, driver, origin,
        // List.of(destination), items);
        // facade.shipments.add(shipment);
        facade.CreateShipment(truck, origin, List.of(destination), items, "Morning", new Date());
        ShipmentDL shipment = facade.shipments.get(0);
        Map<LocationDL, Map<String, Integer>> newItems = new HashMap<>(
                Map.of(destination, new HashMap<>(Map.of("egg carton", 0))));
        facade.EditShipement(shipment, null, null, null, null, newItems,null,null);
        assertFalse(shipment.getDocument().getItemsMap().get(destination).containsKey("egg carton"));
    }

    @Test
    // test for adding too many items to truck
    // should fail
    void testAddItemsFail() throws Exception {
        TruckDL truck = facade.AddTruck(1, "TeslaX", "TypeA", 10);
        LocationDL origin = facade.AddLocation("Main St", 1, "Neverland", "039000000000", "John Doe", "Zone1");
        LocationDL destination = facade.AddLocation("Second St", 2, "Neverland", "123", "Jane Doe", "Zone2");
        facade.AddItem("egg carton", 1.0f);
        facade.AddItem("milk", 2.0f);
        facade.AddItem("bread", 0.5f);
        Map<LocationDL, Map<String, Integer>> items = new HashMap<>(
                Map.of(destination, new HashMap<>(Map.of("egg carton", 1, "milk", 2))));
        // ShipmentDL shipment = new ShipmentDL(truck, driver, origin,
        // List.of(destination), items);
        // facade.shipments.add(shipment);
        facade.CreateShipment(truck, origin, List.of(destination), items, "Morning", new Date());
        ShipmentDL shipment = facade.shipments.get(0);
        Map<LocationDL, Map<String, Integer>> newItems = new HashMap<>(
                Map.of(destination, new HashMap<>(Map.of("bread", 100))));
        Exception exception = assertThrows(Exception.class, () -> {
            facade.EditShipement(shipment, null, null, null, null, newItems, null,null);
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



    //----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
    //----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
    //----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
    //----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
    //----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
    //----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------

    // sending a truck to the database and then retrieving it by id
    @Test
    void testGetTruckById() throws Exception {
        TruckDL truck = facade.AddTruck(1, "TeslaX", "TypeA", 1000);
        TruckMapper truckMapper = new TruckMapper();

        TruckDL foundTruck = truckMapper.toDomain(facade.truckController.getTruck(truck.getId()));
        assertEquals(truck, foundTruck);
    }



    @Test
    void testGetItemByName() {
        try {
            String itemName = "testItem";
            float weight = 1.0f;
            facade.AddItem(itemName, weight);

            ItemDTO foundItem = facade.itemsController.getItemByName(itemName);
            assertNotNull(foundItem);
            assertEquals(itemName, foundItem.getName());
            assertEquals(weight, foundItem.getWeight());
        }
        catch (Exception e){
            fail("Exception should no be thrown");
        }

    }


    // sending a shipment to te database and then retrieving it by id
    @Test
    void testGetShipmentById() throws Exception {
        TruckDL truck = facade.AddTruck(1, "TeslaX", "TypeA", 1000);
        LocationDL origin = facade.AddLocation("Main St", 1, "Neverland", "039000000000", "John Doe", "Zone1");
        LocationDL destination = facade.AddLocation("Second St", 2, "Neverland", "123", "Jane Doe", "Zone2");
        facade.AddItem("egg carton", 1.0f);
        facade.AddItem("milk", 2.0f);
        facade.AddItem("bread", 0.5f);
        Map<LocationDL, Map<String, Integer>> items = Map.of(destination, Map.of("egg carton", 1, "milk", 2));
        facade.CreateShipment(truck, origin, List.of(destination), items, "MORNNING", new Date());
        ShipmentDL shipment = facade.shipments.get(0);
        ShipmentMapper shipmentMapper = new ShipmentMapper();
        ShipmentDL foundShipment = shipmentMapper.toDL(facade.shipmentController.getShipment(shipment.getId()));
        assertEquals(shipment, foundShipment);
    }


    @Test
    void testDriverNull() {
        TruckDL truck = facade.AddTruck(1, "TeslaX", "TypeA", 1000);
        DriverDL driver = null;
        LocationDL origin = facade.AddLocation("Main St", 1, "Neverland", "039000000000", "John Doe", "Zone1");
        LocationDL destination = facade.AddLocation("Second St", 2, "Neverland", "123", "Jane Doe", "Zone2");
        facade.AddItem("egg carton", 1.0f);
        facade.AddItem("milk", 2.0f);
        facade.AddItem("bread", 0.5f);
        Map<LocationDL, Map<String, Integer>> items = Map.of(destination, Map.of("egg carton", 1, "milk", 2));
        try{
        facade.CreateShipment(truck, origin, List.of(destination), items, "MORNING" , new Date());
        }
        catch(Exception e)
        {
            fail("exception");
        }
        DriverDL foundDriver = facade.shipments.get(0).getDriver();
        assertNull(foundDriver, "Driver should be null when not provided");
    }

    @Test
    void unSendableShipment() throws Exception {
        TruckDL truck = facade.AddTruck(1, "TeslaX", "TypeA", 1000);
        LocationDL origin = facade.AddLocation("Main St", 1, "Neverland", "039000000000", "John Doe", "Zone1");
        LocationDL destination = facade.AddLocation("Second St", 2, "Neverland", "123", "Jane Doe", "Zone2");
        facade.AddItem("egg carton", 1.0f);
        facade.AddItem("milk", 2.0f);
        facade.AddItem("bread", 0.5f);
        Map<LocationDL, Map<String, Integer>> items = Map.of(destination, Map.of("egg carton", 1, "milk", 2));
        facade.CreateShipment(truck, origin, List.of(destination), items, "MORNING" , new Date());
        ShipmentDL shipment = facade.shipments.get(0);
        facade.ChangeStatus(shipment, "SENT");
        assertEquals("PENDING", shipment.Status.toString());
    }

}