package Domain;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import Utils.OrderStatus;

public class OrderFacade {

    private List<OrderDL> orders;
    private final SupplierFacade sf;
    private int nextID = 0;

    public OrderFacade(SupplierFacade sf) {
        this.sf = sf;
        this.orders = new ArrayList<>();
    }

    public void createOrder(int supplierID, String destination, int agreementID, List<int[]> Orders) {
        List<OrderItemDL> items = new ArrayList<>();
        for (int[] order : Orders) {
            int itemID = order[0];
            int quantity = order[1];
            int catalogID = getCatalogID(itemID, supplierID, agreementID);
            double totalPrice = calculateTotalPrice(quantity, catalogID, supplierID, agreementID);
            OrderItemDL item = new OrderItemDL(itemID, quantity, catalogID, totalPrice);
            items.add(item);
        }
        OrderDL newOrder = new OrderDL(nextID++, supplierID, agreementID, destination, items);
        orders.add(newOrder);
    }

    public void changeOrder(int orderID, String destination, int agreementID, List<int[]> newItems)
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
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Order not found: " + orderID);
        }
    }

    public void cancelOrder(int orderID) throws IllegalArgumentException {
        for (OrderDL order : orders) {
            if (order.getOrderID() == orderID) {
                order.setOrderStatus(OrderStatus.CANCELLED);
                ;
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

    public void loadData(){
        orders.add(new OrderDL(4001, 1, 3001, java.sql.Date.valueOf("2025-04-10"), "Central Warehouse", List.of(new OrderItemDL(1, 120, 1, 378.00), new OrderItemDL(2, 160, 2, 380.80)), OrderStatus.COMPLETED));
        orders.add(new OrderDL(4002, 1, 3001, java.sql.Date.valueOf("2025-04-15"), "Branch A", List.of(new OrderItemDL(3, 50, 3, 250.00)), OrderStatus.IN_PROGRESS));
        orders.add(new OrderDL(4003, 1, 3001, java.sql.Date.valueOf("2025-04-20"), "Branch B", List.of(new OrderItemDL(1, 90, 1, 315.00), new OrderItemDL(2, 160, 2, 380.80)), OrderStatus.COMPLETED));
        orders.add(new OrderDL(4004, 1, 3001, java.sql.Date.valueOf("2025-04-22"), "Branch C", List.of(new OrderItemDL(2, 80, 2, 224.00)), OrderStatus.CANCELLED));
        orders.add(new OrderDL(4005, 2, 3002, java.sql.Date.valueOf("2025-04-11"), "Central Warehouse", List.of(new OrderItemDL(1, 120, 1, 378.00)), OrderStatus.IN_PROGRESS));
        orders.add(new OrderDL(4006, 2, 3002, java.sql.Date.valueOf("2025-04-16"), "Branch A", List.of(new OrderItemDL(5, 100, 5, 200.00)), OrderStatus.IN_PROGRESS));
        orders.add(new OrderDL(4007, 2, 3002, java.sql.Date.valueOf("2025-04-18"), "Branch B", List.of(new OrderItemDL(6, 80, 6, 240.00)), OrderStatus.COMPLETED));
        orders.add(new OrderDL(4008, 2, 3002, java.sql.Date.valueOf("2025-04-21"), "Branch C", List.of(new OrderItemDL(4, 90, 4, 85.50)), OrderStatus.CANCELLED));
        orders.add(new OrderDL(4009, 3, 3004, java.sql.Date.valueOf("2025-04-12"), "Central Warehouse", List.of(new OrderItemDL(7, 150, 7, 594.00)), OrderStatus.IN_PROGRESS));
        orders.add(new OrderDL(4010, 3, 3003, java.sql.Date.valueOf("2025-04-14"), "Branch A", List.of(new OrderItemDL(9, 100, 9, 250.00)), OrderStatus.COMPLETED));
        orders.add(new OrderDL(4011, 3, 3004, java.sql.Date.valueOf("2025-04-17"), "Branch B", List.of(new OrderItemDL(10, 120, 10, 662.40)), OrderStatus.COMPLETED));
        orders.add(new OrderDL(4012, 3, 3003, java.sql.Date.valueOf("2025-04-23"), "Branch C", List.of(new OrderItemDL(8, 100, 8, 520.00)), OrderStatus.CANCELLED));
    }
}
