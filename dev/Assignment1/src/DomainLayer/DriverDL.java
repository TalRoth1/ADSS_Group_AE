package DomainLayer;
import java.util.List;

public class DriverDL extends shiftEmployee {
    public List<String> LicenseType;
    public boolean isBusy = false;

    public DriverDL(int id, String name, String branch, String bankAccount, int salary, LocalDate startDate,
                    int vacationDays, int sickDays, double educationFund, double socialBenefits,
                    String password, Role.DRIVER , List<String> licenseType) {
        super(id, name, branch, bankAccount, salary, startDate,
                vacationDays, sickDays, educationFund, socialBenefits, password, Role.DRIVER);
        this.LicenseType = licenseType;
    }

    public String toString() {
        return Name + ", License: " + String.join(",", LicenseType);
    }

    public void changeState()
    {
        isBusy = !isBusy;
    }

}
