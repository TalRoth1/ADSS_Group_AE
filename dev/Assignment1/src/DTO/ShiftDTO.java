package DTO;

import java.util.Map;

public class ShiftDTO {

    private String date;
    private String shiftType;
    private int startTime;
    private int endTime;
    private int shiftManagerId;
    private Map<String, Integer> requiredRoles; // role name -> number required
    private Map<Integer, String> assignedEmployeesID; // employee id -> role name
    private Map<Integer, String> availableEmployeesID; // employee id -> role name

    public ShiftDTO(String date, String shiftType, int startTime, int endTime, int shiftManagerId,
            Map<String, Integer> requiredRoles,
            Map<Integer, String> assignedEmployeesID,
            Map<Integer, String> availableEmployeesID) {
        this.date = date;
        this.shiftType = shiftType;
        this.startTime = startTime;
        this.endTime = endTime;
        this.shiftManagerId = shiftManagerId;
        this.requiredRoles = requiredRoles;
        this.assignedEmployeesID = assignedEmployeesID;
        this.availableEmployeesID = availableEmployeesID;
    }

    public String getDate() {
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

    public Map<String, Integer> getRequiredRoles() {
        return requiredRoles;
    }

    public Map<Integer, String> getAssignedEmployeesID() {
        return assignedEmployeesID;
    }

    public Map<Integer, String> getAvailableEmployeesID() {
        return availableEmployeesID;
    }
}
