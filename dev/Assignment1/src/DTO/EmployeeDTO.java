package DTO;

public class EmployeeDTO {
    private int id;
    private String name;
    private String branch;
    private String bankAccount;
    private int salary;
    private String startDate;
    private int vacationDays;
    private int sickDays;
    private double educationFund;
    private double socialBenefits;
    private String password;

    public EmployeeDTO(int id, String name, String branch, String bankAccount, int salary, String startDate,
                       int vacationDays, int sickDays, double educationFund, double socialBenefits,
                       String password) {
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
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getBranch() {
        return branch;
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

}
