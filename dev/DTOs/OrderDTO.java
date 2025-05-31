package DTOs;

import java.util.Date;
import java.util.List;

import Utils.OrderStatus;

public class OrderDTO {
    private final int orderID;
    private final int supplierID;
    private final int contractID;
    private final Date orderDate;
    private final String destination;
    private final List<OrderItemDTO> orderItems;
    private final OrderStatus orderStatus;
    
    public OrderDTO(int orderID, int supplierID, int contractID, Date orderDate, String destination,
            List<OrderItemDTO> orderItems, OrderStatus orderStatus) {
        this.orderID = orderID;
        this.supplierID = supplierID;
        this.contractID = contractID;
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
    public int getContractID() {
        return contractID;
    }
    public Date getOrderDate() {
        return orderDate;
    }
    public String getDestination() {
        return destination;
    }
    public List<OrderItemDTO> getOrderItems() {
        return orderItems;
    }
    public OrderStatus getOrderStatus() {
        return orderStatus;
    }
}
