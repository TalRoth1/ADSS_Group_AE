package PresentationLayer;

import DomainLayer.ShipmentFacade;
import DomainLayer.TruckDL;

import java.util.List;

import DomainLayer.DriverDL;
import DomainLayer.LocationDL;
import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap;

public class UIController {
    public ShipmentFacade shipmentFacade;
    public UIController() {
        shipmentFacade = new ShipmentFacade();
    }

    public LocationDL ChooseStart()
    {
        boolean flag = true;
        List<LocationDL> locations = shipmentFacade.locations;
        LocationDL startLocation = null;
        while(flag)
        {
            System.out.println("Please choose a location from the list below: ");
            for (int i = 0; i < locations.size(); i++) {
                System.out.println(i + ": " + locations.get(i).toString());
            }
            System.out.println("Enter the number of the location you want to choose: ");
            int choice = Integer.parseInt(System.console().readLine());
            if (choice >= 0 && choice < locations.size()) {
                startLocation = locations.get(choice);
                flag = false;
            }
            else {
                System.out.println("Invalid choice. Please try again.");
            }
            
        }
        return startLocation;
    }


    public List<LocationDL> ChooseLocations()
    {
        boolean flag = true;
        List<LocationDL> locations = shipmentFacade.locations;
        List<LocationDL> selectedLocations = new ArrayList<>();
        while(flag)
        {
            System.out.println("Please choose a location from the list below: ");
            for (int i = 0; i < locations.size(); i++) {
                System.out.println(i + ": " + locations.get(i).toString());
                if((i+1) == locations.size())
                {
                    System.out.println((i+1) + ": Finish choosing locations");
                }
            }
            System.out.println("Enter the number of the location you want to choose: ");
            int choice = Integer.parseInt(System.console().readLine());
            if (choice >= 0 && choice < locations.size()) {
                selectedLocations.add(locations.get(choice));
            }
            else if (choice == locations.size())
                flag = false; 
            else {
                System.out.println("Invalid choice. Please try again.");
            }
        }
        return selectedLocations;
    }

    public TruckDL ChooseTruck()
    {
        boolean flag = true;
        List<TruckDL> trucks = shipmentFacade.trucks;
        TruckDL truck = null;
        while(flag)
        {
            System.out.println("Please choose a truck from the list below: ");
            for (int i = 0; i < trucks.size(); i++) {
                System.out.println(i + ": " + trucks.get(i).toString());
            }
            System.out.println("Enter the number of the truck you want to choose: ");
            int choice = Integer.parseInt(System.console().readLine());
            if (choice >= 0 && choice < trucks.size()) {
                truck = trucks.get(choice);
                flag = false;
            }
            else {
                System.out.println("Invalid choice. Please try again.");
            }
            
        }
        return truck;
    }

    public DriverDL ChooseDriver()
    {
        boolean flag = true;
        List<DriverDL> drivers = shipmentFacade.drivers;
        DriverDL driver = null;
        while(flag)
        {
            System.out.println("Please choose a driver from the list below: ");
            for (int i = 0; i < drivers.size(); i++) {
                System.out.println(i + ": " + drivers.get(i).toString());
            }
            System.out.println("Enter the number of the driver you want to choose: ");
            int choice = Integer.parseInt(System.console().readLine());
            if (choice >= 0 && choice < drivers.size()) {
                driver = drivers.get(choice);
                flag = false;
            }
            else {
                System.out.println("Invalid choice. Please try again.");
            }
            
        }
        return driver;
    }

    public Map<LocationDL, Map<String,Integer>> ChooseItems(List<LocationDL> locations)
    {
        boolean flag = true;
        Map<LocationDL, Map<String,Integer>> items = new HashMap<>();
        while(flag)
        {
            System.out.println("Please choose a location from the list below: ");
            for (int i = 0; i < locations.size(); i++) {
                System.out.println(i + ": " + locations.get(i).toString());
                if((i+1) == locations.size())
                {
                    System.out.println((i+1) + ": Finish choosing locations");
                }
            }
            System.out.println("Enter the number of the location you want to choose: ");
            int choice = Integer.parseInt(System.console().readLine());
            if (choice >= 0 && choice < locations.size()) {
                LocationDL location = locations.get(choice);
                Map<String,Integer> itemList = new HashMap<>();
                boolean flag2 = true;
                while(flag2){
                    System.out.println("Please enter the item name: ");
                    String itemName = System.console().readLine();
                    System.out.println("Please enter the item quantity: ");
                    int itemQuantity = Integer.parseInt(System.console().readLine());
                    itemList.put(itemName, itemQuantity);
                    System.out.println("Do you want to add more items? (yes/no)");
                    String answer = System.console().readLine();
                    if(answer.equalsIgnoreCase("no"))
                        flag2 = false;
                }
                items.put(location, itemList);
            }
            else if (choice == locations.size())
                flag = false; 
            else {
                System.out.println("Invalid choice. Please try again.");
            }
        }
        return items;
    }

    public void CreateShipment()
    {
        LocationDL startLocation = ChooseStart();
        List<LocationDL> locations = ChooseLocations();
        TruckDL truck = ChooseTruck();
        DriverDL driver = ChooseDriver();
        Map<LocationDL, Map<String,Integer>> items = ChooseItems(locations);
        shipmentFacade.CreateShipment(truck, driver, startLocation, locations, items);
    }

    public void AddLocation() {
        System.out.println("Please enter the street: ");
        String street = System.console().readLine();
        System.out.println("Please enter the street number: ");
        int streetNumber = Integer.parseInt(System.console().readLine());
        System.out.println("Please enter the city: ");
        String city = System.console().readLine();
        System.out.println("Please enter the contact number: ");
        int contactNumber = Integer.parseInt(System.console().readLine());
        System.out.println("Please enter the contact name: ");
        String contactName = System.console().readLine();
        System.out.println("Please enter the zone: ");
        String zone = System.console().readLine();
        shipmentFacade.AddLocation(street, streetNumber, city, contactNumber, contactName, zone);
    }
    
    public void AddDriver() {
        System.out.println("Please enter the driver name: ");
        String name = System.console().readLine();
        System.out.println("Please enter the license type: ");
        String licenseType = System.console().readLine();
        shipmentFacade.AddDriver(name, licenseType);
    }
    public void AddTruck() {
        System.out.println("Please enter the truck number: ");
        int number = Integer.parseInt(System.console().readLine());
        System.out.println("Please enter the truck model: ");
        String model = System.console().readLine();
        System.out.println("Please enter the truck type: ");
        String type = System.console().readLine();
        System.out.println("Please enter the truck max weight: ");
        float maxWeight = Float.parseFloat(System.console().readLine());
        shipmentFacade.AddTruck(number, model, type, maxWeight);
    }

}
