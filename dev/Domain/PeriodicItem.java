package Domain;

public class PeriodicItem
{
    private int itemID;
    private int quantity;
    private double price;

    public PeriodicItem(int itemID, int quantity, double price)
    {
        this.itemID = itemID;
        this.quantity = quantity;
        this.price = price;
    }

    public int getItemID()
    {
        return itemID;
    }

    public int getQuantity()
    {
        return quantity;
    }

    public double getPrice()
    {
        return price;
    }

    public void setQuantity(int quantity)
    {
        this.quantity = quantity;
    }

    public void setPrice(double price)
    {
        this.price = price;
    }
}
