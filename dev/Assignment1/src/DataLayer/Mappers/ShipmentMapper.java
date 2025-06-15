package DataLayer.Mappers;

import DTO.ShipmentDTO;
import DomainLayer.DriverDL;
import DomainLayer.ShipmentDL;
import DomainLayer.ShipmentDocumentDL;
import DomainLayer.TruckDL;

public class ShipmentMapper {

    //add status to the ShipmentDL
    public static ShipmentDL toDL(ShipmentDTO dto) {
        TruckDL truckDL = TruckMapper.toDomain(dto.getTruck());
        DriverDL driverDL = DriverMapper.toDL(dto.getDriver()); //need to fix this, should get the branch from the dto
        ShipmentDocumentDL documentDL = DocumentMapper.toDL(dto.getDocument());

        ShipmentDL dl = new ShipmentDL(
                dto.getId(),
                truckDL,
                documentDL.getOrigin(),
                documentDL.getLocations(),
                documentDL.getItemsMap(),
                dto.getShiftType(),
                dto.getDateSent(),
                dto.getDateCreated()
        );

        dl.setDriver(driverDL);
        dl.ChangeStatus(dto.getStatus());
        
        return dl;
    }

    public static ShipmentDTO toDTO(ShipmentDL dl) {
        return new ShipmentDTO(
                dl.getId(),
                dl.getDateCreated(),
                dl.getDateSent(),
                TruckMapper.toDTO(dl.getTruck()),
                DriverMapper.toDTO(dl.getDriver()),
                dl.getStatus().name(),
                DocumentMapper.toDTO(dl.getDocument()),
                dl.getShiftType()
        );
    }

}
