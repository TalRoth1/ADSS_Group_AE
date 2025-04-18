package Presentation;

import java.util.List;

import Utils.OrderStatus;

public class OrderPL
{
    private final int orderID;
    private final int supplierID;
    private List<OrderItemPL> orderItems;
    private OrderStatus orderStatus;

    
    OrderPL(int orderID, int supplierID, List<OrderItemPL> orderItems)
    {
        this.orderID = orderID;
        this.supplierID = supplierID;
        this.orderItems = orderItems;
        this.orderStatus = OrderStatus.IN_PROGRESS;
    }

    public int getOrderID()
    {
        return orderID;
    }

    public int getSupplierID()
    {
        return supplierID;
    }

    public List<OrderItemPL> getOrderItems()
{
        return orderItems;
    }

    public OrderStatus getOrderStatus()
    {
        return orderStatus;
    }

    public void setOrderStatus(OrderStatus orderStatus)
    {
        this.orderStatus = orderStatus;
    }

    public void setOrderItems(List<OrderItemPL> orderItems)
    {
        this.orderItems = orderItems;
    }
    public void addOrderItem(OrderItemPL orderItem)
    {
        this.orderItems.add(orderItem);
    }
    public void removeOrderItem(OrderItemPL orderItem)
    {
        this.orderItems.remove(orderItem);
    }

}
