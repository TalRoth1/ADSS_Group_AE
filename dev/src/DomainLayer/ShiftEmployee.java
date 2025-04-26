package DomainLayer;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class ShiftEmployee extends Employee {
    private List<Shift> preferredShifts;
    private Map<Shift, Role> assignedShifts; 
    private List<Role> roles;

    public ShiftEmployee(int id, String name, String branch, String bankAccount, int salary, LocalDate startDate,
                         int vacationDays, int sickDays, double educationFund, double socialBenefits,
                         String password, Role role) {
        super(id, name,branch, bankAccount, salary, startDate, vacationDays, sickDays, educationFund,
                socialBenefits, password);
        this.preferredShifts = new ArrayList<>();
        this.roles = new ArrayList<>();
        this.assignedShifts = new HashMap<>();
        roles.add(role);
    }
    public String getPreferredShiftsToString() { //employee or manager can see emp's preferred shifts
        String res="Preferred shifts: " + "\n";
        if(preferredShifts == null) {
            return "No preferred shifts.";
        }
        for(Shift shift : preferredShifts) {
            res += shift.toString() + "\n";
        }
        return res;
    }

    public String getAssignedShiftsToString() { //employee or manager can see emp's assigned shifts
        String res="Assigned shifts: " + "\n";
        if(assignedShifts == null) {
            return "No assigned shifts.";
        }
        for(Shift shift : assignedShifts.keySet()) {
            res += shift.toString() + "\n";
        }
        return res;
    }

    public String getAssignedEmployeesInfo(Shift shift) {
        if(shift.getShiftManagerId() == this.getId()) {
            return shift.getEmployeesInfo();
        }
        return "You are not authorized to view assigned employees for this shift.";
    }

    //methods

    public String addRole(Role role) {
        if(role == null) {
            return "Role cannot be null.";
        }
        if(roles.contains(role)) {
            return this.getName() +" is already " + role;
        }
        roles.add(role);
        return null;
    }

    public String removeRole(Role role) {
        if(role == null)
            return "Role cannot be null.";
        if(!roles.contains(role))
           return "Role does not exist in the list of roles.";
        roles.remove(role);
        return null;
    }

    public String changeRole(Role oldRole, Role newRole) {
        if(oldRole == null || newRole == null) {
            return "Role cannot be null.";
        }
        if(!roles.contains(oldRole)) {
            return "Old role does not exist in the list of roles.";
        }
        if(roles.contains(newRole)) {
            return "New role already exists in the list of roles.";
        }
        roles.remove(oldRole);
        roles.add(newRole);
        return null;
    }

    public String addPreferredShift(Shift shift) {
        if(isFinishWorking()) {
            return "Employee has finished working and cannot add preferred shifts.";
        }
        if(preferredShifts.contains(shift)) {
            return "Shift already exists in the list of preferred shifts.";
        }
        if(shift == null) {
            return "Shift cannot be null.";
        }
        preferredShifts.add(shift);
        return null;
    }

    public String removePreferredShift(Shift shift) {
        if(isFinishWorking()) {
            return "Employee has finished working and cannot remove preferred shifts.";
        }
        if(!preferredShifts.contains(shift)) {
            return "Shift doesnt exists in the list of preferred shifts.";
        }
        if(shift == null) {
            return "Shift cannot be null.";
        }
        preferredShifts.remove(shift);
        return null;
    }

    public String addAssignedShift(Shift shift, Role role) {
        if(role == null) {
            return "Role cannot be null.";
        }
        if(!roles.contains(role)) {
            return "Role does not exist in the list of roles.";
        }
        if(isFinishWorking()) {
            return "Employee has finished working and cannot add assigned shifts.";
        }
        if(assignedShifts.containsKey(shift)) {
            return "Shift already exists in the list of assigned shifts.";
        }
        if(shift == null) {
            return "Shift cannot be null.";
        }
        assignedShifts.put(shift, role);
        return null;
    }

    public String removeAssignedShift(Shift shift) {
        if(isFinishWorking()) {
            return "Employee has finished working and cannot remove assigned shifts.";
        }
        if(!assignedShifts.containsKey(shift)) {
            return "Shift doesnt exists in the list of assigned shifts.";
        }
        if(shift == null) {
            return "Shift cannot be null.";
        }
        assignedShifts.remove(shift);
        return null;
    }

    public boolean isAvailable(Shift shift) {
        if(isFinishWorking() || preferredShifts == null) {
            return false;
        }
        if(shift == null) {
            throw new IllegalArgumentException("Shift cannot be null.");
        }
        return preferredShifts.contains(shift);
    }

    public void archiveOldShiftsWeekly(LocalDate today) {
        // Find the start of this week (Sunday)
        LocalDate startOfWeek = today.with(DayOfWeek.SUNDAY);
        // Iterate through the assigned shifts and check if they are older than the start of the week
        Iterator<Map.Entry<Shift, Role>> assignedIterator = assignedShifts.entrySet().iterator();
        while (assignedIterator.hasNext()) {
            Map.Entry<Shift, Role> entry = assignedIterator.next();
            Shift shift = entry.getKey();
            if (shift.getDate().isBefore(startOfWeek)) {
                // If the shift is older than the start of the week, remove it from the assigned shifts
                assignedIterator.remove();
            }
        }      
    }

    public boolean isShiftManager() {
        return roles.contains(Role.SHIFT_MANAGER);
    }

    public Map<Shift, Role> getAssignedShifts() {
        return assignedShifts;
    }
    public void setAssignedShifts(Map<Shift, Role> assignedShifts) {
        this.assignedShifts = assignedShifts;
    }
    public List<Shift> getPrefShifts() {
        return preferredShifts;
    }
    public void setPrefShifts(List<Shift> preferredShifts) {
        this.preferredShifts = preferredShifts;
    }

    public List<Role> getRoles() {
        return roles;
    }
}
