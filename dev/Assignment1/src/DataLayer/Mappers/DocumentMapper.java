package DataLayer.Mappers;

import java.util.HashMap;
import java.util.Map;

import DTO.DocumentDTO;
import DTO.LocationDTO;
import DomainLayer.LocationDL;
import DomainLayer.ShipmentDocumentDL;

public class DocumentMapper {
    public static DocumentDTO toDTO(ShipmentDocumentDL dl) {
        LocationDTO originDTO = LocationMapper.toDTO(dl.getOrigin());

        // Map<LocationDTO, Map<String, Integer>>
        Map<LocationDTO, Map<String, Integer>> dtoItems = new HashMap<>();
        for (LocationDL loc : dl.getItemsMap().keySet()) {
            LocationDTO locDTO = LocationMapper.toDTO(loc);
            dtoItems.put(locDTO, new HashMap<>(dl.getItemsMap().get(loc)));
        }

        return new DocumentDTO(0, originDTO, dtoItems, dl.Weight); // 0 = ID not stored in DL
    }

    public static ShipmentDocumentDL toDL(DocumentDTO dto) {
        LocationDL originDL = LocationMapper.toDomain(dto.getOrigin());

        // Map<LocationDL, Map<String, Integer>>
        Map<LocationDL, Map<String, Integer>> dlItems = new HashMap<>();
        for (LocationDTO locDTO : dto.getItems().keySet()) {
            LocationDL locDL = LocationMapper.toDomain(locDTO);
            dlItems.put(locDL, new HashMap<>(dto.getItems().get(locDTO)));
        }

        ShipmentDocumentDL dl = new ShipmentDocumentDL(dlItems, originDL);
        dl.Weight = dto.getWeight();
        return dl;
    }
    
}
