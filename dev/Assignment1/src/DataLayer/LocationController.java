package DataLayer;

import DTO.LocationDTO;
import DataLayer.DAOs.LocationDAO;

import java.sql.*;
import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicInteger;

public class LocationController {

    private DBConnection dbConnection = new DBConnection();
    private Connection connection;
    private LocationDAO locationDAO;
    private static final AtomicInteger idGenerator = new AtomicInteger(0);

    public LocationController() {
        String DB_URL = "main.db";
        dbConnection.connect(DB_URL);
        this.connection = dbConnection.getConnection();
        this.locationDAO = new LocationDAO(connection);
    }

    public void addLocation(LocationDTO location) throws SQLException {
        locationDAO.addLocation(location.getId(), location.getStreet(), location.getStreetNumber(), location.getCity(),
                location.getContactNumber(), location.getContactName(), location.getZone());
    }

    public void updateLocation(LocationDTO location) throws SQLException {
        locationDAO.updateLocation(location.getStreet(), location.getStreetNumber(), location.getCity(),
                location.getContactNumber(), location.getContactName(), location.getZone(), location.getId());
    }

    public void deleteLocation(LocationDTO location) throws SQLException {
        locationDAO.deleteLocation(location.getId());
    }

    public LocationDTO getLocation(String street, int streetNumber, String city) throws SQLException {
        ResultSet rst = locationDAO.getLocation(street, streetNumber, city);
        if (rst != null && rst.next()) {
            return new LocationDTO(
                    rst.getInt("id"),
                    rst.getString("street"),
                    rst.getInt("street_number"),
                    rst.getString("city"),
                    rst.getString("contact_number"),
                    rst.getString("contact_name"),
                    rst.getString("zone"));
        }
        return null;
    }

    public LocationDTO getLocation(int id) throws SQLException {
        ResultSet rst = locationDAO.getLocation(id);
        if (rst != null && rst.next()) {
            return new LocationDTO(
                    rst.getInt("id"),
                    rst.getString("street"),
                    rst.getInt("street_number"),
                    rst.getString("city"),
                    rst.getString("contact_number"),
                    rst.getString("contact_name"),
                    rst.getString("zone"));
        }
        return null;
    }

    public ArrayList<LocationDTO> getAllLocations() throws SQLException {
        ArrayList<LocationDTO> locations = new ArrayList<>();
        ResultSet rst = locationDAO.getAllLocations();
        while (rst != null && rst.next()) {
            locations.add(new LocationDTO(
                    rst.getInt("id"),
                    rst.getString("street"),
                    rst.getInt("street_number"),
                    rst.getString("city"),
                    rst.getString("contact_number"),
                    rst.getString("contact_name"),
                    rst.getString("zone")));
        }
        return locations;
    }

    public void resetLocationsTable() throws SQLException {
        locationDAO.resetTable();
    }

    public void closeConnection() {
        dbConnection.close();
    }

    public void openConnection() {
        dbConnection.open("main.db");
        this.locationDAO = new LocationDAO(DBConnection.getConnection());
    }
}