package DataLayer.Mappers;

import DTO.LocationDTO;
import DTO.ShiftDTO;
import DomainLayer.Shift;

import java.time.LocalDate;

public class ShiftMapper {



    /* private String date;
    private String shiftType;
    private int branchid;
    private int startTime;
    private int endTime;
    private int shiftManagerId;
    private int numOfRequiredcashiers;
    private int numOfRequireddrivers;
    private int numOfRequiredstoreKeepers;
    private int numOfRequiredshipmentManagers;
    private boolean isShipmentShift;
    private List<Integer> employees;

    public ShiftDTO(String date, String shiftType, int startTime, int endTime, int shiftManagerId,
            int numOfRequiredcashiers, int numOfRequireddrivers, int numOfRequiredstoreKeepers,
            int numOfRequiredshipmentManagers, boolean isShipmentShift, int branchid) {
        this.branchid = branchid;
        this.date = date;
        this.shiftType = shiftType;
        this.startTime = startTime;
        this.endTime = endTime;
        this.shiftManagerId = shiftManagerId;
        this.numOfRequiredcashiers = numOfRequiredcashiers;
        this.numOfRequireddrivers = numOfRequireddrivers;
        this.numOfRequiredstoreKeepers = numOfRequiredstoreKeepers;
        this.numOfRequiredshipmentManagers = numOfRequiredshipmentManagers;
        this.isShipmentShift = isShipmentShift;
    }*/

    public static ShiftDTO toDTO(Shift shift) {
        if (shift == null) {
            return null;
        }
        LocationDTO branchDTO = LocationMapper.toDTO(shift.getBranch()); 
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
               branchDTO);
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
                LocalDate.parse(dto.getDate()),
                dto.getShiftType(),
                dto.getStartTime(),
                dto.getEndTime(),
                dto.getShiftManagerId(),
                dto.getNumOfRequiredcashiers(),
                dto.getNumOfRequireddrivers(),
                dto.getNumOfRequiredstoreKeepers(),
                dto.getNumOfRequiredshipmentManagers(),
                dto.isShipmentShift(),
                LocationMapper.toDomain(dto.getBranch()));

        //LocalDate date, ShiftType shiftType, int shiftManagerId, LocationDL branch)
    }
}
