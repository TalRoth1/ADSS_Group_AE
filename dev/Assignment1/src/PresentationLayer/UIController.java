package PresentationLayer;

import DomainLayer.ShipmentFacade;
import DomainLayer.TruckDL;

import java.net.StandardSocketOptions;
import java.util.List;

import DomainLayer.DriverDL;
import DomainLayer.LocationDL;
import java.util.ArrayList;
import java.util.Map;
import java.util.Scanner;
import java.util.HashMap;
import DomainLayer.ShipmentDL;

public class UIController {
    public ShipmentFacade shipmentFacade;
    Scanner scanner = new Scanner(System.in);

    public UIController() {
        shipmentFacade = new ShipmentFacade();
    }

    public LocationDL ChooseStart() {
        boolean flag = true;
        List<LocationDL> locations = shipmentFacade.locations;
        LocationDL startLocation = null;
        while (flag) {
            System.out.println("Please choose a location from the list below: ");
            for (int i = 0; i < locations.size(); i++) {
                System.out.println(i + ": " + locations.get(i).toString());
            }
            System.out.println("Enter the number of the location you want to choose: ");
            int choice = Integer.parseInt(scanner.nextLine());
            if (choice >= 0 && choice < locations.size()) {
                startLocation = locations.get(choice);
                flag = false;
            } else {
                System.out.println("Invalid choice. Please try again.");
            }

        }
        return startLocation;
    }


    public List<LocationDL> ChooseLocations() {
        boolean flag = true;
        List<LocationDL> locations = new ArrayList<>();
        while (flag) {
            System.out.println("Please input a zone: ");
            String zone = scanner.nextLine();
            List<LocationDL> locationsByZone = shipmentFacade.LocationByZone(zone);
            if (locationsByZone.size() == 0) {
                System.out.println("No locations found in this zone. Please try again.");
            } else {
                locations = locationsByZone;
                flag = false;
            }
        }
        List<LocationDL> selectedLocations = new ArrayList<>();
        flag = true;
        while (flag) {
            System.out.println("Please choose a location from the list below: ");
            for (int i = 0; i < locations.size(); i++) {
                System.out.println(i + ": " + locations.get(i).toString());
                if ((i + 1) == locations.size()) {
                    System.out.println((i + 1) + ": Finish choosing locations");
                }
            }
            System.out.println("Enter the number of the location you want to choose: ");
            int choice = Integer.parseInt(scanner.nextLine());
            if (choice >= 0 && choice < locations.size()) {
                selectedLocations.add(locations.get(choice));
            } else if (choice == locations.size())
                flag = false;
            else {
                System.out.println("Invalid choice. Please try again.");
            }
        }
        return selectedLocations;
    }

    public TruckDL ChooseTruck() {
        boolean flag = true;
        List<TruckDL> trucks = shipmentFacade.trucks;
        TruckDL truck = null;
        while (flag) {
            System.out.println("Please choose a truck from the list below: ");
            for (int i = 0; i < trucks.size(); i++) {
                System.out.println(i + ": " + trucks.get(i).toString());
            }
            System.out.println("Enter the number of the truck you want to choose: ");
            int choice = Integer.parseInt(scanner.nextLine());
            if (choice >= 0 && choice < trucks.size()) {
                truck = trucks.get(choice);
                flag = false;
            } else {
                System.out.println("Invalid choice. Please try again.");
            }

        }
        return truck;
    }

    public DriverDL ChooseDriver() {
        boolean flag = true;
        List<DriverDL> drivers = shipmentFacade.drivers;
        DriverDL driver = null;
        while (flag) {
            System.out.println("Please choose a driver from the list below: ");
            for (int i = 0; i < drivers.size(); i++) {
                System.out.println(i + ": " + drivers.get(i).toString());
            }
            System.out.println("Enter the number of the driver you want to choose: ");
            int choice = Integer.parseInt(scanner.nextLine());
            if (choice >= 0 && choice < drivers.size()) {
                driver = drivers.get(choice);
                flag = false;
            } else {
                System.out.println("Invalid choice. Please try again.");
            }

        }
        return driver;
    }

