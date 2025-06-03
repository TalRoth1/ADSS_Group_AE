package DomainLayer;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class ShiftEmployee extends Employee {

    private List<Shift> preferredShifts;
    private Map<Shift, Role> assignedShifts;
    private List<Role> roles;

    public ShiftEmployee(int id, String name, LocationDL branch, String bankAccount, int salary, LocalDate startDate,
            int vacationDays, int sickDays, float educationFund, float socialBenefits,
            String password, Role role) {
        super(id, name, branch, bankAccount, salary, startDate, vacationDays, sickDays, educationFund,
                socialBenefits, password);
        this.preferredShifts = new ArrayList<>();
        this.roles = new ArrayList<>();
        this.assignedShifts = new HashMap<>();
        roles.add(role);
    }

    public void getPreferredShiftsToString() throws Exception { // employee or manager can see emp's preferred shifts
        String res = "Preferred shifts: " + "\n";
        if (preferredShifts == null) {
            throw new Exception("No preferred shifts.");
        }
        for (Shift shift : preferredShifts) {
            res += shift.toString() + "\n";
        }
        System.out.println(res);
    }

    public void getAssignedShiftsToString() throws Exception { // employee or manager can see emp's assigned shifts
        String res = "Assigned shifts: " + "\n";
        if (assignedShifts == null) {
            throw new Exception("No assigned shifts.");
        }
        for (Shift shift : assignedShifts.keySet()) {
            res += shift.toString() + "\n";
        }
        System.out.println(res);
    }

    public String getAssignedEmployeesInfo(Shift shift) {
        if (shift.getShiftManagerId() == this.getId()) {
            return shift.getEmployeesInfo();
        }
        return "You are not authorized to view assigned employees for this shift.";
    }

    // methods
    public void addRole(Role role) throws Exception {
        if (role == null) {
            throw new Exception("Role cannot be null.");
        }
        if (roles.contains(role)) {
            throw new Exception("Role already exists in the list of roles.");
        }
        roles.add(role);
    }

    public void removeRole(Role role) throws Exception {
        if (role == null) {
            throw new Exception("Role cannot be null.");
        }
        if (!roles.contains(role)) {
            throw new Exception("Role does not exist in the list of roles.");
        }
        roles.remove(role);
    }

    public void changeRole(Role oldRole, Role newRole) throws Exception {
        if (oldRole == null || newRole == null) {
            throw new Exception("Roles cannot be null.");
        }
        if (!roles.contains(oldRole)) {
            throw new Exception("Old role does not exist in the list of roles.");
        }
        if (roles.contains(newRole)) {
            throw new Exception("New role already exists in the list of roles.");
        }
        roles.remove(oldRole);
        roles.add(newRole);
    }

    public void addPreferredShift(Shift shift) throws Exception {
        if (isFinishWorking()) {
            throw new Exception("Employee has finished working and cannot add preferred shifts.");
        }
        if (preferredShifts.contains(shift)) {
            throw new Exception("Shift already exists in the list of preferred shifts.");
        }
        if (shift == null) {
            throw new Exception("Shift cannot be null.");
        }
        preferredShifts.add(shift);
    }

    public void removePreferredShift(Shift shift) throws Exception {
        if (isFinishWorking()) {
            throw new Exception("Employee has finished working and cannot remove preferred shifts.");
        }
        if (!preferredShifts.contains(shift)) {
            throw new Exception("Shift does not exist in the list of preferred shifts.");
        }
        if (shift == null) {
            throw new Exception("Shift cannot be null.");
        }
        preferredShifts.remove(shift);
    }

    public void addAssignedShift(Shift shift, Role role) throws Exception {

        if (!roles.contains(role)) {
            throw new Exception("Role is not in the list of roles.");
        }
        if (isFinishWorking()) {
            throw new Exception("Employee has finished working and cannot add assigned shifts.");
        }
        if (assignedShifts.containsKey(shift)) {
            throw new Exception("Shift already exists in the list of assigned shifts.");
        }
        assignedShifts.put(shift, role);
        System.out.println("shift added successfully to assigned shifts.");
    }

    public void removeAssignedShift(Shift shift) throws Exception {
        if (isFinishWorking()) {
            throw new Exception("Employee has finished working and cannot remove assigned shifts.");
        }
        if (!assignedShifts.containsKey(shift)) {
            throw new Exception("Shift does not exist in the list of assigned shifts.");
        }
        if (shift == null) {
            throw new Exception("Shift cannot be null.");
        }
        assignedShifts.remove(shift);
    }

    public boolean isAvailable(Shift shift) throws Exception {
        if (isFinishWorking() || preferredShifts == null) {
            throw new Exception("Employee has finished working or has no preferred shifts.");
        }
        if (shift == null) {
            throw new Exception("Shift cannot be null.");
        }
        return preferredShifts.contains(shift);
    }

    public void archiveOldShiftsWeekly(LocalDate today) {
        // Find the start of this week (Sunday)
        LocalDate startOfWeek = today.with(DayOfWeek.SUNDAY);
        // Iterate through the assigned shifts and check if they are older than the
        // start of the week
        Iterator<Map.Entry<Shift, Role>> assignedIterator = assignedShifts.entrySet().iterator();
        while (assignedIterator.hasNext()) {
            Map.Entry<Shift, Role> entry = assignedIterator.next();
            Shift shift = entry.getKey();
            if (shift.getDate().isBefore(startOfWeek)) {
                // If the shift is older than the start of the week, remove it from the assigned
                // shifts
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

    public void setRoles(List<Role> roles) {
        this.roles = roles;
    }   

    @Override
    public ShiftEmployee clone() {
        ShiftEmployee res = new ShiftEmployee(
                this.getId(),
                this.getName(),
                this.getBranch(),
                this.getBankAccount(),
                this.getSalary(),
                this.getStartDate(),
                this.getVacationDays(),
                this.getSickDays(),
                this.getEducationFund(),
                this.getSocialBenefits(),
                this.getPassword(),
                this.roles.isEmpty() ? Role.CASHIER : this.roles.get(0)
        );
        res.setRoles(new ArrayList<>(this.roles));
        res.setAssignedShifts(new HashMap<>(this.assignedShifts));
        res.setPrefShifts(new ArrayList<>(this.preferredShifts));
        return res;
    }
}
