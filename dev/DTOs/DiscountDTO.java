package DTOs;

public class DiscountDTO {
    private int catalogID;
    private int minimumQuantity;
    private int discountPercentage;

    public DiscountDTO(int catalogID, int minimumQuantity, int discountPercentage) {
        this.catalogID = catalogID;
        this.minimumQuantity = minimumQuantity;
        this.discountPercentage = discountPercentage;
    }
    
}
