package DataLayer;

import DTO.DocumentDTO;
import DTO.DriverDTO;
import DTO.ItemDTO;
import DTO.LocationDTO;
import DTO.ShipmentDTO;
import DTO.TruckDTO;
import DataLayer.DAOs.DriverDAO;
import DataLayer.DAOs.ShipmentDAO;
import DataLayer.DAOs.TruckDAO;
import DomainLayer.ShipmentDL;
import DataLayer.DAOs.DocumentDAO;
import DataLayer.DAOs.DocumentItemDAO;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.sql.*;

import javax.naming.spi.DirStateFactory.Result;
import javax.print.Doc;

public class ShipmentController {
    private DBConnection dbConnection = new DBConnection();
    // private DBConnection dbConnection2 = new DBConnection();
    // private DBConnection dbConnection3 = new DBConnection();
    private ShipmentDAO shipmentDAO;
    private DocumentDAO documentDAO;
    private DocumentItemDAO documentItemDAO;
    private TruckController truckController;
    private DriverController driverController;
    private LocationController locationController;
    private ItemsController itemsController;

    public ShipmentController(TruckController truckController, DriverController driverController,
            LocationController locationController, ItemsController itemsController) {
        String DB_URL = "main.db";
        dbConnection.connect(DB_URL);
        // this.dbConnection2.connect("main.db");
        // this.dbConnection3.connect("main.db");
        this.shipmentDAO = new ShipmentDAO(dbConnection.getConnection());
        this.documentDAO = new DocumentDAO(dbConnection.getConnection());
        this.documentItemDAO = new DocumentItemDAO(dbConnection.getConnection());
        this.truckController = truckController;
        this.driverController = driverController;
        this.locationController = locationController;
        this.itemsController = itemsController;
    }

    public void addShipment(ShipmentDTO shipment) throws SQLException {

        // add shipment's document
        documentDAO.createDocument(shipment.getDocument().getId(), shipment.getDocument().getOrigin().getId(),
                shipment.getDocument().getWeight());

        // add shipment
        shipmentDAO.addShipment(shipment.getId(),
                new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssXXX").format(shipment.getDateCreated()),
                new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssXXX").format(shipment.getDateSent()),
                shipment.getTruck().getId(), shipment.getDriver() == null ? -100 : shipment.getDriver().getId(),
                shipment.getDocument().getId(),
                shipment.getStatus(), shipment.getShiftType());

        // add documents items
        Map<LocationDTO, Map<String, Integer>> items = shipment.getDocument().getItems();
        for (Map.Entry<LocationDTO, Map<String, Integer>> entry : items.entrySet()) {
            LocationDTO location = entry.getKey();
            Map<String, Integer> itemQuantities = entry.getValue();
            for (Map.Entry<String, Integer> itemEntry : itemQuantities.entrySet()) {
                ItemDTO DTOitem = itemsController.getItemByName(itemEntry.getKey());
                String itemName = DTOitem.getName();
                int quantity = itemEntry.getValue();
                documentItemDAO.addDocumentItem(shipment.getDocument().getId(), location.getId(), itemName, quantity);
            }
        }
    }

    public void updateShipment(ShipmentDTO shipment) throws SQLException {
        // update shipment
        shipmentDAO.updateShipment(shipment.getId(),
                new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssXXX").format(shipment.getDateCreated()),
                new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssXXX").format(shipment.getDateSent()),
                shipment.getTruck().getId(), shipment.getDriver().getId(), shipment.getDocument().getId(),
                shipment.getStatus(), shipment.getShiftType());

        // update document
        documentDAO.updateDocument(shipment.getDocument().getId(), shipment.getDocument().getOrigin().getId(),
                shipment.getDocument().getWeight());

        // update documents items
        Map<LocationDTO, Map<String, Integer>> items = shipment.getDocument().getItems();
        for (Map.Entry<LocationDTO, Map<String, Integer>> entry : items.entrySet()) {
            LocationDTO location = entry.getKey();
            Map<String, Integer> itemQuantities = entry.getValue();
            for (Map.Entry<String, Integer> itemEntry : itemQuantities.entrySet()) {
                ItemDTO DTOitem = itemsController.getItemByName(itemEntry.getKey());
                String itemName = DTOitem.getName();
                int quantity = itemEntry.getValue();
                documentItemDAO.updateDocumentItem(shipment.getDocument().getId(), location.getId(), itemName,
                        quantity);
            }
        }
    }

