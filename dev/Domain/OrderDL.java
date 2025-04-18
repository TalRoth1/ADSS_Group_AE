package Domain;

import java.util.Date;
import java.util.List;

import Utils.OrderStatus;

public class OrderDL
{
    private final int orderID;
    private final int supplierID;
    private final Date orderDate;
    private List<OrderItemDL> orderItems;
    private OrderStatus orderStatus;

    public OrderDL(int orderID, int supplierID, List<OrderItemDL> orderItems)
    {
        this.orderID = orderID;
        this.supplierID = supplierID;
        this.orderDate = new Date(); // Set the order date to the current date
        this.orderItems = orderItems;
        this.orderStatus = OrderStatus.IN_PROGRESS;
    }
    public OrderDL(int orderID, int supplierID, Date orderDate, List<OrderItemDL> orderItems, OrderStatus orderStatus)
    {
        this.orderID = orderID;
        this.supplierID = supplierID;
        this.orderDate = orderDate;
        this.orderItems = orderItems;
        this.orderStatus = orderStatus;
    }

    public int getOrderID()
    {
        return orderID;
    }

    public int getSupplierID()
    {
        return supplierID;
    }

    public List<OrderItemDL> getOrderItems()
{
        return orderItems;
    }

    public OrderStatus getOrderStatus()
    {
        return orderStatus;
    }
    public Date getOrderDate()
    {
        return orderDate;
    }

    public void setOrderStatus(OrderStatus orderStatus)
    {
        this.orderStatus = orderStatus;
    }

    public void setOrderItems(List<OrderItemDL> orderItems)
    {
        this.orderItems = orderItems;
    }
    public void addOrderItem(OrderItemDL orderItem)
    {
        this.orderItems.add(orderItem);
    }
    public void removeOrderItem(OrderItemDL orderItem)
    {
        this.orderItems.remove(orderItem);
    }
}
