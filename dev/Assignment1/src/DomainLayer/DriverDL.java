package DomainLayer;
import java.util.List;
import java.time.LocalDate;

public class DriverDL extends ShiftEmployee {
    public List<String> LicenseType;
    public boolean isBusy = false;

    public DriverDL(int id, String name, String branch, String bankAccount, int salary, LocalDate startDate,
                    int vacationDays, int sickDays, double educationFund, double socialBenefits,
                    String password, List<String> licenseType) {
        super(id, name, branch, bankAccount, salary, startDate,
                vacationDays, sickDays, educationFund, socialBenefits, password, Role.DRIVER);
        this.LicenseType = licenseType;
    }

    public String toString() {
        return "" + getName() + ", License: " + String.join(",", LicenseType);
    }


    public void changeState()
    {
        isBusy = !isBusy;
    }



}
