package DataLayer;

import java.sql.*;
import java.util.ArrayList;

import DTO.TruckDTO;
import DataLayer.DAOs.TruckDAO;

public class TruckController {
    private DBConnection dbConnection = new DBConnection();
    private Connection connection;
    private TruckDAO truckDAO;

    public TruckController() {
        String DB_URL = "trucks.db";
        dbConnection.connect(DB_URL);
        this.connection = DBConnection.getConnection();
        this.truckDAO = new TruckDAO(connection);
    }

    public void addTruck(TruckDTO truck) throws SQLException {
        truckDAO.addTruck(truck.getId(), truck.getLicensePlate(), truck.getMaxWeight(),
                truck.getStatus() ? 1 : 0, truck.getType());
    }

    public void updateTruck(TruckDTO truck) throws SQLException {
        truckDAO.updateTruck(   
                truck.getId(),
                truck.getLicensePlate(),
                truck.getMaxWeight(),
                truck.getStatus() ? 1 : 0,
                truck.getType());
    }

    public void deleteTruck(TruckDTO truck) throws SQLException {
        truckDAO.deleteTruck(truck.getId());
    }

    public TruckDTO getTruck(int id) throws SQLException {
        return getTruckFromResultSet(truckDAO.getTruck(id));
    }

    public ArrayList<TruckDTO> getAllTrucks() throws SQLException {
        ArrayList<TruckDTO> trucks = new ArrayList<>();
        ResultSet rst = truckDAO.getAllTrucks();
        while (rst.next()) {
            trucks.add(new TruckDTO(
                    rst.getInt("id"),
                    rst.getString("license_plate"),
                    rst.getString("type"),
                    rst.getFloat("max_weight"),
                    rst.getInt("status") == 1 ? true : false));
        }
        return trucks;
    }    


    private TruckDTO getTruckFromResultSet(ResultSet rst) throws SQLException {
        if (rst.next()) {
            return new TruckDTO(
                    rst.getInt("id"),
                    rst.getString("license_plate"),
                    rst.getString("type"),
                    rst.getFloat("max_weight"),
                    rst.getInt("status") == 1 ? true : false);
        } else {
            throw new SQLException("Truck not found with id: " + rst.getInt("id"));
        }
    }

    public void resetTrucksTable() throws SQLException {
        truckDAO.clearTable();
    }
    

}
