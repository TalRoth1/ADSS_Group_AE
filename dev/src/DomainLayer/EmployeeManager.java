package DomainLayer;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class EmployeeManager extends Employee{

    private Map<Integer, ShiftEmployee> allEmployees;
    private Map<Date, Shift> morningShifts;
    private Map<Date, Shift> eveningShifts;
    
    public EmployeeManager(int id, String name, String bankAccount, int salary, Date startDate, int vacationDays, int sickDays, String username, String password) {
        super(id, name, bankAccount, salary, startDate, vacationDays, sickDays, username, password);
        allEmployees = new HashMap<>();
    }

    //methods 
    public void addEmployee(ShiftEmployee employee) {
        allEmployees.put(employee.getId(), employee);
    }

    public void addEmployee(int id, String name, String bankAccount, int salary, Date startDate, int vacationDays, int sickDays, String username, String password) {
        ShiftEmployee employee = new ShiftEmployee(id, name, bankAccount, salary, startDate, vacationDays, sickDays, username, password);
        allEmployees.put(id, employee);
    }

    public void removeEmployee(int id) {
        allEmployees.remove(id);
    }

    public boolean checkEmployee(int id) {
        return allEmployees.containsKey(id);
    }

    public void updateBankAccountEmployee(int employeeId, String bankAccount) {
        if (!checkEmployee(employeeId)) {
            throw new IllegalArgumentException("Employee not found");
        }
        ShiftEmployee employee = allEmployees.get(employeeId);   
        employee.setBankAccount(bankAccount);
    }

    public void updateSalaryEmployee(int employeeId, int salary) {
        if (!checkEmployee(employeeId)) {
            throw new IllegalArgumentException("Employee not found");
        }
        if(salary<0)
            throw new IllegalArgumentException("invalid salary");
        ShiftEmployee employee = allEmployees.get(employeeId);   
        employee.setSalary(salary);
    }

    public void updateVacationDaysEmployee(int employeeId, int vacationDays) {
        if (!checkEmployee(employeeId)) {
            throw new IllegalArgumentException("Employee not found");
        }
        if(vacationDays<0)
            throw new IllegalArgumentException("invalid vacationDays");
        ShiftEmployee employee = allEmployees.get(employeeId);   
        employee.setVacationDays(vacationDays);
    }

    public void updateSickDaysEmployee(int employeeId, int sickDays) {
        if (!checkEmployee(employeeId)) {
            throw new IllegalArgumentException("Employee not found");
        }
        if(sickDays<0)
            throw new IllegalArgumentException("invalid sickDays");
        ShiftEmployee employee = allEmployees.get(employeeId);   
        employee.setSickDays(sickDays);
    }

    public void fireEmployee(int id){
        if (!checkEmployee(id))
            throw new IllegalArgumentException("Employee not found");
        ShiftEmployee employee = allEmployees.get(id);
        if (employee.isFinishWorking())
            throw new IllegalArgumentException("Employee already fired");
        employee.setFinishWorking(true);
    }

    public void addRoleToEmployee(int employeeID, Role role){
        if (!checkEmployee(employeeID)){
            throw new IllegalArgumentException("employee not exist");
        }
        if (role==null)
            throw new IllegalArgumentException("invalid role");
        ShiftEmployee employee = allEmployees.get(employeeID);
        employee.addRole(role);
    }

    public void changeRoleToEmployee(int employeeID, Role oldRole, Role newRole){
        if (!checkEmployee(employeeID)){
            throw new IllegalArgumentException("employee not exist");
        }
        ShiftEmployee employee = allEmployees.get(employeeID);
        employee.changeRole(oldRole, newRole);
    }

    public void deleteRoleFromEmployee(int employeeID, Role role){
        if (!checkEmployee(employeeID)){
            throw new IllegalArgumentException("employee not exist");
        }
        ShiftEmployee employee = allEmployees.get(employeeID);
           employee.removeRole(role);
    }

    public void addTrainingToEmployee(int employeeID, Training training){
        if (!checkEmployee(employeeID)){
            throw new IllegalArgumentException("employee not exist");
        }
        ShiftEmployee employee = allEmployees.get(employeeID);
        employee.addTraining(training);
    }

    public void removeTrainingFromEmployee(int employeeID, Training training){
        if (!checkEmployee(employeeID)){
            throw new IllegalArgumentException("employee not exist");
        }
        ShiftEmployee employee = allEmployees.get(employeeID);
        employee.removeTraining(training);
    }

    public List<Employee> getAvailableEmployees(Shift shift, Role role) {
        List<Employee> availableEmployees = new ArrayList<>();
        for (ShiftEmployee employee : allEmployees.values()) {
            if (employee.isAvailable(shift)) {
                if (!availableEmployees.contains(role)) {
                    availableEmployees.add(employee);
                }
            }
        }
        return availableEmployees;
    }

    public void changeShiftManager(Shift shift, int oldManager, int newManager) {
        if (!allEmployees.containsKey(newManager) || !allEmployees.containsKey(oldManager) ) {
            throw new IllegalArgumentException("manager not found in the system.");
        }
        if(allEmployees.get(newManager).getRoles().contains(Role.SHIFT_MANAGER)) {
            throw new IllegalArgumentException("this employee cant be shift Manager");
        }
        shift.setShiftManagerId(newManager);
    }

    public String shiftReplacement(Shift shift, int empID, int replacementID) {
        if (!allEmployees.containsKey(empID) || !allEmployees.containsKey(replacementID)) {
            throw new IllegalArgumentException("employee not found in the system.");
        }
        if(replacementID == empID) {
            throw new IllegalArgumentException("replacement employee cannot be the same as the original employee.");
        }
        if (!shift.getAssignedEmployeesID().containsKey(empID)) {
            throw new IllegalArgumentException("employee not assigned to this shift.");
        }
        if (shift.getAssignedEmployeesID().containsKey(replacementID)) {
            throw new IllegalArgumentException("replacement employee already assigned to this shift.");
        }
        ShiftEmployee employee = allEmployees.get(empID);
        ShiftEmployee replacement = allEmployees.get(replacementID);
        if(!replacement.getPreferredShifts().contains(shift)) {
            throw new IllegalArgumentException("replacement employee does not have this shift in their preferred shifts.");
        }
        if(replacement.getRoles().contains(shift.getAssignedEmployeesID().get(empID))){
            throw new IllegalArgumentException("replacement employee does not have the same role as the original employee.");
        }
           shift.removeEmployee(empID);
           shift.addEmployee(replacementID, shift.getAssignedEmployeesID().get(empID));
            return "Shift replacement successful: " + employee.getName() + " has been replaced by " + replacement.getName() + " for shift " + shift.getShiftString();
    }




    //getters and setters
    public void setRequiredRoles(Shift shift, Role role, int numOfEmployees) {
        if(shift != null)
        shift.setRequiredRoles(role, numOfEmployees);
    }

    public void setShiftManager(Shift shift, int id){
        shift.setShiftManagerId(id);
    }

    public String getPrefAllemployees(){
        String s = "" ;
        for (ShiftEmployee e : allEmployees.values()){
            s = s + e.getName() + " " + e.getId() + " Roles: " + e.getRoles() +
                   //"\n this week shifts : " +"\n" + e.getLastPref(0) +
                    "\n next week shiftspref : " +"\n"+ e.getPreferredShifts() +"\n" ;
        }
        return s;
    }

    public String getPrefEmployee(int id){
        if (!checkEmployee(id)){
            throw new IllegalArgumentException("employee not exist");
        }
        ShiftEmployee employee = allEmployees.get(id);
        return employee.getName() + " " + employee.getId() + " Roles: " + employee.getRoles() +
                "\n next week shifts pref: " +"\n"+ employee.getPreferredShifts() +"\n" ;
    }

    
    public String getEmployeeShifts(int employeeID) {
        if (!checkEmployee(employeeID)) {
            throw new IllegalArgumentException("employee not exist");
        }
        ShiftEmployee employee = allEmployees.get(employeeID);
        String morning="Morning Shifts:\n";
        String evening="Evening Shifts:\n";
        for(Date d : morningShifts.keySet()){
            Shift s = morningShifts.get(d);
            if(s.getAssignedEmployeesID().containsKey(employeeID)){
                morning += s.getDate() +", "+"Role: " + s.getRole(employeeID) + "\n";
            }
        }
        for(Date d : eveningShifts.keySet()){
            Shift s = eveningShifts.get(d);
            if(s.getAssignedEmployeesID().containsKey(employeeID)){
                evening += s.getDate() + ", "+"Role: " + s.getRole(employeeID) + "\n";
            }
        }
        return "Employee: " + employee.getName() + " "+ employeeID + "\n" + morning + "\n" + evening;
    }


//set shifts, facade, emp choose pref
}