package DomainLayer;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

public class ShiftEmployee extends Employee {
    private List<Shift> preferredShifts;
    private List<Shift> assignedShifts; 
    private List<Role> roles;
    private Set<Shift> pastShifts = new HashSet<>();

    public ShiftEmployee(int id, String name, String branch, String bankAccount, int salary, LocalDate startDate,
                         int vacationDays, int sickDays, double educationFund, double socialBenefits,
                         String password, Role role) {
        super(id, name,branch, bankAccount, salary, startDate, vacationDays, sickDays, educationFund,
                socialBenefits, password);
        this.preferredShifts = new ArrayList<>();
        this.roles = new ArrayList<>();
        this.assignedShifts = new ArrayList<>();
        roles.add(role);
    }
    public String getPreferredShiftsToString() {
        String res="Preferred shifts: " + "\n";
        if(preferredShifts == null) {
            return "No preferred shifts.";
        }
        for(Shift shift : preferredShifts) {
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

 
    // כשסיים לעבוד אסור לו לבחור משמרות ולהשתבץ!

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

    public String addAssignedShift(Shift shift) {
        if(isFinishWorking()) {
            return "Employee has finished working and cannot add assigned shifts.";
        }
        if(assignedShifts.contains(shift)) {
            return "Shift already exists in the list of assigned shifts.";
        }
        if(shift == null) {
            return "Shift cannot be null.";
        }
        assignedShifts.add(shift);
        return null;
    }

    public String removeAssignedShift(Shift shift) {
        if(isFinishWorking()) {
            return "Employee has finished working and cannot remove assigned shifts.";
        }
        if(!assignedShifts.contains(shift)) {
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
        // Find the start of this week (e.g., Monday)
        LocalDate startOfThisWeek = today.with(java.time.DayOfWeek.SUNDAY);
        Iterator<Shift> iterator = assignedShifts.iterator();
        while (iterator.hasNext()) {
            Shift shift = iterator.next();
            if (shift.getDate().isBefore(startOfThisWeek)) {
                pastShifts.add(shift);
                iterator.remove();
            }
        }
        //remove shifts older than 7 years
        pastShifts.removeIf(shift -> shift.getDate().isBefore(today.minusYears(7)));
    }
    
  //  public boolean isShiftManager(){
   //     return this.trainings.contains(Training.TeamManagement)&&trainings.contains(Training.CancellationCard);
  //  }

    public boolean isShiftManager() {
        return roles.contains(Role.SHIFT_MANAGER);
    }

    public List<Shift> getAssignedShifts() {
        return assignedShifts;
    }
    public void setAssignedShifts(List<Shift> assignedShifts) {
        this.assignedShifts = assignedShifts;
    }
    public List<Shift> getPrefShifts() {
        return preferredShifts;
    }
    public void setPrefShifts(List<Shift> preferredShifts) {
        this.preferredShifts = preferredShifts;
    }

    public Set<Shift> getPastShifts() {
        return pastShifts;
    }
    public void setPastShifts(Set<Shift> pastShifts) {
        this.pastShifts = pastShifts;
    }

    public List<Role> getRoles() {
        return roles;
    }

 


}
