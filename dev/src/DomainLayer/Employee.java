package DomainLayer;

import java.time.LocalDate;

public class Employee {
    private int id;
    private String name;
    private String bankAccount;
    private int salary;
    private LocalDate startDate;
    private int vacationDays;
    private int sickDays;
    private double educationFund; //monthly amount
    private double socialBenefits;//monthly amount
    private String password; 
    protected boolean finishWorking; //is fired 
    private boolean isLoggedIn = false;



    public Employee(int id, String name, String bankAccount, int salary, LocalDate startDate,
                    int vacationDays, int sickDays, double educationFund, double socialBenefits,
                    String password) {
        this.id = id;
        this.name = name;
        this.bankAccount = bankAccount;
        this.salary = salary;
        this.startDate = startDate;
        this.vacationDays = vacationDays;
        this.sickDays = sickDays;
        this.educationFund = educationFund;
        this.socialBenefits = socialBenefits;
        this.password = password;
        this.finishWorking = false; 
    }

    public enum Training {
        TeamManagement,
        CancellationCard
    }

    public boolean login(String password) {
        if (this.password.equals(password)) {
            this.isLoggedIn = true;
            return true;
        } else {
            return false;
        }
    }

    public void logout() { this.isLoggedIn = false;
    }

    public String updatePassword(String oldPassword, String newPassword) {
        if (this.password.equals(oldPassword)) {
            this.password = newPassword;
            return null;
        } else {
            return "Old password is incorrect.";
        }
    }

    public void setTraining(Training training) {
        
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
    public void setBankAccount(String bankAccount) {this.bankAccount = bankAccount;}
    public int getSalary() {
        return salary;
    }
    public void setSalary(int salary) { this.salary = salary;}
    public LocalDate getStartDate() {
        return startDate;
    }
    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }
    public int getVacationDays() {
        return vacationDays;
    }
    public void setVacationDays(int vacationDays) { this.vacationDays = vacationDays;}
    public int getSickDays() {
        return sickDays;
    }
    public void setSickDays(int sickDays) {
        this.sickDays = sickDays;
    }
    public double getEducationFund() { return educationFund;}
    public void setEducationFund(double educationFund) { this.educationFund = educationFund;}
    public double getSocialBenefits() {return socialBenefits;}
    public void setSocialBenefits(double socialBenefits) {this.socialBenefits = socialBenefits;}
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
    public boolean isLoggedIn() { return isLoggedIn; }



}
