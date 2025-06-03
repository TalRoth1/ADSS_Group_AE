package DomainLayer;

public class LocationDL {

    public int id;
    public String Street;
    public int StreetNumber;
    public String City;
    public String ContactNumber;
    public String ContactName;
    public String Zone;

    public LocationDL(int id, String street, int streetNumber, String city, String contactNumber, String contactName,
            String zone) {
        this.id = id;
        this.Street = street;
        this.StreetNumber = streetNumber;
        this.City = city;
        this.ContactNumber = contactNumber;
        this.ContactName = contactName;
        this.Zone = zone;
    }

    public String toString() {
        return Street + " " + StreetNumber + ", " + City + ", " + Zone + ", Contact: " + ContactName + ", Phone: "
                + ContactNumber;
    }

    public int getId() {
        return id;
    }

    public String getContactNumber() {
        return ContactNumber;
    }

    public String getContactName() {
        return ContactName;
    }

    public String getZone() {
        return Zone;
    }

    public String getStreet() {
        return Street;
    }

    public int getStreetNumber() {
        return StreetNumber;
    }

    public String getCity() {
        return City;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        LocationDL that = (LocationDL) obj;
        return StreetNumber == that.StreetNumber && ContactNumber == that.ContactNumber && Street.equals(that.Street)
                && City.equals(that.City) && ContactName.equals(that.ContactName) && Zone.equals(that.Zone);
    }

    @Override
    public int hashCode() {
        return Integer.hashCode(id); // or use Objects.hash(id, otherFields)
    }

}
