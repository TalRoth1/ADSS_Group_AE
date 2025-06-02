package Domain;

public class DiscountDL
{
    private int catalogID;
    private int minimumQuantity;
    private double discountPercentage;

    public DiscountDL(int catalogID, int minimumQuantity, double discountPercentage)
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

    public double getDiscountPercentage()
    {
        return discountPercentage;
    }

    public void setDiscountPercentage(double discountPercentage)
    {
        this.discountPercentage = discountPercentage;
    }
}
