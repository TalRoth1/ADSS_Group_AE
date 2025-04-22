package Service;

import java.util.Date;
import java.util.List;

import Utils.OrderStatus;

public class OrderSL {
    private final int orderID;
    private final int supplierID;
    private int agreementID;
    private final Date orderDate;
    private final String destination;
    private List<OrderItemSL> orderItems;
    private OrderStatus orderStatus;

    OrderSL(int orderID, int supplierID, int agreementID, String destination, List<OrderItemSL> orderItems) {
        this.orderID = orderID;
        this.supplierID = supplierID;
        this.agreementID = agreementID;
        this.orderDate = new Date(); // Set the order date to the current date
        this.destination = destination;
        this.orderItems = orderItems;
        this.orderStatus = OrderStatus.IN_PROGRESS;
    }

    OrderSL(int orderID, int supplierID, int agreementID, Date orderDate, String destination,
            List<OrderItemSL> orderItems, OrderStatus orderStatus) {
        this.orderID = orderID;
        this.supplierID = supplierID;
        this.agreementID = agreementID;
        this.orderDate = orderDate;
        this.destination = destination;
        this.orderItems = orderItems;
        this.orderStatus = orderStatus;
    }

    public int getOrderID() {
        return orderID;
    }

    public int getSupplierID() {
        return supplierID;
    }

    public int getAgreementID() {
        return agreementID;
    }

    public List<OrderItemSL> getOrderItems() {
        return orderItems;
    }

    public String getDestination() {
        return destination;
    }

    public OrderStatus getOrderStatus() {
        return orderStatus;
    }

    public Date getOrderDate() {
        return orderDate;
    }

    public void setOrderStatus(OrderStatus orderStatus) {
        this.orderStatus = orderStatus;
    }

    public void setOrderItems(List<OrderItemSL> orderItems) {
        this.orderItems = orderItems;
    }

    public void addOrderItem(OrderItemSL orderItem) {
        this.orderItems.add(orderItem);
    }

    public void removeOrderItem(OrderItemSL orderItem) {
        this.orderItems.remove(orderItem);
    }

}
