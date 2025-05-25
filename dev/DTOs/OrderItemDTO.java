package DTOs;

public class OrderItemDTO {
    private int itemID;
    private int quantity;
    private int catalogID;
    private double totalPrice;

    public OrderItemDTO(int itemID, int quantity, int catalogID, double totalPrice) {
        this.itemID = itemID;
        this.quantity = quantity;
        this.catalogID = catalogID;
        this.totalPrice = totalPrice;
    }
    
}
