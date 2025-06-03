package DTO;

import java.util.Date;
import java.util.List;
import java.util.Map;

import DomainLayer.Shift;

public class EmployeeDTO {

    private int id;
    private String name;
    private LocationDTO branch;
    private String bankAccount;
    private int salary;
    private Date startDate;
    private int vacationDays;
    private int sickDays;
    private float educationFund;
    private float socialBenefits;
    private String password;
    private boolean isFinishedWorking;
    private boolean isLoggedIn;
    private List<ShiftDTO> prefShifts;
    private Map<ShiftDTO, String> assignedShifts; // string is the role of the employee in the shift
    private List<String> roles;

    public EmployeeDTO(int id, String name, LocationDTO branch, String bankAccount, int salary, Date startDate,
            int vacationDays, int sickDays, float educationFund, float socialBenefits,
            String password, boolean isFinishedWorking, List<ShiftDTO> prefShifts,
            Map<ShiftDTO, String> assignedShifts,List<String> roles) {
        this.id = id;
        this.name = name;
        this.branch = branch;
        this.bankAccount = bankAccount;
        this.salary = salary;
        this.startDate = startDate;
        this.vacationDays = vacationDays;
        this.sickDays = sickDays;
        this.educationFund = educationFund;
        this.socialBenefits = socialBenefits;
        this.password = password;
        this.isFinishedWorking = isFinishedWorking;
        this.isLoggedIn = false;
        this.roles = roles;
        this.assignedShifts = assignedShifts;
        this.prefShifts = prefShifts;
    }

    

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public LocationDTO getBranch() {
        return branch;
    }

    public String getBankAccount() {
        return bankAccount;
    }

    public int getSalary() {
        return salary;
    }

    public Date getStartDate() {
        return startDate;
    }

    public int getVacationDays() {
        return vacationDays;
    }

    public int getSickDays() {
        return sickDays;
    }

    public float getEducationFund() {
        return educationFund;
    }

    public float getSocialBenefits() {
        return socialBenefits;
    }

    public String getPassword() {
        return password;
    }

    public boolean isFinishedWorking() {
        return isFinishedWorking;
    }

    public void setFinishedWorking() {
        isFinishedWorking = true;
    }

    public boolean isLoggedIn() {
        return isLoggedIn;
    }

    public void setLoggedIn() {
        isLoggedIn = true;
    }

    public void setRoles(List<String> roles) {
        this.roles = roles;
    }

    public List<String> getRoles() {
        return roles;
    }

    public Map<ShiftDTO, String> getAssignedShifts() {
        return assignedShifts;
    }

    public List<ShiftDTO> getPrefShifts() {
        return prefShifts;
    }

}
