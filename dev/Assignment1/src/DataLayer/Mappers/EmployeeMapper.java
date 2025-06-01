package DataLayer.Mappers;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import DTO.EmployeeDTO;
import DTO.LocationDTO;
import DTO.ShiftDTO;
import DomainLayer.LocationDL;
import DomainLayer.Role;
import DomainLayer.Shift;
import DomainLayer.ShiftEmployee;

public class EmployeeMapper {

    public static EmployeeDTO toDTO(ShiftEmployee e) {
        if (e == null) {
            return null;
        }
        LocationDTO branchDTO = LocationMapper.toDTO(e.getBranch());
        List<String> roles = new ArrayList<>();
        for (Role role : e.getRoles()) {
            roles.add(role.name());
        }

        List<ShiftDTO> assignedShifts = new ArrayList<>();
        if (e.getAssignedShifts() != null) {
            for (Map.Entry<Shift, Role> entry : e.getAssignedShifts().entrySet()) {
                Shift shift = entry.getKey();
                Role role = entry.getValue();
                assignedShifts.add(new ShiftDTO(
                        shift.getId(),
                        Date.from(shift.getDate().atStartOfDay(ZoneId.systemDefault()).toInstant()),
                        shift.getShiftType().name(),
                        shift.getStartTime(),
                        shift.getEndTime(),
                        shift.getShiftManagerId(),
                        shift.isShipmentShift(),
                        branchDTO,
                        shift.getRequiredRoles().entrySet().stream()
                                .collect(java.util.stream.Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue))
                ));
            }
        }
        //(int id, Date date, String shiftType, int startTime, int endTime, int shiftManagerId,
        //   boolean isShipmentShift, LocationDTO branch, Map<Role, Integer> requiredRoles)

        List<ShiftDTO> preferredShifts = new ArrayList<>();
        if (e.getPrefShifts() != null) {
            for (Shift shift : e.getPrefShifts()) {
                preferredShifts.add(new ShiftDTO(
                        shift.getId(),
                        Date.from(shift.getDate().atStartOfDay(ZoneId.systemDefault()).toInstant()),
                        shift.getShiftType().name(),
                        shift.getStartTime(),
                        shift.getEndTime(),
                        shift.getShiftManagerId(),
                        shift.isShipmentShift(),
                        branchDTO,
                        shift.getRequiredRoles().entrySet().stream()
                                .collect(java.util.stream.Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue))
                ));
            }
        }
        // Convert LocalDate to java.util.Date for DTO
        Date startDate = Date.from(e.getStartDate().atStartOfDay(ZoneId.systemDefault()).toInstant());

        return new EmployeeDTO(
                e.getId(),
                e.getName(),
                branchDTO,
                e.getBankAccount(),
                e.getSalary(),
                startDate,
                e.getVacationDays(),
                e.getSickDays(),
                e.getEducationFund(),
                e.getSocialBenefits(),
                e.getPassword(),
                e.isFinishWorking(),
                roles,
                assignedShifts,
                preferredShifts
        );

    }

    public static ShiftEmployee toDomain(EmployeeDTO dto, List<LocationDL> allBranches, List<ShiftDTO> assignedShifts,
            List<ShiftDTO> preferredShifts) {
        if (dto == null) {
            return null;
        }
        LocationDL branch = null;
        for (LocationDL loc : allBranches) {
            if (loc.getId() == dto.getBranchid().getId()) {
                branch = loc;
                break;
            }
        }
        if (branch == null) {
            throw new IllegalArgumentException("Branch not found for employee");
        }
        LocalDate startDate = dto.getStartDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        Role mainRole = dto.getRoles().isEmpty() ? null : Role.valueOf(dto.getRoles().get(0));
        ShiftEmployee employee = new ShiftEmployee(
                dto.getId(),
                dto.getName(),
                branch,
                dto.getBankAccount(),
                dto.getSalary(),
                startDate,
                dto.getVacationDays(),
                dto.getSickDays(),
                dto.getEducationFund(),
                dto.getSocialBenefits(),
                dto.getPassword(),
                mainRole
        );
        for (String roleStr : dto.getRoles()) {
            Role role = Role.valueOf(roleStr);
            if (!employee.getRoles().contains(role)) {
                try {
                    employee.addRole(role);
                } catch (Exception ignored) {
                }
            }
        }
        if (assignedShifts != null) {
            for (ShiftDTO shiftDTO : assignedShifts) {
                Shift shift = ShiftMapper.toDomain(shiftDTO);
                try {
                    employee.addAssignedShift(shift, employee.getAssignedShifts().get(shift));
                } catch (Exception ignored) {
                }
            }
        }

        if (preferredShifts != null) {
            for (ShiftDTO shiftDTO : preferredShifts) {
                Shift shift = ShiftMapper.toDomain(shiftDTO);
                try {
                    employee.addPreferredShift(shift);
                } catch (Exception ignored) {
                }
            }
        }

        return employee;
    }
}
