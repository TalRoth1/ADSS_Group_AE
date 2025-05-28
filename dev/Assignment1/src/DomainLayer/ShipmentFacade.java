package DomainLayer;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import DataLayer.ShipmentController;
import DataLayer.TruckController;
import DataLayer.LocationController;

public class ShipmentFacade {

    public EmployeeFacade employeeFacade;
    public ShipmentController shipmentController;
    public LocationController locationController;
    public TruckController truckController;
    public List<ShipmentDL> shipments = new ArrayList<>();
    public List<LocationDL> locations = new ArrayList<>();
    // public List<DriverDL> drivers = new ArrayList<>();
    public List<TruckDL> trucks = new ArrayList<>();
    public Map<String, Float> Items = new HashMap<>(Map.of("egg carton", 1.5f, "milk", 1f, "bread", 0.5f, "cheese", 1f,
            "butter", 0.25f, "yogurt", 0.6f, "juice", 0.75f, "soda", 0.75f, "water", 1f, "coffee", 0.5f));

    public ShipmentFacade() {
        this.shipmentController = new ShipmentController();
        this.locationController = new LocationController();
        this.truckController = new TruckController();
    }

    public void SetEmployeeFacade(EmployeeFacade employeeFacade) {
        this.employeeFacade = employeeFacade;
    }

    public void CreateShipment(TruckDL truck, LocationDL origin, List<LocationDL> destinations,
            Map<LocationDL, Map<String, Integer>> items, String shiftTime, Date datetoSend) throws Exception {
        ShipmentDL shipment = new ShipmentDL(truck, origin, destinations, items, shiftTime, datetoSend);
        if (!shipment.WeightCheck(Items)) {
            throw new Exception("Truck is overweight");
        }
        shipment.setWeight(Items);

        // assign driver
        DriverDL driverToSend = tryToAssignShifts(shipment);
        shipments.add(shipment);
    }

    public void ChangeStatus(ShipmentDL shipment, String stat) throws Exception {
        if (stat.equals("SENT")) {
            ShipmentStatus currentStatus = shipment.getStatus();
            if (currentStatus.equals(ShipmentStatus.PENDING)) {
                DriverDL driverToSend = tryToAssignShifts(shipment);
            }
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
        shipments.remove(shipment);
    }

    public LocationDL AddLocation(String street, int streetNumber, String city, String contactNumber,
            String contactName, String zone) {
        LocationDL location = new LocationDL(street, streetNumber, city, contactNumber, contactName, zone);
        locations.add(location);
        return location;
    }

    /*
     * public DriverDL AddDriver(int id, String name, String branch, String
     * bankAccount, int salary, LocalDate startDate,
     * int vacationDays, int sickDays, double educationFund, double socialBenefits,
     * String password, List<String> licenseType) {
     * DriverDL driver = new DriverDL(id, name, branch, bankAccount, salary,
     * startDate,
     * vacationDays, sickDays, educationFund, socialBenefits, password,
     * licenseType);
     * drivers.add(driver);
     * return driver;
     * }
     */
    public TruckDL AddTruck(int number, String model, String type, float maxWeight) {
        TruckDL truck = new TruckDL(number, model, type, 0, maxWeight);
        trucks.add(truck);
        return truck;
    }

    public void AddItem(String itemName, float weight) {
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
        if (truck != null) {
            if (shipmentToEdit.EditTruck(truck.GetNumber(), trucks, Items)) {
                throw new Exception("Truck is overweight or the driver does not have the right license for this truck");
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
    }

    public DriverDL tryToAssignShifts(ShipmentDL shipment) throws Exception {
        if (shipment.getStatus().equals(ShipmentStatus.PENDING)) {
            DriverDL driverToSend = employeeFacade.assignCheck(DatetoLocalDate(shipment.getDateSent()),
                    shipment.getShiftType().toString(), shipment.getDocument().getOrigin(),
                    shipment.getDocument().getLocations(), shipment.getTruck().GetType());
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
}
