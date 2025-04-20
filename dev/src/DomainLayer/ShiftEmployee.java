package DomainLayer;


import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class ShiftEmployee extends Employee {
    private List<Shift> preferredShifts;
    private List<Shift> assignedShifts; 
    private List<Role> roles;
    private List<Training> trainings;
    private List<Shift> pastShifts; //!!!!!!

    public ShiftEmployee(int id, String name, String bankAccount, int salary, LocalDate startDate,
                         int vacationDays, int sickDays, double educationFund, double socialBenefits,
                         String password, Role role) {
        super(id, name, bankAccount, salary, startDate, vacationDays, sickDays, educationFund,
                socialBenefits, password);
        this.preferredShifts = null;
        this.roles = new ArrayList<>();
        this.trainings = new ArrayList<>();
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

    public List<Shift> getPreferredShifts(){
        return preferredShifts;
    }
    public void setPreferredShifts(List<Shift> preferredShifts) {
        this.preferredShifts = preferredShifts;
    }
    public List<Training> getTrainings() {
        return trainings;
    }
    public List<Role> getRoles() {
        return roles;
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

    public String addTraining(Training training) {
        if(training == null) {
            return "Training cannot be null.";
        }
        if(trainings.contains(training)) {
            return "Training already exists in the list of trainings.";
        }
        trainings.add(training);
        return null;
    }

    public String removeTraining(Training training) {
        if(training == null) {
            return "Training cannot be null.";
        }
        if(!trainings.contains(training)) {
            return "Training does not exist in the list of trainings.";
        }
        trainings.remove(training);
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
    
  //  public boolean isShiftManager(){
   //     return this.trainings.contains(Training.TeamManagement)&&trainings.contains(Training.CancellationCard);
  //  }

    public void getAssignedEmployeesInfo(Shift shift) {
        if(shift.getShiftManagerId() == this.getId()) { //allow shift manager to see all employees in the shift
            shift.getEmployeesInfo();
        }
    }

    public List<Shift> getAssignedShifts() {
        return assignedShifts;
    }
    public List<Shift> getPrefShifts() {
        return preferredShifts;
    }

    public List<Shift> getPastShifts() {
        return pastShifts;
    }
    public void setPastShifts(List<Shift> pastShifts) {
        this.pastShifts = pastShifts;
    }
    public void setAssignedShifts(List<Shift> assignedShifts) {
        this.assignedShifts = assignedShifts;
    }

 


}
