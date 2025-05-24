package DataLayer;

import java.sql.Connection;
import java.sql.ResultSet;
import DTO.TruckDTO;

public class TruckController {
    private Connection connection;
    private TruckDAO truckDAO;

    public TruckController(Connection connection) {
        this.connection = connection;
        this.truckDAO = new TruckDAO(connection);
    }

    public void addTruck(TruckDTO truck) throws SQLException {
        truckCheck(truck);
        truckDAO.addTruck(truck.getId(), truck.getLicensePlate(), truck.getWeight());
    }

    public void updateTruck(TruckDTO truck) throws SQLException {
        truckCheck(truck);
        truckDAO.updateTruck(truck.getId(), truck.getLicensePlate(), truck.getWeight());
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
            return new TruckDTO(rst.getInt("id"), rst.getString("license_plate"), rst.getString("type"),
                    rst.getString("status"), rst.getString("location"), rst.getDouble("weight"));
        } else {
            throw new Exception("Truck not found with id: " + rst.getInt("id"));
        }
    }

}
