package Domain;

import java.util.ArrayList;
import java.util.List;

import Utils.OrderStatus;

public class OrderFacade {
    
    private List<OrderDL> orders;
    private int nextID = 0;

    public OrderFacade() {
        this.orders = new ArrayList<>();
    }

    public void createOrder(int supplierID, List<OrderItemDL> items) {
        OrderDL newOrder = new OrderDL(nextID++, supplierID, items);
        orders.add(newOrder);
    }

    public void changeOrder(int orderID, List<OrderItemDL> newItems) throws IllegalArgumentException {
        for (OrderDL order : orders) {
            if (order.getOrderID() == orderID) {
                order.setOrderItems(newItems);
                return;
            }
        }
        throw new IllegalArgumentException("Order not found: " + orderID);
    }

    public void cancelOrder(int orderID) throws IllegalArgumentException {
        for (OrderDL order : orders) {
            if (order.getOrderID() == orderID) {
                order.setOrderStatus(OrderStatus.CANCELLED);;
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

    public List<OrderDL> getOrderHistory(int supplierID) throws IllegalArgumentException {
        List<OrderDL> orderHistory = new ArrayList<>();
        for (OrderDL order : orders) {
            if (order.getSupplierID() == supplierID) {
                orderHistory.add(order);
            }
        }
        return orderHistory;
    }
}
