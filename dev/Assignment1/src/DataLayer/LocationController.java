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
        String DB_URL = "locations.db";
        dbConnection.connect(DB_URL);
        this.connection = DBConnection.getConnection();
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
                    rst.getInt("streetNumber"),
                    rst.getString("city"),
                    rst.getString("contactNumber"),
                    rst.getString("contactName"),
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
                    rst.getInt("streetNumber"),
                    rst.getString("city"),
                    rst.getString("contactNumber"),
                    rst.getString("contactName"),
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
                    rst.getInt("streetNumber"),
                    rst.getString("city"),
                    rst.getString("contactNumber"),
                    rst.getString("contactName"),
                    rst.getString("zone")));
        }
        return locations;
    }
}