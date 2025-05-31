package DTOs;

public class OrderItemDTO {
    private final int itemID;
    private final int quantity;
    private final int catalogID;
    private final double totalPrice;

    public OrderItemDTO(int itemID, int quantity, int catalogID, double totalPrice)
    {
        this.itemID = itemID;
        this.quantity = quantity;
        this.catalogID = catalogID;
        this.totalPrice = totalPrice;
    }
    
    public int getItemID() {
        return itemID;
    }
    public int getQuantity() {
        return quantity;
    }
    public int getCatalogID() {
        return catalogID;
    }
    public double getTotalPrice() {
        return totalPrice;
    }
}
