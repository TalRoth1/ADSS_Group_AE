package Domain;

import java.util.ArrayList;
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
}
