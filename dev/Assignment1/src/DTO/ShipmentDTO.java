package DTO;

import java.util.List;
import java.util.Map;

public class ShipmentDTO {
    private int id;
    private TruckDTO truck;
    private DriverDTO driver;
    private LocationDTO origin; // should go to document as well ya goof. goofball
    private List<LocationDTO> destination; // should go to document as well ya goof. goofball
    private String status;
    // private Map<String, Integer> items; go to document silly
    private DocumentDTO document;

    public ShipmentDTO(int id, TruckDTO truck, DriverDTO driver, LocationDTO origin, List<LocationDTO> destination,
            String status, DocumentDTO document) {
        this.id = id;
        this.truck = truck;
        this.driver = driver;
        this.origin = origin;
        this.destination = destination;
        this.status = status;
        this.document = document;
        // this.items = null;
    }

    public int getId() {
        return id;
    }

    public TruckDTO getTruck() {
        return truck;
    }

    public DriverDTO getDriver() {
        return driver;
    }

    public LocationDTO getOrigin() {
        return origin;
    }

    public List<LocationDTO> getDestination() {
        return destination;
    }

    public String getStatus() {
        return status;
    }

    public DocumentDTO getDocument() {
        return document;
    }

}