    public Map<LocationDL, Map<String, Integer>> ChooseItems(List<LocationDL> locations) {
        boolean flag = true;
        Map<LocationDL, Map<String, Integer>> items = new HashMap<>();
        while (flag) {
            System.out.println("Please choose a location from the list below to select its items: ");
            for (int i = 0; i < locations.size(); i++) {
                System.out.println(i + ": " + locations.get(i).toString());
                if ((i + 1) == locations.size()) {
                    System.out.println((i + 1) + ": Finish choosing locations");
                }
            }
            System.out.println("Enter the number of the location you want to choose: ");
            int choice = Integer.parseInt(scanner.nextLine());
            if (choice >= 0 && choice < locations.size()) {
                LocationDL location = locations.get(choice);
                Map<String, Integer> itemList = new HashMap<>();
                boolean flag2 = true;
                while (flag2) {
                    System.out.println("Please choose an item: ");
                    List<String> itemsToChoose = shipmentFacade.GetItems();
                    boolean flag3 = true;
                    while (flag3) {
                        for (int i = 0; i < itemsToChoose.size(); i++) {
                            System.out.println(i + ": " + itemsToChoose.get(i));
                        }
                        System.out.println("Enter the number of the item you want to choose: ");
                        int choice2 = Integer.parseInt(scanner.nextLine());
                        if (choice2 >= 0 && choice2 < itemsToChoose.size()) {
                            String itemName = itemsToChoose.get(choice2);
                            flag3 = false;
                            System.out.println("Please enter the item quantity: ");
                            int itemQuantity = Integer.parseInt(scanner.nextLine());
                            itemList.put(itemName, itemQuantity);
                        } else {
                            System.out.println("Invalid choice. Please try again.");
                        }
                    }
                    System.out.println("Do you want to add more items? (y/n)");
                    String answer = scanner.nextLine();
                    if (answer.equalsIgnoreCase("n"))
                        flag2 = false;
                }
                items.put(location, itemList);
            } else if (choice == locations.size())
                flag = false;
            else {
                System.out.println("Invalid choice. Please try again.");
            }
        }
        return items;
    }

    public void CreateShipment() {
        LocationDL startLocation = ChooseStart();
        List<LocationDL> locations = ChooseLocations();
        TruckDL truck = ChooseTruck();
        DriverDL driver = ChooseDriver();
        Map<LocationDL, Map<String, Integer>> items = ChooseItems(locations);
        boolean flag = true;
        while (flag) {
            try {
                shipmentFacade.CreateShipment(truck, driver, startLocation, locations, items);
                flag = false;
            } catch (Exception e) {
                if (e.getMessage().equals("Driver does not have the right license for this truck")) {
                    System.out.println("Driver does not have the right license for this truck. Please choose a different driver.");
                    driver = ChooseDriver();
                } else if (e.getMessage().equals("Truck is overweight")) {
                    System.out.println("Truck is overweight. How would you like to proceed?");
                    System.out.println("1. Choose a different truck");
                    System.out.println("2. Edit the items in the shipment");
                    System.out.println("3. Edit the destinations in the shipment");
                    boolean flag2 = true;
                    while (flag2) {
                        int choice = Integer.parseInt(scanner.nextLine());
                        switch (choice) {
                            case 1:
                                flag2 = false;
                                truck = ChooseTruck();
                                break;
                            case 2:
                                flag2 = false;
                                items = ChooseItems(locations);
                                break;
                            case 3:
                                flag2 = false;
                                locations = ChooseLocations();
                                items = ChooseItems(locations);
                                break;
                            default:
                                System.out.println("Invalid choice. Please try again.");
                        }

                    }
                }
            }
        }

    }

