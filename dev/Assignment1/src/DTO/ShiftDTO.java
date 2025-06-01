package DTO;

//import java.time.LocalDate;
import java.util.Date;
import java.util.List;
import java.util.Map;

import DomainLayer.Role;

public class ShiftDTO {

    private int id;
    private Date date;
    private String shiftType;
    private LocationDTO branch;
    private int startTime;
    private int endTime;
    private int shiftManagerId;
    private boolean isShipmentShift;
    private List<Integer> employees; //לשאול אלעד נראלי לא צריך
    private final Map<Role, Integer> requiredRoles = null; // List of roles required for the shift

    public ShiftDTO(int id, Date date, String shiftType, int startTime, int endTime, int shiftManagerId,
            boolean isShipmentShift, LocationDTO branch, Map<Role, Integer> requiredRoles) {
        this.id = id;
        this.branch = branch;
        this.date = date;
        this.shiftType = shiftType;
        this.startTime = startTime;
        this.endTime = endTime;
        this.shiftManagerId = shiftManagerId;
        this.isShipmentShift = isShipmentShift;
        this.requiredRoles.putAll(requiredRoles);
    }

    public Date getDate() {
        return date;
    }

    public String getShiftType() {
        return shiftType;
    }

    public int getStartTime() {
        return startTime;
    }

    public int getEndTime() {
        return endTime;
    }

    public int getShiftManagerId() {
        return shiftManagerId;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public void setShiftType(String shiftType) {
        this.shiftType = shiftType;
    }

    public void setStartTime(int startTime) {
        this.startTime = startTime;
    }

    public void setEndTime(int endTime) {
        this.endTime = endTime;
    }

    public void setShiftManagerId(int shiftManagerId) {
        this.shiftManagerId = shiftManagerId;
    }

    public boolean isShipmentShift() {
        return isShipmentShift;
    }

    public void setBranch(LocationDTO branch) {
        this.branch = branch;
    }

    public LocationDTO getBranch() {
        return branch;
    }

    public int getId() {
        return id;
    }

    public Map<Role, Integer> getRequiredRoles() {
        return requiredRoles;
    }

}
