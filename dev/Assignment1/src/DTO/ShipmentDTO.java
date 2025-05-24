package DTO;

public class ShipmentDTO {
    private int id;
    private String truck;
    private String driver;
    private String origin;
    private String destination;
    private String status;
    private String items;

    public ShipmentDTO(int id, String truck, String driver, String origin, String destination, String status, String items) {
        this.id = id;
        this.truck = truck;
        this.driver = driver;
        this.origin = origin;
        this.destination = destination;
        this.status = status;
        this.items = items;
    }

    public int getId() {
        return id;
    }

    public String getTruck() {
        return truck;
    }

    public String getDriver() {
        return driver;
    }

    public String getOrigin() {
        return origin;
    }

    public String getDestination() {
        return destination;
    }

    public String getStatus() {
        return status;
    }

    public String getItems() {
        return items;
    }

}
    