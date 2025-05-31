package DAL;

public class OrderItemDAO {
    private boolean isPersisted = false;
    private final int orderID;
    private final int itemID;
    private final int quantity;
    private final int catalogID;
    private final double totalPrice;
    private final OrderController orderController;

    public OrderItemDAO(int orderID, int itemID, int quantity, int catalogID, double totalPrice, OrderController orderController) {
        this.orderID = orderID;
        this.itemID = itemID;
        this.quantity = quantity;
        this.catalogID = catalogID;
        this.totalPrice = totalPrice;
        this.orderController = orderController;
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
    public int getOrderID() {
        return orderID;
    }
}
