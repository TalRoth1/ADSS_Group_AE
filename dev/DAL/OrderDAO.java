package DAL;

import java.util.Date;

import DTOs.OrderDTO;
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

    public int getOrderID() {return orderID;}
    public int getSupplierID() {return supplierID;}
    public int getContractID() {return contractID;}
    public Date getOrderDate() {return orderDate;}
    public String getDestination() {return destination;}
    public OrderStatus getOrderStatus() {return orderStatus;}
    public void setOrderDate(Date orderDate) {this.orderDate = orderDate;}
    public void setDestination(String destination) {this.destination = destination;}
    public void setOrderStatus(OrderStatus orderStatus) {this.orderStatus = orderStatus;}
    public void editOrder(OrderDTO orderDTO) {
        orderController.updateOrder(orderID, "orderDate", orderDTO.getOrderDate().toString());
        this.orderDate = orderDTO.getOrderDate();
        this.destination = orderDTO.getDestination();
        this.orderStatus = orderDTO.getOrderStatus();
        this.isPersisted = true; // Mark as persisted after editing
    }
}
