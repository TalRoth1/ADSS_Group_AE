package DTO;

public class TruckDTO {
    private int id;
    private String licensePlate;
    private String type;
    private String status;
    private String location;
    private double weight;

    public TruckDTO(int id, String licensePlate, String type, String status, String location, double weight) {
        this.id = id;
        this.licensePlate = licensePlate;
        this.type = type;
        this.status = status;
        this.location = location;
        this.weight = weight;
    }

    public int getId() {
        return id;
    }

    public String getLicensePlate() {
        return licensePlate;
    }

    public String getType() {
        return type;
    }

    public String getStatus() {
        return status;
    }

    public String getLocation() {
        return location;
    }

    public double getWeight() {
        return weight;
    }

}
