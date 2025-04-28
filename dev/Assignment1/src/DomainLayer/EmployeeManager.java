package DomainLayer;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;
//import DomainLayer.Training;



public class EmployeeManager extends Employee{

    private Map<Integer, ShiftEmployee> allEmployees;
    private Map<LocalDate, Shift> morningShifts;
    private Map<LocalDate, Shift> eveningShifts;
    private Map<LocalDate, Shift> pastShifts;
    
    public EmployeeManager(int id, String name,String branch, String bankAccount, int salary, LocalDate startDate,
                           int vacationDays, int sickDays, double educationFund, double socialBenefits,
                           String password) {
        super(id, name,branch, bankAccount, salary, startDate, vacationDays, sickDays, educationFund,
                socialBenefits, password); 
        allEmployees = new HashMap<>();
        morningShifts = new HashMap<>();
        eveningShifts = new HashMap<>();
        pastShifts = new HashMap<>();
    }

    //methods 
    public String removeEmployee(int id) { //delete employee from the system, currently not used
        allEmployees.remove(id);
        return null;
    }

     public Shift getShift(LocalDate date, ShiftType shiftType) {
        if (shiftType == ShiftType.MORNING) 
            return morningShifts.get(date);
        return eveningShifts.get(date);
     }

    public boolean checkEmployee(int id) { 
        return allEmployees.containsKey(id);
    }

    //update methods
    public String updateBankAccountEmployee(int employeeId, String bankAccount) {
        if(!checkEmployee(employeeId))
            return employeeId + " not exist";
        ShiftEmployee employee = allEmployees.get(employeeId);   
        employee.setBankAccount(bankAccount);
        return null;
    }

    public String updateSalaryEmployee(int employeeId, int salary) {
        if(!checkEmployee(employeeId))
            return employeeId + " not exist";
        if(salary < 0)
            return "invalid salary";
        ShiftEmployee employee = allEmployees.get(employeeId);   
        employee.setSalary(salary);
        return null;
    }

    public String updateVacationDaysEmployee(int employeeId, int vacationDays) {
        if(!checkEmployee(employeeId))
            return employeeId + " not exist";
        if(vacationDays < 0)
            return "invalid vacationDays";
        ShiftEmployee employee = allEmployees.get(employeeId);   
        employee.setVacationDays(vacationDays);
        return null;
    }

    public String updateSickDaysEmployee(int employeeId, int sickDays) {
        if (!checkEmployee(employeeId))
            return employeeId + " not exist";
        if(sickDays < 0)
            return "invalid sickDays";
        ShiftEmployee employee = allEmployees.get(employeeId);   
        employee.setSickDays(sickDays);
        return null;
    }

    public String updateEducationFund(int employeeId, double educationFund) {
        if (!checkEmployee(employeeId))
            return employeeId + " not exist";
        if(educationFund < 0)
            return "invalid educationFund";
        ShiftEmployee employee = allEmployees.get(employeeId);
        employee.setEducationFund(educationFund);
        return null;
    }

    public String updateSocialBenefits(int employeeId, double socialBenefits) {
        if (!checkEmployee(employeeId))
            return employeeId + " not exist";
        if(socialBenefits < 0)
            return "invalid educationFund";
        ShiftEmployee employee = allEmployees.get(employeeId);
        employee.setSocialBenefits(socialBenefits);
        return null;
    }

    //methods for employees
    public String fireEmployee(int id){
        if (!checkEmployee(id))
            return id + " not exist";
        ShiftEmployee employee = allEmployees.get(id);
        if (employee.isFinishWorking())
            return "Employee already fired";
        employee.setFinishWorking(true);
        for (Shift shift : employee.getAssignedShifts().keySet()) {
            shift.removeEmployee(id);
        }
        employee.setPrefShifts(null);
        employee.setAssignedShifts(null);
        return "Employee: " + id + " fired successfully";
    }

