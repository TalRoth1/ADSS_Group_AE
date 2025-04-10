package DomainLayer;
import java.util.Date;
import java.util.List;

public class ShipmentDL {
    public Date Date; // includes the hour and time zone
    public Date Start;
    public int TruckNumber;
    public String DriverName;
    public LocationDL Origin;
    public List<LocationDL> Destinations;
    public ShipmentDocumentDL Document;

    public Boolean DriverCheck() {
        return true;
    }

    public static ShipmentDL CreateShipment() {
        // Constructor logic here
        return new ShipmentDL();
    }
}
