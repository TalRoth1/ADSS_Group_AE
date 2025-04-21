package Presentation;

import java.util.Date;
import java.util.List;

import Utils.OrderStatus;

public class OrderPL
{
    private final int orderID;
    private final int supplierID;
    private final Date orderDate;
    private final String destination;
    private List<OrderItemPL> orderItems;
    private OrderStatus orderStatus;

    
    OrderPL(int orderID, int supplierID, String destination, List<OrderItemPL> orderItems){
        this.orderID = orderID;
        this.supplierID = supplierID;
        this.orderDate = new Date(); // Set the order date to the current date
        this.destination = destination;
        this.orderItems = orderItems;
        this.orderStatus = OrderStatus.IN_PROGRESS;
    }
    OrderPL(int orderID, int supplierID, Date orderDate, String destination, List<OrderItemPL> orderItems, OrderStatus orderStatus){
        this.orderID = orderID;
        this.supplierID = supplierID;
        this.orderDate = orderDate;
        this.destination = destination;
        this.orderItems = orderItems;
        this.orderStatus = orderStatus;
    }

    public int getOrderID(){
        return orderID;
    }

    public int getSupplierID(){
        return supplierID;
    }

    public List<OrderItemPL> getOrderItems(){
        return orderItems;
    }

    public String getDestination(){
        return destination;
    }

    public OrderStatus getOrderStatus(){
        return orderStatus;
    }
    public Date getOrderDate(){
        return orderDate;
    }

    public void setOrderStatus(OrderStatus orderStatus){
        this.orderStatus = orderStatus;
    }

    public void setOrderItems(List<OrderItemPL> orderItems){
        this.orderItems = orderItems;
    }

    public void addOrderItem(OrderItemPL orderItem){
        this.orderItems.add(orderItem);
    }

    public void removeOrderItem(OrderItemPL orderItem){
        this.orderItems.remove(orderItem);
    }

}
