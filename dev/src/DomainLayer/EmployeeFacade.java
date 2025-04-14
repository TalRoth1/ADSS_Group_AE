package DomainLayer;

import java.util.HashMap;
import java.util.Map;

public class EmployeeFacade {
    private Map<Integer, EmployeeManager> employeeManagers;
    private Map<Integer, ShiftEmployee> shiftEmployees;

    public EmployeeFacade() {
        this.employeeManagers = new HashMap<>();
        this.shiftEmployees = new HashMap<>();
    }
    public Employee login(int id, String password) {
        Employee e = getEmployee(id);
        if(e != null && e.login(password))
            return e;
        return null;
    }

    private Employee getEmployee(int id) {
        Employee employee = employeeManagers.get(id);
        if(employee == null)
            employee = shiftEmployees.get(id);
        return employee;
    }

    public String fireEmployee(int employeeId, int empManagerId) {
        if(!isLoggedIn(empManagerId))
            return "You are not logged in";
        if(shiftEmployees.containsKey(employeeId)) {
            EmployeeManager employeeManager = getEmployeeManager(empManagerId);
            return employeeManager.fireEmployee(employeeId);
        }
        else
            return "Can't fire employee: No employee found with ID " + employeeId;
    }

    private boolean isLoggedIn(int id) {
        return getEmployee(id).isLoggedIn();
    }

    private EmployeeManager getEmployeeManager(int id) {
        return employeeManagers.get(id);
    }
}
