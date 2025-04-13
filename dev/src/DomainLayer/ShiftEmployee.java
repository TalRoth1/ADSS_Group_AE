package DomainLayer;

import java.util.Date;
import java.util.List;

public class ShiftEmployee extends Employee {
    private List<Shift> preferredShifts;
    private List<Role> roles;
    private List<Training> trainings;

    public ShiftEmployee(int id, String name, String bankAccount, int salary, Date startDate, int vacationDays, int sickDays, String username, String password) {
        super(id, name, bankAccount, salary, startDate, vacationDays, sickDays, username, password);
        this.preferredShifts = null;
        this.roles = null;
    }
    public List<Shift> getPreferredShifts() {
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

    //methods
    public void addRole(Role role) {
        if(role == null) {
            throw new IllegalArgumentException("Role cannot be null.");
        }
        if(this.roles.contains(role)) {
            throw new IllegalArgumentException("Role already exists in the list of roles.");
        }
        this.roles.add(role);
    }

    public void removeRole(Role role) {
        if(role == null) {
            throw new IllegalArgumentException("Role cannot be null.");
        }
        if(!this.roles.contains(role)) {
            throw new IllegalArgumentException("Role does not exist in the list of roles.");
        }
        this.roles.remove(role);
    }

    public void addTraining(Training training) {
        if(this.trainings.contains(training)) {
            throw new IllegalArgumentException("Training already exists in the list of trainings.");
        }
        if(training == null) {
            throw new IllegalArgumentException("Training cannot be null.");
        }
        if(this.trainings.contains(training)) {
            throw new IllegalArgumentException("Training already exists in the list of trainings.");
        }
        this.trainings.add(training);
    }

    public void removeTraining(Training training) {
        if(training == null) {
            throw new IllegalArgumentException("Training cannot be null.");
        }
        if(!this.trainings.contains(training)) {
            throw new IllegalArgumentException("Training does not exist in the list of trainings.");
        }
        this.trainings.remove(training);
    }
    

    public void changeRole(Role oldRole, Role newRole) {
        if(this.roles.contains(newRole)) {
            throw new IllegalArgumentException("Role already exists in the list of roles.");
        }
        if(oldRole == null || newRole == null) {
            throw new IllegalArgumentException("Old Role or New Role cannot be null.");
        }
        if(!this.roles.contains(oldRole)) {
            throw new IllegalArgumentException("Old Role does not exist in the list of roles.");
        }
        this.roles.add(newRole);
        this.roles.remove(oldRole);
    }

    public void addPreferredShift(Shift shift) {
        if(this.preferredShifts.contains(shift)) {
            throw new IllegalArgumentException("Shift already exists in the list of preferred shifts.");
        }
        if(shift == null) {
            throw new IllegalArgumentException("Shift cannot be null.");
        }
        this.preferredShifts.add(shift);
    }

    public void removePreferredShift(Shift shift) {
        if(!this.preferredShifts.contains(shift)) {
            throw new IllegalArgumentException("Shift doesnt exists in the list of preferred shifts.");
        }
        if(shift == null) {
            throw new IllegalArgumentException("Shift cannot be null.");
        }
        this.preferredShifts.remove(shift);
    }

    public boolean isAvailable(Shift shift) {
        if(isFinishWorking() || this.preferredShifts == null) {
            return false;
        }
        if(shift == null) {
            throw new IllegalArgumentException("Shift cannot be null.");
        }
        return this.preferredShifts.contains(shift);
    }
    
    public boolean isShiftManager(){
        return this.trainings.contains(Training.TeamManagement)&&trainings.contains(Training.CancellationCard);
    }

}
