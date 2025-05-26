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

    public OrderDAO(int orderID, int supplierID, int contractID, Date orderDate, String destination, OrderStatus orderStatus) {
        this.orderID = orderID;
        this.supplierID = supplierID;
        this.contractID = contractID;
        this.orderDate = orderDate;
        this.destination = destination;
        this.orderStatus = orderStatus; // Default status
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
        this.orderDate = orderDate;
    }
    public void setDestination(String destination) {
        this.destination = destination;
    }
    public void setOrderStatus(OrderStatus orderStatus) {
        this.orderStatus = orderStatus;
    }
}
