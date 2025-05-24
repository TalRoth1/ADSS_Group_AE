package DataLayer;
import DTO.LocationDTO;
import java.sql.*;
import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicInteger;


//TODO: add id?
public class LocationController{
    private Connection connection;
    private LocationDAO locationDAO;
    private static final AtomicInteger idGenerator = new AtomicInteger(0);

    public LocationController(Connection connection) {
        this.connection = connection;
        this.locationDAO = new LocationDAO(connection);
    }

    private int generateId() {
        return idGenerator.getAndIncrement();
    }

        
    public void addLocation(LocationDTO location) throws SQLException {
        locationCheck(location.getStreet(), location.getStreetNumber(), location.getCity(), location.getContactNumber(), location.getContactName(), location.getZone());
        locationDAO.addLocation(generateId(),location.getStreet(), location.getStreetNumber(), location.getCity(), location.getContactNumber(), location.getContactName(), location.getZone());
    }

    public void updateLocation(LocationDTO location) throws SQLException {
        locationCheck(location.getStreet(), location.getStreetNumber(), location.getCity(), location.getContactNumber(), location.getContactName(), location.getZone());
        locationDAO.updateLocation(location.getStreet(), location.getStreetNumber(), location.getCity(), location.getContactNumber(), location.getContactName(), location.getZone());
    }

    public void deleteLocation(LocationDTO location) throws SQLException {
        locationCheck(location.getStreet(), location.getStreetNumber(), location.getCity());
        locationDAO.deleteLocation(location.getStreet(), location.getStreetNumber(), location.getCity());
    }
    public LocationDTO getLocation(String street, int streetNumber, String city) throws SQLException {
        locationCheck(street, streetNumber, city);
        ResultSet rst = locationDAO.getLocation(street, streetNumber, city);
        if (rst != null && rst.next()) {
            // Assuming LocationDTO has a constructor that matches the columns returned
            return new LocationDTO(
                rst.getString("street"),
                rst.getInt("streetNumber"),
                rst.getString("city"),
                rst.getString("contactNumber"),
                rst.getString("contactName"),
                rst.getString("zone")
            );
        }
        return null;
    }

    public ArrayList<LocationDTO> getAllLocations() throws SQLException {
        ArrayList<LocationDTO> locations = new ArrayList<>();
        ResultSet rst = locationDAO.getAllLocations();
        while (rst != null && rst.next()) {
            // Assuming LocationDTO has a constructor that matches the columns returned
            locations.add(new LocationDTO(
                rst.getString("street"),
                rst.getInt("streetNumber"),
                rst.getString("city"),
                rst.getString("contactNumber"),
                rst.getString("contactName"),
                rst.getString("zone")
            ));
        }
        return locations;
    }
    /* 
    private boolean locationCheck(String street, int streetNumber, String city, String contactNumber, String contactName, String zone) {
        return (street == null || streetNumber <= 0 || city == null || contactNumber == null || contactName == null || zone == null);
    }*/

    private void locationCheck(String street, int streetNumber, String city, String contactNumber, String contactName, String zone) {
        if (street == null || streetNumber <= 0 || city == null || contactNumber == null || contactName == null || zone == null) {
            throw new IllegalArgumentException("Invalid location data provided.");
        }
    }
    /* 
    private boolean locationCheck(String street, int streetNumber, String city) {
        return (street == null || streetNumber <= 0 || city == null);
    }*/

    private void locationCheck(String street, int streetNumber, String city) {
        if (street == null || streetNumber <= 0 || city == null) {
            throw new IllegalArgumentException("Invalid location data provided.");
        }
    }
}