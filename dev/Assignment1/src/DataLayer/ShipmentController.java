package DataLayer;
import DataLayer.ShipmentDTO;
import java.sql.*;
import DomainLayer.ShipmentDL;

//TODO: check impl with employeeDAO and driverDAO

public class ShipmentController {
    private Connection connection;
    private ShipmentDAO shipmentDAO;



    public ShipmentController(Connection connection) {
        this.connection = connection;
        this.shipmentDAO = new ShipmentDAO(connection);
        this.driverDAO = new DriverDAO(connection);
        this.truckDAO = new TruckDAO(connection);
    }

    public void addShipment(ShipmentDL shipment) throws SQLException {
        shipmentCheck(shipment);
        shipmentDAO.addShipment(shipment);
    }

    public void updateShipment(ShipmentDL shipment) throws SQLException {
        shipmentCheck(shipment);
        shipmentDAO.updateShipment(shipment);
    }

    public void deleteShipment(ShipmentDL shipment) throws SQLException {
        shipmentCheck(shipment);
        shipmentDAO.deleteShipment(shipment);
    }

    public ShipmentDTO getShipment(int id) throws SQLException {
        return shipmentDAO.getShipment(id);
    }

    private void shipmentCheck(ShipmentDL shipment) {
        if (shipment == null || shipment.getTruck() == null || shipment.getDriver() == null || shipment.getOrigin() == null || shipment.getDestinations() == null || shipment.getItems() == null) {
            throw new IllegalArgumentException("Invalid shipment data provided.");
        }
    }
    private void shipmentCheck(int id) {
        if (id <= 0) {
            throw new IllegalArgumentException("Invalid shipment ID provided.");
        }
    }
}