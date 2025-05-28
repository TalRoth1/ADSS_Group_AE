package DataLayer.Mappers;

import DTO.LocationDTO;
import DomainLayer.LocationDL;

public class LocationMapper {
    public static LocationDTO toDTO(LocationDL location) {
        if (location == null) {
            return null;
        }
        return new LocationDTO(location.getStreet(), location.getStreetNumber(), location.getCity(),
                location.ContactNumber, location.ContactName, location.getZone());
    }

    public static LocationDL toDomain(LocationDTO locationDTO) {
        if (locationDTO == null) {
            return null;
        }
        return new LocationDL(locationDTO.getStreet(), locationDTO.getStreetNumber(), locationDTO.getCity(),
                locationDTO.getContactNumber(), locationDTO.getContactName(), locationDTO.getZone());
    }
}
