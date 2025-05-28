package DataLayer.Mappers;

import DTO.TruckDTO;
import DomainLayer.TruckDL;

public class TruckMapper {

    public static TruckDTO toDTO(TruckDL truck) {
        if (truck == null) {
            return null;
        }
        return new TruckDTO(truck.getId(), truck.getLicensePlate(), truck.getType(),
                truck.getWeight(), truck.getMaxWeight(), truck.getStatus());
    }

    public static TruckDL toDomain(TruckDTO truckDTO) {
        if (truckDTO == null) {
            return null;
        }
        return new TruckDL(truckDTO.getId(), truckDTO.getLicensePlate(), truckDTO.getType(),
                truckDTO.getWeight(), truckDTO.getMaxWeight(), truckDTO.getStatus());
    }
}
