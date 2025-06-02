package DAL;

public class DiscountDAO
{
    private int catalogID;
    private int minimumQuantity;
    private double discountPercentage;

    public DiscountDAO(int catalogID, int minimumQuantity, double discountPercentage) {
        this.catalogID = catalogID;
        this.minimumQuantity = minimumQuantity;
        this.discountPercentage = discountPercentage;
    }

    public int getCatalogID() {
        return catalogID;
    }

    public int getMinimumQuantity() {
        return minimumQuantity;
    }

    public double getDiscountPercentage() {
        return discountPercentage;
    }

    public void setMinimumQuantity(int minimumQuantity) {
        this.minimumQuantity = minimumQuantity;
    }

    public void setDiscountPercentage(double discountPercentage) {
        this.discountPercentage = discountPercentage;
    }
}
