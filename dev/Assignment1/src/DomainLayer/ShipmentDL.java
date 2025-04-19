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
        String truckType = truck.GetType();
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
    
    public Boolean WeightCheck(Map<String, Float> itemWeights)
    {
        //assuming all items in Document exists in itemWeight
        float sum =0;
        Map<LocationDL, Map<String, Integer>> curr = Document.getItemsMap();
        for (LocationDL loc : curr.keySet())
        {
            Map<String , Integer> locList = curr.get(loc);
            for(String itemToCheck : locList.keySet())
            {
                int itemAmount = locList.get(itemToCheck);
                sum += itemAmount * itemWeights.get(itemToCheck);
            }
        }
        if(sum > Truck.GetMaxWeight())
        {
            return false;
        }
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

    public Boolean EditDestinations(Map<LocationDL, Map<String, Integer>> items, Map<String,Float> itemWeights)
    {
        ShipmentDocumentDL temp = getDocument();
        Document.Edit(items);
        if(!WeightCheck(itemWeights))
        {
            Document = temp;
            return false;
        }
        UpdateDestinationsFromDoc();
        return true;
    }

    public ShipmentDocumentDL getDocument()
    {
        return Document;
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



    public boolean EditTruck(Integer number , List<TruckDL> trucks, Map<String, Float> itemWeights)
    {
        for(TruckDL truck : trucks)
        {
            if (truck.Number == number)
            {
                TruckDL prev = this.Truck;
                setTruck(truck);
                if(WeightCheck(itemWeights) && DriverCheck(DriverName))
                {
                    return true; 
                }
                setTruck(prev);
            }
        }
        return false;
    }
}


