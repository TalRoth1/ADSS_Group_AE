package Presentation;

import java.util.List;

public class OrderPL
{
    private int orderID;
    private int supplierID;
    private List<OrderItemPL> orderItems;
    
    OrderPL(int orderID, int supplierID, List<OrderItemPL> orderItems)
    {
        this.orderID = orderID;
        this.supplierID = supplierID;
        this.orderItems = orderItems;
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
