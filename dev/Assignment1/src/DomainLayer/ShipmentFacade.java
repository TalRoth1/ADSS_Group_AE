package DomainLayer;
import java.util.List;
import java.util.Map;

public class ShipmentFacade {
    public List<ShipmentDL> shipments;
    public List<LocationDL> locations;
    public List<DriverDL> drivers;
    public List<TruckDL> trucks;
    
    public void CreateShipment(TruckDL truck, DriverDL driver, LocationDL origin, List<LocationDL> destinations, Map<LocationDL, Map<String,Integer>> items) {
        ShipmentDL shipment = new ShipmentDL(truck, driver, origin, destinations, items);
        shipments.add(shipment);
    }

    public void RemoveShipment(ShipmentDL shipment)
    {
        shipments.remove(shipment);
    }

    public LocationDL AddLocation(String street, int streetNumber, String city, int contactNumber, String contactName, String zone) {
        LocationDL location = new LocationDL(street, streetNumber, city, contactNumber, contactName, zone);
        locations.add(location);
        return location;
    }

    public DriverDL AddDriver(String name, String licenseType) {
        DriverDL driver = new DriverDL(name, licenseType);
        drivers.add(driver);
        return driver;
    }

    public TruckDL AddTruck(int number, String model, String type, float maxWeight) {
        TruckDL truck = new TruckDL(number, model, type, 0, maxWeight);
        trucks.add(truck);
        return truck;
    }

    public void EditShipement(ShipmentDL shipment, TruckDL truck, DriverDL driver, LocationDL origin, List<LocationDL> destinations, Map<LocationDL, Map<String,Integer>> items) {
        //assuming that the shipment exists in the list
        int index = shipments.indexOf(shipment);
        ShipmentDL shipmentToEdit = shipments.get(index);
        if(truck != null) {
            shipmentToEdit.EditTruck(truck.GetNumber(), trucks);
        }
        if(driver != null) {
            Boolean check = shipmentToEdit.EditDriver(driver);
            if(!check) {
                System.out.println("Driver does not have the right license for this truck");
            }
        }
        if(origin != null) {
            shipmentToEdit.EditOrigin(origin);
        }
        if(destinations != null) {
            shipmentToEdit.EditDestinations(items);
        }
        if(items != null) {
            shipmentToEdit.EditDestinations(items);
        }
    }
}
