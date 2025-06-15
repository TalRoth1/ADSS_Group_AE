package DTO;

public class LocationDTO {
    public int id;
    public String Street;
    public int StreetNumber;
    public String City;
    public String ContactNumber;
    public String ContactName;
    public String Zone;

    public LocationDTO(int id,String street, int streetNumber, String city, String contactNumber, String contactName,
            String zone) {
        this.id = id;
        this.Street = street;
        this.StreetNumber = streetNumber;
        this.City = city;
        this.ContactNumber = contactNumber;
        this.ContactName = contactName;
        this.Zone = zone;
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

    public String getContactNumber() {
        return ContactNumber;
    }

    public String getContactName() {
        return ContactName;
    }

    public String getZone() {
        return Zone;
    }

    public int getId() {
        return id;
    }

    public LocationDTO() {
        // Default constructor placeholder until they change the employee to hold the branch   
    }

}
