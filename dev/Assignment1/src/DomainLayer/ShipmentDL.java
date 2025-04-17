package DomainLayer;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

public class ShipmentDL {
    public Date Date; // includes the hour and time zone
    public TruckDL Truck;
    public DriverDL DriverName;
    public LocationDL Origin;
    public List<LocationDL> Destinations;
    public ShipmentDocumentDL Document;

    public Boolean DriverCheck(DriverDL driver) {
        TruckDL truck = this.Truck;
        String truckType = Truck.GetType();
        String driverLicense = driver.LicenseType;
        return truckType.equals(driverLicense);
    }

    public Boolean EditDriver(DriverDL driver) {
        if (!DriverCheck(driver)) {
            return false;
        }
        this.DriverName = driver;
        return true;
    }

    public Boolean EditOrigin(LocationDL origin) {
        this.Origin = origin;
        return true;
    }

    private void SetDestinations(List<LocationDL> dest)
    {
        this.Destinations = dest;
    }

    private void setTruck(TruckDL newTruck)
    {
        this.Truck = newTruck;
    }

    public ShipmentDL(TruckDL truck, DriverDL driver, LocationDL origin, List<LocationDL> destinations, Map<LocationDL, Map<String,Integer>> items) {
        this.Truck = truck;
        this.DriverName = driver;
        this.Origin = origin;
        this.Destinations = destinations;
        this.Document = new ShipmentDocumentDL(items);
        this.Date = new Date();
    }

    public void EditDestinations(Map<LocationDL, Map<String, Integer>> items)
    {
        Document.Edit(items);
        UpdateDestinationsFromDoc();
    }

    public void AddDestinations(Map<LocationDL, Map<String, Integer>> items)
    {
        Document.Edit(items);
        UpdateDestinationsFromDoc();
    }

    public void RemoveDestinations(List<LocationDL> destinations)
    {
        Document.Remove(destinations);
        List<LocationDL> curr = new ArrayList<>(this.Destinations);
        curr.removeAll(destinations);
        SetDestinations(curr);
        
    }

    private void UpdateDestinationsFromDoc()
    {
        SetDestinations(Document.getLocations());
    }

    public boolean EditTruck(Integer number , List<TruckDL> trucks)
    {
        for(TruckDL truck : trucks)
        {
            if (truck.Number == number)
            {
                setTruck(truck);
                return true;   
            }
        }
        return false;
    }
}


