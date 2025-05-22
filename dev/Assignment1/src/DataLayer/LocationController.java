package DataLayer;
import DomainLayer.LocationDL;
import java.sql.*;
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

        
    public void addLocation(String street, int streetNumber, String city, String contactNumber, String contactName, String zone) throws SQLException {
        locationCheck(street, streetNumber, city, contactNumber, contactName, zone);
        locationDAO.addLocation(generateId() ,street, streetNumber, city, contactNumber, contactName, zone);
    }

    public void updateLocation(String street, int streetNumber, String city, String contactNumber, String contactName, String zone) throws SQLException {
        locationCheck(street, streetNumber, city, contactNumber, contactName, zone);
        locationDAO.updateLocation(street, streetNumber, city, contactNumber, contactName, zone);
    }

    public void deleteLocation(String street, int streetNumber, String city) throws SQLException {
        locationCheck(street, streetNumber, city);
        locationDAO.deleteLocation(street, streetNumber, city);
    }
    public LocationDL getLocation(String street, int streetNumber, String city) throws SQLException {
        locationCheck(street, streetNumber, city);
        return locationDAO.getLocation(street, streetNumber, city);       
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