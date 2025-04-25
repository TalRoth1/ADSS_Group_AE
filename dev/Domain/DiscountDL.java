package Domain;

public class DiscountDL
{
    private int catalogID;
    private int minimumQuantity;
    private int discountPercentage;
    
    public DiscountDL(int catalogID, int minimumQuantity, int discountPercentage)
    {
        this.catalogID = catalogID;
        this.minimumQuantity = minimumQuantity;
        this.discountPercentage = discountPercentage;
    }

    public int getCatalogID()
    {
        return catalogID;
    }

    public int getMinimumQuantity()
    {
        return minimumQuantity;
    }

    public void setMinimumQuantity(int minimumQuantity)
    {
        this.minimumQuantity = minimumQuantity;
    }

    public int getDiscountPercentage()
    {
        return discountPercentage;
    }

    public void setDiscountPercentage(int discountPercentage)
    {
        this.discountPercentage = discountPercentage;
    }
}
