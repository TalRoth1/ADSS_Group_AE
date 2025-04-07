import java.util.List;
import java.util.Date;

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
    public void setRoles(List<Role> roles) {
        this.roles = roles;
    }   
    public void addPreferedShift(Shift shift) {
        this.preferedShifts.add(shift);
    }
    public void removePreferedShift(Shift shift) {
        this.preferedShifts.remove(shift);
    }
    public void addRole(Role role) {
        this.roles.add(role);
    }
    public void removeRole(Role role) {
        this.roles.remove(role);
    }
    

}
