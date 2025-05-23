package DomainLayer;


import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

public class Shift {
    private LocalDate date;
    private ShiftType shiftType;
    private int startTime; // 24-hour format: e.g. 9:00 AM = 900, 10:30 PM = 2230
    // Morning shifts: default start time is 600 (6:00 AM) and end time is 1400 (2:00 PM)
    // Evening shifts: default start time is 1400 (2:00 PM) and end time is 2200 (10:00 PM)
    private int endTime; // 24-hour format like startTime
    private int shiftManagerId;
    private Map<Role, Integer> requiredRoles; // roles and number of employees required
    private Map<Integer, Role> assignedEmployeesID; 
    private Map<Integer, Role> availableEmployeesID;

    public Shift(LocalDate date, ShiftType shiftType, int shiftManagerId) {
        this.date = date;
        this.shiftType = shiftType;
        if(shiftType == ShiftType.MORNING) {
            this.startTime = 600; // Default start time for morning shifts
            this.endTime = 1400; // Default end time for morning shifts
        }
        else  {
            this.startTime = 1400; //Default start time for evening shifts
            this.endTime = 2200; //Default end time for evening shifts
        }
        this.shiftManagerId = shiftManagerId;
        this.requiredRoles = new HashMap<>();
        this.availableEmployeesID = new HashMap<>();
        this.assignedEmployeesID = new HashMap<>();
        for (Role role : Role.values()) {
            this.requiredRoles.put(role, 0); // Initialize roles with 0 required employees
        }
        
    }

    public String toString() {
        String assigned = getEmployeesInfo();
        return "Shift{" +
                "date=" + date +
                ", shiftType=" + shiftType +
                ", startTime=" + startTime +
                ", endTime=" + endTime +
                ", assignedEmployeesID=" + assigned +
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
    public String setShiftManagerId(int shiftManagerId) {
        if (shiftManagerId <= 0) {
            return "Shift manager ID is invalid. ";
        }
        this.shiftManagerId = shiftManagerId;
        assignedEmployeesID.put(shiftManagerId, Role.SHIFT_MANAGER);
        return null;
    }

    public int getRequiredEmployees(Role role){
        return requiredRoles.get(role);
    }

    public String getShiftString() {
        return date.toString()+" "+shiftType.toString();
    }
    public String setRequiredRoles(Role role, int num) {
        if (!requiredRoles.containsKey(role)) {
            return "Role not required for this shift";
        }
        if (num < 0 ) {
            return "Number of employees cannot be negative";
        }
        requiredRoles.put(role, num);
        return null;
    }

    public String getEmployeesInfo(){
        StringBuilder sb = new StringBuilder();
        sb.append("Shift Manager ID: ").append(shiftManagerId).append("\n");
        sb.append("Assigned Employees: \n");
        for (Map.Entry<Integer, Role> entry : assignedEmployeesID.entrySet()) {
            sb.append("Employee ID: ").append(entry.getKey()).append(", Role: ").append(entry.getValue()).append("\n");
        }
        return sb.toString();
    }


    //methods
    public String addEmployee(int id, Role role) {
        if (assignedEmployeesID.containsKey(id)) {
            return "Employee already assigned to this shift.";
        }
        if (!requiredRoles.containsKey(role)) {
            return "Role not required for this shift.";
        }
        if (requiredRoles.get(role) <= 0) {
            return "No more employees required for this role.";
        }
        assignedEmployeesID.put(id, role);
        requiredRoles.put(role, this.requiredRoles.get(role) - 1);

        return null;
    }

    public String removeEmployee(int id){
        if (!assignedEmployeesID.containsKey(id)) {
            return "Employee not assigned to this shift.";
        }
        Role role = assignedEmployeesID.get(id);
        assignedEmployeesID.remove(id);
        requiredRoles.put(role, requiredRoles.get(role) + 1);
        return null;
    }

    public String addPrefemployee(int id, Role role) {
        if (availableEmployeesID.containsKey(id)) {
            return "already available for this shift.";
        }
        if (!requiredRoles.containsKey(role)) {
            return "Role not required for this shift.";
        }
        availableEmployeesID.put(id, role);
        return null;
    }
    public String removePrefemployee(int id){
        if (!availableEmployeesID.containsKey(id)) {
            return "Employee not assigned to this shift.";
        }
        availableEmployeesID.remove(id);
        return null;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Shift shift = (Shift) o;
        return date.equals(shift.date) && shiftType == shift.shiftType;
    }

    @Override
    public int hashCode() {
        return date.hashCode() + shiftType.hashCode();
    }      

}
