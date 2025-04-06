package Domain;

public class OrderItemDL 
{
    private int itemID;
    private int quantity;
    private int catalogID;
    private int totalPrice;
    
    public OrderItemDL(int orderID, int itemID, int quantity, int catalogID, int totalPrice)
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

    public void settotalPrice(int totalPrice)
    {
        this.totalPrice = totalPrice;
    }

    public int gettotalPrice()
    {
        return totalPrice;
    }    
}
