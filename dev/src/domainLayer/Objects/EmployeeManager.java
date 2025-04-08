
import java.util.Date;
import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap;


public class EmployeeManager extends Employee{

    private Map<Integer, ShiftEmployee> allEmployees;
    public EmployeeManager(int id, String name, String bankAccount, int salary, Date startDate, int vacationDays, int sickDays, String username, String password) {
        super(id, name, bankAccount, salary, startDate, vacationDays, sickDays, username, password);
        allEmployees = new HashMap<>();
    }
    

    //methods 
    public void addEmployee(ShiftEmployee employee) {
        allEmployees.put(employee.getId(), employee);
    }

    public void removeEmployee(ShiftEmployee employee) {
        allEmployees.remove(employee);
    }

    public boolean checkEmployee(int id) {
        return allEmployees.containsKey(id);
    }

    public void updateBankAcountEmployee(int employeeId, String bankAccount) {
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
        ShiftEmployee employee = allEmployees.get(employeeId);   
        employee.setSalary(salary);
    }

    public void updateVacationDaysEmployee(int employeeId, int vacationDays) {
        if (!checkEmployee(employeeId)) {
            throw new IllegalArgumentException("Employee not found");
        }
        ShiftEmployee employee = allEmployees.get(employeeId);   
        employee.setVacationDays(vacationDays);
    }

    public void updateSickDaysEmployee(int employeeId, int sickDays) {
        if (!checkEmployee(employeeId)) {
            throw new IllegalArgumentException("Employee not found");
        }
        ShiftEmployee employee = allEmployees.get(employeeId);   
        employee.setSickDays(sickDays);
    }

    public void addEmployee(int id, String name, String bankAccount, int salary, Date startDate, int vacationDays, int sickDays, String username, String password) {
        ShiftEmployee employee = new ShiftEmployee(id, name, bankAccount, salary, startDate, vacationDays, sickDays, username, password);
        allEmployees.put(id, employee);
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


    
}