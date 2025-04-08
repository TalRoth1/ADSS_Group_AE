import java.util.Date;
import java.util.List;

public class ShiftEmployee extends Employee {
    private List<Shift> preferedShifts;
    private List<Role> roles;

    public ShiftEmployee(int id, String name, String bankAccount, int salary, Date startDate, int vacationDays, int sickDays, String username, String password) {
        super(id, name, bankAccount, salary, startDate, vacationDays, sickDays, username, password);
        this.preferedShifts = null;
        this.roles = null;
    }
    public List<Shift> getPreferedShifts() {
        return preferedShifts;
    }
    public void setPreferedShifts(List<Shift> preferedShifts) {
        this.preferedShifts = preferedShifts;
    }
    public List<Role> getRoles() {
        return roles;
    }



    //methods
    public void addRole(Role role) {
        this.roles.add(role);
    }   
    public void addPreferedShift(Shift shift) {
        this.preferedShifts.add(shift);
    }
    public void removePreferedShift(Shift shift) {
        this.preferedShifts.remove(shift);
    }
    public void removeRole(Role role) {
        this.roles.remove(role);
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
    

}
