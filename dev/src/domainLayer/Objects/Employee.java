import java.util.Date;

public class Employee {
    private int id;
    private String name;
    private String bankAccount;
    private int salary;
    private Date startDate;
    private int vacationDays;
    private int sickDays;
    private String username; 
    private String password; 
    protected boolean finishWorking;
    private boolean isLoggedIn=false;

    public Employee(int id, String name, String bankAccount, int salary, Date startDate, int vacationDays, int sickDays, String username, String password) {
        this.id = id;
        this.name = name;
        this.bankAccount = bankAccount;
        this.salary = salary;
        this.startDate = startDate;
        this.vacationDays = vacationDays;
        this.sickDays = sickDays;
        this.username = username;
        this.password = password;
        this.finishWorking = false; 
    }

    // Getters and Setters
    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public String getBankAccount() {
        return bankAccount;
    }
    public void setBankAccount(String bankAccount) {
        this.bankAccount = bankAccount;
    }
    public int getSalary() {
        return salary;
    }
    public void setSalary(int salary) {
        this.salary = salary;
    }
    public Date getStartDate() {
        return startDate;
    }
    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }
    public int getVacationDays() {
        return vacationDays;
    }
    public void setVacationDays(int vacationDays) {
        this.vacationDays = vacationDays;
    }
    public int getSickDays() {
        return sickDays;
    }
    public void setSickDays(int sickDays) {
        this.sickDays = sickDays;
    }
    public String getUsername() {
        return username;
    }
    public void setUsername(String username) {
        this.username = username;
    }
    public String getPassword() {
        return password;
    }
    public void setPassword(String password) {
        this.password = password;
    }
    public boolean isFinishWorking() {
        return finishWorking;
    }
    public void setFinishWorking(boolean finishWorking) {
        this.finishWorking = finishWorking;
    }

}
