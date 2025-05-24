package DTO;

import java.util.ArrayList;


public class DriverDTO extends EmployeeDTO {
    private ArrayList<String> licenseType;
    private boolean isBusy;


    public DriverDTO(int id, String name, String branch, String bankAccount, int salary, String startDate,
                     int vacationDays, int sickDays, double educationFund, double socialBenefits,
                     String password, String licenseNumber, String vehicleType, String drivingExperience) {
        super(id, name, branch, bankAccount, salary, startDate, vacationDays, sickDays, educationFund,
                socialBenefits, password);
        this.licenseType = new ArrayList<>();
        this.isBusy = false;
    }

    public ArrayList<String> getLicenseTypes() {
        return licenseType;
    }

    public Boolean isBusy() {
        return isBusy;
    }



}
