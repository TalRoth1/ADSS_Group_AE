package DTO;

import java.util.Map;

public class DocumentDTO {
    int id;
    LocationDTO origin;
    Map<LocationDTO, Map<String, Integer>> items;
    float Weight;

    public DocumentDTO(int id, LocationDTO origin, Map<LocationDTO, Map<String, Integer>> items, float weight) {
        this.id = id;
        this.origin = origin;
        this.items = items;
        this.Weight = weight;
    }

    public int getId() {
        return id;
    }

    public LocationDTO getOrigin() {
        return origin;
    }

    public Map<LocationDTO, Map<String, Integer>> getItems() {
        return items;
    }

    public float getWeight() {
        return Weight;
    }

    



}
