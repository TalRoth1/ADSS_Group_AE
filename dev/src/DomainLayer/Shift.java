package DomainLayer;


import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

public class Shift {
    private LocalDate date;
    private ShiftType shiftType;
    private int startTime; 
    private int endTime;
    private int shiftManagerId;
    private Map<Role, Integer> requiredRoles; // roles and number of employees required
    private Map<Integer, Role> assignedEmployeesID; 

    public Shift(LocalDate date, ShiftType shiftType, int startTime,int endTime, int shiftManagerId) {
        this.date = date;
        this.shiftType = shiftType;
        this.startTime = startTime;
        this.endTime = endTime;
        this.shiftManagerId = shiftManagerId;
        for (Role role : Role.values()) {
            this.requiredRoles.put(role, 0); // Initialize roles with 0 required employees
        }
        this.assignedEmployeesID = new HashMap<>();
    }

    public String toString() {
        return "Shift{" +
                "date=" + date +
                ", shiftType=" + shiftType +
                ", startTime=" + startTime +
                ", endTime=" + endTime +
                ", shiftManagerId=" + shiftManagerId +
                ", assignedEmployeesID=" + assignedEmployeesID +
                '}';
    }

    public String getRole(int id){
        if (shiftManagerId == id)
            return "Shift Manager";
        if (assignedEmployeesID.containsKey(id))
            return assignedEmployeesID.get(id).toString();
        if (id <= 0)
            throw new IllegalArgumentException("ID is invalid. ");
        return null;
    }

    // Getters and Setters
    public Map<Integer, Role> getAssignedEmployeesID() {
        return assignedEmployeesID;
    }

    public LocalDate getDate() {
        return date;
    }
    public void setDate(LocalDate date) {
        this.date = date;
    }
    public ShiftType getShiftType() {
        return shiftType;
    }
    public void setShiftType(ShiftType shiftType) {
        this.shiftType = shiftType;
    }
    public int getStartTime() {
        return startTime;
    }
    public void setStartTime(int startTime) {
        this.startTime = startTime;
    }
    public int getEndTime() {
        return endTime;
    }
    public void setEndTime(int endTime) {
        this.endTime = endTime;
    }
    public int getShiftManagerId() {
        return shiftManagerId;
    }
    public void setShiftManagerId(int shiftManagerId) {
        if (shiftManagerId <= 0) {
            throw new IllegalArgumentException("Shift manager ID is invalid. ");
        }
        this.shiftManagerId = shiftManagerId;
    }

    public int getRequiredEmployees(Role role){
        return this.requiredRoles.get(role);
    }

    public String getShiftString() {
        return this.date.toString()+" "+this.shiftType.toString();
    }
    public void setRequiredRoles(Role role, int num) {
        if (!this.requiredRoles.containsKey(role)) {
            throw new IllegalArgumentException("Role not required for this shift.");
        }
        if (this.requiredRoles.get(role) == num) {
            throw new IllegalArgumentException("Number of employees required is already set to this value.");
        }
        if (num < 0 ) {
            throw new IllegalArgumentException("Number of employees cannot be negative");
        }
        this.requiredRoles.put(role, num);
    }


    //methods
    public void addEmployee(int id, Role role) {
        if (this.assignedEmployeesID.containsKey(id)) {
            throw new IllegalArgumentException("Employee already assigned to this shift.");
        }
        if (!this.requiredRoles.containsKey(role)) {
            throw new IllegalArgumentException("Role not required for this shift.");
        }
        if (this.requiredRoles.get(role) <= 0) {
            throw new IllegalArgumentException("No more employees required for this role.");
        }
        this.assignedEmployeesID.put(id, role);
        this.requiredRoles.put(role, this.requiredRoles.get(role) - 1);
    }

    public void removeEmployee(int id){
        if (!this.assignedEmployeesID.containsKey(id)) {
            throw new IllegalArgumentException("Employee not assigned to this shift.");
        }
        Role role = this.assignedEmployeesID.get(id);
        this.assignedEmployeesID.remove(id);
        this.requiredRoles.put(role, this.requiredRoles.get(role) + 1);
    }

}
