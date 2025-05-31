package DAL;

public class DiscountDAO
{
    private boolean isPersisted = false;
    private int catalogID;
    private int minimumQuantity;
    private int discountPercentage;
    private DiscountController discountController; 
    
    public DiscountDAO(int catalogID, int minimumQuantity, int discountPercentage, DiscountController discountController) {
        this.catalogID = catalogID;
        this.minimumQuantity = minimumQuantity;
        this.discountPercentage = discountPercentage;
        this.discountController = discountController;
    }

    public int getCatalogID() {
        return catalogID;
    }

    public int getMinimumQuantity() {
        return minimumQuantity;
    }

    public int getDiscountPercentage() {
        return discountPercentage;
    }

    public void setMinimumQuantity(int minimumQuantity) {
        if (isPersisted) {
            this.minimumQuantity = minimumQuantity;
            discountController.update(catalogID, "minimumQuantity", String.valueOf(minimumQuantity));
        }
    }
    
    public void setDiscountPercentage(int discountPercentage) {
        if (isPersisted) {
            this.discountPercentage = discountPercentage;
            discountController.update(catalogID, "discountPercentage", String.valueOf(discountPercentage));
        }
    }

    public void persist() {
        isPersisted = true;
        discountController.insert(this);
    }
}
