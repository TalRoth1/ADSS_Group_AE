package DTO;

public class TruckDTO {
    private int id;
    private String licensePlate;
    private String type;
    private Boolean status;
    private float maxWeight;

    public TruckDTO(int id, String licensePlate, String type, float maxWeight, boolean status) {
        this.id = id;
        this.licensePlate = licensePlate;
        this.type = type;
        this.maxWeight = maxWeight;
        this.status = status;
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

    public Boolean getStatus() {
        return status;
    }

    public float getMaxWeight() {
        return maxWeight;
    }

}
