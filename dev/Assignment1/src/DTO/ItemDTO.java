package DTO;

public class ItemDTO {
    private String name;
    private float weight;

    public ItemDTO(String name, float weight) {
        this.name = name;
        this.weight = weight;
    }

    public String getName() {
        return name;
    }

    public float getWeight() {
        return weight;
    }

}
