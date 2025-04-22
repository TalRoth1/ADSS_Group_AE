package DomainLayer;
import java.util.List;
import java.util.Map;
import java.util.ArrayList;

public class ShipmentFacade {
    public List<ShipmentDL> shipments = new ArrayList<>();
    public List<LocationDL> locations = new ArrayList<>();
    public List<DriverDL> drivers = new ArrayList<>();
    public List<TruckDL> trucks = new ArrayList<>();
    public Map<String, Float> Items = Map.of("egg carton", 1.5f, "milk", 1f, "bread", 0.5f, "cheese", 1f, "butter", 0.25f, "yogurt", 0.6f, "juice", 0.75f, "soda", 0.75f, "water", 1f, "coffee", 0.5f);

    public void CreateShipment(TruckDL truck, DriverDL driver, LocationDL origin, List<LocationDL> destinations, Map<LocationDL, Map<String,Integer>> items) throws Exception {
        ShipmentDL shipment = new ShipmentDL(truck, driver, origin, destinations, items);
        if(!shipment.DriverCheck(driver))
        {
            throw new Exception("Driver does not have the right license for this truck");
        }
        if (!shipment.WeightCheck(Items)) {
            throw new Exception("Truck is overweight");
        }
        shipments.add(shipment);
    }
    public void ChangeStatus(ShipmentDL shipment, String stat)  {
        shipment.ChangeStatus(stat);
    }

    public List<ShipmentDL> GetStatusShipement(String status) {
        List<ShipmentDL> ans = new ArrayList<>();
        for (ShipmentDL shipment : shipments) {
            if (shipment.Status.toString().toLowerCase().equals(status)) {
                ans.add(shipment);
            }
        }
        return ans;
    }

    public void RemoveShipment(ShipmentDL shipment)
    {
        shipments.remove(shipment);
    }

    public LocationDL AddLocation(String street, int streetNumber, String city, String contactNumber, String contactName, String zone) {
        LocationDL location = new LocationDL(street, streetNumber, city, contactNumber, contactName, zone);
        locations.add(location);
        return location;
    }

    public DriverDL AddDriver(String name, List<String> licenseType) {
        DriverDL driver = new DriverDL(name, licenseType);
        drivers.add(driver);
        return driver;
    }

    public TruckDL AddTruck(int number, String model, String type, float maxWeight) {
        TruckDL truck = new TruckDL(number, model, type, 0, maxWeight);
        trucks.add(truck);
        return truck;
    }

    public void AddItem(String itemName, float weight) {
        Items.put(itemName, weight);
    }

    public List<String> GetItems() {
        List<String> itemList = new ArrayList<>();
        for (String item : Items.keySet()) {
            itemList.add(item);
        }
        return itemList;
    }

    public List<LocationDL> LocationByZone(String zone)
    {
        List<LocationDL> ans = new ArrayList<>();
        for(LocationDL loc : locations)
        {
            if(loc.getZone().equals(zone))
                ans.add(loc);
        }
        return ans;
    }

    public void EditShipement(ShipmentDL shipment, TruckDL truck, DriverDL driver, LocationDL origin, List<LocationDL> destinations, Map<LocationDL, Map<String,Integer>> items) throws Exception {
        //assuming that the shipment exists in the list
        int index = shipments.indexOf(shipment);
        ShipmentDL shipmentToEdit = shipments.get(index);
        if(truck != null) {
            if(shipmentToEdit.EditTruck(truck.GetNumber(), trucks, Items)) {
                throw new Exception("Truck is overweight or the driver does not have the right license for this truck");
            }
        }
        if(driver != null) {
            Boolean check = shipmentToEdit.EditDriver(driver);
            if(!check) {
                throw new Exception("Driver does not have the right license for this truck");
            }
        }
        if(origin != null) {
            shipmentToEdit.EditOrigin(origin, locations);
        }
        if(destinations != null) {
            Boolean check = shipmentToEdit.EditDestinations(items, Items);
            if(!check)
            {
                throw new Exception("Truck cannot support the new weight");
            }
        }
        if(items != null) {
            Boolean check = shipmentToEdit.EditDestinations(items, Items);
            if(!check)
            {
                throw new Exception("Truck cannot support the new weight");
            }
        }
    }

    public String GetDocumentString(ShipmentDL shipment)
    {
        return shipment.getDocument().toString() + "\n" + "Status: " + shipment.getStatus().toString();
    }
}