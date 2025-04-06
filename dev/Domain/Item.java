package Domain;

public class Item 
{
    private int itemID;
    private String name;
    private int price;
    
    public Item(int itemID, String name, int price)
    {
        this.itemID = itemID;
        this.name = name;
        this.price = price;
    }

    public int getItemID()
    {
        return itemID;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public String getName()
    {
        return name;
    }

    public void setPrice(int price)
    {
        this.price = price;
    }

    public int getPrice()
    {
        return price;
    }
}
