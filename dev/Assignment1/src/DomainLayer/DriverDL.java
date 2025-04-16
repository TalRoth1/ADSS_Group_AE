package DomainLayer;

public class DriverDL {
    public String Name;
    public String LicenseType;

    public DriverDL(String name, String licenseType) {
        this.Name = name;
        this.LicenseType = licenseType;
    }

    public String toString() {
        return "Driver Name: " + Name + ", License Type: " + LicenseType;
    }
    
}
