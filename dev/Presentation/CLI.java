package Presentation;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;

import Domain.OrderDL;
import Domain.OrderFacade;
import Domain.SupplierFacade;
import Utils.DeliveryMethod;
import Utils.PaymentMethod;

public class CLI {
    private SupplierFacade sf;
    private OrderFacade of;

    public CLI(SupplierFacade sf, OrderFacade of) {
        this.sf = sf;
        this.of = of;
    }

    public void run() {
        boolean running = true;
        Scanner Scanner = new Scanner(System.in);
        while (running) {
            loadMenu();
            String input = Scanner.nextLine();
            switch (input) {
                case "1":
                    addSupplier(Scanner);
                    break;
                case "2":
                    addAgreement(Scanner);
                    break;
                case "3":
                    changeAgreement(Scanner);
                    break;
                case "4":
                    removeAgreement(Scanner);
                    break;
                case "5":
                    getSuppliedItems(Scanner);
                case "6":
                    getCatalogItems(Scanner);
                    break;
                case "7":
                    createOrder(Scanner);
                    break;
                case "8":
                    getOrderDetails(Scanner);
                    break;
                case "9":
                    changeOrder(Scanner);
                    break;
                case "10":
                    cancelOrder(Scanner);
                    break;
                case "11":
                    getOrderHistory(Scanner);
                    break;
                case "12":
                    loadData();
                    break;
                case "13":
                    System.out.println("Exiting the program. Goodbye!");
                    running = false;
                    break;
                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        }
        Scanner.close();
    }

    public void printRoleMenu() {
        System.out.println("1. Admin");
        System.out.println("2. User");
        System.out.println("3. Guest");
        System.out.println("4. Exit");
    }

    public void loadMenu() {
        System.out.println("1. Add Supplier");
        System.out.println("2. Add Agreement");
        System.out.println("3. Change Agreement");
        System.out.println("4. Remove Agreement");
        System.out.println("5. Get Supplied Items");
        System.out.println("6. Get Cataloged Items");
        System.out.println("7. Create Order");
        System.out.println("8. Get Order Details");
        System.out.println("9. Change Order");
        System.out.println("10. Cancel Order");
        System.out.println("11. Get Order History");
        System.out.println("12. Load Data");
        System.out.println("13. Exit\n");
        System.out.println("Please select an option:");
    }

    public void addSupplier(Scanner Scanner) {
        System.out.println("Please enter the Following Information:\nCompany ID:");
        int companyID = Integer.parseInt(Scanner.nextLine());
        System.out.println("Bank Account:");
        int bankAccount = Integer.parseInt(Scanner.nextLine());
        System.out.println("Payment Method (Cash or credit):");
        PaymentMethod paymentMethod = PaymentMethod.getPaymentMethod(Scanner.nextLine());
        while (paymentMethod == null) {
            System.out.println("Invalid payment method. Please enter 'Cash' or 'Credit':");
            paymentMethod = PaymentMethod.getPaymentMethod(Scanner.nextLine());
        }
        System.out.println("Contact Mail:");
        String contactEmail = Scanner.nextLine();
        System.out.println("Contact Phone:");
        String contactPhone = Scanner.nextLine();
        System.out.println("Delivery Method (Scheduled, On Order or Pickup):");
        DeliveryMethod deliveryMethod = DeliveryMethod.fromString(Scanner.nextLine());
        while (deliveryMethod == null) {
            System.out.println("Invalid delivery method. Please enter 'Scheduled', 'On Order' or 'Pickup':");
            deliveryMethod = DeliveryMethod.fromString(Scanner.nextLine());
        }
        sf.addSupplier(companyID, bankAccount, paymentMethod, contactEmail, contactPhone, deliveryMethod, null);
    }

    public void addAgreement(Scanner Scanner) {
        System.out.println("Please enter the Following Information:\nSupplier ID:");
        int supplierID = Integer.parseInt(Scanner.nextLine());
        List<String[]> billOfQuantities = new ArrayList<>();
        String cont = "Y";
        Map<Integer, Integer> itemCat = new HashMap<>();
        System.out.println("Please enter the Item ID and Catalog ID seperated by ,:");
        while (cont == "Y")
        {
            String[] item = Scanner.nextLine().split(",");
            if (item.length != 2) {
                System.out.println("Invalid input. Please enter the Item ID and Catalog ID in the format: Item ID, Catalog ID");
                continue;
            }
            itemCat.put(Integer.parseInt(item[0]), Integer.parseInt(item[1]));
            System.out.println("Do you want to add another item? (Y/N):");
            cont = Scanner.nextLine().toUpperCase();
        }
        System.out.println("Bill of Quantities (Item ID, Minimum quantity and Discount seperated by ,):");
        cont = "Y";
        while (cont == "Y") {
            String[] discount = Scanner.nextLine().split(",");
            if (discount.length != 3) {
                System.out.println(
                        "Invalid input. Please enter the Bill of Quantities in the format: Item ID, Minimum quantity, Discount");
                continue;
            }
            billOfQuantities.add(discount);
            System.out.println("Do you want to add another item? (Y/N):");
            cont = Scanner.nextLine().toUpperCase();
        }
        sf.addAgreement(supplierID, itemCat, billOfQuantities);
    }

    public void changeAgreement(Scanner scanner) {
        System.out.println("Please enter the Following Information:\nSupplier ID:");
        int supplierID = Integer.parseInt(scanner.nextLine());
        System.out.println("Agreement ID:");
        int agreementID = Integer.parseInt(scanner.nextLine());
        System.out.println("New Bill of Quantities (Item ID, Minimum quantity and Discount seperated by ,):");
        List<String[]> billOfQuantities = new ArrayList<>();
        String cont = "Y";
        while (cont == "Y") {
            String[] discount = scanner.nextLine().split(",");
            if (discount.length != 3) {
                System.out.println(
                        "Invalid input. Please enter the Bill of Quantities in the format: Item ID, Minimum quantity, Discount");
                continue;
            }
            billOfQuantities.add(discount);
            System.out.println("Do you want to add another item? (Y/N):");
            cont = scanner.nextLine().toUpperCase();
        }
        sf.changeAgreement(supplierID, agreementID, billOfQuantities);
    }

    public void removeAgreement(Scanner scanner) {
        System.out.println("Please enter the Following Information:\nSupplier ID:");
        int supplierID = Integer.parseInt(scanner.nextLine());
        System.out.println("Agreement ID:");
        int agreementID = Integer.parseInt(scanner.nextLine());
        sf.removeAgreement(supplierID, agreementID);
    }

    public void getSuppliedItems(Scanner scanner) {
        System.out.println("Please enter the Following Information:\nSupplier ID:");
        int supplierID = Integer.parseInt(scanner.nextLine());
        Set<String> suppliedItems = sf.getSuppliedItems(supplierID);
        if (suppliedItems.isEmpty()) {
            System.out.println("No items supplied by this supplier.");
        } else {
            System.out.println("Supplied Items:");
            for (String item : suppliedItems) {
                System.out.println(item);
            }
        }

    }

    public void getCatalogItems(Scanner scanner) {
        System.out.println("Please enter the Following Information:\nSupplier ID:");
        int supplierID = Integer.parseInt(scanner.nextLine());
        Map<Integer, Integer> catalogItems = sf.getSuppliedCatlogItems(supplierID);
        if (catalogItems.isEmpty()) {
            System.out.println("No items in the catalog for this supplier.");
        } else {
            System.out.println("Catalog Items:");
            for (Map.Entry<Integer, Integer> item : catalogItems.entrySet()) {
                System.out.println("Item ID: " + item.getKey() + ", Catalog ID: " + item.getValue());
            }
        }
    }

    public void createOrder(Scanner scanner) {
        System.out.println("Please enter the Following Information:\nSupplier ID:");
        int supplierID = Integer.parseInt(scanner.nextLine());
        System.out.println("Agreement ID:");
        int agreementID = Integer.parseInt(scanner.nextLine());
        System.out.println("Destination:");
        String destination = scanner.nextLine();
        System.out.println("Order Items (Item ID and Quantity seperated by ,):");
        List<int[]> items = new ArrayList<>();
        String cont = "Y";
        while (cont == "Y") {
            String[] item = scanner.nextLine().split(", ");
            if (item.length != 2) {
                System.out.println("Invalid input. Please enter the Order Items in the format: Item ID, Quantity");
                continue;
            }
            items.add(new int[] { Integer.parseInt(item[0]), Integer.parseInt(item[1]) });
            System.out.println("Do you want to add another item? (Y/N):");
            cont = scanner.nextLine().toUpperCase();
        }
        of.createOrder(supplierID, destination, agreementID, items);
    }

    public void getOrderDetails(Scanner scanner) {
        System.out.println("Please enter the Following Information:\nOrder ID:");
        int orderID = Integer.parseInt(scanner.nextLine());
        try {
            System.out.println(of.getOrder(orderID));
        } catch (IllegalArgumentException e) {
            System.out.println(e.getMessage());
        }
    }

    public void changeOrder(Scanner scanner) {
        System.out.println("Please enter the Following Information:\nOrder ID:");
        int orderID = Integer.parseInt(scanner.nextLine());
        System.out.println("New Destination:");
        String destination = scanner.nextLine();
        System.out.println("New Agreement ID:");
        int agreementID = Integer.parseInt(scanner.nextLine());
        System.out.println("New Order Items (Item ID and Quantity seperated by ,):");
        List<int[]> items = new ArrayList<>();
        String cont = "Y";
        while (cont == "Y") {
            String[] item = scanner.nextLine().split(", ");
            if (item.length != 2) {
                System.out.println("Invalid input. Please enter the Order Items in the format: Item ID, Quantity");
                continue;
            }
            items.add(new int[] { Integer.parseInt(item[0]), Integer.parseInt(item[1]) });
            System.out.println("Do you want to add another item? (Y/N):");
            cont = scanner.nextLine().toUpperCase();
        }
        of.changeOrder(orderID, destination, agreementID, items);
    }

    public void cancelOrder(Scanner scanner) {
        System.out.println("Please enter the Following Information:\nOrder ID:");
        int orderID = Integer.parseInt(scanner.nextLine());
        try {
            of.cancelOrder(orderID);
        } catch (IllegalArgumentException e) {
            System.out.println(e.getMessage());
        }
    }

    public void getOrderHistory(Scanner scanner) {
        System.out.println("Please enter the Following Information:\nSupplier ID:");
        int supplierID = Integer.parseInt(scanner.nextLine());
        List<OrderDL> orderHistory = of.getOrderHistory(supplierID);
        if (orderHistory.isEmpty()) {
            System.out.println("No orders found for this supplier.");
        } else {
            System.out.println("Order History:");
            for (OrderDL order : orderHistory) {
                System.out.println(order + "\n");
            }
        }
    }

    public void loadData()
    {
        loadSuppliers();
        loadOrders();
    }

    public void loadSuppliers()
    {
        sf.loadData();
    }

    public void loadOrders()
    {
        //ofri
    }
}
