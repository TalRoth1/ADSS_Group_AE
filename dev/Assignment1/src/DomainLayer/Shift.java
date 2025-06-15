package DomainLayer;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

public class Shift {

    private int id; // Unique identifier for the shift
    private LocalDate date;
    private ShiftType shiftType;
    private int startTime; // 24-hour format: e.g. 9:00 AM = 900, 10:30 PM = 2230
    // Morning shifts: default start time is 600 (6:00 AM) and end time is 1400
    // (2:00 PM)
    // Evening shifts: default start time is 1400 (2:00 PM) and end time is 2200
    // (10:00 PM)
    private int endTime; // 24-hour format like startTime
    private int shiftManagerId;
    private Map<Role, Integer> requiredRoles; // roles and number of employees required
    private Map<Integer, Role> assignedEmployeesID;
    private Map<Integer, Role> availableEmployeesID; //preffered shifts of employees
    private boolean isShipmentShift = false; // Indicates if the shift includes a shipment
    private LocationDL branch;

    public Shift(int id, LocalDate date, ShiftType shiftType, int shiftManagerId, LocationDL branch) {
        this.id = id;
        this.date = date;
        this.shiftType = shiftType;
        if (shiftType == ShiftType.MORNING) {
            this.startTime = 600; // Default start time for morning shifts
            this.endTime = 1400; // Default end time for morning shifts
        } else {
            this.startTime = 1400; // Default start time for evening shifts
            this.endTime = 2200; // Default end time for evening shifts
        }
        this.shiftManagerId = shiftManagerId;
        this.requiredRoles = new HashMap<>();
        this.availableEmployeesID = new HashMap<>();
        this.assignedEmployeesID = new HashMap<>();
        this.branch = branch;
        for (Role role : Role.values()) {
            this.requiredRoles.put(role, 0); // Initialize roles with 0 required employees
        }

    }

    public Shift(int id, LocalDate date, ShiftType shiftType, int startTime, int endTime, int shiftManagerId, Map<Role, Integer> requiredRoles, Map<Integer, Role> assignedEmployeesID, Map<Integer, Role> availableEmployeesID, boolean isShipmentShift, LocationDL branch) {
        this.id = id;
        this.date = date;
        this.shiftType = shiftType;
        this.startTime = startTime;
        this.endTime = endTime;
        this.shiftManagerId = shiftManagerId;
        this.requiredRoles = requiredRoles;
        this.assignedEmployeesID = assignedEmployeesID;
        this.availableEmployeesID = availableEmployeesID;
        this.isShipmentShift = isShipmentShift;
        this.branch = branch;

        // Ensure all roles are initialized in requiredRoles
        for (Role role : Role.values()) {
            if (!this.requiredRoles.containsKey(role)) {
                this.requiredRoles.put(role, 0);
            }
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

    public String getRole(int id) throws Exception {
        if (shiftManagerId == id) {
            throw new Exception("shift manager. ");
        }
        if (assignedEmployeesID.containsKey(id)) {
            return assignedEmployeesID.get(id).toString();
        }
        if (id <= 0) {
            throw new Exception("ID is invalid. ");
        }
        return "Employee with ID " + id + " is not assigned to this shift.";
    }

    // Getters and Setters
    public Map<Integer, Role> getAssignedEmployeesID() {
        return assignedEmployeesID;
    }

    public Map<Integer, Role> getAvailableEmployeesID() {
        return availableEmployeesID;
    }

    public int getId() {
        return id;
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

    public void setRequiredRoles(Role role, int num) throws Exception {
        if (!requiredRoles.containsKey(role)) {
            throw new Exception("Role not required for this shift.");
        }
        requiredRoles.put(role, num);
    }

    public LocationDL getBranch() {
        return branch;
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

    // methods
    public void addEmployee(int id, Role role) throws Exception {
        if (assignedEmployeesID.containsKey(id)) {
            throw new Exception("This employee is already assigned to this shift.");
        }
        if (!requiredRoles.containsKey(role)) {
            throw new Exception("Role not required for this shift.");
        }
        if (requiredRoles.get(role) <= 0) {
            throw new Exception("No more employees required for this role.");
        }
        assignedEmployeesID.put(id, role);
        requiredRoles.put(role, this.requiredRoles.get(role) - 1);
        System.out.println("Employee with ID " + id + " added to shift on " + date + " as " + role);
    }

    public void removeEmployee(int id) throws Exception {
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

    public void setShipmentShift(boolean shipmentShift) {
        isShipmentShift = shipmentShift;
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

    public Map<Role, Integer> getRequiredRoles() {
        return requiredRoles;
    }

}
