package DataLayer.Mappers;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import DTO.LocationDTO;
import DTO.ShiftDTO;
import DomainLayer.LocationDL;
import DomainLayer.Role;
import DomainLayer.Shift;
import DomainLayer.ShiftType;

public class ShiftMapper {

    public static ShiftDTO toDTO(Shift shift) {
        if (shift == null) {
            return null;
        }
        LocationDTO branchDTO = LocationMapper.toDTO(shift.getBranch());
        return new ShiftDTO(
                shift.getId(),
                Date.from(shift.getDate().atStartOfDay(ZoneId.systemDefault()).toInstant()),
                shift.getShiftType().name(),
                shift.getStartTime(),
                shift.getEndTime(),
                shift.getShiftManagerId(),
                shift.isShipmentShift(),
                branchDTO,
                new HashMap<>(shift.getRequiredRoles())
        );
    }

    public static Shift toDomain(ShiftDTO dto) {
        //ShiftReqRolesDTO requiredRoles = dto.; // This should be set separately if needed
        if (dto == null) {
            return null;
        }
        LocationDL branch = LocationMapper.toDomain(dto.getBranch());
        ShiftType shiftType = ShiftType.valueOf(dto.getShiftType());
        Map<Role, Integer> requiredRoles = dto.getRequiredRoles();

        return new Shift(
                dto.getId(),
                LocalDate.from(dto.getDate().toInstant().atZone(ZoneId.systemDefault())),
                shiftType,
                dto.getStartTime(),
                dto.getEndTime(),
                dto.getShiftManagerId(),
                requiredRoles,
                new HashMap<>(),
                new HashMap<>(),
                dto.isShipmentShift(),
                branch
        );

    }

}
