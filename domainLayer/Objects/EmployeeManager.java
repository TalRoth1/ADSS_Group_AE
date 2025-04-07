import java.io.Serializable;
import java.util.Date;

public class EmployeeManager extends Employee implements Serializable {

    public EmployeeManager(int id, String name, String bankAccount, int salary, Date startDate, int vacationDays, int sickDays, String username, String password) {
        super(id, name, bankAccount, salary, startDate, vacationDays, sickDays, username, password);
        this.jobType = Role.EMPLOYEE_MANAGER;

    }
}