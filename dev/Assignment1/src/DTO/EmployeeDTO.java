package DTO;

import DomainLayer.Role;
import DomainLayer.Shift;
import java.util.List;

public class EmployeeDTO {

    private int id;
    private String name;
    private int branchid;
    private String bankAccount;
    private int salary;
    private String startDate;
    private int vacationDays;
    private int sickDays;
    private double educationFund;
    private double socialBenefits;
    private String password;
    private boolean isFinishedWorking;
    private boolean isLoggedIn;
    private List<Role> roles;
    private List<Shift> shifts;
    private List<Shift> prefShifts;

    public EmployeeDTO(int id, String name, int branchid, String bankAccount, int salary, String startDate,
            int vacationDays, int sickDays, double educationFund, double socialBenefits,
            String password, boolean isFinishedWorking, List<Role> roles, List<Shift> shifts, List<Shift> prefShifts) {
        this.id = id;
        this.name = name;
        this.branchid = branchid;
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
        this.shifts = shifts;
        this.prefShifts = prefShifts;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public int getBranchid() {
        return branchid;
    }

    public String getBankAccount() {
        return bankAccount;
    }

    public int getSalary() {
        return salary;
    }

    public String getStartDate() {
        return startDate;
    }

    public int getVacationDays() {
        return vacationDays;
    }

    public int getSickDays() {
        return sickDays;
    }

    public double getEducationFund() {
        return educationFund;
    }

    public double getSocialBenefits() {
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

    public void setRoles(List<Role> roles) {
        this.roles = roles;
    }

}
