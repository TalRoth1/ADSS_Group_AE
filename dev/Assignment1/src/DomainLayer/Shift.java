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
    private boolean isShipmentShift = false; // Indicates if the shift includes a shipment

    public Shift(LocalDate date, ShiftType shiftType, int shiftManagerId) {
        this.date = date;
        this.shiftType = shiftType;
        if (shiftType == ShiftType.MORNING) {
            this.startTime = 600; // Default start time for morning shifts
            this.endTime = 1400; // Default end time for morning shifts
        } else {
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
        return "Shift{"
                + "date=" + date
                + ", shiftType=" + shiftType
                + ", startTime=" + startTime
                + ", endTime=" + endTime
                + ", assignedEmployeesID=" + assigned
                + '}';
    }

    public String getRole(int id) {
        if (shiftManagerId == id) {
            return "Shift Manager";
        }
        if (assignedEmployeesID.containsKey(id)) {
            return assignedEmployeesID.get(id).toString();
        }
        if (id <= 0) {
            throw new IllegalArgumentException("ID is invalid. ");
        }
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

    public boolean setShiftManagerId(int shiftManagerId) {
        if (shiftManagerId <= 0) {
            return false;
        }
        this.shiftManagerId = shiftManagerId;
        assignedEmployeesID.put(shiftManagerId, Role.SHIFT_MANAGER);
        return true;
    }

    public int getRequiredEmployees(Role role) {
        return requiredRoles.get(role);
    }

    public String getShiftString() {
        return date.toString() + " " + shiftType.toString();
    }

    public boolean setRequiredRoles(Role role, int num) {
        if (!requiredRoles.containsKey(role)) {
            return false;
        }
        requiredRoles.put(role, num);
        return true;
    }

    public String getEmployeesInfo() {
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
            return "this employee is already assigned to this shift.";
        }
        if (!requiredRoles.containsKey(role)) {
            return "Role not required for this shift.";
        }
        if (requiredRoles.get(role) <= 0) {
            return "No more employees required for this role.";
        }
        assignedEmployeesID.put(id, role);
        requiredRoles.put(role, this.requiredRoles.get(role) - 1);

        return "employee added successfully.";
    }

    public void removeEmployee(int id) {
        if (!assignedEmployeesID.containsKey(id)) {
            throw new IllegalArgumentException("Employee not assigned to this shift.");
        }
        Role role = assignedEmployeesID.get(id);
        if (isShipmentShift && (role == Role.DRIVER || role == Role.STORE_KEEPER) && requiredRoles.get(role) == 1) {
            throw new IllegalArgumentException("Cannot remove last driver or store keeper from a shipment shift.");
        }
        assignedEmployeesID.remove(id);
        requiredRoles.put(role, requiredRoles.get(role) + 1);
    }

    public void addPrefemployee(int id, Role role) {
        if (availableEmployeesID.containsKey(id)) {
            throw new IllegalArgumentException("Employee already available for this shift.");
        }
        if (!requiredRoles.containsKey(role)) {
            throw new IllegalArgumentException("Role not required for this shift.");
        }
        availableEmployeesID.put(id, role);
    }

    public void removePrefemployee(int id) {
        if (!availableEmployeesID.containsKey(id)) {
            throw new IllegalArgumentException("Employee not available for this shift.");
        }
        availableEmployeesID.remove(id);
    }

    public boolean isShipmentShift() {
        return isShipmentShift;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Shift shift = (Shift) o;
        return date.equals(shift.date) && shiftType == shift.shiftType;
    }

    @Override
    public int hashCode() {
        return date.hashCode() + shiftType.hashCode();
    }

}
