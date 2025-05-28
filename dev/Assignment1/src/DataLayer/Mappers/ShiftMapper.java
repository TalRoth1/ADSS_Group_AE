package DataLayer.Mappers;

import DTO.ShiftDTO;
import DomainLayer.Shift;

import java.time.LocalDate;

public class ShiftMapper {

    public static ShiftDTO toDTO(Shift shift) {
        if (shift == null) {
            return null;
        }
        return new ShiftDTO(
                shift.getDate().toString(),
                shift.getShiftType().toString(),
                shift.getStartTime(),
                shift.getEndTime(),
                shift.getShiftManagerId(),
                shift.getNumOfRequiredcashiers(),
                shift.getNumOfRequireddrivers(),
                shift.getNumOfRequiredstorekeepers(),
                shift.getNumOfRequiredshipmentManagers(),
                shift.isShipmentShift());
    }

    public static Shift toDomain(ShiftDTO dto) {
        if (dto == null) {
            return null;
        }
        return new Shift(
                dto.getDate().toString(),
                dto.getShiftType().toString(),
                dto.getStartTime(),
                dto.getEndTime(),
                dto.getShiftManagerId(),
                dto.getNumOfRequiredcashiers(),
                dto.getNumOfRequireddrivers(),
                dto.getNumOfRequiredstoreKeepers(),
                dto.getNumOfRequiredshipmentManagers(),
                dto.isShipmentShift());

    }
}