package DomainLayer;

import DataLayer.DriverController;
import DataLayer.ItemsController;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import DTO.ItemDTO;
import DTO.LocationDTO;
import DTO.ShipmentDTO;
import DTO.TruckDTO;
import DataLayer.ShipmentController;
import DataLayer.TruckController;
import DataLayer.Mappers.LocationMapper;
import DataLayer.Mappers.ShipmentMapper;
import DataLayer.Mappers.TruckMapper;
import DataLayer.LocationController;

public class ShipmentFacade {

    public EmployeeFacade employeeFacade;
    public ShipmentController shipmentController;
    public LocationController locationController;
    public TruckController truckController;
    public ItemsController itemsController;
    public LocationMapper locationMapper = new LocationMapper();
    public TruckMapper truckMapper = new TruckMapper();
    public ShipmentMapper shipmentMapper = new ShipmentMapper();
    public List<ShipmentDL> shipments = new ArrayList<>();
    public List<LocationDL> locations = new ArrayList<>();
    // public List<DriverDL> drivers = new ArrayList<>();
    public List<TruckDL> trucks = new ArrayList<>();
    public Map<String, Float> Items = new HashMap<>();

    public ShipmentFacade(EmployeeFacade employeeFacade) {
        this.employeeFacade = employeeFacade;
        this.locationController = new LocationController();
        this.truckController = new TruckController();
        this.itemsController = new ItemsController();
        this.shipmentController = new ShipmentController(truckController, this.employeeFacade.getDriverController(),
                locationController, itemsController);
    }

    public void SetEmployeeFacade(EmployeeFacade employeeFacade) {
        this.employeeFacade = employeeFacade;
    }

    public void CreateShipment(TruckDL truck, LocationDL origin, List<LocationDL> destinations,
            Map<LocationDL, Map<String, Integer>> items, String shiftTime, Date datetoSend) throws Exception {
        ShipmentDL shipment = new ShipmentDL(getHighestShipmentId(), truck, origin, destinations, items, shiftTime,
                datetoSend);
        if (!shipment.WeightCheck(Items)) {
            throw new Exception("Truck is overweight");
        }

        shipment.setWeight(Items);

        DriverDL driverToSend;
        // assign driver
        try {
            driverToSend = tryToAssignShifts(shipment);
        } catch (Exception e) {
            driverToSend = null; // No driver assigned, handle accordingly
        }
        try {
            shipmentController.addShipment(shipmentMapper.toDTO(shipment));
        } catch (Exception e) {
            e.printStackTrace();
            throw new Exception("Error creating shipment: " + e.getMessage());
        }
        shipments.add(shipment);
    }

    public void ChangeStatus(ShipmentDL shipment, String stat) throws Exception {
        ShipmentDL temp = shipment.clone();
        if (stat.equals("SENT")) {
            ShipmentStatus currentStatus = shipment.getStatus();
            DriverDL driverToSend = tryToAssignShifts(shipment);
            if (shipment.DriverBusyCheck()) {
                throw new Exception("Driver is busy");
            }
            if (shipment.TruckBusyCheck()) {
                throw new Exception("Truck is busy");
            }
            shipment.ChangeAvailablity();
        }

        shipment.ChangeStatus(stat);
        // assuming both where busy beforehand
        if (stat.equals("COMPLETED") || stat.equals("PROBLEM") || stat.equals("CANCELLED")) {
            shipment.ChangeAvailablity();
        }

        try {
            shipmentController.updateShipment(shipmentMapper.toDTO(shipment));
        } catch (Exception e) {
            e.printStackTrace();
            shipment = temp; // revert to original shipment
            throw new Exception("Error changing shipment status: " + e.getMessage());
        }
    }

    public List<ShipmentDL> GetStatusShipement(String status) {
        List<ShipmentDL> ans = new ArrayList<>();
        for (ShipmentDL shipment : shipments) {
            if (shipment.Status.toString().equalsIgnoreCase(status)) {
                ans.add(shipment);
            }
        }
        return ans;
    }