    public ShiftEmployee hireEmployee(int employeeId, String employeeName,String branch, String bankAccount, int salary,
        LocalDate startDate, int vacationDays, int sickDays,double educationFund, double socialBenefits,String employeePassword, Role role){
        ShiftEmployee shiftEmployee = new ShiftEmployee(employeeId, employeeName,branch, bankAccount, salary,
                startDate, vacationDays, sickDays, educationFund, socialBenefits, employeePassword, role);
        allEmployees.put(employeeId, shiftEmployee);
        return shiftEmployee;
    }

    public String addRoleToEmployee(int employeeID, Role role){
        if (!checkEmployee(employeeID))
            return employeeID + " doesn't exist";

        ShiftEmployee employee = allEmployees.get(employeeID);
        if (employee.isFinishWorking())
            return "Employee is fired, you can't add him a role";
        return employee.addRole(role);
    }

    public String changeRoleToEmployee(int employeeID, Role oldRole, Role newRole){
        if (!checkEmployee(employeeID))
            return employeeID + " doesn't exist";
        ShiftEmployee employee = allEmployees.get(employeeID);
        if (employee.isFinishWorking())
            return "Employee is fired, you can't change him a role";
        return employee.changeRole(oldRole, newRole);
    }

    public String deleteRoleFromEmployee(int employeeID, Role role){
        if (!checkEmployee(employeeID))
            return employeeID + " doesn't exist";
        ShiftEmployee employee = allEmployees.get(employeeID);
        if (employee.isFinishWorking())
            return "Employee is fired, you can't delete a role from him";
        return employee.removeRole(role);
    }

    public String addTrainingToEmployee(int employeeID, Training training){
        if (!checkEmployee(employeeID))
            return employeeID + " doesn't exist";
        ShiftEmployee employee = allEmployees.get(employeeID);
        return employee.addTraining(training);
    }

    public String removeTrainingFromEmployee(int employeeID, Training training){
        if (!checkEmployee(employeeID))
            return employeeID + " doesn't exist";
        ShiftEmployee employee = allEmployees.get(employeeID);
        return employee.removeTraining(training);
    }

    public String changeShiftManager(Shift shift, int oldManager, int newManager) { //switch the shift manager to another employee
        ShiftEmployee oldManagerE = allEmployees.get(oldManager);
        ShiftEmployee newManagerE = allEmployees.get(newManager);
        if (!checkEmployee(newManager) || !checkEmployee(oldManager)) {
            return "manager not found in the system";
        }
        if(!newManagerE.getRoles().contains(Role.SHIFT_MANAGER)) {
            return "this employee cant be shift Manager";
        }
        if(shift.getAssignedEmployeesID().containsKey(newManager)) {
            return "new manager is already assigned to this shift, remove him first if you want to put him as the shift manager";
        }
        if(!shift.getAssignedEmployeesID().containsKey(oldManager)) {
            return "old manager is not assigned to this shift";
        }
        if(shift.getShiftManagerId() != oldManager) {
            return "old manager is not the shift manager of this shift";
        }
        shift.setShiftManagerId(newManager);
        oldManagerE.removeAssignedShift(shift);
        newManagerE.addAssignedShift(shift, Role.SHIFT_MANAGER);
        shift.removeEmployee(oldManager);
        shift.addEmployee(newManager, Role.SHIFT_MANAGER);
        return null;
    }

    public String shiftReplacement(Shift shift, int empID, int replacementID) { //replace employee in the shift with another employee
        if (!checkEmployee(empID) || !checkEmployee(replacementID) ) {
            return "employee not found in the system";
        }
        if(replacementID == empID) {
            return "replacement employee cannot be the same as the original employee";
        }
        if (!shift.getAssignedEmployeesID().containsKey(empID)) {
            return "original employee not assigned to this shift";
        }
        if (shift.getAssignedEmployeesID().containsKey(replacementID)) {
            return "replacement employee already assigned to this shift";
        }
        ShiftEmployee employee = allEmployees.get(empID);
        ShiftEmployee replacement = allEmployees.get(replacementID);
        Role role = shift.getAssignedEmployeesID().get(empID);
        if (employee.isFinishWorking() || replacement.isFinishWorking()) {
            return "this employee is fired";
        }
        if(shift.getShiftManagerId() == empID || shift.getShiftManagerId() == replacementID) {
            return "shift manager cannot be replaced, there is a different option to change the shift manager";
        }
        if(!replacement.getRoles().contains(shift.getAssignedEmployeesID().get(empID))){
            return "replacement employee does not have the same role as the original employee";
        }
           shift.removeEmployee(empID);
           employee.removeAssignedShift(shift);
           shift.addEmployee(replacementID, role);
           replacement.addAssignedShift(shift, role);

        if (shift.getShiftType() == ShiftType.EVENING) {
            eveningShifts.put(shift.getDate(), shift);
        } else {
            morningShifts.put(shift.getDate(), shift);
        }
           return null;
    }

