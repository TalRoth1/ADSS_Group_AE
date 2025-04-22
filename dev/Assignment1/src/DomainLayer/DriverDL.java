package DomainLayer;
import java.util.List;

public class DriverDL {
    public String Name;
    public List<String> LicenseType;

    public DriverDL(String name, List<String> licenseType) {
        this.Name = name;
        this.LicenseType = licenseType;
    }

    public String toString() {
        return Name + ", License: " + String.join(",", LicenseType);
    }

}