    public void AddLocation() {
        System.out.println("Please enter the street: ");
        String street = scanner.nextLine();
        System.out.println("Please enter the street number: ");
        int streetNumber = Integer.parseInt(scanner.nextLine());
        System.out.println("Please enter the city: ");
        String city = scanner.nextLine();
        System.out.println("Please enter the contact number: ");
        String contactNumber = scanner.nextLine();
        System.out.println("Please enter the contact name: ");
        String contactName = scanner.nextLine();
        System.out.println("Please enter the zone: ");
        String zone = scanner.nextLine();
        shipmentFacade.AddLocation(street, streetNumber, city, contactNumber, contactName, zone);
    }

    public void AddDriver() {
        System.out.println("Please enter the driver name: ");
        String name = scanner.nextLine();
        boolean flag = true;
        List<String> licenses = new ArrayList<>();
        while (flag) {
            System.out.println("Please enter the driver license types or finish to end: ");
            String licenseType = scanner.nextLine();
            if (licenseType.equalsIgnoreCase("Finish"))
                flag = false;
            else
                licenses.add(licenseType);
        }
        shipmentFacade.AddDriver(name, licenses);
    }

    public void AddTruck() {
        System.out.println("Please enter the truck number: ");
        int number = Integer.parseInt(scanner.nextLine());
        System.out.println("Please enter the truck model: ");
        String model = scanner.nextLine();
        System.out.println("Please enter the truck type: ");
        String type = scanner.nextLine();
        System.out.println("Please enter the truck max weight: ");
        float maxWeight = Float.parseFloat(scanner.nextLine());
        shipmentFacade.AddTruck(number, model, type, maxWeight);
    }

    public void AddItem() {
        System.out.println("Please enter the item name: ");
        String itemName = scanner.nextLine();
        System.out.println("Please enter the item weight: ");
        float weight = Float.parseFloat(scanner.nextLine());
        shipmentFacade.AddItem(itemName, weight);
    }

