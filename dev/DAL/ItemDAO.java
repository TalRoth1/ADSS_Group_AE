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

    private int getItemID() {
        return itemID;
    }

    private String getItemName()
    {
        return itemName;
    }
    private void setItemName(String itemName)
    {
        if (isPersisted)
        {
            this.itemName = itemName;
        }
    }
    
    private double getItemPrice()
    {
        return itemPrice;
    }

    private void setItemPrice(double itemPrice)
    {
        if (isPersisted)
        {
            this.itemPrice = itemPrice;
        }
    }
}
