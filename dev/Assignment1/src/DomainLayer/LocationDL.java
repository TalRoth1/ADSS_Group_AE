package DomainLayer;

public class LocationDL {
    public String Street;
    public int StreetNumber;
    public String City;
    public int ContactNumber;
    public String ContactName;
    public String Zone;

    public LocationDL(String street, int streetNumber, String city, int contactNumber, String contactName, String zone) {
        this.Street = street;
        this.StreetNumber = streetNumber;
        this.City = city;
        this.ContactNumber = contactNumber;
        this.ContactName = contactName;
        this.Zone = zone;
    }

    public String toString() {
        return Street + " " + StreetNumber + ", " + City + ", " + Zone + ", Contact: " + ContactName + ", Phone: " + ContactNumber;
    }

    public String getZone()
    {
        return Zone;
    }

    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        LocationDL that = (LocationDL) obj;
        return StreetNumber == that.StreetNumber && ContactNumber == that.ContactNumber && Street.equals(that.Street) && City.equals(that.City) && ContactName.equals(that.ContactName) && Zone.equals(that.Zone);
    }
}
