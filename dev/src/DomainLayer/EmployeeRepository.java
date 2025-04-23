package DomainLayer;
import java.util.ArrayList;
import java.util.List;


public class EmployeeRepository {
    private List<Employee> employees = new ArrayList<>();

    public void addEmployee(Employee e) {
        employees.add(e);
    }

    public List<Employee> getAllEmployees() {
        return employees;
    }

    // אפשר גם getById או חיפוש לפי שם וכו'
}