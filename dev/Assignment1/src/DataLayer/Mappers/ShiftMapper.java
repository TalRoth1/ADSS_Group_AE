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
                shift.getDate(),
                shift.getShiftType(),
                shift.getStartTime(),
                shift.getEndTime(),
                shift.getShiftManagerId(),
                shift.getNumOfRequiredcashiers(),
                shift.getNumOfRequireddrivers(),
                shift.getNumOfRequiredstoreKeepers(),
                shift.getNumOfRequiredshipmentManagers(),
                shift.isShipmentShift(),
                shift.getBranchid());
    }
    // String date, String shiftType, int startTime, int endTime, int
    // shiftManagerId,
    // int numOfRequiredcashiers, int numOfRequireddrivers, int
    // numOfRequiredstoreKeepers,
    // int numOfRequiredshipmentManagers, boolean isShipmentShift, int branchid)

    public static Shift toDomain(ShiftDTO dto) {
        if (dto == null) {
            return null;
        }
        return new Shift(
                dto.getDate(),
                dto.getShiftType(), 
                dto.getStartTime(),


                //LocalDate date, ShiftType shiftType, int shiftManagerId, LocationDL branch)
    }
}
