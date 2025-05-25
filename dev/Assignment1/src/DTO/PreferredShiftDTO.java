package DTO;

public class PreferredShiftDTO {

    private int employeeId;
    private String shiftDate;   // for example "2024-06-01"
    private String shiftType;   // "MORNING" or "EVENING"

    public PreferredShiftDTO(int employeeId, String shiftDate, String shiftType) {
        this.employeeId = employeeId;
        this.shiftDate = shiftDate;
        this.shiftType = shiftType;
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

    public void setEmployeeId(int employeeId) {
        this.employeeId = employeeId;
    }

    public void setShiftDate(String shiftDate) {
        this.shiftDate = shiftDate;
    }

    public void setShiftType(String shiftType) {
        this.shiftType = shiftType;
    }

}