    public void RemoveShipment(ShipmentDL shipment) {
        try {
            shipmentController.deleteShipment(shipmentMapper.toDTO(shipment));
        } catch (Exception e) {
            e.printStackTrace();
            return;
        }
        shipments.remove(shipment);
    }

    public LocationDL AddLocation(String street, int streetNumber, String city, String contactNumber,
            String contactName, String zone) {
        LocationDL location = new LocationDL(getHighestLocationId(), street, streetNumber, city, contactNumber,
                contactName, zone);
        try {
            locationController.addLocation(locationMapper.toDTO(location));
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        locations.add(location);
        employeeFacade.addBranch(location);
        return location;
    }

    public TruckDL AddTruck(int number, String model, String type, float maxWeight) {
        TruckDL truck = new TruckDL(number, model, type, maxWeight);
        try {
            truckController.addTruck(truckMapper.toDTO(truck));
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        trucks.add(truck);
        return truck;
    }

    public void AddItem(String itemName, float weight) {
        try {
            itemsController.addItem(new ItemDTO(itemName, weight));
        } catch (Exception e) {
            e.printStackTrace();
            return;
        }
        Items.put(itemName, weight);
    }

    public List<String> GetItems() {
        List<String> itemList = new ArrayList<>();
        for (String item : Items.keySet()) {
            itemList.add(item);
        }
        return itemList;
    }

    public List<LocationDL> LocationByZone(String zone) {
        List<LocationDL> ans = new ArrayList<>();
        for (LocationDL loc : locations) {
            if (loc.getZone().equals(zone)) {
                ans.add(loc);
            }
        }
        return ans;
    }

    public void EditShipement(ShipmentDL shipment, TruckDL truck, DriverDL driver, LocationDL origin,
            List<LocationDL> destinations, Map<LocationDL, Map<String, Integer>> items, Date dateToSend,
            String shiftTime) throws Exception {
        // assuming that the shipment exists in the list
        int index = shipments.indexOf(shipment);
        ShipmentDL shipmentToEdit = shipments.get(index);
        ShipmentDL temp = shipmentToEdit.clone();
        try {
            if (truck != null) {
                if (!shipmentToEdit.EditTruck(truck.GetNumber(), trucks, Items)) {
                    throw new Exception(
                            "Truck is overweight or the driver does not have the right license for this truck");
                }

            }
            if (driver != null) {
                Boolean check = shipmentToEdit.EditDriver(driver);
                if (!check) {
                    throw new Exception("Driver does not have the right license for this truck");
                }
            }
            if (origin != null) {
                shipmentToEdit.EditOrigin(origin, locations);
            }
            if (destinations != null) {
                Boolean check = shipmentToEdit.EditDestinations(items, Items);
                if (!check) {
                    throw new Exception("Truck cannot support the new weight");
                }
            }
            if (items != null) {
                Boolean check = shipmentToEdit.EditDestinations(items, Items);
                if (!check) {
                    throw new Exception("Truck cannot support the new weight");
                }
            }
            if (shiftTime != null) {
                shipmentToEdit.setShiftType(shiftTime);

            }
            if (dateToSend != null) {
                shipmentToEdit.setDateSent(dateToSend);
            }
            tryToAssignShifts(shipmentToEdit);
            shipmentController.updateShipment(ShipmentMapper.toDTO(shipmentToEdit));
        } catch (Exception e) {
            e.printStackTrace();
            shipmentToEdit = temp; // revert to original shipment
            throw new Exception("Error editing shipment: " + e.getMessage());
        }
    }

    public DriverDL tryToAssignShifts(ShipmentDL shipment) throws Exception {
        if (shipment.getStatus().equals(ShipmentStatus.PENDING)) {
            DriverDL driverToSend = employeeFacade.assignCheck(DatetoLocalDate(shipment.getDateSent()),
                    shipment.getShiftType().toString(), shipment.getDocument().getOrigin(),
                    shipment.getDocument().getLocations(), shipment.getTruck().GetType(), locations);
            if (driverToSend == null) {
                throw new Exception("No available driver for this shipment");
            }
            shipment.setDriver(driverToSend);
            shipment.ChangeStatus("APPROVED");
        }
        return shipment.getDriver();
    }

    public String GetDocumentString(ShipmentDL shipment) {
        return shipment.getDocument().toString() + "\n" + "Status: " + shipment.getStatus().toString();
    }

    private LocalDate DatetoLocalDate(Date date) {
        return date.toInstant()
                .atZone(ZoneId.systemDefault())
                .toLocalDate();
    }

    public void LoadData() {
        try {
            List<LocationDTO> locationDTOs = locationController.getAllLocations();
            for (LocationDTO locationDTO : locationDTOs) {
                LocationDL location = locationMapper.toDomain(locationDTO);
                locations.add(location);
            }
            List<TruckDTO> truckDTOs = truckController.getAllTrucks();
            for (TruckDTO truckDTO : truckDTOs) {
                TruckDL truck = truckMapper.toDomain(truckDTO);
                trucks.add(truck);
            }
            List<ItemDTO> itemDTOs = itemsController.getAllItems();
            for (ItemDTO itemDTO : itemDTOs) {
                Items.put(itemDTO.getName(), itemDTO.getWeight());
            }
            List<ShipmentDTO> shipmentDTOs = shipmentController.getAllShipments();
            for (ShipmentDTO shipmentDTO : shipmentDTOs) {
                ShipmentDL shipment = shipmentMapper.toDL(shipmentDTO);
                shipments.add(shipment);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void ClearDataBase() {
        try {
            locationController.resetLocationsTable();
            truckController.resetTrucksTable();
            itemsController.clearAllItems();
            shipmentController.clearAllShipments();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void MakePredefinedData() {
        ClearDataBase();
        LocationDL loc1 = new LocationDL(1, "First St", 1, "CityA", "123456789", "John Doe", "1");
        LocationDL loc2 = new LocationDL(2, "Main St", 1, "CityA", "123456789", "Spiderman", "1");
        LocationDL loc3 = new LocationDL(3, "Second St", 2, "CityB", "987654321", "Peter Griffin", "2");
        TruckDL truck1 = new TruckDL(1, "ModelX", "A", 10f);
        TruckDL truck2 = new TruckDL(2, "ModelY", "B", 15f);
        TruckDL truck3 = new TruckDL(3, "ModelZ", "C", 20f);
        try {
            locationController.addLocation(locationMapper.toDTO(loc1));
            locationController.addLocation(locationMapper.toDTO(loc2));
            locationController.addLocation(locationMapper.toDTO(loc3));
            truckController.addTruck(truckMapper.toDTO(truck1));
            truckController.addTruck(truckMapper.toDTO(truck2));
            truckController.addTruck(truckMapper.toDTO(truck3));
            itemsController.addItem(new ItemDTO("Milk", 0.5f));
            itemsController.addItem(new ItemDTO("Cola", 1.0f));
            itemsController.addItem(new ItemDTO("Bread", 1.5f));
            employeeFacade.addBranch(loc1);
            employeeFacade.addBranch(loc2);
            employeeFacade.addBranch(loc3);
        } catch (Exception e) {
            e.printStackTrace();
            return;
        }

    }

    public int getHighestLocationId() {
        int maxId = -1;
        for (LocationDL location : locations) {
            if (location.getId() > maxId) {
                maxId = location.getId();
            }
        }
        return maxId + 1; // Return the next available ID
    }

    public int getHighestTruckNumber() {
        int maxNumber = 0;
        for (TruckDL truck : trucks) {
            if (truck.GetNumber() > maxNumber) {
                maxNumber = truck.GetNumber();
            }
        }
        return maxNumber + 1; // Return the next available truck number
    }

    public int getHighestShipmentId() {
        int maxId = 0;
        for (ShipmentDL shipment : shipments) {
            if (shipment.getId() > maxId) {
                maxId = shipment.getId();
            }
        }
        return maxId + 1; // Return the next available shipment ID
    }
}
