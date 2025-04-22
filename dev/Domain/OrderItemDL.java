package Domain;

public class OrderItemDL 
{
    private int itemID;
    private int quantity;
    private int catalogID;
    private double totalPrice;
    
    public OrderItemDL(int itemID, int quantity, int catalogID, double totalPrice)
    {
        this.itemID = itemID;
        this.quantity = quantity;
        this.catalogID = catalogID;
        this.totalPrice = totalPrice;
    }

    public int getItemID()
    {
        return itemID;
    }

    public void setQuantity(int quantity)
    {
        this.quantity = quantity;
    }

    public int getQuantity()
    {
        return quantity;
    }

    public void setCatalogID(int catalogID)
    {
        this.catalogID = catalogID;
    }

    public int getCatalogID()
    {
        return catalogID;
    }

    public void setTotalPrice(double totalPrice)
    {
        this.totalPrice = totalPrice;
    }

    public double getTotalPrice()
    {
        return totalPrice;
    }    
}
