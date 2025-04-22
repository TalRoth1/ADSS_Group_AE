package Domain;

import java.util.Date;
import java.util.List;

import Utils.OrderStatus;

public class OrderDL {
    private final int orderID;
    private final int supplierID;
    private int agreementID;
    private Date orderDate;
    private String destination;
    private List<OrderItemDL> orderItems;
    private OrderStatus orderStatus;

    public OrderDL(int orderID, int supplierID, int agreementID, String destination, List<OrderItemDL> orderItems) {
        this.orderID = orderID;
        this.supplierID = supplierID;
        this.agreementID = agreementID;
        this.orderDate = new Date(); // Set the order date to the current date
        this.destination = destination;
        this.orderItems = orderItems;
        this.orderStatus = OrderStatus.IN_PROGRESS;
    }

    public OrderDL(int orderID, int supplierID, int agreementID, Date orderDate, String destination,
            List<OrderItemDL> orderItems, OrderStatus orderStatus) {
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

    public Date getOrderDate() {
        return orderDate;
    }

    public String getDestination() {
        return destination;
    }

    public List<OrderItemDL> getOrderItems() {
        return orderItems;
    }

    public OrderStatus getOrderStatus() {
        return orderStatus;
    }

    public void setAgreementID(int agreementID) {
        this.agreementID = agreementID;
    }

    public void setDestination(String destination) {
        this.destination = destination;
    }

    public void setOrderDate(Date orderDate) {
        this.orderDate = orderDate;
    }

    public void setOrderStatus(OrderStatus orderStatus) {
        this.orderStatus = orderStatus;
    }

    public void setOrderItems(List<OrderItemDL> orderItems) {
        this.orderItems = orderItems;
    }

    public void addOrderItem(OrderItemDL orderItem) {
        this.orderItems.add(orderItem);
    }

    public void removeOrderItem(OrderItemDL orderItem) {
        this.orderItems.remove(orderItem);
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Order ID: ").append(orderID).append("\n")
                .append("Supplier ID: ").append(supplierID).append("\n")
                .append("Order Date: ").append(orderDate).append("\n")
                .append("Destination: ").append(destination).append("\n")
                .append("Order Items: \n");
        for (OrderItemDL item : orderItems) {
            sb.append(item.toString()).append("\n");
        }
        sb.append("Order Status: ").append(orderStatus).append("\n");
        return sb.toString();
    }
}
