package Service;

import java.util.List;

import Utils.OrderStatus;

public class OrderSL
{
    private final int orderID;
    private final int supplierID;
    private List<OrderItemSL> orderItems;
    private OrderStatus orderStatus;

    
    OrderSL(int orderID, int supplierID, List<OrderItemSL> orderItems)
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

    public List<OrderItemSL> getOrderItems()
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

    public void setOrderItems(List<OrderItemSL> orderItems)
    {
        this.orderItems = orderItems;
    }
    public void addOrderItem(OrderItemSL orderItem)
    {
        this.orderItems.add(orderItem);
    }
    public void removeOrderItem(OrderItemSL orderItem)
    {
        this.orderItems.remove(orderItem);
    }

}
