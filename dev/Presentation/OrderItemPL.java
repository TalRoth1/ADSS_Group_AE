package Presentation;

public class OrderItemPL
{
    private String name;
    private int itemID;
    private int quantity;
    private float price;
    
    OrderItemPL(String name, int itemID, int quantity, float price)
    {
        this.name = name;
        this.itemID = itemID;
        this.quantity = quantity;
        this.price = price;
    }

    public String getName()
    {
        return name;
    }

    public int getItemID()
    {
        return itemID;
    }

    public int getQuantity()
    {
        return quantity;
    }

    public float getPrice()
    {
        return price;
    }

    public void setQuantity(int quantity)
    {
        this.quantity = quantity;
    }

    public void setPrice(float price)
    {
        this.price = price;
    }
}
