package DAL;

public class ItemDAO
{
    private boolean isPersisted = false;
    private int itemID;
    private String itemName;
    private double itemPrice;

    public ItemDAO(int itemID, String itemName, double itemPrice) {
        this.itemID = itemID;
        this.itemName = itemName;
        this.itemPrice = itemPrice;
    }

    public int getItemID() {
        return itemID;
    }

    public String getItemName()
    {
        return itemName;
    }
    public void setItemName(String itemName)
    {
        if (isPersisted)
        {
            this.itemName = itemName;
        }
    }
    
    public double getItemPrice()
    {
        return itemPrice;
    }

    public void setItemPrice(double itemPrice)
    {
        if (isPersisted)
        {
            this.itemPrice = itemPrice;
        }
    }
}
