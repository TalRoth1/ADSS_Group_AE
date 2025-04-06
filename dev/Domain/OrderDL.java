package Domain;

import java.util.List;

public class OrderDL
{
    private int orderID;
    private int supplierID;
    private List<OrderItemDL> items;

    public OrderDL(int orderID, int supplierID, List<OrderItemDL> items)
    {
        this.orderID = orderID;
        this.supplierID = supplierID;
        this.items = items;
    }

    public int getOrderID()
    {
        return orderID;
    }

    public int getSupplierID()
    {
        return supplierID;
    }

    public List<OrderItemDL> getItems()
    {
        return items;
    }

    public void setItems(List<OrderItemDL> items)
    {
        this.items = items;
    }
}
