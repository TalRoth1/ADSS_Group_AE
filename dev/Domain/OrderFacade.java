package Domain;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import Utils.OrderStatus;

public class OrderFacade {

    private final List<OrderDL> orders;
    private final SupplierFacade sf;
    private int nextID = 0;

    public OrderFacade(SupplierFacade sf) {
        this.sf = sf;
        this.orders = new ArrayList<>();
    }

    public void createOrder(int supplierID, String destination, int agreementID, Date orderDate, List<int[]> Orders) {
        List<OrderItemDL> items = new ArrayList<>();
        if(!verifySupplier(supplierID)) {
            System.out.println("Can't create order, supplier not found");
            return;
        }
        if(!verifyAgreement(supplierID, agreementID)) {
            System.out.println("Can't create order, agreement not found");
            return;
        }
        for (int[] order : Orders) {
            int itemID = order[0];
            int quantity = order[1];
            int catalogID = getCatalogID(itemID, supplierID, agreementID);
            double totalPrice = calculateTotalPrice(quantity, catalogID, supplierID, agreementID);
            OrderItemDL item = new OrderItemDL(itemID, quantity, catalogID, totalPrice);
            items.add(item);
        }
        OrderDL newOrder = new OrderDL(nextID++, supplierID, agreementID, orderDate, destination, items);
        orders.add(newOrder);
    }

    public void changeOrder(int orderID, String destination, Date orderDate, int agreementID, List<int[]> newItems)
            throws IllegalArgumentException {
        try {
            OrderDL order = getOrder(orderID);
            List<OrderItemDL> items = new ArrayList<>();
            for (int[] item : newItems) {
                int itemID = item[0];
                int quantity = item[1];
                int catalogID = getCatalogID(itemID, order.getSupplierID(), agreementID);
                double totalPrice = calculateTotalPrice(quantity, catalogID, order.getSupplierID(), agreementID);
                OrderItemDL newItem = new OrderItemDL(itemID, quantity, catalogID, totalPrice);
                items.add(newItem);
            }
            order.setOrderItems(items);
            order.setAgreementID(agreementID);
            order.setDestination(destination);
            order.setOrderDate(orderDate);
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Order not found: " + orderID);
        }
    }

    public void cancelOrder(int orderID) throws IllegalArgumentException {
        for (OrderDL order : orders) {
            if (order.getOrderID() == orderID) {
                order.setOrderStatus(OrderStatus.CANCELLED);
                return;
            }
        }
        throw new IllegalArgumentException("Order not found: " + orderID);
    }

    public OrderDL getOrder(int orderID) throws IllegalArgumentException {
        for (OrderDL order : orders) {
            if (order.getOrderID() == orderID) {
                return order;
            }
        }
        throw new IllegalArgumentException("Order not found: " + orderID);
    }

    public List<OrderDL> getOrderHistory(int supplierID) {
        List<OrderDL> orderHistory = new ArrayList<>();
        for (OrderDL order : orders) {
            if (order.getSupplierID() == supplierID) {
                orderHistory.add(order);
            }
        }
        return orderHistory;
    }

    private boolean verifySupplier(int supplierID){
        return sf.getSupplier(supplierID) != null;
    }

    private boolean verifyAgreement(int supplierID, int agreementID){
        return sf.getAgreement(supplierID, agreementID) != null;
    }

    private double calculateTotalPrice(int quantity, int catalogID, int supplierID, int agreementID) {
        double price = quantity * sf.getAgreement(supplierID, agreementID).getItem(catalogID).getPrice();
        DiscountDL discount = sf.getAgreement(supplierID, agreementID).getDiscount(catalogID);
        if (discount != null) {
            if (discount.getMinimumQuantity() <= quantity) {
                price -= price * discount.getDiscountPercentage() / 100;
            }
        }
        return price;
    }

    private int getCatalogID(int itemID, int supplierID, int agreementID) {
        return sf.getAgreement(supplierID, agreementID).getItemCatalogID(itemID);
    }

    public void loadData() {
        createOrder(1, "Central Warehouse", 1, java.sql.Date.valueOf("2025-04-10"), Arrays.asList(
                new int[] { 1, 120 },
                new int[] { 2, 160 }));

        createOrder(1, "Branch A", 1, java.sql.Date.valueOf("2025-04-15"), List.of(
                new int[]{1, 50}));

        createOrder(1, "Branch B", 1, java.sql.Date.valueOf("2025-04-20"), List.of(
                new int[]{2, 90}));

        createOrder(1, "Branch C", 1, java.sql.Date.valueOf("2025-04-22"), List.of(
                new int[]{2, 80}));

        createOrder(2, "Central Warehouse", 1, java.sql.Date.valueOf("2025-04-11"), List.of(
                new int[]{6, 220}));

        createOrder(2, "Branch A", 1, java.sql.Date.valueOf("2025-04-16"), List.of(
                new int[]{6, 100}));

        createOrder(2, "Branch B", 1, java.sql.Date.valueOf("2025-04-18"), List.of(
                new int[]{6, 80}));

        createOrder(2, "Branch C", 1, java.sql.Date.valueOf("2025-04-21"), List.of(
                new int[]{6, 90}));

        createOrder(3, "Central Warehouse", 1, java.sql.Date.valueOf("2025-04-12"), List.of(
                new int[]{7, 150}));

        createOrder(3, "Branch A", 2, java.sql.Date.valueOf("2025-04-14"), List.of(
                new int[]{10, 100}));

        createOrder(3, "Branch B", 2, java.sql.Date.valueOf("2025-04-17"), List.of(
                new int[]{10, 120}));

        createOrder(3, "Branch C", 1, java.sql.Date.valueOf("2025-04-23"), List.of(
                new int[]{7, 100}));

    }
}