    public String createDefaultShift(LocalDate date, ShiftType shiftType) {
        if (date == null || shiftType == null) {
            return "invalid date or shift type";
        }
        if (shiftType == ShiftType.MORNING && morningShifts.containsKey(date)) {
            return "morning shift already exists for this date";
        } else if (shiftType == ShiftType.EVENING && eveningShifts.containsKey(date)) {
            return "evening shift already exists for this date";
        }
        Shift shift = new Shift(date, shiftType, -1);
        if (shiftType == ShiftType.MORNING) {
            morningShifts.put(date, shift);
        } else if (shiftType == ShiftType.EVENING) {
            eveningShifts.put(date, shift);
        }
        return null;
    }

    public String createShift(LocalDate date, ShiftType shiftType, int shiftManagerId) { //create a new shift, definig the date, type and shift manager
        if (date == null || shiftType == null) {
            return "invalid date or shift type";
        }
        if(!checkEmployee(shiftManagerId)) {
            return "shift manager not found in the system.";
        }
        if (!allEmployees.get(shiftManagerId).getRoles().contains(Role.SHIFT_MANAGER)) {
            return "this employee can't be a shift manager";
        }
        
        if (shiftType == ShiftType.MORNING && morningShifts.containsKey(date)) {
            return "morning shift already exists for this date";
        } else if (shiftType == ShiftType.EVENING && eveningShifts.containsKey(date)) {
            return "evening shift already exists for this date";
        }
        Shift shift = new Shift(date, shiftType, shiftManagerId);
        if (shiftType == ShiftType.MORNING) {
            morningShifts.put(date, shift);
        } else if (shiftType == ShiftType.EVENING) {
            eveningShifts.put(date, shift);
        }
        pastShifts.put(date, shift);
        return null;
    }

    public String addEmployeeToShift(int id, Shift shift, Role role){ 
        if (!checkEmployee(id)) {
            return "employee not exist";
        }
        ShiftEmployee employee = allEmployees.get(id);
        if (employee.isFinishWorking()){
            return "this employee is fired";
        }
        if (!employee.getRoles().contains(role)) {
            return "this employee does not have this role";
        }
        // if (!employee.isAvailable(shift)) {
        //     return "this employee is not available for this shift";
        // }
        if (shift.getAssignedEmployeesID().containsKey(id)) {
            return "this employee is already assigned to this shift";
        }
        String response = shift.addEmployee(id, role);
        if(response != null) {
            return response;
        }
        //return employee.addAssignedShift(shift, role);
        String res = employee.addAssignedShift(shift, role);
        if(res != null) {
            return res;
        }
        if(shift.getShiftType() == ShiftType.MORNING) {
            morningShifts.put(shift.getDate(), shift);
        }
        else if(shift.getShiftType() == ShiftType.EVENING) {
            eveningShifts.put(shift.getDate(), shift);
        }
        return null;
    }

    public String removeEmployeeFromShift(int id, Shift shift) {
        if (!checkEmployee(id)) {
            return "employee not exist";
        }
        ShiftEmployee employee = allEmployees.get(id);
        if (employee.isFinishWorking()){
            return "this employee is fired";
        }
        if (!shift.getAssignedEmployeesID().containsKey(id)) {
            return "this employee is not assigned to this shift";
        }
        shift.removeEmployee(id);
        employee.removeAssignedShift(shift);
        return null;
    }

