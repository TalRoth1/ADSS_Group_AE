package DTOs;

import java.util.Date;
import java.util.List;

import Domain.OrderItemDL;
import Utils.OrderStatus;

public class OrderDTO {
    private final int orderID;
    private final int supplierID;
    private final int agreementID;
    private Date orderDate;
    private String destination;
    private List<OrderItemDL> orderItems;
    private OrderStatus orderStatus;
    
    public OrderDTO(int orderID, int supplierID, int agreementID, Date orderDate, String destination,
            List<OrderItemDL> orderItems, OrderStatus orderStatus) {
        this.orderID = orderID;
        this.supplierID = supplierID;
        this.agreementID = agreementID;
        this.orderDate = orderDate;
        this.destination = destination;
        this.orderItems = orderItems;
        this.orderStatus = orderStatus;
    }
}
