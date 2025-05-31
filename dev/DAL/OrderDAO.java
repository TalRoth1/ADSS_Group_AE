package DAL;

import java.util.Date;

import Utils.OrderStatus;

public class OrderDAO {

    private boolean isPersisted = false;
    private final int orderID;
    private final int supplierID;
    private final int contractID;
    private Date orderDate;
    private String destination;
    private OrderStatus orderStatus;
    private final OrderController orderController;

    public OrderDAO(int orderID, int supplierID, int contractID, Date orderDate, String destination, OrderStatus orderStatus, OrderController orderController) {
        this.orderID = orderID;
        this.supplierID = supplierID;
        this.contractID = contractID;
        this.orderDate = orderDate;
        this.destination = destination;
        this.orderStatus = orderStatus;
        this.orderController = orderController;
    }

    public int getOrderID() {
        return orderID;
    }
    public int getSupplierID() {
        return supplierID;
    }
    public int getContractID() {
        return contractID;
    }
    public Date getOrderDate() {
        return orderDate;
    }
    public String getDestination() {
        return destination;
    }
    public OrderStatus getOrderStatus() {
        return orderStatus;
    }
    public void setOrderDate(Date orderDate) {
        if (isPersisted) {
            this.orderDate = orderDate;
            orderController.updateOrder(orderID, "orderDate", orderDate.toString());
        }
    }
    public void setDestination(String destination) {
        if (isPersisted) {
            this.destination = destination;
            orderController.updateOrder(orderID, "destination", destination);
        }
    }
    public void setOrderStatus(OrderStatus orderStatus) {
        if (isPersisted) {
            this.orderStatus = orderStatus;
            orderController.updateOrder(orderID, "orderStatus", orderStatus.name());
        }
    }
    public void persist(){
        orderController.insertOrder(this);
        isPersisted = true;
    }
}
