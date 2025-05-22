package DomainLayer;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

public class ShipmentDL {
    public Date DateCreated; // includes the hour and time zone
    public Date DateSent;
    public TruckDL Truck;
    public DriverDL DriverName;
    public List<LocationDL> Destinations;
    public ShipmentDocumentDL Document;
    public ShipmentStatus Status = ShipmentStatus.PENDING;


    public Boolean DriverCheck(DriverDL driver) {
        TruckDL truck = this.Truck;
        String truckType = truck.GetType();
        for(String type : driver.LicenseType)
        {
            if(type.equals(truckType))
            {
                return true;
            }
        }
        return false;
    }

    public Boolean EditDriver(DriverDL driver) {
        if (!DriverCheck(driver)) {
            return false;
        }
        this.DriverName = driver;
        return true;
    }

    public void ChangeStatus(String stat) {
        switch (stat) {
            case "PENDING":
                this.Status = ShipmentStatus.PENDING;
                break;
            case "SENT":
                this.Status = ShipmentStatus.SENT;
                this.DateSent = new Date();
                break;
            case "PROBLEM":
                this.Status = ShipmentStatus.PROBLEM;
                break;
            case "CANCELLED":
                this.Status = ShipmentStatus.CANCELLED;
                break;
                case "COMPLETED":
                this.Status = ShipmentStatus.COMPLETED;
                break;
            default:
                throw new IllegalArgumentException("Invalid status: " + stat);
        }
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

    public Boolean EditOrigin(LocationDL origin, List<LocationDL> locations) {
        return Document.EditOrigin(origin, Document.getLocations());
    }

    private void SetDestinations(List<LocationDL> dest)
    {
        this.Destinations = dest;
    }

    private void setTruck(TruckDL newTruck)
    {
        this.Truck = newTruck;
    }

    public void setWeight(Map<String, Float> Items)
    {
        this.Document.setWeight(Items);
    }

    public ShipmentDL(TruckDL truck, DriverDL driver, LocationDL origin, List<LocationDL> destinations, Map<LocationDL, Map<String,Integer>> items) {
        this.Truck = truck;
        this.DriverName = driver;
        this.Destinations = destinations;
        this.Document = new ShipmentDocumentDL(items, origin);
        this.DateCreated = new Date();
        this.DateSent = null;
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

    public ShipmentStatus getStatus()
    {
        return Status;
    }

    public String toString()
    {
        return "Truck: " + Truck.GetNumber() +
                ", Driver: " + DriverName.getName() +
                ", Origin: " + Document.getOrigin().toString() +
                ", Destinations: " + Document.getLocations().toString() +
                ", Date Created: " + DateCreated.toString() +
                (DateSent != null ? ", Date Sent: " + DateSent.toString() : "");
    }

    public void ChangeAvailablity()
    {
        Truck.changeState();
        DriverName.changeState();
    }
    
    public boolean DriverBusyCheck()
    {
        return DriverName.isBusy;
    }

    public boolean TruckBusyCheck()
    {
        return Truck.IsBusy;
    }

    public int getTruckNumber()
    {
        return Truck.GetNumber();
    }

    public int getDriverId()
    {
        return DriverName.getId();
    }
}


