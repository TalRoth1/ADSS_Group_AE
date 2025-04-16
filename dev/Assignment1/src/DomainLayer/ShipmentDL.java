package DomainLayer;
import java.util.Date;
import java.util.List;
import java.util.Map;

public class ShipmentDL {
    public Date Date; // includes the hour and time zone
    public TruckDL TruckNumber;
    public DriverDL DriverName;
    public LocationDL Origin;
    public List<LocationDL> Destinations;
    public ShipmentDocumentDL Document;

    public Boolean DriverCheck() {
        return true;
    }

    private void SetDestinations(List<LocationDL> dest)
    {
        this.Destinations = dest;
    }

    public ShipmentDL(TruckDL truck, DriverDL driver, LocationDL origin, List<LocationDL> destinations, Map<LocationDL, Map<String,Integer>> items) {
        this.TruckNumber = truck;
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
        
        
    }

    private void UpdateDestinationsFromDoc()
    {
        SetDestinations(Document.getLocations());
    }

    private void UpdateDestinationsFromList()
    {
        
    }
}
