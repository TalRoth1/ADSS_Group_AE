import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class Shift {
    private Date date;
    private ShiftType shiftType;
    private int startTime; 
    private int endTime;
    private int shiftManagerId;
    private Map<Role, Integer> requiredRoles; // roles and number of employees required
    private Map<Integer, Role> assignedEmployeesID; 

    public Shift(Date date, ShiftType shiftType, int startTime,int endTime, int shiftManagerId) {
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

    // Getters and Setters
    public List<Integer> getAssignedEmployeesID() {
        return assignedEmployeesID;
    }

    public Date getDate() {
        return date;
    }
    public void setDate(Date date) {
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
    public Map<Role, Integer> getRequiredRoles() {
        return requiredRoles;
    }


    //methods
    public void addEmployee(ShiftEmployee e, Role role) {
        if (this.assignedEmployeesID.contains(e.getId())) {
            throw new IllegalArgumentException("Employee already assigned to this shift.");
        }
        if (!this.requiredRoles.containsKey(role)) {
            throw new IllegalArgumentException("Role not required for this shift.");
        }
        if (this.requiredRoles.get(role) <= 0) {
            throw new IllegalArgumentException("No more employees required for this role.");
        }
        this.assignedEmployeesID.add(e.getId(), role);
        this.requiredRoles.put(role, this.requiredRoles.get(role) - 1);
    }

    public void removeEmployee(ShiftEmployee e){
        if (!this.assignedEmployeesID.contains(e.getId())) {
            throw new IllegalArgumentException("Employee not assigned to this shift.");
        }
        Role role = e.getRole(); 
        this.assignedEmployeesID.remove(e.getId());
        this.requiredRoles.put(role, this.requiredRoles.get(role) + 1);
    }

}