    //getters and setters
    public String setTimes(Shift shift, int start, int end) { //set start time and end time of the shift
        if (start < 0 || end < 0 || start >= end){
            return "invalid start or end time";
        }
        if(shift == null){
            return "shift not exist";
        }
        if((shift.getShiftType() == ShiftType.MORNING && (end > 1400 || start < 600)) || shift.getShiftType() == ShiftType.EVENING && (start < 1400 || end > 2200)) {
            return "invalid start or end time for this shift type";
        }
        shift.setStartTime(start);
        shift.setEndTime(end);
        //אולי צריך לשנות גם את השעה פה
        return null;
    }

    public void setRequiredRole(Shift shift, Role role, int numOfEmployees) { //set the amount of employess of this specific role for the shift
        if(shift != null)
            shift.setRequiredRoles(role, numOfEmployees);
    }

    public String setShiftManager(Shift shift, int id){
        if(!checkEmployee(id) ){
            return "employee not exist";
        }
        if(allEmployees.get(id).isFinishWorking()){
            return "this employee is fired";
        }
        if(!allEmployees.get(id).getRoles().contains(Role.SHIFT_MANAGER)) {
            return "this employee cant be shift Manager";
        }
        if(shift.getShiftManagerId() == id) {
            return "this employee is already the shift manager";
        }
        if(shift.getAssignedEmployeesID().containsKey(id)) {
            return "this employee is already assigned to this shift";
        }   
        shift.setShiftManagerId(id);
        return null;
    }

    public String getPrefAllEmployees(){ //all the shifts of all employees
        StringBuilder sb = new StringBuilder();
        for (ShiftEmployee e : allEmployees.values()) {
            if (!e.isFinishWorking()) {
                sb.append(e.getName()).append(" ").append(e.getId()).append(" Roles: ").append(e.getRoles())
                .append("\n next week shiftspref:\n").append(e.getPrefShifts()).append("\n");
            }
        }
        return sb.toString();
    }

  

    
    public String getAssignedEmployeeShiftsManager(int employeeID) { //all the assigned shifts of the employee
        if (!checkEmployee(employeeID)) {
            return "employee not exist";
        }
        ShiftEmployee employee = allEmployees.get(employeeID);
        String morning="Morning Shifts:\n";
        String evening="Evening Shifts:\n";
        for(LocalDate d : morningShifts.keySet()){
            Shift s = morningShifts.get(d);
            if(s.getAssignedEmployeesID().containsKey(employeeID)){
                morning += s.getDate() +", "+"Role: " + s.getRole(employeeID) + "\n";
            }
        }
        for(LocalDate d : eveningShifts.keySet()){
            Shift s = eveningShifts.get(d);
            if(s.getAssignedEmployeesID().containsKey(employeeID)){
                evening += s.getDate() + ", "+"Role: " + s.getRole(employeeID) + "\n";
            }
        }
        return "Employee: " + employee.getName() + " "+ employeeID + "\n" + morning + "\n" + evening;
    }

    public String getAvailableEmployees(Shift shift, Role role) { //all the employees that can work in this shift and have this role
        String res = "Available employees for this shift and role: " + role.toString() + "\n";
        for (ShiftEmployee employee : allEmployees.values()) {
            if (employee.getRoles().contains(role) && employee.isAvailable(shift)) {
                    res += employee.getName() + " " + employee.getId() + "\n";
            }
        }
        return res;
    }

    public void archiveWeeklyForAllEmployees() {
        LocalDate today = LocalDate.now(); 
        for (ShiftEmployee employee : allEmployees.values()) {
            employee.archiveOldShiftsWeekly(today);
        }
    }

    public ShiftEmployee getEmployee(int id) {
        return allEmployees.get(id);
    }

    public Map<LocalDate, Shift> getPastShifts() {
        return pastShifts;
    }

    public void addEmployee(ShiftEmployee employee) {
        allEmployees.put(employee.getId(), employee);
    }

}