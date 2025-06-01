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

        // Convert Map<Role, Integer> to Map<String, Integer>
        Map<String, Integer> requiredRolesDTO = new HashMap<>();
        for (Map.Entry<Role, Integer> entry : shift.getRequiredRoles().entrySet()) {
            requiredRolesDTO.put(entry.getKey().name(), entry.getValue());
        }

        // Convert Map<Integer, Role> to Map<Integer, String> for assignedEmployeesID
        Map<Integer, String> assignedEmployeesDTO = new HashMap<>();
        for (Map.Entry<Integer, Role> entry : shift.getAssignedEmployeesID().entrySet()) {
            assignedEmployeesDTO.put(entry.getKey(), entry.getValue().name());
        }

        // Convert Map<Integer, Role> to Map<Integer, String> for availableEmployeesID
        Map<Integer, String> availableEmployeesDTO = new HashMap<>();
        for (Map.Entry<Integer, Role> entry : shift.getAvailableEmployeesID().entrySet()) {
            availableEmployeesDTO.put(entry.getKey(), entry.getValue().name());
        }
        return new ShiftDTO(
                shift.getId(),
                Date.from(shift.getDate().atStartOfDay(ZoneId.systemDefault()).toInstant()),
                shift.getShiftType().name(),
                shift.getStartTime(),
                shift.getEndTime(),
                shift.getShiftManagerId(),
                shift.isShipmentShift(),
                branchDTO,
                requiredRolesDTO,
                assignedEmployeesDTO,
                availableEmployeesDTO);
    }

    public static Shift toDomain(ShiftDTO dto) {

        if (dto == null) {
            return null;
        }
        LocationDL branch = LocationMapper.toDomain(dto.getBranch());
        ShiftType shiftType = ShiftType.valueOf(dto.getShiftType());

        // Convert Map<String, Integer> to Map<Role, Integer>
        Map<Role, Integer> requiredRoles = new HashMap<>();
        for (Map.Entry<String, Integer> entry : dto.getRequiredRoles().entrySet()) {
            requiredRoles.put(Role.valueOf(entry.getKey()), entry.getValue());
        }

        // Convert Map<Integer, String> to Map<Integer, Role> for assignedEmployeesID
        Map<Integer, Role> assignedEmployees = new HashMap<>();
        for (Map.Entry<Integer, String> entry : dto.getAssignedEmployeesID().entrySet()) {
            assignedEmployees.put(entry.getKey(), Role.valueOf(entry.getValue()));
        }

        // Convert Map<Integer, String> to Map<Integer, Role> for availableEmployeesID
        Map<Integer, Role> availableEmployees = new HashMap<>();
        for (Map.Entry<Integer, String> entry : dto.getAvailableEmployeesID().entrySet()) {
            availableEmployees.put(entry.getKey(), Role.valueOf(entry.getValue()));
        }

        return new Shift(
                dto.getId(),
                LocalDate.from(dto.getDate().toInstant().atZone(ZoneId.systemDefault())),
                shiftType,
                dto.getStartTime(),
                dto.getEndTime(),
                dto.getShiftManagerId(),
                requiredRoles,
                assignedEmployees,
                availableEmployees,
                dto.isShipmentShift(),
                branch);

    }

}