    public void EditTruck(ShipmentDL shipment) {
        TruckDL truck = ChooseTruck();
        try {
            shipmentFacade.EditShipement(shipment, truck, null, null, null, null);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    public void EditDriver(ShipmentDL shipment) {
        DriverDL driver = ChooseDriver();
        try {
            shipmentFacade.EditShipement(shipment, null, driver, null, null, null);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    public void EditOrigin(ShipmentDL shipment) {
        LocationDL origin = ChooseStart();
        try {
            shipmentFacade.EditShipement(shipment, null, null, origin, null, null);
        } catch (Exception e) {
            System.out.println("An error occurred while editing the shipment: " + e.getMessage());
        }
    }

    public void EditDestinations(ShipmentDL shipment) {
        List<LocationDL> locations = ChooseLocations();
        Map<LocationDL, Map<String, Integer>> items = ChooseItems(locations);
        try {
            shipmentFacade.EditShipement(shipment, null, null, null, locations, items);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    public void EditItems(ShipmentDL shipment) {
        List<LocationDL> locations = shipment.Destinations;
        Map<LocationDL, Map<String, Integer>> items = ChooseItems(locations);
        try {
            shipmentFacade.EditShipement(shipment, null, null, null, null, items);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    public void EditShipement() {
        List<ShipmentDL> shipments = shipmentFacade.GetStatusShipement("Pending");
        ShipmentDL shipment = null;
        System.out.println("Please choose a shipment from the list below: ");
        for (int i = 0; i < shipments.size(); i++) {
            System.out.println(i + ": " + shipments.get(i).toString());
        }
        System.out.println("Enter the number of the shipment you want to edit: ");
        int choice = Integer.parseInt(scanner.nextLine());
        boolean flag = true;
        while (flag) {
            if (choice >= 0 && choice < shipments.size()) {
                shipment = shipments.get(choice);
                flag = false;
            } else {
                System.out.println("Invalid choice. Please try again.");
            }
        }
        flag = true;
        while (flag) {
            System.out.println("Please choose what you want to edit: ");
            System.out.println("1. Truck");
            System.out.println("2. Driver");
            System.out.println("3. Origin");
            System.out.println("4. Destinations");
            System.out.println("5. Items");
            System.out.println("6. Finish editing shipment");
            int choice2 = Integer.parseInt(scanner.nextLine());
            switch (choice2) {
                case 1:
                    EditTruck(shipment);
                    break;
                case 2:
                    EditDriver(shipment);
                    break;
                case 3:
                    EditOrigin(shipment);
                    break;
                case 4:
                    EditDestinations(shipment);
                    break;
                case 5:
                    EditItems(shipment);
                    break;
                case 6:
                    flag = false;
                    break;
                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        }

    }

    public void ChangeStatus() {
        System.out.println("Please choose a status from the list below (1-4): ");
        System.out.println("1. Send");
        System.out.println("2. Problem");
        System.out.println("3. Cancelled");
        System.out.println("4. Dont Change");
        int choice = Integer.parseInt(scanner.nextLine());
        List<ShipmentDL> shipments  = new ArrayList<>();
        ShipmentDL shipment = null;
        boolean flag;
        switch (choice) {
            case 1:
                shipments = shipmentFacade.GetStatusShipement("Pending");
                System.out.println("Please choose a shipment from the list below: ");
                flag = true;
                while (flag) {
                    for (int i = 0; i < shipments.size(); i++) {
                        System.out.println(i + ": " + shipments.get(i).toString());
                    }
                    int choice2 = Integer.parseInt(scanner.nextLine());
                    if (choice2 >= 0 && choice2 < shipments.size()) {
                        shipment = shipments.get(choice2);
                        flag = false;
                    } else {
                        System.out.println("Invalid choice. Please try again.");
                    }
                }
                shipmentFacade.ChangeStatus(shipment, "SENT");
                break;
            case 2:
                shipments = shipmentFacade.GetStatusShipement("Sent");
                System.out.println("Please choose a shipment from the list below: ");
                flag = true;
                while (flag) {
                    for (int i = 0; i < shipments.size(); i++) {
                        System.out.println(i + ": " + shipments.get(i).toString());
                    }
                    int choice2 = Integer.parseInt(scanner.nextLine());
                    if (choice2 >= 0 && choice2 < shipments.size()) {
                        shipment = shipments.get(choice2);
                        flag = false;
                    } else {
                        System.out.println("Invalid choice. Please try again.");
                    }
                }
                shipmentFacade.ChangeStatus(shipment, "PROBLEM");
                break;
            case 3:
                shipments = shipmentFacade.GetStatusShipement("Pending");
                System.out.println("Please choose a shipment from the list below: ");
                flag = true;
                while (flag) {
                    for (int i = 0; i < shipments.size(); i++) {
                        System.out.println(i + ": " + shipments.get(i).toString());
                    }
                    int choice2 = Integer.parseInt(scanner.nextLine());
                    if (choice2 >= 0 && choice2 < shipments.size()) {
                        shipment = shipments.get(choice2);
                        flag = false;
                    } else {
                        System.out.println("Invalid choice. Please try again.");
                    }
                }
                shipmentFacade.ChangeStatus(shipment, "CANCELLED");
                break;
            case 4:
                break;
            default:
                System.out.println("Invalid choice. Please try again.");
        }
    }

    public void ShowDocuments()
    {
        List<ShipmentDL> shipments = shipmentFacade.GetStatusShipement("Pending");
        shipments.addAll(shipmentFacade.GetStatusShipement("Sent"));
        shipments.addAll(shipmentFacade.GetStatusShipement("Problem"));
        shipments.addAll(shipmentFacade.GetStatusShipement("Cancelled"));
        System.out.println("Please choose a shipment from the list below: ");
        boolean flag = true;
        ShipmentDL shipment = null;
        while (flag) {
            for (int i = 0; i < shipments.size(); i++) {
                System.out.println(i + ": " + shipments.get(i).toString());
            }
            int choice = Integer.parseInt(scanner.nextLine());
            if (choice >= 0 && choice < shipments.size()) {
                shipment = shipments.get(choice);
                System.out.println(shipment.toString());
                flag = false;
            } else {
                System.out.println("Invalid choice. Please try again.");
            }
        }
        System.out.println(shipmentFacade.GetDocumentString(shipment));
    }
}
