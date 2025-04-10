package DomainLayer;
import java.util.List;

public class ShipmentFacade {
    public List<ShipmentDL> shipments;
    
    public void CreateShipment() {
        ShipmentDL shipment = ShipmentDL.CreateShipment();
        shipments.add(shipment);
    } 
}
