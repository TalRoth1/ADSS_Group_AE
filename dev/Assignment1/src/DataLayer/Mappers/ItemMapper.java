package DataLayer.Mappers;

import DTO.ItemDTO;

public class ItemMapper {

    // This class is responsible for mapping between Item objects and database
    // records.
    // It will contain methods to convert Item objects to database records and vice
    // versa.

    // Example method to map an Item object to a database record
    public ItemDTO mapToDTO(String itemName, float itemWeight) {
        if (itemName == null || itemWeight <= 0) {
            throw new IllegalArgumentException("Invalid item data provided.");
        }
        return new ItemDTO(itemName, itemWeight);
    }

}
