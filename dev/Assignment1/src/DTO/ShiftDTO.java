package DTO;

//import java.time.LocalDate;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Objects;

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
    // private List<Integer> employees; //i think this is not needed
    private Map<String, Integer> requiredRoles = null; // List of roles required for the shift
    private Map<Integer, String> assignedEmployeesID = null; // Map of assigned employees' IDs to their roles
    private Map<Integer, String> availableEmployeesID = null; // Map of available employees' IDs to their roles

    public ShiftDTO(int id, Date date, String shiftType, int startTime, int endTime, int shiftManagerId,
            boolean isShipmentShift, LocationDTO branch, Map<String, Integer> requiredRoles,
            Map<Integer, String> assignedEmployeesID, Map<Integer, String> availableEmployeesID) {
        this.id = id;
        this.branch = branch;
        this.date = date;
        this.shiftType = shiftType;
        this.startTime = startTime;
        this.endTime = endTime;
        this.shiftManagerId = shiftManagerId;
        this.isShipmentShift = isShipmentShift;
        this.requiredRoles = requiredRoles;
        this.assignedEmployeesID = assignedEmployeesID;
        this.availableEmployeesID = availableEmployeesID;
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

    public Map<String, Integer> getRequiredRoles() {
        return requiredRoles;
    }

    public Map<Integer, String> getAssignedEmployeesID() {
        return assignedEmployeesID;
    }

    public Map<Integer, String> getAvailableEmployeesID() {
        return availableEmployeesID;
    }

    public void setRequiredRoles(Map<String, Integer> requiredRoles) {
        this.requiredRoles.clear();
        this.requiredRoles.putAll(requiredRoles);
    }


    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        ShiftDTO shiftDTO = (ShiftDTO) o;
        return Objects.equals(date, shiftDTO.date) &&
                Objects.equals(shiftType, shiftDTO.shiftType) &&
                Objects.equals(branch, shiftDTO.branch);
    }

    public int hashCode() {
        return Objects.hash(date, shiftType, branch);
    }
}
