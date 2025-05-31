package DTO;

import java.util.List;

public class DriverDTO extends EmployeeDTO {

    private List<String> licenseType;
    private boolean isBusy;

    public DriverDTO(int id, String name, int branchid, String bankAccount, int salary, String startDate,
                      int vacationDays, int sickDays, double educationFund, double socialBenefits,
                      String password, boolean isFinishedWorking, List<String> licenseType, boolean isBusy) {
        super(id, name, branchid, bankAccount, salary, startDate, vacationDays, sickDays,
              educationFund, socialBenefits, password, isFinishedWorking, null, null, null);// fix thiS after EmployeeDTO is fixed
        this.licenseType = licenseType;
        this.isBusy = isBusy;
    }

    public List<String> getLicenseTypes() {
        return licenseType;
    }

    public Boolean isBusy() {
        return isBusy;
    }

}
