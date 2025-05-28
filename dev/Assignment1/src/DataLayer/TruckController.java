package DataLayer;

import java.sql.*;
import java.sql.Connection;
import java.sql.ResultSet;
import DTO.TruckDTO;
import DataLayer.DAOs.TruckDAO;

public class TruckController {
    private DBConnection dbConnection = new DBConnection();
    private Connection connection;
    private TruckDAO truckDAO;

    public TruckController() {
        String DB_URL = "Trucks.db";
        DBConnection.connect(DB_URL);
        this.connection = DBConnection.getConnection();
        this.truckDAO = new TruckDAO(connection);
    }

    public void addTruck(TruckDTO truck) throws SQLException {
        truckCheck(truck);
        truckDAO.addTruck(truck.getId(), truck.getLicensePlate(), truck.getWeight());
    }

    public void updateTruck(TruckDTO truck) throws SQLException {
        truckCheck(truck);
        truckDAO.updateTruck(truck.getId(), truck.getLicensePlate(), truck.getWeight(), truck.getMaxWeight(),
                truck.getStatus() ? 1 : 0, truck.getType());
    }

    public void deleteTruck(TruckDTO truck) throws SQLException {
        truckCheck(truck);
        truckDAO.deleteTruck(truck.getId());
    }

    public TruckDTO getTruck(int id) throws SQLException {
        return getTruckFromResultSet(truckDAO.getTruck(id));
    }

    private void truckCheck(TruckDTO truck) {
        if (truck == null || truck.getId() <= 0 || truck.getLicensePlate() == null || truck.getWeight() <= 0) {
            throw new IllegalArgumentException("Invalid truck data provided.");
        }
    }

    private TruckDTO getTruckFromResultSet(ResultSet rst) throws SQLException {
        if (rst.next()) {
            return new TruckDTO(
                    rst.getInt("id"),
                    rst.getString("license_plate"),
                    rst.getString("type"),
                    rst.getFloat("weight"),
                    rst.getFloat("max_weight"),
                    rst.getInt("status") == 1 ? true : false);
        } else {
            throw new SQLException("Truck not found with id: " + rst.getInt("id"));
        }
    }

}