    public void deleteShipment(ShipmentDTO shipment) throws SQLException {
        // delete shipment
        shipmentDAO.deleteShipment(shipment.getId());

        // delete document
        documentDAO.deleteDocument(shipment.getDocument().getId());

        // delete document items
        documentItemDAO.deleteAllDocumentItems(shipment.getDocument().getId());
    }

    public ShipmentDTO getShipment(int id) throws SQLException {
        ResultSet rst = shipmentDAO.getShipment(id);
        if (rst.next()) {
            int truckId = rst.getInt("truck_id");
            int driverId = rst.getInt("driver_id");
            int documentId = rst.getInt("document_id");
            TruckDTO truck = truckController.getTruck(truckId);
            DriverDTO driver = driverController.getDriver(driverId);
            ResultSet docRst = documentDAO.getDocument(documentId);
            if (!docRst.next()) {
                throw new SQLException("Document not found with id: " + documentId);
            }
            ResultSet docItemRst = documentItemDAO.getDocumentItems(documentId);
            Map<LocationDTO, Map<String, Integer>> items = new HashMap<>();
            while (docItemRst.next()) {
                int locationId = docItemRst.getInt("locationID");
                String itemName = itemsController.getItemByName(docItemRst.getString("itemName")).getName();
                int quantity = docItemRst.getInt("amount");
                LocationDTO location = locationController.getLocation(locationId);

                items.putIfAbsent(location, new HashMap<>());
                items.get(location).put(itemName, quantity);
            }
            DocumentDTO document = new DocumentDTO(documentId,
                    locationController.getLocation(docRst.getInt("originID")),
                    items, docRst.getFloat("weight"));
            ShipmentDTO shipment = new ShipmentDTO(rst.getInt("id"),
                    rst.getDate("dateCreated"), rst.getDate("dateSent"),
                    truck, driver, rst.getString("status"), document, rst.getString("shiftType"));
            return shipment;
        } else {
            throw new SQLException("Shipment not found with id: " + id);
        }
    }

    public ArrayList<ShipmentDTO> getAllShipments() throws SQLException {
        ResultSet rst = shipmentDAO.getAllShipments();
        ArrayList<ShipmentDTO> shipments = new ArrayList<>();
        while (rst.next()) {
            int truckId = rst.getInt("truck_id");
            int driverId = rst.getInt("driver_id");
            int documentId = rst.getInt("doc_id");
            TruckDTO truck = truckController.getTruck(truckId);
            DriverDTO driver = driverController.getDriver(driverId);
            ResultSet docRst = documentDAO.getDocument(documentId);
            if (!docRst.next()) {
                throw new SQLException("Document not found with id: " + documentId);
            }
            ResultSet docItemRst = documentItemDAO.getDocumentItems(documentId);
            Map<LocationDTO, Map<String, Integer>> items = new HashMap<>();
            while (docItemRst.next()) {
                int locationId = docItemRst.getInt("locationID");
                String itemName = itemsController.getItemByName(docItemRst.getString("itemName")).getName();
                int quantity = docItemRst.getInt("amount");
                LocationDTO location = locationController.getLocation(locationId);

                items.putIfAbsent(location, new HashMap<>());
                items.get(location).put(itemName, quantity);
            }
            DocumentDTO document = new DocumentDTO(documentId,
                    locationController.getLocation(docRst.getInt("originID")),
                    items, docRst.getFloat("weight"));
            ShipmentDTO shipment = new ShipmentDTO(rst.getInt("id"),
                    rst.getDate("dateCreated"), rst.getDate("dateSent"),
                    truck, driver, rst.getString("status"), document, rst.getString("shiftType"));
            shipments.add(shipment);
        }
        return shipments;
    }

    public void clearAllShipments() throws SQLException {
        documentItemDAO.clearTable(); // delete dependent table first
        shipmentDAO.clearTable(); // then shipments
        documentDAO.clearTable(); // finally documents (also resets AUTOINCREMENT)
    }

    public void closeConnection() {
        dbConnection.close();
        // dbConnection2.close();
        // dbConnection3.close();
    }

    public void openConnection() {
        dbConnection.open("main.db");
        // dbConnection2.open("main.db");
        // dbConnection3.open("main.db");
        this.shipmentDAO = new ShipmentDAO(dbConnection.getConnection());
        this.documentDAO = new DocumentDAO(dbConnection.getConnection());
        this.documentItemDAO = new DocumentItemDAO(dbConnection.getConnection());
        truckController.openConnection();
        driverController.openConnection();
        locationController.openConnection();
        itemsController.openConnection();
    }
}