package DTO;

public class EmployeeShiftDTO {

    private int employeeId;
    private String shiftDate; // for example "2024-06-01"
    private String shiftType; // "MORNING" or "EVENING"

    private String role;

    public EmployeeShiftDTO(int employeeId, String shiftDate, String shiftType, String role) {
        this.employeeId = employeeId;
        this.shiftDate = shiftDate;
        this.shiftType = shiftType;
        this.role = role;
    }

    public int getEmployeeId() {
        return employeeId;
    }

    public String getShiftDate() {
        return shiftDate;
    }

    public String getShiftType() {
        return shiftType;
    }

    public String getRole() {
        return role;
    }

    public void setEmployeeId(int employeeId) {
        this.employeeId = employeeId;
    }

    public void setShiftDate(String shiftDate) {
        this.shiftDate = shiftDate;
    }

    public void setShiftType(String shiftType) {
        this.shiftType = shiftType;
    }

    public void setRole(String role) {
        this.role = role;
    }

}
