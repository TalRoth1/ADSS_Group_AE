package DTO;

import java.util.Date;
import java.util.List;

public class DriverDTO extends EmployeeDTO {

    private List<String> licenseType;
    private boolean isBusy;

    public DriverDTO(int id, String name, LocationDTO branch, String bankAccount, int salary, Date startDate,
            int vacationDays, int sickDays, float educationFund, float socialBenefits,
            String password, boolean isFinishedWorking, List<String> licenseType, boolean isBusy) {
        super(id, name, branch, bankAccount, salary, startDate, vacationDays, sickDays,
                educationFund, socialBenefits, password, isFinishedWorking, null, null, null);
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
