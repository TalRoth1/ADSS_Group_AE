package DTO;

public class TruckDTO {
    private int id;
    private String licensePlate;
    private String type;
    private Boolean status;
    private float weight;
    private float maxWeight;

    public TruckDTO(int id, String licensePlate, String type, float weight, float maxWeight, boolean status) {
        this.id = id;
        this.licensePlate = licensePlate;
        this.type = type;
        this.weight = weight;
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

    public float getWeight() {
        return weight;
    }

    public float getMaxWeight() {
        return maxWeight;
    }

}
